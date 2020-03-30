package Util;

/**
 * Generic class to work on pairs, so we do not have to use the class implemented in the javafx package.
 * @param <K> The type of the first field.
 * @param <V> The type of the second field.
 */
public class Pair<K, V>
{
    private K first;
    private V second;

    public Pair(K first, V second)
    {
        this.first = first;
        this.second = second;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public K first()
    {
        return first;
    }

    public void setFirst(K first)
    {
        this.first = first;
    }

    public V second()
    {
        return second;
    }

    public void setSecond(V second)
    {
        this.second = second;
    }
}
