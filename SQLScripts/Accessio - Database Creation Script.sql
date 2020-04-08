drop database Accessio;
create database Accessio;
use Accessio;

-- this is good --
create table Users(
	UserID INT PRIMARY KEY NOT NULL auto_increment,
    Email VARCHAR(100) NOT NULL,
    Name VARCHAR(100) NOT NULL,
    Username VARCHAR(100) NOT NULL,
    Password VARCHAR(20) NOT NULL
);
-- this should be fine --
create table Locations(
	LocationID INT PRIMARY KEY NOT NULL auto_increment,
    LocationName VARCHAR(1000) NOT NULL,
    Address VARCHAR(1000) NOT NULL,
    PhoneNumber VARCHAR(200) not null,
    Website VARCHAR(200) not null,
    ElevatorRating double, -- these values can be empty on default, will be filled as users review --
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
-- this looks good --
create table Reviews(
	ReviewID int primary key not null auto_increment,
    LocationID INT not null,
    UserID int not null,
    Review VARCHAR(2000) not null,
    foreign key fk1(LocationID) references Locations(LocationID),
    foreign key fk2(UserID) references Users(UserID)
);
-- there is no primary key for this table --
create table ReviewResponse(
	ReviewID int not null,
    Upvotes int not null,
    Downvotes int not null,
    foreign key fk1(ReviewID) references Reviews(ReviewID)
);

            