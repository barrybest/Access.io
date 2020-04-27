-- ---------------------- Creation of database ---------------------- 
DROP DATABASE IF EXISTS accessio;
CREATE DATABASE accessio;
USE accessio;

CREATE TABLE Users(
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

CREATE TABLE Locations(
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

CREATE TABLE Reviews(
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
    foreign key fk1(LocationID) references Locations(LocationID),
    foreign key fk2(UserID) references Users(UserID)
);

-- ---------------------- Add locations ---------------------- 
INSERT INTO Locations (LocationName, Address, PhoneNumber, Website, Latitude, Longitude) VALUES
("Dave's Hot Chicken", "970 N Western Ave, Los Angeles, CA 90029", "3232138090", "www.daveshotchicken.com", 34.112916370408485, -118.33701659459621),
("Thai by Trio", "2700 S Figueroa St, Los Angeles, CA 90007", "2135365699", "triohousela.com", 34.02707550024998, -118.27646715100855),
("Salvatori Computer Science Center", "941 Bloom Walk, Los Angeles, CA 90089", "2137404494", "https://calendar.usc.edu/salvatori_computer_science_center_sal", 34.0194210333121, -118.28947033267468),
("Pot of Cha", "3013 S Figueroa St, Los Angeles, CA 90007", "2135161888", "potofcha.com", 34.02492428479946, -118.27875235117972),
("Seeley G. Mudd Building", "3620 McClintock Ave, Los Angeles, CA 90089", "2137407036", "https://calendar.usc.edu/seeley_g_mudd_building_sgm", 34.02118619925614, -118.28914042096585),
("Los Angeles Coliseum", "3911 Figueroa St, Los Angeles, CA 90037", "2137477111", "www.lacoliseum.com", 34.01402750912101, -118.28799243550748),
("Treehouse Rooftop", "686 N Spring St, Los Angeles, CA 90012", "2139887064", "treehouserooftop.com", 34.05952999885075, -118.23786257766187),
("Grace Ford Salvatori Hall", "900 W 36th St, Los Angeles, CA 90089", "2137402666", "https://calendar.usc.edu/grace_ford_salvatori_hall_gfs", 34.02127513124883, -118.28800316434354),
("Everybody's Kitchen", "642 W 34th St, Los Angeles, CA 90007", "2137400269", "https://hospitality.usc.edu/dining_locations/everybodys-kitchen/", 34.02112610919359, -118.28215833753347),
("Trader Joe's", "3131 S Hoover St Ste 1920, Los Angeles, CA 90089", "2137491497", "https://locations.traderjoes.com/ca/los-angeles/250/", 34.02606569046466, -118.28483912162483);

-- ---------------------- Add users ---------------------- 
INSERT INTO Users (Email, Name, Username, Password, City, Stars, Verified, Handicap) VALUES
("imangry@gmail.com", "John Doe", "johndoe23", "password", "Los Angeles", "1", "1", "None"),
("president@hotmail.com", "Barack Obama", "bobama", "usa", "Washington D.C.", "5", "1", "None"),
("happywheels@usc.edu", "Roberta Roller", "theyseemerollin", "12345", "Atlanta", "4", "0", "Wheelchair user");

-- ---------------------- Add reviews ---------------------- 
INSERT INTO Reviews (LocationID, UserID, Title, Body, ElevatorRating, RampRating, DoorRating,
OtherRating, Upvotes, Downvotes) VALUES
("2", "1", "This place is terrible.", "Not only is their food terrible, but there is no automatic door and they didn't open it for me for five minutes.", "1", "1", "1", "1", 0, 5),
("2", "2", "A great Thai-me!", "Great service, happy to help, and very zen", "5", "5", "5", "5", 11, 1),
("1", "1", "Chicken too hot", "The chicken is too hot and didn't help my asthma.", "1", "1", "1", "1", 0, 2),
("1", "3", "So happy", "Dave's Hot Chicken left me so happy that I honestly didn't notice any issues with the place.", "5", "5", "5", "5", 2, 4),
("8", "3", "Stairway to Hell", "This building has so many unnecessary stairs. You can't access most of the sitting areas if you're traveling in a wheelchair, which is really disappointing for a college campus.", "3", "1", "1", "1", 10, 2),
("8", "1", "Disgusting", "Not only is GFS super dark and depressing, but the doors are usually locked and super heavy, so you have to wait for someone to run over and let you in. Made me late to a meeting.", "2", "1", "1", "2", 5, 0),
("10", "2", "Give it a medal!", "I love this store! Automatic doors, enough space to move around, and free samples! What could be better?", "5", "5", "5", "5", 5, 1),
("10", "1", "Satisfied", "Even I can admit that this is an accessible abode on a campus that doesn't seem to care much about our community. Still, they're lucky there's no rating for gentrification.", "5", "5", "5", "4", 11, 1);
