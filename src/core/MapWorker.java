package core;
import java.util.List;
import java.util.Map;

public interface MapWorker extends Worker {
	public Map<Object, POI_Photos> map(List<ListOfCheckins> checkins);
	public void notifyMaster();
	public void sendToReducers(Map<Object, POI_Photos> toReducer);
}
