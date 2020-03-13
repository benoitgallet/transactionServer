package Server;

import java.net.Socket;

public class TransactionWorker implements Runnable
{
    private Socket clientSocket;
    private Thread thread;

    public void TransactionWorker(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        this.thread = null;
    }

    public void start()
    {
        if (null == this.thread)
        {
            this.thread = new Thread(this, "thread");
        }
        this.thread.start();
    }

    @Override
    public void run()
    {
        //TODO Listen on the socket for read/write operations on accounts
        //TODO Consider the scenarios where we are using Optimistic Concurrency Control and no control at all
    }
}
