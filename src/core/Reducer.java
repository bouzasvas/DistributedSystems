package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.net.*;
import java.io.*;

public class Reducer implements ReduceWorker {
	
	private int reducerPort;
	ServerSocket reducer = null;
	Socket client = null;
	
	static int mapsArrived;
	List<Map<Object, Long>> fromMapper = new ArrayList<Map<Object, Long>>();
	
	public Reducer(int port) {
		if (checkPortAvailability(port))
			this.reducerPort = port;
		else {
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
					System.out.println("Getting results from Mapper "+mapsArrived);
					@SuppressWarnings("unchecked")
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
				//synchronized (client) {
					receiveDataFromMap();
					if (mapsArrived == 3) {
						for (Map<Object, Long> map: fromMapper) {
							for(Object key : map.keySet())
						        {
						             System.out.println(key + " : " +map.get(key));			   
						        }				
						}
					}
						//reduce(fromMapper);
				//}
			}
		};
		Thread request = new Thread(requestsRunnable);
		request.start();
	}

	@Override
	public void waitForMasterAck() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Integer, Object> reduce(List<Map<Object, Long>> listOfMaps) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendResults(Map<Integer, Object> toClient) {
		// TODO Auto-generated method stub
		
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
