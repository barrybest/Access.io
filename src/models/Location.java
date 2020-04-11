package models;

import java.util.ArrayList;

public class Location {
	public int locationID;
	public ArrayList<Integer> reviews;
	
	public Location(int locID) {
		this.locationID = locID;
	}
	
	// Return the name of a location
	public String getName(int locationID) {
		String name = "";
		name = SQLCalls.locationToName(locationID);
		return name;
	}
	
	// Return the address of a location
	public String getAddress(int locationID) {
		String address = "";
		address = SQLCalls.locationToAddress(locationID);
		return address;
	}
	
	// Return the average ratings of a location in an array list
	public ArrayList<Integer> getAverages(int locationID) {
		ArrayList<Integer> averages = new ArrayList<Integer>();
		averages = SQLCalls.locationToAverages(locationID);
		return averages;
	}
	
	// Return the phone number of a location as a String
	public String getPhone(int locationID) {
		String phoneNum = "";
		phoneNum = SQLCalls.locationToPhone(locationID);
		return phoneNum;
	}
	
	// Return the website URL of a location
	public String getURL(int locationID) {
		String URL = "";
		URL = SQLCalls.locationToURL(locationID);
		return URL;
	}
}