package Server;

import Util.Account;
import Util.Util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class TransactionServer
{
    private static ServerSocket serverSocket;
    private static TransactionManager transactionManager;
    private static ArrayList<Account> accountList;

    public TransactionServer()
    {
        accountList = (ArrayList<Account>) Util.initializeAccounts();
        transactionManager = new TransactionManager();
        serverSocket = null;
    }

    public static void main(String[] args)
    {
        new TransactionServer();
        listenNewTransactions();
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

        if (null != serverSocket)
        {
            while (true)
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
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static ArrayList<Account> getAccountList()
    {
        return accountList;
    }
}
