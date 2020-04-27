package models;

public class Review {
	private String title;
	private String body;
	private String userName;
	private int upvotes;
	private int downvotes;
	private String locationName;
	
	public Review(String title, 
			String body, 
			String userName, 
			int upvotes, 
			int downvotes, 
			String locationName) {
		this.title = title;
		this.body = body;
		this.userName = userName;
		this.upvotes = upvotes;
		this.downvotes = downvotes;
		this.locationName = locationName;
	}
}

