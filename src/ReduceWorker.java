import java.util.Map;

public interface ReduceWorker extends Worker {
	public void waitForMasterAck();
	public Map<Integer, Object> reduce(int key, Object value);
	public void sendResults(Map<Integer,Object> toClient);

}
