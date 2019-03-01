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
import SOURCES.Utilitaires.Util;
import java.awt.Component;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author user
 */

public class RenduTableArticle implements TableCellRenderer {
    
    private InterfaceMonnaie monnaie;
    private ImageIcon iconeEdition;
    private Vector<InterfaceArticle> listeArticles;
    private ModeleListeArticles modeleListeArticles;

    public RenduTableArticle(InterfaceMonnaie monnaie, Vector<InterfaceArticle> listeArticles, ModeleListeArticles modeleListeArticles, ImageIcon iconeEdition) {
        this.monnaie = monnaie;
        this.iconeEdition = iconeEdition;
        this.listeArticles = listeArticles;
        this.modeleListeArticles = modeleListeArticles;
    }
    
    
    private String getArticle(int idArticle){
        for(InterfaceArticle article : listeArticles){
            if(idArticle == article.getId()){
                return article.getNom();
            }
        }
        return "";
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         //{"Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC", "Tranches"};
        CelluleSimpleTableau cellule = null;
        switch (column) {
            case 0:
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, null);
                break;
            case 1:
                String nomArticle = getArticle(Integer.parseInt(value+""));
                cellule = new CelluleSimpleTableau(" " + nomArticle + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 2:
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, iconeEdition);
                break;
            case 3:
            case 5:
            case 6:
            case 7:
                String mont = Util.getMontantFrancais(Double.parseDouble(value+""));
                cellule = new CelluleSimpleTableau(" " + mont + " " + monnaie.getCode() + " ", CelluleSimpleTableau.ALIGNE_DROITE, null);
                break;
            case 4:
                String mont01 = Util.getMontantFrancais(Double.parseDouble(value+""));
                cellule = new CelluleSimpleTableau(" " + mont01 + " " + monnaie.getCode() + " ", CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 8:
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, iconeEdition);
                break;
        }
        cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
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
