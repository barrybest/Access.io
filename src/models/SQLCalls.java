package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLCalls {
	public static final String CREDENTIALS_STRING = "jdbc:mysql://google/Accessio?cloudSqlInstance=accessio:us-west1:accessio&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=user&password=user";
	static Connection conn = null;
	
	// Call in main servlet whenever we're starting execution --> connection is always saved
	public SQLCalls() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(CREDENTIALS_STRING);
		} catch (Exception e) {
			// handle exception
		}
	}
	
// --------------------------------- For Location.java ---------------------------------
	
	public static String locationToName(int locationID) {
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
	
	public static String locationToAddress(int locationID) {
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
	
	public static ArrayList<Integer> locationToAverages(int locationID) {
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
	
	public static String locationToPhone(int locationID) {
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
	
	public static String locationToURL(int locationID) {
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
	
// --------------------------------- For Profile.java ---------------------------------
	
// --------------------------------- For Review.java ---------------------------------
	

}