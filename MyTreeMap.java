package treemap;

/**
 * @author Monika Sobolewska
 */
public interface MyTreeMap<AnyKey, AnyValue> {
    
    /**
     * Insert into the tree.
     * @param x the key to insert.
     * @param y the value to insert.
     */
    public void put(AnyKey x, AnyValue y);
    
    /**
     * Returns a value associated with a key.
     * @param x is the key
     * @return the value associated with the key
     */
    public AnyValue get(AnyKey x);
    
    
    /**
     * Removes a key-value pair from the tree.
     * @param x is the key to be removed
     */
    public void remove(AnyKey x);
    
    /**
     * Returns tree contents as a string.
     * @return tree contents as a string
     */
    public String toString();
    
    /**
     * Prints n most frequent elements in the map.
     * @param n is less than or equal to map size
     */
    public void PrintMostFrequent(int n);

}
