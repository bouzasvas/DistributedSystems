package core;

import java.util.Map;
import java.net.*;
import java.io.*;

public class Reducer implements ReduceWorker {
	
	private int reducerPort;
	ServerSocket reducer = null;
	Socket client = null;
	
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
//			while (true) {
				try {
					reducer = new ServerSocket(reducerPort);
					System.out.println("Running on local port "+reducer.getLocalPort()+" and waiting for connections..");
				} catch (IOException e) {
					System.err.println("Could not initialize Reduce Server");
					e.printStackTrace();
				}
				try {
					client = reducer.accept();
				}
				catch (IOException e) {
					System.err.println("Problem while trying to connect to Reducer");
				}
				
				waitForTasksThread();
//			}
	}

	@Override
	public void waitForTasksThread() {	
		Runnable requestsRunnable = new Runnable() {
			public void run() {
				ObjectInputStream input = null;
				ObjectOutputStream output = null;
				synchronized (client) {		
					try {
						input = new ObjectInputStream(client.getInputStream());
						output = new ObjectOutputStream(client.getOutputStream());
					}
					catch (IOException e) {
						System.err.println("Could not initialize IO objects");
						e.printStackTrace();
					}
					
					try {
						String fromMapper = (String) input.readObject();
						System.out.println(fromMapper);
					}
					catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
					
//					try {						
//						String msg = "Successfully connected to "+client.getInetAddress()+" on port: "+client.getPort();
//						output.writeObject(msg);
//						output.flush();
//					} catch (IOException e) {
//						System.err.println("Could not send reply to client");
//					}
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
