package core;
import java.util.Map;
import java.net.*;

public class Reducer implements ReduceWorker {
	
	private int reducerPort;
	ServerSocket reducer = null;
	Socket client = null;
	
	public Reducer(int port) {
		this.reducerPort = port;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void waitForTasksThread() {
		// TODO Auto-generated method stub
		
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

}
