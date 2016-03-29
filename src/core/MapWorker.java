package core;
import java.util.Map;

public interface MapWorker extends Worker {
	public Map<Integer, Object> map(Object key, Object value);
	public void notifyMaster();
	public void sendToReducers(Map<Integer, Object> toReducer);
}
