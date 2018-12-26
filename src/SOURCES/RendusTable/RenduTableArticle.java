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
        CelluleSimpleTableau celluleNum = null;
        if (column == 0) {
            celluleNum = new CelluleSimpleTableau(" " + value + " ", false, iconeEdition);
        }else{
            if(column == 1){
                celluleNum = new CelluleSimpleTableau(" " + value + " ", true, iconeEdition);
            }else{
                if(column == 3){
                    celluleNum = new CelluleSimpleTableau(" " + value + " " + monnaie + " ", true, iconeEdition);
                }else{
                    celluleNum = new CelluleSimpleTableau(" " + value + " " + monnaie + " ", true, null);
                }
            }
        }
        celluleNum.ecouterSelection(isSelected, row);
        
        return celluleNum;
    }
}
