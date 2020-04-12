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
		PrintWriter pw = response.getWriter();
		//log in -- returning user
		if(requestType.equals("login") && requestType != null) {
			//check to see if user exists
			String username = request.getParameter("userName");
			String password = request.getParameter("password");
			pw.println("Logging in user: " + username);
			//if user exists -- log in, grant access to webpage
			
			//if username exists but the password does not match --- "Incorrect password."
			
			//if username does not exist --- "This username does not exist, please create an account."
		}
		
		//register -- new user
		else if(requestType.equals("register")) {
			//check to see if user exists
			String username = request.getParameter("userName");
			String password = request.getParameter("password");
			pw.println("registering the user: " + username);
			//check database, if user exists, return error message --- "User already exists."
			
			//
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
