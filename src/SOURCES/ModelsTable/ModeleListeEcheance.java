/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable;

import SOURCES.Interface.ArticleFacture;
import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Interface.EcheanceFacture;
import SOURCES.Interface.PaiementFacture;
import SOURCES.Utilitaires.Util;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeEcheance extends AbstractTableModel {

    private String[] titreColonnes = {"Facture", "Initiale", "Echéance", "Jrs restant", "Montant du", "Montant payé", "Progression"};
    private Vector<EcheanceFacture> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private ModeleListePaiement modeleListePaiement;
    private ModeleListeArticles modeleListeArticles;

    public ModeleListeEcheance(JScrollPane parent, ModeleListePaiement modeleListePaiement, ModeleListeArticles modeleListeArticles, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.modeleListePaiement = modeleListePaiement;
        this.modeleListeArticles = modeleListeArticles;
    }

    public void setListeEcheance(Vector<EcheanceFacture> listeData) {
        this.listeData = listeData;
        redessinerTable();
    }

    public EcheanceFacture getPaiement(int row) {
        if (row < listeData.size() && row != -1) {
            EcheanceFacture art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Vector<EcheanceFacture> getListeData() {
        return listeData;
    }

    public void AjouterEcheance(EcheanceFacture art) {
        this.listeData.add(art);
        redessinerTable();
    }

    private void deleteLigne(int row) {
        if (row <= listeData.size()) {
            this.listeData.removeElementAt(row);
        }
        redessinerTable();
    }

    public void SupprimerEcheance(int row, boolean mustConfirm) {
        if (row < listeData.size() && row != -1) {
            EcheanceFacture articl = listeData.elementAt(row);
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

    public double getMontantDu() {
        double mnt = 0;
        for (EcheanceFacture art : listeData) {
            mnt = mnt + art.getMontantDu();
        }
        return Util.round(mnt, 2);
    }
    
    public double getMontantPaye() {
        double mnt = 0;
        for (EcheanceFacture art : listeData) {
            mnt = mnt + art.getMontantPaye();
        }
        return Util.round(mnt, 2);
    }

    public double getReste() {
        double Tsolde = Util.round(getMontantDu() - getMontantPaye(), 2);
        return Tsolde;
    }

    public void viderListe() {
        int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir vider cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            this.listeData.removeAllElements();
            redessinerTable();
        }
    }

    public EcheanceFacture getPaiement_id(int id) {
        for (EcheanceFacture paiem : listeData) {
            if (paiem.getId() == id) {
                return paiem;
            }
        }
        return null;
    }

    private double getMontantTotalPayable() {
        double tot = 0;
        if (modeleListeArticles != null) {
            for (ArticleFacture articleApayer : modeleListeArticles.getListeData()) {
                tot = tot + articleApayer.getTotalTTC();
            }
        }
        return tot;
    }

    private double getMontantTotalPaye() {
        double tot = 0;
        for (PaiementFacture paiement : modeleListePaiement.getListeData()) {
            tot = tot + paiement.getMontant();
        }
        return tot;
    }

    private void redessinerTable() {
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
        //private String[] titreColonnes = {"Facture", "Initiale", "Echéance", "Jrs restant", "Montant du", "Montant payé", "Progression"};
        switch (columnIndex) {
            case 0:
                return listeData.elementAt(rowIndex).getNumeroFacture();
            case 1:
                return listeData.elementAt(rowIndex).getDateInitiale();
            case 2:
                return listeData.elementAt(rowIndex).getDateFinale();
            case 3:
                return listeData.elementAt(rowIndex).getNbJoursRestant();
            case 4:
                return listeData.elementAt(rowIndex).getMontantDu();
            case 5:
                return listeData.elementAt(rowIndex).getMontantPaye();
            case 6:
                return listeData.elementAt(rowIndex).getEtatProgression();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //private String[] titreColonnes = {"Facture", "Initiale", "Echéance", "Jrs restant", "Montant du", "Montant payé", "Progression"};
        switch (columnIndex) {
            case 0:
                return String.class;//Numero facture
            case 1:
                return Date.class;//Date initiale
            case 2:
                return Date.class;//Date finale
            case 3:
                return int.class;//Jrs restant
            case 4:
                return Double.class;//montant du
            case 5:
                return Double.class;//montant paye
            case 6:
                return JProgressBar.class;//Progression
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1 || columnIndex == 2 || columnIndex == 4) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //private String[] titreColonnes = {"Facture", "Initiale", "Echéance", "Jrs restant", "Montant du", "Montant payé", "Progression"};
        EcheanceFacture echeance = listeData.get(rowIndex);
        switch (columnIndex) {
            case 0:
                echeance.setDate((Date) aValue);
                break;
            case 1:
                String nom = aValue + "";
                if(nom.contains("_")){
                    nom = nom.split("_")[1];
                }
                echeance.setNomArticle(nom);
                break;
            case 2:
                echeance.setNomDepositaire(aValue + "");
                break;
            case 3:
                echeance.setMontant(Double.parseDouble(aValue + ""));
                break;
            default:
                break;
        }
        listeData.set(rowIndex, echeance);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

}
