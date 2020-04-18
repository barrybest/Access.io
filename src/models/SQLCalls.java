package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONObject;
import com.sun.org.apache.xpath.internal.operations.And;

public class SQLCalls {
	// Note: This connection assumes that your user is root and your password is root
	// Database name is accessio
	public static final String CREDENTIALS_STRING = "jdbc:mysql://localhost/accessio?user=root&password=Studdermuffin12!&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
	static Connection conn = null;
	
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
	
	// Returns all information about a Location except review list
	public String getLocation(int locID) {
		String locJson = "";
		try {
			Statement st = conn.createStatement();
			ArrayList<Double> locationRatings = locationToAverages(locID);
			ResultSet loc = st.executeQuery("SELECT * From Locations WHERE LocationID='" + locID + "';");
			Location location = new Location(loc.getString("LocationName"), loc.getString("Address"), loc.getString("PhoneNumber"),
					loc.getString("Website"), loc.getDouble("ElevatorRating"), loc.getDouble("RampRating"), loc.getDouble("DoorRating"),
					loc.getDouble("Other"), null);
			// Review list is null right now because our database isn't updated
			JSONObject locationObject = new JSONObject(location);
			locJson = locationObject.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locJson;
	}
	
	public int verifyLocation(String locName) {
		int locID = -1;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT LocationID FROM Locations WHERE LocationName='" + locName + "';");
			if (rs.next()) locID = rs.getInt("LocationID");
			System.out.println(locID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locID;
	}
	
	// I can do this as soon as we update the SQL database
	public ArrayList<String> locationToReviews(int locationID) {
		ArrayList<String> reviews = new ArrayList<String>();
		return reviews;
	}
	
	// Method to leave rating --> make sure this updates average ratings in Locations table
	public void leaveRating(int locationID, String userID, double elevatorRating, double rampRating, double doorRating, double otherRating) {
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO Ratings (UserID, LocationID, ElevatorRating, RampRating, DoorRating, Other) VALUES ('" +
			userID + "', '" + locationID + "', '" + elevatorRating + "', '" + rampRating + "', '" + doorRating + "', '" + otherRating + "')");
			
			// Set new elevator rating average
			String newElevator = "";
			ResultSet rs = st.executeQuery("IFNULL((SELECT AVG(ElevatorRating) FROM Ratings WHERE LocationID = '" + locationID + "'), 0)");
			if (rs.next()) newElevator = rs.getString("ElevatorRating");
			st.executeUpdate("UPDATE Locations SET ElevatorRating='" + newElevator + "' WHERE LocationID = '" + locationID + "'");
			
			// Set new ramp rating average
			String newRamp = "";
			rs = st.executeQuery("IFNULL((SELECT AVG(RampRating) FROM Ratings WHERE LocationID = '" + locationID + "'), 0)");
			if (rs.next()) newElevator = rs.getString("RampRating");
			st.executeUpdate("UPDATE Locations SET RampRating='" + newRamp + "' WHERE LocationID = '" + locationID + "'");
			
			// Set new door rating average
			String newDoor = "";
			rs = st.executeQuery("IFNULL((SELECT AVG(DoorRating) FROM Ratings WHERE LocationID = '" + locationID + "'), 0)");
			if (rs.next()) newElevator = rs.getString("DoorRating");
			st.executeUpdate("UPDATE Locations SET DoorRating='" + newDoor + "' WHERE LocationID = '" + locationID + "'");
			
			// Set new other rating average
			String newOther = "";
			rs = st.executeQuery("IFNULL((SELECT AVG(OtherRating) FROM Ratings WHERE LocationID = '" + locationID + "'), 0)");
			if (rs.next()) newElevator = rs.getString("OtherRating");
			st.executeUpdate("UPDATE Locations SET OtherRating='" + newOther + "' WHERE LocationID = '" + locationID + "'");
			
			return;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
// --------------------------------- For Profile.java ---------------------------------
	
	// sets a user's city
	public void setCity(String city, String userID) {
		try {
			Statement st = conn.createStatement();
			st.executeQuery("UPDATE Users SET City='" + city + "' WHERE UserID='" + userID + "'");
		} catch(SQLException e) {
			System.out.println("SQLException in setCity: " + e.getMessage());
		}
	}
	
	// set a user's verified status
	public void setVerified(String userID, boolean verified) {
		try {
			Statement st = conn.createStatement();
			st.executeQuery("UPDATE Users SET Verified='" + verified + "' WHERE UserID='" + userID + "'");
		} catch(SQLException e) {
			System.out.println("SQLException in setCity: " + e.getMessage());
		}
	}
	
	public void setHandicap(String userID, String handicap) {
		try {
			Statement st = conn.createStatement();
			st.executeQuery("UPDATE Users SET Hnadicap='" + handicap + "' WHERE UserID='" + userID + "'");
		} catch(SQLException e) {
			System.out.println("SQLException in setCity: " + e.getMessage());
		}
	}
	
	// increments a user's stars
	//  stars displayed to user will be divided by 5 with a max of 25
	public void incStars(String userID) {
		try {
			Statement st = conn.createStatement();
			ResultSet starsRS = st.executeQuery("SELECT Stars From Users WHERE UserID='" + userID + "'");
			if(starsRS.next()) {
				if(starsRS.getDouble("Stars") < 25) {
					st.executeQuery("UPDATE Users SET Stars='" + starsRS.getDouble("Stars") + "' WHERE UserID='" + userID + "'");
				}
			}
		} catch (SQLException e) {
			System.out.println("SQLException in incStars: " + e.getMessage());
		}
	}
	
	// gets the serialize JSON object of the profile for a given userID
	public String getProfile(String userID) {
		String jsonUser = "";
		try {
			Statement st = conn.createStatement();
			ResultSet userRS = st.executeQuery("SELECT * From Users WHERE UserID='" + userID + "'");
			ResultSet reviewRS = st.executeQuery("SELECT * From Reviews WHERE UserID='" + userID + "'");
			ResultSet imageRS = st.executeQuery("SELECT * From ReviewPictures WHERE UserID='" + userID + "'");
			if(userRS.next()) {
				ArrayList<Review> reviews = new ArrayList<Review>();
				while(reviewRS.next()) {
					ResultSet locationRS = st.executeQuery("SELECT * From Locations WHERE LocationID='" + reviewRS.getInt("LocationID") + "'");
					Location location = new Location(locationRS.getString("LocationName"), locationRS.getString("Address"), 
							locationRS.getString("PhoneNumber"), locationRS.getString("Website"), 
							locationRS.getDouble("ElevatorRating"), locationRS.getDouble("RampRating"), 
							locationRS.getDouble("DoorRating"), locationRS.getDouble("Other"), new ArrayList<Review>());
					Review review = new Review(reviewRS.getString("Title"), reviewRS.getString("Body"), userRS.getString("Name"),
							reviewRS.getInt("upvotes"), reviewRS.getInt("downvotes"), location);
					reviews.add(review);
				}
				Profile profile = new Profile(userRS.getString("Name"), userRS.getString("City"), userRS.getDouble("Stars"),
						userRS.getBoolean("Verified"), userRS.getString("Handicap"), imageRS.getString("ImageData"), reviews);
				JSONObject profileObject = new JSONObject(profile);
				jsonUser = profileObject.toString();
			}
		} catch (SQLException e) {
			System.out.println("SQLException in getUser: " + e.getMessage());
		}
		return jsonUser;
	}
	
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
					"SELECT Upvote FROM Reviews WHERE LocationID='" + locationID + "'");
			if (rs.next()) upvote = Integer.parseInt(rs.getString("Upvote"));
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
					"SELECT Downvote FROM Reviews WHERE LocationID='" + locationID + "'");
			if (rs.next()) downvote = Integer.parseInt(rs.getString("Downvote"));
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
					"UPDATE Reviews SET Upvote='" + upvotecount + "' WHERE LocationID='" + locationID + "'"); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public void addDownvote(String locationID, int downvotecount) {
		try {
			Statement st = conn.createStatement();
			//only WHERE locationID cuz votecounts are the same for everyone
			st.executeUpdate(
					"UPDATE Reviews SET Downvote='" + downvotecount + "' WHERE locationID='" + locationID + "'");
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