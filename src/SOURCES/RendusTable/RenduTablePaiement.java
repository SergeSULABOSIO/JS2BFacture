/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTable;

import SOURCES.Interface.InterfacePaiement;
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
public class RenduTablePaiement implements TableCellRenderer {

    private String monnaie;
    private ImageIcon iconeEdition;

    public RenduTablePaiement(String monnaie, ImageIcon iconeEdition) {
        this.monnaie = monnaie;
        this.iconeEdition = iconeEdition;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"N°", "Date", "Article", "Référence", "Mode", "Montant reçu", "Reste"};
        CelluleSimpleTableau celluleNum = null;
        if (column == 0 || column == 1 || column == 2 || column == 3 || column == 4) {
            switch (column) {
                case 0:
                    celluleNum = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, null);
                    break;
                case 1:
                    celluleNum = new CelluleSimpleTableau(" " + Util.getDateFrancais(((Date)value)) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                    break;
                case 4:
                    String mode = "Null";
                    try{
                        int Imode = Integer.parseInt(value+"");
                        if(Imode == InterfacePaiement.MODE_BANQUE){
                            mode = "BANQUE";
                        }else if(Imode == InterfacePaiement.MODE_CAISSE){
                            mode = "CAISSE";
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    celluleNum = new CelluleSimpleTableau(" " + mode + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                    break;
                default:
                    celluleNum = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                    break;
            }
        } else {
            if (column == 5) {
                celluleNum = new CelluleSimpleTableau(" " + value + " " + monnaie + " ", CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
            } else {
                celluleNum = new CelluleSimpleTableau(" " + value + " " + monnaie + " ", CelluleSimpleTableau.ALIGNE_DROITE, null);
            }
        }
        celluleNum.ecouterSelection(isSelected, row);
        return celluleNum;
    }
}
