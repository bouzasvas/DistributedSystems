import java.util.Map;

public interface ReduceWorker {
	public void waitForMasterAck();
	public Map<Integer, Object> reduce(int key, Object value);
	public void sendResults(Map<Integer,Object> toClient);

}
