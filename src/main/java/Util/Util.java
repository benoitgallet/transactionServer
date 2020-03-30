package Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class Util
{
    public static final int port = 12345;
    public static boolean optimisticLocking = true;
    public static final String transferCommand = "/transfer";
    public static final int nbTransactions = 20;

    private static final int nbAccount = 10;

    public static Collection<Account> initializeAccounts()
    {
        List<Account> accounts = new ArrayList<>(nbAccount);
        for (int i = 0; i < nbAccount; ++i)
        {
//            accounts.add(new Account(i, 50 * i));
            //TODO Create a new account with (i, some_random_int)
            //TODO add the account to the collection of accounts
        }
        return accounts;
    }
}
