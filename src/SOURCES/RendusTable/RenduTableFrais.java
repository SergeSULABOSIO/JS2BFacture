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
import Source.Objet.LiaisonFraisPeriode;
import Source.Objet.Monnaie;
import Source.Objet.Periode;
import Source.UI.CelluleTableauSimple;
import java.awt.Component;
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
                String pourc = "";
                for (int i = 0; i < article.getLiaisonsPeriodes().size(); i++) {
                    LiaisonFraisPeriode liaison = article.getLiaisonsPeriodes().elementAt(i);
                    if (liaison != null) {
                        
                        double pc = liaison.getPourcentage();
                        long signaturePeriode = liaison.getSignaturePeriode();
                        
                        String nomPeriode = "";
                        Periode p = getPeriode(signaturePeriode);
                        if(p != null){
                            nomPeriode = p.getNom();
                        }
                        
                        if (i != article.getLiaisonsPeriodes().size() - 1) {
                            pourc += pourc + nomPeriode + ": " + pc + "% - ";
                        } else {
                            pourc += nomPeriode + ": " + pc + "%";
                        }
                    }

                }
                return labelArticle + " (" + pourc + ").";
            }
        }
        return "";
    }
    
    private Periode getPeriode(long signaturePeriode){
        if(parametresFacture != null){
            for(Periode p: parametresFacture.getListePeriodes()){
                if(p.getSignature() == signaturePeriode){
                    return p;
                }
            }
        }
        return null;
    }

    private String getCodeMonnaie(int idMonnaie) {
        if(parametresFacture != null){
            for(Monnaie m: parametresFacture.getListeMonnaies()){
                if(m.getId() == idMonnaie){
                    return m.getCode();
                }
            }
        }
        return "";
    }
    
    
    private String getMontantFrais(int idFrais){
        String txt = "";
        if(donneesFacture != null){
            for(Frais f: donneesFacture.getArticles()){
                if(f.getId() == idFrais){
                    return UtilFacture.getMontantFrancais(f.getMontantDefaut()) + " " + getCodeMonnaie(f.getIdMonnaie());
                }
            }
        }
        return txt;
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
                cellule = new CelluleTableauSimple(couleurBasique, " " + getMontantFrais(Integer.parseInt(value + "")) + " ", CelluleTableauSimple.ALIGNE_CENTRE, null);
                break;
        }
        if (cellule != null) {
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
