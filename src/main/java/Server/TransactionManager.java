package Server;

import Transaction.Transaction;
import Util.Account;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionManager
{
    private ConcurrentLinkedQueue<Integer> writeSet;     // Write set for backward validation, containing the account numbers
    private AtomicInteger transactionId;                 // Used to attribute an id to transactions during validation

    public TransactionManager()
    {
        this.writeSet = new ConcurrentLinkedQueue<>();
        this.transactionId = new AtomicInteger(0);
    }

    public void newTransaction(Socket clientSocket)
    {
        //TODO Start a new TransactionWorker that listens for read/write operations from the socket
    }

    public Account readTransaction(int accountNumber)
    {
//        return new Account(TransactionServer.getAccountList().get(accountNumber));
        return null;
    }

    public void writeTransaction(int accountNumber, int amount)
    {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
