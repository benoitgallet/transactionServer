package Util;

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
