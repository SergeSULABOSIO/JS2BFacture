/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTable;

import SOURCES.Interface.InterfaceArticle;
import SOURCES.ModelsTable.ModeleListeEcheance;
import SOURCES.UI.CelluleProgressionTableau;
import SOURCES.UI.CelluleSimpleTableau;
import SOURCES.Utilitaires.ParametresFacture;
import SOURCES.Utilitaires.Util;
import java.awt.Component;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author user
 */
public class RenduTableEcheance implements TableCellRenderer {

    private ImageIcon iconeProgression;
    private ModeleListeEcheance modeleListeEcheance;
    private ParametresFacture parametresFacture;

    public RenduTableEcheance(ParametresFacture parametresFacture, ModeleListeEcheance modeleListeEcheance, ImageIcon iconeProgression) {
        this.iconeProgression = iconeProgression;
        this.modeleListeEcheance = modeleListeEcheance;
        this.parametresFacture = parametresFacture;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"N°", "Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
        CelluleSimpleTableau celluleNum = null;
        switch (column) {
            case 0:
                celluleNum = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, null);
                celluleNum.ecouterSelection(isSelected, row, InterfaceArticle.BETA_EXISTANT, hasFocus);
                return celluleNum;
            case 1:
                celluleNum = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, null);
                celluleNum.ecouterSelection(isSelected, row, InterfaceArticle.BETA_EXISTANT, hasFocus);
                return celluleNum;
            case 2:
                celluleNum = new CelluleSimpleTableau(" Du " + Util.getDateFrancais(((Date) value)) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, null);
                celluleNum.ecouterSelection(isSelected, row, InterfaceArticle.BETA_EXISTANT, hasFocus);
                return celluleNum;
            case 3:
                celluleNum = new CelluleSimpleTableau(" au " + Util.getDateFrancais(((Date) value)) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, null);
                celluleNum.ecouterSelection(isSelected, row, InterfaceArticle.BETA_EXISTANT, hasFocus);
                return celluleNum;
            case 4:
                celluleNum = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, null);
                celluleNum.ecouterSelection(isSelected, row, InterfaceArticle.BETA_EXISTANT, hasFocus);
                return celluleNum;
            case 5:
                String mont = Util.getMontantFrancais(Double.parseDouble(value+""));
                celluleNum = new CelluleSimpleTableau(" " + mont + " " + parametresFacture.getMonnaieOutPut().getCode() + " ", CelluleSimpleTableau.ALIGNE_DROITE, null);
                celluleNum.ecouterSelection(isSelected, row, InterfaceArticle.BETA_EXISTANT, hasFocus);
                return celluleNum;
            case 6:
                double valeur = Double.parseDouble(value+"");
                double montDu = modeleListeEcheance.getEcheance_row(row).getMontantDu();
                CelluleProgressionTableau celluleProgress = new CelluleProgressionTableau(parametresFacture.getMonnaieOutPut().getCode(), valeur, montDu, iconeProgression);
                celluleProgress.ecouterSelection(isSelected, row, hasFocus);
                return celluleProgress;
            default:
                return null;
        }
    }
}
