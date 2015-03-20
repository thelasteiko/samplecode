package treemap;

import java.util.NoSuchElementException;

/**
 * Implements a top-down splay tree. Note that all "matching" is based on the
 * compareTo method.
 * 
 * @author Mark Allen Weiss
 * 
 * Implements a top-down splay tree using nodes with a key and value.
 * @see treemap.BinaryNode
 * 
 * This class assumes that searches are accomplished by the key and that
 * the value is the frequency.
 * @see treemap.SplayTree#PrintMostFrequent(int)
 * @author Melinda Robertson
 * @version Feb 23, 2015
 * 
 * @param <K>
 *            is of a generic type.
 * @param <V>
 *            is of a generic type.
 */
public class SplayTree<K extends Comparable<? super K>, V extends Comparable<? super V>>
		implements MyTreeMap<K, V> {
	/**
	 * The root of the tree.
	 */
	private BinaryNode<K, V> root;

	/**
	 * A node that indicates the end of the tree.
	 */
	private BinaryNode<K, V> nullNode;

	/**
	 * A new node to insert. Otherwise will be null.
	 */
	private BinaryNode<K, V> newNode = null; // Used between different inserts

	/**
	 * Header node for {@link treemap.SplayTree#splay(Comparable, BinaryNode)}
	 */
	private BinaryNode<K, V> header = new BinaryNode<K, V>(null, null); // For
																		// splay

	/**
	 * Construct the tree.
	 */
	public SplayTree() {
		nullNode = new BinaryNode<K, V>(null, null);
		nullNode.left = nullNode.right = nullNode;
		root = nullNode;
	}

	/**
	 * Test if the tree is logically empty.
	 * 
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty() {
		return root == nullNode;
	}

	/**
	 * @see treemap.MyTreeMap#put(Object, Object)
	 * @see treemap.SplayTree#insert(Comparable, Comparable)
	 */
	@Override
	public void put(K x, V y) {
		insert(x, y);
		nullNode.myKey = null;
	}

	/**
	 * @see treemap.MyTreeMap#get(Object)
	 * @see treemap.SplayTree#splay(Comparable, BinaryNode)
	 */
	@Override
	public V get(K x) {
		if (isEmpty())
			return null;
		root = splay(x, root);
		nullNode.myKey = null;
		if (root.myKey.compareTo(x) == 0)
			return root.myValue;
		return null;
	}

	/**
	 * Remove from the tree.
	 * 
	 * @see treemap.MyTreeMap#remove(Object)
	 * @see treemap.SplayTree#splay(Comparable, BinaryNode)
	 * @param x
	 *            the item to remove.
	 */
	public void remove(K x) {
		BinaryNode<K, V> newTree;

		// If x is found, it will be at the root
		root = splay(x, root);
		if (root.myKey.compareTo(x) != 0)
			return; // Item not found; do nothing

		if (root.left == nullNode)
			newTree = root.right;
		else {
			// Find the maximum in the left subtree
			// Splay it to the root; and then attach right child
			newTree = root.left;
			newTree = splay(x, newTree);
			newTree.right = root.right;
		}
		root = newTree;
	}

	/**
	 * @see treemap.MyTreeMap#toString()
	 * @see treemap.SplayTree#toString(BinaryNode)
	 * @throws NoSuchElementException
	 *             if there are no elements.
	 */
	@Override
	public String toString() {
		if (root == nullNode)
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
		if (root == nullNode)
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
			if (keys[i] != null) {
				// root = splay(keys[i], root);
				System.out.print(i + " " + keys[i] + "=" + values[i] + "; ");
			}
		}
	}

	/**
	 * Insert into the tree.
	 * 
	 * @param x
	 *            the key to insert.
	 * @param y
	 * 			the value to insert.
	 */
	public void insert(K x, V y) {
		if (newNode == null)
			newNode = new BinaryNode<K, V>(x, y);

		if (root == nullNode) {
			newNode.left = newNode.right = nullNode;
			root = newNode;
		} else {
			root = splay(x, root);

			int compareResult = x.compareTo(root.myKey);

			if (compareResult < 0) {
				newNode.left = root.left;
				newNode.right = root;
				root.left = nullNode;
				root = newNode;
			} else if (compareResult > 0) {
				newNode.right = root.right;
				newNode.left = root;
				root.right = nullNode;
				root = newNode;
			} else {
				root.myValue = y;// Update frequency
			}
		}
		newNode = null; // So next insert will call new
	}

	/**
	 * Returns a String representing the tree in ascending order. Helper method
	 * for {@link treemap.SplayTree#toString()}
	 * 
	 * @param root
	 *            is the root of the subtree.
	 * @param s
	 *            is the String that will be added to and returned.
	 * @return the String that represents the tree.
	 */
	private String toString(BinaryNode<K, V> root) {
		String s = "";
		if (root == nullNode)
			return s;

		s += toString(root.left);
		s += root.myKey + "=" + root.myValue + ", ";
		s += toString(root.right);

		return s;
	}

	/**
	 * Searches through the tree and stores the maximum values in an array.
	 * Helper method for {@link treemap.SplayTree#PrintMostFrequent(int)}
	 * 
	 * @param amount
	 *            is the array of maximum values.
	 * @param root
	 *            is the root of the subtree.
	 */
	private void PrintMostFrequent(K[] keys, V[] values, BinaryNode<K, V> root) {
		if (root == nullNode)
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
	 * {@link treemap.SplayTree#PrintMostFrequent(Comparable[], Comparable[], BinaryNode)}
	 * 
	 * @param min
	 *            is the node with the least value.
	 * @param root
	 *            is the root of the subtree.
	 * @return the node that has the least value in the tree.
	 */
	private BinaryNode<K, V> findMinFrequency(BinaryNode<K, V> min,
			BinaryNode<K, V> root) {
		if (root == nullNode)
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
	 * {@link treemap.SplayTree#PrintMostFrequent(Comparable[], Comparable[], BinaryNode)}
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
	 * Internal method to perform a top-down splay. The last accessed node
	 * becomes the new root.
	 * 
	 * @param x
	 *            the target item to splay around.
	 * @param t
	 *            the root of the subtree to splay.
	 * @return the subtree after the splay.
	 */
	private BinaryNode<K, V> splay(K x, BinaryNode<K, V> t) {
		BinaryNode<K, V> leftTreeMax, rightTreeMin;

		header.left = header.right = nullNode;
		leftTreeMax = rightTreeMin = header;

		nullNode.myKey = x; // Guarantee a match

		for (;;) {
			int compareResult = x.compareTo(t.myKey);

			if (compareResult < 0) {
				if (x.compareTo(t.left.myKey) < 0)
					t = rotateWithLeftChild(t);
				if (t.left == nullNode)
					break;
				// Link Right
				rightTreeMin.left = t;
				rightTreeMin = t;
				t = t.left;
			} else if (compareResult > 0) {
				if (x.compareTo(t.right.myKey) > 0)
					t = rotateWithRightChild(t);
				if (t.right == nullNode)
					break;
				// Link Left
				leftTreeMax.right = t;
				leftTreeMax = t;
				t = t.right;
			} else
				break;
		}

		leftTreeMax.right = t.left;
		rightTreeMin.left = t.right;
		t.left = header.right;
		t.right = header.left;
		return t;
	}

	/**
	 * Rotate binary tree node with left child. For AVL trees, this is a single
	 * rotation for case 1.
	 */
	private static <K, V> BinaryNode<K, V> rotateWithLeftChild(
			BinaryNode<K, V> k2) {
		BinaryNode<K, V> k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		return k1;
	}

	/**
	 * Rotate binary tree node with right child. For AVL trees, this is a single
	 * rotation for case 4.
	 */
	private static <K, V> BinaryNode<K, V> rotateWithRightChild(
			BinaryNode<K, V> k1) {
		BinaryNode<K, V> k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		return k2;
	}
}
