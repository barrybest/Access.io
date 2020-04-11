package server;
import models.Review; //can't import models, but I can import classes from those packages
import models.Profile;
import models.Location;

public class WebServer {
	
	//web server needs to deal with HTTP requests from the front end
	
	//accept requests and get the information requested from the front end
	
	//call models to retrieve the data, will need to be sent back to the client (JSON??)
	
	public WebServer(int port) {
		
	}
	public static void main (String[] args) {
		new WebServer(11111);
	}
}
