package core;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Master {
	static Mapper mapper;
	static Reducer reducer;

	static List<Integer> ports = new ArrayList<Integer>();
	
	static int function;
	static int mapperID;
	static Scanner input = new Scanner(System.in);

	public static void initPorts() { //this method initializes the List ports with the contents of file ports_numbers
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
	
	
	private static List<Double> initCoordinates (int k) { //this method is used for passing coordinates values to mappers
		//data for testing
		double minX, maxX, minY, maxY;
		
		minX  = -74.0144996501386;
		maxX = -73.9018372248612;
		minY = 40.67747711364791;
		maxY = 40.76662365086325;
		
		List<Double> coordinates = new ArrayList<Double>();
		if (k == 1) { //if is mapper1
			coordinates.add(minX);
			double newMaxX = ((maxX-minX)*1/3)+minX;
			coordinates.add(newMaxX);
			coordinates.add(minY);
			coordinates.add(maxY);
		}
		else if (k == 2) { //if is mapper2
			double newMinX =  ((maxX-minX)*1/3)+minX;
			coordinates.add(newMinX);
			double newMaxX = ((maxX-minX)*2/3)+minX;
			coordinates.add(newMaxX);
			coordinates.add(minY);
			coordinates.add(maxY);
		}
		else if (k ==3) { //if is mapper3
			double newMinX = ((maxX-minX)*2/3)+minX;
			coordinates.add(newMinX);
			coordinates.add(maxX);
			coordinates.add(minY);
			coordinates.add(maxY);
		}
			return coordinates;
	}
	
	private static String setDate(String date) {
		return date;
	}
	
	public static void welcomeMsg() { //Prints welcome messages program starts
		System.out.println("Select Role:");
		System.out.println("1) Client");
		System.out.println("2) Mapper");
		System.out.println("3) Reducer");
		System.out.print(">");
	}
	

	public static void main(String[] args) {

		initPorts();
		welcomeMsg();
		function = input.nextInt();

		if (function == 1) {			

			//ConnectToMapper is like Clients
			ConnectToMapper map1 = new ConnectToMapper(initCoordinates(1), setDate("2012-05-09 12:54:16"), "localhost", ports.get(0)); //Client1
			ConnectToMapper map2 = new ConnectToMapper(initCoordinates(2), setDate("2012-05-09 12:54:16"), "localhost", ports.get(1)); //Client2
			ConnectToMapper map3 = new ConnectToMapper(initCoordinates(3), setDate("2012-05-09 12:54:16"), "localhost", ports.get(2)); //Client3
			
			//Starting Threads
			map1.start();
			map2.start();
			map3.start();	
		}
		else if (function == 2) {
			System.out.println("Select mapper from 1 to 3");
			System.out.print(">");
			mapperID = input.nextInt();
			if (mapperID == 0||mapperID > 3) {
				System.out.println("No mapper found for this selection!");
			}
			int port = mapperID-1;
			
			mapper = new Mapper(ports.get(port));		
			mapper.initialize();
		} 
		else if (function == 3) {
			Reducer reducer = new Reducer(ports.get(3));
			reducer.initialize();
		}
		else {
			System.out.println("Select one of the available options");
		}
	}
}