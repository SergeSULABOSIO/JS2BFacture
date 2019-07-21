/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE_FACTURE;

import SOURCES.CallBackFacture.EcouteurFacture;
import SOURCES.UI.PanelFacture;
import SOURCES.Utilitaires_Facture.ParametresFacture;
import java.util.Date;
import java.util.Vector;
import SOURCES.Utilitaires_Facture.DonneesFacture;
import SOURCES.Utilitaires_Facture.SortiesFacture;
import SOURCES.Utilitaires_Facture.UtilFacture;
import Source.Interface.InterfaceClasse;
import Source.Interface.InterfaceEleve;
import Source.Interface.InterfaceExercice;
import Source.Interface.InterfaceMonnaie;
import Source.Interface.InterfacePaiement;
import Source.Interface.InterfacePeriode;
import Source.Objet.Classe;
import Source.Objet.Eleve;
import Source.Objet.Entreprise;
import Source.Objet.Exercice;
import Source.Objet.Frais;
import Source.Objet.LiaisonFraisPeriode;
import Source.Objet.Monnaie;
import Source.Objet.Periode;
import static java.lang.Thread.sleep;

/**
 *
 * @author HP Pavilion
 */
public class Principal_Facture extends javax.swing.JFrame {

    public int idUtilisateur = 1;
    public String nomUtilisateur = "Serge SULA BOSIO";
    public int idFacture = 20;
    public int idClasse = 3;
    public double tva = 0;
    public double remise = 0;
    public String numeroFacture = "" + (new Date().getTime());

    public Entreprise entreprise = new Entreprise(-1, "S2B, Simple.Intuitif", "167B, Av. ITAGA, C./LINGWALA, KINSHASA - RDC", "+243844803514", "info@s2b-simple.com", "www.s2b-simple.com", "EquityBank Congo", "S2B", "000000002114545", "0012554", "CDKIS0012", "logo.png", "RCCM/CD/KIN45-59", "IDNAT000124", "IMP1213");
    public Exercice exercice = new Exercice(12, entreprise.getId(), idUtilisateur, "Année Scolaire 2019-2020", new Date(), UtilFacture.getDate_AjouterAnnee(new Date(), 1), InterfaceExercice.BETA_EXISTANT);
    public Eleve eleve = new Eleve(120, entreprise.getId(), idUtilisateur, exercice.getId(), idClasse, (new Date().getTime() + 45), "CM2", "167B, Av. ITAGA, C. LINGWALA", "+24382-87-27-706", "TONGO", "BATANGILA", "CHRISTIAN", InterfaceEleve.STATUS_ACTIF, InterfaceEleve.SEXE_MASCULIN, new Date(), InterfaceEleve.BETA_EXISTANT);
    //Types de monnaies
    public Monnaie MONNAIE_USD = new Monnaie(20, entreprise.getId(), idUtilisateur, exercice.getId(), "Dollars Américains", "$", InterfaceMonnaie.NATURE_MONNAIE_ETRANGERE, 1620, new Date().getTime(), InterfaceMonnaie.BETA_EXISTANT);
    public Monnaie MONNAIE_CDF = new Monnaie(21, entreprise.getId(), idUtilisateur, exercice.getId(), "Francs Congolais", "Fc", InterfaceMonnaie.NATURE_MONNAIE_LOCALE, 1, new Date().getTime() + 1, InterfaceMonnaie.BETA_EXISTANT);

    //Type des périodes
    public Periode Trimestre01 = new Periode(1, entreprise.getId(), idUtilisateur, exercice.getId(), "1er Trimestre", exercice.getDebut(), exercice.getFin(), (new Date().getTime()), InterfacePeriode.BETA_EXISTANT);
    public Periode Trimestre02 = new Periode(2, entreprise.getId(), idUtilisateur, exercice.getId(), "2ème Trimestre", exercice.getDebut(), exercice.getFin(), (new Date().getTime()), InterfacePeriode.BETA_EXISTANT);
    public Periode Trimestre03 = new Periode(3, entreprise.getId(), idUtilisateur, exercice.getId(), "3ème Trimestre", exercice.getDebut(), exercice.getFin(), (new Date().getTime()), InterfacePeriode.BETA_EXISTANT);

    public Vector<LiaisonFraisPeriode> liaisonInsription = new Vector<>();
    public Vector<LiaisonFraisPeriode> liaisonMinervale = new Vector<>();
    public Vector<LiaisonFraisPeriode> liaisonTravManuel = new Vector<>();

    //Types de Frais existants
    public Frais INSCRIPTION = null;
    public Frais MINERVALE = null;
    public Frais TRAVAIL_MANUEL = null;

    public Vector<Frais> donneesArticles = new Vector<>();
    public PanelFacture panelFacture = null;

    /**
     * Creates new form TestPrincipal
     */
    public Principal_Facture() {
        initComponents();
    }

    private ParametresFacture getParametres() {
        //On charge les paramètres
        Vector<InterfaceMonnaie> listeMonnaies = new Vector();
        listeMonnaies.addElement(MONNAIE_USD);
        listeMonnaies.addElement(MONNAIE_CDF);

        Vector<InterfaceClasse> listeClasse = new Vector<>();
        listeClasse.addElement(new Classe(1, idUtilisateur, entreprise.getId(), exercice.getId(), "CM1", 50, "Local 01", UtilFacture.generateSignature(), InterfaceClasse.BETA_EXISTANT));
        listeClasse.addElement(new Classe(2, idUtilisateur, entreprise.getId(), exercice.getId(), "CM2", 50, "Local 02", UtilFacture.generateSignature(), InterfaceClasse.BETA_EXISTANT));
        listeClasse.addElement(new Classe(3, idUtilisateur, entreprise.getId(), exercice.getId(), "CE1", 50, "Local 03", UtilFacture.generateSignature(), InterfaceClasse.BETA_EXISTANT));
        
        Vector<InterfacePeriode> listePeriodes = new Vector<>();
        listePeriodes.add(Trimestre01);
        listePeriodes.add(Trimestre02);
        listePeriodes.add(Trimestre03);

        return new ParametresFacture(idFacture, numeroFacture, idUtilisateur, nomUtilisateur, entreprise, exercice, MONNAIE_USD, listeMonnaies, listeClasse, listePeriodes);
    }

    private DonneesFacture getDonnees() {
        //On charge les données
        

        liaisonInsription.add(new LiaisonPeriodeFrais(Trimestre01.getId(), Trimestre01.getNom(), 100));
        liaisonInsription.add(new LiaisonPeriodeFrais(Trimestre02.getId(), Trimestre02.getNom(), 0));
        liaisonInsription.add(new LiaisonPeriodeFrais(Trimestre03.getId(), Trimestre03.getNom(), 0));

        liaisonMinervale.add(new LiaisonPeriodeFrais(Trimestre01.getId(), Trimestre01.getNom(), 50));
        liaisonMinervale.add(new LiaisonPeriodeFrais(Trimestre02.getId(), Trimestre02.getNom(), 25));
        liaisonMinervale.add(new LiaisonPeriodeFrais(Trimestre03.getId(), Trimestre03.getNom(), 25));
        
        liaisonTravManuel.add(new LiaisonPeriodeFrais(Trimestre01.getId(), Trimestre01.getNom(), 100));
        liaisonTravManuel.add(new LiaisonPeriodeFrais(Trimestre02.getId(), Trimestre02.getNom(), 0));
        liaisonTravManuel.add(new LiaisonPeriodeFrais(Trimestre03.getId(), Trimestre03.getNom(), 0));

        INSCRIPTION = new TEST_Article(12, "INSCRIPTION", 1, "Année", MONNAIE_CDF.getId(), tva, 10000, remise, liaisonInsription, InterfaceArticle.BETA_EXISTANT);
        MINERVALE = new TEST_Article(2, "MINERVALE", 1, "Année", MONNAIE_USD.getId(), tva, 1500, remise, liaisonMinervale, InterfaceArticle.BETA_EXISTANT);
        TRAVAIL_MANUEL = new TEST_Article(121, "TRAVAIL MANUEL", 1, "Année", MONNAIE_USD.getId(), tva, 10, remise, liaisonTravManuel, InterfaceArticle.BETA_EXISTANT);

        Vector<InterfaceArticle> donneesArticles = new Vector<>();
        donneesArticles.add(INSCRIPTION);
        donneesArticles.add(MINERVALE);
        donneesArticles.add(TRAVAIL_MANUEL);

        Vector<InterfacePaiement> donneesPaiements = new Vector<>();
        //donneesPaiements.add(new TEST_Paiement(120, eleve.getId(), INSCRIPTION.getId(), eleve.getNom(), INSCRIPTION.getNom(), eleve.getNom(), 5000, new Date(), InterfacePaiement.MODE_CAISSE, "DSER22445", InterfacePaiement.BETA_EXISTANT));

        return new DonneesFacture(eleve, donneesArticles, donneesPaiements);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        //Initialisation du gestionnaire des factures
        this.panelFacture = new PanelFacture(jTabbedPane1, getParametres(), getDonnees(), new EcouteurFacture() {
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

                                for (InterfacePaiement paiement : sortiesFacture.getPaiements()) {
                                    if (paiement.getBeta() == InterfacePaiement.BETA_MODIFIE || paiement.getBeta() == InterfacePaiement.BETA_NOUVEAU) {
                                        System.out.println(" * Paiement: " + paiement.toString());
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
            java.util.logging.Logger.getLogger(Principal_Facture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal_Facture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal_Facture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal_Facture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal_Facture().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
