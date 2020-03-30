package Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Krishna Chaitanya Pullela, Manoj Mallidi, Benoit Gallet
 */
public class Util
{
    public static final String serverAddress = "localhost";
    public static final int port = 12345;
    public static boolean optimisticLocking = true;

    public static final int nbTransactions = 20;
    public static final int nbAccount = 10;
    public static final int initialAccountAmount = 10;

    /**
     * Initialize a collection of accounts with the same amount. Several parameters from above can be changed to
     * adapt the behavior of this function (e.g., the number of accounts or their initial amount).
     * @return A collection of accounts.
     */
    public static Collection<Account> initializeAccounts()
    {
        List<Account> accounts = new ArrayList<>(nbAccount);
        for (int i = 0; i < nbAccount; ++i)
        {
            accounts.add(new Account(i, initialAccountAmount));
        }
        return accounts;
    }
}
