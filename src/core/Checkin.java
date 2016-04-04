package core;

import java.util.List;

public class Checkin {

	//only mapper "talks" to server
	//so you have to keep all the values that we need to return to client
	
	private String POI, POI_name, POI_category, POI_category_id;
	private double Longitude, Latitude;
	private String datetime;
	private String photoURL;
	
	public Checkin(String pOI, String pOI_name, String pOI_category, String pOI_category_id, double latitude,
			double longitude, String datetime, String photoURL) {
		POI = pOI;
		POI_name = pOI_name;
		POI_category = pOI_category;
		POI_category_id = pOI_category_id;
		Longitude = longitude;
		Latitude = latitude;
		this.datetime = datetime;
		this.photoURL = photoURL;
	}
	
	public String getPOI() {
		return this.POI;
	}
	
	public String getPOI_name() {
		return POI_name;
	}

	public void setPOI_name(String pOI_name) {
		POI_name = pOI_name;
	}

	public String getPOI_category() {
		return POI_category;
	}

	public void setPOI_category(String pOI_category) {
		POI_category = pOI_category;
	}

	public String getPOI_category_id() {
		return POI_category_id;
	}

	public void setPOI_category_id(String pOI_category_id) {
		POI_category_id = pOI_category_id;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getPhotoURL() {
		return photoURL;
	}

	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	public void setPOI(String pOI) {
		POI = pOI;
	}

	public String printCheckin() { //TO-SEE method return type
		return POI+"|"+POI_name+"|"+POI_category+"|"+POI_category_id+"|"+Latitude+"|"+Longitude+"|"+datetime+"|"+photoURL;
	}
}
