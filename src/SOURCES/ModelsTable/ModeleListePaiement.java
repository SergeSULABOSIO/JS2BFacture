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
import SOURCES.Interface.InterfacePaiement;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListePaiement extends AbstractTableModel {

    private String[] titreColonnes = {"Date", "Article", "Dépositaire", "Montant", "Reste"};
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
        switch (columnIndex) {
            case 0:
                return listeData.elementAt(rowIndex).getDate();
            case 1:
                if (listeData.elementAt(rowIndex).getNomArticle().trim().length() != 0) {
                    return listeData.elementAt(rowIndex).getIdArticle() + "_" + listeData.elementAt(rowIndex).getNomArticle();
                } else {
                    return listeData.elementAt(rowIndex).getNomArticle();
                }
            case 2:
                return listeData.elementAt(rowIndex).getNomDepositaire();
            case 3:
                return listeData.elementAt(rowIndex).getMontant();
            case 4:
                return getReste(listeData.elementAt(rowIndex).getIdArticle());
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Date.class;//Date
            case 1:
                return String.class;//NomArticle
            case 2:
                return String.class;//NomDepositaire
            case 3:
                return Double.class;//Montant
            case 4:
                return Double.class;//Reste
            default:
                return Object.class;
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 4) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        InterfacePaiement article = listeData.get(rowIndex);
        switch (columnIndex) {
            case 0:
                article.setDate((Date) aValue);
                break;
            case 1:
                String nom = aValue + "";
                if (nom.contains("_")) {
                    nom = nom.split("_")[1];
                }
                article.setNomArticle(nom);
                break;
            case 2:
                article.setNomDepositaire(aValue + "");
                break;
            case 3:
                //System.out.println("RESTE - AVANT MODIF : " + getReste(article.getIdArticle()));
                //System.out.println("MONTANT SAISI : " + Double.parseDouble(aValue + ""));
                article.setMontant(Double.parseDouble(aValue + ""));
                break;
            default:
                break;
        }
        listeData.set(rowIndex, article);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }
}
