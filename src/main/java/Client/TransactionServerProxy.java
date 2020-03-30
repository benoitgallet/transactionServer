package Client;

import Message.Message;
import Message.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class TransactionServerProxy implements Runnable
{
    private Thread serverProxyThread;
    private StringBuilder logger;                   // Use a StringBuilder to log the operations with the server
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private int account1;                           // Account to withdraw money from
    private int transferAmount;                     // Amount of money to withdraw
    private int account2;                           // Account to credit money to
    private int transactionNumber;                  // Number of the transaction

    public TransactionServerProxy(ObjectInputStream inputStream, ObjectOutputStream outputStream, int account1, int transferAmount, int account2, int transactionNumber)
    {
        this.serverProxyThread = null;
        this.logger = new StringBuilder();

        this.inputStream = inputStream;
        this.outputStream = outputStream;

        this.account1 = account1;
        this.transferAmount = transferAmount;
        this.account2 = account2;
        this.transactionNumber = transactionNumber;
    }

    public void start()
    {
        if (null == serverProxyThread)
        {
            this.serverProxyThread = new Thread(this);
            this.serverProxyThread.start();
        }
    }

    @Override
    public void run()
    {

        // Assert that we can communicate with the server correctly
        if (null == inputStream || null == outputStream)
        {
            System.out.println("Could not create the streams to the server");
            return;
        }

        boolean transactionCommitted = false;

        // Create variables related to the transaction
        int readAccount1 = 0;
        int writeAccount1 = 0;
        int readAccount2 = 0;
        int writeAccount2 = 0;

        // While the transaction has not been committed by the server, we reiterate the same transaction
        // This loop thus handles the potential abortions
        while (!transactionCommitted)
        {
            open();
            readAccount1 = read(account1);
            writeAccount1 = write(transferAmount * -1);
            readAccount2 = read(account2);
            writeAccount2 = write(transferAmount);
            transactionCommitted = close();
        }

        // Close the different streams to the server
        try
        {
            outputStream.close();
        } catch (IOException e)
        {
//            e.printStackTrace();
        }
        try
        {
            inputStream.close();
        } catch (IOException e)
        {
//            e.printStackTrace();
        }

        // Prompt the result of the transaction
        logger.append("\nTransaction ").append(transactionNumber).append(" transfering ").append(transferAmount)
                .append(" from Account ").append(account1).append(" to Account ").append(account2).append('\n');
        logger.append("Transaction ").append(transactionNumber).append(", Account ").append(account1).append(": ")
                .append(readAccount1).append(" -> ").append(writeAccount1).append('\n');
        logger.append("Transaction ").append(transactionNumber).append(", Account ").append(account2).append(": ")
                .append(readAccount2).append(" -> ").append(writeAccount2);

        System.out.println(logger.toString());

    }

    /**
     * Function to open a new transaction with the server.
     */
    public void open()
    {
        // Indicate to the server that the client is ready to start a transaction
        Message openMessage = new Message(MessageType.START_TRANSACTION, 0);
        try
        {
            outputStream.writeObject(openMessage);
            outputStream.flush();
        } catch (IOException e)
        {
//                e.printStackTrace();
        }
    }

    /**
     * Function to indicate to the server that the transaction is over.
     * @return True if the transaction has been committed by the server, false otherwise (i.e., if the transaction has been
     * aborted by the server).
     */
    public boolean close()
    {
        // Indicate to the server that the transaction is done
        Message closeMessage = new Message(MessageType.CLOSE_TRANSACTION, 0);
        try
        {
            outputStream.writeObject(closeMessage);
            outputStream.flush();
        } catch (IOException e)
        {
//                e.printStackTrace();
        }

        // Get the result from the server
        Message closeResult = null;
        try
        {
            closeResult = (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
//                e.printStackTrace();
        }

        if (null != closeResult)
        {
            // If the transaction has been committed, then we return true and this thread can stop its execution
            if (MessageType.FINISH_TRANSACTION == closeResult.getMessageType())
            {
                return true;
            }
        }

        // The transaction has not been committed by the server, so we will reiterate the same transaction.
        // We set a random timer to delay this new transaction from the other transactions running on the server, inspired
        // by the several strategies from the book on resolving the transaction conflicts.
        try
        {
            Thread.sleep(new Random().nextInt(50) + 1);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Read function that performs the read part of a transaction.
     * @param accountNumber The account number to read.
     * @return The amount currently available on the read account.
     */
    public int read(int accountNumber)
    {
        // Indicate to the server that the operation is a read
        Message readMessage = new Message(MessageType.READ_REQUEST, accountNumber);
        try
        {
            outputStream.writeObject(readMessage);
            outputStream.flush();
        } catch (IOException e)
        {
//            e.printStackTrace();
        }

        // Read the result from the server
        Message readResult = null;
        try
        {
            readResult = (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
//            e.printStackTrace();
        }

        if (null != readResult)
        {
            // Return the amount available on this account
            return readResult.getValue();
        } else {
            // Return an error value otherwise
            return -1;
        }
    }

    /**
     * Function to update the amount of an account. Note that we consider that the write operation always concern the
     * account that was previously read following a `read' operation.
     * @param amount The value to update the last read account's amount.
     * @return The updated amount of the last read account.
     */
    public int write(int amount)
    {
        // Indicate the server that the client would like to update the amount of the last read account
        Message writeMessage = new Message(MessageType.WRITE_REQUEST, amount);
        try
        {
            outputStream.writeObject(writeMessage);
            outputStream.flush();
        } catch (IOException e)
        {
//            e.printStackTrace();
        }

        // Read the result from the server
        Message writeResult = null;
        try
        {
            writeResult = (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
//            e.printStackTrace();
        }

        if (null != writeResult)
        {
            // Return the updated amount of the last read account
            return writeResult.getValue();
        } else {
            // Return an error value otherwise
            return -1;
        }
    }
}
