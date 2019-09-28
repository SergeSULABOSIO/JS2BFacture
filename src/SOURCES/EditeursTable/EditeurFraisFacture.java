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
import SOURCES.ModelsTable.ModeleListePaiement;
import Source.Objet.Frais;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author user
 */
public class EditeurFraisFacture extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private Vector<Frais> listeArticles;
    private ModeleListePaiement modeleListePaiement;

    public EditeurFraisFacture(Vector<Frais> listeArticles, ModeleListePaiement modeleListePaiement) {
        this.listeArticles = listeArticles;
        this.modeleListePaiement = modeleListePaiement;
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
        if (this.listeArticles != null) {
            for (Frais article : this.listeArticles) {
                if (this.modeleListePaiement != null) {
                    double reste = this.modeleListePaiement.getReste(article.getId(), -1);
                    if(reste > 0){
                        //System.out.println("reste " + reste+", frais = " + article.getNom());
                        this.champEditionCombo.addItem(article.getNom());
                    }else{
                        //On doit désactiver le champ de saisie de frais
                        
                    }
                }else{
                    this.champEditionCombo.addItem(article.getNom());
                }
            }
        }

    }

    private int getIdFrais(String nom) {
        for (Frais articleRech : this.listeArticles) {
            String nomA = articleRech.getNom();
            if (nomA.trim().equals(nom.trim())) {
                //System.out.println("Selection USER: " + articleRech.getId());
                return articleRech.getId();
            }
        }
        return -1;
    }

    private String getFrais(int id) {
        for (Frais articleRech : this.listeArticles) {
            if (id == articleRech.getId()) {
                return articleRech.getNom();
            }
        }
        return "";
    }

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        if(champEditionCombo.getItemCount() != 0){
            return getIdFrais(champEditionCombo.getSelectedItem() + "");
        }else{
            return -1;
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        initCombo();
        //System.out.println("valeur: " + value);
        String defaultSelection = getFrais(Integer.parseInt(value+""));
        
        if(champEditionCombo.getItemCount() != 0){
            champEditionCombo.setSelectedItem(defaultSelection);
            return champEditionCombo;
        }else{
            return null;
        }
    }

}


