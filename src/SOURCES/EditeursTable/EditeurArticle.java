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
import SOURCES.Interface.InterfaceArticle;
import SOURCES.ModelsTable.ModeleListePaiement;

/**
 *
 * @author user
 */
public class EditeurArticle extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private Vector<InterfaceArticle> listeArticles;
    private ModeleListePaiement modeleListePaiement;

    public EditeurArticle(Vector<InterfaceArticle> listeArticles, ModeleListePaiement modeleListePaiement) {
        this.listeArticles = listeArticles;
        this.modeleListePaiement = modeleListePaiement;
        initCombo();
    }
    
    public int getTailleCombo(){
        return this.champEditionCombo.getItemCount();
    }
    
    public void initCombo() {
        this.champEditionCombo.removeAllItems();
        if (this.listeArticles != null) {
            for (InterfaceArticle article : this.listeArticles) {
                if (this.modeleListePaiement != null) {
                    double reste = this.modeleListePaiement.getReste(article.getId());
                    if(reste > 0){
                        //System.out.println("reste " + reste+", frais = " + article.getNom());
                        this.champEditionCombo.addItem(article.getNom());
                    }
                }else{
                    this.champEditionCombo.addItem(article.getNom());
                }
            }
        }

    }

    private int getIdArticle(String nom) {
        for (InterfaceArticle articleRech : this.listeArticles) {
            String id_nom = articleRech.getNom();
            if (id_nom.trim().equals(nom.trim())) {
                return articleRech.getId();
            }
        }
        return -1;
    }

    private String getArticle(int id) {
        for (InterfaceArticle articleRech : this.listeArticles) {
            if (id == articleRech.getId()) {
                return articleRech.getNom();
            }
        }
        return "";
    }

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        return getIdArticle(champEditionCombo.getSelectedItem() + "");
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        initCombo();
        String nomArticle = getArticle(Integer.parseInt(value + ""));
        if(nomArticle.trim().length() != 0){
            this.champEditionCombo.setSelectedItem(nomArticle);
        }
        return champEditionCombo;
    }

}
