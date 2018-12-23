/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.ArticleFacture;
import SOURCES.Interface.EcheanceFacture;
import SOURCES.Interface.PaiementFacture;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class Donnees {
    private Vector<ArticleFacture> articles;
    private Vector<PaiementFacture> paiements;
    private Vector<EcheanceFacture> echeance;

    public Donnees(Vector<ArticleFacture> articles, Vector<PaiementFacture> paiements, Vector<EcheanceFacture> echeance) {
        this.articles = articles;
        this.paiements = paiements;
        this.echeance = echeance;
    }

    public Donnees(Vector<ArticleFacture> articles, Vector<PaiementFacture> paiements) {
        this.articles = articles;
        this.paiements = paiements;
        this.echeance = new Vector<>();
    }

    public Vector<EcheanceFacture> getEcheance() {
        return echeance;
    }

    public void setEcheance(Vector<EcheanceFacture> echeance) {
        this.echeance = echeance;
    }

    

    public Vector<ArticleFacture> getArticles() {
        return articles;
    }

    public void setArticles(Vector<ArticleFacture> articles) {
        this.articles = articles;
    }

    public Vector<PaiementFacture> getPaiements() {
        return paiements;
    }

    public void setPaiements(Vector<PaiementFacture> paiements) {
        this.paiements = paiements;
    }

    @Override
    public String toString() {
        return "Donnees{" + "articles=" + articles + ", paiements=" + paiements + '}';
    }
    
    
}
