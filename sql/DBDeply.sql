drop database if exists accessio;
create database accessio;
use accessio;

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
	PictureID INT primary key NOT NULL auto_increment,
	ReviewID INT NOT NULL,
	ImageData VARBINARY(MAX) NOT NULL
);

-- location table
create table Locations(
	LocationID INT PRIMARY KEY NOT NULL auto_increment,
    LocationName VARCHAR(1000) NOT NULL,
    Address VARCHAR(1000) NOT NULL,
    PhoneNumber VARCHAR(200) NOT NULL,
    Website VARCHAR(200) NOT NULL,
    Latitude DOUBLE NOT NULL,
    Longitude DOUBLE NOT NULL,
    -- can be empty on default, will be filled as users review
    Rating double
);

-- review table
create table Reviews(
	ReviewID INT primary key NOT NULL auto_increment,
    LocationID INT NOT NULL,
    UserID INT NOT NULL,
    Title VARCHAR(100) NOT NULL,
    Body VARCHAR(2000) NOT NULL,
    Rating double,
    Upvotes INT,
    Downvotes INT,
    foreign key fk1(LocationID) references Locations(LocationID),
    foreign key fk2(UserID) references Users(UserID)
);

-- review pictures table
create table ReviewPictures(
	PictureID INT primary key NOT NULL auto_increment,
	ReviewID INT NOT NULL,
	ImageData VARBINARY(MAX) NOT NULL
);