/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import java.util.Date;
import SOURCES.Interface.InterfacePaiement;

/**
 *
 * @author HP Pavilion
 */
public class TEST_Paiement implements InterfacePaiement{
    public int id;
    public int idEleve;
    public int idArticle;
    public int mode;
    public String reference;
    public String nomEleve;
    public String nomArticle;
    public String nomDepositaire;
    public double montant;
    public Date date;
    public int beta;

    public TEST_Paiement() {
    }

    public TEST_Paiement(int id, int idEleve, int idArticle, String nomEleve, String nomArticle, String nomDepositaire, double montant, Date date, int mode, String reference, int beta) {
        this.id = id;
        this.idEleve = idEleve;
        this.idArticle = idArticle;
        this.nomEleve = nomEleve;
        this.nomArticle = nomArticle;
        this.nomDepositaire = nomDepositaire;
        this.montant = montant;
        this.date = date;
        this.mode = mode;
        this.reference = reference;
        this.beta = beta;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEleve() {
        return idEleve;
    }

    public void setIdEleve(int idEleve) {
        this.idEleve = idEleve;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNomEleve() {
        return nomEleve;
    }

    public void setNomEleve(String nomEleve) {
        this.nomEleve = nomEleve;
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
        return "TEST_Paiement{" + "id=" + id + ", idEleve=" + idEleve + ", idArticle=" + idArticle + ", mode=" + mode + ", reference=" + reference + ", nomEleve=" + nomEleve + ", nomArticle=" + nomArticle + ", nomDepositaire=" + nomDepositaire + ", montant=" + montant + ", date=" + date + ", beta=" + beta + '}';
    }
}
