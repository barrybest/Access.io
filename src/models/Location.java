package models;

import java.util.ArrayList;

public class Location {
	int locationID;
	String name;
	String address;
	ArrayList<Integer> reviews;
	String phoneNumber;
	String website;
	
	// Instantiate Location object
	public Location(int locID,
					String locName,
					String locAddress,
					ArrayList<Integer> locReviews,
					String locPhone,
					String locSite) {
		this.locationID = locID;
		this.name = locName;
		this.address = locAddress;
		this.reviews = locReviews;
		this.phoneNumber = locPhone;
		this.website = locSite;
	}
}