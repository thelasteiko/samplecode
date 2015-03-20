package treemap;

/**
 * Implements a set of objects using a hash table. The hash table uses separate
 * chaining to resolve collisions. Entries consist of a key and value. The value
 * is assumed to be the frequency. Both the key and value must extend
 * Comparable.
 * 
 * @author Melinda Robertson
 * @version Mar 10, 2015
 *
 * @param <K>
 *            is of a generic type.
 * @param <V>
 *            is of a generic type.
 */
public class HashSet<K extends Comparable<? super K>, V extends Comparable<? super V>>
		implements MyTreeMap<K, V> {

	/**
	 * The load factor of the hash set. Determines when to rehash.
	 */
	private static final double MAX_LOAD_FACTOR = 0.75;

	/**
	 * The initial size of the hash set.
	 */
	private final int INITSIZE = 31;

	/**
	 * The data stored in the hash table.
	 */
	private HashEntry[] elementData;

	/**
	 * The number of elements in the table.
	 */
	private int size;

	/**
	 * Constructs an empty set.
	 */
	@SuppressWarnings("unchecked")
	public HashSet() {
		elementData = new HashSet.HashEntry[INITSIZE];
		size = 0;
	}

	/**
	 * Returns true if there are no elements in this hash table.
	 * 
	 * @return true if there are no elements, false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns the number of elements in the hash table.
	 * 
	 * @return the number of elements.
	 */
	public int size() {
		return size;
	}

	/**
	 * @see treemap.MyTreeMap#put(Object, Object)
	 */
	@Override
	public void put(K x, V y) {
		int bucket = hashFunction(x);
		HashEntry temp = elementData[bucket];
		while (temp != null) {
			if (temp.key.equals(x)) {
				temp.data = y;
				return;
			}
			temp = temp.next;
		}
		add(x, y);
	}

	/**
	 * @see treemap.MyTreeMap#get(Object)
	 */
	@Override
	public V get(K x) {
		int bucket = hashFunction(x);
		HashEntry current = elementData[bucket];
		while (current != null) {
			if (current.key.equals(x)) {
				return current.data;
			}
			current = current.next;
		}
		return null;
	}

	/**
	 * @see treemap.MyTreeMap#PrintMostFrequent(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void PrintMostFrequent(int n) {
		HashEntry[] a = new HashSet.HashEntry[n];
		a[n - 1] = findMinValue();

		for (int i = 0; i < elementData.length; i++) {
			HashEntry temp = elementData[i];
			while (temp != null) {
				for (int j = n - 1; j >= 0; j--) {
					if (a[j] == null)
						break;
					if (temp.data.compareTo(a[j].data) >= 0) {
						pushLeft(a, j);
						a[j] = temp;
						break;
					}
				}
				temp = temp.next;
			}
		}

		for (int m = 0; m < a.length; m++) {
			System.out.print(a[m].key + "=" + a[m].data + ", ");
		}
	}

	/**
	 * Adds the given element to this set, whether or not it's already in the
	 * set.
	 * 
	 * @param key
	 *            is the key of the element to add.
	 * @param value
	 *            is the value of the element to add.
	 */
	public void add(K key, V value) {
		if (loadFactor() >= MAX_LOAD_FACTOR) {
			rehash();
		}

		// insert new value at front of list
		int bucket = hashFunction(key);
		elementData[bucket] = new HashEntry(key, value, elementData[bucket]);
		size++;
	}

	/**
	 * Returns true if the given value is found in this set.
	 * 
	 * @param value
	 *            is the key of the entry.
	 * @return true if the set contains the value, false otherwise.
	 */
	public boolean contains(K value) {
		int bucket = hashFunction(value);
		HashEntry current = elementData[bucket];
		while (current != null) {
			if (current.data.equals(value)) {
				return true;
			}
			current = current.next;
		}
		return false;
	}

	/**
	 * Removes the given value if it is contained in the set. If the set does
	 * not contain the value, has no effect.
	 */
	public void remove(K value) {
		int bucket = hashFunction(value);
		if (elementData[bucket] != null) {
			// check front of list
			if (elementData[bucket].data.equals(value)) {
				elementData[bucket] = elementData[bucket].next;
				size--;
			} else {
				// check rest of list
				HashEntry current = elementData[bucket];
				while (current.next != null && !current.next.data.equals(value)) {
					current = current.next;
				}

				// if the element is found, remove it
				if (current.next != null && current.next.data.equals(value)) {
					current.next = current.next.next;
					size--;
				}
			}
		}
	}

	/**
	 * Returns a string representation of this queue, such as "[10, 20, 30]";
	 * The elements are not guaranteed to be listed in sorted order.
	 * 
	 * @see treemap.MyTreeMap#toString()
	 */
	@Override
	public String toString() {
		String result = "{";
		boolean first = true;
		if (!isEmpty()) {
			for (int i = 0; i < elementData.length; i++) {
				HashEntry current = elementData[i];
				while (current != null) {
					if (!first) {
						result += ", ";
					}
					result += current.key + "=" + current.data;
					first = false;
					current = current.next;
				}
			}
		}
		return result + "}";
	}

	/**
	 * Helper method for {@link treemap.HashSet#PrintMostFrequent(int)}. Moves
	 * the elements in an array of HashEntries left to replace the first value.
	 * 
	 * @param a
	 *            is the array to shift.
	 * @param index
	 *            is the index to shift up to.
	 */
	private void pushLeft(HashEntry[] a, int index) {
		for (int i = 0; i < index; i++) {
			a[i] = a[i + 1];
		}
	}

	/**
	 * Helper method for {@link treemap.HashSet#PrintMostFrequent(int)}. Finds
	 * the HashEntry with the least data value.
	 * 
	 * @return the HashEntry with the least data value.
	 */
	private HashEntry findMinValue() {
		HashEntry min = elementData[0];
		for (int i = 0; i < elementData.length; i++) {
			HashEntry temp = elementData[i];
			while (temp != null) {
				if (min == null)
					min = temp;
				if (temp.data.compareTo(min.data) < 0) {
					min = temp;
				}
				temp = temp.next;
			}
		}
		return min;
	}

	/**
	 * Returns the preferred hash bucket index for the given value.
	 * 
	 * @param value
	 *            is the key of the entry.
	 * @return an appropriate index number.
	 */
	private int hashFunction(K value) {
		return Math.abs(value.hashCode()) % elementData.length;
	}

	/**
	 * Returns the load factor for the hash table.
	 * 
	 * @return the load factor.
	 */
	private double loadFactor() {
		return (double) size / elementData.length;
	}

	/**
	 * Resizes the hash table to twice its former size.
	 */
	@SuppressWarnings("unchecked")
	private void rehash() {
		// replace element data array with a larger empty version
		HashEntry[] oldElementData = elementData;
		elementData = new HashSet.HashEntry[getPrime(elementData.length)];
		size = 0;

		// re-add all of the old data into the new array
		for (int i = 0; i < oldElementData.length; i++) {
			HashEntry current = oldElementData[i];
			while (current != null) {
				add(current.key, current.data);
				current = current.next;
			}
		}
	}

	/**
	 * Helper method for {@link treemap.HashSet#rehash()}
	 * 
	 * @param n
	 *            is the length of the old array.
	 * @return a new array length that is a prime roughly twice n.
	 */
	private int getPrime(int n) {
		int prime = n * 2 - 1;
		boolean isPrime = false;
		while (!isPrime) {
			for (int i = 2; i < Math.sqrt(prime); i++) {
				if (prime % i == 0) {
					isPrime = false;
					break;
				} else
					isPrime = true;
			}
			if (!isPrime)
				prime--;
		}
		return prime;
	}

	/**
	 * Represents a single key-value pair in a chain stored in one hash bucket.
	 * 
	 * @author Melinda Robertson
	 *
	 */
	private class HashEntry {
		/**
		 * The key of the entry.
		 */
		public K key;

		/**
		 * The data in the entry.
		 */
		public V data;

		/**
		 * The next HashEntry with the same hash value.
		 */
		public HashEntry next;

		/**
		 * A constructor for a HashEntry.
		 * 
		 * @param key
		 *            is the key of the entry.
		 * @param data
		 *            is the data in the entry.
		 */
		@SuppressWarnings("unused")
		public HashEntry(K key, V data) {
			this(key, data, null);
		}

		/**
		 * A constructor for a HashEntry.
		 * 
		 * @param key
		 *            is the key of the entry.
		 * @param data
		 *            is the data in the entry.
		 * @param next
		 *            is the next HashEntry with the same hash value.
		 */
		public HashEntry(K key, V data, HashEntry next) {
			this.key = key;
			this.data = data;
			this.next = next;
		}
	}
}
