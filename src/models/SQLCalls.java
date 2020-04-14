package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLCalls {
	// Note: This connection assumes that your user is root and your password is root
	// Database name is Accessio
	public static final String CREDENTIALS_STRING = "jdbc:mysql://localhost/Accessio?user=root&password=root&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
	static Connection conn = null;
	
	// Call in servlet whenever we're starting execution --> connection is always saved
	public SQLCalls() {
		try {
			conn = DriverManager.getConnection(CREDENTIALS_STRING);
		} catch (Exception e) {
			// handle exception
		}
	}
	
// --------------------------------- For Location.java ---------------------------------
	
	public String locationToName(int locationID) {
		String locName = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT LocationName FROM Locations WHERE LocationID='" + locationID + "'");
			locName = rs.getString("LocationName");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locName;
	}
	
	public String locationToAddress(int locationID) {
		String address = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Address FROM Locations WHERE LocationID='" + locationID + "'");
			address = rs.getString("Address");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return address;
	}
	
	public ArrayList<Integer> locationToAverages(int locationID) {
		ArrayList<Integer> averages = new ArrayList<Integer>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT ElevatorRating, DoorRating, RampRating, Other FROM Locations WHERE LocationID='" + locationID + "'");
			averages.add(Integer.parseInt(rs.getString("ElevatorRating")));
			averages.add(Integer.parseInt(rs.getString("DoorRating")));
			averages.add(Integer.parseInt(rs.getString("RampRating")));
			averages.add(Integer.parseInt(rs.getString("Other")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return averages;
	}
	
	public String locationToPhone(int locationID) {
		String phone = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT PhoneNumber FROM Locations WHERE LocationID='" + locationID + "'");
			phone = rs.getString("PhoneNumber");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return phone;
	}
	
	public String locationToURL(int locationID) {
		String site = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Website FROM Locations WHERE LocationID='" + locationID + "'");
			site = rs.getString("Website");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return site;
	}
	
	// Method to add review
	public void leaveReview(int locationID, int userID, String review) {
		try {
			String locID = Integer.toString(locationID);
			
			Statement st = conn.createStatement();
			//working on this ResultSet rs = st.executeQuery("INSERT INTO Reviews (LocationID, UserID, Review, Upvotes, Downvotes) VALUES ('" + locationID + "', '" + userID + ', ')
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Method to leave rating --> make sure this updates average ratings table
	
// --------------------------------- For Profile.java ---------------------------------
	
// --------------------------------- For Review.java ---------------------------------

	public static String reviewToUserID(int reviewID) {
		String userID = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT UserID FROM Reviews WHERE reviewID='" + reviewID + "'");
			userID = rs.getString("UserID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userID;
	}

	public static String reviewToTitle(int reviewID) {
		String title = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Title FROM Reviews WHERE reviewID='" + reviewID + "'");
			title = rs.getString("Title");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return title;
	}

	public static String reviewToBody(int reviewID) {
		String body = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Body FROM Reviews WHERE reviewID='" + reviewID + "'");
            body = rs.getString("Body");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return body;
	}

	public static String reviewToUpvote(int reviewID) {
		String upvote = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Upvote FROM Reviews WHERE reviewID='" + reviewID + "'");
			upvote = rs.getString("Upvote");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return upvote;
	}

	public static String reviewToDownvote(int reviewID) {
		String downvote = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Downvote FROM Reviews WHERE reviewID='" + reviewID + "'");
			downvote = rs.getString("Downvote");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return downvote;
	}

    public static void addUpvote(int reviewID, int upvotecount) {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("UPDATE Reviews SET Upvote='" + upvotecount + "' WHERE reviewID='" + reviewID + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public static void addDownvote(int reviewID, int downvotecount) {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("UPDATE Reviews SET Downvote='" + downvotecount + "' WHERE reviewID='" + reviewID + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

} 