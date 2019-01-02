/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTable;

import SOURCES.UI.CelluleSimpleTableau;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author user
 */

public class RenduTableArticle implements TableCellRenderer {
    
    private String monnaie;
    private ImageIcon iconeEdition;

    public RenduTableArticle(String monnaie, ImageIcon iconeEdition) {
        this.monnaie = monnaie;
        this.iconeEdition = iconeEdition;
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
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 2:
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, iconeEdition);
                break;
            case 3:
            case 5:
            case 6:
            case 7:
                cellule = new CelluleSimpleTableau(" " + value + " " + monnaie + " ", CelluleSimpleTableau.ALIGNE_DROITE, null);
                break;
            case 4:
                cellule = new CelluleSimpleTableau(" " + value + " " + monnaie + " ", CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 8:
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, iconeEdition);
                break;
        }
        cellule.ecouterSelection(isSelected, row);
        return cellule;
    }
}
