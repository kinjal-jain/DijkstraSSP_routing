import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the logic to build a Fibonacci Heap and uses the Node
 * skeleton from Class FibonacciNode.
 * 
 * @author Kinjal Jain
 * 
 */
public class FibonacciHeap {

	FibonacciNode minNode;
	int totalNodes;

	// Default Constructor
	FibonacciHeap() {

	}

	private static final double oneOverLogPhi = 1.0 / Math.log((1.0 + Math
			.sqrt(5.0)) / 2.0);

	// Insert a node into Fibonacci Heap
	public void insert(FibonacciNode node, int key, int index) {
		node.key = key;
		node.index = index;
		if (minNode != null) {
			node.left = minNode;
			node.right = minNode.right;
			minNode.right = node;
			node.right.left = node;

			if (key < minNode.key) {
				minNode = node;
			}
		} else {
			node.right = node;
			minNode = node;
		}

		totalNodes++;
	}

	/**
	 * This function removes the minimum node from the Fibonacci Heap and
	 * returns the minimum node.
	 * 
	 * @return temp The min node
	 */
	public FibonacciNode removeMin() {
		FibonacciNode temp = minNode;

		if (temp != null) {
			int totalChildren = temp.degree;
			FibonacciNode node = temp.child;
			FibonacciNode tempRight;

			while (totalChildren > 0) {
				tempRight = node.right;
				node.left.right = node.right;
				node.right.left = node.left;
				node.left = minNode;
				node.right = minNode.right;
				minNode.right = node;
				node.right.left = node;
				node.parent = null;
				node = tempRight;
				totalChildren--;
			}

			temp.left.right = temp.right;
			temp.right.left = temp.left;

			if (temp == temp.right) {
				minNode = null;
			} else {
				minNode = temp.right;
				consolidate();
			}

			totalNodes--;
		}

		return temp;
	}

	/**
	 * This function implements the functionality of a Fibonacci Cut
	 * 
	 * @param first
	 *            The first node
	 * @param second
	 *            The Second node
	 */
	protected void removeAndCut(FibonacciNode first, FibonacciNode second) {
		first.left.right = first.right;
		first.right.left = first.left;
		second.degree--;

		if (second.child == first) {
			second.child = first.right;
		}

		if (second.degree == 0) {
			second.child = null;
		}

		first.left = minNode;
		first.right = minNode.right;
		minNode.right = first;
		first.right.left = first;

		first.parent = null;
		first.childCut = false;
	}

	/**
	 * This function implements the functionality to link two Fibonacci nodes.
	 * 
	 * @param tempNode2
	 * @param tempNode1
	 */
	protected void link(FibonacciNode tempNode2, FibonacciNode tempNode1) {
		tempNode2.left.right = tempNode2.right;
		tempNode2.right.left = tempNode2.left;
		tempNode2.parent = tempNode1;

		if (tempNode1.child == null) {
			tempNode1.child = tempNode2;
			tempNode2.right = tempNode2;
			tempNode2.left = tempNode2;
		} else {
			tempNode2.left = tempNode1.child;
			tempNode2.right = tempNode1.child.right;
			tempNode1.child.right = tempNode2;
			tempNode2.right.left = tempNode2;
		}

		tempNode1.degree++;
		tempNode2.childCut = false;
	}

	protected void consolidate() {
		int arraySize = ((int) Math.floor(Math.log(totalNodes) * oneOverLogPhi)) + 1;

		List<FibonacciNode> nodeArray = new ArrayList<FibonacciNode>(
				arraySize);

		for (int i = 0; i < arraySize; i++) {
			nodeArray.add(null);
		}

		int numRoots = 0;
		FibonacciNode tmtempNodes = minNode;

		if (tmtempNodes != null) {
			numRoots++;
			tmtempNodes = tmtempNodes.right;

			while (tmtempNodes != minNode) {
				numRoots++;
				tmtempNodes = tmtempNodes.right;
			}
		}

		while (numRoots > 0) {
			int tmpDegree = tmtempNodes.degree;
			FibonacciNode next = tmtempNodes.right;

			for (;;) {
				FibonacciNode tmp1 = nodeArray.get(tmpDegree);
				if (tmp1 == null) {
					break;
				}

				if (tmtempNodes.key > tmp1.key) {
					FibonacciNode bufferNode = tmp1;
					tmp1 = tmtempNodes;
					tmtempNodes = bufferNode;
				}

				link(tmp1, tmtempNodes);

				nodeArray.set(tmpDegree, null);
				tmpDegree++;
			}

			nodeArray.set(tmpDegree, tmtempNodes);

			tmtempNodes = next;
			numRoots--;
		}

		minNode = null;

		for (int i = 0; i < arraySize; i++) {
			FibonacciNode tempNode = nodeArray.get(i);
			if (tempNode == null) {
				continue;
			}

			if (minNode != null) {
				tempNode.left.right = tempNode.right;
				tempNode.right.left = tempNode.left;

				tempNode.left = minNode;
				tempNode.right = minNode.right;
				minNode.right = tempNode;
				tempNode.right.left = tempNode;

				if (tempNode.key < minNode.key) {
					minNode = tempNode;
				}
			} else {
				minNode = tempNode;
			}
		}
	}

	/**
	 * This function implements the functionality of Cascading Cut
	 * 
	 * @param node
	 *            Node on which cascading cut has to be performed.
	 */
	protected void cascadingCut(FibonacciNode node) {
		FibonacciNode parentNode = node.parent;

		if (parentNode != null) {
			if (!node.childCut) {
				node.childCut = true;
			} else {
				removeAndCut(node, parentNode);

				cascadingCut(parentNode);
			}
		}
	}

	/**
	 * This function implements the functionality of Decrease Key
	 * 
	 * @param node
	 *            Node on which Decrease Key has to be performed.
	 * @param key
	 *            Value of Key
	 */
	public void decreaseKey(FibonacciNode node, int key) {
		if (key > node.key) {
			throw new IllegalArgumentException(
					"The Key value is larger than the node key.");
		}

		node.key = key;

		FibonacciNode parentNode = node.parent;

		if ((parentNode != null) && (node.key < parentNode.key)) {
			removeAndCut(node, parentNode);
			cascadingCut(parentNode);
		}

		if (node.key < minNode.key) {
			minNode = node;
		}
	}

}// Fibonacci Heap Class ends

