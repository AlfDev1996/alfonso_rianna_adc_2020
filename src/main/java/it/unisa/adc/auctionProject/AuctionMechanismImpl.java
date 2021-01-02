package it.unisa.adc.auctionProject;

import it.unisa.adc.auctionProject.beans.Auction;
import it.unisa.adc.auctionProject.beans.AuctionBid;
import it.unisa.adc.auctionProject.beans.User;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import net.tomp2p.dht.FutureRemove;

public class AuctionMechanismImpl implements AuctionMechanism {

    final private Peer peer;
    final private PeerDHT _dht;
    final private User utente;
    final private int DEFAULT_MASTER_PORT = 4000;

    final private ArrayList<String> my_auction = new ArrayList<String>();
    final private ArrayList<String> my_object = new ArrayList<String>();

    public AuctionMechanismImpl(int _id, String _master_peer, final MessageListener _listener, String name) throws Exception {
        peer = new PeerBuilder(Number160.createHash(_id)).ports(DEFAULT_MASTER_PORT + _id).start();
        _dht = new PeerBuilderDHT(peer).start();
        utente = new User(name);
        utente.setPeerAddress(_dht.peer().peerAddress());

        FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(_master_peer)).ports(DEFAULT_MASTER_PORT).start();
        fb.awaitUninterruptibly();
        if (fb.isSuccess()) {
            peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
        } else {
            throw new Exception("Errore di Bootstrap");
        }

        peer.objectDataReply(new ObjectDataReply() {

            public Object reply(PeerAddress sender, Object request) throws Exception {
                return _listener.parseMessage(request);

            }
        });
    }

    @Override
    public boolean createAuction(String _auction_name, Date _end_time, double _reserved_price, String _description) {

        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_auction_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                if (!futureGet.isEmpty()) {
                    return false;
                }
                Auction asta = new Auction(_auction_name, _description, _end_time, new ArrayList<AuctionBid>(), _reserved_price, utente);
                _dht.put(Number160.createHash(_auction_name)).data(new Data(asta)).start().awaitUninterruptibly();
                my_object.add(asta.getName_auction());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String checkAuction(String _auction_name) throws IOException, ClassNotFoundException {
        FutureGet futureGet = _dht.get(Number160.createHash(_auction_name)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (futureGet.isEmpty()) {
                return "Asta non Trovata";
            }
            Auction asta;
            asta = (Auction) futureGet.dataMap().values().iterator().next().object();
            String status = asta.checkStatus();
            if (status.contains("in corso") && my_object.contains(_auction_name)) {
                status = status.replace("ti restano per offrire", "-");
            }
            return status;

        }
        return null;
    }

    @Override
    public String placeAbid(String _auction_name, double _bid_amount) {
        if (_bid_amount <= 0) {
            return "L'offerta deve essere strettamente maggiore di 0";
        }
        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_auction_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                if (futureGet.isEmpty()) {
                    return "Asta " + _auction_name + " non Trovata";
                }

                AuctionBid bid = new AuctionBid();
                bid.setBid(_bid_amount);
                bid.setBidder(utente);
                Auction asta;
                asta = (Auction) futureGet.dataMap().values().iterator().next().object();

                if (my_object.contains(asta.getName_auction())) {
                    return "Impossibile aumentare offerta sui propri prodotti";
                }

                String status = asta.addBid(bid);
                if (status.equalsIgnoreCase("added")) {
                    _dht.put(Number160.createHash(_auction_name)).data(new Data(asta)).start().awaitUninterruptibly();
                    String msg = utente.getName() + " Rilancia €" + bid.getBid() + " su " + asta.getName_auction();
                    notifiesParticipants(asta, msg);

                    if (!my_auction.contains(asta.getName_auction())) {
                        my_auction.add(asta.getName_auction());
                        long diff = asta.getExpire_date().getTime() - (new Date(System.currentTimeMillis() + 3600 * 1000).getTime());
                        getWinner(diff, _auction_name);
                    }
                    return "Offerta effettuata";
                }
                if (status.equalsIgnoreCase("expired")) {
                    return "Asta Scaduta";
                }
                if (status.equalsIgnoreCase("highestBidPresent")) {
                    return "Offerta di prezzo maggiore presente per questo utente";
                }
                if (status.equalsIgnoreCase("toosmall")) {
                    return "Offerta Migliore non raggiunta";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void getWinner(long endTime, final String auctionName) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    if (my_auction.contains(auctionName)) {
                        String status = checkAuction(auctionName);
                        System.out.println(status);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        },
                endTime
        );
    }

    public String cancelBid(String _auction_name) throws IOException, ClassNotFoundException {
        FutureGet futureGet = _dht.get(Number160.createHash(_auction_name)).start();
        futureGet.awaitUninterruptibly();

        if (futureGet.isSuccess()) {
            if (futureGet.isEmpty()) {
                return "Asta non Trovata";
            }
            Auction asta;
            asta = (Auction) futureGet.dataMap().values().iterator().next().object();
            String status = asta.checkStatus();
            if (!my_auction.contains(asta.getName_auction())) {
                return "Non sei un partecipante";
            }
            if (!status.contains("in corso")) {
                return "Troppo Tardi";
            }
            double removed = asta.removeBid(_auction_name, utente);
            if (removed != 0) {
                my_auction.remove(asta.getName_auction());
                _dht.put(Number160.createHash(_auction_name)).data(new Data(asta)).start().awaitUninterruptibly();
                if (asta.getPartecipanti().size() > 0) {
                    if (asta.isHighest(removed)) {
                        String msg = "Prezzo di " + asta.getName_auction() + " scende a " + asta.getPartecipanti().get(0).getBid();
                        notifiesParticipants(asta, msg);
                    }
                }
                return "Offerta Eliminata con Successo";
            } else {
                return "Errore nella Rimozione Della Offerta";
            }
        }
        return "Error in FutureGet";
    }

    public String getDescriptionByName(String auction) throws ClassNotFoundException, IOException {
        FutureGet futureGet = _dht.get(Number160.createHash(auction)).start();
        futureGet.awaitUninterruptibly();

        if (futureGet.isSuccess()) {
            if (futureGet.isEmpty()) {
                return "Asta non Trovata";
            }
            Auction asta;
            asta = (Auction) futureGet.dataMap().values().iterator().next().object();
            String description = asta.getDescription();

            return description;

        }
        return null;
    }

    public String summaryOfMyObject(boolean participation) throws IOException, ClassNotFoundException {
        String msg = "";
        if (my_object.size() > 0) {
            msg += "Hai messo in vendita " + my_object.size() + " Oggetti";
            for (String s : new ArrayList<>(my_object)) {
                msg += "\n" + checkAuction(s);
            }

            if (msg.contains("in corso")) {
                msg = msg.replace("ti restano per offrire", "-");
            }

        } else {
            msg += "Non hai ancora messo in vendita oggetti";
        }
        if (participation) {
            msg += "\n";
            if (my_auction.size() > 0) {
                msg += "\nStai partecipando a " + my_auction.size() + " aste";
                for (String s : new ArrayList<>(my_auction)) {
                    msg += "\n" + checkAuction(s);
                }

            } else {
                msg += "\nNon stai partecipando ad alcuna asta";
            }
        }
        return msg;
    }

    public boolean leaveSystem() throws IOException, ClassNotFoundException {

        for (String s : new ArrayList<>(my_auction)) {
            cancelBid(s);
        }
        _dht.peer().announceShutdown().start().awaitUninterruptibly();
        return true;
    }

    private void notifiesParticipants(Auction asta, String msg) {
        ArrayList<AuctionBid> peer_participants = asta.getPartecipanti();
        if (!peer_participants.isEmpty() && peer_participants != null) {
            for (AuctionBid peer : peer_participants) {
                if (peer.getBidder().getPeerAddress() != utente.getPeerAddress()) {
                    FutureDirect futureDirect = _dht.peer().sendDirect(peer.getBidder().getPeerAddress()).object(msg).start();
                    futureDirect.awaitUninterruptibly();
                }
            }
        }
    }

    public String removeAuction(String auctionName) throws IOException, ClassNotFoundException {
        if (!my_object.contains(auctionName)) {
            return "Solo il proprietario può eliminare l'asta";
        }

        String status = checkAuction(auctionName);
        if (!status.contains("in corso")) {
            return "Asta scaduta o non trovata";
        } else if (status.contains("offerente")) {
            return "Troppo tardi, l'asta ha già dei partecipanti";
        } else {
            my_object.remove(auctionName);
            FutureRemove fr = _dht.remove(Number160.createHash(auctionName)).start().awaitUninterruptibly();
        }
        return "Asta " + auctionName + " Eliminata con successo";
    }

    public long getMillisEndAuction(String auction) throws ClassNotFoundException, IOException {
        FutureGet futureGet = _dht.get(Number160.createHash(auction)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (futureGet.isEmpty()) {
                return -1;
            }
            Auction asta;
            asta = (Auction) futureGet.dataMap().values().iterator().next().object();
            long diff = (asta.getExpire_date().getTime() - new Date(System.currentTimeMillis() + 3600 * 1000).getTime());
            return diff;
        }
        return -1;

    }

    public boolean interestedAuction(String name) {
        return my_auction.contains(name);
    }

    public String updateAuctionDescription(String _auction_name, String description) throws ClassNotFoundException, IOException {
        if (!my_object.contains(_auction_name)) {
            return "Solo il proprietario può modificare la descrizione";
        }
        FutureGet futureGet = _dht.get(Number160.createHash(_auction_name)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (futureGet.isEmpty()) {
                return "Asta non trovata";
            }
            Auction asta;
            asta = (Auction) futureGet.dataMap().values().iterator().next().object();
            String s = asta.checkStatus();
            if (!s.contains("in corso")) {
                return "Asta Scaduta, impossibile modificare la descrizione";
            } else {
                asta.setDescription(description);
                _dht.put(Number160.createHash(_auction_name)).data(new Data(asta)).start().awaitUninterruptibly();
                String msg = "Descrizione dell'asta " + _auction_name + " modificata";
                notifiesParticipants(asta, msg);
                return "Modifica effettuata correttamente";

            }
        }

        return "Error";

    }
    
    public String raiseOnAuction(String auction_name, double amount) throws ClassNotFoundException, IOException{
        FutureGet futureGet = _dht.get(Number160.createHash(auction_name)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (futureGet.isEmpty()) {
                return "Asta Non Presente";
            }
            Auction asta;
            asta = (Auction) futureGet.dataMap().values().iterator().next().object();
            Double bid = asta.getMaxBid();
                bid+= amount;
                return this.placeAbid(asta.getName_auction(), bid);
            }
        return null;
    
    }
}
