package Client;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class TransactionServerProxy
{
    private int account1;
    private int amount;
    private int account2;

    public TransactionServerProxy(int account1, int amount, int account2)
    {
        this.account1 = account1;
        this.amount = amount;
        this.account2 = account2;
    }

    public void performaTransaction()
    {
        //TODO Connect to the server, and perform a transaction with it through a socket, using read/write operations
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
