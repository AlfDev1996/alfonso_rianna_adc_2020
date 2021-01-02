package it.unisa.adc.auctionProject.beans;

import net.tomp2p.peers.PeerAddress;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String name;
    private PeerAddress peerAddress;


    public User() {
        this.name = "";
        this.peerAddress= null;
    }


    public PeerAddress getPeerAddress() {
        return peerAddress;
    }

    public void setPeerAddress(PeerAddress peerAddress) {
        this.peerAddress = peerAddress;
    }

    public User(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


