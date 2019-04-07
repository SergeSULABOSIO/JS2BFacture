/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTable;

import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.Interface.InterfacePeriode;
import SOURCES.ModelsTable.ModeleListePaiement;
import SOURCES.UI.CelluleSimpleTableau;
import SOURCES.Utilitaires.DonneesFacture;
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
public class RenduTablePaiement implements TableCellRenderer {

    private ImageIcon iconeEdition;
    private ModeleListePaiement modeleListePaiement;
    private ParametresFacture parametresFacture;
    private DonneesFacture donneesFacture;

    public RenduTablePaiement(DonneesFacture donneesFacture, ParametresFacture parametresFacture, ModeleListePaiement modeleListePaiement, ImageIcon iconeEdition) {
        this.iconeEdition = iconeEdition;
        this.modeleListePaiement = modeleListePaiement;
        this.donneesFacture = donneesFacture;
        this.parametresFacture = parametresFacture;
    }

    private String getCodeMonnaie(int row) {
        InterfaceArticle article = getArticle_(modeleListePaiement.getPaiement(row).getIdArticle());
        if (article != null) {
            for (InterfaceMonnaie Imonnaie : parametresFacture.getListeMonnaies()) {
                if (article.getIdMonnaie() == Imonnaie.getId()) {
                    return Imonnaie.getCode();
                }
            }
        }
        return "";
    }

    private String getArticle(int id) {
        InterfaceArticle Iart = getArticle_(id);
        if (Iart != null) {
            return Iart.getNom();
        }
        return "";
    }

    private InterfaceArticle getArticle_(int id) {
        for (InterfaceArticle articleRech : this.donneesFacture.getArticles()) {
            if (id == articleRech.getId()) {
                return articleRech;
            }
        }
        return null;
    }

    private String getMode(Object value) {
        String mode = "Null";
        try {
            int Imode = Integer.parseInt(value + "");
            if (Imode == InterfacePaiement.MODE_BANQUE) {
                mode = "BANQUE";
            } else if (Imode == InterfacePaiement.MODE_CAISSE) {
                mode = "CAISSE";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mode;
    }

    private String getPeriode(Object value) {
        String periode = "Null";
        int idPeriode = Integer.parseInt(value + "");
        for (InterfacePeriode Iperiode : parametresFacture.getListePeriodes()) {
            if(Iperiode.getId() == idPeriode){
                periode = Iperiode.getNom();
                break;
            }
        }
        return periode;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"N°", "Date", "Article", "Référence", "Mode", "Période", "Montant reçu", "Reste"};
        CelluleSimpleTableau celluleNum = null;
        if (column == 0 || column == 1 || column == 2 || column == 3 || column == 4 || column == 5) {
            switch (column) {
                case 0:
                    celluleNum = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, null);
                    break;
                case 1:
                    celluleNum = new CelluleSimpleTableau(" " + Util.getDateFrancais(((Date) value)) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                    break;
                case 2:
                    celluleNum = new CelluleSimpleTableau(" " + getArticle(Integer.parseInt(value + "")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                    break;
                case 4:
                    celluleNum = new CelluleSimpleTableau(" " + getMode(value) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                    break;
                case 5:
                    celluleNum = new CelluleSimpleTableau(" " + getPeriode(value) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                    break;
                default:
                    celluleNum = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                    break;
            }
        } else {
            if (column == 6) {
                celluleNum = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
            } else {
                celluleNum = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleSimpleTableau.ALIGNE_DROITE, null);
            }
        }
        celluleNum.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
        return celluleNum;
    }

    private int getBeta(int row) {
        if (this.modeleListePaiement != null) {
            InterfacePaiement Ipaiement = this.modeleListePaiement.getPaiement(row);
            if (Ipaiement != null) {
                return Ipaiement.getBeta();
            }
        }
        return InterfacePaiement.BETA_NOUVEAU;
    }
}
