/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Facture;


import Source.Objet.Eleve;
import Source.Objet.Frais;
import Source.Objet.Paiement;
import java.util.Vector;


/**
 *
 * @author HP Pavilion
 */
public class DonneesFacture {
    private Eleve eleve;
    private Vector<Frais> articles;
    private Vector<Paiement> paiements;
    
    public DonneesFacture(Eleve eleve, Vector<Frais> articles, Vector<Paiement> paiements) {
        this.eleve = eleve;
        this.articles = articles;
        this.paiements = paiements;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Vector<Frais> getArticles() {
        return articles;
    }

    public void setArticles(Vector<Frais> articles) {
        this.articles = articles;
    }

    public Vector<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(Vector<Paiement> paiements) {
        this.paiements = paiements;
    }

    @Override
    public String toString() {
        return "DonneesFacture{" + "eleve=" + eleve + ", articles=" + articles + ", paiements=" + paiements + '}';
    }
}
