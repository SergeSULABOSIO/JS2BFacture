/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.UI;

import BEAN_BARRE_OUTILS.BarreOutils;
import BEAN_BARRE_OUTILS.Bouton;
import BEAN_BARRE_OUTILS.BoutonListener;
import BEAN_MenuContextuel.MenuContextuel;
import BEAN_MenuContextuel.RubriqueListener;
import BEAN_MenuContextuel.RubriqueSimple;
import ICONES.Icones;
import SOURCES.CallBackFacture.EcouteurActualisationFacture;
import SOURCES.CallBackFacture.EcouteurAjoutFacture;
import SOURCES.CallBackFacture.EcouteurFacture;
import SOURCES.EditeursTable.EditeurFraisFacture;
import SOURCES.EditeursTable.EditeurDateFacture;
import SOURCES.EditeursTable.EditeurModeFacture;
import SOURCES.EditeursTable.EditeurPeriodeFacture;
import SOURCES.ModelsTable.ModeleListeFrais;
import SOURCES.ModelsTable.ModeleListePaiement;
import SOURCES.Utilitaires_Facture.ParametresFacture;
import SOURCES.RendusTable.RenduTableFrais;
import SOURCES.RendusTable.RenduTablePaiement;
import SOURCES.GenerateurPDF.DocumentPDFFacture;
import SOURCES.ModelsTable.ModeleListeEcheance;
import SOURCES.Utilitaires_Facture.UtilFacture;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.table.TableColumn;
import SOURCES.RendusTable.RenduTableEcheance;
import SOURCES.Utilitaires_Facture.DataFacture;
import SOURCES.Utilitaires_Facture.DonneesFacture;
import SOURCES.Utilitaires_Facture.SortiesFacture;
import Source.Callbacks.EcouteurEnregistrement;
import Source.Callbacks.EcouteurSuppressionElement;
import Source.Callbacks.EcouteurUpdateClose;
import Source.Callbacks.EcouteurValeursChangees;
import Source.GestionClickDroit;
import Source.GestionEdition;
import Source.Interface.InterfaceFrais;
import Source.Interface.InterfacePaiement;
import Source.Interface.InterfaceUtilisateur;
import Source.Objet.CouleurBasique;
import Source.Objet.Echeance;
import Source.Objet.Eleve;
import Source.Objet.Exercice;
import Source.Objet.Frais;
import Source.Objet.Monnaie;
import Source.Objet.Paiement;
import Source.Objet.Periode;
import Source.Objet.Utilisateur;
import java.awt.Color;
import java.util.Vector;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author HP Pavilion
 */
public class PanelContenuFacture extends javax.swing.JPanel {

    /**
     * Creates new form FacturePanel
     */
    private int indexTabSelected = 0;
    private boolean isAssigetis = true;
    private Icones icones = null;
    private PanelContenuFacture moi = null;
    private BarreOutils barreOutilsA = null;
    private MenuContextuel menuContextuel = null;
    private RubriqueSimple rubAjouter, rubSupprimer, rubVider, rubActualiser, rubImprimer, rubPDF, rubFermer, rubEnregistrer, rubEdition, rubRecu = null;
    private Bouton btAjouter, btSupprimer, btVider, btActualiser, btImprimer, btPDF, btFermer, btEnregistrer, btRecu, btEdition;

    private ModeleListeFrais modeleListeArticles = null;
    private ModeleListePaiement modeleListePaiement = null;
    private ModeleListeEcheance modeleListeEcheance = null;
    private EditeurFraisFacture editeurArticle = null;
    private EcouteurUpdateClose ecouteurClose;
    private EcouteurFacture ecouteurFacture = null;
    private EcouteurAjoutFacture ecouteurAjout;

    private Vector<Paiement> paiementsSelected = new Vector<Paiement>();
    private Frais SelectedArticle = null;
    private Paiement SelectedPaiement = null;
    private Echeance SelectedEcheance = null;
    public DataFacture dataFacture;
    private Date dateFacture = null;
    private CouleurBasique couleurBasique;
    private JProgressBar progress;
    public EcouteurActualisationFacture ecouteurActualisationFacture;
    private GestionEdition gestionEdition = new GestionEdition();

    public PanelContenuFacture(CouleurBasique couleurBasique, JProgressBar progress, DataFacture dataFacture, EcouteurFacture ecouteurFacture, EcouteurUpdateClose ecouteurClose) {
        initComponents();
        this.progress = progress;
        this.couleurBasique = couleurBasique;
        this.dataFacture = dataFacture;
        this.ecouteurClose = ecouteurClose;
        this.ecouteurFacture = ecouteurFacture;
        this.init();

        parametrerTableArticles();
        parametrerTablePaiement();
        parametrerTableEcheance();
        actualiserTotaux();
        activerBoutons(tabPrincipal.getSelectedIndex());
        tabPrincipal.setSelectedIndex(1);
        ecouterClickDroit();
    }

    private void actualiserEditeur() {
        gestionEdition.reinitialiser();
        modeleListePaiement.actualiser();
    }

    private void setEditionMode() {
        switch (indexTabSelected) {
            case 1:
                if (SelectedPaiement != null && gestionEdition != null) {
                    if (gestionEdition.isEditable(SelectedPaiement.getId(), indexTabSelected)) {
                        gestionEdition.setModeEdition(SelectedPaiement.getId(), indexTabSelected, false);
                    } else {
                        gestionEdition.setModeEdition(SelectedPaiement.getId(), indexTabSelected, true);
                    }
                    modeleListePaiement.actualiser();
                }
                break;
            default:
        }
    }

    private void ecouterClickDroit() {
        new GestionClickDroit(menuContextuel, tableListeArticle, scrollListeArticles).init();
        new GestionClickDroit(menuContextuel, tableListeEcheance, scrollListeEcheances).init();
        new GestionClickDroit(menuContextuel, tableListePaiement, scrollListePaiement).init();
    }

    public void setBtEnregistrerNouveau() {
        if (rubEnregistrer != null && btEnregistrer != null) {
            rubEnregistrer.setCouleur(couleurBasique.getCouleur_foreground_objet_nouveau());                                        //mEnreg.setCouleur(Color.blue);
            btEnregistrer.setForeground(couleurBasique.getCouleur_foreground_objet_nouveau());
        }
    }

    public EcouteurActualisationFacture getEcouteurActualisationFacture() {
        return ecouteurActualisationFacture;
    }

    public void setEcouteurActualisationFacture(EcouteurActualisationFacture ecouteurActualisationFacture) {
        this.ecouteurActualisationFacture = ecouteurActualisationFacture;
    }

    private void init() {
        this.icones = new Icones();
        this.moi = this;
        this.labTelephone.setIcon(icones.getTéléphone_01());
        this.labNomClient.setIcon(icones.getClient_01());
        this.labTotalPaye.setIcon(icones.getCaisse_01());
        this.labTotalSolde.setIcon(icones.getCaisse_01());
        this.labTotalTTC.setIcon(icones.getCaisse_01());
        this.tabPrincipal.setIconAt(0, icones.getTaxes_01());   //Frais
        this.tabPrincipal.setIconAt(1, icones.getClient_01());  //Relevé de compte
        this.tabPrincipal.setIconAt(2, icones.getCalendrier_01()); //Revenu
        PanelFacture.labStatus.setIcon(icones.getInfos_01());
        this.labAdresseClient.setIcon(icones.getAdresse_01());

        Eleve eleve = dataFacture.getDonneesFacture().getEleve();
        Exercice exercice = dataFacture.getParametresFacture().getExercice();
        if (eleve != null & exercice != null) {
            String Sexercice = exercice.getNom() + " [" + UtilFacture.getDateFrancais(exercice.getDebut()) + " - " + UtilFacture.getDateFrancais(exercice.getFin()) + "].";
            labNomClient.setText(eleve.getNom() + " " + eleve.getPostnom() + " " + eleve.getPrenom() + ", " + Sexercice);
            labTelephone.setText(eleve.getTelephonesParents());
            labAdresseClient.setText(eleve.getAdresse());
        }

        this.ecouteurAjout = new EcouteurAjoutFacture() {
            @Override
            public void setAjoutPaiement(ModeleListePaiement modeleListePaiement) {
                double montant = 0;
                Date newDate = new Date();
                Eleve eleveQuiPaie = dataFacture.getDonneesFacture().getEleve();
                if (eleveQuiPaie != null) {
                    String nomEleveQuiPaie = eleveQuiPaie.getNom() + " " + eleveQuiPaie.getPostnom() + " " + eleveQuiPaie.getPrenom();
                    modeleListePaiement.AjouterPaiement(new Paiement(-1, dataFacture.getParametresFacture().getExercice().getId(), eleveQuiPaie.getId(), -1, -1, nomEleveQuiPaie, "", nomEleveQuiPaie, montant, newDate, InterfacePaiement.MODE_CAISSE, newDate.getTime() + "", InterfacePaiement.BETA_NOUVEAU));
                }
            }

            @Override
            public void setAjoutEcheance(ModeleListeEcheance modeleListeEcheance) {
                Exercice exercice = dataFacture.getParametresFacture().getExercice();
                if (exercice != null) {
                    int nbEcheancesExistant = modeleListeEcheance.getRowCount();
                    String nomTransche = "1ère Tranche";
                    if (nbEcheancesExistant > 1) {
                        nomTransche = (nbEcheancesExistant + 1) + "ème Tranche";
                    }
                    modeleListeEcheance.AjouterEcheanceAutomatique(new Echeance(-1, nomTransche, -1, exercice.getDebut(), exercice.getFin(), "RAS", 0, 0, dataFacture.getParametresFacture().getMonnaieOutPut().getId()));
                    //modeleListeEcheance.AjouterEcheanceAutomatique(new Echeance(-1, nomTransche, parametres.getIdFacture(), exercice.getDebut(), exercice.getFin(), parametres.getNumero(), 0, 0, parametres.getMonnaieOutPut().getId()));
                }
            }
        };

        setBoutons();
        setMenuContextuel();
    }

    public Vector<Paiement> getPaiementsSelected() {
        return paiementsSelected;
    }

    public ParametresFacture getParametres() {
        return dataFacture.getParametresFacture();
    }

    public Date getDateFacture() {
        return dateFacture;
    }

    public int getIndexTabSelected() {
        return indexTabSelected;
    }

    public boolean isIsAssigetis() {
        return isAssigetis;
    }

    public ModeleListeFrais getModeleListeArticles() {
        return modeleListeArticles;
    }

    public ModeleListePaiement getModeleListePaiement() {
        return modeleListePaiement;
    }

    public ModeleListeEcheance getModeleListeEcheance() {
        return modeleListeEcheance;
    }

    public String getNomfichierPreuve() {
        return "Output.pdf";
    }

    public boolean isImprimerRelever() {
        return isReleverCompte.isSelected();
    }

    public boolean isImprimerPlanPaiement() {
        return isPlanPaiement.isSelected();
    }

    public DonneesFacture getDonneesFacture() {
        return dataFacture.getDonneesFacture();
    }

    private void initModelTableArticle() {
        this.modeleListeArticles = new ModeleListeFrais(couleurBasique, this.scrollListeArticles, btEnregistrer, rubEnregistrer, this.dataFacture.getParametresFacture(), new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                //Actualisation des listes de base
                if (modeleListeEcheance != null) {
                    modeleListeEcheance.actualiser();
                }
                actualiserTotaux();
            }
        });
        this.editeurArticle = new EditeurFraisFacture(this.dataFacture.getDonneesFacture().getArticles(), modeleListePaiement);
        this.tableListeArticle.setModel(this.modeleListeArticles);
    }

    private void chargerDataTableArticle() {
        //On charge les données s'il y en a
        if (this.dataFacture.getDonneesFacture() != null) {
            this.modeleListeArticles.setListeFrais(this.dataFacture.getDonneesFacture().getArticles());
        }
    }

    private void setTaille(TableColumn column, int taille, boolean fixe, TableCellEditor editor) {
        column.setPreferredWidth(taille);
        if (fixe == true) {
            column.setMaxWidth(taille);
            column.setMinWidth(taille);
        }
        if (editor != null) {
            column.setCellEditor(editor);
        }
    }

    private void fixerColonnesTableArticles(boolean resizeTable) {
        //{"N°", "Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC"};
        //Parametrage du rendu de la table
        this.tableListeArticle.setDefaultRenderer(Object.class, new RenduTableFrais(couleurBasique, this.dataFacture.getDonneesFacture(), this.dataFacture.getParametresFacture(), this.modeleListeArticles));
        this.tableListeArticle.setRowHeight(25);

        //{"N°", "Frais", "Montant"}; en plus il faurdra afficher son fractionnement selon les périodes
        setTaille(this.tableListeArticle.getColumnModel().getColumn(0), 30, true, null);
        setTaille(this.tableListeArticle.getColumnModel().getColumn(1), 600, false, null);
        setTaille(this.tableListeArticle.getColumnModel().getColumn(2), 80, true, null);

        //On écoute les sélction
        this.tableListeArticle.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    ecouterArticleSelectionne();
                    actualiserTotaux();
                }
            }
        });

        if (resizeTable == true) {
            this.tableListeArticle.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
    }

    private void ecouterArticleSelectionne() {
        int ligneSelected = tableListeArticle.getSelectedRow();
        if (ligneSelected != -1) {
            this.SelectedArticle = modeleListeArticles.getFrais(ligneSelected);
            if (SelectedArticle != null) {
                Monnaie Imon = UtilFacture.getMonnaie(dataFacture.getParametresFacture(), SelectedArticle.getIdMonnaie());
                if (Imon != null) {
                    this.ecouteurClose.onActualiser(SelectedArticle.getNom() + ", " + ", Total TTC : " + UtilFacture.getMontantFrancais(SelectedArticle.getMontantDefaut()) + " " + Imon.getCode(), icones.getTaxes_01());
                }
            }
        }
    }

    private void ecouterPaiementSelectionne() {
        int ligneSelected = tableListePaiement.getSelectedRow();
        if (ligneSelected != -1) {
            this.SelectedPaiement = modeleListePaiement.getPaiement(ligneSelected);
            if (SelectedPaiement != null) {
                Periode Iper = UtilFacture.getPeriode(dataFacture.getParametresFacture(), SelectedPaiement.getIdPeriode());
                Frais Iart = UtilFacture.getFrais(dataFacture.getDonneesFacture(), SelectedPaiement.getIdFrais());
                if (Iart != null) {
                    Monnaie Imon = UtilFacture.getMonnaie(dataFacture.getParametresFacture(), Iart.getIdMonnaie());
                    if (Iper != null && Imon != null) {
                        this.ecouteurClose.onActualiser(Iper.getNom() + ": " + UtilFacture.getDateFrancais(SelectedPaiement.getDate()) + ", ref.: " + SelectedPaiement.getReferenceTransaction() + ", montant : " + UtilFacture.getMontantFrancais(SelectedPaiement.getMontant()) + " " + Imon.getCode() + " pour " + SelectedPaiement.getNomFrais() + ", reste (" + UtilFacture.getMontantFrancais(modeleListePaiement.getReste(SelectedPaiement.getIdFrais(), SelectedPaiement.getIdPeriode())) + " " + Imon.getCode() + ").", icones.getClient_01());
                    }
                }
            }
        }
        chargerPaiementsSeletced();
    }

    private void ecouterEcheanceSelectionne() {
        int ligneSelected = tableListeEcheance.getSelectedRow();
        if (ligneSelected != -1) {
            this.SelectedEcheance = modeleListeEcheance.getEcheance_row(ligneSelected);
            if (SelectedEcheance != null) {
                this.ecouteurClose.onActualiser("Entre " + UtilFacture.getDateFrancais(SelectedEcheance.getDateInitiale()) + " et " + UtilFacture.getDateFrancais(SelectedEcheance.getDateFinale()) + ", il faut payer " + UtilFacture.getMontantFrancais(UtilFacture.round(SelectedEcheance.getMontantDu(), 2)) + " " + this.dataFacture.getParametresFacture().getMonnaieOutPut().getCode(), icones.getCalendrier_01());
            }
        }
    }

    private void parametrerTableArticles() {
        initModelTableArticle();
        chargerDataTableArticle();
        fixerColonnesTableArticles(true);
    }

    private void initModelTablePaiement() {
        this.modeleListePaiement = new ModeleListePaiement(gestionEdition, couleurBasique, this.scrollListePaiement, btEnregistrer, rubEnregistrer, this.dataFacture.getDonneesFacture(), this.dataFacture.getParametresFacture(), new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                actualiserTotaux();
            }
        });
        this.editeurArticle = new EditeurFraisFacture(this.dataFacture.getDonneesFacture().getArticles(), modeleListePaiement);
        this.tableListePaiement.setModel(this.modeleListePaiement);
    }

    private void chargerDataTablePaiement() {
        if (this.dataFacture.getDonneesFacture().getPaiements() != null) {
            this.modeleListePaiement.setListePaiements(this.dataFacture.getDonneesFacture().getPaiements());
        }
    }

    private void fixerColonnesTablePaiement(boolean resizeTable) {
        //{"N°", "Date", "Article", "Référence", "Mode", "Période", "Montant reçu", "Reste"};
        //Parametrage du rendu de la table
        this.tableListePaiement.setDefaultRenderer(Object.class, new RenduTablePaiement(gestionEdition, couleurBasique, this.dataFacture.getDonneesFacture(), this.dataFacture.getParametresFacture(), this.modeleListePaiement, icones.getModifier_01()));
        this.tableListePaiement.setRowHeight(25);

        setTaille(this.tableListePaiement.getColumnModel().getColumn(0), 30, true, null);
        setTaille(this.tableListePaiement.getColumnModel().getColumn(1), 150, true, new EditeurDateFacture(this.modeleListePaiement));
        setTaille(this.tableListePaiement.getColumnModel().getColumn(2), 200, false, editeurArticle);
        setTaille(this.tableListePaiement.getColumnModel().getColumn(3), 200, true, null);
        setTaille(this.tableListePaiement.getColumnModel().getColumn(4), 150, true, new EditeurModeFacture());
        setTaille(this.tableListePaiement.getColumnModel().getColumn(5), 150, true, new EditeurPeriodeFacture(dataFacture.getParametresFacture().getListePeriodes()));
        setTaille(this.tableListePaiement.getColumnModel().getColumn(6), 120, true, null);
        setTaille(this.tableListePaiement.getColumnModel().getColumn(7), 120, true, null);

        //On écoute les sélction
        this.tableListePaiement.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    ecouterPaiementSelectionne();
                    actualiserTotaux();
                }
            }
        });

        if (resizeTable == true) {
            this.tableListePaiement.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
    }

    private void parametrerTablePaiement() {
        initModelTablePaiement();
        chargerDataTablePaiement();
        fixerColonnesTablePaiement(true);
    }

    private void initModelTableEcheance() {
        this.modeleListeEcheance = new ModeleListeEcheance(scrollListeEcheances, modeleListePaiement, modeleListeArticles, dataFacture.getParametresFacture(), new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                actualiserTotaux();
            }
        });
        //Parametrage du modele contenant les données de la table
        this.tableListeEcheance.setModel(this.modeleListeEcheance);
    }

    private void chargerDataTableEcheance() {
        //Rien
    }

    private void fixerColonnesTableEcheance(boolean resizeTable) {
        //Parametrage du rendu de la table
        this.tableListeEcheance.setDefaultRenderer(Object.class, new RenduTableEcheance(couleurBasique, this.dataFacture.getParametresFacture(), modeleListeEcheance, icones.getSablier_01()));
        this.tableListeEcheance.setRowHeight(25);

        //{"N°", "Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
        setTaille(this.tableListeEcheance.getColumnModel().getColumn(0), 30, true, null);
        setTaille(this.tableListeEcheance.getColumnModel().getColumn(1), 150, false, null);
        setTaille(this.tableListeEcheance.getColumnModel().getColumn(2), 130, true, null);
        setTaille(this.tableListeEcheance.getColumnModel().getColumn(3), 130, true, null);
        setTaille(this.tableListeEcheance.getColumnModel().getColumn(4), 150, true, null);
        setTaille(this.tableListeEcheance.getColumnModel().getColumn(5), 120, true, null);
        setTaille(this.tableListeEcheance.getColumnModel().getColumn(6), 150, true, null);

        //On écoute les sélction
        this.tableListeEcheance.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    ecouterEcheanceSelectionne();
                    actualiserTotaux();
                }
            }
        });

        if (resizeTable == true) {
            this.tableListeEcheance.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
    }

    private void parametrerTableEcheance() {
        initModelTableEcheance();
        chargerDataTableEcheance();
        fixerColonnesTableEcheance(true);
    }

    private void genererRecu() {
        if (paiementsSelected != null) {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous les exporter le reçu dans un fichier PDF?", "Avertissement", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                try {
                    SortiesFacture sortie = getSortieFacture(btRecu, rubRecu);
                    DocumentPDFFacture recuPDF = new DocumentPDFFacture(this, DocumentPDFFacture.ACTION_OUVRIR, true, sortie);
                    paiementsSelected.removeAllElements();
                    modeleListePaiement.redessinerTable();
                    activerRecu(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void exporterPDF() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous les exporter dans un fichier PDF?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                SortiesFacture sortie = getSortieFacture(btPDF, rubPDF);
                DocumentPDFFacture docpdf = new DocumentPDFFacture(this, DocumentPDFFacture.ACTION_OUVRIR, false, sortie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean mustBeSaved() {
        boolean rep = false;
        //On vérifie dans la liste d'articles
        for (Frais Iarticle : this.modeleListeArticles.getListeData()) {
            if (Iarticle.getBeta() == InterfaceFrais.BETA_MODIFIE || Iarticle.getBeta() == InterfaceFrais.BETA_NOUVEAU) {
                rep = true;
            }
        }

        //On vérifie aussi dans la liste des paiements
        for (InterfacePaiement Ipaiement : this.modeleListePaiement.getListeData()) {
            if (Ipaiement.getBeta() == InterfacePaiement.BETA_MODIFIE || Ipaiement.getBeta() == InterfacePaiement.BETA_NOUVEAU) {
                rep = true;
            }
        }
        return rep;
    }

    private void fermer() {
        if (mustBeSaved() == true) {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous enregistrer les modifications et/ou ajouts apportés à ces données?", "Avertissement", JOptionPane.YES_NO_CANCEL_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.ecouteurFacture.onEnregistre(getSortieFacture(btEnregistrer, rubEnregistrer));
                this.ecouteurClose.onFermer();
            } else if (dialogResult == JOptionPane.NO_OPTION) {
                this.ecouteurClose.onFermer();
            }
        } else {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir fermer cette fenêtre?", "Avertissement", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.ecouteurClose.onFermer();
            }
        }
    }

    private SortiesFacture getSortieFacture(Bouton boutonDeclencheur, RubriqueSimple rubriqueDeclencheur) {
        SortiesFacture sortiesFacture = new SortiesFacture(
                this.modeleListePaiement.getListeData(),
                new EcouteurEnregistrement() {
            @Override
            public void onDone(String message) {
                ecouteurClose.onActualiser(message, icones.getAimer_01());
                if (boutonDeclencheur != null) {
                    boutonDeclencheur.appliquerDroitAccessDynamique(true);
                }
                if (rubriqueDeclencheur != null) {
                    rubriqueDeclencheur.appliquerDroitAccessDynamique(true);
                }

                //On redessine les tableau afin que les couleurs se réinitialisent / Tout redevient noire
                if (modeleListeArticles != null) {
                    modeleListeArticles.redessinerTable();
                }
                if (modeleListePaiement != null) {
                    modeleListePaiement.redessinerTable();
                }
                if (modeleListeEcheance != null) {
                    modeleListeEcheance.redessinerTable();
                }
            }

            @Override
            public void onError(String message) {
                ecouteurClose.onActualiser(message, icones.getAlert_01());
                if (boutonDeclencheur != null) {
                    boutonDeclencheur.appliquerDroitAccessDynamique(true);
                }
                if (rubriqueDeclencheur != null) {
                    rubriqueDeclencheur.appliquerDroitAccessDynamique(true);
                }
            }

            @Override
            public void onUploading(String message) {
                ecouteurClose.onActualiser(message, icones.getSablier_01());
                if (boutonDeclencheur != null) {
                    boutonDeclencheur.appliquerDroitAccessDynamique(false);
                }
                if (rubriqueDeclencheur != null) {
                    rubriqueDeclencheur.appliquerDroitAccessDynamique(false);
                }
            }
        });
        return sortiesFacture;
    }

    private void imprimer() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir imprimer ce document?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                SortiesFacture sortie = getSortieFacture(btImprimer, rubImprimer);
                DocumentPDFFacture documentPDF = new DocumentPDFFacture(this, DocumentPDFFacture.ACTION_IMPRIMER, false, sortie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void enregistrer() {
        if (this.ecouteurFacture != null) {
            rubEnregistrer.setCouleur(Color.BLACK);
            btEnregistrer.setForeground(Color.BLACK);
            SortiesFacture sortiesFacture = getSortieFacture(btEnregistrer, rubEnregistrer);
            this.ecouteurFacture.onEnregistre(sortiesFacture);

            actualiserEditeur();
        }
    }

    private void actualiser() {
        //System.out.println("Actualisation...");
        new Thread() {
            public void run() {
                if (progress != null) {
                    progress.setVisible(true);
                    progress.setIndeterminate(true);
                }

                EcouteurValeursChangees eco = new EcouteurValeursChangees() {
                    @Override
                    public void onValeurChangee() {
                        //initModelTableEleves();
                        parametrerTableArticles();
                        parametrerTablePaiement();
                        parametrerTableEcheance();

                        actualiserTotaux();
                        //System.out.println("Actualisation...Fin");
                    }
                };

                if (ecouteurActualisationFacture != null) {
                    dataFacture = ecouteurActualisationFacture.onRechargeDonneesEtParametres();
                    eco.onValeurChangee();
                }

            }
        }.start();
    }

    private void actualiserTotaux() {
        String monn = dataFacture.getParametresFacture().getMonnaieOutPut().getCode();
        double Tpaye = 0;
        if (modeleListePaiement != null) {
            Tpaye = modeleListePaiement.getTotalMontant();
        }
        double Tttc = 0;
        if (modeleListeArticles != null) {
            Tttc = modeleListeArticles.getTotal_TTC();
        }
        double Tsolde = UtilFacture.round(Tttc - Tpaye, 2);

        labTotalTTC.setText(UtilFacture.getMontantFrancais(Tttc) + " " + monn);
        labTotalPaye.setText(UtilFacture.getMontantFrancais(Tpaye) + " " + monn);
        labTotalSolde.setText(UtilFacture.getMontantFrancais(Tsolde) + " " + monn);
    }

    private void supprimer() {
        switch (indexTabSelected) {
            case 0:
                modeleListeArticles.SupprimerFrais(tableListeArticle.getSelectedRow(), new EcouteurSuppressionElement() {
                    @Override
                    public void onSuppressionConfirmee(int idElement) {
                        //System.out.println("Ce n'est pas ici qu'un frais peut être supprimé");
                    }
                });
                break;
            case 1:
                modeleListePaiement.SupprimerPaiement(tableListePaiement.getSelectedRow(), true, new EcouteurSuppressionElement() {
                    @Override
                    public void onSuppressionConfirmee(int idElement) {
                        ecouteurFacture.onDetruitPaiement(idElement);
                    }
                });
                break;
            default:
                //modeleListeEcheance.SupprimerEcheance(tableListeEcheance.getSelectedRow(), true);
                break;
        }
    }

    private void vider() {
        switch (indexTabSelected) {
            case 0:
                modeleListeArticles.viderListe();
                break;
            case 1:
                //System.out.println("*** SUPPRESSION DES PAIEMENTS");
                modeleListePaiement.viderListe();
                if (ecouteurFacture != null) {
                    ecouteurFacture.onDetruitTousLesPaiements(dataFacture.getDonneesFacture().getEleve().getId(), dataFacture.getParametresFacture().getExercice().getId());
                }
                break;
            default:
                //modeleListeEcheance.viderListe();

                break;
        }
    }

    private void ajouter() {
        switch (indexTabSelected) {
            case 0:
                //this.ecouteurAjout.setAjoutArticle(modeleListeArticles);
                break;
            case 1:
                if (modeleListeArticles.getRowCount() != 0) {
                    //On reinitialise la liste d'article dans le combo
                    if (this.editeurArticle != null) {
                        this.editeurArticle.initCombo();
                        //System.out.println(".initCombo()...");
                        if (this.editeurArticle.getTailleCombo() != 0) {
                            this.ecouteurAjout.setAjoutPaiement(modeleListePaiement);
                        } else {
                            JOptionPane.showMessageDialog(tabPrincipal, "Désolé " + this.dataFacture.getParametresFacture().getUtilisateur().getNom() + ", il n'y a plus d'autre frais à payer !");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(tabPrincipal, "Aucun article n'a été séléctionné !");
                }
                break;
        }
    }

    private void setBoutons() {
        //BtAjouter
        btAjouter = new Bouton(12, "Ajouter", "Ajouter un élement", false, icones.getAjouter_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                ajouter();
            }
        });
        //BtSupprimer
        btSupprimer = new Bouton(12, "Supprimer", "Supprimer l'élement sélectionné", false, icones.getSupprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                supprimer();
            }
        });

        //Vider
        btVider = new Bouton(12, "Vider", "Vider cette liste", false, icones.getAnnuler_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                vider();
            }
        });

        //Actualiser
        btActualiser = new Bouton(12, "Actualiser", "Actualiser la liste active", false, icones.getSynchroniser_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                actualiser();
            }
        });

        //Imprimer
        btImprimer = new Bouton(12, "Imprimer", "Imprimer le contenu de la liste active", false, icones.getImprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                imprimer();
            }
        });

        //Export. PDF
        btPDF = new Bouton(12, "Exp. PDF", "Généer le rapport PDF du contenu actif", false, icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                exporterPDF();
            }
        });

        //Fermer
        btFermer = new Bouton(12, "Fermer", "Fermer cet onglet", false, icones.getFermer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                fermer();
            }
        });

        //Fermer
        btEnregistrer = new Bouton(12, "Enregistrer", "Enregistrer les modifications apportées aux données.", false, icones.getEnregistrer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                enregistrer();
            }
        });
        btEnregistrer.setGras(true);

        //Recu
        btRecu = new Bouton(12, "Prod. Reçu", "Produire le reçu imprimable", false, icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                genererRecu();
            }
        });

        btEdition = new Bouton(12, "Edition", "", true, icones.getModifier_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                setEditionMode();
            }
        });

        //Il faut respecter les droits d'accès attribué à l'utilisateur actuel!
        barreOutilsA = new BarreOutils(barreOutilsArticles);
        if (dataFacture.getParametresFacture().getUtilisateur() != null) {
            Utilisateur user = dataFacture.getParametresFacture().getUtilisateur();
            
            if (user.getDroitFacture() == InterfaceUtilisateur.DROIT_CONTROLER) {
                barreOutilsA.AjouterBouton(btEnregistrer);
                barreOutilsA.AjouterBouton(btAjouter);
                barreOutilsA.AjouterBouton(btEdition);
                barreOutilsA.AjouterSeparateur();
                barreOutilsA.AjouterBouton(btSupprimer);
                barreOutilsA.AjouterBouton(btVider);
            }
            barreOutilsA.AjouterBouton(btActualiser);
            barreOutilsA.AjouterSeparateur();
            barreOutilsA.AjouterBouton(btImprimer);
            barreOutilsA.AjouterBouton(btPDF);
            barreOutilsA.AjouterBouton(btRecu);
            barreOutilsA.AjouterSeparateur();
            barreOutilsA.AjouterBouton(btFermer);
        }
    }

    private void setMenuContextuel() {
        rubAjouter = new RubriqueSimple("Ajouter", 12, false, icones.getAjouter_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                ajouter();
            }
        });
        rubSupprimer = new RubriqueSimple("Supprimer", 12, false, icones.getSupprimer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                supprimer();
            }
        });

        rubVider = new RubriqueSimple("Vider", 12, false, icones.getAnnuler_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                vider();
            }
        });

        rubActualiser = new RubriqueSimple("Actualiser", 12, false, icones.getSynchroniser_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                actualiser();
            }
        });

        rubImprimer = new RubriqueSimple("Imprimer", 12, false, icones.getImprimer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                imprimer();
            }
        });

        rubFermer = new RubriqueSimple("Fermer", 12, false, icones.getFermer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                fermer();
            }
        });

        rubPDF = new RubriqueSimple("Exp. PDF", 12, false, icones.getPDF_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                exporterPDF();
            }
        });

        rubEnregistrer = new RubriqueSimple("Enregistrer", 12, true, icones.getEnregistrer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                enregistrer();
            }
        });

        rubRecu = new RubriqueSimple("Prod. Reçu", 12, false, icones.getPDF_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                genererRecu();
            }
        });

        rubEdition = new RubriqueSimple("Editer", 12, false, icones.getModifier_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                setEditionMode();
            }
        });

        //Il faut respecter les droits d'accès attribué à l'utilisateur actuel!
        menuContextuel = new MenuContextuel();
        if (dataFacture.getParametresFacture().getUtilisateur() != null) {
            Utilisateur user = dataFacture.getParametresFacture().getUtilisateur();

            if (user.getDroitFacture() == InterfaceUtilisateur.DROIT_CONTROLER) {
                menuContextuel.Ajouter(rubEnregistrer);
                menuContextuel.Ajouter(rubAjouter);
                menuContextuel.Ajouter(rubEdition);
                menuContextuel.Ajouter(new JPopupMenu.Separator());
                menuContextuel.Ajouter(rubSupprimer);
                menuContextuel.Ajouter(rubVider);
            }
            menuContextuel.Ajouter(rubActualiser);
            menuContextuel.Ajouter(new JPopupMenu.Separator());
            menuContextuel.Ajouter(rubImprimer);
            menuContextuel.Ajouter(rubPDF);
            menuContextuel.Ajouter(rubRecu);
            menuContextuel.Ajouter(new JPopupMenu.Separator());
            menuContextuel.Ajouter(rubFermer);
        }

    }

    public void activerBoutons(int selectedTab) {
        this.indexTabSelected = selectedTab;
        if (selectedTab == 2 || selectedTab == 0) {
            activeAjoutEtSuppresion(false);
        } else {
            activeAjoutEtSuppresion(true);
        }
        activerRecu(false);
    }

    private void activerRecu(boolean rep) {
        if (btRecu != null && rubRecu != null) {
            btRecu.appliquerDroitAccessDynamique(rep);
            rubRecu.appliquerDroitAccessDynamique(rep);
        }
    }

    private void chargerPaiementsSeletced() {
        paiementsSelected.removeAllElements();
        int[] indexPaiements = tableListePaiement.getSelectedRows();
        if (indexPaiements != null) {
            for (int i = 0; i < indexPaiements.length; i++) {
                paiementsSelected.addElement(modeleListePaiement.getPaiement(indexPaiements[i]));
            }
            activerRecu(true);
        } else {
            paiementsSelected.removeAllElements();
            activerRecu(false);
        }
    }

    private void activeAjoutEtSuppresion(boolean rep) {
        if (btAjouter != null) {
            btAjouter.appliquerDroitAccessDynamique(rep);
        }
        if (btSupprimer != null) {
            btSupprimer.appliquerDroitAccessDynamique(rep);
            btVider.appliquerDroitAccessDynamique(rep);
        }
        if (rubAjouter != null) {
            rubAjouter.appliquerDroitAccessDynamique(rep);
        }
        if (rubSupprimer != null) {
            rubSupprimer.appliquerDroitAccessDynamique(rep);
            rubVider.appliquerDroitAccessDynamique(rep);
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

        jPanel3 = new javax.swing.JPanel();
        labTypeClient = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labNomClient = new javax.swing.JLabel();
        labTelephone = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        labAdresseClient = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        labTotalTTC = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        labTotalPaye = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        labTotalSolde = new javax.swing.JLabel();
        isReleverCompte = new javax.swing.JCheckBox();
        barreOutilsArticles = new javax.swing.JToolBar();
        jButton5 = new javax.swing.JButton();
        tabPrincipal = new javax.swing.JTabbedPane();
        scrollListeArticles = new javax.swing.JScrollPane();
        tableListeArticle = new javax.swing.JTable();
        scrollListePaiement = new javax.swing.JScrollPane();
        tableListePaiement = new javax.swing.JTable();
        scrollListeEcheances = new javax.swing.JScrollPane();
        tableListeEcheance = new javax.swing.JTable();
        isPlanPaiement = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        labTypeClient.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labTypeClient.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labTypeClient.setText("Elève :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Téléphone :");

        labNomClient.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labNomClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labNomClient.setText("SULA BOSIO Serge");

        labTelephone.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labTelephone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labTelephone.setText("(+243) 84 480 35 14, (+243) 82 87 27 706");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Adresse :");

        labAdresseClient.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labAdresseClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labAdresseClient.setText("167b, Av. ITAGA, C. LINGWALA, KINSHASA, RDC");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Montant dû :");

        labTotalTTC.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labTotalTTC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labTotalTTC.setText("00000000 USD");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel11.setText("Payé :");

        labTotalPaye.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labTotalPaye.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labTotalPaye.setText("00000000 USD");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel12.setText("Solde restant dû :");

        labTotalSolde.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labTotalSolde.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labTotalSolde.setText("00000000 USD");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(labTypeClient, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labNomClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labTelephone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labAdresseClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labTotalTTC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labTotalPaye, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labTotalSolde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labTypeClient)
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
                    .addComponent(jLabel10)
                    .addComponent(labTotalTTC))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(labTotalPaye))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(labTotalSolde)))
        );

        isReleverCompte.setBackground(new java.awt.Color(255, 255, 255));
        isReleverCompte.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        isReleverCompte.setSelected(true);
        isReleverCompte.setText("Imprimer également le relevé de compte");

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

        tabPrincipal.addTab("Frais", scrollListeArticles);

        scrollListePaiement.setBackground(new java.awt.Color(255, 255, 255));
        scrollListePaiement.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                scrollListePaiementMouseDragged(evt);
            }
        });
        scrollListePaiement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollListePaiementMouseClicked(evt);
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
        tableListePaiement.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tableListePaiementMouseDragged(evt);
            }
        });
        tableListePaiement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListePaiementMouseClicked(evt);
            }
        });
        tableListePaiement.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableListePaiementKeyReleased(evt);
            }
        });
        scrollListePaiement.setViewportView(tableListePaiement);
        if (tableListePaiement.getColumnModel().getColumnCount() > 0) {
            tableListePaiement.getColumnModel().getColumn(0).setPreferredWidth(50);
            tableListePaiement.getColumnModel().getColumn(0).setHeaderValue("Facture");
            tableListePaiement.getColumnModel().getColumn(1).setResizable(false);
            tableListePaiement.getColumnModel().getColumn(2).setResizable(false);
            tableListePaiement.getColumnModel().getColumn(2).setPreferredWidth(50);
        }

        tabPrincipal.addTab("Paiements", scrollListePaiement);

        scrollListeEcheances.setBackground(new java.awt.Color(255, 255, 255));
        scrollListeEcheances.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollListeEcheancesMouseClicked(evt);
            }
        });

        tableListeEcheance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "D. initiale", "Echéance", "Jours restant", "Montant du", "Montant payé", "Progression"
            }
        ));
        tableListeEcheance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListeEcheanceMouseClicked(evt);
            }
        });
        scrollListeEcheances.setViewportView(tableListeEcheance);
        if (tableListeEcheance.getColumnModel().getColumnCount() > 0) {
            tableListeEcheance.getColumnModel().getColumn(0).setResizable(false);
            tableListeEcheance.getColumnModel().getColumn(1).setResizable(false);
            tableListeEcheance.getColumnModel().getColumn(1).setPreferredWidth(50);
            tableListeEcheance.getColumnModel().getColumn(2).setResizable(false);
            tableListeEcheance.getColumnModel().getColumn(3).setResizable(false);
            tableListeEcheance.getColumnModel().getColumn(5).setResizable(false);
        }

        tabPrincipal.addTab("Plan de paiement", scrollListeEcheances);

        isPlanPaiement.setBackground(new java.awt.Color(255, 255, 255));
        isPlanPaiement.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        isPlanPaiement.setSelected(true);
        isPlanPaiement.setText("Imprimer également le Plan de paiement");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
            .addComponent(barreOutilsArticles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(isReleverCompte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(isPlanPaiement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(barreOutilsArticles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(isReleverCompte)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(isPlanPaiement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void scrollListeArticlesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListeArticlesMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 0);
    }//GEN-LAST:event_scrollListeArticlesMouseClicked

    private void tableListeArticleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeArticleMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 0);
    }//GEN-LAST:event_tableListeArticleMouseClicked

    private void tabPrincipalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPrincipalStateChanged
        // TODO add your handling code here:
        activerBoutons(((JTabbedPane) evt.getSource()).getSelectedIndex());

    }//GEN-LAST:event_tabPrincipalStateChanged

    private void tableListePaiementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListePaiementMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 1);

    }//GEN-LAST:event_tableListePaiementMouseClicked

    private void scrollListePaiementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListePaiementMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 1);
    }//GEN-LAST:event_scrollListePaiementMouseClicked

    private void tableListeEcheanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeEcheanceMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 2);
    }//GEN-LAST:event_tableListeEcheanceMouseClicked

    private void scrollListeEcheancesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListeEcheancesMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 2);
    }//GEN-LAST:event_scrollListeEcheancesMouseClicked

    private void tableListePaiementKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableListePaiementKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListePaiementKeyReleased

    private void scrollListePaiementMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListePaiementMouseDragged
        // TODO add your handling code here:

    }//GEN-LAST:event_scrollListePaiementMouseDragged

    private void tableListePaiementMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListePaiementMouseDragged
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListePaiementMouseDragged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barreOutilsArticles;
    private javax.swing.JCheckBox isPlanPaiement;
    private javax.swing.JCheckBox isReleverCompte;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel labAdresseClient;
    private javax.swing.JLabel labNomClient;
    private javax.swing.JLabel labTelephone;
    private javax.swing.JLabel labTotalPaye;
    private javax.swing.JLabel labTotalSolde;
    private javax.swing.JLabel labTotalTTC;
    private javax.swing.JLabel labTypeClient;
    private javax.swing.JScrollPane scrollListeArticles;
    private javax.swing.JScrollPane scrollListeEcheances;
    private javax.swing.JScrollPane scrollListePaiement;
    private javax.swing.JTabbedPane tabPrincipal;
    private javax.swing.JTable tableListeArticle;
    private javax.swing.JTable tableListeEcheance;
    private javax.swing.JTable tableListePaiement;
    // End of variables declaration//GEN-END:variables
}
