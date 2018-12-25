/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EndusTable;

import SOURCES.UI.CelluleProgressionTableau;
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
public class RenduTableEcheance implements TableCellRenderer {

    private String monnaie;
    private ImageIcon iconeEdition, iconeProgression;

    public RenduTableEcheance(String monnaie, ImageIcon iconeEdition, ImageIcon iconeProgression) {
        this.monnaie = monnaie;
        this.iconeEdition = iconeEdition;
        this.iconeProgression = iconeProgression;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        CelluleSimpleTableau celluleNum = null;
        if (column == 1 || column == 2) {
            celluleNum = new CelluleSimpleTableau(" " + ((Date) value).toLocaleString() + " ", false, iconeEdition);
            celluleNum.ecouterSelection(isSelected, row);
            return celluleNum;
        } else if (column == 4) {
            celluleNum = new CelluleSimpleTableau(" " + value + " " + monnaie + " ", true, iconeEdition);
            celluleNum.ecouterSelection(isSelected, row);
            return celluleNum;
        } else if(column == 5){
            int valeur = Integer.parseInt(value+"");
            CelluleProgressionTableau celluleProgress = new CelluleProgressionTableau(monnaie, valeur, 100, iconeEdition);
            celluleProgress.ecouterSelection(isSelected, row);
            return celluleProgress;
        }else{
            return null;
        }
    }
}
