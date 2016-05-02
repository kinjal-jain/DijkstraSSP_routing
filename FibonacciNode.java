/**
 * This Class defines the skeleton of a Fibonacci Node
 * 
 * @author Kinjal Jain
 * 
 */
public class FibonacciNode {

	FibonacciNode parent;
	FibonacciNode child;
	FibonacciNode right;
	FibonacciNode left;
	int degree;
	boolean childCut;
	int key;
	int index;

	// Getter function to get the key
	int getKey() {
		return key;
	}

}
