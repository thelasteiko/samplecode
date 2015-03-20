package treemap;

import java.util.NoSuchElementException;

/**
 * Implements an unbalanced binary search tree. Note that all "matching" is
 * based on the compareTo method.
 * 
 * @author Mark Allen Weiss
 * 
 * Implements an unbalanced binary search tree using a few methods from
 * the original by Weiss. This implementation uses nodes with both a key
 * and value.
 * @see treemap.BinaryNode
 * 
 * This class assumes that searches are accomplished by the key and that
 * the value is the frequency.
 * @see treemap.BinarySearchTree#PrintMostFrequent(int)
 * 
 * @author Melinda Robertson
 * @version Feb 16, 2015
 * 
 * @param <K>
 *            is of a generic type.
 * @param <V>
 *            is of a generic type.
 */
public class BinarySearchTree<K extends Comparable<? super K>, V extends Comparable<? super V>>
		implements MyTreeMap<K, V> {
	


	/** The tree root. */
	protected BinaryNode<K, V> root;

	/**
	 * Construct the tree.
	 */
	public BinarySearchTree() {
		root = null;
	}

	/**
	 * @see treemap.MyTreeMap#get(Object)
	 */
	@Override
	public V get(K x) {
		return get(x, this.root);
	}

	/**
	 * @see treemap.MyTreeMap#put(Object, Object)
	 * @see treemap.BinarySearchTree#insert(Comparable, Comparable, BinaryNode)
	 */
	@Override
	public void put(K x, V y) {
		root = insert(x, y, root);
	}

	/**
	 * @see treemap.MyTreeMap#remove(Object)
	 */
	@Override
	public void remove(K x) {
		root = remove(x, root);
	}

	/**
	 * Test if the tree is logically empty.
	 * 
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * @see treemap.MyTreeMap#toString()
	 * @throws NoSuchElementException
	 *             if there are no elements.
	 */
	@Override
	public String toString() {
		if (root == null)
			throw new NoSuchElementException("There are no elements.");
		return "{" + toString(root) + "}";
	}

	/**
	 * @see treemap.MyTreeMap#PrintMostFrequent(int)
	 * @throws NoSuchElementException
	 *             if there are no elements.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void PrintMostFrequent(int n) {
		if (root == null)
			throw new NoSuchElementException("There are no elements.");
		// Finds the minimum value to start the list.
		BinaryNode<K, V> a = findMinFrequency(root, root);
		K[] keys = (K[]) new Comparable[n];
		V[] values = (V[]) new Comparable[n];
		keys[n - 1] = a.myKey;
		values[n - 1] = a.myValue;

		// Calls helper method to fill the rest of the array.
		PrintMostFrequent(keys, values, root);

		// Prints the result.
		for (int i = 0; i < keys.length; i++) {
			// Accounts for the situation that the user chose a number
			// larger than the tree size.
			if (keys[i] != null)
				System.out.print(keys[i] + "=" + values[i] + ", ");
		}
	}

	/**
	 * This returns the value associated with a key. Helper method for
	 * {@link treemap.BinarySearchTree#get(Comparable)}
	 * 
	 * @param x
	 *            is the key to search for.
	 * @param root
	 *            is the root of the subtree.
	 * @return the value at the specified key or null if not found.
	 */
	private V get(K x, BinaryNode<K, V> root) {
		if (root == null)
			return null;
		if (root.myKey.compareTo(x) < 0)
			return get(x, root.right);
		if (root.myKey.compareTo(x) > 0)
			return get(x, root.left);
		if (root.myKey.equals(x))
			return root.myValue;
		return null;
	}

	/**
	 * Searches through the tree and stores the maximum values in an array.
	 * Helper method for {@link treemap.BinarySearchTree#PrintMostFrequent(int)}
	 * 
	 * @param amount
	 *            is the array of maximum values.
	 * @param root
	 *            is the root of the subtree.
	 */
	private void PrintMostFrequent(K[] keys, V[] values, BinaryNode<K, V> root) {
		if (root == null)
			return;

		// Checks the values already in the arrays and pushes them
		// left to make room for a new value if it is greater than
		// the next element to the left.
		for (int i = keys.length - 1; i >= 0; i--) {
			if (values[i] == null)
				break; // Met a null value - end of list.
			if (root.myValue.compareTo(values[i]) >= 0) {
				pushLeft(keys, values, i);
				keys[i] = root.myKey;
				values[i] = root.myValue;
				break; // Found the correct place to insert so break.
			}
		}

		PrintMostFrequent(keys, values, root.right);
		PrintMostFrequent(keys, values, root.left);
	}

	/**
	 * Moves the elements of two arrays to the left, replacing the first item in
	 * the array. Helper method for
	 * {@link treemap.BinarySearchTree#PrintMostFrequent(Comparable[], Comparable[], BinaryNode)}
	 * 
	 * @param keys
	 *            is an array of a generic data type.
	 * @param values
	 *            is an array of a generic data type.
	 * @param index
	 *            is where this method stops moving elements. This is the
	 *            insertion point for a new value.
	 */
	private void pushLeft(K[] keys, V[] values, int index) {
		for (int i = 0; i < index; i++) {
			keys[i] = keys[i + 1];
			values[i] = values[i + 1];
		}
	}

	/**
	 * Finds and returns the node with the least value. Helper method for
	 * {@link treemap.BinarySearchTree#PrintMostFrequent(Comparable[], Comparable[], BinaryNode)}
	 * 
	 * @param min
	 *            is the node with the least value.
	 * @param root
	 *            is the root of the subtree.
	 * @return the node that has the least value in the tree.
	 */
	private BinaryNode<K, V> findMinFrequency(BinaryNode<K, V> min,
			BinaryNode<K, V> root) {
		if (root == null)
			return min;
		if (root.myValue.compareTo(min.myValue) <= 0) {
			min = findMinFrequency(root, root.left);
		} else
			min = findMinFrequency(min, root.left);

		if (root.myValue.compareTo(min.myValue) <= 0) {
			min = findMinFrequency(root, root.right);
		} else
			min = findMinFrequency(min, root.right);

		return min;
	}

	/**
	 * Internal method to insert into a subtree. Helper method for
	 * {@link treemap.BinarySearchTree#put(Comparable, Comparable)}
	 * 
	 * @param x
	 *            the item to insert.
	 * @param t
	 *            the node that roots the tree.
	 * @return the new root.
	 */
	protected BinaryNode<K, V> insert(K x, V y, BinaryNode<K, V> t) {
		if (t == null)
			t = new BinaryNode<K, V>(x, y);
		else if (x.compareTo(t.myKey) < 0)
			t.left = insert(x, y, t.left);
		else if (x.compareTo(t.myKey) > 0)
			t.right = insert(x, y, t.right);
		else
			t.myValue = y;
		return t;
	}

	/**
	 * Internal method to remove from a subtree. Helper method for
	 * {@link treemap.BinarySearchTree#remove(Comparable)}
	 * 
	 * @param x
	 *            the key of the item to remove.
	 * @param t
	 *            the node that roots the tree.
	 * @return the new root.
	 * @throws ItemNotFoundException
	 *             if x is not found.
	 */
	protected BinaryNode<K, V> remove(K x, BinaryNode<K, V> t) {
		if (t == null)
			throw new IllegalArgumentException(x.toString());
		if (x.compareTo(t.myKey) < 0)
			t.left = remove(x, t.left);
		else if (x.compareTo(t.myKey) > 0)
			t.right = remove(x, t.right);
		else if (t.left != null && t.right != null) // Two children
		{
			BinaryNode<K, V> temp = findMin(t.right);
			t.myKey = temp.myKey;
			t.myValue = temp.myValue;
			t.right = removeMin(t.right);
		} else
			t = (t.left != null) ? t.left : t.right;
		return t;
	}

	/**
	 * Internal method to remove minimum item from a subtree. Helper method for
	 * {@link treemap.BinarySearchTree#remove(Comparable, BinaryNode)}
	 * 
	 * @param t
	 *            the node that roots the tree.
	 * @return the new root.
	 * @throws ItemNotFoundException
	 *             if t is empty.
	 */
	protected BinaryNode<K, V> removeMin(BinaryNode<K, V> t) {
		if (t == null)
			throw new IllegalArgumentException();
		else if (t.left != null) {
			t.left = removeMin(t.left);
			return t;
		} else
			return t.right;
	}

	/**
	 * Returns a String representing the tree in ascending order. Helper method
	 * for {@link treemap.BinarySearchTree#toString()}
	 * 
	 * @param root
	 *            is the root of the subtree.
	 * @param s
	 *            is the String that will be added to and returned.
	 * @return the String that represents the tree.
	 */
	private String toString(BinaryNode<K, V> root) {
		String s = "";
		if (root == null)
			return s;

		s += toString(root.left);
		s += root.myKey + "=" + root.myValue + ", ";
		s += toString(root.right);

		return s;
	}

	/**
	 * Internal method to find the smallest item in a subtree. Helper method for
	 * {@link treemap.BinarySearchTree#remove(Comparable)}
	 * 
	 * @param t
	 *            the node that roots the tree.
	 * @return node containing the smallest item.
	 */
	protected BinaryNode<K, V> findMin(BinaryNode<K, V> t) {
		if (t.left == null)
			return t;
		return findMin(t.left);
	}
}
