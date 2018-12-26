/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import SOURCES.UI.DialogueFacture;
import SOURCES.CallBack.EcouteurFacture;
import SOURCES.UI.PanelFacture;
import SOURCES.Utilitaires.Donnees;
import SOURCES.Utilitaires.Parametres;
import java.util.Date;
import java.util.Vector;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceClient;
import SOURCES.Utilitaires.ExerciceFiscale;

/**
 *
 * @author HP Pavilion
 */
public class TestPrincipal extends javax.swing.JFrame {
    
    private Parametres parametres;
    /**
     * Creates new form TestPrincipal
     */
    public TestPrincipal() {
        initComponents();
    }
    
    
    private void initData(){
        String numeroFacture = ""+(new Date().getTime());
        int idFacture = 20;
        Date dateDebut = new Date(118, 0, 1);
        Date dateFin = new Date(118, 11, 31);
        ExerciceFiscale anneeScolaire = new ExerciceFiscale(dateDebut, dateFin, "Année Scolaire 2018-2019");
        Vector<InterfaceArticle> listArticles = new Vector<>();
        listArticles.add(new TESTProduit(12, "INSCRIPTION", 1, "Année", 0, 50, 0, 1));
        listArticles.add(new TESTProduit(2, "MINERVALE", 1, "Année", 0, 1500, 0, 3));
        listArticles.add(new TESTProduit(121, "TRAVAIL MANUEL", 1, "Année", 0, 10, 0, 1));
        
        TESTClient client = new TESTClient(12, "Christian MUTA KANKUNGWALA", "(+243)84 480 35 14", "cmuta@aib-brokers.com", "RAS");
        
        TESTEntreprise entreprise = new TESTEntreprise(-1, "S2B, Simple.Intuitif", "167B, Av. ITAGA, C./LINGWALA, KINSHASA - RDC", "+243844803514", "info@s2b-simple.com", "www.s2b-simple.com", "EquityBank Congo", "S2B", "000000002114545", "0012554", "CDKIS0012", "logo.png", "RCCM/CD/KIN45-59", "IDNAT000124", "IMP1213");
        
        String monnaie = "USD";
        int idMonnaie = 10;
        double tva = 0;
        double remise = 0;
        
        this.parametres = new Parametres("Serge SULA BOSIO", numeroFacture, idFacture, listArticles, client, entreprise, monnaie, idMonnaie, tva, remise, anneeScolaire);
        
        this.parametres.setEcouteurFacture(new EcouteurFacture() {
            @Override
            public void onEnregistre(InterfaceClient client, Vector<InterfaceArticle> articles, Vector<InterfacePaiement> paiements, Vector<InterfaceEcheance> echeances) {
                
                System.out.println("CLIENT : " + client.getNom());
                System.out.println("ARTICLES : ");
                double tot = 0;
                for(InterfaceArticle article : articles){
                    System.out.println(" * "+article.getQte()+", " + article.getNom()+", "+ article.getTotalTTC());
                    tot += article.getTotalTTC();
                }
                System.out.println("Total : "+tot+" "+parametres.getMonnaie());
                System.out.println("PAIEMENTS : ");
                double paie = 0;
                for(InterfacePaiement paiement : paiements){
                    System.out.println(" * "+paiement.getDate().toLocaleString()+", " + paiement.getNomDepositaire()+", "+ paiement.getMontant()+", ");
                    paie += paiement.getMontant();
                }
                System.out.println("Total : "+paie+" "+parametres.getMonnaie());
                System.out.println("Total Solde : "+(tot - paie)+" "+parametres.getMonnaie());
                System.out.println("PLAN DE PAIEMENT:");
                for(InterfaceEcheance echeance : echeances){
                    System.out.println(" * Echéance : "+echeance.toString());
                }
            }
        });
        
        Vector<InterfaceArticle> articles = new Vector<>();
        articles.add(new TESTProduit(12, "INSCRIPTION", 1, "Année", 0, 50, 0, 1));
        articles.add(new TESTProduit(2, "MINERVALE", 1, "Année", 0, 1500, 0, 3));
        articles.add(new TESTProduit(121, "TRAVAIL MANUEL", 1, "Année", 0, 10, 0, 1));
        
        Vector<InterfacePaiement> paiements = new Vector<>();
        paiements.add(new TESTPaiement(-1, 12, 12, "Serge SULA BOSIO", "INSCRIPTION", "Serge SULA BOSIO", 5, new Date()));
        paiements.add(new TESTPaiement(-1, 12, 12, "Serge SULA BOSIO", "INSCRIPTION", "Serge SULA BOSIO", 5, new Date()));
        paiements.add(new TESTPaiement(-1, 12, 12, "Serge SULA BOSIO", "INSCRIPTION", "Serge SULA BOSIO", 5, new Date()));
        paiements.add(new TESTPaiement(-1, 12, 12, "Serge SULA BOSIO", "INSCRIPTION", "Serge SULA BOSIO", 5, new Date()));
        
        paiements.add(new TESTPaiement(-1, 12, 2, "Serge SULA BOSIO", "MINERVALE", "Serge SULA BOSIO", 100, new Date()));
        paiements.add(new TESTPaiement(-1, 12, 2, "Serge SULA BOSIO", "MINERVALE", "Serge SULA BOSIO", 100, new Date()));
        paiements.add(new TESTPaiement(-1, 12, 2, "Serge SULA BOSIO", "MINERVALE", "Serge SULA BOSIO", 100, new Date()));
        
        Vector<InterfaceEcheance> echeances = new Vector<>();
        echeances.add(new TESTEcheance(-1, "PREMIER VERSEMENT", -1, new Date(), new Date(), numeroFacture, 0, 600, 1, monnaie));
        
        this.parametres.setDonnees(new Donnees(articles, paiements));
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Tester Dialog");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Tester Tab");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(501, Short.MAX_VALUE))
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        initData();    
        DialogueFacture df = new DialogueFacture(this, true, parametres);
        df.show();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        initData();
        PanelFacture pf = new PanelFacture(parametres, jTabbedPane1);
        
        
        jTabbedPane1.add("Facture", pf);
        jTabbedPane1.setSelectedComponent(pf);
        
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TestPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
