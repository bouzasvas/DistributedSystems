package core;
import java.util.List;
import java.util.Map;

public interface MapWorker extends Worker {
	public Map<String, Integer> map(List<ListOfCheckins> checkins);
	public void notifyMaster();
	public void sendToReducers(Map<String, Integer> toReducer);
}
