package server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		response.getWriter().append("Served at: ").append(request.getContextPath());
		//pass in the name of the location that is being requested...
		//String location = request.getParameter("locationName");
		//following parameters for the type of info being requested - contacts, ratings, reviews
		//String infoRequested = request.getParameter("infoRequested");
		//types of requests
		//general -- return everything
		
			//contacts -- return the contacts for the location
		
			//ratings -- return the average ratings for the location
		
			//reviews -- return all the reviews for the location
		
		//leave review -- insert user review into our database
		
		//leave rating -- insert user rating into our database
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
