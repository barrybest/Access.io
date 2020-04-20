package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.And;

public class SQLCalls {
	// Note: This connection assumes that your user is root and your password is root
	// Database name is accessio
	public static final String CREDENTIALS_STRING = "jdbc:mysql://localhost/accessio?user=root&password=root&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
	static Connection conn = null;
	
	// Call in servlet whenever we're starting execution --> connection is always saved
	public SQLCalls() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(CREDENTIALS_STRING);
		} catch (Exception e) {
			// handle exception
			System.out.println("There was a problem establishing a connection to the database.");
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
	
	public int findClient(String clientID) {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * FROM Users where username='"+ clientID +"'");
			while(rs.next()) {
				String tempName = rs.getString("username");
				if(tempName.equals(clientID)) {
					return rs.getInt("userID");
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return -1;
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
			ResultSet loc = st.executeQuery("SELECT * From Locations WHERE LocationID='" + locID + "';");
			if (loc.next()) {
				Location location = new Location(loc.getString("LocationName"), loc.getString("Address"), loc.getString("PhoneNumber"), loc.getString("Website"), loc.getDouble("Rating"), null);
				// Review list is null right now because our database isn't updated
				Gson gson = new Gson();
				locJson = gson.toJson(location);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locJson;
	}
	
	// Make sure that location exists --> return locationID if it does, -1 if not
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
	
	// Return a JSON of all reviews for provided location
	public String getReviews(int locationID) {
		ArrayList<Review> reviews = new ArrayList<Review>();
		String revJson = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM Reviews WHERE LocationID = '" + locationID + "';");
			while (rs.next()) {
				Review current = new Review(rs.getString("Title"), rs.getString("Body"), rs.getDouble("Rating"), 
						"", rs.getInt("Upvotes"), rs.getInt("Downvotes"), "", "");
				ResultSet rs2 = st.executeQuery("SELECT Name FROM Users WHERE UserID = '" + rs.getInt("UserID") + "';");
				if (rs2.next()) current.userName = rs2.getString("Name");
				rs2 = st.executeQuery("SELECT LocationName FROM Locations WHERE LocationID = '" + rs.getInt("LocationID") + "';");
				if (rs2.next()) current.locationName = rs2.getString("LocationName");
				rs2 = st.executeQuery("SELECT ImageData FROM ReviewPictures WHERE ReviewID = '" + rs.getInt("ReviewID") + "';");
				if (rs2.next()) current.image = rs2.getString("ImageData");
				reviews.add(current);
			}
			Gson gson = new Gson();
			revJson = gson.toJson(reviews);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return revJson;
	}
	
// --------------------------------- For Profile.java ---------------------------------
	
	// sets a user's city
	public String setCity(String username, String city) {
		try {
			Statement st = conn.createStatement();
			st.executeQuery("UPDATE Users SET City='" + city + "' WHERE Username='" + username + "'");
		} catch(SQLException e) {
			System.out.println("SQLException in setCity: " + e.getMessage());
			return "SQLException in setCity: " + e.getMessage();
		}
		return "success";
	}
	
	// set a user's verified status
	public String setVerified(String username, String verified) {
		try {
			Statement st = conn.createStatement();
			if(verified.equals("true")) {
				st.executeQuery("UPDATE Users SET Verified='" + true + "' WHERE Username='" + username + "'");
			}
			else {
				st.executeQuery("UPDATE Users SET Verified='" + null + "' WHERE Username='" + username + "'");
			}
		} catch(SQLException e) {
			System.out.println("SQLException in setCity: " + e.getMessage());
			return "SQLException in setCity: " + e.getMessage();
		}
		return "success";
	}
	
	// sets a user's handicap
	public String setHandicap(String username, String handicap) {
		try {
			Statement st = conn.createStatement();
			st.executeQuery("UPDATE Users SET Hnadicap='" + handicap + "' WHERE Username='" + username + "'");
		} catch(SQLException e) {
			System.out.println("SQLException in setCity: " + e.getMessage());
			return "SQLException in setCity: " + e.getMessage();
		}
		return "success";
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
	
	// gets the serialized JSON object of the profile for a given userID
	public String getProfile(String username) {
		String jsonUser = "";
		try {
			Statement st = conn.createStatement();
			ResultSet userRS = st.executeQuery("SELECT Name, IFNULL(City, \"\"), IFNULL(Verified, false), IFNULL(Handicap, \"\") From Users WHERE Username='" + username + "'");
			if(userRS.next()) {
				ResultSet reviewRS = st.executeQuery("SELECT Title, Body, IFNULL(Upvotes, 0), IFNULL(Downvotes, 0) From Reviews WHERE UserID='" + userRS.getInt("UserID") + "'");
				ResultSet profileImageRS = st.executeQuery("IFNULL(SELECT ImageData From ProfilePictures WHERE UserID='" + userRS.getInt("UserID") + "', \"\")");
				ArrayList<Review> reviews = new ArrayList<Review>();
				while(reviewRS.next()) {
					ResultSet locationRS = st.executeQuery("SELECT * From Locations WHERE LocationID='" + reviewRS.getInt("LocationID") + "'");
					Location location = new Location(locationRS.getString("LocationName"), locationRS.getString("Address"), 
							locationRS.getString("PhoneNumber"), locationRS.getString("Website"), 
							locationRS.getDouble("Rating"), new ArrayList<Review>());
					Review review = new Review(reviewRS.getString("Title"), reviewRS.getString("Body"), reviewRS.getDouble("Rating"),
							userRS.getString("Name"), reviewRS.getInt("Upvotes"), reviewRS.getInt("Downvotes"), location);
					reviews.add(review);
				}
				Profile profile = new Profile(userRS.getString("Name"), userRS.getString("City"), userRS.getDouble("Stars"),
						userRS.getBoolean("Verified"), userRS.getString("Handicap"), profileImageRS.getString("ImageData"), reviews);
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
	
	public String reviewToRating(String locationID, String UserID) {
		String rating = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT Rating FROM Reviews WHERE UserID='" + UserID + "' AND LocationID='" + locationID + "'");
            if (rs.next()) rating = rs.getString("Rating");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rating;
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
    public void addReview(String locationID, String UserID, String reviewTitle, String reviewBody, double rating) {
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(
					"INSERT INTO Reviews (LocationID, UserID, Title, Body, Rating, Upvotes, Downvotes) VALUES "
					+ "('" + locationID + "', '" + UserID + "', '" + reviewTitle + "', '" + reviewBody + "', '" +
						rating + "', 0, 0");
			// Update average in Locations table
			double avgRating = 0;
			ResultSet rs = st.executeQuery("IFNULL((SELECT AVG(Rating) FROM Reviews WHERE LocationID = '" + locationID + "'), 0)");
			if (rs.next()) avgRating = rs.getDouble("Rating");
			st.executeUpdate("UPDATE Locations SET Rating='" + avgRating + "' WHERE LocationID = '" + locationID + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
