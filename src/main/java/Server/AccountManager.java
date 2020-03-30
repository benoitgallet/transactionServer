package Server;

import Util.Account;
import Util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class AccountManager
{
    public static ArrayList<Account> accountList;

    /**
     * Constructor that initialize the list of accounts following the function defined in Util.
     */
    public AccountManager()
    {
        accountList = (ArrayList<Account>) Util.initializeAccounts();
    }

    /**
     * Function to read a specific account, and that returns a copy of the account to work on tentative data.
     * @param accountId The account id to read.
     * @return A copy of the account to read.
     */
    public Account read(int accountId)
    {
        return new Account(accountList.get(accountId));
    }

    /**
     * Function that updates a specific account.
     * @param account The account to update.
     */
    public void write(Account account)
    {
        accountList.set(account.getNumber(), account);
    }

    /**
     * Function that updates several accounts given a set of accounts.
     * @param accountSet The account to update.
     */
    public void write(List<Account> accountSet)
    {
        for(Account account : accountSet)
        {
            accountList.set(account.getNumber(), account);
        }
    }

    public ArrayList<Account> getAccountList()
    {
        return accountList;
    }
}
