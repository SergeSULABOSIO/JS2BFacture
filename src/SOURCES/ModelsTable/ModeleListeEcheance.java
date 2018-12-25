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

    private String[] titreColonnes = {"Nom", "Date initiale", "Echéance", "Jrs restant", "Tranches", "Progression"};
    private Vector<EcheanceFacture> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private ModeleListePaiement modeleListePaiement;
    private ModeleListeArticles modeleListeArticles;
    private double totalDue = 0;
    private double totalDue_by_installment = 0;
    private double totalPaid = 0;
    private double x = 0;

    public ModeleListeEcheance(JScrollPane parent, ModeleListePaiement modeleListePaiement, ModeleListeArticles modeleListeArticles, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.modeleListePaiement = modeleListePaiement;
        this.modeleListeArticles = modeleListeArticles;

        calculerTranche();
    }

    public void setListeEcheance(Vector<EcheanceFacture> listeData) {
        this.listeData = listeData;
        redessinerTable();
    }

    public Vector<EcheanceFacture> getListeData() {
        return listeData;
    }

    private void ajouterJours(EcheanceFacture echeance, int nbJours) {
        Date today = new Date();
        Date datef = new Date();
        datef.setDate(today.getDate() + nbJours);

        echeance.setDateInitiale(today);
        echeance.setDateFinale(datef);
    }

    public void AjouterEcheance(EcheanceFacture echeance) {
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

    public double getTotalMontantDu() {
        double tot = 0;
        if (modeleListeArticles != null) {
            for (ArticleFacture articleApayer : modeleListeArticles.getListeData()) {
                tot = tot + articleApayer.getTotalTTC();
            }
        }
        return tot;
    }

    public double getTotalMontantPaye() {
        double tot = 0;
        for (PaiementFacture paiement : modeleListePaiement.getListeData()) {
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

    public EcheanceFacture getEcheance_id(int id) {
        for (EcheanceFacture paiem : listeData) {
            if (paiem.getId() == id) {
                return paiem;
            }
        }
        return null;
    }

    public EcheanceFacture getEcheance_row(int row) {
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
        EcheanceFacture eche = this.listeData.elementAt(rowIndex);
        Date today = new Date();
        int nbjour = eche.getDateFinale().getDate() - today.getDate();
        if (nbjour > 0) {
            return nbjour + " jours";
        } else {
            return nbjour + " jour";
        }
    }
    
    private double getEtatProgression(int rowIndex) {
        calculerTranche();
        return Util.round(totalDue_by_installment * (rowIndex + 1), 2);
    }

    private void calculerTranche() {
        if (this.modeleListeArticles != null) {
            this.totalDue = modeleListeArticles.getTotal_TTC();
            if (!listeData.isEmpty()) {
                totalDue_by_installment = totalDue / listeData.size();
            } else {
                totalDue_by_installment = totalDue / 1;
            }
        }
    }

    private double getMontantAPaye(int rowIndex) {
        calculerTranche();
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
        EcheanceFacture echeance = listeData.get(rowIndex);
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
