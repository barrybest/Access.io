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
		
		// Pass in location name to grab information
		String locationName = request.getParameter("locationName");
		
		// Connect to MySQL database
		SQLCalls locationCall = new SQLCalls();
		
		// Return locationID if successful, -1 if not
		int locationID = locationCall.verifyLocation(locationName);
		
		// Return all information regardless of what the request is for, just so it's stored and simple
		if (locationID != -1) { // If location is in database already
			// LocationInfo is JSON object containing all location parameters
			String locationInfo = locationCall.getLocation(locationID);
			
		} else { // If not, let's add it
			
		}
		
		/* idk what we're doing with ratings rn
		String requestType = request.getParameter("requestType");
		
		// Leave rating -- insert user rating into our database
		if (requestType.equalsIgnoreCase("rating") && requestType != null) {
			String userID = request.getParameter("userID");
			String elevatorRating = request.getParameter("elevator");
			String rampRating = request.getParameter("ramp");
			String doorRating = request.getParameter("door");
			String otherRating = request.getParameter("other");
			locationCall.leaveRating(locationID, userID, elevatorRating, rampRating, doorRating, otherRating);
		}
		*/
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
