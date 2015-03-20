package treemap;

import java.util.Iterator;

//Basic node stored in unbalanced binary search trees
//Note that this class is not accessible outside
//of this package.
/**
 * A class that defines a node to use with binary trees.
 * 
 * @author Melinda Robertson
 *
 * @param <K>
 *            is of a generic type.
 * @param <V>
 *            is of a generic type.
 */
class BinaryNode<K, V> {
	/**
	 * The key for the value.
	 */
	K myKey;

	/**
	 * The value stored in this node.
	 */
	V myValue;

	/**
	 * The left child.
	 */
	BinaryNode<K, V> left;

	/**
	 * The right child.
	 */
	BinaryNode<K, V> right;

	/**
	 * Constructs a binary node with a key and value.
	 * @param theKey	is the key with which to find the value.
	 * @param theValue	is the value to store in this node.
	 */
	BinaryNode(K theKey, V theValue) {
		myKey = theKey;
		myValue = theValue;
		left = right = null;
	}
}

class BinaryNodeIterator<K, V> implements Iterator<K> {
	
	BinaryNode<K, V> current;
	boolean removeOK;

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public K next() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
