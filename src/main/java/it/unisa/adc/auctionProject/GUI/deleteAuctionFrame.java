/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.adc.auctionProject.GUI;

import it.unisa.adc.auctionProject.AuctionMechanismImpl;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author alfonso
 */
public class deleteAuctionFrame extends javax.swing.JFrame {
private mainFrame mainRif;
private AuctionMechanismImpl peerRif;
    /**
     * Creates new form deleteAuctionFrame
     */
    public deleteAuctionFrame(mainFrame f, AuctionMechanismImpl peerRif) {
        initComponents();
        this.mainRif=f;
        this.peerRif=peerRif;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtNameAuction = new javax.swing.JTextField();
        btnDeleteAuctionNow = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Asta da eliminare");

        txtNameAuction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameAuctionActionPerformed(evt);
            }
        });

        btnDeleteAuctionNow.setBackground(new java.awt.Color(255, 0, 9));
        btnDeleteAuctionNow.setText("Elimina Ora");
        btnDeleteAuctionNow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteAuctionNowMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNameAuction, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteAuctionNow, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txtNameAuction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDeleteAuctionNow, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNameAuctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameAuctionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameAuctionActionPerformed

    private void btnDeleteAuctionNowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteAuctionNowMouseClicked
        // TODO add your handling code here:
        String name = txtNameAuction.getText();
        String status="";
        if(name.equalsIgnoreCase("")|| name.chars().allMatch(Character::isDigit)){
            JOptionPane.showMessageDialog(this, "Errore Dati di Input:\nNome non vuoto o valore unicamente numerico");
        }else
        {
            try {
               String result= peerRif.removeAuction(name);
               mainRif.outConsoleSet(result);
            } catch (IOException ex) {
                Logger.getLogger(deleteAuctionFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(deleteAuctionFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

    }//GEN-LAST:event_btnDeleteAuctionNowMouseClicked

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeleteAuctionNow;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtNameAuction;
    // End of variables declaration//GEN-END:variables
}
