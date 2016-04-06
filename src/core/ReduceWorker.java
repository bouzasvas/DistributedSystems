package core;
import java.util.List;
import java.util.Map;

public interface ReduceWorker extends Worker {
	public void waitForMasterAck();
	public Map<Integer, Object> reduce(List<List<Map.Entry<Object, Long>>> listOfMaps);
	public void sendResults(Map<Integer,Object> toClient);

}
