/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable;

import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Utilitaires.XX_Echeance;
import SOURCES.Utilitaires.Util;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.Interface.InterfaceEcheance;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeEcheance extends AbstractTableModel {

    private String[] titreColonnes = {"Nom", "Date initiale", "Echéance", "Jrs restant", "Tranches", "Progression"};
    private Vector<InterfaceEcheance> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private ModeleListePaiement modeleListePaiement;
    private ModeleListeArticles modeleListeArticles;
    private String monnaie;
    private int idMonnaie;
    private String numeroFacture;
    private int idFacture;
    
    private double totalDue = 0;
    private double totalDue_by_installment = 0;
    private double totalPaid = 0;
    private double x = 0;

    public ModeleListeEcheance(JScrollPane parent, ModeleListePaiement modeleListePaiement, ModeleListeArticles modeleListeArticles, EcouteurValeursChangees ecouteurModele, String monnaie, int idMonnaie, String numeroFacture, int idFacture) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.modeleListePaiement = modeleListePaiement;
        this.modeleListeArticles = modeleListeArticles;
        this.monnaie = monnaie;
        this.idMonnaie = idMonnaie;
        this.numeroFacture = numeroFacture;
        this.idFacture = idFacture;
        
        this.calculerTranches();
    }

    public void setListeEcheance(Vector<InterfaceEcheance> listeData) {
        if (listeData != null) {
            if (!listeData.isEmpty()) {
                this.listeData = listeData;
                redessinerTable();
            }
        }

    }

    public Vector<InterfaceEcheance> getListeData() {
        return listeData;
    }

    private void ajouterJours(InterfaceEcheance echeance, int nbJours) {
        Date today = new Date();
        Date datef = new Date();
        datef.setDate(today.getDate() + nbJours);

        echeance.setDateInitiale(today);
        echeance.setDateFinale(datef);
    }

    public void AjouterEcheance(InterfaceEcheance echeance) {
        ajouterJours(echeance, 5);
        this.listeData.add(echeance);
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
            InterfaceEcheance articl = listeData.elementAt(row);
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

    public double getTotalMontantDu() {
        double tot = 0;
        if (modeleListeArticles != null) {
            for (InterfaceArticle articleApayer : modeleListeArticles.getListeData()) {
                tot = tot + articleApayer.getTotalTTC();
            }
        }
        return tot;
    }

    public double getTotalMontantPaye() {
        double tot = 0;
        for (InterfacePaiement paiement : modeleListePaiement.getListeData()) {
            tot = tot + paiement.getMontant();
        }
        return tot;
    }

    public double getTotalReste() {
        double Tsolde = Util.round(getTotalMontantDu() - getTotalMontantPaye(), 2);
        return Tsolde;
    }

    public void viderListe() {
        int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir vider cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            this.listeData.removeAllElements();
            redessinerTable();
        }
    }

    public InterfaceEcheance getEcheance_id(int id) {
        for (InterfaceEcheance paiem : listeData) {
            if (paiem.getId() == id) {
                return paiem;
            }
        }
        return null;
    }

    public InterfaceEcheance getEcheance_row(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceEcheance art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
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

    public String getNBJoursRestant(int rowIndex) {
        InterfaceEcheance eche = this.listeData.elementAt(rowIndex);
        Date today = new Date();
        int nbjour = eche.getDateFinale().getDate() - today.getDate();
        if (nbjour > 0) {
            return nbjour + " jours";
        } else {
            return nbjour + " jour";
        }
    }

    private double getEtatProgression(int rowIndex) {

        return Util.round(totalDue_by_installment * (rowIndex + 1), 2);
    }

    private void calculerTranches() {
        //Comptage du nombre maximale des tranches à considérer
        int nbTotalTranche = 0;
        for (InterfaceArticle art : modeleListeArticles.getListeData()) {
            if (art.getTranches() > nbTotalTranche) {
                nbTotalTranche = art.getTranches();
            }
        }
        System.out.println("nbTotalTranche = " + nbTotalTranche);
        
        
        //Création des tranches en fonction du nombre connu déjà
        for (int i = 0; i < nbTotalTranche; i++) {
            XX_Echeance trancheTempo = new XX_Echeance(-1, "Tranche n°" + (i + 1), idFacture, new Date(), new Date(), "", 0, 0, idMonnaie, monnaie);
            System.out.println(" * " + trancheTempo.toString());
            this.AjouterEcheance(trancheTempo);
        }

        for (InterfaceArticle art : modeleListeArticles.getListeData()) {
            //Récupération du nb de tranche et de leur montant y relatifs
            int nbTranches = art.getTranches();
            double montTranche = 0;
            if (nbTranches != 0) {
                montTranche = art.getTotalTTC() / nbTranches;
            } else {
                montTranche = art.getTotalTTC() / 1;
            }
            //
        }

        /*
        if (this.modeleListeArticles != null) {
            this.totalDue = modeleListeArticles.getTotal_TTC();
            if (!listeData.isEmpty()) {
                totalDue_by_installment = totalDue / listeData.size();
            } else {
                totalDue_by_installment = totalDue / 1;
            }
        }
         */
    }

    private double getMontantAPaye(int rowIndex) {

        return totalDue_by_installment;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //private String[] titreColonnes = {"Nom", "Date initiale", "Echéance", "Jrs restant", "Montant dû", "Progression"};
        switch (columnIndex) {
            case 0:
                return listeData.elementAt(rowIndex).getNom();
            case 1:
                return listeData.elementAt(rowIndex).getDateInitiale();
            case 2:
                return listeData.elementAt(rowIndex).getDateFinale();
            case 3:
                return getNBJoursRestant(rowIndex);
            case 4:
                return getMontantAPaye(rowIndex);
            case 5:
                return getEtatProgression(rowIndex);
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //private String[] titreColonnes = {"Nom", "Date initiale", "Echéance", "Jrs restant", "Montant dû", "Progression"};
        switch (columnIndex) {
            case 0:
                return String.class;//Nom
            case 1:
                return Date.class;//Date initiale
            case 2:
                return Date.class;//Date finale
            case 3:
                return String.class;//Jrs restant
            case 4:
                return Double.class;//montant paye
            case 5:
                return Double.class;//Progression
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //private String[] titreColonnes = {"Date initiale", "Echéance", "Jrs restant", "Montant dû", "Progression"};
        if (columnIndex == 0 || columnIndex == 1 || columnIndex == 2 || columnIndex == 4) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //private String[] titreColonnes = {"Date initiale", "Echéance", "Jrs restant", "Montant dû", "Progression"};
        InterfaceEcheance echeance = listeData.get(rowIndex);
        switch (columnIndex) {
            case 0:
                echeance.setNom(aValue + "");
                break;
            case 1:
                echeance.setDateInitiale((Date) aValue);
                break;
            case 2:
                echeance.setDateFinale((Date) aValue);
                break;
            case 4:
                echeance.setMontantDu(Double.parseDouble(aValue + ""));
                break;
            default:
                break;
        }
        listeData.set(rowIndex, echeance);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

}
