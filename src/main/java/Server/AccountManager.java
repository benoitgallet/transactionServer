package Server;

import Util.Account;
import Util.Util;

import java.util.ArrayList;
import java.util.List;

public class AccountManager
{
    public static List<Account> accountList;

    public AccountManager()
    {
        accountList = (ArrayList<Account>) Util.initializeAccounts();
    }
}
