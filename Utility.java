import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Utility {

	final String spaceDelimiter = " ";
	final char asteriskConstant = '*';
	final String blankConstant = "";

	public Graph graphFileReader(String path) {
		DataInputStream dis = null;

		// this try clause consist of code to read the vertices and edges of the
		// graph from the input file

		try {
			dis = new DataInputStream(new FileInputStream(path));

			String input = dis.readLine().toString();
			String tokens[] = input.split(spaceDelimiter);
			int vertices = Integer.parseInt(tokens[0]);
			int edges = Integer.parseInt(tokens[1]);
			Graph graph = new Graph(vertices);

			for (int i = 0; i < edges; i++) {

				input = dis.readLine().toString();

				if (input.equals(blankConstant)) {
					i--;
					continue;
				}

				tokens = input.split(spaceDelimiter);
				int vertex1 = Integer.parseInt(tokens[0]);
				int vertex2 = Integer.parseInt(tokens[1]);
				int weight = Integer.parseInt(tokens[2]);

				graph.add(vertex1, vertex2, weight);

			}
			dis.close();
			return graph;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public ArrayList<char[]> ipFileReader(String path, int numberNodes) {
		ArrayList<char[]> charArray = new ArrayList<char[]>();
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new FileInputStream(path));
			for (int i = 0; i < numberNodes; i++) {
				charArray.add(convertToBinary(dis.readLine()));
				dis.readLine();
			}
			dis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return charArray;
	}

	private static char[] convertToBinary(String input) {
		byte[] bytes = new byte[0];
		try {
			bytes = InetAddress.getByName(input).getAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String output = new BigInteger(1, bytes).toString(2);
		return output.toCharArray();

	}

	public String printCharArray(char[] intermediateNode) {
		String charArray = "";
		for (int i = 0; i < 32 && intermediateNode[i] != asteriskConstant; i++) {
			charArray = charArray + intermediateNode[i];
		}
		return charArray;
	}

}
