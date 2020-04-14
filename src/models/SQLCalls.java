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
	public static final String CREDENTIALS_STRING = "jdbc:mysql://localhost/accessio?user=root&password=root";
	public static Connection conn = null;
	
	// Call in servlet whenever we're starting execution --> connection is always saved
	public SQLCalls() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(CREDENTIALS_STRING);
		} catch (Exception e) {
			// handle exception
			System.out.println("there was a problem establishing a connection to the database");
		}
	}

// --------------------------------- For LoginServ ---------------------------------
	public boolean verifyClient(String clientID) {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * FROM Users where username='"+ clientID +"'");
			while(rs.next()) {
				String tempName = rs.getString("username");
				if(tempName.equals(clientID)) {
					return true;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean verifyToken(String clientID, String token) {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * from Users where username='"+ clientID +"'");
			while(rs.next()) {
				String tempPass = rs.getString("Password");
				System.out.println(tempPass);
				if(tempPass.equals(token)) {
					return true;
				}
			}	
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int newUser(String email, String name, String clientID, String token) {
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO Users (Email, Name, Username, Password) VALUES ('"
					+ email + "', '" + name + "', '" + clientID + "', '" + token + "')");
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
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

	public static String reviewToUserID(int reviewID) {
		String userID = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT UserID FROM Reviews WHERE reviewID='" + reviewID + "'");
			if (rs.next()) userID = rs.getString("UserID");
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
			if (rs.next()) title = rs.getString("Title");
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
            if (rs.next()) body = rs.getString("Body");
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
			if (rs.next()) upvote = rs.getString("Upvote");
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
			if (rs.next()) downvote = rs.getString("Downvote");
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