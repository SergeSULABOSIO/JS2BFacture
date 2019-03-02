/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import java.util.Vector;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceEleve;
import SOURCES.Interface.InterfacePaiement;

/**
 *
 * @author HP Pavilion
 */
public class DonneesFacture {
    private InterfaceEleve eleve;
    private Vector<InterfaceArticle> articles;
    private Vector<InterfacePaiement> paiements;
    
    public DonneesFacture(InterfaceEleve eleve, Vector<InterfaceArticle> articles, Vector<InterfacePaiement> paiements) {
        this.eleve = eleve;
        this.articles = articles;
        this.paiements = paiements;
    }
    
    public Vector<InterfaceArticle> getArticles() {
        return articles;
    }

    public void setArticles(Vector<InterfaceArticle> articles) {
        this.articles = articles;
    }

    public Vector<InterfacePaiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(Vector<InterfacePaiement> paiements) {
        this.paiements = paiements;
    }

    public InterfaceEleve getEleve() {
        return eleve;
    }

    public void setEleve(InterfaceEleve eleve) {
        this.eleve = eleve;
    }

    @Override
    public String toString() {
        return "DonneesFacture{" + "eleve=" + eleve + ", articles=" + articles + ", paiements=" + paiements + '}';
    }
}
