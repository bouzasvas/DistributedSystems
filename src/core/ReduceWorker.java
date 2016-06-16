package core;
import java.util.List;
import java.util.Map;

public interface ReduceWorker extends Worker {
	public void waitForMasterAck();
	//Map<Object, POI_Photos> reduce(List<Map<Object, POI_Photos>> fromMapper);
	//void sendResults(Map<Object, POI_Photos> toClient);
	Map<Object, List> reduce(List<Map<Object, List>> fromMapper);
	void sendResults(Map<Object, List> map);

}
