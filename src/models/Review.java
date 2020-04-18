package models;

public class Review {
	private String title;
	private String body;
	private double rating;
	String userName;
	private int upvotes;
	private int downvotes;
	String locationName;
	String image;
	
public Review(String title,
			  String body,
			  double rating,
			  String userName,
			  int upvotes,
			  int downvotes,
			  String locationName,
			  String image) {
		this.title = title;
		this.body = body;
		this.rating = rating;
		this.userName = userName;
		this.upvotes = upvotes;
		this.downvotes = downvotes;
		this.locationName = locationName;
		this.image = image;
	}
}

