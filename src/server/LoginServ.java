package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServ")
public class LoginServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginServ() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String requestType = request.getParameter("requestType");
		String clientID = request.getParameter("email"); //this could be email or username!
		
		//establish database connection!
		
		//check database to see if this username/email already exists...
		
		
		PrintWriter pw = response.getWriter();
			//log in -- returning user
			if(requestType.equals("login") && requestType != null) {
				//check to see if user exists
				String username = request.getParameter("userName");
				String password = request.getParameter("password");
				pw.println("Logging in user: " + username);
				//get info from database, verify that passwords are identical...
			
			}
			//register -- new user
			else if(requestType.equals("register")) {
				//check to see if user exists
				String username = request.getParameter("userName");
				String password = request.getParameter("password");
				pw.println("registering the user: " + username);
				//store new user in database -- needs the values
				
				//email
				//name
				//username
				//pasword
				
				//
			
			}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
