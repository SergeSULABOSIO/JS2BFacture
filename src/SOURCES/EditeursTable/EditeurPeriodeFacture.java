/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable;

import Source.Interface.InterfacePeriode;
import Source.Objet.Periode;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author user
 */
public class EditeurPeriodeFacture extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private Vector<Periode> listePeriodes;

    public EditeurPeriodeFacture(Vector<Periode> listePeriodes) {
        this.listePeriodes = listePeriodes;
        initCombo();
    }
    
    public int getTailleCombo(){
        return this.champEditionCombo.getItemCount();
    }
    
    public void initCombo() {
        this.champEditionCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Clic: " + e.getActionCommand());
                fireEditingStopped();
            }
        });
        this.champEditionCombo.removeAllItems();
        if (this.listePeriodes != null) {
            this.listePeriodes.forEach((Iperiode) -> {
                this.champEditionCombo.addItem(Iperiode.getNom());
            });
        }
    }

    private int getIdPeriode(String nom) {
        for (InterfacePeriode articleRech : this.listePeriodes) {
            String nomA = articleRech.getNom();
            if (nomA.trim().equals(nom.trim())) {
                //System.out.println("Selection USER: " + articleRech.getId());
                return articleRech.getId();
            }
        }
        return -1;
    }

    private String getPeriode(int id) {
        for (InterfacePeriode articleRech : this.listePeriodes) {
            if (id == articleRech.getId()) {
                return articleRech.getNom();
            }
        }
        return "";
    }

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        return getIdPeriode(champEditionCombo.getSelectedItem() + "");
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        initCombo();
        //System.out.println("valeur: " + value);
        String defaultSelection = getPeriode(Integer.parseInt(value+""));
        champEditionCombo.setSelectedItem(defaultSelection);
        return champEditionCombo;
    }

}


