package Util;

public class Account
{
    private int number;
    private int amount;

    public Account()
    {

    }

    /**
     * Constructor to make a copy of an account and work on tentative data.
     * @param account The account to copy.
     */
    public Account(Account account)
    {
        this.number = account.number;
        this.amount = account.amount;
    }

    public Account(int number, int amount)
    {
        this.number = number;
        this.amount = amount;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (number != account.number) return false;
        return amount == account.amount;
    }

    @Override
    public String toString()
    {
        return "Account{" + "number=" + number + ", amount=" + amount + '}';
    }
}
