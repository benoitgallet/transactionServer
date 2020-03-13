package Transaction;


//TODO might delete these classes
public class Transaction
{
    private int id;
    private int account1;

    public Transaction()
    {

    }

    public Transaction(int id, int account1)
    {
        this.id = id;
        this.account1 = account1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getAccount1()
    {
        return account1;
    }

    public void setAccount1(int account1)
    {
        this.account1 = account1;
    }
}
