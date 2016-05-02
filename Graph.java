import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class implements the functionality of a Graph along with its utility
 * functions.
 * 
 * @author Kinjal Jain
 * 
 */
public class Graph {

	ArrayList<GraphNode>[] adj;
	int noOfVertices;

	// Default Constructor
	@SuppressWarnings("unchecked")
	Graph(int v) {

		adj = (ArrayList<GraphNode>[]) new ArrayList[v];

		for (int i = 0; i < v; i++) {
			adj[i] = new ArrayList<GraphNode>();
		}

		noOfVertices = v;
	}

	public void add(int vertex1, int vertex2, int weight) {

		GraphNode newNode1 = new GraphNode(vertex1, weight);
		GraphNode newNode2 = new GraphNode(vertex2, weight);
		adj[vertex1].add(newNode2);
		adj[vertex2].add(newNode1);

	}

	/**
	 * This function implements the logic for Dijkstra Algorithm to find the
	 * shortest path between two given nodes on a Graph.
	 * 
	 * @param source
	 *            Source Node
	 * @param dest
	 *            Destination Node
	 * @return Shortest Path between two nodes
	 */
	public LinkedList<Integer> dijkstra(int source, int dest) {

		FibonacciHeap fh = new FibonacciHeap();
		FibonacciNode fn[] = new FibonacciNode[noOfVertices];
		int parent[] = new int[noOfVertices];
		boolean sptSet[] = new boolean[noOfVertices];
		int srcToDest = 0;

		for (int i = 0; i < noOfVertices; i++) {

			fn[i] = new FibonacciNode();
			if (i != source) {
				fh.insert(fn[i], Integer.MAX_VALUE, i);
			}
			sptSet[i] = false;
		}

		fh.insert(fn[source], 0, source);

		for (int count = 0; count < noOfVertices - 1; count++) {

			FibonacciNode temp = fh.removeMin();
			int u = temp.index;
			sptSet[u] = true;
			Iterator<GraphNode> iterator = adj[u].iterator();

			while (iterator.hasNext()) {

				GraphNode node = iterator.next();
				int v = node.getIndex();
				if (!sptSet[v]
						&& (fn[u].getKey() + node.getWeight()) < fn[v].getKey()) {
					fh.decreaseKey(fn[v], fn[u].getKey() + node.getWeight());
					if (v == dest) {
						srcToDest = fn[u].getKey() + node.getWeight();
					}
					parent[v] = u;
				}
			}

		}

		int temp = dest;
		LinkedList<Integer> list = new LinkedList<Integer>();

		while (temp != source) {
			list.add(temp);
			temp = parent[temp];
		}

		list.add(source);
		Collections.reverse(list);
		System.out.println(srcToDest);

		return list;
	}// end of function

	/**
	 * This function implements the logic for Dijkstra Algorithm and returns the
	 * shortest path from single source to all nodes in a graph
	 * 
	 * @param source
	 *            Source node
	 * @return Map of shortest paths from a single source
	 */
	public HashMap<Integer, LinkedList<Integer>> dijkstra(int source) {

		FibonacciHeap fh = new FibonacciHeap();
		FibonacciNode fn[] = new FibonacciNode[noOfVertices];
		int srcToDest[] = new int[noOfVertices];
		int parent[] = new int[noOfVertices];
		boolean sptSet[] = new boolean[noOfVertices];

		for (int i = 0; i < noOfVertices; i++) {

			srcToDest[i] = 0;
			fn[i] = new FibonacciNode();
			if (i != source) {
				fh.insert(fn[i], Integer.MAX_VALUE, i);
			}
			sptSet[i] = false;
		}

		fh.insert(fn[source], 0, source);

		for (int count = 0; count < noOfVertices - 1; count++) {

			FibonacciNode temp = fh.removeMin();
			int u = temp.index;

			sptSet[u] = true;

			Iterator<GraphNode> iterator = adj[u].iterator();

			while (iterator.hasNext()) {

				GraphNode node = iterator.next();
				int v = node.getIndex();
				if (!sptSet[v]
						&& (fn[u].getKey() + node.getWeight()) < fn[v].getKey()) {
					fh.decreaseKey(fn[v], fn[u].getKey() + node.getWeight());
					srcToDest[v] = fn[u].getKey() + node.getWeight();
					parent[v] = u;
				}
			}

		}

		HashMap<Integer, LinkedList<Integer>> shortestPaths = new HashMap<Integer, LinkedList<Integer>>();
		for (int i = 0; i < noOfVertices; i++) {
			LinkedList<Integer> shortestPathList = new LinkedList<Integer>();
			int x = i;
			while (x != source) {
				shortestPathList.add(x);
				x = parent[x];
			}
			shortestPathList.add(source);

			Collections.reverse(shortestPathList);
			shortestPaths.put(i, shortestPathList);
		}
		return shortestPaths;
	} // End of Function

	// This function prints the edges and nodes of a graph
	public void printGraph() {

		for (int i = 0; i < noOfVertices; i++) {

			System.out.print("vertex[" + i + "] = ");
			Iterator<GraphNode> iterator = adj[i].iterator();
			while (iterator.hasNext()) {
				iterator.next().print();
			}

			System.out.println("");

		}
	}
}
