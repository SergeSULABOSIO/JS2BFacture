/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.CallBack.EcouteurEnregistrement;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceClient;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfacePaiement;
import java.util.Vector;

/**
 *
 * @author user
 */
public class SortiesFacture {
    private InterfaceClient client;
    private Vector<InterfaceArticle> articles;
    private Vector<InterfacePaiement> paiements;
    private Vector<InterfaceEcheance> echeances;
    private EcouteurEnregistrement ecouteurEnregistrement;

    public SortiesFacture(InterfaceClient client, Vector<InterfaceArticle> articles, Vector<InterfacePaiement> paiements, Vector<InterfaceEcheance> echeances, EcouteurEnregistrement ecouteurEnregistrement) {
        this.client = client;
        this.articles = articles;
        this.paiements = paiements;
        this.echeances = echeances;
        this.ecouteurEnregistrement = ecouteurEnregistrement;
    }

    public InterfaceClient getClient() {
        return client;
    }

    public void setClient(InterfaceClient client) {
        this.client = client;
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

    public Vector<InterfaceEcheance> getEcheances() {
        return echeances;
    }

    public void setEcheances(Vector<InterfaceEcheance> echeances) {
        this.echeances = echeances;
    }

    public EcouteurEnregistrement getEcouteurEnregistrement() {
        return ecouteurEnregistrement;
    }

    public void setEcouteurEnregistrement(EcouteurEnregistrement ecouteurEnregistrement) {
        this.ecouteurEnregistrement = ecouteurEnregistrement;
    }
    
    
}
