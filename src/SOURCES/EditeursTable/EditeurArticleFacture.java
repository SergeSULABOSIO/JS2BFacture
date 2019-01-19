/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable;

import SOURCES.ModelsTable.ModeleListeArticles;
import java.awt.Component;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import SOURCES.Interface.InterfaceArticle;

/**
 *
 * @author user
 */
public class EditeurArticleFacture extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private Vector<InterfaceArticle> listeArticle;
    

    public EditeurArticleFacture(Vector<InterfaceArticle> listeArticle) {
        this.listeArticle = listeArticle;
        initCombo();
    }

    private void initCombo() {
        this.champEditionCombo.removeAllItems();
        for (InterfaceArticle article : listeArticle) {
            this.champEditionCombo.addItem(article.getNom());
        }
    }

    private int getIdArticle(String nom) {
        for (InterfaceArticle articleRech : listeArticle) {
            String id_nom = articleRech.getNom();
            if (id_nom.trim().equals(nom.trim())) {
                return articleRech.getId();
            }
        }
        return -1;
    }
    
    private InterfaceArticle getArticle_id(int id) {
        for (InterfaceArticle articleRech : listeArticle) {
            if (id == articleRech.getId()) {
                return articleRech;
            }
        }
        return null;
    }
    
    private InterfaceArticle getArticle_nom(String nom) {
        for (InterfaceArticle articleRech : listeArticle) {
            if (nom.trim().equals(articleRech.getNom().trim())) {
                return articleRech;
            }
        }
        return null;
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
        InterfaceArticle iArt = getArticle_id(Integer.parseInt(value+""));
        if(iArt != null){
            this.champEditionCombo.setSelectedItem(iArt.getNom());
        }
        return champEditionCombo;
    }

}
