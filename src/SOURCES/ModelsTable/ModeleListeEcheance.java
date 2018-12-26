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
import SOURCES.Utilitaires.ExerciceFiscale;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeEcheance extends AbstractTableModel {

    private String[] titreColonnes = {"Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
    private Vector<InterfaceEcheance> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private ModeleListePaiement modeleListePaiement;
    private ModeleListeArticles modeleListeArticles;
    private String monnaie;
    private int idMonnaie;
    private String numeroFacture;
    private int idFacture;

    private ExerciceFiscale exerciceFiscale;
    public int nombreTranches = 1;

    public ModeleListeEcheance(JScrollPane parent, ModeleListePaiement modeleListePaiement, ModeleListeArticles modeleListeArticles, EcouteurValeursChangees ecouteurModele, String monnaie, int idMonnaie, String numeroFacture, int idFacture, ExerciceFiscale exerciceFiscale) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.modeleListePaiement = modeleListePaiement;
        this.modeleListeArticles = modeleListeArticles;
        this.monnaie = monnaie;
        this.idMonnaie = idMonnaie;
        this.numeroFacture = numeroFacture;
        this.idFacture = idFacture;
        this.exerciceFiscale = exerciceFiscale;

        this.setDefaultTranches();
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

    public void AjouterEcheance(InterfaceEcheance echeance) {
        //ajouterJours(echeance, 5);
        this.listeData.add(echeance);
        
        redessinerTable();
    }

    public void AjouterEcheanceAutomatique(InterfaceEcheance echeance) {
        this.listeData.add(echeance);
        
        redessinerTable();
    }

    public void AjouterEcheancesAutomatique(Vector<InterfaceEcheance> echeances) {
        this.listeData.addAll(echeances);
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

    public void redessinerTable() {
        setDefaultTranches();
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
        int nbjour = (int) Util.getNombre_jours_from_today(eche.getDateFinale());
        String jourS = " jour";
        if(nbjour > 0 || nbjour < -1){
            jourS = " jours";
        }else if(nbjour == 0){
            jourS = "";
        }
        String commentaire = "Expire dans "+ nbjour + jourS;
        if(nbjour < 0){
           commentaire = "Expirée depuis " + (nbjour * -1) + jourS;
        }else if(nbjour == 0){
            commentaire = "Expire aujourd'hui";
        }
        return commentaire;
    }

    private int getNbTranches() {
        this.nombreTranches = 0;
        this.modeleListeArticles.getListeData().stream().filter((art) -> (art.getTranches() > this.nombreTranches)).forEachOrdered((art) -> {
            this.nombreTranches = art.getTranches();
        });
        //System.out.println("nbTotalTranche = " + this.nombreTranches);
        return this.nombreTranches;
    }

    private void creerTranches() {
        this.listeData.removeAllElements();
        int nombre = getNbTranches();
        for (int i = 0; i < nombre; i++) {
            String nomTranche = "1ère Tranche";
            if ((i + 1) > 1) {
                nomTranche = (i + 1) + "ème Tranche";
            }
            XX_Echeance trancheTempo = new XX_Echeance(-1, nomTranche, idFacture, this.exerciceFiscale.getDebut(), exerciceFiscale.getFin(), numeroFacture, 0, 0, idMonnaie, monnaie);
            
            this.listeData.add(trancheTempo);
        }
    }

    private void calculerPeriodesTranches() {
        double daysExercice = Util.getNombre_jours(exerciceFiscale.getFin(), exerciceFiscale.getDebut());
        double nbDaysParTranche = daysExercice / nombreTranches;
        long nbDaysParTrancheLong = (long) ((nbDaysParTranche) * 1000 * 60 * 60 * 24);
        long cumulDays = 0;
        //System.out.println("Exercice Fiscale : " + exerciceFiscale.toString());
        
        double montantDuTrancheEncors = this.modeleListeArticles.getTotal_TTC();
        if(nombreTranches != 0){
            montantDuTrancheEncors = this.modeleListeArticles.getTotal_TTC()/nombreTranches;
        }
        if (!this.listeData.isEmpty()) {
            for (int i = 0; i < nombreTranches; i++) {
                InterfaceEcheance echeEncours = (XX_Echeance) listeData.elementAt(i);
                //On calcul la période de validité de la tranche
                long dA = exerciceFiscale.getDebut().getTime() + cumulDays;
                long dB = dA + nbDaysParTrancheLong;
                echeEncours.setDateInitiale(new Date(dA));
                echeEncours.setDateFinale(new Date(dB));
                cumulDays = cumulDays + nbDaysParTrancheLong;
                
                //On calcul les montants dûs pour cette tranche
                echeEncours.setMontantDu(montantDuTrancheEncors);
                
                
                //On calcul les montants déjà payés pour cette tranche
                double montPaye = 0;
                for(InterfacePaiement paiement : modeleListePaiement.getListeData()){
                    if(paiement.getDate().compareTo(echeEncours.getDateInitiale()) > 0 && paiement.getDate().compareTo(echeEncours.getDateFinale()) <= 0){
                        montPaye = montPaye + paiement.getMontant();
                        //System.out.println(" * " + echeEncours.getNom() + ":" + paiement.getNomArticle() + " : " + paiement.getMontant()+" " + this.monnaie+", date : " + paiement.getDate());
                    }
                }
                echeEncours.setMontantPaye(montPaye);
                //System.out.println(" * " + echeEncours.getNom() + " : " + nbDaysParTranche + " Jours (" + Util.getDateFrancais(echeEncours.getDateInitiale()) + " - " + Util.getDateFrancais(echeEncours.getDateFinale()) + ").");
            }
        }
        //System.out.println("Nombre total des jours : " + daysExercice + ", exercice : " + this.exerciceFiscale.getNom());
    }

    private void setDefaultTranches() {
        creerTranches();
        calculerPeriodesTranches();
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //{"Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
        
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
                setDefaultTranches();
                return listeData.elementAt(rowIndex).getMontantDu();
            case 5:
                setDefaultTranches();
                return listeData.elementAt(rowIndex).getMontantPaye();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
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
        //{"Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
        if (columnIndex == 0 || columnIndex == 1 || columnIndex == 2 || columnIndex == 4) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
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
