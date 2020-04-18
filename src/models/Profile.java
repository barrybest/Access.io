package models;

import java.util.ArrayList;

public class Profile {
	private String name;
	private String city;
	private double stars;
	private boolean verified;
	private String handicap;
	private String picture;
	private ArrayList<Review> reviews;
	
	public Profile(String name,
			String city,
			double stars,
			boolean verified,
			String handicap,
			String picture,
			ArrayList<Review> reviews) {
		this.name = name;
		this.city = city;
		this.reviews = reviews;
		this.stars = stars;
		this.verified = verified;
		this.handicap = handicap;
		this.picture =  picture;
	}
}
