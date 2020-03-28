package Server;

import Message.Message;
import Message.MessageType;
import Util.Account;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class TransactionWorker implements Runnable
{
    private Socket clientSocket;
    private Thread thread;

    private LinkedList<Account> readSet;      // Keep track of the accounts read by the transaction
    private LinkedList<Account> writeSet;     // Keep track of the accounts written by the transaction
    private int currentTransactionId;

    public TransactionWorker(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        this.thread = null;

        this.readSet = new LinkedList<>();
        this.writeSet = new LinkedList<>();
    }

    public void start()
    {
        if (null == this.thread)
        {
            this.thread = new Thread(this, "thread");
        }
        this.thread.start();
    }

    @Override
    public void run()
    {
        ObjectInputStream inputStream = null;
        try
        {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        ObjectOutputStream outputStream = null;
        try
        {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        if (null == inputStream || null == outputStream)
        {
            System.out.println("Error creating the streams");
            try
            {
                clientSocket.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return;
        }

        boolean listenOperation = true;

        while(listenOperation)
        {
            Message inputMessage = null;
            try
            {
                inputMessage = (Message) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }

            if (null != inputMessage)
            {
                switch (inputMessage.getMessageType())
                {
                    case START_TRANSACTION:
                    {
                        currentTransactionId = TransactionManager.transactionId.getAndAdd(1);
                        break;
                    }
                    case READ_REQUEST:
                    {
                        int readAccountNumber = inputMessage.getValue();
                        Account readAccount = new Account(AccountManager.accountList.get(readAccountNumber));
                        readSet.addLast(readAccount);
                        Message outputMessage = new Message(MessageType.READ_RESULT, readAccount.getAmount());
                        try
                        {
                            outputStream.writeObject(outputMessage);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case WRITE_REQUEST: // We consider that the write operations concern the last Account that was read
                    {
                        int writeAmount = inputMessage.getValue();
                        Account writeAccount = readSet.getLast();
                        writeAccount.setAmount(writeAccount.getAmount() + writeAmount);
                        writeSet.addLast(writeAccount);
                        Message outputMessage = new Message(MessageType.WRITE_RESULT, writeAccount.getAmount());
                        try
                        {
                            outputStream.writeObject(outputMessage);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case FINISH_TRANSACTION: // The client wants to validate the pending transaction
                    {
                        //TODO Write the condition where we don't use optimistic concurrency control and just update the accounts
                        Message outputMessage;
                        if (validateTransactions())
                        {
                            // The transactions are validated and committed
                            outputMessage = new Message(MessageType.FINISH_TRANSACTION, 0);
                        } else {
                            // The transactions are not validated and are aborted
                            // We keep the connection with the client open so he can start the transactions again
                            outputMessage = new Message(MessageType.ABORT_TRANSACTION, 1);
                        }
                        try
                        {
                            outputStream.writeObject(outputMessage);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case CLOSE_TRANSACTION: // The client wants to disconnect from the server
                    {
                        listenOperation = false;
                        try
                        {
                            inputStream.close();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        try
                        {
                            outputStream.close();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        try
                        {
                            clientSocket.close();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }

    public boolean validateTransactions()
    {
        //TODO Backward validation
        return true;
    }
}
