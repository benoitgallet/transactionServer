package Server;

import Util.Util;

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


}
