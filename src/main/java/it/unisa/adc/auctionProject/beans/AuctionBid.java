package it.unisa.adc.auctionProject.beans;

import java.io.Serializable;

public class AuctionBid implements Serializable {
    private User bidder;
    private double bid;

    public AuctionBid() {
        this.bid=0;
        this.bidder= null;

    }

    public AuctionBid(User bidder, float bid) {
        this.bidder = bidder;
        this.bid = bid;
    }

    public User getBidder() {
        return bidder;
    }

    public void setBidder(User bidder) {
        this.bidder = bidder;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }
}
