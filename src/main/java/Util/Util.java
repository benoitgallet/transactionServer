package Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Util
{
    public static final int port = 12345;
    public static boolean optimisticLocking = true;
    public static final String transferCommand = "/transfer";

    private static final int nbAccount = 10;

    public static Collection<Account> initializeAccounts()
    {
        List<Account> accounts = new ArrayList<>(nbAccount);
        for (int i = 0; i < nbAccount; ++i)
        {
            accounts.add(new Account(i, 50 * i));
        }
        return accounts;
    }
}
