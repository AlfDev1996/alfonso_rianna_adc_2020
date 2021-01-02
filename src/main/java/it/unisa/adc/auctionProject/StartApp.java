/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.adc.auctionProject;

import it.unisa.adc.auctionProject.GUI.mainFrame;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


/**
 *
 * @author alfonso
 */
public class StartApp {
    @Option(name="-m", aliases="--masterip", usage="the master peer ip address", required=true)
    private static String master;

    @Option(name="-id", aliases="--identifierpeer", usage="the unique identifier for this peer", required=true)
    private static int id;

    @Option(name="-name", aliases="--namePeer", usage="system user name", required=true)
    private static String nameUser;
    
    @Option(name="-gui", aliases="--guiEnabling", usage="enabling the graphic interface", required=true)
    private static String guiEnabling;
    
    
     public static void main( String[] args ) throws Exception {
         StartApp start = new StartApp();
         final CmdLineParser parser = new CmdLineParser(start);
         parser.parseArgument(args);
         if(guiEnabling.equalsIgnoreCase("yes")){
            mainFrame mainGui = new mainFrame(master, id, nameUser);
            mainGui.setResizable(false);
            mainGui.setVisible(true);
         }else{
             App a= new App(master, nameUser, id);
             a.start();
         }
     }
    
}
