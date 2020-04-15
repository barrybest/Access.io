package models;

public class Review {
	public int reviewID;
	public String userID;
	public String locationID;
	public int upvotecount;
	public int downvotecount;
	public Location location;
	public String reviewtitle;
	public String reviewbody;
	
	public Review(String userID, String locationID, String reviewtitle, String reviewbody){
		this.userID = userID;
		this.locationID = locationID;
		this.reviewtitle = reviewtitle;
		this.reviewbody = reviewbody;
	}
}

