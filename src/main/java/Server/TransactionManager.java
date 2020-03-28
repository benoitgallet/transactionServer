package Server;

import Util.Pair;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class TransactionManager
{
    // As stated in the book, these operations require to be atomic
    public static ConcurrentLinkedQueue<Pair<Integer, Integer>> writeSet;     // Write set for backward validation, containing the transaction ids and the account numbers
    public static AtomicInteger transactionValidationId;                      // Used to attribute an id to transactions during validation
    public static AtomicInteger transactionId;                                // Assign a unique id to the transactions

    public TransactionManager()
    {
        writeSet = new ConcurrentLinkedQueue<>();
        transactionValidationId = new AtomicInteger(0);
        transactionId = new AtomicInteger(0);
    }

    public void newTransaction(Socket clientSocket)
    {
        //TODO Start a new TransactionWorker that listens for read/write operations from the socket
        new TransactionWorker(clientSocket).start();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
