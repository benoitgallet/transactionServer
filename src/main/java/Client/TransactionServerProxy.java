package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Util.Util;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class TransactionServerProxy implements Runnable
{
    private Socket socketToServer;
    private Thread serverProxyThread;
    Util Util = new Util();

    public TransactionServerProxy(Socket socketToServer)
    {
        this.socketToServer = socketToServer;
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
    	
    	try {
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(socketToServer.getInputStream()));
			PrintWriter outputStream = new PrintWriter(socketToServer.getOutputStream(), true);
			
			int account1 = (int) Math.floor(Math.random() * Util.nbAccount);
			int account2 = (int) Math.floor(Math.random() * Util.nbAccount);
			int amount = (int) Math.ceil(Math.random() * Util.nbInitialAmount);
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    }
}
