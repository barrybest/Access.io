package models;

public class Review {
	private String title;
	private String body;
	private double rating;
	private String userName;
	private int upvotes;
	private int downvotes;
	private Location location;
	
	public Review(String title, String body, double rating, String userName, int upvotes, int downvotes, Location location) {
		this.title = title;
		this.body = body;
		this.rating = rating;
		this.userName = userName;
		this.upvotes = upvotes;
		this.downvotes = downvotes;
		this.location = location;
	}
}

