/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable;

import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Utilitaires.Util;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import SOURCES.Interface.InterfaceArticle;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeArticles extends AbstractTableModel {

    private String[] titreColonnes = {"N°", "Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC", "Tranches"};
    private Vector<InterfaceArticle> listeData = new Vector<>();
    private Vector<InterfaceArticle> listeTypeArticle;
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    //private boolean canPayVAT;
    private double pourcTVA = 16;

    public ModeleListeArticles(JScrollPane parent, double pourcTVA, Vector<InterfaceArticle> listeTypeArticle, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.pourcTVA = pourcTVA;
        this.listeTypeArticle = listeTypeArticle;
        appliquerAssigetissementTVA();
    }

    public void setListeArticles(Vector<InterfaceArticle> listeData) {
        this.listeData = listeData;
        appliquerAssigetissementTVA();
        redessinerTable();
    }

    public InterfaceArticle getArticle(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceArticle art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public InterfaceArticle getArticle_id(int id) {
        if (id != -1) {
            for (InterfaceArticle art : listeData) {
                //System.out.println(" - id="+art.getId());
                if (id == art.getId()) {
                    return art;
                }
            }
        }
        return null;
    }

    public Vector<InterfaceArticle> getListeData() {
        return listeData;
    }

    public void setVat(double pourc) {
        this.pourcTVA = pourc;
        appliquerAssigetissementTVA();
        redessinerTable();
    }

    private void appliquerAssigetissementTVA() {
        changeVat();
    }

    private void changeVat() {
        listeData.forEach((art) -> {
            art.setTvaPoucentage(pourcTVA);
        });
    }

    public void AjouterArticle(InterfaceArticle art) {
        this.listeData.add(art);
        appliquerAssigetissementTVA();
        redessinerTable();
    }

    public void SupprimerArticle(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceArticle articl = listeData.elementAt(row);
            if (articl != null) {
                int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir supprimer cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    if (row <= listeData.size()) {
                        this.listeData.removeElementAt(row);
                    }
                    redessinerTable();
                }
            }
        }
    }

    public double getTotal_Net() {
        double mntTva = 0;
        double mntTTC = 0;
        for (InterfaceArticle art : listeData) {
            mntTva = mntTva + art.getTvaMontant();
            mntTTC = mntTTC + art.getTotalTTC();
        }
        return Util.round((mntTTC - mntTva), 2);
    }

    public double getTotal_Net_AvantRabais() {
        double mnt = 0;
        for (InterfaceArticle art : listeData) {
            mnt = mnt + art.getPrixUHT_avant_rabais();
        }
        return Util.round((mnt), 2);
    }

    public double getTotal_TVA() {
        double a = 0;
        for (InterfaceArticle art : listeData) {
            a = a + art.getTvaMontant();
        }
        return Util.round(a, 2);
    }

    public double getTotal_Rabais() {
        double a = 0;
        for (InterfaceArticle art : listeData) {
            a = a + art.getRabais();
        }
        return Util.round(a, 2);
    }

    public double getTotal_TTC() {
        double a = 0;
        for (InterfaceArticle art : listeData) {
            a = a + art.getTotalTTC();
        }
        return Util.round(a, 2);
    }

    public void viderListe() {
        int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir vider cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            this.listeData.removeAllElements();
            redessinerTable();
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

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //{"N°", "Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC", "Tranches"};
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1) + "";
            case 1:
                return listeData.elementAt(rowIndex).getId();
            case 2:
                return listeData.elementAt(rowIndex).getQte();
            case 3:
                return listeData.elementAt(rowIndex).getPrixUHT_avant_rabais();
            case 4:
                return listeData.elementAt(rowIndex).getRabais();
            case 5:
                return listeData.elementAt(rowIndex).getPrixUHT_apres_rabais();
            case 6:
                return listeData.elementAt(rowIndex).getTvaMontant();
            case 7:
                return listeData.elementAt(rowIndex).getTotalTTC();
            default:
                return listeData.elementAt(rowIndex).getTranches();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"N°", "Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC", "Tranches"};
        switch (columnIndex) {
            case 0:
                return String.class;//N°
            case 1:
                return Integer.class;//Nom
            case 2:
                return Double.class;//Qte
            case 3:
                return Double.class;//Prix U
            case 4:
                return Double.class;//Rabais
            case 5:
                return Double.class;//Prix U
            case 6:
                return Double.class;//Mnt Tva
            case 7:
                return Double.class;//Mnt TTC
            case 8:
                return Integer.class;//Tranches
            default:
                return Object.class;
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1 || columnIndex == 2 || columnIndex == 4 || columnIndex == 8) {
            return true;
        } else {
            return false;
        }
    }

    private void updateNomArticle(InterfaceArticle newArticle) {
        if (newArticle != null && listeTypeArticle != null) {
            for (InterfaceArticle Iarticle : listeTypeArticle) {
                if (Iarticle.getId() == newArticle.getId()) {
                    newArticle.setNom(Iarticle.getNom());
                    newArticle.setPrixUHT_avant_rabais(Iarticle.getPrixUHT_avant_rabais());
                    newArticle.setUnite(Iarticle.getUnite());
                    newArticle.setTranches(Iarticle.getTranches());
                    return;
                }
            }
        }
        redessinerTable();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC", "Tranches"};
        InterfaceArticle article = listeData.get(rowIndex);
        switch (columnIndex) {
            case 1:
                article.setId(Integer.parseInt(aValue + ""));
                updateNomArticle(article);
                break;
            case 2:
                article.setQte(Double.parseDouble(aValue + ""));
                break;
            case 4:
                article.setRabais(Double.parseDouble(aValue + ""));
                break;
            case 8:
                article.setTranches(Integer.parseInt(aValue + ""));
                break;
            default:
                break;
        }
        //System.out.println("idd = "+article.getId());
        listeData.set(rowIndex, article);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

}
