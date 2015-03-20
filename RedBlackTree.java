package treemap;

import java.util.NoSuchElementException;

/**
 * Implements a red-black tree. Note that all "matching" is based on the
 * compareTo method.
 * 
 * @author Mark Allen Weiss
 * 
 * Implements a red-black tree with nodes that have a key and value.
 * @see treemap.BinaryNode
 * 
 * This class assumes that searches are accomplished by
 * the key and that the value is the frequency.
 * @see treemap.BinarySearchTree#PrintMostFrequent(int)
 * 
 * @author Melinda Robertson
 * @version Feb 26, 2015
 */
public class RedBlackTree<K extends Comparable<? super K>, V extends Comparable<? super V>>
		implements MyTreeMap<K, V> {

	/**
	 * The header's node right child is the root.
	 */
	private RedBlackNode<K, V> header;
	
	/**
	 * A 'null' node that indicates the end of the tree.
	 */
	private RedBlackNode<K, V> nullNode;

	/**
	 * It's final and black.
	 */
	private static final int BLACK = 1; // BLACK must be 1
	/**
	 * It's final and red.
	 */
	private static final int RED = 0;

	/**
	 * Used in insert routine and its helpers
	 */
	private RedBlackNode<K, V> current;
	/**
	 * Used in insert routine and its helpers
	 */
	private RedBlackNode<K, V> parent;
	/**
	 * Used in insert routine and its helpers
	 */
	private RedBlackNode<K, V> grand;
	/**
	 * Used in insert routine and its helpers
	 */
	private RedBlackNode<K, V> great;

	/**
	 * Construct the tree.
	 */
	public RedBlackTree() {
		nullNode = new RedBlackNode<K, V>(null, null);
		nullNode.left = nullNode.right = nullNode;
		header = new RedBlackNode<K, V>(null, null);
		header.left = header.right = nullNode;
	}
	
	/**
	 * Test if the tree is logically empty.
	 * 
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty() {
		return header.right == nullNode;
	}

	/**
	 * @see treemap.MyTreeMap#put(Object, Object)
	 * @see treemap.RedBlackTree#insert(Comparable, Comparable)
	 */
	@Override
	public void put(K x, V y) {
		insert(x, y);
		current = null;
		nullNode.myKey = null;
		nullNode.myValue = null;
	}

	/**
	 * @see treemap.MyTreeMap#get(Object)
	 * @see treemap.RedBlackTree#find(Comparable)
	 */
	@Override
	public V get(K x) {
		V value = find(x);
		current = null;
		nullNode.myKey = null;
		nullNode.myValue = null;
		return value;
	}

	/**
	 * @see treemap.MyTreeMap#toString()
	 * @throws NoSuchElementException
	 *             if there are no elements.
	 */
	@Override
	public String toString() {
		if (header.right == nullNode)
			throw new NoSuchElementException("There are no elements.");
		return "{" + toString(header.right) + "}";
	}

	/**
	 * @see treemap.MyTreeMap#PrintMostFrequent(int)
	 * @throws NoSuchElementException
	 *             if there are no elements.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void PrintMostFrequent(int n) {
		if (header.right == nullNode)
			throw new NoSuchElementException("There are no elements.");
		// Finds the minimum value to start the list.
		RedBlackNode<K, V> a = findMinFrequency(header.right, header.right);
		K[] keys = (K[]) new Comparable[n];
		V[] values = (V[]) new Comparable[n];
		keys[n - 1] = a.myKey;
		values[n - 1] = a.myValue;

		// Calls helper method to fill the rest of the array.
		PrintMostFrequent(keys, values, header.right);

		// Prints the result.
		for (int i = 0; i < keys.length; i++) {
			// Accounts for the situation that the user chose a number
			// larger than the tree size.
			if (keys[i] != null)
				System.out.print(keys[i] + "=" + values[i] + ", ");
		}
	}

	/**
	 * Searches through the tree and stores the maximum values in an array.
	 * Helper method for {@link treemap.RedBlackTree#PrintMostFrequent(int)}
	 * @param amount
	 *            is the array of maximum values.
	 * @param root
	 *            is the root of the subtree.
	 */
	private void PrintMostFrequent(K[] keys, V[] values, RedBlackNode<K, V> root) {
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
	 * Moves the elements of two arrays to the left, replacing the first item in
	 * the array.
	 * Helper method for {@link treemap.RedBlackTree#PrintMostFrequent(Comparable[], Comparable[], RedBlackNode)}
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
	 * Finds and returns the node with the least value.
	 * Helper method for {@link treemap.RedBlackTree#PrintMostFrequent(Comparable[], Comparable[], RedBlackNode)}
	 * @param min
	 *            is the node with the least value.
	 * @param root
	 *            is the root of the subtree.
	 * @return the node that has the least value in the tree.
	 */
	private RedBlackNode<K, V> findMinFrequency(RedBlackNode<K, V> min,
			RedBlackNode<K, V> root) {
		if (root == nullNode)
			return min;
		if (root.myValue.compareTo(min.myValue) < 0) {
			min = findMinFrequency(root, root.left);
		} else
			min = findMinFrequency(min, root.left);

		if (root.myValue.compareTo(min.myValue) < 0) {
			min = findMinFrequency(root, root.right);
		} else
			min = findMinFrequency(min, root.right);

		return min;
	}

	/**
	 * Returns a String representing the tree in ascending order.
	 * Helper method for {@link treemap.RedBlackTree#toString()}
	 * @param root
	 *            is the root of the subtree.
	 * @param s
	 *            is the String that will be added to and returned.
	 * @return the String that represents the tree.
	 */
	private String toString(RedBlackNode<K, V> root) {
		String s = "";
		if (root == nullNode)
			return s;

		s += toString(root.left);
		s += root.myKey + "=" + root.myValue + ", ";
		s += toString(root.right);

		return s;
	}

	/**
	 * Compare item and t.element, using compareTo, with caveat that if t is
	 * header, then item is always larger. This routine is called if is possible
	 * that t is header. If it is not possible for t to be header, use compareTo
	 * directly.
	 */
	private final int compare(K item, RedBlackNode<K, V> t) {
		if (t == header)
			return 1;
		else
			return item.compareTo(t.myKey);
	}

	/**
	 * Insert into the tree.
	 * @see treemap.RedBlackTree#put(Comparable, Comparable)
	 * @param key
	 *            the key to insert.
	 * @param value 
	 * 				the value to insert.
	 */
	public void insert(K key, V value) {
		current = parent = grand = header;
		nullNode.myKey = key;
		nullNode.myValue = value;

		while (compare(key, current) != 0) {
			great = grand;
			grand = parent;
			parent = current;
			current = compare(key, current) < 0 ? current.left : current.right;

			// Check if two red children; fix if so
			if (current.left.color == RED && current.right.color == RED)
				handleReorient(key);
		}

		// Insertion fails if already present
		if (current != nullNode) {
			if (current.myKey.equals(key)) {
				current.myValue = value;
				return;
			}
		} else {
			current = new RedBlackNode<K, V>(key, value, nullNode, nullNode);
		}

		// Attach to parent
		if (compare(key, parent) < 0)
			parent.left = current;
		else
			parent.right = current;
		handleReorient(key);
	}

	/**
	 * Remove from the tree.
	 * 
	 * @param x
	 *            the item to remove.
	 * @throws UnsupportedOperationException
	 *             if called.
	 */
	public void remove(K x) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Find an item in the tree.
	 * @see treemap.RedBlackTree#get(Comparable)
	 * @param x
	 *            the item to search for.
	 * @return the matching item or null if not found.
	 */
	private V find(K x) {
		nullNode.myKey = x;
		current = header.right;

		for (;;) {
			if (x.compareTo(current.myKey) < 0)
				current = current.left;
			else if (x.compareTo(current.myKey) > 0)
				current = current.right;
			else if (current != nullNode)
				return current.myValue;
			else
				return null;
		}
	}

	/**
	 * Internal routine that is called during an insertion if a node has two red
	 * children. Performs flip and rotations.
	 * 
	 * @param item
	 *            the item being inserted.
	 */
	private void handleReorient(K key) {
		// Do the color flip
		current.color = RED;
		current.left.color = BLACK;
		current.right.color = BLACK;

		if (parent.color == RED) // Have to rotate
		{
			grand.color = RED;
			if ((compare(key, grand) < 0) != (compare(key, parent) < 0))
				parent = rotate(key, grand); // Start dbl rotate
			current = rotate(key, great);
			current.color = BLACK;
		}
		header.right.color = BLACK; // Make root black
	}

	/**
	 * Internal routine that performs a single or double rotation. Because the
	 * result is attached to the parent, there are four cases. Called by
	 * handleReorient.
	 * 
	 * @param item
	 *            the item in handleReorient.
	 * @param parent
	 *            the parent of the root of the rotated subtree.
	 * @return the root of the rotated subtree.
	 */
	private RedBlackNode<K, V> rotate(K key, RedBlackNode<K, V> parent) {
		if (compare(key, parent) < 0)
			return parent.left = compare(key, parent.left) < 0 ? rotateWithLeftChild(parent.left)
					: // LL
					rotateWithRightChild(parent.left); // LR
		else
			return parent.right = compare(key, parent.right) < 0 ? rotateWithLeftChild(parent.right)
					: // RL
					rotateWithRightChild(parent.right); // RR
	}

	/**
	 * Rotate binary tree node with left child.
	 */
	private static <K, V> RedBlackNode<K, V> rotateWithLeftChild(
			RedBlackNode<K, V> k2) {
		RedBlackNode<K, V> k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		return k1;
	}

	/**
	 * Rotate binary tree node with right child.
	 */
	private static <K, V> RedBlackNode<K, V> rotateWithRightChild(
			RedBlackNode<K, V> k1) {
		RedBlackNode<K, V> k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		return k2;
	}

	/**
	 * Private inner class for red black nodes.
	 * @author Melinda Robertson
	 *
	 * @param <K> is of a generic type.
	 * @param <V> is of a generic type.
	 */
	private static class RedBlackNode<K, V> {
		/**
		 * The key for the node.
		 */
		K myKey;
		/**
		 * The value of the node.
		 */
		V myValue;
		/**
		 * The left child.
		 */
		RedBlackNode<K, V> left;
		/**
		 * The right child.
		 */
		RedBlackNode<K, V> right;
		/**
		 * The color of the node.
		 */
		int color;

		/**
		 * Constructor for a node with no children.
		 * @param theKey is the key of the node.
		 * @param theValue is the value of the node.
		 */
		RedBlackNode(K theKey, V theValue) {
			this(theKey, theValue, null, null);
		}

		/**
		 * Constructor for a node that has children.
		 * @param theKey is the key of the node.
		 * @param theValue is the value of the node.
		 * @param lt is the left child.
		 * @param rt is the right child.
		 */
		RedBlackNode(K theKey, V theValue, RedBlackNode<K, V> lt,
				RedBlackNode<K, V> rt) {
			myKey = theKey;
			myValue = theValue;
			left = lt;
			right = rt;
			color = RedBlackTree.BLACK;
		}
	}
}
