package core;

import java.util.Scanner;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Client {
	
	private ServerSocket fromReducer = null;
	private Socket reducer = null;
	
	private Map<Object, Long> fromClient = null;
	private List<String> addr_ports = null;
	
	private double minX, maxX, minY, maxY;
	private String fromDate, toDate;
	private String[] dates = new String[2];
	
	private Scanner input = new Scanner(System.in);
	
	public Client(List<String> addr_ports) {
		this.addr_ports = addr_ports;
		selectInput();
	}
	
	public void initServer() {
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		
		try {
			int port = Integer.parseInt(addr_ports.get(9));
			fromReducer = new ServerSocket(port);
			reducer = fromReducer.accept();
			
			out = new ObjectOutputStream(reducer.getOutputStream());
			in = new ObjectInputStream(reducer.getInputStream());
			
			String msg = (String) in.readObject();
			System.out.println(msg);
			
			fromClient = (Map<Object, Long>) in.readObject();
		}
		catch (IOException e) {
			System.err.println("Could not initialize client server...");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Could not get the message from Reducer...");
			e.printStackTrace();
		}
		finally {
			try {
				out.close();
				in.close();
				reducer.close();
			}
			catch (IOException e) {
				System.err.println("Could not close streams...");
			}
		}
	}
	
	public void printResults() {
		System.out.println("\n-----FINAL RESULTS FROM REDUCER-----");
		System.out.println();
		for(Object key : fromClient.keySet())
        {
             System.out.println("POI: " + key + " Count of Checkin: " +fromClient.get(key));			   
        }
	}
	
	public void requestAndConnect() {
		ConnectToMapper map1 = new ConnectToMapper(initCoordinates(1, minX, maxX, minY, maxY), dates, addr_ports.get(0), Integer.parseInt(addr_ports.get(1))); //Client1
		ConnectToMapper map2 = new ConnectToMapper(initCoordinates(2, minX, maxX, minY, maxY), dates, addr_ports.get(2), Integer.parseInt(addr_ports.get(3))); //Client2
		ConnectToMapper map3 = new ConnectToMapper(initCoordinates(3, minX, maxX, minY, maxY), dates, addr_ports.get(4), Integer.parseInt(addr_ports.get(5))); //Client3
		
		map1.start();
		map2.start();
		map3.start();
	}
	
	private void selectInput() {
		System.out.println("\n1) Default Values for testing");
		System.out.println("2) Define your own values");
		System.out.print(">");
		
		int choice = input.nextInt();
		
		do {
			if (choice == 1) {
				queryValues(true);
				break;
			}
			else if (choice == 2) {
				queryValues(false);
				break;
			}
			else {
				System.out.println("Choose one of the available options");
				choice = input.nextInt();
			}
		} while (true);
	}
	
	private void queryValues(boolean defaultValues) {		
		if (defaultValues) {
			this.minX  = -74.0144996501386;
			this.maxX = -73.9018372248612;
			this.minY = 40.67747711364791;
			this.maxY = 40.76662365086325;
			initDate(defaultValues, "", "");
		}
		else {
			System.out.println("Enter the minimun Longitude: ");
			System.out.print(">");
			minX = input.nextDouble();
			
			System.out.println("Enter the maximum Longitude: ");
			System.out.print(">");
			maxX = input.nextDouble();
			
			System.out.println("Enter the minimun Latitude: ");
			System.out.print(">");
			minY = input.nextDouble();
			
			System.out.println("Enter the maximum Latitude: ");
			System.out.print(">");
			maxY = input.nextDouble();
			
			System.out.println("Enter the minimun Date: ");
			System.out.print(">");
			input.next();
			fromDate = input.nextLine();
			
			System.out.println("Enter the maximum Date: ");
			System.out.print(">");
			toDate = input.nextLine();
			
			initDate(false, fromDate, toDate);
		}
	}
	
	private static List<Double> initCoordinates (int k, double minX, double maxX, double minY, double maxY) { //this method is used for passing coordinates values to mappers	
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
	
	private void initDate (boolean def, String min, String max) {
		String minDate, maxDate;
		
		if (def) {
			minDate = "2012-05-09 00:00:00";
			maxDate = "2012-11-06 23:59:00";
		}
		else {
			minDate = min;
			maxDate =max;
		}
		this.dates[0] = minDate;
		this.dates[1] = maxDate;
	}
}
