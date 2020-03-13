package Transaction;

public class WriteTransaction extends Transaction
{
    private int account2;
    private int amount;

    public WriteTransaction()
    {

    }

    public WriteTransaction(int id, int account1, int amount, int account2)
    {
        super(id, account1);
        this.account2 = account2;
        this.amount = amount;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getAccount2()
    {
        return account2;
    }

    public void setAccount2(int account2)
    {
        this.account2 = account2;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }
}
