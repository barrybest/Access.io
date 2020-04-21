package models;

import java.util.ArrayList;

public class Location {
	String name;
	private String address;
	private String phoneNumber;
	private String website;
	private double rating;
	private ArrayList<Review> reviews;
	
	// Return information for a Location object
	public Location(String name,
					String address,
					String phoneNumber,
					String website,
					double rating,
					ArrayList<Review> reviews) {
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.website = website;
		this.rating = rating;
		this.reviews = reviews;
	}
}