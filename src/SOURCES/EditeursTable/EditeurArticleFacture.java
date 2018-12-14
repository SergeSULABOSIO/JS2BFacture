/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable;

import SOURCES.Interface.ArticleFacture;
import SOURCES.ModelsTable.ModeleListeArticles;
import java.awt.Component;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author user
 */
public class EditeurArticleFacture extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private Vector<ArticleFacture> listeArticle;
    private ModeleListeArticles modeleListeArticles;
    private int updatedRow;

    public EditeurArticleFacture(Vector<ArticleFacture> listeArticle, ModeleListeArticles modeleListeArticles) {
        this.listeArticle = listeArticle;
        this.modeleListeArticles = modeleListeArticles;
        initCombo();
    }

    private void initCombo() {
        this.champEditionCombo.removeAllItems();
        for (ArticleFacture article : listeArticle) {
            this.champEditionCombo.addItem(article.getId() + "_" + article.getNom());
        }
    }

    private ArticleFacture getArticle(String nom) {
        for (ArticleFacture articleRech : listeArticle) {
            String id_nom = articleRech.getId() + "_" + articleRech.getNom();
            if (id_nom.equals(nom)) {
                return articleRech;
            }
        }
        return null;
    }

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        String nomSelArt = champEditionCombo.getSelectedItem() + "";
        ArticleFacture artFromBase = getArticle(nomSelArt);
        if (artFromBase != null) {
            ArticleFacture updatedArticleInTable = modeleListeArticles.getArticle(updatedRow);
            if (updatedArticleInTable != null) {
                //On charge infos de base sur l'article qui vient d'être séléctionné par le client
                updatedArticleInTable.setPrixUHT_avant_rabais(artFromBase.getPrixUHT_avant_rabais());
                updatedArticleInTable.setUnite(artFromBase.getUnite());
                updatedArticleInTable.setId(artFromBase.getId());
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
