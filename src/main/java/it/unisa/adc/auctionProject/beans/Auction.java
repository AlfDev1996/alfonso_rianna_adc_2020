package it.unisa.adc.auctionProject.beans;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import net.tomp2p.futures.FutureDirect;

public class Auction implements Serializable {

    private String name_auction, description;
    private Date expire_date, start_date;
    private ArrayList<AuctionBid> slot;
    private double reserved_price;

    public Auction() {
        this.name_auction = "";
        this.description = "";
        this.expire_date = null;
        this.start_date = new Date(System.currentTimeMillis() + 3600 * 1000);

        this.slot = new ArrayList<>();
        this.reserved_price = 0;

    }

    public Auction(String name_auction, String description, Date expire_date, ArrayList<AuctionBid> partecipanti, double reserved_price, User owner) {

        this.name_auction = name_auction;
        this.description = description;
        this.expire_date = expire_date;
        this.start_date = new Date(System.currentTimeMillis() + 3600 * 1000);
        this.slot = partecipanti;
        this.reserved_price = reserved_price;

    }

    public String addBid(AuctionBid bid) {
        if (this.expire_date.before(new Date(System.currentTimeMillis() + 3600 * 1000))) {
            return "expired";
        }

        for (Iterator<AuctionBid> offer = slot.iterator(); offer.hasNext();) {
            AuctionBid o = offer.next();
            if (o.getBidder().getName().equals(bid.getBidder().getName())) {
                if (o.getBid() >= bid.getBid()) {
                    return "highestBidPresent";
                }
                if (this.slot.size() > 0 && bid.getBid() > this.slot.get(0).getBid()) {
                    offer.remove();
                }

            }
        }

        if (this.slot.size() > 0 && this.slot.get(0).getBid() >= bid.getBid()) {
            return "tooSmall";
        }

        this.slot.add(0, bid);
        return "added";
    }

    public String checkStatus() {
        Date dataCorrente = new Date(System.currentTimeMillis() + 3600 * 1000);

        if (dataCorrente.after(expire_date)) {
            if (slot.size() > 1 && slot.get(0).getBid() > reserved_price) {
                return "Asta " + this.name_auction + " conclusa - Vincitore: " + slot.get(0).getBidder().getName() + " che paga: €" + slot.get(1).getBid() + 1;

            } else if (slot.size() == 1 && slot.get(0).getBid() > reserved_price) {
                return "Asta " + this.name_auction + " conclusa - Vincitore: " + slot.get(0).getBidder().getName() + " che paga: €" + slot.get(0).getBid();

            } else if (slot.size() > 0 && slot.get(0).getBid() < reserved_price) {
                return "Asta " + this.name_auction + " Conclusa - Prezzo di riserva non raggiunto";
            } else {
                return "Asta " + this.name_auction + " conclusa senza partecipanti";
            }

        } else {
            long[] diff = getDateDiff(dataCorrente, expire_date);
            if (dataCorrente.before(expire_date) && this.slot.size() > 0) {

                return "Asta " + this.name_auction + " in corso ti restano per offrire " + diff[3] + " Giorni " + diff[0] + " Ore " + diff[1] + " Minuti e " + diff[2] + " secondi.\nMigliore offerente " + this.slot.get(0).getBidder().getName() + " Che ha offerto €" + this.slot.get(0).getBid();
            } else {
                return "Asta " + this.name_auction + " in corso ti restano per offrire " + diff[3] + " Giorni " + diff[0] + " Ore " + diff[1] + " Minuti e " + diff[2] + " secondi.";
            }
        }

    }

    public double removeBid(String auctionName, User bidder) {
        if (auctionName != "" && bidder != null) {
            for (Iterator<AuctionBid> offer = slot.iterator(); offer.hasNext();) {
                AuctionBid o = offer.next();
                if (o.getBidder().getName().equals(bidder.getName())) {

                    offer.remove();
                    return o.getBid();
                }
            }
        }

        return 0;
    }

    private static long[] getDateDiff(Date date1, Date date2) {

        //System.out.println("Data INizio -> "+date1+"\n");
        //System.out.println("Data Fine -> "+date2);
        long[] difference = new long[4];

        long difference_In_Time = date2.getTime() - date1.getTime();

        long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
        difference[0] = difference_In_Hours;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        difference[1] = difference_In_Minutes;

        long difference_In_Seconds = (difference_In_Time / 1000) % 60;
        difference[2] = difference_In_Seconds;

        long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
        difference[3] = difference_In_Days;

        return difference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Auction auction = (Auction) o;
        return name_auction.equals(auction.name_auction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name_auction);
    }

    public double getMaxBid() {
        if (this.slot.size() > 0) {
            return this.slot.get(0).getBid();
        } else {
            return 0;
        }
    }

    public boolean isHighest(double d) {
        return d > this.slot.get(0).getBid();
    }

    public String getName_auction() {
        return name_auction;
    }

    public void setName_auction(String name_auction) {
        this.name_auction = name_auction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(Date expire_date) {
        this.expire_date = expire_date;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public ArrayList<AuctionBid> getPartecipanti() {
        return slot;
    }

    public void setPartecipanti(ArrayList<AuctionBid> partecipanti) {
        this.slot = partecipanti;
    }

    public double getReserved_price() {
        return reserved_price;
    }

    public void setReserved_price(double reserved_price) {
        this.reserved_price = reserved_price;
    }
}
