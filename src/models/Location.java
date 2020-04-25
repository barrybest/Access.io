package models;

import java.util.ArrayList;

public class Location {
	String name;
	private String address;
	private String phoneNumber;
	private String website;
	private double elevatorRating;
	private double rampRating;
	private double doorRating;
	private double otherRating;
	private ArrayList<Review> reviews;
	
	// Return information for a Location object
	public Location(String name,
					String address,
					String phoneNumber,
					String website,
					double elevatorRating,
					double rampRating,
					double doorRating,
					double otherRating,
					ArrayList<Review> reviews) {
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.website = website;
		this.elevatorRating = elevatorRating;
		this.rampRating = rampRating;
		this.doorRating = doorRating;
		this.otherRating = otherRating;
		this.reviews = reviews;
		if (address == null) this.address = "";
		if (phoneNumber == null) this.phoneNumber = "";
		if (website == null) this.website = "";
	}
}