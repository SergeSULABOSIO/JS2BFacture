/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTable;

import SOURCES.ModelsTable.ModeleListeEcheance;
import SOURCES.UI.CelluleProgressionTableau;
import SOURCES.UI.CelluleSimpleTableau;
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

    private String monnaie;
    private ImageIcon iconeEdition, iconeProgression;
    private ModeleListeEcheance modeleListeEcheance;

    public RenduTableEcheance(String monnaie, ImageIcon iconeEdition, ImageIcon iconeProgression, ModeleListeEcheance modeleListeEcheance) {
        this.monnaie = monnaie;
        this.iconeEdition = iconeEdition;
        this.iconeProgression = iconeProgression;
        this.modeleListeEcheance = modeleListeEcheance;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
        CelluleSimpleTableau celluleNum = null;
        switch (column) {
            case 0:
                celluleNum = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, null);
                celluleNum.ecouterSelection(isSelected, row);
                return celluleNum;
            case 1:
            case 2:
                celluleNum = new CelluleSimpleTableau(" " + Util.getDateFrancais(((Date) value)) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                celluleNum.ecouterSelection(isSelected, row);
                return celluleNum;
            case 3:
                celluleNum = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, null);
                celluleNum.ecouterSelection(isSelected, row);
                return celluleNum;
            case 4:
                celluleNum = new CelluleSimpleTableau(" " + value + " " + monnaie + " ", CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                celluleNum.ecouterSelection(isSelected, row);
                return celluleNum;
            case 5:
                double valeur = Double.parseDouble(value+"");
                double montDu = modeleListeEcheance.getEcheance_row(row).getMontantDu();
                CelluleProgressionTableau celluleProgress = new CelluleProgressionTableau(monnaie, valeur, montDu, iconeProgression);
                celluleProgress.ecouterSelection(isSelected, row);
                return celluleProgress;
            default:
                return null;
        }
    }
}
