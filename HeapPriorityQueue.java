package treemap;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * This class is a min-heap that uses nodes with keys and values. Values are
 * assumed to be the frequency.
 * 
 * @author Melinda Robertson
 * @version Mar 4, 2015
 * @param <K>
 *            is of a generic type.
 * @param <V>
 *            is of a generic type.
 */
public class HeapPriorityQueue<K extends Comparable<? super K>, V extends Comparable<? super V>>
		implements MyTreeMap<K, V> {

	private final int INITSIZE = 10;
	private HeapNode[] data;
	private int size;

	/**
	 * Constructs a min-heap using nodes with keys and values.
	 */
	@SuppressWarnings("unchecked")
	public HeapPriorityQueue() {
		data = new HeapPriorityQueue.HeapNode[INITSIZE];
		size = 0;
	}

	/**
	 * Returns whether the queue is empty.
	 * 
	 * @return true if size is zero. False otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * @see treemap.MyTreeMap#put(Object, Object)
	 */
	@Override
	public void put(K key, V value) {
		add(key, value);
	}

	/**
	 * @see treemap.MyTreeMap#get(Object)
	 */
	@Override
	public V get(K key) {
		if (isEmpty())
			return null;
		for (int i = 1; i <= size; i++) {
			if (data[i].key.equals(key)) {
				return data[i].value;
			}
		}
		return null;
	}

	/**
	 * @see treemap.MyTreeMap#remove(Object)
	 * @throws UnsupportedOperationException
	 *             because you can't do this with a heap.
	 */
	@Override
	public void remove(K key) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see treemap.MyTreeMap#PrintMostFrequent(int)
	 * @throws NoSuchElementException
	 *             if size equals zero.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void PrintMostFrequent(int n) {
		if (isEmpty())
			throw new NoSuchElementException();

		HeapNode[] nodes = new HeapPriorityQueue.HeapNode[n];
		nodes[n - 1] = data[1];

		for (int j = size - 1; j > 0; j--) {
			for (int i = n - 1; i >= 0; i--) {
				if (nodes[i] == null)
					break;
				if (data[j].value.compareTo(nodes[i].value) >= 0) {
					pushLeft(nodes, i);
					nodes[i] = data[j];
					break;
				}
			}
		}

		for (int m = 0; m < nodes.length; m++) {
			if (nodes[m] != null)
				System.out.print(nodes[m].key + "=" + nodes[m].value + ", ");
		}
	}

	/**
	 * Adds the given element to this queue.
	 * 
	 * @param key
	 *            is the key to add.
	 * @param value
	 *            is the value of the key to add.
	 */
	public void add(K key, V value) {
		// resize if necessary
		if (size + 1 >= data.length) {
			data = Arrays.copyOf(data, data.length * 2);
		}

		if (!isEmpty()) {
			// Check to see if the item is already there.
			for (int i = 1; i <= size; i++) {
				if (data[i] == null)
					break;
				if (data[i].key.equals(key)) {
					data[i].value = value;
					return;
				}
			}
		}

		// insert as new rightmost leaf
		data[size + 1] = new HeapNode(key, value);

		// "bubble up" toward root as necessary to fix ordering
		int index = size + 1;
		boolean found = false; // have we found the proper place yet?
		while (!found && hasParent(index)) {
			int parent = parent(index);
			if (data[index].key.compareTo(data[parent].key) < 0) {
				swap(data, index, parent(index));
				index = parent(index);
			} else {
				found = true; // found proper location; stop the loop
			}
		}

		size++;
	}

	/**
	 * Returns the minimum value in the queue without modifying the queue.
	 * 
	 * @return the minimum value in the queue.
	 * @throws NoSuchElementException
	 *             if the queue is empty.
	 */
	public K peek() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return data[1].key;
	}

	/**
	 * Removes and returns the minimum value in the queue.
	 * 
	 * @return the minimum value in the queue.
	 * @throws NoSuchElementException
	 *             if the queue is empty.
	 */
	public K remove() {
		K result = peek();

		// move rightmost leaf to become new root
		data[1] = data[size];
		size--;

		// "bubble down" root as necessary to fix ordering
		int index = 1;
		boolean found = false; // have we found the proper place yet?
		while (!found && hasLeftChild(index)) {
			int left = leftChild(index);
			int right = rightChild(index);
			int child = left;
			if (hasRightChild(index)
					&& data[right].key.compareTo(data[left].key) < 0) {
				child = right;
			}

			if (data[index].key.compareTo(data[child].key) > 0) {
				swap(data, index, child);
				index = child;
			} else {
				found = true; // found proper location; stop the loop
			}
		}

		return result;
	}

	/**
	 * Returns the number of elements in the queue.
	 * 
	 * @return the number of elements.
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns a string representation of this queue, such as "[10, 20, 30]";
	 * The elements are not guaranteed to be listed in sorted order.
	 * 
	 * @see treemap.MyTreeMap#toString()
	 */
	@SuppressWarnings("unchecked")
	public String toString() {
		HeapNode[] nodes = new HeapPriorityQueue.HeapNode[size];
		int i = 0;
		int stop = size - 1;
		String result = "{";
		while(!isEmpty() && i < nodes.length){
			nodes[i] = this.data[1];
			if (i == stop) result += nodes[i].key + "=" + nodes[i].value;
			else result += nodes[i].key + "=" + nodes[i].value + ", ";
			remove();
			i++;
		}
		for (int j = 0; j < nodes.length; j++) {
			this.add(nodes[j].key, nodes[j].value);
		}
		return result + "}";
	}

	/**
	 * Moves the elements of a generic array left one, replacing the first
	 * value, until the indicated index is reached.
	 * 
	 * @param nodes
	 *            is the generic array.
	 * @param index
	 *            is the index to end at.
	 */
	private <E> void pushLeft(E[] nodes, int index) {
		for (int i = 0; i < index; i++) {
			nodes[i] = nodes[i + 1];
		}
	}

	/**
	 * Helpers for navigating indexes up/down the tree.
	 * 
	 * @param index
	 *            is the index of the child.
	 * @return the index of the parent.
	 */
	private int parent(int index) {
		return index / 2;
	}

	/**
	 * Returns index of left child of given index.
	 * 
	 * @param index
	 *            is the index of the parent.
	 * @return the index of the left child.
	 */
	private int leftChild(int index) {
		return index * 2;
	}

	/**
	 * Returns index of right child of given index.
	 * 
	 * @param index
	 *            is the index of the parent.
	 * @return the index of the right child.
	 */
	private int rightChild(int index) {
		return index * 2 + 1;
	}

	/**
	 * Returns true if the node at the given index has a parent (is not the
	 * root).
	 * 
	 * @param index
	 *            is the index of the child.
	 * @return true if the child has a parent, false if it is the root.
	 */
	private boolean hasParent(int index) {
		return index > 1;
	}

	/**
	 * Returns true if the node at the given index has a non-empty left child.
	 * 
	 * @param index
	 *            is the index of the parent.
	 * @return true if the parent has a left child, false otherwise.
	 */
	private boolean hasLeftChild(int index) {
		return leftChild(index) <= size;
	}

	/**
	 * Returns true if the node at the given index has a non-empty right child.
	 * 
	 * @param index
	 *            is the index of the parent.
	 * @return true if the parent has a right child, false otherwise.
	 */
	private boolean hasRightChild(int index) {
		return rightChild(index) <= size;
	}

	/**
	 * Switches the values at the two given indexes of the given array.
	 * 
	 * @param a
	 *            is the generic array.
	 * @param index1
	 *            is the first index.
	 * @param index2
	 *            is the second index.
	 */
	private <E> void swap(E[] a, int index1, int index2) {
		E temp = a[index1];
		a[index1] = a[index2];
		a[index2] = temp;
	}

	/**
	 * The HeapNode class offers map functionality to a heap.
	 * 
	 * @author Melinda Robertson
	 * @version Mar 5, 2015
	 */
	private class HeapNode {
		private K key;
		private V value;

		HeapNode(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public String toString() {
			return this.key.toString() + "=" + this.value.toString();
		}
	}
}
