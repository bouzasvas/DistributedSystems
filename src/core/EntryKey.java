package core;

import java.util.List;

public class EntryKey {
	
	private Object namePOI;
	private List<String> photos;
	private double Longitude, Latitude;
	private int count;
	
	
	public EntryKey(Object name, List<String> photos, double Longtitude, double Latitude){
		this.namePOI = name;
		this.photos = photos;
		this.Longitude = Longtitude;
		this.Latitude = Latitude;
		this.setCount(photos.size());
	}


	public List<String> getPhotos() {
		return photos;
	}


	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}


	public Object getNamePOI() {
		return namePOI;
	}


	public void setNamePOI(Object namePOI) {
		this.namePOI = namePOI;
	}


	public double getLatitude() {
		return Latitude;
	}


	public void setLatitude(double latitude) {
		Latitude = latitude;
	}


	public double getLongitude() {
		return Longitude;
	}


	public void setLongitude(double longitude) {
		Longitude = longitude;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}

}
