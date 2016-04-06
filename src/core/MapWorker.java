package core;
import java.util.List;
import java.util.Map;

public interface MapWorker extends Worker {
	public List<Object> lst(List<ListOfCheckins> checkins);
	public void notifyMaster();
	public void sendToReducers(Map<Object, Long> toReducer);
}
