/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import java.util.Vector;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.Interface.InterfaceEcheance;

/**
 *
 * @author HP Pavilion
 */
public class Donnees {
    private Vector<InterfaceArticle> articles;
    private Vector<InterfacePaiement> paiements;
    private Vector<InterfaceEcheance> echeance;

    public Donnees(Vector<InterfaceArticle> articles, Vector<InterfacePaiement> paiements, Vector<InterfaceEcheance> echeance) {
        this.articles = articles;
        this.paiements = paiements;
        this.echeance = echeance;
    }

    public Donnees(Vector<InterfaceArticle> articles, Vector<InterfacePaiement> paiements) {
        this.articles = articles;
        this.paiements = paiements;
        this.echeance = new Vector<>();
    }

    public Vector<InterfaceEcheance> getEcheance() {
        return echeance;
    }

    public void setEcheance(Vector<InterfaceEcheance> echeance) {
        this.echeance = echeance;
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

    @Override
    public String toString() {
        return "Donnees{" + "articles=" + articles + ", paiements=" + paiements + '}';
    }
    
    
}
