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
                                .withDefaultValue(1.0)
                                .read("Prezzo Di Riserva:");

                        String expireDate = textIO.newStringInputReader()
                                .withDefaultValue("01/01/2100 00:00")
                                .read("Data Scadenza Formato [DD/MM/YYYY hh:mm]:");

                        String test = verifyParameter(nameAuction, descrizione, expireDate, reservePrice);
                        if (test.equalsIgnoreCase("success")) {

                            Date expire_date = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(expireDate);

                            if (peer.createAuction(nameAuction, expire_date, reservePrice, descrizione)) {
                                terminal.printf("\nAsta %s creata con successo \n", nameAuction);
                            } else {
                                terminal.printf("\nErrore nella creazione dell'asta\n");
                            }

                        } else {
                            terminal.printf("\nErrore parametri: %s \n", test);
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

                            String check = verifyParameter(auctionName, null, null, amount);
                            if (check.equalsIgnoreCase("success")) {
                                String result = peer.placeAbid(auctionName, amount);
                                terminal.printf(" %s\n", result + "\n");
                            } else {
                                terminal.printf("\nErrore parametri: %s \n", check);
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
                            String checkAuction = verifyParameter(auctionName, null, null, null);
                            if (checkAuction.equalsIgnoreCase("success")) {

                                String result = peer.raiseOnAuction(auctionName, raiseValue);
                                terminal.printf(" %s\n", result + "\n");
                            } else {
                                terminal.printf("\nErrore parametri: %s \n", checkAuction);
                            }
                        }
                        break;

                    case 3:
                        terminal.printf("Inserisci Nome Asta sulla quale annullare offerta\n");
                        String astaCancelBid = textIO.newStringInputReader()
                                .withDefaultValue("Asta Default")
                                .read("Name:");
                        String cancelBidCheck = verifyParameter(astaCancelBid, null, null, null);
                        if (cancelBidCheck.equalsIgnoreCase("success")) {
                            String res = peer.cancelBid(astaCancelBid);
                            terminal.printf(" %s\n", res + "\n");
                        } else {
                            terminal.printf("\nErrore parametri: %s \n", cancelBidCheck);
                        }
                        break;

                    case 4:
                        terminal.printf("Inserisci Nome Asta da controllare\n");
                        String astaStatus = textIO.newStringInputReader()
                                .withDefaultValue("Asta Default")
                                .read("Name:");
                        String checkStaturParameter = verifyParameter(astaStatus, null, null, null);
                        terminal.printf("Includere descrizione ?\n");
                        boolean includeDescription = textIO.newBooleanInputReader().withDefaultValue(false).read("include?");
                        if (checkStaturParameter.equalsIgnoreCase("success")) {
                            String response = peer.checkAuction(astaStatus);
                            if (includeDescription) {
                                response += "\n" + peer.getDescriptionByName(astaStatus);
                            }
                            terminal.printf(" %s\n", response + "\n");
                        } else {
                            terminal.printf("\nErrore parametri: %s \n", checkStaturParameter);
                        }
                        break;
                    case 5:
                        terminal.printf("Inserisci Nome Asta su cui eliminare l'oferta\n");
                        String deleteBidAuction = textIO.newStringInputReader()
                                .withDefaultValue("Asta Default")
                                .read("Name:");
                         String deleteBidCheck = verifyParameter(deleteBidAuction, null, null, null);
                         if (deleteBidCheck.equalsIgnoreCase("success")) {
                        String resDeleteBid = peer.cancelBid(deleteBidAuction);
                        terminal.printf(" \n%s\n", resDeleteBid);
                         }else {
                            terminal.printf("\nErrore parametri: %s \n", deleteBidCheck);
                        }
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
                        
                        String deleteAuctionCheck = verifyParameter(deleteAuction, null, null, null);
                        if (deleteAuctionCheck.equalsIgnoreCase("success")) {
                        String resDeleteAuction = peer.removeAuction(deleteAuction);
                        terminal.printf(" %s\n", resDeleteAuction + "\n");
                        }else {
                            terminal.printf("\nErrore parametri: %s \n", deleteAuctionCheck);
                        }
                        break;

                    case 8:
                        terminal.printf("Inserisci Nome Asta sulla quale aggiornare la descrizione\n");
                        String updateAuctionName = textIO.newStringInputReader()
                                .withDefaultValue("Asta Default")
                                .read("Name:");
                        String updateDescription = textIO.newStringInputReader()
                                .withDefaultValue("Default description")
                                .read("Description:");
                        
                        String updateDescriptionCheck = verifyParameter(updateAuctionName, updateDescription, null, null);
                        if (updateDescriptionCheck.equalsIgnoreCase("success")) {
                        String resUpdate = peer.updateAuctionDescription(updateAuctionName, updateDescription);
                        terminal.printf(" %s\n", resUpdate + "\n");
                        }else {
                            terminal.printf("\nErrore parametri: %s \n", updateDescriptionCheck);
                        }
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

    private String verifyParameter(String nome, String descrizione, String data, Double prezzo) {
        if (nome != null && nome.chars().allMatch(Character::isDigit)) {
            return "Il nome dell'asta non è un valore unicamente numerico";
        } else if (descrizione != null && descrizione.chars().allMatch(Character::isDigit)) {
            return "La descrizione dell'asta non è un valore unicamente numerico";
        } else if (prezzo != null && prezzo <= 0) {
            return "Il prezzo non può essere minore o pari a 0";
        } else {

            try {
                if (data != null) {
                    Date expire_date = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(data);
                    if (expire_date.before(new Date(System.currentTimeMillis() + 3600 * 1000))) {
                        return "La data di scadenza dell'asta non può essere precedente alla data attuale";
                    }
                }
            } catch (Exception e) {
                return "Formato Data errato";

            }

        }

        return "success";

    }
}
