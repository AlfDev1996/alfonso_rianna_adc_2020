/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.adc.auctionProject.GUI;

import it.unisa.adc.auctionProject.AuctionMechanismImpl;
import it.unisa.adc.auctionProject.MessageListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.OptionPaneUI;

/**
 *
 * @author alfonso
 */
public class mainFrame extends javax.swing.JFrame{
private createAuctionFrame create;
private placeBidFrame placeBid;
private checkAuctionFrame checkFrame;
private deleteBid deleteBidFrame;
private deleteAuctionFrame deleteAuctionFrame;
    /**
     * Creates new form mainFrame
     */
private String masterIp;
private int id;
private String name;
private AuctionMechanismImpl peer;
private ArrayList<String> scheduledMessage = new ArrayList<>() ;
    public mainFrame(String masterIp, int id, String name) throws Exception {
        class MessageListenerImpl implements MessageListener {
            int peerid;

            public MessageListenerImpl(int peerid) {
                this.peerid = peerid;

            }

            public Object parseMessage(Object obj) {

                //System.out.println("\n" + peerid + "] (Direct Message Received) " + obj + "\n\n");
                outConsoleSet(peerid + "] (Avviso) " + obj);
                return "success";
            }

        }
        
        this.masterIp =masterIp;
        this.id=id;
        this.name=name;
        this.peer =new AuctionMechanismImpl(id, masterIp, new MessageListenerImpl(id), name);
        
        initComponents();
        welcomeMsg.setText("Benvenuto, "+this.name);
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnStatusAuction = new javax.swing.JButton();
        btnCreateAuction = new javax.swing.JButton();
        btnMyObject = new javax.swing.JButton();
        btnPlaceBid = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnLeaveSystem = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        consoleDisplay = new javax.swing.JTextArea();
        welcomeMsg = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnDeleteAuction = new javax.swing.JButton();
        btnUpdateDescription = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnStatusAuction.setText("Stato Asta");
        btnStatusAuction.setMaximumSize(new java.awt.Dimension(94, 35));
        btnStatusAuction.setMinimumSize(new java.awt.Dimension(94, 35));
        btnStatusAuction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStatusAuctionMouseClicked(evt);
            }
        });

        btnCreateAuction.setText("Crea Asta");
        btnCreateAuction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCreateAuctionMouseClicked(evt);
            }
        });
        btnCreateAuction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateAuctionActionPerformed(evt);
            }
        });

        btnMyObject.setText("Miei Oggetti");
        btnMyObject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMyObjectMouseClicked(evt);
            }
        });

        btnPlaceBid.setText("Offri");
        btnPlaceBid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPlaceBidMouseClicked(evt);
            }
        });
        btnPlaceBid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlaceBidActionPerformed(evt);
            }
        });

        btnDelete.setText("Elimina Offerta");
        btnDelete.setMaximumSize(new java.awt.Dimension(94, 35));
        btnDelete.setMinimumSize(new java.awt.Dimension(94, 35));
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteMouseClicked(evt);
            }
        });

        btnLeaveSystem.setBackground(new java.awt.Color(216, 35, 35));
        btnLeaveSystem.setText("Exit");
        btnLeaveSystem.setActionCommand("Quit");
        btnLeaveSystem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLeaveSystemMouseClicked(evt);
            }
        });

        consoleDisplay.setEditable(false);
        consoleDisplay.setColumns(20);
        consoleDisplay.setFont(new java.awt.Font("Ubuntu Mono", 0, 15)); // NOI18N
        consoleDisplay.setRows(5);
        jScrollPane1.setViewportView(consoleDisplay);

        jButton1.setText("Pulisci Console");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        btnDeleteAuction.setText("Rimuovi Asta");
        btnDeleteAuction.setMaximumSize(new java.awt.Dimension(94, 35));
        btnDeleteAuction.setMinimumSize(new java.awt.Dimension(94, 35));
        btnDeleteAuction.setPreferredSize(new java.awt.Dimension(94, 35));
        btnDeleteAuction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteAuctionMouseClicked(evt);
            }
        });

        btnUpdateDescription.setText("Aggiorna Desc");
        btnUpdateDescription.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateDescriptionMouseClicked(evt);
            }
        });
        btnUpdateDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateDescriptionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnMyObject, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCreateAuction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnStatusAuction, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                            .addComponent(btnPlaceBid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnDeleteAuction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnLeaveSystem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUpdateDescription))
                        .addContainerGap(37, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(welcomeMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(welcomeMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnStatusAuction, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCreateAuction, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPlaceBid, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnMyObject, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUpdateDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDeleteAuction, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnLeaveSystem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        btnCreateAuction.getAccessibleContext().setAccessibleName("btnCrea");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
   public void outConsoleSet(String text){
       consoleDisplay.append("\n"+text+"\n");
   }
   
   public void messageForUser(mainFrame parent,long endTime, final String auctionName){
       new java.util.Timer().schedule(
                new java.util.TimerTask() {
            @Override
            public void run() {
                        String status;
                try {
                   
                    if(peer.interestedAuction(auctionName)){
                      
                    status = peer.checkAuction(auctionName);
                     JOptionPane.showMessageDialog(parent, status);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                       
                    
                
            }
        },
                endTime
        );
       
       //
   }
   
   public static void setConsole(String text){
       
   }
    
    
    private void btnCreateAuctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateAuctionActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnCreateAuctionActionPerformed

    private void btnCreateAuctionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCreateAuctionMouseClicked
        // TODO add your handling code here:
        if(create ==  null || !create.isShowing()){
        create = new createAuctionFrame(this,peer);
        create.setLocation(this.getX(), this.getY());
        create.setResizable(false);
        create.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(this, "La finestra è già aperta");
            
        }
            
        
    }//GEN-LAST:event_btnCreateAuctionMouseClicked

    private void btnPlaceBidMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPlaceBidMouseClicked
        // TODO add your handling code here:
        if(placeBid == null || !placeBid.isShowing()){
        placeBid = new placeBidFrame(this,peer,scheduledMessage);
        placeBid.setLocation(this.getX(), this.getY());
        placeBid.setResizable(false);
        placeBid.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(this, "La finestra è già aperta");
            
        }
    }//GEN-LAST:event_btnPlaceBidMouseClicked

    private void btnStatusAuctionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStatusAuctionMouseClicked
        // TODO add your handling code here:
        if(checkFrame == null || !checkFrame.isShowing()){
        checkFrame = new checkAuctionFrame(this,peer);
        checkFrame.setLocation(this.getX(), this.getY());
        checkFrame.setResizable(false);
        checkFrame.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(this, "La finestra è già aperta");
            
        }
    }//GEN-LAST:event_btnStatusAuctionMouseClicked

    private void btnDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseClicked
        // TODO add your handling code here:
         if(deleteBidFrame == null || !deleteBidFrame.isShowing()){
        deleteBidFrame = new deleteBid(this,peer);
        deleteBidFrame.setLocation(this.getX(), this.getY());
        deleteBidFrame.setResizable(false);
        deleteBidFrame.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(this, "La finestra è già aperta");
            
        }
    }//GEN-LAST:event_btnDeleteMouseClicked

    private void btnLeaveSystemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLeaveSystemMouseClicked
        // TODO add your handling code here:
        int reply = JOptionPane.showConfirmDialog(this, "Sicuro di voler lasciare il sistema?", "Close?",  JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION)
        {
            try {
                peer.leaveSystem();
            } catch (IOException ex) {
                Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
             System.exit(0);
        }
    }//GEN-LAST:event_btnLeaveSystemMouseClicked

    private void btnMyObjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMyObjectMouseClicked
    try {
        // TODO add your handling code here:
        String s="";
        int includeParticipation =JOptionPane.showConfirmDialog(this,"Includere Aste a cui si partecipa", "Seleziona",JOptionPane.YES_NO_OPTION);
        if(includeParticipation == JOptionPane.YES_OPTION)
            s=peer.summaryOfMyObject(true);
        else
            if(includeParticipation== JOptionPane.NO_OPTION)
                s= peer.summaryOfMyObject(false);
        outConsoleSet(s);
    } catch (IOException ex) {
        Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_btnMyObjectMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        consoleDisplay.setText("");
    }//GEN-LAST:event_jButton1MouseClicked

    private void btnDeleteAuctionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteAuctionMouseClicked
        // TODO add your handling code here:
         if(deleteAuctionFrame ==  null || !deleteAuctionFrame.isShowing()){
             deleteAuctionFrame = new deleteAuctionFrame(this,peer);
             deleteAuctionFrame.setLocation(this.getX(),this.getY());
             deleteAuctionFrame.setResizable(false);
             deleteAuctionFrame.setVisible(true);
         }else{
            JOptionPane.showMessageDialog(this, "La finestra è già aperta");
            
        }
    }//GEN-LAST:event_btnDeleteAuctionMouseClicked

    private void btnUpdateDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateDescriptionActionPerformed

    private void btnUpdateDescriptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateDescriptionMouseClicked
        // TODO add your handling code here:
        String name="", description="";
        JTextField auctioName = new JTextField(15);
        JTextField desc = new JTextField(20);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Nome Asta"));
        myPanel.add(auctioName);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Descrizione"));
        myPanel.add(desc);

        int result = JOptionPane.showConfirmDialog(this, myPanel,"Inserire Nome Asta e descrizione", JOptionPane.OK_CANCEL_OPTION);
        if(result == JOptionPane.OK_OPTION){
            name= auctioName.getText();
            description = desc.getText();
            if(name.equalsIgnoreCase("") ||  description.equalsIgnoreCase("")){
                JOptionPane.showMessageDialog(this, "Errore Dati di Input");
            } else
            {
                try {
                    String res = peer.updateAuctionDescription(name, description);
                    outConsoleSet(res);
                } catch (ClassNotFoundException ex) {
                     JOptionPane.showMessageDialog(this, "Errore nella modifica");
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                   
                } catch (IOException ex) {
                     JOptionPane.showMessageDialog(this, "Errore nella modifica");
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            
                
            }
        }
    }//GEN-LAST:event_btnUpdateDescriptionMouseClicked

    private void btnPlaceBidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlaceBidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPlaceBidActionPerformed

    /**
     * @param args the command line arguments
     */
    
   /* public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
    /*    try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
     /*   java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainFrame().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateAuction;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteAuction;
    private javax.swing.JButton btnLeaveSystem;
    private javax.swing.JButton btnMyObject;
    private javax.swing.JButton btnPlaceBid;
    private javax.swing.JButton btnStatusAuction;
    private javax.swing.JButton btnUpdateDescription;
    private javax.swing.JTextArea consoleDisplay;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel welcomeMsg;
    // End of variables declaration//GEN-END:variables
}
