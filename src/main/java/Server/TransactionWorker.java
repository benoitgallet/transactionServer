package Server;

import Message.Message;
import Message.MessageType;
import Util.Account;
import Util.Pair;
import Util.Util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class TransactionWorker implements Runnable
{
    private Thread thread;                      // Worker thread for concurrency
    private StringBuilder logger;               // StringBuilder to act as a logger for the transaction

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private LinkedList<Account> readSet;        // Keep track of the accounts read by the transaction
    private LinkedList<Account> writeSet;       // Keep track of the accounts written by the transaction
    private int currentTransactionId;           // Id of the active transaction, especially used during validation

    public TransactionWorker(ObjectInputStream inputStream, ObjectOutputStream outputStream)
    {
        this.thread = null;
        logger = new StringBuilder();
        this.inputStream = inputStream;
        this.outputStream = outputStream;

        this.readSet = new LinkedList<>();
        this.writeSet = new LinkedList<>();
    }

    public void start()
    {
        // Create and start a new thread
        if (null == this.thread)
        {
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    /**
     * Main function that listens on the socket for simple operations from the client.
     */
    @Override
    public void run()
    {
        // Assert that we can communicate with the client
        if (null == inputStream || null == outputStream)
        {
            System.out.println("Error creating the streams");
            return;
        }

        boolean listenOperation = true;

        // While the client does not close the transaction
        while(listenOperation)
        {
            Message inputMessage = null;
            try
            {
                inputMessage = (Message) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e)
            {
//                e.printStackTrace();
            }

            if (null != inputMessage)
            {
                // Switch over the different MessageType that we can read from the client
                switch (inputMessage.getMessageType())
                {
                    // The client is starting a new transaction
                    case START_TRANSACTION:
                    {
                        // The transaction becomes active, and so is assigned a unique id
                        currentTransactionId = TransactionManager.transactionId.getAndAdd(1);
                        break;
                    }
                    // The client wants to read an account
                    case READ_REQUEST:
                    {
                        // The client sends which account they want to read
                        int readAccountNumber = inputMessage.getValue();
                        Account readAccount = read(readAccountNumber);

                        // Write to the client the amount of the account that was just read
                        Message outputMessage = new Message(MessageType.READ_RESULT, readAccount.getAmount());
                        try
                        {
                            outputStream.writeObject(outputMessage);
                            outputStream.flush();
                        } catch (IOException e)
                        {
//                            e.printStackTrace();
                        }
                        break;
                    }
                    // The client wants to write a new amount on an account
                    // We consider that the write operations concern the last Account that was read
                    case WRITE_REQUEST:
                    {
                        // Read the transfer amount from the client
                        int writeAmount = inputMessage.getValue();
                        Account writtenAccount = write(writeAmount);

                        // Write to the client the new amount of the last account that was read
                        Message outputMessage = new Message(MessageType.WRITE_RESULT, writtenAccount.getAmount());
                        try
                        {
                            outputStream.writeObject(outputMessage);
                            outputStream.flush();
                        } catch (IOException e)
                        {
//                            e.printStackTrace();
                        }
                        break;
                    }
                    // The client wants to disconnect from the server, so we have to validate the transaction
                    case CLOSE_TRANSACTION:
                    {
                        Message outputMessage;
                        // If the server uses optimistic concurrency control to handle concurrent transactions
                        if (Util.optimisticLocking)
                        {
                            if (validateTransactions())
                            {
                                // Commit the transaction
                                commitTransaction();
                                logger.append("[Transaction ").append(currentTransactionId).append("] has committed its changes\n");

                                // Confirm to the client that the transaction has been validated
                                outputMessage = new Message(MessageType.FINISH_TRANSACTION, 0);
                                listenOperation = false;
                                // Prints the result of this transaction
                                System.out.println(logger.toString());
                                TransactionServer.displayAccounts(false);
                            } else {
//                                TransactionManager.transactionsToAbort.remove(currentTransactionId);
                                TransactionServer.transactionManager.removeTransactions(currentTransactionId);
                                // The transaction is not validated and is aborted
                                // We keep the connection with the client open so they can start the transaction again
                                logger.append("[Transaction ").append(currentTransactionId).append("] aborted its changes\n");
                                // Clear the read and write sets so the transaction can restart correctly
                                readSet.clear();
                                writeSet.clear();

                                // Inform the client that the transaction has been aborted so they can start it again
                                outputMessage = new Message(MessageType.ABORT_TRANSACTION, 1);
                            }
                        } else { // Not using optimistic concurrency control
                            // All the transactions are committed without any protection whatsoever
                            commitTransaction();
                            logger.append("[Transaction ").append(currentTransactionId).append("] has committed its changes without control\n");

                            // Inform the client that the transactions has been committed (or at least attempted to be, as there is no control)
                            // Prints the result of this transaction
                            outputMessage = new Message(MessageType.FINISH_TRANSACTION, 0);
                            listenOperation = false;
                            System.out.println(logger.toString());
                            TransactionServer.displayAccounts(false);
                        }

                        // Effectively inform the client if the transaction was committed or aborted
                        try
                        {
                            outputStream.writeObject(outputMessage);
                            outputStream.flush();
                        } catch (IOException e)
                        {
//                            e.printStackTrace();
                        }

                        // Close the different streams as well as the socket to the client
                        try
                        {
                            outputStream.close();
                        } catch (IOException e)
                        {
//                            e.printStackTrace();
                        }
                        try
                        {
                            inputStream.close();
                        } catch (IOException e)
                        {
//                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Read operation to read the amount of a specific account.
     * @param readAccountNumber The account id whose the client wants to know the amount.
     * @return Return the account that was read.
     */
    public Account read(int readAccountNumber)
    {
        // Read the account from the AccountManager, that returns a copy of this account so the transaction works on tentative data
        Account readAccount = TransactionServer.accountManager.read(readAccountNumber);
        // Add this account to the transaction's read set
        readSet.addLast(readAccount);
        // Log this operation
        logger.append("[Transaction ").append(currentTransactionId).append("] reads account ").append(readAccountNumber).append('\n');
        TransactionManager.readSet.add(new Pair<>(this.currentTransactionId, readAccountNumber));
        return readAccount;
    }

    /**
     * Updates the amount of an account by a given value. Note that the account that is written is the same as the account
     * that was previously read, and should thus happen after receiving a `read' operation.
     * @param writeAmount The value to add/substract to the previously read account's amount.
     * @return The account with its updated amount.
     */
    public Account write(int writeAmount)
    {
        // Write on the last account that was read
        Account writeAccount = readSet.getLast();
        // Update its amount
        writeAccount.setAmount(writeAccount.getAmount() + writeAmount);
        // Add it to the write set of this transaction
        writeSet.addLast(writeAccount);
        logger.append("[Transaction ").append(currentTransactionId).append("] writes ").append(writeAmount).append(" on account ").append(writeAccount.getNumber()).append('\n');
        return writeAccount;
    }

    /**
     * Ask the AccountManager to write into memory the write set of this transaction (i.e., the accounts with their
     * updated amount).
     */
    public void commitTransaction()
    {
        TransactionServer.accountManager.write(writeSet);
    }

    /**
     * Operates a forward validation to determine if the current transaction can be committed or not.
     * @return True if the transaction can be validated, false otherwise.
     */
    public synchronized boolean validateTransactions()
    {
        // Iterate over the global read set and check if there is a conflict
        Iterator<Pair<Integer, Integer>> it = TransactionManager.readSet.iterator();
        while(it.hasNext())
        {
            Pair<Integer, Integer> transaction = it.next();
            for (Account account : writeSet)
            {
                if (transaction.first() != currentTransactionId && transaction.second() == account.getNumber())
                {
                    // Another transaction overlaps, so abort this one
                    return false;
                }
            }
        }

        // Remove this transaction from the global read set
        TransactionServer.transactionManager.removeTransactions(currentTransactionId);
        return true;
    }
}
