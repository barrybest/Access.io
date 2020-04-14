package server;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.SQLCalls;

/**
 * Servlet implementation class LocServ
 */
@WebServlet("/LocServ")
public class LocServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LocServ() {
        super();
    }

    //need to do figure out where we will take the averages of the ratings for each location
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Pass in location identifier to grab information
		// (Question: Will we be passing in locationID or a MapKit identifier like lat/long?)
		String locationID = request.getParameter("locationID");
		
		// Connect to MySQL database
		SQLCalls locationCall = new SQLCalls();
		
		// Return all information regardless of what the request is for, just so it's stored and simple
		if (locationID != null) { // If location is in database already
			String locationName = locationCall.locationToName(locationID);
			String locationAddress = locationCall.locationToAddress(locationID);
			ArrayList<String> locationRatings = locationCall.locationToAverages(locationID);
			String locationPhone = locationCall.locationToPhone(locationID);
			String locationSite = locationCall.locationToURL(locationID);
			
			/* Test output */
			System.out.println(locationName);
			System.out.println(locationAddress);
			System.out.println(locationPhone);
			System.out.println(locationSite);
		} else { // If not, let's add it
			// DISCUSS HOW WE'RE GOING TO DO THIS WITH FRONT-END TEAM
		}
		
		String requestType = request.getParameter("requestType");
		
		// Leave review -- insert user review into our database
		if (requestType.equalsIgnoreCase("review")) {
			String userID = request.getParameter("userID");
			String review = request.getParameter("review");
			locationCall.leaveReview(locationID, userID, review);
		}
		
		// Leave rating -- insert user rating into our database
		if (requestType.equalsIgnoreCase("rating")) {
			String userID = request.getParameter("userID");
			String elevatorRating = request.getParameter("elevator");
			String rampRating = request.getParameter("ramp");
			String doorRating = request.getParameter("door");
			String otherRating = request.getParameter("other");
			locationCall.leaveRating(locationID, userID, elevatorRating, rampRating, doorRating, otherRating);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
