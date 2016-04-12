package gui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JTextField;
import javax.swing.JTextPane;

import core.Mapper;
import core.Reducer;

public class MasterGUI {
	
	Scanner input = new Scanner(System.in);
	
	static Mapper mapper;
	static Reducer reducer;
	static Client client;
	
	int function, mapperID;
	
	static List<String> addr_ports = new ArrayList<String>();
	
	public void initPorts() { //this method initializes the List ports with the contents of file ports_numbers
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
	
	public MasterGUI(int funct, String[] args) {
		initPorts();
		if (funct == 1) {
			initClient(args);
		}
		else if (funct == 2) {
			initMapper(args);
		}
		else {
			initReducer();
		}
	}
	
	
	public void initClient(String[] args) {
		boolean def;
		int id = Integer.valueOf(args[0]);
		
		if (id == 0) {
			def = false;
			client = new Client(addr_ports, def);
			client.setTopK(Integer.valueOf(args[1]));
			double minX = Double.parseDouble(args[2]);
			double maxX = Double.parseDouble(args[3]);
			double minY = Double.parseDouble(args[4]);
			double maxY = Double.parseDouble(args[5]);
			String fromDate = args[6];
			String toDate = args[7];
			
			client.queryValues(def,minX , maxX, minY, maxY, fromDate, toDate);
		}
		else {
			def = true;
			client = new Client(addr_ports, def);
			client.setTopK(Integer.valueOf(args[1]));
		}
		client.requestAndConnect();
		client.initServer();
	}
	
	public void initMapper(String[] args) {
		int id = Integer.valueOf(args[0]);
		int port = 2*id-1;
		
		mapper = new Mapper(Integer.parseInt(addr_ports.get(port)), addr_ports.get(6), Integer.parseInt(addr_ports.get(7)));		
		mapper.initialize();
	}
	
	public void initReducer() {
		Reducer reducer = new Reducer(Integer.parseInt(addr_ports.get(7)), addr_ports.get(8), Integer.parseInt(addr_ports.get(9)));
		reducer.initialize();
	}
}
