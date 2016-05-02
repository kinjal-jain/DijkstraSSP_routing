import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * @author Kinjal Jain
 * 
 */
public class Routing {

	public static void main(String args[]) {

		Utility util = new Utility();
		// reads all required command line arguments
		// and prints error message if not properly formatted
		String pathOfGraphFile = "part2/input_graphsmall_part2.txt";
		String pathOfIPAddrFile = "part2/input_ipsmall_part2.txt";
		int srcNode = 1;
		int destNode = 6;
		try {
			// pathOfGraphFile = args[0];
			// pathOfIPAddrFile = args[1];
			// sourceNodeNumber = Integer.parseInt(args[3]);
			// destinationNodeNumber = Integer.parseInt(args[4]);
		} catch (Exception e) {
			System.out
					.println("Please check the command line arguments to match the following format : ");
			System.out
					.println("java routing file_name_1 file_name_2 source_node destination_node");
		}

		// Returns the Graph by reading the input form file_1
		Graph graph = util.graphFileReader(pathOfGraphFile);

		// Returns the list of IP Address in binary format by reading the input
		// form file_2
		ArrayList<char[]> ipAddressList = util.ipFileReader(pathOfIPAddrFile,
				graph.noOfVertices);

		// This function prints the shortest distance between source and
		// destination nodes and returns the shortest path
		LinkedList<Integer> shortestPath = graph.dijkstra(srcNode, destNode);

		// For all nodes in the shortest path (except the destination node),
		// generate hashtable records containing destination as key
		// and next hop as value.
		HashMap<Integer, HashMap<Integer, Integer>> allDestinationPath = new HashMap<Integer, HashMap<Integer, Integer>>();
		for (int i = 0; i < shortestPath.size() - 1; i++) {
			HashMap<Integer, Integer> destinationPath = new HashMap<Integer, Integer>();

			// call dijkstra and get shortest paths
			HashMap<Integer, LinkedList<Integer>> wholePaths = graph
					.dijkstra(shortestPath.get(i));

			// from wholePaths, insert into into destinationPath,
			// key as the IP address of the destinations' node number
			// data as the next 2nd node number in the shortest path
			for (int j = 0; j < graph.noOfVertices; j++) {
				if (j != shortestPath.get(i))
					destinationPath.put(j, wholePaths.get(j).get(1));
			}

			allDestinationPath.put(shortestPath.get(i), destinationPath);
		}

		// Generates an array of Binary Tries for all nodes in the calculated
		// shortest path except for the destination node.
		BinaryTrie tries[] = new BinaryTrie[shortestPath.size() - 1];
		for (int i = 0; i < tries.length; i++) {
			tries[i] = new BinaryTrie();
			int currentNode = shortestPath.get(i);
			for (int j = 0; j < graph.noOfVertices; j++) {
				// System.out.println(allDestinationPath.get(currentNode));
				if (currentNode != j) {
					char[] destinationIPAddress = ipAddressList.get(j);
					// System.out.print(i+"--");
					tries[i].addNode(destinationIPAddress, allDestinationPath
							.get(currentNode).get(j));
				}
			}
			// System.out.println("**");
			// tries[i].print();
			// System.out.println("**");
			// prune / compress trie
			tries[i].compress();
			// System.out.println("==");
			// tries[i].print();
			// System.out.println("==");
		}

		// For all Tries of the nodes in the shortest path from source to
		// destination (except destination),
		// Get the prefix IP address (key) of the leaf Binary Trie node which
		// points to the next hop (data) that should be visited to reach
		// destination,
		// and print the prefix IP address in CDR format
		for (int i = 0; i < tries.length; i++) {
			char intermediateNode[] = tries[i].search(ipAddressList
					.get(destNode));
			System.out.print(util.printCharArray(intermediateNode)
					+ util.spaceDelimiter);
		}
		// System.out.print(displayCharArray(ipAddresses.get(3)));
		// System.out.println(displayCharArray(ipAddresses.get(destinationNodeNumber)));
	}
}