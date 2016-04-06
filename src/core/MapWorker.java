package core;
import java.util.List;
import java.util.Map;

public interface MapWorker extends Worker {
	public List<Map.Entry<Object, Long>> map(List<ListOfCheckins> checkins);
	public void notifyMaster();
	public void sendToReducers(List<Map.Entry<Object, Long>> toReducer);
}
