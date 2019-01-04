/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import java.util.Date;
import SOURCES.Interface.InterfacePaiement;

/**
 *
 * @author HP Pavilion
 */
public class XX_Paiement implements InterfacePaiement{
    public int id;
    public int idClient;
    public int idArticle;
    public int mode;
    public String reference;
    public String nomClient;
    public String nomArticle;
    public String nomDepositaire;
    public double montant;
    public Date date;

    public XX_Paiement() {
    }

    public XX_Paiement(int id, int idClient, int idArticle, String nomClient, String nomArticle, String nomDepositaire, double montant, Date date, int mode, String reference) {
        this.id = id;
        this.idClient = idClient;
        this.idArticle = idArticle;
        this.nomClient = nomClient;
        this.nomArticle = nomArticle;
        this.nomDepositaire = nomDepositaire;
        this.montant = montant;
        this.date = date;
        this.mode = mode;
        this.reference = reference;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getNomArticle() {
        return nomArticle;
    }

    public void setNomArticle(String nomArticle) {
        this.nomArticle = nomArticle;
    }

    public String getNomDepositaire() {
        return nomDepositaire;
    }

    public void setNomDepositaire(String nomDepositaire) {
        this.nomDepositaire = nomDepositaire;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    @Override
    public String getReferenceTransaction() {
        return this.reference;
    }

    @Override
    public int getMode() {
        return this.mode;
    }

    @Override
    public void setReferenceTransaction(String reference) {
        this.reference = reference;
    }

    @Override
    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "TESTPaiement{" + "id=" + id + ", idClient=" + idClient + ", idArticle=" + idArticle + ", nomClient=" + nomClient + ", nomArticle=" + nomArticle + ", nomDepositaire=" + nomDepositaire + ", montant=" + montant + ", date=" + date + '}';
    }
}
