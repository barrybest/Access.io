package models;

import java.util.ArrayList;

public class Location {
	public int locationID;
	public ArrayList<Integer> reviews;
	
	public Location(int locID) {
		this.locationID = locID;
	}
	
	public String getName(int locationID) {
		String name = "";
		// Use MySQL here
		return name;
	}
	
	public String getAddress(int locationID) {
		String address = "";
		// Use MySQL here
		return address;
	}
	
	public ArrayList<Integer> getAverages(int locationID) {
		ArrayList<Integer> averages = new ArrayList<Integer>();
		// Use MySQL here
		return averages;
	}
	
	public int getPhone(int locationID) {
		int phoneNum = 0;
		// Use MySQL here
		return phoneNum;
	}
	
	public String getURL(int locationID) {
		String URL = "";
		// Use MySQL here
		return URL;
	}
}