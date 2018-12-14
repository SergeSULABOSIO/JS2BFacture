/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.ArticleFacture;
import SOURCES.Interface.ClientFacture;
import SOURCES.CallBack.EcouteurAjout;
import ICONES.Icones;
import SOURCES.CallBack.EcouteurFacture;
import SOURCES.Interface.EntrepriseFacture;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class Parametres {
    private Icones icones;
    private String numero;
    private Vector<ArticleFacture> listArticles;
    private ClientFacture client;
    private EntrepriseFacture entreprise;
    private String monnaie;
    private double tva;
    private double remise;
    private EcouteurAjout ecouteurAjout;
    private EcouteurFacture ecouteurFacture = null;
    private Donnees donnees = null;

    public Parametres(String numero, Vector<ArticleFacture> listArticles, ClientFacture client, EntrepriseFacture entreprise, String monnaie, double tva, double remise, EcouteurAjout ecouteurAjout) {
        this.numero = numero;
        this.listArticles = listArticles;
        this.client = client;
        this.monnaie = monnaie;
        this.tva = tva;
        this.remise = remise;
        this.ecouteurAjout = ecouteurAjout;
        this.entreprise = entreprise;
        this.icones = new Icones();
    }

    public EntrepriseFacture getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(EntrepriseFacture entreprise) {
        this.entreprise = entreprise;
    }
    
    

    public Icones getIcones() {
        return icones;
    }

    public void setIcones(Icones icones) {
        this.icones = icones;
    }
    

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Donnees getDonnees() {
        return donnees;
    }

    public void setDonnees(Donnees donnees) {
        this.donnees = donnees;
    }

    public Vector<ArticleFacture> getListArticles() {
        return listArticles;
    }

    public void setListArticles(Vector<ArticleFacture> listArticles) {
        this.listArticles = listArticles;
    }

    public ClientFacture getClient() {
        return client;
    }

    public void setClient(ClientFacture client) {
        this.client = client;
    }

    public String getMonnaie() {
        return monnaie;
    }

    public void setMonnaie(String monnaie) {
        this.monnaie = monnaie;
    }

    public double getTva() {
        return tva;
    }

    public void setTva(double tva) {
        this.tva = tva;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public EcouteurAjout getEcouteurAjout() {
        return ecouteurAjout;
    }

    public void setEcouteurAjout(EcouteurAjout ecouteurAjout) {
        this.ecouteurAjout = ecouteurAjout;
    }

    public EcouteurFacture getEcouteurFacture() {
        return ecouteurFacture;
    }

    public void setEcouteurFacture(EcouteurFacture ecouteurFacture) {
        this.ecouteurFacture = ecouteurFacture;
    }

    
}
