package Server;

import Util.Account;
import Util.Util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
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
        displayAccounts(true);

        listenNewTransactions();
    }

    public static void listenNewTransactions()
    {
        try
        {
            // Opens the server on localhost
            serverSocket = new ServerSocket(Util.port, Util.nbTransactions, InetAddress.getByName(Util.serverAddress));
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
                    // Accept new connections from clients
                    clientSocket = serverSocket.accept();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

                if (null != clientSocket)
                {
                    // Start a new transaction
                    try
                    {
                        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                        ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                        transactionManager.newTransaction(inputStream, outputStream);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    nbTransactionsOpened++;
                }
            }
        }
    }

    /**
     * Function to display the current state of all the accounts, as well as the cumulative amount of all the accounts.
     * The detail of each account is optional.
     * @param verbose True to display the detail of each account, false otherwise.
     */
    public static void displayAccounts(boolean verbose)
    {
        long cumulativeAmount = 0;
        for(Account account : accountManager.getAccountList())
        {
            if (verbose)
            {
                System.out.println(account.toString());
            }
            cumulativeAmount += account.getAmount();
        }
        System.out.println("Cumulative amount on all accounts = " + cumulativeAmount);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
