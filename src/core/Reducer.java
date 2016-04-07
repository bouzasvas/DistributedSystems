package core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.net.*;
import java.io.*;

public class Reducer implements ReduceWorker {
	
	private String clientAddress;
	private int clientPort;
	
	private int reducerPort;
	ServerSocket reducer = null;
	Socket client = null;
	
	static int mapsArrived;
	
	List<Map<Object, Long>> fromMapper = new ArrayList<Map<Object,Long>>();
	
	public Reducer(int port) {
		if (checkPortAvailability(port))
			this.reducerPort = port;
		else {
			System.out.println("Port "+port+" is in use!");
			System.exit(-1);
		}
	}
	
	public Reducer(int port, String clientAddress, int clientPort) {
		if (checkPortAvailability(port)) {
			this.reducerPort = port;
			this.clientAddress = clientAddress;
			this.clientPort = clientPort;
		} else {
			System.out.println("Port "+port+" is in use!");
			System.exit(-1);
		}
	}

	@Override
	public void initialize() {
		
		try {
			this.mapsArrived = 0;
			reducer = new ServerSocket(reducerPort);
			System.out.println("Running on local port "+reducer.getLocalPort()+" and waiting for connections..");
			
			while (true) {	
				client = reducer.accept();
				mapsArrived++;
				waitForTasksThread();
			}
		}
		catch (IOException e) {
			System.err.println("Could not initialize Reduce Server...");
		}
	}

	public void receiveDataFromMap() {
		ObjectInputStream input = null;
		ObjectOutputStream output = null;	
			try {
			output = new ObjectOutputStream(client.getOutputStream());
			input = new ObjectInputStream(client.getInputStream());
			}
			catch (IOException e) {
				System.err.println("Could not initialize IO objects");
				e.printStackTrace();
			}
			//synchronized (input) {
				try {
					Map<Object, Long> dataFromMap = (Map<Object, Long>) input.readObject();
					fromMapper.add(dataFromMap);
					
//						for(Object key : fromMapper.keySet())
//				        {
//				             System.out.println(key + " : " +fromMapper.get(key));			   
//				        }
				}
				catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				try {						
					String msg = "Successfully connected to "+client.getInetAddress()+" on port: "+client.getPort();
					output.writeObject(msg);
					output.flush();
				} catch (IOException e) {
					System.err.println("Could not send reply to client");
				}
			//}
		}
	
	@Override
	public void waitForTasksThread() {	
		Runnable requestsRunnable = new Runnable() {
			public void run() {
				System.out.println("Getting results from Mapper "+mapsArrived+"...\n");
					receiveDataFromMap();
					if (mapsArrived == 3) {
						//printMapValues();
						reduce(fromMapper);
						//sendResults(reduce(fromMapper));
					}
					//sendResults(reduce(fromMapper));
			}
		};
		Thread request = new Thread(requestsRunnable);
		request.start();
	}
	
	public void printMapValues() {
		for(Map<Object, Long> item : fromMapper){
			System.out.println("*************************");
			for (Entry e : item.entrySet()) {
				System.out.println("POI: "+e.getKey()+"\tCount: "+e.getValue());;
			}
        }
	}

	@Override
	public void waitForMasterAck() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Object, Long> reduce(List<Map<Object, Long>> fromMapper) {
        Map<Object, Long> toClient = new LinkedHashMap<Object, Long>();
        toClient = fromMapper.stream()
        		.reduce(toClient ,(o1,o2)->
        		{o1.putAll(o2);
                 return o1;});
        
        toClient = toClient.entrySet().stream().sorted(Map.Entry.comparingByValue((v1,v2)->v2.compareTo(v1)))
	        .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (x,y)-> {throw new AssertionError();}, LinkedHashMap::new));
        
       for (Entry<Object, Long> entry : toClient.entrySet()) {
    	   System.out.println("POI: "+entry.getKey()+"\tCount: "+entry.getValue());
       }
        
        return toClient;
    }

	@Override
	public void sendResults(Map<Object, Long> toClient) {
		Socket toClientSocket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		
		try {
			toClientSocket = new Socket(InetAddress.getByName(clientAddress), clientPort);
			out = new ObjectOutputStream(toClientSocket.getOutputStream());
			in = new ObjectInputStream(toClientSocket.getInputStream());
			
			out.writeObject("Hello from Reducer");
			out.flush();
		}
		catch (IOException e) {
			System.err.println("Could not connect to client...");
			e.printStackTrace();
		}
		finally {
			if (!(toClientSocket.getInetAddress().equals("localhost"))) {
				try {
					in.close();
					out.close();
					toClientSocket.close();
				} catch (IOException e) {
					System.err.println("Could not close streams...");
				}
			}
		}
	}
	
	public boolean checkPortAvailability(int port) {
		ServerSocket available = null;
		try {
			available = new ServerSocket(port);
			available.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
