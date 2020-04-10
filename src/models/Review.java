package models;

public class Review {
	public int reviewID;
	public int userID;
	public int upvotecount;
	public int downvotecount;
	public Location location;
	public String reviewtitle;
	public String reviewbody;
	
	public Review(int reviewID, Location location, String reviewtitle, String reviewbody){
		this.reviewID = reviewID;
		this.location = location;
		this.reviewtitle = reviewtitle;
		this.reviewbody = reviewbody;
	}
	
	//get UserID of the reviewer for specified review
	public int getUserID(int reviewID){
		return userID;
	}
	
	//upvote and downvote
	public void upvote(int reviewID){
		upvotecount++;
	}
		public void downvote(int reviewID){
		downvotecount++;
	}
	
	//get and set the title of review
	public String getTitle(int reviewID){
		return reviewtitle;
	}
	public void setTitle(int reviewID, String reviewtitle){
		this.reviewtitle = reviewtitle;
	}
	
	//get and set body of review
	public String getBody(int reviewID){
		return reviewbody;
	}
	public void setBody(int reviewID, String reviewbody){
		this.reviewbody = reviewbody;
	}
}
