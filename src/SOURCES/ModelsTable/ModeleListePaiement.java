/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable;

import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Utilitaires.Util;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfacePaiement;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListePaiement extends AbstractTableModel {

    private String[] titreColonnes = {"N°", "Date", "Article", "Référence", "Mode", "Montant reçu", "Reste"};
    private Vector<InterfacePaiement> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private ModeleListeArticles modeleListeArticles;

    public ModeleListePaiement(JScrollPane parent, ModeleListeArticles modeleListeArticles, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.modeleListeArticles = modeleListeArticles;
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

    public double getTotalMontant() {
        double mnt = 0;
        for (InterfacePaiement art : listeData) {
            mnt = mnt + art.getMontant();
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

    private double getMontantTotalPayable(int idArticle) {
        double tot = 0;
        if (modeleListeArticles != null) {
            for (InterfaceArticle articleApayer : modeleListeArticles.getListeData()) {
                if (idArticle == articleApayer.getId()) {
                    tot = tot + articleApayer.getTotalTTC();
                }
            }
        }
        return tot;
    }

    private double getMontantTotalPaye(int idArticle) {
        double tot = 0;
        for (InterfacePaiement paiement : listeData) {
            if (paiement.getIdArticle() == idArticle) {
                tot = tot + paiement.getMontant();
            }
        }
        return tot;
    }

    public double getReste(int idArticle) {
        double reste = getMontantTotalPayable(idArticle) - getMontantTotalPaye(idArticle);
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
        //{"N°", "Date", "Article", "Référence", "Mode", "Montant reçu", "Reste"};
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1) + "";
            case 1:
                return listeData.elementAt(rowIndex).getDate();
            case 2:
                return listeData.elementAt(rowIndex).getIdArticle();
            case 3:
                return listeData.elementAt(rowIndex).getReferenceTransaction();
            case 4:
                return listeData.elementAt(rowIndex).getMode();
            case 5:
                return listeData.elementAt(rowIndex).getMontant();
            case 6:
                return getReste(listeData.elementAt(rowIndex).getIdArticle());
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"N°", "Date", "Article", "Référence", "Mode", "Montant reçu", "Reste"};
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
                return Double.class;//Montant
            case 6:
                return Double.class;//Reste
            default:
                return Object.class;
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //{"N°", "Date", "Article", "Référence", "Mode", "Montant reçu", "Reste"};
        if (columnIndex == 0 || columnIndex == 6) {
            return false;
        } else {
            return true;
        }
    }
    
    private void updateArticle(InterfacePaiement newArticle) {
        if (newArticle != null && modeleListeArticles != null) {
            for (InterfaceArticle Iarticle : modeleListeArticles.getListeData()) {
                if (Iarticle.getId() == newArticle.getId()) {
                    newArticle.setNomArticle(Iarticle.getNom());
                    newArticle.setMontant(0);
                    return;
                }
            }
        }
        redessinerTable();
    }

    
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Date", "Article", "Référence", "Mode", "Montant reçu", "Reste"};
        InterfacePaiement Ipaiement = listeData.get(rowIndex);
        String avant = Ipaiement.toString();
        switch (columnIndex) {
            case 1:
                Ipaiement.setDate((Date) aValue);
                break;
            case 2:
                Ipaiement.setIdArticle(Integer.parseInt(aValue+""));
                updateArticle(Ipaiement);
                break;
            case 3:
                Ipaiement.setReferenceTransaction(aValue + "");
                break;
            case 4:
                Ipaiement.setMode(Integer.parseInt(aValue + ""));
                break;
            case 5:
                Ipaiement.setMontant(Double.parseDouble(aValue + ""));
                break;
            default:
                break;
        }
        String apres = Ipaiement.toString();
        if(!avant.equals(apres)){
            if(Ipaiement.getId() != -1){
                Ipaiement.setBeta(InterfacePaiement.BETA_MODIFIE);
            }
        }
        listeData.set(rowIndex, Ipaiement);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }
}
