package it.unisa.adc.auctionProject;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuctionMechanismImplTest {

    private static AuctionMechanismImpl peer0;
    private static AuctionMechanismImpl peer1;
    private static AuctionMechanismImpl peer2;
    private static AuctionMechanismImpl peer3;

    public AuctionMechanismImplTest() {

    }

    @BeforeClass
    public static void setup() throws Exception {

        class MessageListenerImpl implements MessageListener {

            int peerid;

            public MessageListenerImpl(int peerid) {
                this.peerid = peerid;
            }

            public Object parseMessage(Object obj) {
                System.out.println(peerid + "] (Direct Message Received) " + obj);
                return "success";
            }
        }

        peer0 = new AuctionMechanismImpl(0, "127.0.0.1", new MessageListenerImpl(0), "Alfonso");
        peer1 = new AuctionMechanismImpl(1, "127.0.0.1", new MessageListenerImpl(1), "Giovanni");
        peer2 = new AuctionMechanismImpl(2, "127.0.0.1", new MessageListenerImpl(2), "Raffaele");
        peer3 = new AuctionMechanismImpl(3, "127.0.0.1", new MessageListenerImpl(3), "Samuele");
    }

    @Test
    public void A_createAuctionTest() {
        /*Method for testing auction creation.
        *An auction due date is declared
        *the createAuction method is called, and the return value is verified to be True*/
        Date expire = new Date();
        assertTrue(peer0.createAuction("Asta Pc", expire, 10.20, "Asta per un pc"));
        System.err.println("");

    }

    @Test
    public void B_createAuctionExistingTest() throws InterruptedException {
        /*Method for testing auction creation.
        *An auction due date is declared
        *peer0 creates an auction
        *peer0 tries to create an auction with the same name as the one created previously
        *The return value turns out to be false and the auction is not created
         */
        Date expire = new Date();
        assertTrue(peer0.createAuction("Asta Duplicate", expire, 10.20, "Asta per un pc"));
        assertFalse(peer0.createAuction("Asta Duplicate", expire, 11.20, "Asta "));

    }

    @Test
    public void C_checkAuctionInProgressWithoutParticipantsTest() throws IOException, ClassNotFoundException, InterruptedException {
        /*Test method for checking auction status
        * Declaration of an end date that is set 10 seconds longer than the current system time and date
        * peer1 creates a new auction
        * peer2 checks the status of the auction
        * This will be in progress*/
        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        expire.setTime(expire.getTime() + 10000);
        peer1.createAuction("Auction Test Prova", expire, 10.0, "Test Description");
        assertTrue(peer2.checkAuction("Auction Test Prova").contains("in corso"));
    }

    @Test
    public void D_checkAuctionExpiredWithoutParticipantsTest() throws IOException, ClassNotFoundException, InterruptedException {
        /* Test method for checking auction status
        * Auction declaration with expiry date equal to the instant in which it is created
        * peer1 creates a new auction
        * peer2 checks the status of the auction
        * This should have expired
         */
        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        peer1.createAuction("Auction Exp", expire, 10.0, "Test Description");
        assertEquals("Asta Auction Exp conclusa senza partecipanti", peer3.checkAuction("Auction Exp"));
    }

    @Test
    public void E_placeAbidOnAuctionInProgressTest() throws InterruptedException, IOException, ClassNotFoundException {
        /*Test method for placing a bid on an auction
        * Auction declaration with expiry date equal to the instant in which it is created
        * 500 seconds are added to the expire date
        * peer1 creates a new auction
        * peer2 place a bid on auction
        * the result should be an offer made*/

        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        expire.setTime(expire.getTime() + 500000);
        assertTrue(peer1.createAuction("Asta Test Bid", expire, 11.20, "Default Description"));
        assertEquals("Offerta effettuata", peer2.placeAbid("Asta Test Bid", 36));

    }

    @Test
    public void F_placeAbidIOnAuctionExpiredTest() throws InterruptedException, IOException, ClassNotFoundException {
        /*Test method for placing a bid on an auction
        * Auction declaration with expiry date equal to the instant in which it is created
        * peer1 creates a new auction
        * peer 2 tries to offer
        * The offer is not successful due to the auction expired*/

        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        assertTrue(peer1.createAuction("test-1", expire, 11.20, "Default Description"));
        assertEquals("Asta Scaduta", peer2.placeAbid("test-1", 36));

    }

    @Test
    public void G_placeAbidIOnYourProductTest() throws InterruptedException, IOException, ClassNotFoundException {
        /*Test method for placing a bid on an auction
        * Auction declaration with expiry date equal to the instant in which it is created
        * 5 seconds are added to the expire date
        * peer1 creates a new auction
        * peer 1 offers on its own auction
        * the offer is not successful as it is impossible to increase the offer on one's products*/

        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        expire.setTime(expire.getTime() + 5000);
        assertTrue(peer1.createAuction("My Auction", expire, 11.20, "Default Description"));
        assertEquals("Impossibile aumentare offerta sui propri prodotti", peer1.placeAbid("My Auction", 36));
    }

    @Test
    public void H_placeAbidILowerThanBestOfferTest() throws InterruptedException, IOException, ClassNotFoundException {
        /*Test method for placing a bid on an auction
        * Auction declaration with expiry date equal to the instant in which it is created
        * 100 seconds are added to the expire date
        * peer1 creates a new auction
        * peer 2 place a bid
        * peer 3 makes a lower value bid on the same auction
        * The bid on the auction fails as the best bid was not outbid
         */

        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        expire.setTime(expire.getTime() + 10000000);
        assertTrue(peer1.createAuction("Auction Test1", expire, 11.20, "Default Description"));
        assertEquals("Offerta effettuata", peer3.placeAbid("Auction Test1", 19));
        assertEquals("Offerta Migliore non raggiunta", peer2.placeAbid("Auction Test1", 14));

    }

    @Test
    public void I_placeAbidNegativeAmountTest() throws InterruptedException, IOException, ClassNotFoundException {
        /*Test method for placing a bid on an auction
        * Auction declaration with expiry date equal to the instant in which it is created
        * 100 seconds are added to the expire date
        * peer1 creates a new auction
        * peer 2 place a bid with negative amount
        * The bid on the auction fails 
         */

        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        expire.setTime(expire.getTime() + 100000);
        peer1.createAuction("Auction_TestNegative", expire, 11.20, "Default Description");
        String res = peer2.placeAbid("Auction_TestNegative", -14);
        assertEquals("L'offerta deve essere strettamente maggiore di 0", res);

    }

    @Test
    public void J_placeAbidNonExistentAuctionTest() throws InterruptedException, IOException, ClassNotFoundException {
        /*Test method for placing a bid on an auction
        * peer1 places a bid
        * The auction on which the bid is placed does not exist
        * The bid on the auction fails
         */

        String res = peer2.placeAbid("Non Exist", 15);
        assertEquals("Asta Non Exist non Trovata", res);

    }

    @Test
    public void J_placeAbidMultipleOfferSamePeerTest() throws InterruptedException, IOException, ClassNotFoundException {
        /*Test method for placing a bid on an auction
        * peer1 creates an auction
        * peer3 bids on the created auction
        * peer3 makes a new bid of lower value on the same auction
         */

        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        expire.setTime(expire.getTime() + 100000);
        assertTrue(peer1.createAuction("AuctionMultipleBid", expire, 1.0, "Default Description"));
        assertEquals("Offerta effettuata", peer3.placeAbid("AuctionMultipleBid", 19));
        assertEquals("Offerta di prezzo maggiore presente per questo utente", peer3.placeAbid("AuctionMultipleBid", 15));

    }

    @Test
    public void J_placeAbidRaiseMethodTest() throws InterruptedException, IOException, ClassNotFoundException {
        /*Test method for placing a bid on an auction
        * peer1 places a bid
        * The auction on which the bid is placed does not exist
        * The bid on the auction fails
         */

        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        expire.setTime(expire.getTime() + 100000);
        assertTrue(peer1.createAuction("Raise Auction", expire, 1.0, "Default Description"));
        assertEquals("Offerta effettuata", peer3.placeAbid("Raise Auction", 19));
        assertEquals("Offerta effettuata", peer2.raiseOnAuction("Raise Auction", 1));
        String status = peer1.checkAuction("Raise Auction");
        assertTrue(status.contains("Migliore offerente Raffaele Che ha offerto €20.0"));

    }

    @Test
    public void J_closeAuctionWithWinnerOneBidderTest() throws InterruptedException, IOException, ClassNotFoundException {
        /*method for testing the win on an auction
        * peer1 creates an auction
        * peer3 makes a valid offer
        * after the auction has expired peer1 checks the status of the auction
        * the winner turns out to be peer3
         */

        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        expire.setTime(expire.getTime() + 1000);
        assertTrue(peer1.createAuction("Auction Test WW", expire, 11.20, "Default Description"));
        assertEquals("Offerta effettuata", peer3.placeAbid("Auction Test WW", 19));
        Thread.sleep(2000);
        assertEquals("Asta Auction Test WW conclusa - Vincitore: Samuele che paga: €19.0", peer1.checkAuction("Auction Test WW"));
    }

    @Test
    public void J_closeAuctionWithWinnerPlusBidderTest() throws InterruptedException, IOException, ClassNotFoundException {
        /*method for testing the win on an auction
        * peer1 creates an auction
        * peer2 makes a valid offer
        * peer0 makes a valid offer
        * peer3 makes a valid offer
        * after the auction has expired peer1 checks the status of the auction
        * the winner turns out to be peer3
         */

        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        expire.setTime(expire.getTime() + 1000);
        assertTrue(peer1.createAuction("Auction Test WWPb", expire, 11.20, "Default Description"));
        assertEquals("Offerta effettuata", peer2.placeAbid("Auction Test WWPb", 17));
        assertEquals("Offerta effettuata", peer0.placeAbid("Auction Test WWPb", 18));
        assertEquals("Offerta effettuata", peer3.placeAbid("Auction Test WWPb", 19));
        Thread.sleep(2000);
        assertEquals("Asta Auction Test WWPb conclusa - Vincitore: Samuele che paga: €18.01", peer1.checkAuction("Auction Test WWPb"));
    }

    @Test
    public void J_closeAuctionRservedPriceNotReachedTest() throws InterruptedException, IOException, ClassNotFoundException {
        /*method for testing the win on an auction
        * peer1 creates an auction
        * peer2 makes a valid offer
        * peer0 makes a valid offer
        * peer3 makes a valid offer
        * after the auction has expired peer1 checks the status of the auction
        * no bidder has made a bid higher than the reserve price
         */

        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        expire.setTime(expire.getTime() + 1000);
        assertTrue(peer1.createAuction("Auction Reserved Price", expire, 50.25, "Default Description"));
        assertEquals("Offerta effettuata", peer2.placeAbid("Auction Reserved Price", 17));
        assertEquals("Offerta effettuata", peer0.placeAbid("Auction Reserved Price", 18));
        assertEquals("Offerta effettuata", peer3.placeAbid("Auction Reserved Price", 19));
        Thread.sleep(2000);
        assertEquals("Asta Auction Reserved Price Conclusa - Prezzo di riserva non raggiunto", peer1.checkAuction("Auction Reserved Price"));
    }

    @Test
    public void K_removeAuctionTest() throws IOException, ClassNotFoundException, InterruptedException {
        /*test method to delete an auction
        *peer1 creates an auction
        *then call the auction removal method
        *the auction is successfully removed
         */
        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        expire.setTime(expire.getTime() + 100000);
        peer1.createAuction("Deleted", expire, 11.20, "Default Description");
        String res = peer1.removeAuction("Deleted");
        assertEquals("Asta Deleted Eliminata con successo", res);

    }

    @Test
    public void L_removeAuctionWithoutPermissionsTest() throws IOException, ClassNotFoundException, InterruptedException {
        /*test method to delete an auction
        *peer2 creates an auction
        *after peer3 calls the method to remove the previously created auction from peer2
        *removal of the auction is not allowed
         */
        Date expire = new Date(System.currentTimeMillis() + 3600 * 1000);
        expire.setTime(expire.getTime() + 100000);
        peer2.createAuction("Test No Permissions", expire, 11.20, "Default Description");
        String res = peer3.removeAuction("Test No Permissions");
        assertEquals("Solo il proprietario può eliminare l'asta", res);

    }

    @Test
    public void M_summaryOfMyObjectNoObjectTest() throws IOException, ClassNotFoundException {
        /*test method for the summary of items offered for sale
        *peer3 calls the item summary function before listing any items
        *the returned set is empty
         */
        Date expireSummary = new Date(System.currentTimeMillis() + 3600 * 1000);
        expireSummary.setTime(expireSummary.getTime() + 100000);
        String res = peer3.summaryOfMyObject(false);
        assertEquals("Non hai ancora messo in vendita oggetti", res);

    }

    @Test
    public void N_summaryOfMyObjectTest() throws IOException, ClassNotFoundException {
        /*test method for the summary of items offered for sale
        *peer3 creates an auction to sell an item
        *peer3 calls the function for summary of items for sale, the return set has exactly 1 item
         */
        Date expireSummary = new Date(System.currentTimeMillis() + 3600 * 1000);
        expireSummary.setTime(expireSummary.getTime() + 100000);
        assertTrue(peer3.createAuction("Test Summary", expireSummary, 10.20, "Asta per un pc"));
        String res = peer3.summaryOfMyObject(false);
        assertTrue(res.contains("Hai messo in vendita 1 Oggetti"));

    }

    @Test
    public void O_cancelBidTest() throws IOException, ClassNotFoundException {
        /*method for testing the removal of a bid on an auction
        *peer1 creates an auction
        *peer3 makes an offer
        *peer3 removes its offer
         */
        Date expireCancelBid = new Date(System.currentTimeMillis() + 3600 * 1000);
        expireCancelBid.setTime(expireCancelBid.getTime() + 100000);
        assertTrue(peer1.createAuction("Test Cancel Bid", expireCancelBid, 10.20, "test"));
        peer3.placeAbid("Test Cancel Bid", 10);
        String res = peer3.cancelBid("Test Cancel Bid");
        assertEquals("Offerta Eliminata con Successo", res);

    }

    @Test
    public void P_cancelBidNonPartecipatingAuctionTest() throws IOException, ClassNotFoundException {
        /*method for testing the removal of a bid on an auction
        *peer1 creates an auction
        *peer3 calls the method for removing the auction bid previously created by peer1
        *peer3 is not a participant
         */
        Date expireCancelBidNp = new Date(System.currentTimeMillis() + 3600 * 1000);
        expireCancelBidNp.setTime(expireCancelBidNp.getTime() + 100000);
        peer1.createAuction("Np Auction", expireCancelBidNp, 1, "test np");
        String res = peer3.cancelBid("Np Auction");
        assertEquals("Non sei un partecipante", res);

    }

    @Test
    public void P_cancelBidOnExpiredAuctionTest() throws IOException, ClassNotFoundException, InterruptedException {
        /*method for testing the removal of a bid on an auction
        *peer1 creates an auction
        *peer3 bids on the auction created by peer1
        *peer3 calls the method for removing the auction bid previously created by peer1 after the auction has expired
        *too late
         */
        Date expireCancelBidNp = new Date(System.currentTimeMillis() + 3600 * 1000);
        expireCancelBidNp.setTime(expireCancelBidNp.getTime() + 1000);
        assertTrue(peer1.createAuction("Auction Lenovo Idea", expireCancelBidNp, 1, "Description Random"));
        assertEquals("Offerta effettuata", peer3.placeAbid("Auction Lenovo Idea", 20));
        Thread.sleep(2000);
        assertEquals("Troppo Tardi", peer3.cancelBid("Auction Lenovo Idea"));

    }

    @Test
    public void Q_updateAuctionDescriptionTest() throws IOException, ClassNotFoundException {
        /*
        *test method for updating an auction description
        *peer1 creates an auction with an initial description
        *peer1 changes the description of the previously created auction
        *peer1 verifies that the auction description has been updated
         */
        Date expireUpdateDesc = new Date(System.currentTimeMillis() + 3600 * 1000);
        expireUpdateDesc.setTime(expireUpdateDesc.getTime() + 100000);
        assertTrue(peer1.createAuction("Test Update Description", expireUpdateDesc, 1, "Descrizione"));
        assertEquals("Modifica effettuata correttamente", peer1.updateAuctionDescription("Test Update Description", "Descrizione updated"));
        assertEquals("Descrizione updated", peer1.getDescriptionByName("Test Update Description"));
    }

    @Test
    public void R_updateAuctionDescriptionWithoutPermissionsTest() throws IOException, ClassNotFoundException {
        /*
        *test method for updating an auction description
        *peer1 creates an auction with an initial description
        *peer2 tries to modify the auction description created by peer1 without success
         */
        Date expireUpdateDescNp = new Date(System.currentTimeMillis() + 3600 * 1000);
        expireUpdateDescNp.setTime(expireUpdateDescNp.getTime() + 100000);
        assertTrue(peer1.createAuction("Test Update Description Np", expireUpdateDescNp, 1, "Descrizione"));
        assertEquals("Solo il proprietario può modificare la descrizione", peer2.updateAuctionDescription("Test Update Description Np", "Descrizione updated"));
    }

    @Test
    public void leaveSystemTest() throws IOException, ClassNotFoundException {
        /*
        *test method for updating an auction description
        *peer1 creates an auction with an initial description
        *peer2 tries to modify the auction description created by peer1 without success
         */
        assertTrue(peer0.leaveSystem());
        assertTrue(peer1.leaveSystem());
        assertTrue(peer2.leaveSystem());
        assertTrue(peer3.leaveSystem());

    }

}
