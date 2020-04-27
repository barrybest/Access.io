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

	public String reviewToUpvote(String locationID) {
		String upvote = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT Upvote FROM Reviews WHERE LocationID='" + locationID + "'");
			if (rs.next()) upvote = rs.getString("Upvote");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return upvote;
	}

	public String reviewToDownvote(String locationID) {
		String downvote = "";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT Downvote FROM Reviews WHERE LocationID='" + locationID + "'");
			if (rs.next()) downvote = rs.getString("Downvote");
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