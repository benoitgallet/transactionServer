package Client;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import Util.*;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class TransactionClient
{
    public static void main(String[] args)
    {
    	Util Util = new Util();
        //TODO In a for loop iterating over Util.nbTransactions, create at each iteration a Socket
        // Establish a connection with the server using the Socket (see Util class for the port)
        // Create a new TransactionServerProxy thread with the Socket connected to the server
    	
    	
       
    	for (int i = 0; i < Util.nbTransactions ; i++) {
    		
    		try {
    			
    			Properties prop = readPropertiesFile("TransactionServer.properties");
    			
    			Socket socketToServer = new Socket(prop.getProperty("SERVER_HOST"),Util.port);
    			
				new Thread(new TransactionServerProxy(socketToServer)).start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
    }
    
    
    public static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream fis = null;
        Properties prop = null;
        try {
           fis = new FileInputStream(fileName);
           prop = new Properties();
           prop.load(fis);
        } catch(FileNotFoundException fnfe) {
           fnfe.printStackTrace();
        } catch(IOException ioe) {
           ioe.printStackTrace();
        } finally {
           fis.close();
        }
        return prop;
     }
}
