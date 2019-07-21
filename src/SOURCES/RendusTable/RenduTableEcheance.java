/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTable;


import SOURCES.ModelsTable.ModeleListeEcheance;
import SOURCES.UI.CelluleProgressionFacture;
import SOURCES.Utilitaires_Facture.ParametresFacture;
import SOURCES.Utilitaires_Facture.UtilFacture;
import Source.Interface.InterfaceFrais;
import Source.Objet.CouleurBasique;
import Source.UI.CelluleTableauSimple;
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
    private CouleurBasique couleurBasique;

    public RenduTableEcheance(CouleurBasique couleurBasique, ParametresFacture parametresFacture, ModeleListeEcheance modeleListeEcheance, ImageIcon iconeProgression) {
        this.couleurBasique = couleurBasique;
        this.iconeProgression = iconeProgression;
        this.modeleListeEcheance = modeleListeEcheance;
        this.parametresFacture = parametresFacture;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"N°", "Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
        CelluleTableauSimple celluleNum = null;
        switch (column) {
            case 0:
                celluleNum = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_CENTRE, null);
                celluleNum.ecouterSelection(isSelected, row, InterfaceFrais.BETA_EXISTANT, hasFocus);
                return celluleNum;
            case 1:
                celluleNum = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_GAUCHE, null);
                celluleNum.ecouterSelection(isSelected, row, InterfaceFrais.BETA_EXISTANT, hasFocus);
                return celluleNum;
            case 2:
                celluleNum = new CelluleTableauSimple(couleurBasique, " Du " + UtilFacture.getDateFrancais(((Date) value)) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, null);
                celluleNum.ecouterSelection(isSelected, row, InterfaceFrais.BETA_EXISTANT, hasFocus);
                return celluleNum;
            case 3:
                celluleNum = new CelluleTableauSimple(couleurBasique, " au " + UtilFacture.getDateFrancais(((Date) value)) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, null);
                celluleNum.ecouterSelection(isSelected, row, InterfaceFrais.BETA_EXISTANT, hasFocus);
                return celluleNum;
            case 4:
                celluleNum = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_GAUCHE, null);
                celluleNum.ecouterSelection(isSelected, row, InterfaceFrais.BETA_EXISTANT, hasFocus);
                return celluleNum;
            case 5:
                String mont = UtilFacture.getMontantFrancais(Double.parseDouble(value+""));
                celluleNum = new CelluleTableauSimple(couleurBasique, " " + mont + " " + parametresFacture.getMonnaieOutPut().getCode() + " ", CelluleTableauSimple.ALIGNE_DROITE, null);
                celluleNum.ecouterSelection(isSelected, row, InterfaceFrais.BETA_EXISTANT, hasFocus);
                return celluleNum;
            case 6:
                double valeur = Double.parseDouble(value+"");
                double montDu = modeleListeEcheance.getEcheance_row(row).getMontantDu();
                CelluleProgressionFacture celluleProgress = new CelluleProgressionFacture(couleurBasique, parametresFacture.getMonnaieOutPut().getCode(), valeur, montDu, iconeProgression);
                celluleProgress.ecouterSelection(isSelected, row, hasFocus);
                return celluleProgress;
            default:
                return null;
        }
    }
}
