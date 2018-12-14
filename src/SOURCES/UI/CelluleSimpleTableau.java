/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.UI;

import ICONES.Icones;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;

/**
 *
 * @author user
 */
public class CelluleSimpleTableau extends javax.swing.JPanel {
    
    private ImageIcon iconeEdition;
    
    /**
     * Creates new form PanValeurTable
     */
    public CelluleSimpleTableau(String val, boolean alignerDroite, ImageIcon iconeEdition) {
        initComponents();
        this.iconeEdition = iconeEdition;
        setValeur(val);
        if(alignerDroite == true){
            labvaleur.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        }else{
            labvaleur.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        }
        labvaleur.setIcon(this.iconeEdition);
    }
    
    
    
    
    private void ecouterLigneImpare(int row){
        if((row % 2) == 0){
            this.setBackground(Color.WHITE);
        }else{
            this.setBackground(Color.lightGray);
        }
    }
    
    public void ecouterSelection(boolean isSelected, int row){
        if(isSelected == true){
            labvaleur.setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
            labvaleur.setForeground(Color.WHITE);
            this.setBackground(Color.BLACK);
        }else{
            labvaleur.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 11));
            labvaleur.setForeground(Color.BLACK);
            ecouterLigneImpare(row);
        }
    }
    
    
    public void setValeur(String val){
        this.labvaleur.setText(val);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labvaleur = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        labvaleur.setBackground(new java.awt.Color(255, 255, 255));
        labvaleur.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labvaleur.setText("2000.15 $");
        labvaleur.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labvaleur, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labvaleur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labvaleur;
    // End of variables declaration//GEN-END:variables
}
