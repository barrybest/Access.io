-- ---------------------- Creation of database ---------------------- 
drop database if exists accessio;
create database accessio;
use accessio;

create table Users(
	UserID INT PRIMARY KEY NOT NULL auto_increment,
    Email VARCHAR(100) NOT NULL,
    Name VARCHAR(100) NOT NULL,
    Username VARCHAR(100) NOT NULL,
    Password VARCHAR(20) NOT NULL,
    City VARCHAR(1000),
    Stars DOUBLE,
    Verified BOOLEAN,
    Handicap VARCHAR(1000)
);

create table ProfilePictures(
	PictureID INT primary key NOT NULL auto_increment,
	UserID INT NOT NULL,
	ImageData VARBINARY(MAX) NOT NULL,
	FOREIGN KEY fk1(UserID) REFERENCES Users(UserID)
);

create table Locations(
	LocationID INT PRIMARY KEY NOT NULL auto_increment,
    LocationName VARCHAR(1000) NOT NULL,
    Address VARCHAR(1000) NOT NULL,
    PhoneNumber VARCHAR(200) NOT NULL,
    Website VARCHAR(200) NOT NULL,
    Latitude DOUBLE NOT NULL,
    Longitude DOUBLE NOT NULL,
    ElevatorRating DOUBLE,
    RampRating DOUBLE,
    DoorRating DOUBLE,
    OtherRating DOUBLE
);

create table Reviews(
	ReviewID INT PRIMARY KEY NOT NULL auto_increment,
    LocationID INT NOT NULL,
    UserID INT NOT NULL,
    Title VARCHAR(100) NOT NULL,
    Body VARCHAR(2000) NOT NULL,
    ElevatorRating DOUBLE,
    RampRating DOUBLE,
    DoorRating DOUBLE,
    OtherRating DOUBLE,
    Upvotes INT,
    Downvotes INT,
    FOREIGN KEY fk1(LocationID) REFERENCES Locations(LocationID),
    FOREIGN KEY fk2(UserID) REFERENCES Users(UserID)
);

create table ReviewPictures(
	PictureID INT PRIMARY KEY NOT NULL auto_increment,
	ReviewID INT NOT NULL,
	ImageData VARBINARY(MAX) NOT NULL,
	FOREIGN KEY fk1(ReviewID) REFERENCES Reviews(ReviewID)
);

-- ---------------------- Add locations ---------------------- 
INSERT INTO Locations (LocationName, Address, PhoneNumber, Website, Latitude, Longitude) VALUES
("Dave's Hot Chicken", "970 N Western Ave, Los Angeles, CA 90029", "3232138090", "www.daveshotchicken.com", 0, 0),
("Trio House", "2700 S Figueroa St, Los Angeles, CA 90007", "2135365699", "triohousela.com", 0, 0),
("Salvatori Computer Science Center", "941 Bloom Walk, Los Angeles, CA 90089", "2137404494", "https://calendar.usc.edu/salvatori_computer_science_center_sal", 0, 0),
("Pot of Cha", "3013 S Figueroa St, Los Angeles, CA 90007", "2135161888", "potofcha.com", 0, 0),
("Seeley G. Mudd Building", "3620 McClintock Ave, Los Angeles, CA 90089", "2137407036", "https://calendar.usc.edu/seeley_g_mudd_building_sgm", 0, 0),
("Los Angeles Coliseum", "3911 Figueroa St, Los Angeles, CA 90037", "2137477111", "www.lacoliseum.com", 0, 0),
("Treehouse Rooftop", "686 N Spring St, Los Angeles, CA 90012", "2139887064", "treehouserooftop.com", 0, 0),
("Grace Ford Salvatori Hall", "900 W 36th St, Los Angeles, CA 90089", "2137402666", "https://calendar.usc.edu/grace_ford_salvatori_hall_gfs", 0, 0),
("Everybody's Kitchen", "642 W 34th St, Los Angeles, CA 90007", "2137400269", "https://hospitality.usc.edu/dining_locations/everybodys-kitchen/", 0, 0),
("Target", "3131 S Hoover St #1910, Los Angeles, CA 90089", "2132753149", "https://www.target.com/sl/los-angeles-usc-village/3217", 0, 0),
("Trader Joe's", "3131 S Hoover St Ste 1920, Los Angeles, CA 90089", "2137491497", "https://locations.traderjoes.com/ca/los-angeles/250/", 0, 0);