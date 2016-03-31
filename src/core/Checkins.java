package core;

public class Checkins {

	private String POI;
	private int POI_sum;
	
	public Checkins(String POI, int POI_sum) {
		this.POI = POI;
		this.POI_sum = POI_sum;
	}

	public String getPOI() {
		return POI;
	}

	public void setPOI(String pOI) {
		POI = pOI;
	}

	public int getPOI_sum() {
		return POI_sum;
	}

	public void setPOI_sum(int pOI_sum) {
		POI_sum = pOI_sum;
	}
	
	public String print() {
		return this.POI+"|"+this.POI_sum;
	}
	
	public int count(String poi) {
		int count = 0;
		if (this.POI == poi) {
			count++;
		}
		return count;
	}
}
