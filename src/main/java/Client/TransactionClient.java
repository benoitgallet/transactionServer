package Client;

import Util.Util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class TransactionClient
{
    public static void main(String[] args)
    {
        Random rand = new Random();
        // Create Util.nbTransactions transactions, and thus as many threads
    	for (int i = 0; i < Util.nbTransactions ; i++)
    	{
    	    // Use random values for the account numbers and the transfer amount
    	    int account1 = rand.nextInt(Util.nbAccount);
    	    int transferAmount = rand.nextInt(Util.initialAccountAmount) + 1;
    	    int account2;
    	    do {
    	        // Check that the two accounts are distinct (avoid transferring from an account to the same account)
                account2 = rand.nextInt(Util.nbAccount);
            } while(account1 == account2);

//            System.out.println("Creating transaction " + i + ": transfering " + transferAmount + " between Accounts " + account1 + " and " + account2);

    	    Socket socketToServer = null;
    		try
            {
                // Connect to the server
    			socketToServer = new Socket(InetAddress.getByName(Util.serverAddress), Util.port);
			} catch (IOException e) {
				e.printStackTrace();
			}

    		if (null != socketToServer)
            {
                try
                {
                    // Create a new client thread that will perform a transaction
                    ObjectOutputStream outputStream = new ObjectOutputStream(socketToServer.getOutputStream());
                    ObjectInputStream inputStream = new ObjectInputStream(socketToServer.getInputStream());
                    TransactionServerProxy serverProxy = new TransactionServerProxy(inputStream, outputStream, account1, transferAmount, account2, i);
                    serverProxy.start();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Cannot connect to the server");
            }
        }
        System.out.println("All the transactions have been launched");
    }
}
