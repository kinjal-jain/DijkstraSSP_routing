/**
 * 
 * @author Kinjal Jain
 * 
 */
public class BinaryTrie {

	private Utility util = new Utility();

	// Definition of a Node in Binary Trie
	private class Node {
		char destArray[];
		int nextHop, index;
		boolean isBranch;
		Node left, right, parent;

		Node(char[] destArray, int nextHop, Node parent) {
			this.destArray = destArray;
			this.nextHop = nextHop;
			this.parent = parent;
			isBranch = false;
		}

		public Node(int index, Node parent) {
			isBranch = true;
			this.index = index;
			this.parent = parent;
		}
	}

	Node root;

	// Default Constructor
	BinaryTrie() {
		root = null;
	}

	/**
	 * Inserts a new (destination ip, next hop) pair
	 * 
	 * @param dest
	 *            Destination IP in binary
	 * @param nxt
	 *            Node number of the next hop in the shortest path to reach
	 *            destination
	 */

	/**
	 * This function inserts a new node in the Binary Trie.
	 * 
	 * @param destIP
	 *            Destination IP Address in binary
	 * @param next
	 *            Next node position for the next hop in the shortest path to
	 *            destination
	 */
	void addNode(char destIP[], int next) {

		// Initialize root node if not initialized
		if (root == null) {
			root = new Node(destIP, next, null);
			return;
		}

		// TODO handle the case for duplicate insert

		// If root is initialized,
		// 1. Reach a leaf node by doing binary search
		// 2. Compare the key to be inserted with the key of the leaf node and
		// find the index position
		// where they differ first
		// 3. Call insertAt to create a branch for the differing position and
		// creating & linking the new node and relinking the old leaf node

		// Binary search going through all branch nodes.
		Node n = root;
		while (n.isBranch) {
			if (destIP[n.index] == 0) {
				n = n.left;
			} else {
				n = n.right;
			}
		}

		// Find the first differentiating position
		int i = 0;
		while (i < 32) {
			if (destIP[i] != n.destArray[i]) {
				break;
			}
			i++;
		}

		// create new branch for the differing position and link new leaf node
		// and old leaf node to it
		insertAt(destIP, next, n.parent, i);

	}

	/**
	 * Traverses up the trie till it finds a branch node that has branch index
	 * position less than diffPosition. Then creates a new branch node that has
	 * branch index position = diffPosition, adds it as left/right child to the
	 * branch found above. Then adds the new leaf node and old node (that was
	 * previously at the position of the new branch node), as its children
	 * 
	 * In case branchNode becomes null while traversing up, store the old root in
	 * a temp variable, create a new root branch node with branching index =
	 * diffPosition, add the new leaf node and the old root as it child and make
	 * this new branch node as root
	 * 
	 * @param dest
	 * @param nexthop
	 * @param branchNode
	 * @param diffPosition
	 */
	private void insertAt(char[] dest, int nexthop, Node branchNode,
			int diffPosition) {

		// traverse up the trie
		while (branchNode != null && branchNode.index > diffPosition) {
			branchNode = branchNode.parent;
		}

		Node newBranch = new Node(diffPosition, branchNode);
		Node newLeaf = new Node(dest, nexthop, newBranch);

		// Case when branchNode becomes null,
		// i.e. root is reached
		if (branchNode == null) {
			Node oldRoot = root;
			oldRoot.parent = newBranch;
			if (dest[diffPosition] == '0') {
				newBranch.left = newLeaf;
				newBranch.right = oldRoot;
			} else {
				newBranch.left = oldRoot;
				newBranch.right = newLeaf;
			}
			root = newBranch;
		} else { // branchNode does not become null

			Node oldNode;

			// add the newBranch as a left/right child of branchNode
			// based on whether the destn bit at branchNode's index is 0 or 1
			if (dest[branchNode.index] == '0') {
				oldNode = branchNode.left;
				branchNode.left = newBranch;
			} else {
				oldNode = branchNode.right;
				branchNode.right = newBranch;
			}

			// oldNode will become child of newBranch
			oldNode.parent = newBranch;

			// add newLeaf as right/left child of newBranch
			// based on whether the destn bit at diffPosition index is 0 or 1
			if (dest[diffPosition] == '0') {
				newBranch.left = newLeaf;
				newBranch.right = oldNode;
			} else {
				newBranch.right = newLeaf;
				newBranch.left = oldNode;
			}
		}

	}

	public void compress() {
		compress(root);
	}

	private void compress(Node node) {
		if (node == null) {
			return;
		}

		if (node.isBranch) {
			if (node.left.isBranch) {
				compress(node.left);
			} else if (node.index < 31) {
				node.left.destArray[node.index + 1] = '*';
			}
		}

		if (node.right.isBranch) {
			compress(node.right);
		} else if (node.index < 31) {
			node.right.destArray[node.index + 1] = '*';
		}

		if (node.isBranch && !node.left.isBranch && !node.right.isBranch
				&& node.left.nextHop == node.right.nextHop) {
			node.destArray = node.left.destArray;
			node.isBranch = false;
			node.nextHop = node.left.nextHop;
			node.left = null;
			node.right = null;
			node.destArray[node.parent.index + 1] = '*';
		}

	}

	public char[] search(char[] destn) {
		Node x = root;
		if (x == null) {
			return null;
		}

		while (x.isBranch) {
			if (destn[x.index] == '0') {
				x = x.left;
			} else {
				x = x.right;
			}
		}

		return x.destArray;
	}

	public void print() {
		print(root, 0);
	}

	private void print(Node node, int level) {
		System.out.println(" " + level);
		if (node.isBranch) {
			System.out.println("Index = " + node.index);
			System.out.println("Left : ");
			print(node.left, level + 1);
			System.out.println("Right : ");
			print(node.right, level + 1);
		} else {
			System.out.println(util.printCharArray(node.destArray) + " "
					+ node.nextHop);
		}
	}
}