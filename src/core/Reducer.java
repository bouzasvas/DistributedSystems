package core;

import java.util.Map;
import java.net.*;
import java.io.*;

public class Reducer implements ReduceWorker {
	
	private int reducerPort;
	ServerSocket reducer = null;
	Socket client = null;
	
	Map<Object, Long> fromMapper = null;
	
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
			reducer = new ServerSocket(reducerPort);
			System.out.println("Running on local port "+reducer.getLocalPort()+" and waiting for connections..");
			
			while (true) {	
				client = reducer.accept();
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
			try {
				fromMapper = (Map<Object, Long>) input.readObject();
				
				for(Object key : fromMapper.keySet())
			        {
			             System.out.println(key + " : " +fromMapper.get(key));			   
			        }
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
		}
	
	@Override
	public void waitForTasksThread() {	
		Runnable requestsRunnable = new Runnable() {
			public void run() {
				synchronized (client) {
					receiveDataFromMap();
				}
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
	public Map<Integer, Object> reduce(int key, Object value) {
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
