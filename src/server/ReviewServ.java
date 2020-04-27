package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import models.Review;
import models.SQLCalls;


@WebServlet("/ReviewServ")
public class ReviewServ extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ReviewServ() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Need to map the review to a location and a User
		String locationID = request.getParameter("locationID");
		String userID = request.getParameter("userID");  //maybe getAttribute()?? since user won't be changing often
														 //so maybe we can session.setAttribute("userID", userID) 

		// Connect to MySQL database
		SQLCalls ReviewCalls = new SQLCalls();
		String reviewTitle = "";
		String reviewBody = "";
		
		// Communicate with front end
		PrintWriter pw = response.getWriter();
		
		if(locationID != null && userID != null){
			reviewTitle = ReviewCalls.reviewToTitle(locationID, userID);
			reviewBody = ReviewCalls.reviewToBody(locationID, userID);
			String userName = ReviewCalls.reviewToName(userID);
			String locName = ReviewCalls.reviewToLocation(locationID);
			int upvote = ReviewCalls.reviewToUpvote(locationID, userID);
			int downvote = ReviewCalls.reviewToDownvote(locationID, userID);
			double elevatorRating = ReviewCalls.reviewToElevatorRating(locationID, userID);
			double rampRating = ReviewCalls.reviewToRampRating(locationID, userID);
			double doorRating = ReviewCalls.reviewToDoorRating(locationID, userID);
			double otherRating = ReviewCalls.reviewToOtherRating(locationID, userID);
			
			Review review = new Review(reviewTitle, reviewBody, elevatorRating, rampRating, doorRating, otherRating,
					userName, upvote, downvote, locName);
		
			Gson gson = new Gson();
			String jsonReview = gson.toJson(review);
			
			// Send JSON to front end
			pw.println(jsonReview);
		}
		
		//NOTE FOR FRONTEND: these are the names of the buttons to submit forms
		//1. name="submit" is for submitting a review; return 1 if success
		//2. name="upvote" is for upvoting; return 2 if success
		//3. name="downvote" for downvoting; return 3 if success
    
 /*  ORIGINAL CODE of lines 39-62 WITHOUT ADDING OTHER RATINGS
// 		PrintWriter pw = response.getWriter();
// 		//////* TESTING TO GET VALUES FROM MYSQL DB	*///////
// 		if(locationID != null && userID != null){
// 			reviewTitle = ReviewCalls.reviewToTitle(locationID, userID);
// 			reviewBody = ReviewCalls.reviewToBody(locationID, userID);
// 			int upvote = ReviewCalls.reviewToUpvote(locationID, userID);
// 			int downvote = ReviewCalls.reviewToDownvote(locationID, userID);
// 			String getrating = ReviewCalls.reviewToRating(locationID, userID);
// 			// Output error to user that they can only leave one review per location....
// 			//TEST OUTPUT
// 			System.out.println(reviewTitle);
// 			System.out.println(reviewBody);
// 			System.out.println(upvote);
// 			System.out.println(downvote);
// 			System.out.println(getrating);
// 		}
		
		//NOTE FOR FRONTEND: these are the names of the buttons to submit forms
		//1. name="submitReview" is for submitting a review
		//2. name="upvote" is for upvoting
		//3. name="downvote" for downvoting
		String requestType = request.getParameter("requestType");
		
		if(requestType.contentEquals("submitReview")){
			String newtitle = request.getParameter("title");
			String newbody = request.getParameter("body");
			double elevatorRating = Double.parseDouble(request.getParameter("elevatorRating"));
			double rampRating = Double.parseDouble(request.getParameter("rampRating"));
			double doorRating = Double.parseDouble(request.getParameter("doorRating"));
			double otherRating = Double.parseDouble(request.getParameter("otherRating"));
			ReviewCalls.addReview(locationID, userID, newtitle, newbody, elevatorRating, rampRating, doorRating, otherRating);
			pw.println("1");
		}
		else if (requestType.contentEquals("upvote")) {
			int currupvote = ReviewCalls.reviewToUpvote(locationID, userID);
			currupvote++;
			ReviewCalls.addUpvote(locationID, currupvote, userID);
			pw.println("2");
		}
		//if downvote, increase the downvote count in database, dependent upon whether or not user has already downvoted before
		else if (requestType.contentEquals("downvote")) {
			int currdownvote = ReviewCalls.reviewToUpvote(locationID, userID);
			currdownvote--;
			ReviewCalls.addDownvote(locationID, currdownvote, userID);
			pw.println("3");
		}
		//get upvote and downvotes
		else if(requestType.contentEquals("getUpvote")){
			pw.println(ReviewCalls.reviewToUpvote(locationID, userID));
		}
		else if(requestType.contentEquals("getDownvote")){
			pw.println(ReviewCalls.reviewToDownvote(locationID, userID));
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
