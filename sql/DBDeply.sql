drop database Accessio;
create database Accessio;
use Accessio;

-- user table
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

-- profile picture table
create table ProfilePictures(
	PictureID INT primary key not null auto_increment,
	ReviewID INT not null,
	ImageData VARBINARY(MAX) not null
);

-- location table
create table Locations(
	LocationID INT PRIMARY KEY NOT NULL auto_increment,
    LocationName VARCHAR(1000) NOT NULL,
    Address VARCHAR(1000) NOT NULL,
    PhoneNumber VARCHAR(200) not null,
    Website VARCHAR(200) not null,
    -- these values can be empty on default, will be filled as users review
    ElevatorRating double, 
    RampRating double,
    DoorRating double,
    Other double
);

-- this is good, may take some kinks to work out --
create table Ratings(
	RatingID INT primary key auto_increment not null,
    UserID INT NOT NULL,
    LocationID INT NOT NULL,
    ElevatorRating double not null,
    RampRating double not null,
    DoorRating double not null,
    Other double not null,
    foreign key fk1(UserID) references Users(UserID),
    foreign key fk2(LocationID) references Locations(LocationID)
);

-- review table
create table Reviews(
	ReviewID INT primary key not null auto_increment,
    LocationID INT not null,
    UserID INT not null,
    Review VARCHAR(2000) not null,
    Upvotes INT not null,
    Downvotes INT not null,
    foreign key fk1(LocationID) references Locations(LocationID),
    foreign key fk2(UserID) references Users(UserID)
);

-- review pictures table
create table Review Pictures(
	PictureID INT primary key not null auto_increment,
	ReviewID INT not null,
	ImageData VARBINARY(MAX) not null
);
            