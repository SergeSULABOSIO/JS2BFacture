/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.UI;

import SOURCES.CallBack.EcouteurUpdateClose;
import SOURCES.Utilitaires.Parametres;
import javax.swing.JTabbedPane;


/**
 *
 * @author HP Pavilion
 */
public class PanelFacture extends javax.swing.JPanel {

    /**
     * Creates new form PanelFacture
     */
    
    private JTabbedPane parent;
    private PanelFacture moi;
    
    public PanelFacture(Parametres parametres, JTabbedPane parent) {
        initComponents();
        this.parent = parent;
        setPanelFacture(parametres);
        this.moi = this;
    }
    
    private void setPanelFacture(Parametres parametres){
        this.scrollContenuFacture.setViewportView(new Panel(parametres, new EcouteurUpdateClose() {
            @Override
            public void onActualiser(String texte) {
                labStatus.setText(texte);
            }

            @Override
            public void onFermer() {
                //parent.removeAll();
                parent.remove(moi);
            }
        }));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labStatus = new javax.swing.JLabel();
        scrollContenuFacture = new javax.swing.JScrollPane();

        labStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labStatus.setText("Prêt.");

        scrollContenuFacture.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollContenuFacture)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrollContenuFacture, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labStatus)
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel labStatus;
    private javax.swing.JScrollPane scrollContenuFacture;
    // End of variables declaration//GEN-END:variables
}