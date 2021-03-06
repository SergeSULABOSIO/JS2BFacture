/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.UI;

import SOURCES.CallBackFacture.EcouteurActualisationFacture;
import SOURCES.CallBackFacture.EcouteurFacture;
import SOURCES.Utilitaires_Facture.DataFacture;
import Source.Callbacks.EcouteurFreemium;
import Source.Callbacks.EcouteurUpdateClose;
import Source.Objet.CouleurBasique;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
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
    private CouleurBasique couleurBasique;
    private DataFacture dataFacture;
    private JProgressBar progress;
    public PanelContenuFacture pcfacture = null;
    private EcouteurFreemium ef = null;
    
    public PanelFacture(EcouteurFreemium ef, CouleurBasique couleurBasique, JProgressBar progress, JTabbedPane parent, DataFacture dataFacture, EcouteurFacture ecouteurFacture, EcouteurActualisationFacture ecouteurActualisationFacture) {
        initComponents();
        this.ef = ef;
        this.progress = progress;
        this.couleurBasique = couleurBasique;
        this.parent = parent;
        setPanelFacture(dataFacture, progress, ecouteurFacture, ecouteurActualisationFacture);
        this.moi = this;
    }

    
    private void setPanelFacture(DataFacture dataFacture, JProgressBar progress, EcouteurFacture ecouteurFacture, EcouteurActualisationFacture ecouteurActualisationFacture){
        pcfacture = new PanelContenuFacture(ef, couleurBasique, progress, dataFacture, ecouteurFacture, new EcouteurUpdateClose() {
            
            @Override
            public void onFermer() {
                //parent.removeAll();
                parent.remove(moi);
            }

            @Override
            public void onActualiser(String texte, ImageIcon icone) {
                labStatus.setText(texte);
                labStatus.setIcon(icone);
            }
        });
        
        pcfacture.setEcouteurActualisationFacture(ecouteurActualisationFacture);
        
        this.scrollContenuFacture.setViewportView(pcfacture);
    }
    
    public void setBtEnregistrerNouveau() {
        if(pcfacture != null){
            pcfacture.setBtEnregistrerNouveau();
        }
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
