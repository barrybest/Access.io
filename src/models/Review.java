package models;

public class Review {
	private String title;
	private String body;
	private double elevatorRating;
	private double rampRating;
	private double doorRating;
	private double otherRating;
	String userName;
	private int upvotes;
	private int downvotes;
	String locationName;
	String image;
	
public Review(String title,
			  String body,
			  double elevatorRating,
			  double rampRating,
			  double doorRating,
			  double otherRating,
			  String userName,
			  int upvotes,
			  int downvotes,
			  String locationName,
			  String image) {
		this.title = title;
		this.body = body;
		this.elevatorRating = elevatorRating;
		this.rampRating = rampRating;
		this.doorRating = doorRating;
		this.otherRating = otherRating;
		this.userName = userName;
		this.upvotes = upvotes;
		this.downvotes = downvotes;
		this.locationName = locationName;
		this.image = image;
	}
}

