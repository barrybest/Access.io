package server;

import java.io.IOException;
import java.io.PrintWriter;

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
		
		// Communicate with front end
		PrintWriter pw = response.getWriter();
		
		// Return locationID if successful, -1 if not
		int locationID = locationCall.verifyLocation(locationName);
		
		if (locationID != -1) { // If location is in database already
			// LocationInfo is JSON object containing all location parameters
			String locationInfo = locationCall.getLocation(locationID);
			
			// Send JSON to front end
			pw.println(locationInfo);
			
		} else { // If location isn't in database...
			pw.println("Location doesn't exist.");
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
