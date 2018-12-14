/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable;

import SOURCES.Interface.ArticleFacture;
import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Utilitaires.Util;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeArticles extends AbstractTableModel {

    private String[] titreColonnes = {"Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC"};
    private Vector<ArticleFacture> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    //private boolean canPayVAT;
    private double pourcTVA = 16;

    public ModeleListeArticles(JScrollPane parent, double pourcTVA, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.pourcTVA = pourcTVA;

        appliquerAssigetissementTVA();
    }

    public void setListeArticles(Vector<ArticleFacture> listeData) {
        this.listeData = listeData;
        appliquerAssigetissementTVA();
        redessinerTable();
    }

    public ArticleFacture getArticle(int row) {
        if (row < listeData.size() && row != -1) {
            ArticleFacture art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public ArticleFacture getArticle_id(int id) {
        if (id != -1) {
            for (ArticleFacture art : listeData) {
                //System.out.println(" - id="+art.getId());
                if (id == art.getId()) {
                    return art;
                }
            }
        }
        return null;
    }

    public Vector<ArticleFacture> getListeData() {
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

    public void AjouterArticle(ArticleFacture art) {
        this.listeData.add(art);
        appliquerAssigetissementTVA();
        redessinerTable();
    }

    public void SupprimerArticle(int row) {
        if (row < listeData.size() && row != -1) {
            ArticleFacture articl = listeData.elementAt(row);
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
        for (ArticleFacture art : listeData) {
            mntTva = mntTva + art.getTvaMontant();
            mntTTC = mntTTC + art.getTotalTTC();
        }
        return Util.round((mntTTC - mntTva), 2);
    }

    public double getTotal_TVA() {
        double a = 0;
        for (ArticleFacture art : listeData) {
            a = a + art.getTvaMontant();
        }
        return Util.round(a, 2);
    }

    public double getTotal_Rabais() {
        double a = 0;
        for (ArticleFacture art : listeData) {
            a = a + art.getRabais();
        }
        return Util.round(a, 2);
    }

    public double getTotal_TTC() {
        double a = 0;
        for (ArticleFacture art : listeData) {
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
        switch (columnIndex) {
            case 0:
                if(listeData.elementAt(rowIndex).getNom().trim().length() != 0){
                    return listeData.elementAt(rowIndex).getId() + "_" + listeData.elementAt(rowIndex).getNom();
                }else{
                    return listeData.elementAt(rowIndex).getNom();
                }
            case 1:
                return listeData.elementAt(rowIndex).getQte();
            case 2:
                return listeData.elementAt(rowIndex).getPrixUHT_avant_rabais();
            case 3:
                return listeData.elementAt(rowIndex).getRabais();
            case 4:
                return listeData.elementAt(rowIndex).getPrixUHT_apres_rabais();
            case 5:
                return listeData.elementAt(rowIndex).getTvaMontant();
            default:
                return listeData.elementAt(rowIndex).getTotalTTC();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;//Nom
            case 1:
                return Double.class;//Qte
            case 2:
                return Double.class;//Prix U
            case 3:
                return Double.class;//Rabais
            case 4:
                return Double.class;//Prix U
            case 5:
                return Double.class;//Mnt Tva
            case 6:
                return Double.class;//Mnt TTC
            default:
                return Object.class;
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0 || columnIndex == 1 || columnIndex == 3) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ArticleFacture article = listeData.get(rowIndex);
        switch (columnIndex) {
            case 0:
                String nom = aValue + "";
                if(nom.contains("_")){
                    nom = nom.split("_")[1];
                }
                article.setNom(nom);
                break;
            case 1:
                article.setQte(Double.parseDouble(aValue + ""));
                break;
            case 3:
                article.setRabais(Double.parseDouble(aValue + ""));
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
