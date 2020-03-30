package Server;

import Util.Util;
import Util.Account;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class TransactionServer
{
    private static ServerSocket serverSocket;
    public static TransactionManager transactionManager;
    public static AccountManager accountManager;

    public TransactionServer()
    {
        transactionManager = new TransactionManager();
        accountManager = new AccountManager();
        serverSocket = null;
    }

    public static void main(String[] args)
    {
        new TransactionServer();
        displayAccounts();

        listenNewTransactions();

//        System.out.println(logger.toString());
        displayAccounts();
    }

    public static void listenNewTransactions()
    {
        try
        {
            // Opens the server on localhost
            serverSocket = new ServerSocket(Util.port);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        int nbTransactionsOpened = 0;
        if (null != serverSocket)
        {
            while (nbTransactionsOpened < Util.nbTransactions)
            {
                Socket clientSocket = null;
                try
                {
                    clientSocket = serverSocket.accept();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

                if (null != clientSocket)
                {
                    // Start a new transaction
                    transactionManager.newTransaction(clientSocket);
                    nbTransactionsOpened++;
                }
            }
        }
    }

    public static void displayAccounts()
    {
        long cumulativeAmount = 0;
        for(Account account : accountManager.getAccountList())
        {
            System.out.println(account.toString());
            cumulativeAmount += account.getAmount();
        }
        System.out.println("Cumulative amount on all accounts = " + cumulativeAmount);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
