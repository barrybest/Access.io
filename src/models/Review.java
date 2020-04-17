package models;

public class Review {
	private String title;
	private String body;
	private String userName;
	private int upvotes;
	private int downvotes;
	private String locationName;
	private String image;
	
	public Review(String title, 
			String body, 
			String userName, 
			int upvotes, 
			int downvotes, 
			String locationName, 
			String image) {
		this.title = title;
		this.body = body;
		this.userName = userName;
		this.upvotes = upvotes;
		this.downvotes = downvotes;
		this.locationName = locationName;
		this.image = image;
	}
}

