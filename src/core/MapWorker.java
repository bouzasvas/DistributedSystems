package core;
import java.util.Map;

public interface MapWorker extends Worker {
	public Map<String, Integer> map(Map<String, Integer> checkins);
	public void notifyMaster();
	public void sendToReducers(Map<Integer, Object> toReducer);
}
