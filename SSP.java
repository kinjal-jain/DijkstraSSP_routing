import java.util.Iterator;
import java.util.LinkedList;

public class SSP {

	public static void main(String args[]) {

		// int src = Integer.parseInt(args[1]);
		// int dest = Integer.parseInt(args[2]);
		// String path = new String(args[0]);
		int src = 0;
		int dest = 999;
		String path = "part1/input_1000_50_part1.txt";
		Utility util = new Utility();
		Graph graph = util.graphFileReader(path);
		LinkedList<Integer> shortestPath = graph.dijkstra(src, dest);
		Iterator<Integer> iterator = shortestPath.iterator();

		while (iterator.hasNext()) {
			System.out.print(iterator.next() + " ");
		}

	}
}
