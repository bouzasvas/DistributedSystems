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
	
	public MasterGUI(int function, int id, int topKForClient) {
			initPorts();
		if (function == 1) {
			boolean def;
			if (id == 0)
				def = false;
			else
				def = true;
			client = new Client(addr_ports, def);
			client.setTopK(topKForClient);
			client.requestAndConnect();
			client.initServer();
		}
		else if (function == 2) {
			int port = 2*id-1;
			
			mapper = new Mapper(Integer.parseInt(addr_ports.get(port)), addr_ports.get(6), Integer.parseInt(addr_ports.get(7)));		
			mapper.initialize();
		}
		else if (function == 3) {
			Reducer reducer = new Reducer(Integer.parseInt(addr_ports.get(7)), addr_ports.get(8), Integer.parseInt(addr_ports.get(9)));
			reducer.initialize();
		}
	}

}
