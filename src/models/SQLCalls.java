package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

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
	
	public boolean verifyEmail(String email) {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * FROM Users where email='"+ email +"'");
			while(rs.next()) {
				String tempName = rs.getString("email");
				if(tempName.equals(email)) {
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
	public String getLocation(int locationID) {
		String locJson = "";
		try {
			Statement st = conn.createStatement();
			ResultSet loc = st.executeQuery("SELECT * From Locations WHERE LocationID='" + locationID + "';");
			if (loc.next()) {
				Location location = new Location(locationID, loc.getString("LocationName"), loc.getString("Address"),
						loc.getString("PhoneNumber"), loc.getString("Website"), loc.getDouble("ElevatorRating"),
						loc.getDouble("RampRating"), loc.getDouble("DoorRating"), loc.getDouble("OtherRating"),
						getReviews(locationID));
				Gson gson = new Gson();
				locJson = gson.toJson(location);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locJson;
	}
	
	// Check if location exists and if lat/long match --> if not, make new location
	public int verifyLocation(String locName, double latitude, double longitude) {
		int locID = -1;
		// Trim latitude and longitude to 2 decimal places
		latitude = ((double)((int)(latitude *100.0)))/100.0;
		longitude = ((double)((int)(longitude *100.0)))/100.0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM Locations WHERE LocationName='" + locName + "';");
			if (rs.next()) {
				// Trim found location's latitude and longitude to 2 decimal places
				double locLat = rs.getDouble("Latitude");
				locLat = ((double)((int)(locLat *100.0)))/100.0;
				double locLong = rs.getDouble("Longitude");
				locLong = ((double)((int)(locLong *100.0)))/100.0;
				if (locLat == latitude && locLong == longitude) {
					locID = rs.getInt("LocationID");
				}
			}
			if (locID == -1) { // If location wasn't found with matching latitude and longitude
				st.executeUpdate("INSERT INTO Locations (LocationName, Latitude, Longitude) VALUES ('" + locName + "', '" + latitude + "', '" + longitude + "');");
				rs = st.executeQuery("SELECT LocationID FROM Locations WHERE LocationName='" + locName + "' AND Latitude = '" + latitude + "' AND Longitude = '" + longitude + "';");
				if (rs.next()) locID = rs.getInt("LocationID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locID;
	}
	
	// Return the average ratings in every category for provided location
	
	// Return an array list of all reviews for provided location
	public ArrayList<Review> getReviews(int locationID) {
		ArrayList<Review> reviews = new ArrayList<Review>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM Reviews WHERE LocationID='" + locationID + "';");
			while (rs.next()) {
				Review current = new Review(rs.getString("Title"), rs.getString("Body"), rs.getDouble("ElevatorRating"), rs.getDouble("RampRating"),
						rs.getDouble("DoorRating"), rs.getDouble("OtherRating"), "", rs.getInt("Upvotes"), rs.getInt("Downvotes"), "", "");
				int userID = rs.getInt("UserID");
				int reviewID = rs.getInt("ReviewID");
				Statement st2 = conn.createStatement();
				ResultSet rs2 = st2.executeQuery("SELECT Name FROM Users WHERE UserID='" + userID + "';");
				if (rs2.next()) current.userName = rs2.getString("Name");
				Statement st3 = conn.createStatement();
				ResultSet rs3 = st3.executeQuery("SELECT LocationName FROM Locations WHERE LocationID='" + locationID + "';");
				if (rs3.next()) current.locationName = rs3.getString("LocationName");
				Statement st4 = conn.createStatement();
				ResultSet rs4 = st4.executeQuery("SELECT ImageData FROM ReviewPictures WHERE ReviewID='" + reviewID + "';");
				if (rs4.next()) current.image = rs4.getString("ImageData");
				reviews.add(current);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reviews;
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
				st.executeUpdate("UPDATE Users SET Verified='" + true + "' WHERE Username='" + username + "'");
			}
			else {
				st.executeUpdate("UPDATE Users SET Verified='" + null + "' WHERE Username='" + username + "'");
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
			st.executeUpdate("UPDATE Users SET Hnadicap='" + handicap + "' WHERE Username='" + username + "'");
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
					st.executeUpdate("UPDATE Users SET Stars='" + starsRS.getDouble("Stars") + "' WHERE UserID='" + userID + "'");
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
			Statement userST = conn.createStatement();
			Statement reviewST = conn.createStatement();
			Statement locationST = conn.createStatement();
			ResultSet userRS = userST.executeQuery("SELECT Name, IFNULL(City, \"\"), IFNULL(Verified, false), IFNULL(Handicap, \"\") From Users WHERE Username='" + username + "'");
			if(userRS.next()) {
				ResultSet reviewRS = reviewST.executeQuery("SELECT Title, Body, IFNULL(Upvotes, 0), IFNULL(Downvotes, 0) From Reviews WHERE UserID='" + userRS.getInt("UserID") + "'");
				ArrayList<Review> reviews = new ArrayList<Review>();
				while(reviewRS.next()) {
					ResultSet locationRS = locationST.executeQuery("SELECT LocationName From Locations WHERE LocationID='" + reviewRS.getInt("LocationID") + "'");
					Review review = new Review(reviewRS.getString("Title"), reviewRS.getString("Body"), userRS.getString("Name"),
							reviewRS.getInt("upvotes"), reviewRS.getInt("downvotes"), locationRS.getString("LocationName"));
					reviews.add(review);
				}
				Profile profile = new Profile(userRS.getString("Name"), userRS.getString("City"), userRS.getDouble("Stars"),
						userRS.getBoolean("Verified"), userRS.getString("Handicap"), reviews);
				Gson gson = new Gson();
				jsonUser = gson.toJson(profile);
			}
		} catch (SQLException e) {
			System.out.println("SQLException in getUser: " + e.getMessage());
		}
		return jsonUser;
	}
	
// --------------------------------- For Review.java ---------------------------------

	public String reviewToName(String UserID) {
		String userName = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT Name FROM Users WHERE UserID='" + UserID + "';");
			if (rs.next()) userName = rs.getString("Name");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userName;
	}
	
	public String reviewToLocation(String LocationID) {
		String locationName = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT LocationName FROM Locations WHERE LocationID='" + LocationID + "';");
			if (rs.next()) locationName = rs.getString("LocationName");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locationName;
	}
	
	public String reviewToImage(String LocationID, String UserID) {
		String reviewID = "";
		String reviewImage = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT ReviewID FROM Reviews WHERE LocationID='" + LocationID + "' AND UserID='" + UserID + "';");
			if (rs.next()) reviewID = rs.getString("ReviewID");
			rs = st.executeQuery("SELECT ImageData FROM ReviewPictures WHERE ReviewID = '" + reviewID + "';");
			if (rs.next()) reviewImage = rs.getString("ImageData");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reviewImage;
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
	
	public double reviewToElevatorRating(String locationID, String UserID) {
		double rating = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT ElevatorRating FROM Reviews WHERE UserID='" + UserID + "' AND LocationID='" + locationID + "'");
            if (rs.next()) rating = rs.getDouble("ElevatorRating");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rating;
	}
	
	public double reviewToRampRating(String locationID, String UserID) {
		double rating = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT RampRating FROM Reviews WHERE UserID='" + UserID + "' AND LocationID='" + locationID + "'");
            if (rs.next()) rating = rs.getDouble("RampRating");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rating;
	}
	
	public double reviewToDoorRating(String locationID, String UserID) {
		double rating = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT DoorRating FROM Reviews WHERE UserID='" + UserID + "' AND LocationID='" + locationID + "'");
            if (rs.next()) rating = rs.getDouble("DoorRating");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rating;
	}
	
	public double reviewToOtherRating(String locationID, String UserID) {
		double rating = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT OtherRating FROM Reviews WHERE UserID='" + UserID + "' AND LocationID='" + locationID + "'");
            if (rs.next()) rating = rs.getDouble("OtherRating");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rating;
	}

	public int reviewToUpvote(String locationID, String UserID) {
		int upvote = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT Upvotes FROM Reviews WHERE LocationID='" + locationID + "' AND UserID='" + UserID + "'");
			if (rs.next()) upvote = Integer.parseInt(rs.getString("Upvotes"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return upvote;
	}

	public int reviewToDownvote(String locationID, String UserID) {
		int downvote = 0;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT Downvotes FROM Reviews WHERE LocationID='" + locationID + "'AND UserID='" + UserID + "'");
			if (rs.next()) downvote = Integer.parseInt(rs.getString("Downvotes"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return downvote;
	}

    public void addUpvote(String locationID, int upvotecount, String UserID) {
		try {
			Statement st = conn.createStatement();
			//only WHERE locationID cuz votecounts are the same for everyone
			st.executeUpdate(
					"UPDATE Reviews SET Upvotes='" + upvotecount + "' WHERE LocationID='" + locationID + "' AND UserID='" + UserID + "'"); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public void addDownvote(String locationID, int downvotecount, String UserID) {
		try {
			Statement st = conn.createStatement();
			//only WHERE locationID cuz votecounts are the same for everyone
			st.executeUpdate(
					"UPDATE Reviews SET Downvotes='" + downvotecount + "' WHERE locationID='" + locationID + "' AND UserID='" + UserID + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    public void addReview(String locationID, String UserID, String reviewTitle, String reviewBody, double elevator, double ramp, double door, double other) {
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO Reviews (LocationID, UserID, Title, Body, ElevatorRating, RampRating, DoorRating, " + 
					"OtherRating, Upvotes, Downvotes) VALUES ('" + locationID + "', '" + UserID + "', '" + reviewTitle + "', '" + reviewBody
					+ "', '" + elevator + "', '" + ramp + "', '" + door + "', '" + other + "', '0', '0');");
			// Update averages in Locations table
			double elevatorRating = 0;
			ResultSet rs = st.executeQuery("SELECT IFNULL(AVG(ElevatorRating),0) FROM Reviews WHERE LocationID='" + locationID + "'");
			if (rs.next()) elevatorRating = rs.getDouble("IFNULL(AVG(ElevatorRating),0)");
			st.executeUpdate("UPDATE Locations SET ElevatorRating='" + elevatorRating + "' WHERE LocationID='" + locationID + "'");
			double rampRating = 0;
			rs = st.executeQuery("SELECT IFNULL(AVG(RampRating),0) FROM Reviews WHERE LocationID='" + locationID + "'");
			if (rs.next()) rampRating = rs.getDouble("IFNULL(AVG(RampRating),0)");
			st.executeUpdate("UPDATE Locations SET RampRating='" + rampRating + "' WHERE LocationID='" + locationID + "'");
			double doorRating = 0;
			rs = st.executeQuery("SELECT IFNULL(AVG(DoorRating),0) FROM Reviews WHERE LocationID='" + locationID + "'");
			if (rs.next()) doorRating = rs.getDouble("IFNULL(AVG(DoorRating),0)");
			st.executeUpdate("UPDATE Locations SET DoorRating='" + doorRating + "' WHERE LocationID='" + locationID + "'");
			double otherRating = 0;
			rs = st.executeQuery("SELECT IFNULL(AVG(OtherRating),0) FROM Reviews WHERE LocationID='" + locationID + "'");
			if (rs.next()) otherRating = rs.getDouble("IFNULL(AVG(OtherRating),0)");
			st.executeUpdate("UPDATE Locations SET OtherRating='" + otherRating + "' WHERE LocationID='" + locationID + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
