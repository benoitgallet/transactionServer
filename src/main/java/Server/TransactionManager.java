package Server;

import Util.Pair;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class TransactionManager
{
    // As stated in the book, these operations require to be atomic
    public static ConcurrentLinkedQueue<Pair<Integer, Integer>> readSet;        // Read set for forward validation, containing the transaction ids and the account numbers
    public static AtomicInteger transactionId;                                  // Assign a unique id to the transactions

    public TransactionManager()
    {
        readSet = new ConcurrentLinkedQueue<>();
        transactionId = new AtomicInteger(0);
    }

    public void newTransaction(ObjectInputStream inputStream, ObjectOutputStream outputStream)
    {
        // Create a new worker thread to perform a transaction
        TransactionWorker transactionWorker = new TransactionWorker(inputStream, outputStream);
        transactionWorker.start();
    }

    /**
     * Remove the read operations of transactions that are validated.
     * @param transactionId The transaction's id whose read operations should be removed.
     */
    public void removeTransactions(int transactionId)
    {
        for(Pair<Integer, Integer> transaction : readSet)
        {
            if (transaction.first() == transactionId)
            {
                readSet.remove(transaction);
            }
        }
    }
}
