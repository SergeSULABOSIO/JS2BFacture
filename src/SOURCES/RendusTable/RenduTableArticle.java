/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTable;

import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.ModelsTable.ModeleListeArticles;
import SOURCES.UI.CelluleSimpleTableau;
import SOURCES.Utilitaires.DonneesFacture;
import SOURCES.Utilitaires.LiaisonPeriodeFrais;
import SOURCES.Utilitaires.ParametresFacture;
import SOURCES.Utilitaires.Util;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author user
 */
public class RenduTableArticle implements TableCellRenderer {

    private ImageIcon iconeEdition;
    private ModeleListeArticles modeleListeArticles;
    private DonneesFacture donneesFacture;
    private ParametresFacture parametresFacture;

    public RenduTableArticle(DonneesFacture donneesFacture, ParametresFacture parametresFacture, ModeleListeArticles modeleListeArticles, ImageIcon iconeEdition) {
        this.donneesFacture = donneesFacture;
        this.parametresFacture = parametresFacture;
        this.iconeEdition = iconeEdition;
        this.modeleListeArticles = modeleListeArticles;
    }

    private String getArticle(int idArticle) {
        for (InterfaceArticle article : donneesFacture.getArticles()) {
            if (idArticle == article.getId()) {
                String labelArticle = article.getNom();
                String pourc = "(";
                for(int i=0; i<article.getLiaisonsPeriodes().size(); i++){
                    if(i != article.getLiaisonsPeriodes().size() - 1){
                        pourc += (article.getLiaisonsPeriodes()).elementAt(i).getPourcentage() +"% - ";
                    }else{
                        pourc += (article.getLiaisonsPeriodes()).elementAt(i).getPourcentage() +"%";
                    }
                }
                pourc += ")"; 
                return labelArticle + " " + pourc;
            }
        }
        return "";
    }

    private String getCodeMonnaie(int row) {
        InterfaceArticle article = modeleListeArticles.getArticle(row);
        if (article != null) {
            for (InterfaceMonnaie Imonnaie : parametresFacture.getListeMonnaies()) {
                if (article.getIdMonnaie() == Imonnaie.getId()) {
                    return Imonnaie.getCode();
                }
            }
        }
        return "";
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"N°", "Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC", "Tranches"};
        CelluleSimpleTableau cellule = null;
        switch (column) {
            case 0:
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, null);
                break;
            case 1:
                cellule = new CelluleSimpleTableau(" " + getArticle(Integer.parseInt(value + "")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, null);
                break;
            case 2:
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value + "")) + " ", CelluleSimpleTableau.ALIGNE_CENTRE, null);
                break;
            case 3:
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleSimpleTableau.ALIGNE_DROITE, null);
                break;
            case 4:
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleSimpleTableau.ALIGNE_DROITE, null);
                break;
            case 5:
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleSimpleTableau.ALIGNE_DROITE, null);
                break;
            case 6:
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleSimpleTableau.ALIGNE_DROITE, null);
                break;
            case 7:
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleSimpleTableau.ALIGNE_DROITE, null);
                break;
            case 8:
                cellule = new CelluleSimpleTableau(" " + value + " tranche(s) ", CelluleSimpleTableau.ALIGNE_DROITE, null);
                break;
        }
        if(cellule != null){
            cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
        }
        return cellule;
    }

    private int getBeta(int row) {
        if (this.modeleListeArticles != null) {
            InterfaceArticle Iarticle = this.modeleListeArticles.getArticle(row);
            if (Iarticle != null) {
                return Iarticle.getBeta();
            }
        }
        return InterfaceArticle.BETA_NOUVEAU;
    }
}
