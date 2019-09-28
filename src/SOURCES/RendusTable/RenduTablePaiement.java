/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTable;


import SOURCES.ModelsTable.ModeleListePaiement;
import SOURCES.Utilitaires_Facture.DonneesFacture;
import SOURCES.Utilitaires_Facture.ParametresFacture;
import SOURCES.Utilitaires_Facture.UtilFacture;
import Source.GestionEdition;
import Source.Interface.InterfacePaiement;
import Source.Objet.CouleurBasique;
import Source.Objet.Frais;
import Source.Objet.Monnaie;
import Source.Objet.Paiement;
import Source.Objet.Periode;
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
public class RenduTablePaiement implements TableCellRenderer {

    private ImageIcon iconeEdition;
    private ModeleListePaiement modeleListePaiement;
    private ParametresFacture parametresFacture;
    private DonneesFacture donneesFacture;
    private CouleurBasique couleurBasique;
    private GestionEdition gestionEdition;

    public RenduTablePaiement(GestionEdition gestionEdition, CouleurBasique couleurBasique, DonneesFacture donneesFacture, ParametresFacture parametresFacture, ModeleListePaiement modeleListePaiement, ImageIcon iconeEdition) {
        this.couleurBasique = couleurBasique;
        this.iconeEdition = iconeEdition;
        this.modeleListePaiement = modeleListePaiement;
        this.donneesFacture = donneesFacture;
        this.parametresFacture = parametresFacture;
        this.gestionEdition = gestionEdition;
    }

    private String getCodeMonnaie(int row) {
        Frais article = getFrais_(modeleListePaiement.getPaiement(row).getIdFrais());
        if (article != null) {
            for (Monnaie Imonnaie : parametresFacture.getListeMonnaies()) {
                if (article.getIdMonnaie() == Imonnaie.getId()) {
                    return Imonnaie.getCode();
                }
            }
        }
        return "";
    }

    private String getFrais(int id) {
        Frais Iart = getFrais_(id);
        if (Iart != null) {
            return Iart.getNom();
        }
        return "";
    }

    private Frais getFrais_(int id) {
        for (Frais articleRech : this.donneesFacture.getArticles()) {
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
        for (Periode Iperiode : parametresFacture.getListePeriodes()) {
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
        CelluleTableauSimple celluleNum = null;
        
        ImageIcon icone = null;
        if(gestionEdition != null){
            Paiement Ieleve = this.modeleListePaiement.getPaiement(row);
            if(Ieleve != null){
                if(gestionEdition.isEditable(Ieleve.getId(), 1)){
                    icone = iconeEdition;
                }
            }
        }
        
        if (column == 0 || column == 1 || column == 2 || column == 3 || column == 4 || column == 5) {
            switch (column) {
                case 0:
                    celluleNum = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_CENTRE, null);
                    break;
                case 1:
                    celluleNum = new CelluleTableauSimple(couleurBasique, " " + UtilFacture.getDateFrancais(((Date) value)) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                    break;
                case 2:
                    celluleNum = new CelluleTableauSimple(couleurBasique, " " + getFrais(Integer.parseInt(value + "")) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                    break;
                case 4:
                    celluleNum = new CelluleTableauSimple(couleurBasique, " " + getMode(value) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                    break;
                case 5:
                    celluleNum = new CelluleTableauSimple(couleurBasique, " " + getPeriode(value) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                    break;
                default:
                    celluleNum = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                    break;
            }
        } else {
            if (column == 6) {
                celluleNum = new CelluleTableauSimple(couleurBasique, " " + UtilFacture.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
            } else {
                celluleNum = new CelluleTableauSimple(couleurBasique, " " + UtilFacture.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleTableauSimple.ALIGNE_DROITE, null);
            }
        }
        celluleNum.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
        return celluleNum;
    }

    private int getBeta(int row) {
        if (this.modeleListePaiement != null) {
            Paiement Ipaiement = this.modeleListePaiement.getPaiement(row);
            if (Ipaiement != null) {
                return Ipaiement.getBeta();
            }
        }
        return InterfacePaiement.BETA_NOUVEAU;
    }
}










