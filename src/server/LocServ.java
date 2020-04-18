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
			
			// Test JSON
			System.out.println(locationInfo);
			
		} else { // If not, let's add it
			
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
