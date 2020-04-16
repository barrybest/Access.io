package server;
import models.SQLCalls;

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
		String clientID = request.getParameter("userName"); //this could be email or username!
		//establish database connection!
		SQLCalls mysql = new SQLCalls();
		//print writer to respond to front-end
		PrintWriter pw = response.getWriter();
			//log in -- returning user
			//expected parameters -- username, password
			if(requestType.equals("login") && requestType != null) {
				//check to see if user exists
				if(mysql.verifyClient(clientID)) {
					//verify the password for this client is equal to the database token
					String token = request.getParameter("password");
					if(mysql.verifyToken(clientID, token)) {
						pw.println("Logging in user: " + clientID); //return userID for front-end
					}
					else
						pw.println("That is not the correct password for this user."); //return -1 for front-end
				}
				else 
					pw.println("This user does not exist."); //return -1 for front-end
			
			}
			//register -- new user
			//expected parameters -- email, username, name, password
			else if(requestType.equals("register") && requestType != null) {
				//check to see if user exists
				if(mysql.verifyClient(clientID))
					pw.println("This user already exists."); //return -1 for front-end
				//store new user in database -- needs the values
				else {
					String email = request.getParameter("email");
					String name = request.getParameter("name");
					String token = request.getParameter("password");
					int insertStatus = mysql.newUser(email, name, clientID, token); //1 if successful creation, -1 if problem with insert
					if(insertStatus == 1)
						pw.println("Successfully registered the user: " + clientID);
					else
						pw.println("Error registering the user: " + clientID);
				}
			}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
