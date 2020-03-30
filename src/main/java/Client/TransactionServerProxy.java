package Client;

import java.net.Socket;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class TransactionServerProxy implements Runnable
{
    private Socket socketToServer;
    private Thread serverProxyThread;

    public TransactionServerProxy(Socket socketToServer)
    {
        //TODO Constructor
    }

    public void start()
    {
        //TODO Create the thread
    }

    @Override
    public void run()
    {
        //TODO Create the input and output streams
        // Create random values for account 1, account 2 and the transfer amount
        // Break down the transfer into opening a transaction, read and write operations, and closing the transaction
        // Eventually create these functions, and cf. MessageType class to see what to send
        // E.g.: Message openTrans = new Message(MessageType.START_TRANSACTION, 0)
        // Include either the account number or the amount for the other steps of the transaction
        // Listen for the result of each request (e.g.: READ_RESULT or WRITE_RESULT)
        // When the transaction is done, leave
    }
}
