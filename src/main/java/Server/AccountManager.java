package Server;

import Util.Account;
import Util.Util;

import java.util.ArrayList;

public class AccountManager
{
    public static ArrayList<Account> accountList;

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

    public ArrayList<Account> getAccountList()
    {
        return accountList;
    }
}
