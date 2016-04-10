package gui;
import java.util.List;
import java.util.Map;

public interface ReduceWorker extends Worker {
	public void waitForMasterAck();
	public Map<Object, Long> reduce(List<Map<Object, Long>> listOfMaps);
	public void sendResults(Map<Object, Long> toClient);

}
