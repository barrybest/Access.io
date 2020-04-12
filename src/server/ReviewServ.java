package server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ReviewServ")
public class ReviewServ extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ReviewServ() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//want to allow upvote and downvote per review
		String requestType = request.getParameter("requestType");
		//if upvote... increase the upvote count in database, dependent upon whether or not user has already upvoted before
		if (requestType.contentEquals("upvote")) {
			
		}
		//if downvote, increase the downvote count in database, dependent upon whether or not user has already downvoted before
		if (requestType.contentEquals("downvote")) {
			
		}
		//want to allow users to undo their upvote / downvote ??
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
