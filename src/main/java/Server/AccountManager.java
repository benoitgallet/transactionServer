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

    public Account read(int accountId)
    {
        return new Account(accountList.get(accountId));
    }

    public void write(Account account)
    {
        accountList.set(account.getNumber(), account);
    }
}
