/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import SOURCES.Interface.InterfacePaiement;

/**
 *
 * @author user
 */
public class EditeurModePaiement extends AbstractCellEditor implements TableCellEditor {

    public JComboBox<String> champEditionCombo = new JComboBox();
    public EditeurModePaiement() {
        initCombo();
    }

    public void initCombo() {
        this.champEditionCombo.removeAllItems();
        this.champEditionCombo.addItem("CAISSE");
        this.champEditionCombo.addItem("BANQUE");
    }

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        return champEditionCombo.getSelectedIndex();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        int Imode = Integer.parseInt((value + "").trim());
        if (Imode == InterfacePaiement.MODE_BANQUE) {
            this.champEditionCombo.setSelectedIndex(InterfacePaiement.MODE_BANQUE);
        } else if (Imode == InterfacePaiement.MODE_CAISSE) {
            this.champEditionCombo.setSelectedIndex(InterfacePaiement.MODE_CAISSE);
        }
        return champEditionCombo;
    }

}
