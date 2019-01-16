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
    private ModeleListeArticles modeleListeArticles;
    private int updatedRow;

    public EditeurArticleFacture(Vector<InterfaceArticle> listeArticle, ModeleListeArticles modeleListeArticles) {
        this.listeArticle = listeArticle;
        this.modeleListeArticles = modeleListeArticles;
        initCombo();
    }

    private void initCombo() {
        this.champEditionCombo.removeAllItems();
        for (InterfaceArticle article : listeArticle) {
            this.champEditionCombo.addItem(article.getId() + "_" + article.getNom());
        }
    }

    private InterfaceArticle getArticle(String nom) {
        for (InterfaceArticle articleRech : listeArticle) {
            String id_nom = articleRech.getId() + "_" + articleRech.getNom();
            if (id_nom.trim().equals(nom.trim())) {
                return articleRech;
            }
        }
        return null;
    }

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        String nomSelArt = champEditionCombo.getSelectedItem() + "";
        InterfaceArticle artFromBase = getArticle(nomSelArt);
        if (artFromBase != null) {
            InterfaceArticle updatedArticleInTable = modeleListeArticles.getArticle(updatedRow);
            if (updatedArticleInTable != null) {
                //On charge infos de base sur l'article qui vient d'être séléctionné par le client
                updatedArticleInTable.setPrixUHT_avant_rabais(artFromBase.getPrixUHT_avant_rabais());
                updatedArticleInTable.setUnite(artFromBase.getUnite());
                updatedArticleInTable.setId(artFromBase.getId());
                updatedArticleInTable.setTranches(artFromBase.getTranches());
                //System.out.println("id="+artFromBase.getId());
            }
        }
        return nomSelArt;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        this.updatedRow = row;
        this.champEditionCombo.setSelectedItem(value);
        return champEditionCombo;
    }

}
