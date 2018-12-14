/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable;


import SOURCES.Interface.ArticleFacture;
import SOURCES.ModelsTable.ModeleListePaiement;
import SOURCES.Interface.PaiementFacture;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.util.Date;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author user
 */

public class EditeurDatePaiement extends AbstractCellEditor implements TableCellEditor {

    private JDateChooser dateChooser = new JDateChooser();
    private ModeleListePaiement modeleListePaiement;
    private int updatedRow;
    
    public EditeurDatePaiement(Vector<ArticleFacture> listeArticle, ModeleListePaiement modeleListePaiement) {
        this.modeleListePaiement = modeleListePaiement;
    }

    

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        PaiementFacture updatedPaiementInTable = modeleListePaiement.getPaiement(updatedRow);
        return dateChooser.getDate();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        this.updatedRow = row;
        dateChooser.setDate(new Date());
        return dateChooser;
    }

}
