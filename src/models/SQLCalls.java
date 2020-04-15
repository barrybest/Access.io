package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.sun.org.apache.xpath.internal.operations.And;

public class SQLCalls {
	// Note: This connection assumes that your user is root and your password is root
	// Database name is Accessio
	public static final String CREDENTIALS_STRING = "jdbc:mysql://localhost/accessio?user=root&password=root&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
	static Connection conn = null;
	
	// Call in servlet whenever we're starting execution --> connection is always saved
	public SQLCalls() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(CREDENTIALS_STRING);
		} catch (Exception e) {
			// handle exception
		}
	}
	
// --------------------------------- For Location.java ---------------------------------
	
	public String locationToName(String locationID) {
		String locName = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT LocationName FROM Locations WHERE LocationID='" + locationID + "'");
			if (rs.next()) locName = rs.getString("LocationName");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locName;
	}
	
	public String locationToAddress(String locationID) {
		String address = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Address FROM Locations WHERE LocationID='" + locationID + "'");
			if (rs.next()) address = rs.getString("Address");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return address;
	}
	
	public ArrayList<String> locationToAverages(String locationID) {
		ArrayList<String> averages = new ArrayList<String>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT ElevatorRating, DoorRating, RampRating, Other FROM Locations WHERE LocationID='" + locationID + "'");
			if (rs.next()) {
				averages.add(rs.getString("ElevatorRating"));
				averages.add(rs.getString("DoorRating"));
				averages.add(rs.getString("RampRating"));
				averages.add(rs.getString("Other"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return averages;
	}
	
	public String locationToPhone(String locationID) {
		String phone = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT PhoneNumber FROM Locations WHERE LocationID='" + locationID + "'");
			if (rs.next()) phone = rs.getString("PhoneNumber");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return phone;
	}
	
	public String locationToURL(String locationID) {
		String site = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Website FROM Locations WHERE LocationID='" + locationID + "'");
			if (rs.next()) site = rs.getString("Website");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return site;
	}
	
	// Method to add review
	public void leaveReview(String locationID, String userID, String review) {
		try {
			Statement st = conn.createStatement();
			st.executeQuery("INSERT INTO Reviews (LocationID, UserID, Review, Upvotes, Downvotes) VALUES ('"
			+ locationID + "', '" + userID + "', '" + review + "', '0', '0')");
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Method to leave rating --> make sure this updates average ratings in Locations table
	public void leaveRating(String locationID, String userID, String elevatorRating, String rampRating, String doorRating, String otherRating) {
		try {
			Statement st = conn.createStatement();
			st.executeQuery("INSERT INTO Ratings (UserID, LocationID, ElevatorRating, RampRating, DoorRating, Other) VALUES ('" +
			userID + "', '" + locationID + "', '" + elevatorRating + "', '" + rampRating + "', '" + doorRating + "', '" + otherRating + "')");
			
			// Set new elevator rating average
			String newElevator = "";
			ResultSet rs = st.executeQuery("IFNULL((SELECT AVG(ElevatorRating) FROM Ratings WHERE LocationID = '" + locationID + "'), 0)");
			if (rs.next()) newElevator = rs.getString("ElevatorRating");
			st.executeQuery("UPDATE Locations SET ElevatorRating='" + newElevator + "' WHERE LocationID = '" + locationID + "'");
			
			// Set new ramp rating average
			String newRamp = "";
			rs = st.executeQuery("IFNULL((SELECT AVG(RampRating) FROM Ratings WHERE LocationID = '" + locationID + "'), 0)");
			if (rs.next()) newElevator = rs.getString("RampRating");
			st.executeQuery("UPDATE Locations SET RampRating='" + newRamp + "' WHERE LocationID = '" + locationID + "'");
			
			// Set new door rating average
			String newDoor = "";
			rs = st.executeQuery("IFNULL((SELECT AVG(DoorRating) FROM Ratings WHERE LocationID = '" + locationID + "'), 0)");
			if (rs.next()) newElevator = rs.getString("DoorRating");
			st.executeQuery("UPDATE Locations SET DoorRating='" + newDoor + "' WHERE LocationID = '" + locationID + "'");
			
			// Set new other rating average
			String newOther = "";
			rs = st.executeQuery("IFNULL((SELECT AVG(OtherRating) FROM Ratings WHERE LocationID = '" + locationID + "'), 0)");
			if (rs.next()) newElevator = rs.getString("OtherRating");
			st.executeQuery("UPDATE Locations SET OtherRating='" + newOther + "' WHERE LocationID = '" + locationID + "'");
			
			return;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
// --------------------------------- For Profile.java ---------------------------------
	
// --------------------------------- For Review.java ---------------------------------

	public String reviewToUserID(String locationID, String UserID) {
		String userID = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT UserID FROM Reviews WHERE UserID='" + UserID + "' AND LocationID='" + locationID + "'");
			if (rs.next()) userID = rs.getString("UserID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userID;
	}
	
	public String reviewToLocationID(String locationID, String UserID) {
		String userID = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT LocationID FROM Reviews WHERE UserID='" + UserID + "' AND LocationID='" + locationID + "'");
			if (rs.next()) userID = rs.getString("locationID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userID;
	}

	public String reviewToTitle(String locationID, String UserID) {
		String title = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT Title FROM Reviews WHERE UserID='" + UserID + "' AND LocationID='" + locationID + "'");
			if (rs.next()) title = rs.getString("Title");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return title;
	}

	public String reviewToBody(String locationID, String UserID) {
		String body = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT Body FROM Reviews WHERE UserID='" + UserID + "' AND LocationID='" + locationID + "'");
            if (rs.next()) body = rs.getString("Body");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return body;
	}

	public int reviewToUpvote(String locationID) {
		int upvote = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT Upvotes FROM Reviews WHERE LocationID='" + locationID + "'");
			if (rs.next()) upvote = rs.getInt("Upvotes");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return upvote;
	}

	public int reviewToDownvote(String locationID) {
		int downvote = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT Downvotes FROM Reviews WHERE LocationID='" + locationID + "'");
			if (rs.next()) downvote = rs.getInt("Downvotes");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return downvote;
	}

    public void addUpvote(String locationID, int upvotecount) {
		try {
			Statement st = conn.createStatement();
			//only WHERE locationID cuz votecounts are the same for everyone
			st.executeUpdate(
					"UPDATE Reviews SET Upvotes='" + upvotecount + "' WHERE LocationID='" + locationID + "'"); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public void addDownvote(String locationID, int downvotecount) {
		try {
			Statement st = conn.createStatement();
			//only WHERE locationID cuz votecounts are the same for everyone
			st.executeUpdate(
					"UPDATE Reviews SET Downvotes='" + downvotecount + "' WHERE locationID='" + locationID + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    public void addReview(String locationID, String UserID, String reviewTitle, String reviewBody) {
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(
					"INSERT INTO Reviews " + "VALUES (" + locationID + ", " + UserID + ", " + reviewTitle + ", " + reviewBody + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

} 