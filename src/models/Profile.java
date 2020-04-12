package models;

import java.io.FileInputStream;
import java.util.ArrayList;

public class Profile {
	String name;
	String city;
	ArrayList<Review> reviews;
	double stars;
	boolean verified;
	String handicap;
	FileInputStream imageStream;
	
	public Profile(String name,
				   String city,
				   ArrayList<Review> reviews,
				   double stars,
				   boolean verified,
				   String handicap,
				   FileInputStream imageStream) {
		this.name = name;
		this.city = city;
		this.reviews = reviews;
		this.stars = stars;
		this.verified = verified;
		this.handicap = handicap;
		this.imageStream = imageStream;
	}
}
