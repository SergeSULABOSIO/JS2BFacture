/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable;


import SOURCES.ModelsTable.ModeleListeEcheance;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.util.Date;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import SOURCES.Interface.InterfaceEcheance;

/**
 *
 * @author user
 */

public class EditeurDateInitialeEcheance extends AbstractCellEditor implements TableCellEditor {

    private JDateChooser dateChooser = new JDateChooser();
    private ModeleListeEcheance modeleListeEcheance;
    private int updatedRow;
    
    public EditeurDateInitialeEcheance(ModeleListeEcheance modeleListeEcheance) {
        this.modeleListeEcheance = modeleListeEcheance;
    }


    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        InterfaceEcheance updatedEcheanceInTable = modeleListeEcheance.getEcheance_row(updatedRow);
        updatedEcheanceInTable.setDateInitiale(dateChooser.getDate());
        this.modeleListeEcheance.redessinerTable();
        return dateChooser.getDate();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        this.updatedRow = row;
        dateChooser.setDate((Date)value);
        return dateChooser;
    }

}
