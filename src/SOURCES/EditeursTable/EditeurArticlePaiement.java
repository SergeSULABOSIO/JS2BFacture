/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable;

import SOURCES.ModelsTable.ModeleListeArticles;
import SOURCES.ModelsTable.ModeleListePaiement;
import java.awt.Component;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfacePaiement;

/**
 *
 * @author user
 */
public class EditeurArticlePaiement extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private Vector<InterfaceArticle> listeArticle;
    private ModeleListePaiement modeleListePaiement;
    private ModeleListeArticles modeleListeArticles;
    private int updatedRow;

    public EditeurArticlePaiement(Vector<InterfaceArticle> listeArticle, ModeleListeArticles modeleListeArticles, ModeleListePaiement modeleListePaiement) {
        this.listeArticle = listeArticle;
        this.modeleListePaiement = modeleListePaiement;
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

        //A partir de l'article que le user a séléctionné, on va trouver vite ce qu'il est sensé payer et le lui montrer
        InterfaceArticle artFromBase = getArticle(nomSelArt);
        if (artFromBase != null) {
            InterfaceArticle artFromFacture = modeleListeArticles.getArticle_id(artFromBase.getId());
            InterfacePaiement updatedPaiementInTable = modeleListePaiement.getPaiement(updatedRow);
            if (artFromFacture != null) {
                if (updatedPaiementInTable != null) {
                    //On charge infos de base sur l'article qui vient d'être séléctionné par le client
                    //double reste = modeleListePaiement.getReste(artFromFacture.getId());
                    updatedPaiementInTable.setIdArticle(artFromFacture.getId());
                    //updatedPaiementInTable.setMontant(0);
                    /*
                    if (reste != 0) {
                        updatedPaiementInTable.setMontant(reste);
                    }
                     */
                }
            } else {
                updatedPaiementInTable.setIdArticle(-1);
                updatedPaiementInTable.setMontant(0);
            }
        }
        /*
        
         */
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
