package server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.SQLCalls;

//Servlet implementation class ProfileServ
@WebServlet("/ProfileServ")
public class ProfileServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    // @see HttpServlet#HttpServlet()
    public ProfileServ() {
        super();
    }

	
	// @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestType = request.getParameter("requestType");
		String username = request.getParameter("Username");
		SQLCalls mysql = new SQLCalls();
		PrintWriter pw = response.getWriter();
		if(requestType.equals("getProfile")) {
			System.out.println("getting profile");
			pw.println(mysql.getProfile(username));
		}
		else if(requestType.equals("setCity")) {
			pw.println(mysql.setCity(username, request.getParameter("city")));
		}
		else if(requestType.equals("setVerified")) {
			pw.println(mysql.setVerified(username, request.getParameter("verified")));
		}
		else if(requestType.equals("setHandicap")) {
			pw.println(mysql.setHandicap(username, request.getParameter("handicap")));
		}
		else if(requestType.equals("incStars")) {
			pw.println(mysql.incStars(username));
		}
	}

	 // @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
