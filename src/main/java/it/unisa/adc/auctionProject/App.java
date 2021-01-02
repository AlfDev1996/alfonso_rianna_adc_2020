package it.unisa.adc.auctionProject;

import org.kohsuke.args4j.Option;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import javax.sound.midi.Soundbank;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {

    private static int id;
    private static String master;
    private static String nameUser;

    public App(String master, String nameUser, int id) throws Exception {
        this.id = id;
        this.nameUser = nameUser;
        this.master = master;

        //App ap = new App();
        //final CmdLineParser parser = new CmdLineParser(ap);
    }

    public void start() throws Exception {
        TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal terminal = textIO.getTextTerminal();

        class MessageListenerImpl implements MessageListener {

            int peerid;

            public MessageListenerImpl(int peerid) {
                this.peerid = peerid;

            }

            public Object parseMessage(Object obj) {

                //System.out.println("\n" + peerid + "] (Direct Message Received) " + obj + "\n\n");
                terminal.printf(peerid + "] (Avviso) " + obj);
                return "success";
            }

        }
        try {
            // parser.parseArgument(args);

            AuctionMechanismImpl peer = new AuctionMechanismImpl(id, master, new MessageListenerImpl(id), nameUser);
            terminal.printf("\nStaring peer id: %d on master node: %s User Name: %s", id, master, nameUser);

            while (true) {
                printMenu(terminal);

                int option = textIO.newIntInputReader()
                        .withMaxVal(9)
                        .withMinVal(1)
                        .read("Option");

                switch (option) {
                    case 1:
                        terminal.printf("Inserisci Nome Asta\n");
                        String nameAuction = textIO.newStringInputReader()
                                .withDefaultValue("Asta Default")
                                .read("Name:");

                        String descrizione = textIO.newStringInputReader()
                                .withDefaultValue("Descrizione-default")
                                .read("Descrizione:");

                        double reservePrice = textIO.newDoubleInputReader()
                                .withDefaultValue(0.0)
                                .read("Prezzo Di Riserva:");

                        String expireDate = textIO.newStringInputReader()
                                .withDefaultValue("01/01/2100 00:00")
                                .read("Data Scadenza Formato [DD/MM/YYYY hh:mm]:");

                        Date expire_date = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(expireDate);

                        if (reservePrice <= 0) {
                            terminal.printf("Prezzo di riserva errato");
                            continue;
                        }
                        
                        if(expire_date.before(new Date(System.currentTimeMillis() + 3600 * 1000))){
                            terminal.printf("La data di creazione deve essere successiva alla data corrente locale");
                            continue;
                        }
                        if (peer.createAuction(nameAuction, expire_date, reservePrice, descrizione)) {
                            terminal.printf("Asta %s creata con successo ", nameAuction);
                        } else {
                            terminal.printf("Errore nella creazione dell'asta");
                        }
                        break;

                    case 2:

                        printRaise(terminal);
                        int optionBid = textIO.newIntInputReader()
                                .withMaxVal(2)
                                .withMinVal(1)
                                .read("Option");

                        if (optionBid == 1) {
                            terminal.printf("Inserisci Nome Asta\n");
                            String auctionName = textIO.newStringInputReader()
                                    .withDefaultValue("Asta Default")
                                    .read("Name:");

                            terminal.printf("Inserisci prezzo da offrire\n");
                            double amount = textIO.newDoubleInputReader()
                                    .withDefaultValue(1.0)
                                    .read("Offerta: ");

                            if (amount > 0) {
                                String result = peer.placeAbid(auctionName, amount);
                                terminal.printf(" %s\n", result + "\n");
                            } else {
                                terminal.printf("Error in Parameters\n");
                            }
                        } else {
                            terminal.printf("Inserisci Nome Asta\n");
                            String auctionName = textIO.newStringInputReader()
                                    .withDefaultValue("Asta Default")
                                    .read("Name:");
                            printRaiseBid(terminal);
                            int raiseValue = textIO.newIntInputReader()
                                .withMaxVal(3)
                                .withMinVal(1)
                                .read("Raise Value");
                            String result = peer.raiseOnAuction(auctionName, raiseValue);
                            terminal.printf(" %s\n", result + "\n");
                        }
                        break;

                    case 3:
                        terminal.printf("Inserisci Nome Asta sulla quale annullare offerta\n");
                        String astaCancelBid = textIO.newStringInputReader()
                                .withDefaultValue("Asta Default")
                                .read("Name:");

                        String res = peer.cancelBid(astaCancelBid);
                        terminal.printf(" %s\n", res + "\n");
                        break;

                    case 4:
                        terminal.printf("Inserisci Nome Asta da controllare\n");
                        String astaStatus = textIO.newStringInputReader()
                                .withDefaultValue("Asta Default")
                                .read("Name:");

                        terminal.printf("Includere descrizione ?\n");
                        boolean includeDescription = textIO.newBooleanInputReader().withDefaultValue(false).read("include?");
                        String response = peer.checkAuction(astaStatus);
                        if (includeDescription) {
                            response += "\n" + peer.getDescriptionByName(astaStatus);
                        }
                        terminal.printf(" %s\n", response + "\n");

                        break;
                    case 5:
                        terminal.printf("Inserisci Nome Asta su cui eliminare l'oferta\n");
                        String deleteBidAuction = textIO.newStringInputReader()
                                .withDefaultValue("Asta Default")
                                .read("Name:");
                        String resDeleteBid = peer.cancelBid(deleteBidAuction);
                        terminal.printf(" %s\n", resDeleteBid + "\n");
                        break;

                    case 6:
                        terminal.printf("Includere anche le aste a cui stai partecipando ?\n");
                        boolean part = textIO.newBooleanInputReader().withDefaultValue(false).read("include?");
                        terminal.printf("%s", peer.summaryOfMyObject(part));
                        break;

                    case 7:
                        terminal.printf("Inserisci Nome Asta da eliminare\n");
                        String deleteAuction = textIO.newStringInputReader()
                                .withDefaultValue("Asta Default")
                                .read("Name:");
                        String resDeleteAuction = peer.removeAuction(deleteAuction);
                        terminal.printf(" %s\n", resDeleteAuction + "\n");
                        break;

                    case 8:
                        terminal.printf("Inserisci Nome Asta sulla quale aggiornare la descrizione\n");
                        String updateAuctionName = textIO.newStringInputReader()
                                .withDefaultValue("Asta Default")
                                .read("Name:");
                        String updateDescription = textIO.newStringInputReader()
                                .withDefaultValue("Default description")
                                .read("Description:");
                        String resUpdate = peer.updateAuctionDescription(updateAuctionName, updateDescription);
                        terminal.printf(" %s\n", resUpdate + "\n");
                        break;
                    case 9:
                        terminal.printf("Sei sicuro di voler abbandonare il sistema ?");
                        boolean exit = textIO.newBooleanInputReader().withDefaultValue(false).read("exit?");
                        if (exit) {
                            peer.leaveSystem();
                            System.exit(0);
                        }
                        break;
                    default:
                        break;
                }
            }

        } catch (CmdLineException clEx) {
            System.err.println("ERROR: Unable to parse command-line options: " + clEx);
        }
    }

    public static void printMenu(TextTerminal terminal) {
        terminal.printf("\n1 - CREA ASTA\n");
        terminal.printf("\n2 - FAI UNA OFFERTA\n");
        terminal.printf("\n3 - ANNULLA OFFERTA\n");
        terminal.printf("\n4 - STATO ASTA\n");

        terminal.printf("\n5 - ELIMINA OFFERTA\n");
        terminal.printf("\n6 - MIEI OGGETTI\n");
        terminal.printf("\n7 - ELIMINA ASTA\n");
        terminal.printf("\n8 - AGGIORNA DESCRIZIONE\n");
        terminal.printf("\n9 - ESCI\n");

    }

    public static void printRaise(TextTerminal terminal) {
        terminal.printf("\n1 - Offerta Standard\n");
        terminal.printf("\n2 - Raise su Prezzo Massimo\n");

    }
    
    public static void printRaiseBid(TextTerminal terminal) {
        terminal.printf("\n1 - €1\n");
        terminal.printf("\n2 - €2\n");
        terminal.printf("\n3 - €3\n");

    }
    
}
