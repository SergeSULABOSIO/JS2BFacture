/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.UI;

import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.CallBack.EcouteurUpdateClose;
import BEAN_BARRE_OUTILS.BarreOutils;
import BEAN_BARRE_OUTILS.Bouton;
import BEAN_BARRE_OUTILS.BoutonListener;
import BEAN_MenuContextuel.MenuContextuel;
import BEAN_MenuContextuel.RubriqueListener;
import BEAN_MenuContextuel.RubriqueSimple;
import ICONES.Icones;
import SOURCES.CallBack.EcouteurFacture;
import SOURCES.Interface.ArticleFacture;
import SOURCES.EditeursTable.EditeurArticleFacture;
import SOURCES.EditeursTable.EditeurArticlePaiement;
import SOURCES.EditeursTable.EditeurDatePaiement;
import SOURCES.ModelsTable.ModeleListeArticles;
import SOURCES.ModelsTable.ModeleListePaiement;
import SOURCES.Interface.PaiementFacture;
import SOURCES.Utilitaires.Parametres;
import SOURCES.EndusTable.RenduTableArticle;
import SOURCES.EndusTable.RenduTablePaiement;
import SOURCES.Interface.ClientFacture;
import SOURCES.Interface.EntrepriseFacture;
import SOURCES.Utilitaires.Util;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.table.TableColumn;

/**
 *
 * @author HP Pavilion
 */
public class Panel extends javax.swing.JPanel {

    /**
     * Creates new form FacturePanel
     */
    public int indexTabSelected = 0;
    public boolean isAssigetis = true;
    private Icones icones = null;
    private BarreOutils barreOutilsA = null;
    private MenuContextuel menuContextuel = null;
    private RubriqueSimple rubAjouter, rubSupprimer, rubVider, rubActualiser, rubImprimer, rubPDF, rubFermer, rubWRD, rubEnregistrer = null;

    private Bouton btAjouter, btSupprimer, btVider, btActualiser, btImprimer, btPDF, btFermer, btWORD, btEnregistrer;

    public ModeleListeArticles modeleListeArticles = null;
    public ModeleListePaiement modeleListePaiement = null;
    //public EcouteurFacture ecouteurFacture = null;
    private EcouteurUpdateClose callBackSynthese;
    private Parametres parametres;

    public Panel(Parametres parametres, EcouteurUpdateClose callBackSynthese) {
        this.initComponents();
        this.parametres = parametres;
        this.callBackSynthese = callBackSynthese;
        this.icones = new Icones();

        setClient();
        setContactEtBanques();
        setTypeFacture();
        //Liste d'articles
        setBoutonsOutilsArticles();
        setMenuContArticles();
        //Liste des paiements - Rélévé de compte
        parametrerTableArticles();
        parametrerTablePaiement();
        actualiserTotaux();

        this.dateFacture.setDate(new Date());
        this.chTva.setText(this.parametres.getTva() + "");
    }

    private void setClient() {
        if (this.parametres.getClient() != null) {
            ClientFacture  client = this.parametres.getClient();
            labNomClient.setIcon(icones.getClient_01());
            labNomClient.setText(client.getNom());

            labTelephone.setIcon(icones.getTéléphone_01());
            labTelephone.setText(client.getTelephone());

            labAdresseClient.setIcon(icones.getAdresse_01());
            labAdresseClient.setText(client.getAdresse());
        }
    }

    private void setContactEtBanques() {
        if (this.parametres.getEntreprise() != null) {
            EntrepriseFacture entreprise = this.parametres.getEntreprise();
            labContactNom.setText(entreprise.getNom());
            labContactAdresse.setText(entreprise.getAdresse());
            labContactEmails.setText(entreprise.getEmail());
            labContactSiteWeb.setText(entreprise.getSiteWeb());
            labContactTelephone.setText(entreprise.getTelephone());
            
            labDetailBanque.setText(entreprise.getBanque());
            labDetailIntitule.setText(entreprise.getIntituleCompte());
            labDetailsIBAN.setText(entreprise.getIBAN());
            labDetailsNumero.setText(entreprise.getNumeroCompte());
            labDetailsSwiftCode.setText(entreprise.getCodeSwift());
        }
    }

    private void parametrerTableArticles() {
        this.modeleListeArticles = new ModeleListeArticles(this.scrollListeArticles, this.parametres.getTva(), new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                actualiserTotaux();
            }
        });

        //On charge les données s'il y en a
        if (this.parametres.getDonnees() != null) {
            this.modeleListeArticles.setListeArticles(this.parametres.getDonnees().getArticles());
        }

        //Parametrage du modele contenant les données de la table
        this.tableListeArticle.setModel(this.modeleListeArticles);

        //Parametrage du rendu de la table
        this.tableListeArticle.setDefaultRenderer(Object.class, new RenduTableArticle(this.parametres.getMonnaie(), icones.getModifier_01()));
        this.tableListeArticle.setRowHeight(25);

        TableColumn colNomArt = this.tableListeArticle.getColumnModel().getColumn(0);
        colNomArt.setCellEditor(new EditeurArticleFacture(this.parametres.getListArticles(), this.modeleListeArticles));

        TableColumn col_Qt = this.tableListeArticle.getColumnModel().getColumn(1);
        TableColumn col_PU_avant_Rabais = this.tableListeArticle.getColumnModel().getColumn(2);
        TableColumn col_Rabais = this.tableListeArticle.getColumnModel().getColumn(3);
        TableColumn col_PU_aprs_Rabais = this.tableListeArticle.getColumnModel().getColumn(4);
        TableColumn col_Tva_mnt = this.tableListeArticle.getColumnModel().getColumn(5);
        TableColumn col_TTC_mnt = this.tableListeArticle.getColumnModel().getColumn(6);

        colNomArt.setPreferredWidth(300);
        col_Qt.setPreferredWidth(80);
        col_PU_avant_Rabais.setPreferredWidth(150);
        col_Rabais.setPreferredWidth(150);
        col_PU_aprs_Rabais.setPreferredWidth(150);
        col_Tva_mnt.setPreferredWidth(150);
        col_TTC_mnt.setPreferredWidth(150);

        appliquerTva();
    }

    private void parametrerTablePaiement() {
        this.modeleListePaiement = new ModeleListePaiement(this.scrollListeReleveCompte, this.modeleListeArticles, new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                actualiserTotaux();
            }
        });

        //On charge les données s'il y en a
        if (this.parametres.getDonnees() != null) {
            this.modeleListePaiement.setListePaiements(this.parametres.getDonnees().getPaiements());
        }

        //Parametrage du modele contenant les données de la table
        this.tableListePaiement.setModel(this.modeleListePaiement);

        //Parametrage du rendu de la table
        this.tableListePaiement.setDefaultRenderer(Object.class, new RenduTablePaiement(this.parametres.getMonnaie(), icones.getModifier_01()));
        this.tableListePaiement.setRowHeight(25);

        TableColumn col_Date = this.tableListePaiement.getColumnModel().getColumn(0);
        col_Date.setCellEditor(new EditeurDatePaiement(this.parametres.getListArticles(), this.modeleListePaiement));

        TableColumn col_Article = this.tableListePaiement.getColumnModel().getColumn(1);
        col_Article.setCellEditor(new EditeurArticlePaiement(this.parametres.getListArticles(), this.modeleListeArticles, this.modeleListePaiement));

        TableColumn col_Depositaire = this.tableListePaiement.getColumnModel().getColumn(2);
        TableColumn col_Montant = this.tableListePaiement.getColumnModel().getColumn(3);
        TableColumn col_Reste = this.tableListePaiement.getColumnModel().getColumn(4);

        col_Date.setPreferredWidth(200);
        col_Article.setPreferredWidth(200);
        col_Depositaire.setPreferredWidth(200);
        col_Montant.setPreferredWidth(100);
        col_Reste.setPreferredWidth(100);
    }

    private void appliquerTva() {
        if (this.parametres.getTva() == 0) {
            combVatRule.setSelectedIndex(1);
        } else {
            combVatRule.setSelectedIndex(0);
        }
    }

    private void exporterPDF() {

    }

    private void exporterWORD() {

    }

    private void fermer() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir fermer cette fenêtre?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            callBackSynthese.onFermer();
        }
    }

    private void imprimer() {

    }

    private void enregistrer() {
        EcouteurFacture ef = this.parametres.getEcouteurFacture();
        if (ef != null) {
            ef.onEnregistre(this.parametres.getClient(), this.modeleListeArticles.getListeData(), this.modeleListePaiement.getListeData());
            fermer();
        }
    }

    private void actualiserTotaux() {
        double Tpaye = 0;
        if (modeleListePaiement != null) {
            Tpaye = modeleListePaiement.getTotalMontant();
        }
        double Tnet = modeleListeArticles.getTotal_Net();
        double Ttva = modeleListeArticles.getTotal_TVA();
        double Tttc = modeleListeArticles.getTotal_TTC();
        double Trab = modeleListeArticles.getTotal_Rabais();
        double Tsolde = Util.round(Tttc - Tpaye, 2);

        labTotalHT.setText(Tnet + " " + this.parametres.getMonnaie());
        labRemise.setText("-" + Trab + " " + this.parametres.getMonnaie());
        labTotalTVA.setText(Ttva + " " + this.parametres.getMonnaie());
        labTotalTTC.setText(Tttc + " " + this.parametres.getMonnaie());
        labTotalPaye.setText(Tpaye + " " + this.parametres.getMonnaie());
        labTotalSolde.setText(Tsolde + " " + this.parametres.getMonnaie());

        callBackSynthese.onActualiser("Mnt TTC (" + Tttc + " " + this.parametres.getMonnaie() + "), Mnt payé (" + Tpaye + " " + this.parametres.getMonnaie() + "), Solde (" + Tsolde + " " + this.parametres.getMonnaie() + ").");
    }

    private void supprimer() {
        if (indexTabSelected == 0) {
            modeleListeArticles.SupprimerArticle(tableListeArticle.getSelectedRow());
        } else {
            modeleListePaiement.SupprimerPaiement(tableListePaiement.getSelectedRow(), true);
        }
    }

    private void vider() {
        if (indexTabSelected == 0) {
            modeleListeArticles.viderListe();
        } else {
            modeleListePaiement.viderListe();
        }
    }

    private void ajouter() {
        if (indexTabSelected == 0) {
            this.parametres.getEcouteurAjout().setAjoutArticle(modeleListeArticles);
        } else {
            if (modeleListeArticles.getRowCount() != 0) {
                this.parametres.getEcouteurAjout().setAjoutPaiement(modeleListePaiement);
            } else {
                JOptionPane.showMessageDialog(tabPrincipal, "Aucun article n'a été séléctionné !");
            }
        }
    }

    private void setMenuContArticles() {
        rubAjouter = new RubriqueSimple("Ajouter", icones.getAjouter_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                ajouter();
            }
        });
        rubSupprimer = new RubriqueSimple("Supprimer", icones.getSupprimer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                supprimer();
            }
        });

        rubVider = new RubriqueSimple("Vider", icones.getAnnuler_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                vider();
            }
        });

        rubActualiser = new RubriqueSimple("Actualiser", icones.getSynchroniser_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                actualiserTotaux();
            }
        });

        rubImprimer = new RubriqueSimple("Imprimer", icones.getImprimer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                imprimer();
            }
        });

        rubFermer = new RubriqueSimple("Fermer", icones.getFermer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                fermer();
            }
        });

        rubPDF = new RubriqueSimple("Exp. PDF", icones.getPDF_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                exporterPDF();
            }
        });

        rubWRD = new RubriqueSimple("Exp. Word", icones.getMS_Word_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                exporterWORD();
            }
        });

        rubEnregistrer = new RubriqueSimple("Enreg & Fermer", icones.getEnregistrer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                enregistrer();
            }
        });

        menuContextuel = new MenuContextuel();
        menuContextuel.Ajouter(rubAjouter);
        menuContextuel.Ajouter(rubSupprimer);
        menuContextuel.Ajouter(rubVider);
        menuContextuel.Ajouter(rubActualiser);
        menuContextuel.Ajouter(new JPopupMenu.Separator());
        menuContextuel.Ajouter(rubImprimer);
        menuContextuel.Ajouter(rubPDF);
        menuContextuel.Ajouter(rubWRD);
        menuContextuel.Ajouter(new JPopupMenu.Separator());
        menuContextuel.Ajouter(rubEnregistrer);
        menuContextuel.Ajouter(rubFermer);
    }

    private void setBoutonsOutilsArticles() {
        //BtAjouter
        btAjouter = new Bouton(12, "Ajouter", icones.getAjouter_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                ajouter();
            }
        });
        //BtSupprimer
        btSupprimer = new Bouton(12, "Supprimer", icones.getSupprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                supprimer();
            }
        });

        //Vider
        btVider = new Bouton(12, "Vider", icones.getAnnuler_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                vider();
            }
        });

        //Actualiser
        btActualiser = new Bouton(12, "Actualiser", icones.getSynchroniser_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                actualiserTotaux();
            }
        });

        //Imprimer
        btImprimer = new Bouton(12, "Imprimer", icones.getImprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                imprimer();
            }
        });

        //Export. PDF
        btPDF = new Bouton(12, "Exp. PDF", icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                exporterPDF();
            }
        });

        //Export. MS WORD
        btWORD = new Bouton(12, "Exp. Word", icones.getMS_Word_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                exporterWORD();
            }
        });

        //Fermer
        btFermer = new Bouton(12, "Fermer", icones.getFermer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                fermer();
            }
        });

        //Fermer
        btEnregistrer = new Bouton(12, "Enreg. & Fermer", icones.getEnregistrer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                enregistrer();
            }
        });

        barreOutilsA = new BarreOutils(barreOutilsArticles);
        barreOutilsA.AjouterBouton(btAjouter);
        barreOutilsA.AjouterBouton(btSupprimer);
        barreOutilsA.AjouterBouton(btVider);
        barreOutilsA.AjouterBouton(btActualiser);
        barreOutilsA.AjouterSeparateur();
        barreOutilsA.AjouterBouton(btImprimer);
        barreOutilsA.AjouterBouton(btPDF);
        barreOutilsA.AjouterBouton(btWORD);
        barreOutilsA.AjouterSeparateur();
        barreOutilsA.AjouterBouton(btEnregistrer);
        barreOutilsA.AjouterBouton(btFermer);
    }

    private void ecouterMenContA(java.awt.event.MouseEvent evt, boolean isArticle) {
        if (evt.getButton() == MouseEvent.BUTTON3) {
            if (isArticle == true) {
                menuContextuel.afficher(scrollListeArticles, evt.getX(), evt.getY());
            } else {
                menuContextuel.afficher(scrollListeReleveCompte, evt.getX(), evt.getY());
            }
        }

        if (isArticle == true) {
            ArticleFacture artcl = modeleListeArticles.getArticle(tableListeArticle.getSelectedRow());
            if (artcl != null) {
                this.callBackSynthese.onActualiser(artcl.getNom() + ", " + artcl.getQte() + " " + artcl.getUnite() + ", Total TTC : " + artcl.getTotalTTC() + " " + this.parametres.getMonnaie());
            }
        } else {
            PaiementFacture paiment = modeleListePaiement.getPaiement(tableListePaiement.getSelectedRow());
            if (paiment != null) {
                this.callBackSynthese.onActualiser(paiment.getDate().toLocaleString() + ", " + paiment.getNomDepositaire() + " a payé " + paiment.getMontant() + " " + this.parametres.getMonnaie() + " pour " + paiment.getNomArticle() + ", reste (" + modeleListePaiement.getReste(paiment.getIdArticle()) + " " + this.parametres.getMonnaie() + ").");
            }
        }

    }

    private void setTypeFacture() {
        labTypeFacture.setText(comboTypeFacture.getSelectedItem() + " n°" + this.parametres.getNumero());
    }

    private void changeVatRule() {
        if (combVatRule.getSelectedIndex() == 0) {//Ce client paie la TVA
            try {
                chTva.setEditable(true);
                double pourcTva = Double.parseDouble(chTva.getText().trim());
                modeleListeArticles.setVat(pourcTva);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {//Ce client ne paie pas la TVA
            chTva.setEditable(false);
            modeleListeArticles.setVat(0);
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

        jPanel1 = new javax.swing.JPanel();
        labNomEse = new javax.swing.JLabel();
        labAdresseEse = new javax.swing.JLabel();
        labTelephoneEse = new javax.swing.JLabel();
        labEmailEse = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        labTypeFacture = new javax.swing.JLabel();
        comboTypeFacture = new javax.swing.JComboBox<>();
        dateFacture = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labNomClient = new javax.swing.JLabel();
        labTelephone = new javax.swing.JLabel();
        chTva = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        combVatRule = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        labAdresseClient = new javax.swing.JLabel();
        labLogo = new javax.swing.JLabel();
        panSynthese = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        labTotalHT = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        labTotalTVA = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        labTotalTTC = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        labTotalSolde = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        labTotalPaye = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        labRemise = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        barreOutilsArticles = new javax.swing.JToolBar();
        jButton5 = new javax.swing.JButton();
        panDetailsBancaires = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        labDetailBanque = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        labDetailIntitule = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        labDetailsNumero = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        labDetailsIBAN = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        labDetailsSwiftCode = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        panContacts = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        labContactNom = new javax.swing.JLabel();
        labContactTelephone = new javax.swing.JLabel();
        labContactEmails = new javax.swing.JLabel();
        labContactSiteWeb = new javax.swing.JLabel();
        labContactAdresse = new javax.swing.JLabel();
        tabPrincipal = new javax.swing.JTabbedPane();
        scrollListeArticles = new javax.swing.JScrollPane();
        tableListeArticle = new javax.swing.JTable();
        scrollListeReleveCompte = new javax.swing.JScrollPane();
        tableListePaiement = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        labNomEse.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labNomEse.setText("UAP RDC Sarl");

        labAdresseEse.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labAdresseEse.setForeground(new java.awt.Color(153, 153, 153));
        labAdresseEse.setText("167B, Itaga, Lingwala, Kinshasa/RDC");

        labTelephoneEse.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labTelephoneEse.setForeground(new java.awt.Color(153, 153, 153));
        labTelephoneEse.setText("(+243)844803514");

        labEmailEse.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labEmailEse.setForeground(new java.awt.Color(153, 153, 153));
        labEmailEse.setText("Info@aib-brokers.com");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labNomEse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labAdresseEse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labTelephoneEse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labEmailEse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(labNomEse)
                .addGap(0, 0, 0)
                .addComponent(labAdresseEse)
                .addGap(0, 0, 0)
                .addComponent(labTelephoneEse)
                .addGap(0, 0, 0)
                .addComponent(labEmailEse)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        labTypeFacture.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labTypeFacture.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labTypeFacture.setText("Facture No. 123");

        comboTypeFacture.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Facture", "Facture pro forma", "Bon de Commande", "Bon d'entrée en Stock", "Bon de sortie du Stock" }));
        comboTypeFacture.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboTypeFactureItemStateChanged(evt);
            }
        });

        dateFacture.setDateFormatString("dd/MM/yyyy HH:mm:ss");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labTypeFacture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateFacture, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                    .addComponent(comboTypeFacture, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(labTypeFacture)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(dateFacture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(comboTypeFacture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Client :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Téléphone :");

        labNomClient.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labNomClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labNomClient.setText("SULA BOSIO Serge");

        labTelephone.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labTelephone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labTelephone.setText("(+243) 84 480 35 14, (+243) 82 87 27 706");

        chTva.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        chTva.setText("16");
        chTva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                chTvaKeyReleased(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Tva (%) :");

        combVatRule.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ce client n’est pas exonéré de la Taxe sur la Valeur Ajoutée.", "Ce client est exonéré de la Taxe sur la Valeur Ajoutée." }));
        combVatRule.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combVatRuleItemStateChanged(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Adresse :");

        labAdresseClient.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labAdresseClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labAdresseClient.setText("167b, Av. ITAGA, C. LINGWALA, KINSHASA, RDC");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labNomClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(chTva, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(combVatRule, 0, 1, Short.MAX_VALUE))
                            .addComponent(labTelephone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labAdresseClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(labNomClient))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(labTelephone))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(labAdresseClient))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(chTva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(combVatRule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture03.png"))); // NOI18N

        panSynthese.setBackground(new java.awt.Color(255, 255, 255));
        panSynthese.setBorder(javax.swing.BorderFactory.createTitledBorder("Synthèse"));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel5.setText("Total HT");

        labTotalHT.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labTotalHT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labTotalHT.setText("2539.90 $");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel7.setText("TVA (16%)");

        labTotalTVA.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labTotalTVA.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labTotalTVA.setText("507.98 $");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel9.setText("Total TTC");

        labTotalTTC.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labTotalTTC.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labTotalTTC.setText("2946.28 $");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel18.setText("Solde");

        labTotalSolde.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labTotalSolde.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labTotalSolde.setText("2946.28 $");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 153, 51));
        jLabel17.setText("Total payé");

        labTotalPaye.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labTotalPaye.setForeground(new java.awt.Color(0, 153, 51));
        labTotalPaye.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labTotalPaye.setText("2946.28 $");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 0, 0));
        jLabel6.setText("Remise");

        labRemise.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labRemise.setForeground(new java.awt.Color(204, 0, 0));
        labRemise.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labRemise.setText("- 100 $");

        javax.swing.GroupLayout panSyntheseLayout = new javax.swing.GroupLayout(panSynthese);
        panSynthese.setLayout(panSyntheseLayout);
        panSyntheseLayout.setHorizontalGroup(
            panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSyntheseLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panSyntheseLayout.createSequentialGroup()
                        .addGroup(panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labTotalHT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labTotalTVA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labRemise, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panSyntheseLayout.createSequentialGroup()
                        .addGroup(panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labTotalTTC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labTotalSolde, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                            .addComponent(labTotalPaye, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(3, 3, 3))
        );
        panSyntheseLayout.setVerticalGroup(
            panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSyntheseLayout.createSequentialGroup()
                .addGroup(panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(labRemise))
                .addGap(0, 0, 0)
                .addGroup(panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(labTotalHT))
                .addGap(0, 0, 0)
                .addGroup(panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(labTotalTVA))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(labTotalTTC))
                .addGap(0, 0, 0)
                .addGroup(panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(labTotalPaye))
                .addGap(0, 0, 0)
                .addGroup(panSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(labTotalSolde))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Imprimer également le relevé de compte");

        barreOutilsArticles.setBackground(new java.awt.Color(255, 255, 255));
        barreOutilsArticles.setFloatable(false);
        barreOutilsArticles.setRollover(true);

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture02.png"))); // NOI18N
        jButton5.setText("Ajouter");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        barreOutilsArticles.add(jButton5);

        panDetailsBancaires.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Détails bancaires");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(153, 153, 153));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Banque :");

        labDetailBanque.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labDetailBanque.setForeground(new java.awt.Color(153, 153, 153));
        labDetailBanque.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labDetailBanque.setText("Equity Bank Congo SA");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(153, 153, 153));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Intitulé :");

        labDetailIntitule.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labDetailIntitule.setForeground(new java.awt.Color(153, 153, 153));
        labDetailIntitule.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labDetailIntitule.setText("UAP RDC Sarl");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(153, 153, 153));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Numéro :");

        labDetailsNumero.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labDetailsNumero.setForeground(new java.awt.Color(153, 153, 153));
        labDetailsNumero.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labDetailsNumero.setText("00018000010267415120011");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(153, 153, 153));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("IBAN :");

        labDetailsIBAN.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labDetailsIBAN.setForeground(new java.awt.Color(153, 153, 153));
        labDetailsIBAN.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labDetailsIBAN.setText("PRCBCDKI");

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(153, 153, 153));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Code Swift :");

        labDetailsSwiftCode.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labDetailsSwiftCode.setForeground(new java.awt.Color(153, 153, 153));
        labDetailsSwiftCode.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labDetailsSwiftCode.setText("PRCBCDKI");

        javax.swing.GroupLayout panDetailsBancairesLayout = new javax.swing.GroupLayout(panDetailsBancaires);
        panDetailsBancaires.setLayout(panDetailsBancairesLayout);
        panDetailsBancairesLayout.setHorizontalGroup(
            panDetailsBancairesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDetailsBancairesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panDetailsBancairesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panDetailsBancairesLayout.createSequentialGroup()
                        .addGroup(panDetailsBancairesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                        .addGap(5, 5, 5)
                        .addGroup(panDetailsBancairesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labDetailBanque, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labDetailIntitule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labDetailsNumero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labDetailsIBAN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labDetailsSwiftCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        panDetailsBancairesLayout.setVerticalGroup(
            panDetailsBancairesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDetailsBancairesLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel11)
                .addGap(0, 0, 0)
                .addGroup(panDetailsBancairesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(labDetailBanque))
                .addGap(0, 0, 0)
                .addGroup(panDetailsBancairesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(labDetailIntitule))
                .addGap(0, 0, 0)
                .addGroup(panDetailsBancairesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(labDetailsNumero))
                .addGap(0, 0, 0)
                .addGroup(panDetailsBancairesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(labDetailsIBAN))
                .addGap(0, 0, 0)
                .addGroup(panDetailsBancairesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(labDetailsSwiftCode))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panContacts.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("Contact");

        labContactNom.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labContactNom.setForeground(new java.awt.Color(153, 153, 153));
        labContactNom.setText("UAP RDC Sarl");

        labContactTelephone.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labContactTelephone.setForeground(new java.awt.Color(153, 153, 153));
        labContactTelephone.setText("(+243)84 480 35 14, (+243)82 87 27 706");

        labContactEmails.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labContactEmails.setForeground(new java.awt.Color(153, 153, 153));
        labContactEmails.setText("ssula@s2b-simple.com; sulabosiog@gmail.com");

        labContactSiteWeb.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labContactSiteWeb.setForeground(new java.awt.Color(153, 153, 153));
        labContactSiteWeb.setText("www.s2b-simple.com");

        labContactAdresse.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labContactAdresse.setForeground(new java.awt.Color(153, 153, 153));
        labContactAdresse.setText("167b, Av. ITAGA, C. LINGWALA, KINSHASA");

        javax.swing.GroupLayout panContactsLayout = new javax.swing.GroupLayout(panContacts);
        panContacts.setLayout(panContactsLayout);
        panContactsLayout.setHorizontalGroup(
            panContactsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panContactsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panContactsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labContactNom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labContactTelephone, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labContactEmails, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labContactSiteWeb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labContactAdresse, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        panContactsLayout.setVerticalGroup(
            panContactsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panContactsLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel12)
                .addGap(0, 0, 0)
                .addComponent(labContactNom)
                .addGap(0, 0, 0)
                .addComponent(labContactAdresse)
                .addGap(0, 0, 0)
                .addComponent(labContactTelephone)
                .addGap(0, 0, 0)
                .addComponent(labContactEmails)
                .addGap(0, 0, 0)
                .addComponent(labContactSiteWeb)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabPrincipal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabPrincipalStateChanged(evt);
            }
        });

        scrollListeArticles.setBackground(new java.awt.Color(255, 255, 255));
        scrollListeArticles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollListeArticlesMouseClicked(evt);
            }
        });

        tableListeArticle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Article", "Qunatité", "Unités", "Prix Unitaire HT", "Tva %", "Tva", "Total TTC"
            }
        ));
        tableListeArticle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListeArticleMouseClicked(evt);
            }
        });
        scrollListeArticles.setViewportView(tableListeArticle);
        if (tableListeArticle.getColumnModel().getColumnCount() > 0) {
            tableListeArticle.getColumnModel().getColumn(0).setResizable(false);
            tableListeArticle.getColumnModel().getColumn(0).setPreferredWidth(100);
            tableListeArticle.getColumnModel().getColumn(1).setResizable(false);
            tableListeArticle.getColumnModel().getColumn(2).setResizable(false);
            tableListeArticle.getColumnModel().getColumn(3).setResizable(false);
            tableListeArticle.getColumnModel().getColumn(4).setResizable(false);
            tableListeArticle.getColumnModel().getColumn(6).setResizable(false);
            tableListeArticle.getColumnModel().getColumn(6).setPreferredWidth(50);
        }

        tabPrincipal.addTab("Facture", scrollListeArticles);

        scrollListeReleveCompte.setBackground(new java.awt.Color(255, 255, 255));
        scrollListeReleveCompte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollListeReleveCompteMouseClicked(evt);
            }
        });

        tableListePaiement.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Date", "Dépositaire", "Montant"
            }
        ));
        tableListePaiement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListePaiementMouseClicked(evt);
            }
        });
        scrollListeReleveCompte.setViewportView(tableListePaiement);
        if (tableListePaiement.getColumnModel().getColumnCount() > 0) {
            tableListePaiement.getColumnModel().getColumn(0).setResizable(false);
            tableListePaiement.getColumnModel().getColumn(0).setPreferredWidth(50);
            tableListePaiement.getColumnModel().getColumn(1).setResizable(false);
            tableListePaiement.getColumnModel().getColumn(2).setResizable(false);
            tableListePaiement.getColumnModel().getColumn(2).setPreferredWidth(50);
        }

        tabPrincipal.addTab("Relevé de compte", scrollListeReleveCompte);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panDetailsBancaires, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(5, 5, 5)
                        .addComponent(panContacts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panSynthese, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(labLogo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tabPrincipal, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barreOutilsArticles, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
            .addComponent(jSeparator4)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(barreOutilsArticles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tabPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(panSynthese, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panDetailsBancaires, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panContacts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboTypeFactureItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboTypeFactureItemStateChanged
        // TODO add your handling code here:
        setTypeFacture();
    }//GEN-LAST:event_comboTypeFactureItemStateChanged

    private void scrollListeArticlesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListeArticlesMouseClicked
        // TODO add your handling code here:
        ecouterMenContA(evt, true);
    }//GEN-LAST:event_scrollListeArticlesMouseClicked

    private void tableListeArticleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeArticleMouseClicked
        // TODO add your handling code here:
        ecouterMenContA(evt, true);
    }//GEN-LAST:event_tableListeArticleMouseClicked

    private void combVatRuleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combVatRuleItemStateChanged
        // TODO add your handling code here:
        changeVatRule();
    }//GEN-LAST:event_combVatRuleItemStateChanged

    private void chTvaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chTvaKeyReleased
        // TODO add your handling code here:
        changeVatRule();
    }//GEN-LAST:event_chTvaKeyReleased

    private void tabPrincipalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPrincipalStateChanged
        // TODO add your handling code here:
        JTabbedPane tab = (JTabbedPane) evt.getSource();
        this.indexTabSelected = tab.getSelectedIndex();
        System.out.println("Tab changed to : " + tab.getTitleAt(indexTabSelected));
    }//GEN-LAST:event_tabPrincipalStateChanged

    private void tableListePaiementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListePaiementMouseClicked
        // TODO add your handling code here:
        ecouterMenContA(evt, false);
    }//GEN-LAST:event_tableListePaiementMouseClicked

    private void scrollListeReleveCompteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListeReleveCompteMouseClicked
        // TODO add your handling code here:
        ecouterMenContA(evt, false);
    }//GEN-LAST:event_scrollListeReleveCompteMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barreOutilsArticles;
    private javax.swing.JTextField chTva;
    private javax.swing.JComboBox<String> combVatRule;
    private javax.swing.JComboBox<String> comboTypeFacture;
    private com.toedter.calendar.JDateChooser dateFacture;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel labAdresseClient;
    private javax.swing.JLabel labAdresseEse;
    private javax.swing.JLabel labContactAdresse;
    private javax.swing.JLabel labContactEmails;
    private javax.swing.JLabel labContactNom;
    private javax.swing.JLabel labContactSiteWeb;
    private javax.swing.JLabel labContactTelephone;
    private javax.swing.JLabel labDetailBanque;
    private javax.swing.JLabel labDetailIntitule;
    private javax.swing.JLabel labDetailsIBAN;
    private javax.swing.JLabel labDetailsNumero;
    private javax.swing.JLabel labDetailsSwiftCode;
    private javax.swing.JLabel labEmailEse;
    private javax.swing.JLabel labLogo;
    private javax.swing.JLabel labNomClient;
    private javax.swing.JLabel labNomEse;
    private javax.swing.JLabel labRemise;
    private javax.swing.JLabel labTelephone;
    private javax.swing.JLabel labTelephoneEse;
    private javax.swing.JLabel labTotalHT;
    private javax.swing.JLabel labTotalPaye;
    private javax.swing.JLabel labTotalSolde;
    private javax.swing.JLabel labTotalTTC;
    private javax.swing.JLabel labTotalTVA;
    private javax.swing.JLabel labTypeFacture;
    private javax.swing.JPanel panContacts;
    private javax.swing.JPanel panDetailsBancaires;
    private javax.swing.JPanel panSynthese;
    private javax.swing.JScrollPane scrollListeArticles;
    private javax.swing.JScrollPane scrollListeReleveCompte;
    private javax.swing.JTabbedPane tabPrincipal;
    private javax.swing.JTable tableListeArticle;
    private javax.swing.JTable tableListePaiement;
    // End of variables declaration//GEN-END:variables
}