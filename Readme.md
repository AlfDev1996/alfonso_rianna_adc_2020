
# **Auction Mechanism**

  

> Development of an application based on a p2p network. Specifically, an application that implements a second price auction mechanism (ebay model).
This type of auction requires the highest bidder on a  object to occupy the first slot in the list of offers, the second the second place and so on. The buyer who made the highest bid wins the item but pays the price offered by the second bidder.
The basic system allows you to create a new auction (including parameters such as expiration time, reserve price, name and description), check the current status of an auction and possibly place a bid on a specific auction.

>

![enter image description here](https://www.glasbergen.com/wp-content/gallery/technology-cartoons/security29.gif)

### **Fundamental operations**

 - **Create Auction**: this operation allows a generic user of the system to create an auction
 - **Check Auction Status**: this operation allows you to check the current status of an auction
 - **Place a bid**: this operation allows you to bid on an auction

These operations with their method signatures and parameters are described in the interface below

## AuctionMechanism Java API
```java

import java.util.Date;

/**

Copyright 2017 Universita' degli Studi di Salerno

Licensed under the Apache License, Version 2.0 (the "License");

you may not use this file except in compliance with the License.

You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software

distributed under the License is distributed on an "AS IS" BASIS,

WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and

limitations under the License.

An auction mechanism API based on P2P .

Each peer can sell and buy goods using a Second-Price Auctions (EBay).

second-price auction is a non-truthful auction mechanism for multiple items.

Each bidder places a bid. The highest bidder gets the first slot,

the second-highest, the second slot and so on, but the highest

bidder pays the price bid by the second-highest bidder,

the second-highest pays the price bid by the third-highest, and so on.

*/

  

public interface AuctionMechanism {

/**

* Creates a new auction for a good.

* @param _auction_name a String, the name identify the auction.

* @param _end_time a Date that is the end time of an auction.

* @param _reserved_price a double value that is the reserve minimum pricing selling.

* @param _description a String describing the selling goods in the auction.

* @return true if the auction is correctly created, false otherwise.

*/

public boolean createAuction(String _auction_name, Date _end_time, double _reserved_price, String _description);

/**

* Checks the status of the auction.

* @param _auction_name a String, the name of the auction.

* @return a String value that is the status of the auction.

*/

public String checkAuction(String _auction_name);

/**

* Places a bid for an auction if it is not already ended.

* @param _auction_namea a String, the name of the auction.

* @param _bid_amount a double value, the bid for an auction.

* @return a String value that is the status of the auction.

*/

public String placeAbid(String _auction_name, double _bid_amount);

}

```

## Technologies used for development

- **Programming language**: The project is based on Java v.1.8.0_271
- **Graphic development framework**: Java Swing
- **DHT management**: TOM P2P v5.0
- **Software Project Management**: Apache Maven v.3.6.3
- **Containerization technology**: Docker v.19.03.13
- **Testing**: JUnit v4.11
- **IDE**: NetBeans 
 
 # Development
 The development has as its key point the development and expansion of the functions seen in the previous paragraph.
## Project Structure

Maven was used as Software Project Management, so the various dependencies including that of Tom p2p were inserted in the *pom.xml* file, below the repositories and dependencies used

```xml
<repositories>
        <repository>
            <id>tomp2p.net</id>
            <url>http://tomp2p.net/dev/mvn/</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.beryx</groupId>
            <artifactId>text-io</artifactId>
            <version>3.3.0</version>
        </dependency>

        <dependency>
            <groupId>net.tomp2p</groupId>
            <artifactId>tomp2p-all</artifactId>
            <version>5.0-Beta8</version>
        </dependency>

        <dependency>
            <groupId>args4j</groupId>
            <artifactId>args4j</artifactId>
            <version>2.33</version>
        </dependency>

    </dependencies>
```
## Packages
The project is structured in 3 fundamental packages

 - it.unisa.adc.auctionProject
 - it.unisa.adc.auctionProject.GUI
 - it.unisa.adc.auctionProject.beans
### it.unisa.adc.auctionProject.beans
Contains system entities, which are:
 - User.java
 - AuctionBid.java
 - Auction.java

#### User.java
Represents the generic user of the system, in our case a generic peer, the variables of the class are:

- Name
- Address of the peer

The class has standard get and set.

#### AuctionBid.java
This class represents a generic bid placed on an auction, the variables of the class are:

- An object of type User
- Offer (double)

#### Auction.java
This class represents a generic auction object, the variables of the class are:

- Name
- Description
- Expire_date
- Start_Date
- slot (Array list of AuctionBid Object)
- reserved_price

The class is equipped with the standard get and set methods, in addition to the standard methods it has some specific methods:

 - `addBid(AuctionBid bid)`:
	* This function takes an AuctionBid object as input, initially verifies that the auction has not expired, immediately afterwards it checks the set of bids for the indicated auction, in order to check if the user already has other offers of higher price / minor for the same auction. If the user has a higher offer for this auction then the insertion of the new offer is rejected otherwise if he has a previous offer with a lower price this is deleted and the new offer with a higher price is inserted.
- `checkStatus()`:
	* This function checks the status of the auction. Specifically, it checks whether this has ended or not, if it is over it indicates if it ended with a winner and the price to pay or if it ended without having participants or the reserve price was not reached, instead if it is still in progress, when there is no time to offer, who is the best bidder if present
- `getDateDiff(Date date1, Date date2)`:
	* A function that returns a vector containing, days, minutes and seconds that differ on two dates

### it.unisa.adc.auctionProject
Contains the core of the project, the files contained in the package are:

 - StartApp.java
 - App.java
 - AuctionMechanism.java
 - MessageListener.java
 - AuctionMechanismImpl.java
 - CreateAuctionException.java

#### StartApp.java

The project is basically based on two versions, one that makes use of *Text-IO*: is a library for creating Java console applications. It can be used in applications that need to read interactive input from the user;  and a version based on an interface created with Java Swing.
This class which contains the main, first parses the parameters that are passed to it from the command line, depending on the value of the *GUI* parameter which can be "yes" or "no", starts the correct class.

#### App.java

Main class for using the application via the version without interface, therefore from the command line.

#### AuctionMechanism.java && MessageListener.java

Respectively interface with the basic operations seen in the previous paragraph and interface for listener of messages received by a peer.

#### CreateAuctionException.java

Custom exception for auction creation

#### AuctionMechanismImpl.java

Main class for developing both basic and additional methods, the methods contained in the class are:

 - `createAuction(String _auction_name, Date _end_time, double _reserved_price, String _description) `:
	 * Method for creating an auction, takes as input: auction name, auction end date, reserve price and a description. After checking if the auction with the name passed in input does not already exist, create a new auction object and load it into _dht and return true, false otherwise.
 - `checkAuction(String _auction_name)`:
	*  Method to check the current status of an auction, in the form of a string; takes as input the name of the auction. Initially checks that the auction exists, fetches the auction object from _dht and checks its status using the checkStatus method of the auction object.
 - `placeAbid(String _auction_name, double _bid_amount)`:
	 * Method to place a bid on an auction, returns a string and takes as input the name of the auction and the offer.
If the auction on which you want to bid exists and the bid is strictly greater than 0 then the addBid method of the auction object is used to add the new bid. If the offer is entered correctly, in addition to updating the auction object in the DHT, all participants of that auction are notified of the new offer, the auction is inserted among the auctions in which the user participates and a new TimerTask is activated to notify the user when the auction ends.
 - `getWinner(long endTime, final String auctionName)`:
	* This function takes as input a time in milliseconds and the name of the auction, it schedules a task that will be executed after the time passed as a parameter. Specifically, this function is called up after the auction has ended and informs the user of the outcome of the auctions in which he is participating.
 - `cancelBid(String _auction_name)`:
	 * This feature is used to remove a bid on a particular auction. If the specified auction exists, it is verified that the conditions necessary and useful to remove an offer are met, i.e. the user who wants to remove the offer is a participant and that the auction has not expired.
If the offer is successfully deleted, and that offer was the maximum price, participants are notified of the lowering of the maximum threshold in order to offer.
 - `getDescriptionByName(String auction)`:
	* Function that returns the description of an auction by name
 - `summaryOfMyObject(boolean participation)`:
	 * This function to check the items offered for sale by a user and their status. In this summary there is the possibility to also include the status not of your own objects but of the auctions you participate in, to also include the auctions you participate in you must set "participation = true"
 - `notifiesParticipants(Auction asta, String msg)`:
	 * This function is used to notify all users participating in an auction of an event
 - `removeAuction(String auctionName)`:
	 * This function is used to delete an auction. Verify that the auction exists, has not expired and also that it does not already have participants, if these conditions are met then proceed with the removal
 - `getMillisEndAuction(String auction)`:
	 * This function calculates the number of milliseconds left until an auction has expired
 - `interestedAuction(String name)`:
	 * This function is used to check if a user is still interested in an auction or has deleted their offer
 - `updateAuctionDescription(String _auction_name, String description)`:
	 * This function is used to update the description of an auction.
Verify that the user making this change is the owner of that auction, the auction exists and has not expired.
If these conditions occur, the description is updated
 - `raiseOnAuction(String auction_name, double amount)`:
	* This function implements an alternative method to the classic offer on an object.
Instead of specifying the precise price to offer, the user can indicate a choice that is whether to offer 1.2 or 3 euros above the current maximum price.
 - `leaveSystem()`:
	* This function takes care of canceling any offers of the peer who is leaving the network and starts the procedure for leaving the network by the peer.

### it.unisa.adc.auctionProject.GUI
Contains all the frames that implement the graphical version of the system. The idea of the interface is very simple, that is, the user has buttons available to carry out the implemented operations and a console that reports results, errors or any messages to be read by the user.
> There may be differences in graphical rendering depending on the host system; the solution was tested on a docker container
> 
- `mainFrame.java`:
	* Main frame, system home
- `placeBidFrame.java`:
	* Frame that allows you to read the parameters necessary to make an offer, both in classic mode and in raise mode
- `deleteBid.java`:
	* Frame used for removing an offer
- `deleteAuctionFrame.java`:
	* Frame used for removing an auction
- `createAuctionFrame.java`:
	* Frame used for creating an auction
- `checkAuctionFrame.java`:
	* Frame used to check the status of an auction

### Test Case
The project was tested through a test class, using Junit v4.11 which is a unit testing framework for the Java programming language.
The test class has 4 static class variables, these 4 variables are of type AuctionMechanismImpl and represent 4 peers used for the test.

    private static AuctionMechanismImpl peer0;
    private static AuctionMechanismImpl peer1;
    private static AuctionMechanismImpl peer2;
    private static AuctionMechanismImpl peer3;

Since peers are initialized only once we can use the setup method which is annotated with "@BeforeClass" and the peers are initialized as follows:

	peer0= new AuctionMechanismImpl(0,"127.0.0.1", new MessageListenerImpl(0),"Alfonso");
    peer1= new AuctionMechanismImpl(1,"127.0.0.1", new MessageListenerImpl(1),"Giovanni");
    peer2= new AuctionMechanismImpl(2,"127.0.0.1", new MessageListenerImpl(2),"Raffaele");
    peer3= new AuctionMechanismImpl(3,"127.0.0.1", new MessageListenerImpl(3),"Samuele");

The test scenarios that have been created specifically are:

 - Create an auction
 - Create an auction that already exists
 - Check the status of the auction in progress without participants
 - Check the status of the auction expired without participants
 - Make a bid on an ongoing auction
 - Bid on an expired auction
 - Bid on your own auction
 - Make a bid, however, without reaching the maximum bid already present
 - Make a bid with a negative amount
 - Bid on a non-existent auction
 - Place multiple bids from the same peer on the same auction
 - Make a bid using the raise method
 - Auction closed and winner verification, with only 1 bidder
 - Closed auction and winner verification, with multiple bidders
 - Auction closed without reaching the reserve price
 - Removal of auction
 - Removing an auction without permissions
 - Report of items offered for sale without items
 - Report of items offered for sale with items
 - Deleting a bid on an auction
 - Bid deletion on an auction you are not participating in
 - Bid deletion on an expired auction
 - Auction description update
 - Auction description update without permissions
 - Leave the system

# How To Run

## Contenerization

Containers are an abstraction at the app layer that packages code and dependencies together. Multiple containers can run on the same machine and share the OS kernel with other containers, each running as isolated processes in user space. Containers take up less space than VMs (container images are typically tens of MBs in size), can handle more applications and require fewer VMs and Operating systems.
So this technique inherently fits the solution, the solution contains a dockerfile which allows you to launch an ubuntu container for execution

## Dockerfile


```dockerfile
FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/AlfDev1996/PlaceAuctionProject.git

FROM maven:3.5-jdk-8-alpine
WORKDIR /app
COPY --from=0 /app/PlaceAuctionProject /app
RUN mvn clean
RUN mvn package

FROM java:openjdk-8
WORKDIR /app
ENV MASTERIP=127.0.0.1
ENV ID=0
ENV NAME=DEFAULTNAME
ENV GUI=NO
COPY --from=1 /app/target/placeAuctionProject-1.0-jar-with-dependencies.jar /app

CMD java -jar placeAuctionProject-1.0-jar-with-dependencies.jar -m $MASTERIP -id $ID -name $NAME -gui $GUI
```
4 environment variables have been defined:

 - *MASTERIP*: IP address of the Masterpeer, used for the access of peers in the network, therefore for the bootstrap phase, the localhost address is indicated by default in the dockerfile.
 - *ID*: Unique identifier (offset for the port)
 - *NAME*: Name of the user / peer accessing the system.
 - *GUI*: Parameter that allows you to decide when starting up whether to start the version with a graphical interface or not.

