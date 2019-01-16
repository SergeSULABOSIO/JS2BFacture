/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable;


import SOURCES.ModelsTable.ModeleListePaiement;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.util.Date;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import SOURCES.Interface.InterfacePaiement;

/**
 *
 * @author user
 */

public class EditeurDatePaiement extends AbstractCellEditor implements TableCellEditor {

    private JDateChooser dateChooser = new JDateChooser();
    private ModeleListePaiement modeleListePaiement;
    private int updatedRow;
    
    public EditeurDatePaiement(ModeleListePaiement modeleListePaiement) {
        this.modeleListePaiement = modeleListePaiement;
    }

    

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        InterfacePaiement updatedPaiementInTable = modeleListePaiement.getPaiement(updatedRow);
        updatedPaiementInTable.setDate(dateChooser.getDate());
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
