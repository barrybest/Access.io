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
		int locationID = Integer.parseInt(request.getParameter("locationID"));
		
		// Return all information regardless of what the request is for, just so it's stored and simple
		// (Question: What do we return if the location isn't in our database yet?)
		SQLCalls locationCall = new SQLCalls();
		String locationName = locationCall.locationToName(locationID);
		String locationAddress = locationCall.locationToAddress(locationID);
		ArrayList<Integer> locationRatings = locationCall.locationToAverages(locationID);
		String locationPhone = locationCall.locationToPhone(locationID);
		String locationSite = locationCall.locationToURL(locationID);
		
		//leave review -- insert user review into our database
		
		//leave rating -- insert user rating into our database
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
