package treemap;

import java.util.NoSuchElementException;

/**
 * Implements an AVL tree. Note that all "matching" is based on the compareTo
 * method.
 * 
 * @author Mark Allen Weiss
 * 
 *         Implements an AVL tree using nodes with a key and value.
 * @see treemap.BinaryNode
 * 
 *      This class assumes that searches are accomplished by the key and that
 *      the value is the frequency.
 * @see treemap.AvlTree#PrintMostFrequent(int)
 * @author Melinda Robertson
 * @version Feb 23, 2015
 * 
 * @param <K>
 *            is of a generic type.
 * @param <V>
 *            is of a generic type.
 */
public class AvlTree<K extends Comparable<? super K>, V extends Comparable<? super V>>
		implements MyTreeMap<K, V> {

	/** The tree root. */
	private AvlNode<K, V> root;

	/**
	 * Construct the tree.
	 */
	public AvlTree() {
		root = null;
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
	 * @see treemap.MyTreeMap#put(Object, Object)
	 * @see treemap.AvlTree#insert(Comparable, Comparable, AvlNode)
	 */
	@Override
	public void put(K x, V y) {
		root = insert(x, y, root);
	}

	/**
	 * @see treemap.MyTreeMap#get(Object)
	 */
	@Override
	public V get(K x) {
		return get(x, this.root);
	}

	/**
	 * @see treemap.MyTreeMap#remove(Object)
	 */
	@Override
	public void remove(K x) {
		root = remove(x, root);
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
		AvlNode<K, V> a = findMinFrequency(root, root);
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
	 * Internal method to insert into a subtree. Helper method for
	 * {@link treemap.AvlTree#put(Comparable, Comparable)}
	 * 
	 * @param x
	 *            the key of the item to insert.
	 * @param y
	 *            the value of the item to insert.
	 * @param t
	 *            the node that roots the subtree.
	 * @return the new root of the subtree.
	 */
	private AvlNode<K, V> insert(K x, V y, AvlNode<K, V> t) {
		if (t == null)
			return new AvlNode<K, V>(x, y);

		int compareResult = x.compareTo(t.myKey);

		if (compareResult < 0)
			t.left = insert(x, y, t.left);
		else if (compareResult > 0)
			t.right = insert(x, y, t.right);
		else
			t.myValue = y; // Duplicate; update value
		return balance(t);
	}

	/**
	 * Returns the value associated with a key. Helper method for
	 * {@link treemap.AvlTree#get(Comparable)}
	 * 
	 * @param x
	 *            is the key to search for.
	 * @param root
	 *            is the root of the subtree.
	 * @return the value associated with the key.
	 */
	private V get(K x, AvlNode<K, V> root) {
		if (root == null)
			return null;

		int compareResult = x.compareTo(root.myKey);

		if (compareResult < 0)
			return get(x, root.left);
		else if (compareResult > 0)
			return get(x, root.right);
		else
			return root.myValue;
	}

	/**
	 * Internal method to remove from a subtree. Helper method for
	 * {@link treemap.AvlTree#remove(Comparable)}
	 * 
	 * @param x
	 *            the item to remove.
	 * @param t
	 *            the node that roots the subtree.
	 * @return the new root of the subtree.
	 */
	private AvlNode<K, V> remove(K x, AvlNode<K, V> t) {
		if (t == null)
			return t; // Item not found; do nothing

		int compareResult = x.compareTo(t.myKey);

		if (compareResult < 0)
			t.left = remove(x, t.left);
		else if (compareResult > 0)
			t.right = remove(x, t.right);
		else if (t.left != null && t.right != null) // Two children
		{
			AvlNode<K, V> temp = findMin(t.right);
			t.myKey = temp.myKey;
			t.myValue = temp.myValue;
			t.right = remove(t.myKey, t.right);
		} else
			t = (t.left != null) ? t.left : t.right;
		return balance(t);
	}

	/**
	 * Returns a String representing the tree in ascending order. Helper method
	 * for {@link treemap.AvlTree#toString()}
	 * 
	 * @param root
	 *            is the root of the subtree.
	 * @param s
	 *            is the String that will be added to and returned.
	 * @return the String that represents the tree.
	 */
	private String toString(AvlNode<K, V> root) {
		String s = "";
		if (root == null)
			return s;

		s += toString(root.left);
		s += root.myKey + "=" + root.myValue + ", ";
		s += toString(root.right);

		return s;
	}

	/**
	 * Searches through the tree and stores the maximum values in an array.
	 * Helper method for {@link treemap.AvlTree#PrintMostFrequent(int)}
	 * 
	 * @param amount
	 *            is the array of maximum values.
	 * @param root
	 *            is the root of the subtree.
	 */
	private void PrintMostFrequent(K[] keys, V[] values, AvlNode<K, V> root) {
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
	 * Finds and returns the node with the least value. Helper method for
	 * {@link treemap.AvlTree#PrintMostFrequent(Comparable[], Comparable[], AvlNode)}
	 * 
	 * @param min
	 *            is the node with the least value.
	 * @param root
	 *            is the root of the subtree.
	 * @return the node that has the least value in the tree.
	 */
	private AvlNode<K, V> findMinFrequency(AvlNode<K, V> min, AvlNode<K, V> root) {
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
	 * Moves the elements of two arrays to the left, replacing the first item in
	 * the array. Helper method for
	 * {@link treemap.AvlTree#PrintMostFrequent(Comparable[], Comparable[], AvlNode)}
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
	 * Final variable that controls how 'unbalanced' the tree is allowed to be.
	 */
	private static final int ALLOWED_IMBALANCE = 1;

	/**
	 * Iterates up the tree and determines if it is unbalanced. An unbalanced
	 * tree means that the difference between the right subtree and left subtree
	 * is greater than the ALLOWED_IMBALANCE. Adjusts the tree accordingly.
	 * Assume t is either balanced or within one of being balanced.
	 * 
	 * @see treemap.AvlTree#ALLOWED_IMBALANCE
	 * @param t
	 *            is the root of the subtree.
	 * @return the new root of the tree.
	 */
	private AvlNode<K, V> balance(AvlNode<K, V> t) {
		if (t == null)
			return t;

		if (height(t.left) - height(t.right) > ALLOWED_IMBALANCE)
			if (height(t.left.left) >= height(t.left.right))
				t = rotateWithLeftChild(t);
			else
				t = doubleWithLeftChild(t);
		else if (height(t.right) - height(t.left) > ALLOWED_IMBALANCE)
			if (height(t.right.right) >= height(t.right.left))
				t = rotateWithRightChild(t);
			else
				t = doubleWithRightChild(t);

		t.height = Math.max(height(t.left), height(t.right)) + 1;
		return t;
	}

	/**
	 * Checks the balance of the root.
	 */
	public void checkBalance() {
		checkBalance(root);
	}

	/**
	 * Determines if a tree is balanced by checking the height of its children.
	 * 
	 * @param t
	 *            is the root of the tree.
	 * @return the height of the root.
	 */
	private int checkBalance(AvlNode<K, V> t) {
		if (t == null)
			return -1;

		if (t != null) {
			int hl = checkBalance(t.left);
			int hr = checkBalance(t.right);
			if (Math.abs(height(t.left) - height(t.right)) > 1
					|| height(t.left) != hl || height(t.right) != hr)
				System.out.println("OOPS!!");
		}

		return height(t);
	}

	/**
	 * Internal method to find the smallest item in a subtree.
	 * 
	 * @param t
	 *            the node that roots the tree.
	 * @return node containing the smallest item.
	 */
	private AvlNode<K, V> findMin(AvlNode<K, V> t) {
		if (t == null)
			return t;

		while (t.left != null)
			t = t.left;
		return t;
	}

	/**
	 * Return the height of node t, or -1, if null.
	 */
	private int height(AvlNode<K, V> t) {
		return t == null ? -1 : t.height;
	}

	/**
	 * Rotate binary tree node with left child. For AVL trees, this is a single
	 * rotation for case 1. Update heights, then return new root.
	 */
	private AvlNode<K, V> rotateWithLeftChild(AvlNode<K, V> k2) {
		AvlNode<K, V> k1 = (AvlNode<K, V>) k2.left;
		k2.left = k1.right;
		k1.right = k2;
		k2.height = Math.max(height((AvlNode<K, V>) k2.left),
				height((AvlNode<K, V>) k2.right)) + 1;
		k1.height = Math.max(height((AvlNode<K, V>) k1.left), k2.height) + 1;
		return k1;
	}

	/**
	 * Rotate binary tree node with right child. For AVL trees, this is a single
	 * rotation for case 4. Update heights, then return new root.
	 */
	private AvlNode<K, V> rotateWithRightChild(AvlNode<K, V> k1) {
		AvlNode<K, V> k2 = (AvlNode<K, V>) k1.right;
		k1.right = k2.left;
		k2.left = k1;
		k1.height = Math.max(height((AvlNode<K, V>) k1.left),
				height((AvlNode<K, V>) k1.right)) + 1;
		k2.height = Math.max(height((AvlNode<K, V>) k2.right), k1.height) + 1;
		return k2;
	}

	/**
	 * Double rotate binary tree node: first left child with its right child;
	 * then node k3 with new left child. For AVL trees, this is a double
	 * rotation for case 2. Update heights, then return new root.
	 */
	private AvlNode<K, V> doubleWithLeftChild(AvlNode<K, V> k3) {
		k3.left = rotateWithRightChild((AvlNode<K, V>) k3.left);
		return rotateWithLeftChild(k3);
	}

	/**
	 * Double rotate binary tree node: first right child with its left child;
	 * then node k1 with new right child. For AVL trees, this is a double
	 * rotation for case 3. Update heights, then return new root.
	 */
	private AvlNode<K, V> doubleWithRightChild(AvlNode<K, V> k1) {
		k1.right = rotateWithLeftChild((AvlNode<K, V>) k1.right);
		return rotateWithRightChild(k1);
	}

	/**
	 * Private inner class to define a node to use with an AVL tree. This is the
	 * same as BinaryNode but adds height.
	 * 
	 * @author Melinda Robertson
	 *
	 * @param <K>
	 *            is a key of a generic type.
	 * @param <V>
	 *            is a value of a generic type.
	 */
	private static class AvlNode<K, V> {
		/**
		 * Height of the node. A leaf is at height 0.
		 */
		int height;
		/**
		 * The key for the node.
		 */
		K myKey;
		/**
		 * The value of the node.
		 */
		V myValue;
		/**
		 * Right child.
		 */
		AvlNode<K, V> right;
		/**
		 * Left child.
		 */
		AvlNode<K, V> left;

		/**
		 * Constructor for a node.
		 * @param theKey is the key of the node.
		 * @param theValue is the value of the node.
		 */
		AvlNode(K theKey, V theValue) {
			myKey = theKey;
			myValue = theValue;
			height = 0;
		}
	}
}
