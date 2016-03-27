import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Master {
	static Socket client;
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
					while ((line = reader.readLine())!=null) {
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
				client = new Socket(InetAddress.getByName("localhost"), ports.get(0));
				ObjectInputStream in = new ObjectInputStream(client.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
				
			}
			else if (function == 2) {
				mapper = new Mapper(ports.get(0));
				mapper.initialize();
			}
			else if (function == 3) {
				System.out.println("Which port would you like to use for Reducer?");
				port = input.nextInt();
				Reducer reducer = new Reducer(port);
				reducer.initialize();
			}
			
	
		}

}
