package core;

import java.util.List;

public class POI_Photos {
	
	private Object POI;
	private Object POI_Name;
	private List<String> photos;
	private double Longitude, Latitude;
	private int count;
	
	
	public POI_Photos(Object POI, Object POI_Name, List<String> photos, double Longtitude, double Latitude){
		this.POI = POI;
		this.POI_Name = POI_Name;
		this.photos = photos;
		this.Longitude = Longtitude;
		this.Latitude = Latitude;
		this.setCount(photos.size());
	}


	public Object getPOI() {
		return POI;
	}


	public void setPOI(Object pOI) {
		POI = pOI;
	}


	public Object getPOI_Name() {
		return POI_Name;
	}


	public void setPOI_Name(Object pOI_Name) {
		POI_Name = pOI_Name;
	}


	public List<String> getPhotos() {
		return photos;
	}


	public void setPhotos(List<String> photos) {
		this.photos = photos;
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


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}

	public POI_Photos returnClass() {
		return this;
	}
	

}
