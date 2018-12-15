/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EndusTable;

import SOURCES.UI.CelluleSimpleTableau;
import java.awt.Component;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author user
 */
public class RenduTablePaiement implements TableCellRenderer {

    private String monnaie;
    private ImageIcon iconeEdition;

    public RenduTablePaiement(String monnaie, ImageIcon iconeEdition) {
        this.monnaie = monnaie;
        this.iconeEdition = iconeEdition;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        CelluleSimpleTableau celluleNum = null;
        if (column == 0 || column == 1 || column == 2) {
            if(column == 0){
                celluleNum = new CelluleSimpleTableau(" " + ((Date)value).toLocaleString() + " ", false, iconeEdition);
            }else{
                celluleNum = new CelluleSimpleTableau(" " + value + " ", false, iconeEdition);
            }
        } else {
            if (column == 3) {
                celluleNum = new CelluleSimpleTableau(" " + value + " " + monnaie + " ", true, iconeEdition);
            } else {
                celluleNum = new CelluleSimpleTableau(" " + value + " " + monnaie + " ", true, null);
            }
        }
        celluleNum.ecouterSelection(isSelected, row);
        return celluleNum;
    }
}