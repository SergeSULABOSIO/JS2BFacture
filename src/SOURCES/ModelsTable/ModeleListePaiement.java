/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable;

import BEAN_BARRE_OUTILS.Bouton;
import BEAN_MenuContextuel.RubriqueSimple;
import SOURCES.Utilitaires_Facture.UtilFacture;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import SOURCES.Utilitaires_Facture.DonneesFacture;
import SOURCES.Utilitaires_Facture.ParametresFacture;
import Source.Callbacks.EcouteurSuppressionElement;
import Source.Callbacks.EcouteurValeursChangees;
import Source.Interface.InterfacePaiement;
import Source.Objet.CouleurBasique;
import Source.Objet.Frais;
import Source.Objet.Paiement;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListePaiement extends AbstractTableModel {

    private String[] titreColonnes = {"N°", "Date", "Frais", "Référence", "Mode", "Période", "Montant reçu", "Reste / Période"};
    private Vector<Paiement> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private DonneesFacture donneesFacture;
    private ParametresFacture parametresFacture;
    private Bouton btEnreg;
    private RubriqueSimple mEnreg;
    private CouleurBasique colBasique;

    public ModeleListePaiement(CouleurBasique colBasique, JScrollPane parent, Bouton btEnreg, RubriqueSimple mEnreg, DonneesFacture donneesFacture, ParametresFacture parametresFacture, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.colBasique = colBasique;
        this.ecouteurModele = ecouteurModele;
        this.donneesFacture = donneesFacture;
        this.parametresFacture = parametresFacture;
        this.mEnreg = mEnreg;
        this.btEnreg = btEnreg;
    }

    public void setListePaiements(Vector<Paiement> listeData) {
        this.listeData = listeData;
        redessinerTable();
    }

    public Paiement getPaiement(int row) {
        if (row < listeData.size() && row != -1) {
            Paiement art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Vector<Paiement> getListeData() {
        return listeData;
    }

    public void AjouterPaiement(Paiement art) {
        this.listeData.add(art);
        //mEnreg.setCouleur(Color.blue);
        //btEnreg.setForeground(Color.blue);
        //System.out.println("Ajout!!!!"+art.toString());
        mEnreg.setCouleur(colBasique.getCouleur_foreground_objet_nouveau());                                        //mEnreg.setCouleur(Color.blue);
        btEnreg.setForeground(colBasique.getCouleur_foreground_objet_nouveau());                                   //btEnreg.setForeground(Color.blue);
        redessinerTable();
    }

    private void deleteLigne(int row) {
        if (row <= listeData.size()) {
            this.listeData.removeElementAt(row);
        }
        redessinerTable();
    }

    public void SupprimerPaiement(int row, boolean mustConfirm, EcouteurSuppressionElement ecouteurSuppressionElement) {
        if (row < listeData.size() && row != -1) {
            Paiement articl = listeData.elementAt(row);
            if (articl != null) {
                int idASupp = articl.getId();
                if (mustConfirm == true) {
                    if (articl != null) {
                        int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir supprimer cette ligne?", "Avertissement", JOptionPane.YES_NO_OPTION);
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            deleteLigne(row);
                            ecouteurSuppressionElement.onSuppressionConfirmee(idASupp);
                        }
                    }
                } else {
                    deleteLigne(row);
                    ecouteurSuppressionElement.onSuppressionConfirmee(idASupp);
                }
            }

        }
    }

    private Frais getFrais(int idArticle) {
        for (Frais Ia : donneesFacture.getArticles()) {
            if (idArticle == Ia.getId()) {
                return Ia;
            }
        }
        return null;
    }

    public double getTotalMontant() {
        double mnt = 0;
        for (Paiement paiem : listeData) {
            Frais Ia = getFrais(paiem.getIdFrais());
            if (Ia != null) {
                mnt = mnt + UtilFacture.getMontantOutPut(parametresFacture, Ia.getIdMonnaie(), paiem.getMontant());
            }
        }
        return UtilFacture.round(mnt, 2);
    }

    public double getTotalReste(ModeleListeFrais modelArticle) {
        double Tsolde = UtilFacture.round(modelArticle.getTotal_TTC() - getTotalMontant(), 2);
        return Tsolde;
    }

    public void viderListe() {
        int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir vider cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            this.listeData.removeAllElements();
            redessinerTable();
        }
    }

    public Paiement getPaiement_idFrais(int id) {
        for (Paiement paiem : listeData) {
            if (paiem.getIdFrais() == id) {
                return paiem;
            }
        }
        return null;
    }

    private double getMontantTotalPayable(int idArticle, int idPeriode) {
        double tot = 0;
        if (donneesFacture != null) {
            for (Frais articleApayer : donneesFacture.getArticles()) {
                if (idArticle == articleApayer.getId()) {
                    //On doit tenir compte du % défini dans le lien entre Frais et Période
                    tot = tot + ((articleApayer.getMontantDefaut() * UtilFacture.getPourcentagePeriode(parametresFacture, idPeriode, articleApayer)) / 100);
                }
            }
        }
        return tot;
    }

    private double getMontantTotalPaye(int idArticle, int idPeriode) {
        double tot = 0;
        for (Paiement paiement : listeData) {
            if (idPeriode == -1) {
                if (paiement.getIdFrais() == idArticle) {
                    tot += paiement.getMontant();
                }
            } else {
                if (paiement.getIdFrais() == idArticle && paiement.getIdPeriode() == idPeriode) {
                    tot += paiement.getMontant();
                }
            }

        }
        return tot;
    }

    public double getReste(int idArticle, int idPeriode) {
        double reste = getMontantTotalPayable(idArticle, idPeriode) - getMontantTotalPaye(idArticle, idPeriode);
        reste = UtilFacture.round(reste, 2);
        if (reste < 0) {
            return 0;
        } else {
            return reste;
        }
    }

    public void redessinerTable() {
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return listeData.size();
    }

    @Override
    public int getColumnCount() {
        return titreColonnes.length;
    }

    @Override
    public String getColumnName(int column) {
        return titreColonnes[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //{"N°", "Date", "Article", "Référence", "Mode", "Période", "Montant reçu", "Reste"};
        Paiement Ipaiement = listeData.elementAt(rowIndex);
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1) + "";
            case 1:
                return Ipaiement.getDate();
            case 2:
                return Ipaiement.getIdFrais();
            case 3:
                return Ipaiement.getReferenceTransaction();
            case 4:
                return Ipaiement.getMode();
            case 5:
                return Ipaiement.getIdPeriode();
            case 6:
                return Ipaiement.getMontant();
            case 7:
                return getReste(Ipaiement.getIdFrais(), Ipaiement.getIdPeriode());
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"N°", "Date", "Article", "Référence", "Mode", "Période", "Montant reçu", "Reste"};
        switch (columnIndex) {
            case 0:
                return String.class;//N°
            case 1:
                return Date.class;//Date
            case 2:
                return Integer.class;//NomArticle
            case 3:
                return String.class;//Reference
            case 4:
                return Integer.class;//Mode
            case 5:
                return Integer.class;//Période
            case 6:
                return Double.class;//Montant
            case 7:
                return Double.class;//Reste
            default:
                return Object.class;
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //{"N°", "Date", "Article", "Référence", "Mode", "Période", "Montant reçu", "Reste"};
        if (columnIndex == 0 || columnIndex == 7) {
            return false;
        } else {
            return true;
        }
    }

    private void updateArticle(Paiement newPaiement) {
        if (newPaiement != null && donneesFacture != null) {
            for (Frais Iarticle : donneesFacture.getArticles()) {
                if (Iarticle.getId() == newPaiement.getIdFrais()) {
                    //System.out.println("Article: " + Iarticle.getNom());
                    newPaiement.setNomFrais(Iarticle.getNom());
                    newPaiement.setMontant(0);
                    return;
                }
            }
        }
        redessinerTable();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Date", "Article", "Référence", "Mode", "Période", "Montant reçu", "Reste"};
        Paiement Ipaiement = listeData.get(rowIndex);
        if (Ipaiement != null) {
            String avant = Ipaiement.toString();
            switch (columnIndex) {
                case 1:
                    Ipaiement.setDate((Date) aValue);
                    break;
                case 2:
                    int newIdFrais = Integer.parseInt(aValue + "");
                    if (Ipaiement.getIdFrais() != newIdFrais && newIdFrais != -1) {
                        Ipaiement.setIdFrais(newIdFrais);
                        updateArticle(Ipaiement);
                    }
                    break;
                case 3:
                    Ipaiement.setReferenceTransaction(aValue + "");
                    break;
                case 4:
                    Ipaiement.setMode(Integer.parseInt(aValue + ""));
                    break;
                case 5:
                    Ipaiement.setIdPeriode(Integer.parseInt(aValue + ""));
                    break;
                case 6:
                    Ipaiement.setMontant(Double.parseDouble(aValue + ""));
                    break;
                default:
                    break;
            }
            String apres = Ipaiement.toString();
            if (!avant.equals(apres)) {
                if (Ipaiement.getBeta() == InterfacePaiement.BETA_EXISTANT) {
                    Ipaiement.setBeta(InterfacePaiement.BETA_MODIFIE);
                    //mEnreg.setCouleur(Color.blue);
                    //btEnreg.setForeground(Color.blue);
                    mEnreg.setCouleur(colBasique.getCouleur_foreground_objet_nouveau());                                        //mEnreg.setCouleur(Color.blue);
                    btEnreg.setForeground(colBasique.getCouleur_foreground_objet_nouveau());                                   //btEnreg.setForeground(Color.blue);
                }
            }
            listeData.set(rowIndex, Ipaiement);
            ecouteurModele.onValeurChangee();
            fireTableDataChanged();
        }
    }
}
