/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTable;


import SOURCES.ModelsTable.ModeleListeFrais;
import SOURCES.Utilitaires_Facture.DonneesFacture;
import SOURCES.Utilitaires_Facture.ParametresFacture;
import SOURCES.Utilitaires_Facture.UtilFacture;
import Source.Interface.InterfaceFrais;
import Source.Objet.CouleurBasique;
import Source.Objet.Frais;
import Source.Objet.Monnaie;
import Source.UI.CelluleTableauSimple;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author user
 */
public class RenduTableFrais implements TableCellRenderer {

    private ModeleListeFrais modeleListeArticles;
    private DonneesFacture donneesFacture;
    private ParametresFacture parametresFacture;
    private CouleurBasique couleurBasique;

    public RenduTableFrais(CouleurBasique couleurBasique, DonneesFacture donneesFacture, ParametresFacture parametresFacture, ModeleListeFrais modeleListeArticles) {
        this.couleurBasique = couleurBasique;
        this.donneesFacture = donneesFacture;
        this.parametresFacture = parametresFacture;
        this.modeleListeArticles = modeleListeArticles;
    }

    private String getFrais(int idArticle) {
        for (Frais article : donneesFacture.getArticles()) {
            if (idArticle == article.getId()) {
                String labelArticle = article.getNom();
                String pourc = "(";
                for(int i=0; i<article.getLiaisonsPeriodes().size(); i++){
                    if(i != article.getLiaisonsPeriodes().size() - 1){
                        pourc += (article.getLiaisonsPeriodes()).elementAt(i).getPourcentage() +"% - ";
                    }else{
                        pourc += (article.getLiaisonsPeriodes()).elementAt(i).getPourcentage() +"%";
                    }
                }
                pourc += ")"; 
                return labelArticle + " " + pourc;
            }
        }
        return "";
    }

    private String getCodeMonnaie(int row) {
        Frais article = modeleListeArticles.getFrais(row);
        if (article != null) {
            for (Monnaie Imonnaie : parametresFacture.getListeMonnaies()) {
                if (article.getIdMonnaie() == Imonnaie.getId()) {
                    return Imonnaie.getCode();
                }
            }
        }
        return "";
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"N°", "Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC", "Tranches"};
        CelluleTableauSimple cellule = null;
        switch (column) {
            case 0:
                cellule = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_CENTRE, null);
                break;
            case 1:
                cellule = new CelluleTableauSimple(couleurBasique, " " + getFrais(Integer.parseInt(value + "")) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, null);
                break;
            case 2:
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilFacture.getMontantFrancais(Double.parseDouble(value + "")) + " ", CelluleTableauSimple.ALIGNE_CENTRE, null);
                break;
            case 3:
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilFacture.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleTableauSimple.ALIGNE_DROITE, null);
                break;
            case 4:
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilFacture.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleTableauSimple.ALIGNE_DROITE, null);
                break;
            case 5:
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilFacture.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleTableauSimple.ALIGNE_DROITE, null);
                break;
            case 6:
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilFacture.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleTableauSimple.ALIGNE_DROITE, null);
                break;
            case 7:
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilFacture.getMontantFrancais(Double.parseDouble(value + "")) + " " + getCodeMonnaie(row) + " ", CelluleTableauSimple.ALIGNE_DROITE, null);
                break;
            case 8:
                cellule = new CelluleTableauSimple(couleurBasique, " " + value + " tranche(s) ", CelluleTableauSimple.ALIGNE_DROITE, null);
                break;
        }
        if(cellule != null){
            cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
        }
        return cellule;
    }

    private int getBeta(int row) {
        if (this.modeleListeArticles != null) {
            Frais Iarticle = this.modeleListeArticles.getFrais(row);
            if (Iarticle != null) {
                return Iarticle.getBeta();
            }
        }
        return InterfaceFrais.BETA_NOUVEAU;
    }
}
