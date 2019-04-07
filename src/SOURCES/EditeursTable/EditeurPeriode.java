/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable;

import java.awt.Component;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import SOURCES.Interface.InterfacePeriode;

/**
 *
 * @author user
 */
public class EditeurPeriode extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private Vector<InterfacePeriode> listePeriodes;

    public EditeurPeriode(Vector<InterfacePeriode> listePeriodes) {
        this.listePeriodes = listePeriodes;
        initCombo();
    }
    
    public int getTailleCombo(){
        return this.champEditionCombo.getItemCount();
    }
    
    public void initCombo() {
        this.champEditionCombo.removeAllItems();
        if (this.listePeriodes != null) {
            for (InterfacePeriode Iperiode : this.listePeriodes) {
                this.champEditionCombo.addItem(Iperiode.getNom());
                
                /*
                if (this.modeleListePaiement != null) {
                    double reste = this.modeleListePaiement.getReste(article.getId());
                    if(reste > 0){
                        //System.out.println("reste " + reste+", frais = " + article.getNom());
                        this.champEditionCombo.addItem(article.getNom());
                    }
                }else{
                    this.champEditionCombo.addItem(article.getNom());
                }
                */
                
            }
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
        System.out.println("valeur: " + value);
        String defaultSelection = getPeriode(Integer.parseInt(value+""));
        champEditionCombo.setSelectedItem(defaultSelection);
        return champEditionCombo;
    }

}
