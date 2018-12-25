/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.CallBack.EcouteurAjout;
import ICONES.Icones;
import SOURCES.CallBack.EcouteurFacture;
import SOURCES.ModelsTable.ModeleListeArticles;
import SOURCES.ModelsTable.ModeleListeEcheance;
import SOURCES.ModelsTable.ModeleListePaiement;
import TEST_EXEMPLE.TESTPaiement;
import TEST_EXEMPLE.TESTProduit;
import java.util.Date;
import java.util.Vector;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceEntreprise;
import SOURCES.Interface.InterfaceClient;

/**
 *
 * @author HP Pavilion
 */
public class Parametres {
    private Icones icones;
    private String numero;
    private Vector<InterfaceArticle> listArticles;
    private InterfaceClient client;
    private InterfaceEntreprise entreprise;
    private String monnaie;
    private int idMonnaie;
    private int idFacture;
    private double tva;
    private double remise;
    private EcouteurAjout ecouteurAjout;
    private EcouteurFacture ecouteurFacture = null;
    private Donnees donnees = null;
    private String nomUtilisateur;
    private Date dateRepere;

    public Parametres(String nomUtilisateur, String numero, int idFacture, Vector<InterfaceArticle> listArticles, InterfaceClient client, InterfaceEntreprise entreprise, String monnaie, int idMonnaie, double tva, double remise, Date dateRepere) {
        this.nomUtilisateur = nomUtilisateur;
        this.numero = numero;
        this.listArticles = listArticles;
        this.client = client;
        this.monnaie = monnaie;
        this.tva = tva;
        this.remise = remise;
        this.entreprise = entreprise;
        this.idMonnaie = idMonnaie;
        this.idFacture = idFacture;
        this.icones = new Icones();
        this.dateRepere = dateRepere;
        this.ecouteurAjout = new EcouteurAjout() {
            @Override
            public void setAjoutArticle(ModeleListeArticles modeleListeArticles) {
                double tvaPrc = 16;
                double punit = 0;
                double rabais = 0;
                int nbTranches = 3;
                modeleListeArticles.AjouterArticle(new XX_Article(-1, "", 1, "Pièce", tvaPrc, punit, rabais, nbTranches));
            }

            @Override
            public void setAjoutPaiement(ModeleListePaiement modeleListePaiement) {
                double montant = 0;
                modeleListePaiement.AjouterPaiement(new XX_Paiement(-1, client.getId(), -1, client.getNom(), "", client.getNom(), montant, new Date()));
            }

            @Override
            public void setAjoutEcheance(ModeleListeEcheance modeleListeEcheance) {
                int nbEcheancesExistant = modeleListeEcheance.getRowCount();
                Date dateNewEch = dateRepere;
                dateNewEch.setDate(dateRepere.getDate()+5);
                modeleListeEcheance.AjouterEcheance(new XX_Echeance(-1, "TRANCHE N°" + (nbEcheancesExistant+1), idFacture, dateRepere, dateNewEch, numero, 0, 0, idMonnaie, monnaie));
            }
        };
    }

    public Date getDateRepere() {
        return dateRepere;
    }

    public void setDateRepere(Date dateRepere) {
        this.dateRepere = dateRepere;
    }
    
    

    public InterfaceEntreprise getEntreprise() {
        return entreprise;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public int getIdMonnaie() {
        return idMonnaie;
    }

    public void setIdMonnaie(int idMonnaie) {
        this.idMonnaie = idMonnaie;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }
    
    

    public void setEntreprise(InterfaceEntreprise entreprise) {
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

    public Vector<InterfaceArticle> getListArticles() {
        return listArticles;
    }

    public void setListArticles(Vector<InterfaceArticle> listArticles) {
        this.listArticles = listArticles;
    }

    public InterfaceClient getClient() {
        return client;
    }

    public void setClient(InterfaceClient client) {
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
