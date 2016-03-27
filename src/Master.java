import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Master {
	static Socket client;
	static ObjectInputStream in = null;
	static ObjectOutputStream out = null;

	static Mapper mapper;
	static Reducer reducer;

	static List<Integer> ports = new ArrayList<Integer>();
	static int function;
	static Scanner input = new Scanner(System.in);

	public static void initPorts() {
		try {
			FileReader fr = new FileReader("ports_numbers");
			BufferedReader reader = new BufferedReader(fr);
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					ports.add(Integer.parseInt(line));
				}
			} catch (IOException e) {
				System.err.println("Error reading next line...");
			}
		} catch (FileNotFoundException e) {
			System.err.println("Error loading file..");
		}
	}

	public static void main(String[] args) {

		initPorts();
		System.out.println("Press 1 for Client\n2 for Mapper\nand 3 for Reducer");
		function = input.nextInt();

		if (function == 1) {
			double minX, maxX, minY, maxY = 0;
			String datetime;
			try {
				client = new Socket(InetAddress.getByName("localhost"), ports.get(0));
				//in = new ObjectInputStream(client.getInputStream());
				out = new ObjectOutputStream(client.getOutputStream());
				minX = -74.9;
				maxX = -73.9;
				minY = 40.6;
				maxY = 40.9;
				datetime = "2012-04-04 00:00:00";
				out.writeDouble(minX);
				out.flush();
				out.writeDouble(maxX);
				out.flush();
				out.writeDouble(minY);
				out.flush();
				out.writeDouble(maxY);
				out.flush();
				out.writeObject(datetime);
				out.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				try {
					client.close();
					//in.close();
					out.close();
				} catch (IOException e) {
					System.err.println("Error closing streams...");
				}

			}
		}

		else if (function == 2) {
			mapper = new Mapper(ports.get(0));
			mapper.initialize();
		} else if (function == 3) {
			Reducer reducer = new Reducer(ports.get(3));
			reducer.initialize();
		}

	}

}
