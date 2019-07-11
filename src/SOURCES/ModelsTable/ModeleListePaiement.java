/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable;

import BEAN_BARRE_OUTILS.Bouton;
import BEAN_MenuContextuel.RubriqueSimple;
import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Utilitaires.Util;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.Utilitaires.DonneesFacture;
import SOURCES.Utilitaires.ParametresFacture;
import java.awt.Color;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListePaiement extends AbstractTableModel {

    private String[] titreColonnes = {"N°", "Date", "Article", "Référence", "Mode", "Période", "Montant reçu", "Reste / Période"};
    private Vector<InterfacePaiement> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private DonneesFacture donneesFacture;
    private ParametresFacture parametresFacture;
    private Bouton btEnreg;
    private RubriqueSimple mEnreg;

    public ModeleListePaiement(JScrollPane parent, Bouton btEnreg, RubriqueSimple mEnreg, DonneesFacture donneesFacture, ParametresFacture parametresFacture, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.donneesFacture = donneesFacture;
        this.parametresFacture = parametresFacture;
        this.mEnreg = mEnreg;
        this.btEnreg = btEnreg;
    }

    public void setListePaiements(Vector<InterfacePaiement> listeData) {
        this.listeData = listeData;
        redessinerTable();
    }

    public InterfacePaiement getPaiement(int row) {
        if (row < listeData.size() && row != -1) {
            InterfacePaiement art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Vector<InterfacePaiement> getListeData() {
        return listeData;
    }

    public void AjouterPaiement(InterfacePaiement art) {
        this.listeData.add(art);
        mEnreg.setCouleur(Color.blue);
        btEnreg.setForeground(Color.blue);
        //System.out.println("Ajout!!!!"+art.toString());
        redessinerTable();
    }

    private void deleteLigne(int row) {
        if (row <= listeData.size()) {
            this.listeData.removeElementAt(row);
        }
        redessinerTable();
    }

    public void SupprimerPaiement(int row, boolean mustConfirm) {
        if (row < listeData.size() && row != -1) {
            InterfacePaiement articl = listeData.elementAt(row);
            if (mustConfirm == true) {
                if (articl != null) {
                    int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir supprimer cette ligne?", "Avertissement", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        deleteLigne(row);
                    }
                }
            } else {
                deleteLigne(row);
            }
        }
    }

    private InterfaceArticle getArticle(int idArticle) {
        for (InterfaceArticle Ia : donneesFacture.getArticles()) {
            if (idArticle == Ia.getId()) {
                return Ia;
            }
        }
        return null;
    }

    public double getTotalMontant() {
        double mnt = 0;
        for (InterfacePaiement paiem : listeData) {
            InterfaceArticle Ia = getArticle(paiem.getIdArticle());
            if (Ia != null) {
                mnt = mnt + Util.getMontantOutPut(parametresFacture, Ia.getIdMonnaie(), paiem.getMontant());
            }
        }
        return Util.round(mnt, 2);
    }

    public double getTotalReste(ModeleListeArticles modelArticle) {
        double Tsolde = Util.round(modelArticle.getTotal_TTC() - getTotalMontant(), 2);
        return Tsolde;
    }

    public void viderListe() {
        int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir vider cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            this.listeData.removeAllElements();
            redessinerTable();
        }
    }

    public InterfacePaiement getPaiement_idArticle(int id) {
        for (InterfacePaiement paiem : listeData) {
            if (paiem.getIdArticle() == id) {
                return paiem;
            }
        }
        return null;
    }

    private double getMontantTotalPayable(int idArticle, int idPeriode) {
        double tot = 0;
        if (donneesFacture != null) {
            for (InterfaceArticle articleApayer : donneesFacture.getArticles()) {
                if (idArticle == articleApayer.getId()) {
                    //On doit tenir compte du % défini dans le lien entre Frais et Période
                    tot = tot + ((articleApayer.getTotalTTC() * Util.getPourcentagePeriode(parametresFacture, idPeriode, articleApayer)) / 100);
                }
            }
        }
        return tot;
    }

    private double getMontantTotalPaye(int idArticle, int idPeriode) {
        double tot = 0;
        for (InterfacePaiement paiement : listeData) {
            if (idPeriode == -1) {
                if (paiement.getIdArticle() == idArticle) {
                    tot += paiement.getMontant();
                }
            }else{
                if (paiement.getIdArticle() == idArticle && paiement.getIdPeriode() == idPeriode) {
                    tot += paiement.getMontant();
                }
            }

        }
        return tot;
    }

    public double getReste(int idArticle, int idPeriode) {
        double reste = getMontantTotalPayable(idArticle, idPeriode) - getMontantTotalPaye(idArticle, idPeriode);
        reste = Util.round(reste, 2);
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
        InterfacePaiement Ipaiement = listeData.elementAt(rowIndex);
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1) + "";
            case 1:
                return Ipaiement.getDate();
            case 2:
                return Ipaiement.getIdArticle();
            case 3:
                return Ipaiement.getReferenceTransaction();
            case 4:
                return Ipaiement.getMode();
            case 5:
                return Ipaiement.getIdPeriode();
            case 6:
                return Ipaiement.getMontant();
            case 7:
                return getReste(Ipaiement.getIdArticle(), Ipaiement.getIdPeriode());
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

    private void updateArticle(InterfacePaiement newPaiement) {
        if (newPaiement != null && donneesFacture != null) {
            for (InterfaceArticle Iarticle : donneesFacture.getArticles()) {
                if (Iarticle.getId() == newPaiement.getIdArticle()) {
                    //System.out.println("Article: " + Iarticle.getNom());
                    newPaiement.setNomArticle(Iarticle.getNom());
                    newPaiement.setMontant(0);
                    return;
                }
            }
        }
        redessinerTable();
    }

    private void updateMontantDuEtReste(InterfacePaiement Ipaiement) {

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Date", "Article", "Référence", "Mode", "Période", "Montant reçu", "Reste"};
        InterfacePaiement Ipaiement = listeData.get(rowIndex);
        String avant = Ipaiement.toString();
        switch (columnIndex) {
            case 1:
                Ipaiement.setDate((Date) aValue);
                break;
            case 2:
                Ipaiement.setIdArticle(Integer.parseInt(aValue + ""));
                updateArticle(Ipaiement);
                break;
            case 3:
                Ipaiement.setReferenceTransaction(aValue + "");
                break;
            case 4:
                Ipaiement.setMode(Integer.parseInt(aValue + ""));
                break;
            case 5:
                Ipaiement.setIdPeriode(Integer.parseInt(aValue + ""));
                updateMontantDuEtReste(Ipaiement);
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
                mEnreg.setCouleur(Color.blue);
                btEnreg.setForeground(Color.blue);
            }
        }
        listeData.set(rowIndex, Ipaiement);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }
}
