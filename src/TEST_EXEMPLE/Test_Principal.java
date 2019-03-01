/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import SOURCES.UI.DialogueFacture;
import SOURCES.CallBack.EcouteurFacture;
import SOURCES.UI.PanelFacture;
import SOURCES.Utilitaires.Parametres;
import java.util.Date;
import java.util.Vector;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceExercice;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.Utilitaires.DonneesFacture;
import SOURCES.Utilitaires.ExerciceFiscale;
import SOURCES.Utilitaires.SortiesFacture;
import SOURCES.Utilitaires.Util;

/**
 *
 * @author HP Pavilion
 */
public class Test_Principal extends javax.swing.JFrame {

    private Parametres parametres = null;
    private DonneesFacture donnees = null;
    private int idUtilisateur = 1;
    private String nomUtilisateur = "Serge SULA BOSIO";
    private TEST_Client client = new TEST_Client(12, "Elève", "Christian MUTA KANKUNGWALA", "(+243)84 480 35 14", "cmuta@aib-brokers.com", "RAS");
    private TEST_Entreprise entreprise = new TEST_Entreprise(-1, "S2B, Simple.Intuitif", "167B, Av. ITAGA, C./LINGWALA, KINSHASA - RDC", "+243844803514", "info@s2b-simple.com", "www.s2b-simple.com", "EquityBank Congo", "S2B", "000000002114545", "0012554", "CDKIS0012", "logo.png", "RCCM/CD/KIN45-59", "IDNAT000124", "IMP1213");
    private ExerciceFiscale anneeScolaire = new ExerciceFiscale(new Date(119, 0, 1), new Date(119, 11, 31), "Année Scolaire 2018-2019");
    private int idFacture = 20;
    private int idMonnaie = 10;
    private double tva = 0;
    private double remise = 0;
    private String numeroFacture = "" + (new Date().getTime());
    public TEST_Exercice exercice = new TEST_Exercice(12, entreprise.getId(), idUtilisateur, nomUtilisateur, new Date(), Util.getDate_AjouterAnnee(new Date(), 1), InterfaceExercice.BETA_EXISTANT);
    public TEST_Monnaie defaultMonnaie = new TEST_Monnaie(idMonnaie, entreprise.getId(), idUtilisateur, exercice.getId(), "Dollars Américains", "$", InterfaceMonnaie.NATURE_MONNAIE_ETRANGERE, 1620, new Date().getTime(), InterfaceMonnaie.BETA_EXISTANT);
    private Vector<InterfaceArticle> typesArticles = new Vector<>();
    private Vector<InterfaceArticle> donneesArticles = new Vector<>();
    private Vector<InterfacePaiement> donneesPaiements = new Vector<>();

    private PanelFacture panelFacture = null;
    private DialogueFacture dialogueFacture = null;
    
    public TEST_Article INSCRIPTION = new TEST_Article(12, "INSCRIPTION", 1, "Année", defaultMonnaie.getId(), 0, 50, 0, 1, InterfaceArticle.BETA_EXISTANT);
    public TEST_Article MINERVALE = new TEST_Article(2, "MINERVALE", 1, "Année", defaultMonnaie.getId(), 0, 1500, 0, 3, InterfaceArticle.BETA_EXISTANT);
    public TEST_Article TRAVAIL_MANUEL = new TEST_Article(121, "TRAVAIL MANUEL", 1, "Année", defaultMonnaie.getId(), 0, 10, 0, 1, InterfaceArticle.BETA_EXISTANT);

    /**
     * Creates new form TestPrincipal
     */
    public Test_Principal() {
        initComponents();
    }

    private void initParametres() {
        //On charge les types d'articles qui existent
        typesArticles.removeAllElements();
        typesArticles.add(INSCRIPTION);
        typesArticles.add(MINERVALE);
        typesArticles.add(TRAVAIL_MANUEL);

        //Initialisation des paramètres
        this.parametres = new Parametres(nomUtilisateur, numeroFacture, idFacture, typesArticles, client, entreprise, defaultMonnaie, tva, remise, anneeScolaire);

        //Initialisation de l'écouteur du gestionnaire de facture
        this.parametres.setEcouteurFacture(new EcouteurFacture() {
            @Override
            public void onEnregistre(SortiesFacture sortiesFacture) {

                Thread th = new Thread() {
                    @Override
                    public void run() {
                        try {
                            /**/
                            if (sortiesFacture != null) {
                                sortiesFacture.getEcouteurEnregistrement().onUploading("Chargement...");
                                sleep(1000);
                                
                                for (InterfaceArticle article : sortiesFacture.getArticles()) {
                                    if(article.getBeta() == InterfaceArticle.BETA_MODIFIE || article.getBeta() == InterfaceArticle.BETA_NOUVEAU){
                                        System.out.println(" * Article: " + article.toString());
                                        
                                        //Après enregistrement
                                        article.setBeta(InterfaceArticle.BETA_EXISTANT);
                                    }
                                }
                                
                                for (InterfacePaiement paiement : sortiesFacture.getPaiements()) {
                                    if(paiement.getBeta() == InterfacePaiement.BETA_MODIFIE || paiement.getBeta() == InterfacePaiement.BETA_NOUVEAU){
                                        System.out.println(" * Paiement: " + paiement.toString());
                                        
                                        //Après enregistrement
                                        paiement.setBeta(InterfacePaiement.BETA_EXISTANT);
                                    }
                                }
                                
                                sortiesFacture.getEcouteurEnregistrement().onDone("Enregistré!");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                th.start();

            }
        });

        //On charges les articles séléctionés
        donneesArticles.removeAllElements();
        donneesArticles.add(INSCRIPTION);
        donneesArticles.add(MINERVALE);
        donneesArticles.add(TRAVAIL_MANUEL);

        //On charge les paiements déjà reçus ou  effectués par le client
        donneesPaiements.removeAllElements();
        donneesPaiements.add(new TEST_Paiement(120, 12, 12, "Serge SULA BOSIO", INSCRIPTION.getNom(), "Serge SULA BOSIO", 5, new Date(), InterfacePaiement.MODE_CAISSE, "DSEREDVFGFD22445", InterfacePaiement.BETA_EXISTANT));

        //Initialisation des données (Articles et paiements reçus)
        donnees = new DonneesFacture(donneesArticles, donneesPaiements);

        //Chargement des données (articles & paiements reçus)
        this.parametres.setDonnees(donnees);
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

        //Initialisation des données
        initParametres();

        //Initialisation du gestionnaire des factures sous forme de la boîte de dialogue
        dialogueFacture = new DialogueFacture(this, true, parametres);

        //On l'affiche
        dialogueFacture.show();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        //Initialisation des données
        initParametres();

        //Initialisation du gestionnaire des factures
        this.panelFacture = new PanelFacture(parametres, jTabbedPane1);

        //Chargement du gestionnaire sur l'onglet
        jTabbedPane1.add("Facture", panelFacture);

        //On séléctionne l'onglet actuel
        jTabbedPane1.setSelectedComponent(panelFacture);

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
            java.util.logging.Logger.getLogger(Test_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Test_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Test_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Test_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Test_Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
