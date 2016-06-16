package core;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class InitApp {
	static Mapper mapper;
	static Reducer reducer;

	static List<String> addr_ports = new ArrayList<String>();
	
	static int function;
	static int mapperID;
	static Scanner input = new Scanner(System.in);

	public static void initPorts() { //this method initializes the List ports with the contents of file ports_numbers
		try {
			FileReader fr = new FileReader("ADDR_PORTS");
			BufferedReader reader = new BufferedReader(fr);
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					addr_ports.add(line);
				}
			} catch (IOException e) {
				System.err.println("Error reading next line...");
			}
		} catch (FileNotFoundException e) {
			System.err.println("Error loading file..");
		}
	}
	
	
	public static void welcomeMsg() { //Prints welcome messages program starts
		System.out.println("Select Role:");
		System.out.println("1) Mapper");
		System.out.println("2) Reducer");
		System.out.print(">");
	}
	

	public static void main(String[] args) {

		initPorts();
		welcomeMsg();
		function = input.nextInt();

		if (function == 1) {
			System.out.println("Select mapper from 1 to 3");
			System.out.print(">");
			mapperID = input.nextInt();
			if (mapperID == 0||mapperID > 3) {
				System.out.println("No mapper found for this selection!");
			}
			int port = 2*mapperID-1;
			
			mapper = new Mapper(Integer.parseInt(addr_ports.get(port)), addr_ports.get(6), Integer.parseInt(addr_ports.get(7)));		
			mapper.initialize();
		} 
		
		else if (function == 2) {
			Reducer reducer = new Reducer(Integer.parseInt(addr_ports.get(7)), addr_ports.get(8), Integer.parseInt(addr_ports.get(9)));
			reducer.initialize();
		}
		
		else {
			System.out.println("Select one of the available options");
		}
	}
}