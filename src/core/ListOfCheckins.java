package core;

import java.util.ArrayList;
import java.util.List;

public class ListOfCheckins {

	private List<Checkin> checkinsList;
	
	public ListOfCheckins() {
		checkinsList = new ArrayList<Checkin>();
	}
	
	public void addCheckin(String pOI, String pOI_name, String pOI_category, String pOI_category_id, double longtitude,
			double latitude, String datetime, String photoURL) {
		checkinsList.add(new Checkin(pOI, pOI_name, pOI_category, pOI_category_id, longtitude, latitude, datetime, photoURL));
	}
	
	public Checkin getCheckin(int index) {
		return checkinsList.get(index);
	}
	
	public int count(String POI) {
		int checkinNumber = 0;
		for (Checkin ch : checkinsList) {
			if (ch.getPOI().equals(POI))
				checkinNumber++;
		}
		return checkinNumber;
	}
	
	public void printCheckins() {
		for (Checkin checkin : checkinsList ) {
			checkin.printCheckin();
		}
	}

	public void printCheckins(String POI) {
		for (Checkin checkin : checkinsList ) {
			if (checkin.getPOI().equals(POI))
					checkin.printCheckin();
		}
	}
	  public List<Checkin> getList(){
	        return this.checkinsList;
	    }
	
//	public int count(String poi) {
//		int count = 0;
//		if (this.POI == poi) {
//			count++;
//		}
//		return count;
//	}
}
