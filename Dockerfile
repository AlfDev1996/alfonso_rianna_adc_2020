FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/AlfDev1996/alfonso_rianna_adc_2020.git

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
