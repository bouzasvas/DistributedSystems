import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Master {
//	static Socket client;
//	static ObjectInputStream in = null;
//	static ObjectOutputStream out = null;

	static Mapper mapper;
	static Reducer reducer;

	static List<Integer> ports = new ArrayList<Integer>();
	static int function;
	static int mapperID;
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
			//ConnectToMapper is like Clients
			ConnectToMapper map1 = new ConnectToMapper("localhost", ports.get(0)); //Client1
			ConnectToMapper map2 = new ConnectToMapper("localhost", ports.get(1)); //Client2
			ConnectToMapper map3 = new ConnectToMapper("localhost", ports.get(2)); //Client3
			
			//Starting Threads
			map1.start();
			map2.start();
			map3.start();	
		}
		else if (function == 2) {
			System.out.println("Select mapper from 1 to 3");
			mapperID = input.nextInt();
			int port = mapperID-1;
			
			mapper = new Mapper(ports.get(port));		
			mapper.initialize();
		} 
		else if (function == 3) {
			Reducer reducer = new Reducer(ports.get(3));
			reducer.initialize();
		}
	}
}
