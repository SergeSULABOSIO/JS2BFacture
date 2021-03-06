/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable;


import SOURCES.ModelsTable.ModeleListePaiement;
import Source.Objet.Paiement;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author user
 */

public class EditeurDateFacture extends AbstractCellEditor implements TableCellEditor {

    private JDateChooser dateChooser = new JDateChooser();
    private ModeleListePaiement modeleListePaiement;
    private int updatedRow;
    
    public EditeurDateFacture(ModeleListePaiement modeleListePaiement) {
        this.modeleListePaiement = modeleListePaiement;
        this.dateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                fireEditingStopped();
            }
        });
    }

    

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        Paiement updatedPaiementInTable = modeleListePaiement.getPaiement(updatedRow);
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

