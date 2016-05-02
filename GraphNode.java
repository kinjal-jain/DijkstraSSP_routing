/**
 * This class implements the skeleton of a Graph Node
 * 
 * @author Kinjal Jain
 * 
 */
public class GraphNode {

	int index;
	int weight;

	// Default Constructor
	GraphNode(int index, int weight) {
		this.index = index;
		this.weight = weight;
	}

	// This function prints the index along with the weight of the vertex.
	protected void print() {
		System.out.print(index + "(" + weight + ")");
	}

	// Getter to get Weight
	protected int getWeight() {
		return weight;
	}

	// Getter to get Index
	protected int getIndex() {
		return index;

	}
}
