package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

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
		PrintWriter pw = response.getWriter();
		//////* TESTING TO GET VALUES FROM MYSQL DB	*///////
		if(locationID != null && userID != null){
			reviewTitle = ReviewCalls.reviewToTitle(locationID, userID);
			reviewBody = ReviewCalls.reviewToBody(locationID, userID);
			int upvote = ReviewCalls.reviewToUpvote(locationID, userID);
			int downvote = ReviewCalls.reviewToDownvote(locationID, userID);
			String getrating = ReviewCalls.reviewToRating(locationID, userID);
			// Output error to user that they can only leave one review per location....
			//TEST OUTPUT
			System.out.println(reviewTitle);
			System.out.println(reviewBody);
			System.out.println(upvote);
			System.out.println(downvote);
			System.out.println(getrating);
		}
		
		//NOTE FOR FRONTEND: these are the names of the buttons to submit forms
		//1. name="submitReview" is for submitting a review
		//2. name="upvote" is for upvoting
		//3. name="downvote" for downvoting
		String requestType = request.getParameter("requestType");
		
		if(requestType.contentEquals("submitReview")){
			String newtitle = request.getParameter("title");
			String newbody = request.getParameter("body");
			double rating = Double.parseDouble(request.getParameter("rating"));
			ReviewCalls.addReview(locationID, userID, newtitle, newbody, rating);
		}
		else if (requestType.contentEquals("upvote")) {
			int currupvote = ReviewCalls.reviewToUpvote(locationID, userID);
			currupvote++;
			ReviewCalls.addUpvote(locationID, currupvote, userID);
		}
		//if downvote, increase the downvote count in database, dependent upon whether or not user has already downvoted before
		else if (requestType.contentEquals("downvote")) {
			int currdownvote = ReviewCalls.reviewToUpvote(locationID, userID);
			currdownvote--;
			ReviewCalls.addDownvote(locationID, currdownvote, userID);
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
