/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import java.util.Date;
import SOURCES.Interface.InterfaceEcheance;

/**
 *
 * @author HP Pavilion
 */
public class XX_Echeance implements InterfaceEcheance{
    
    public int id;
    public String nom;
    public int idFacture;
    public Date dateInitiale;
    public Date dateFinale;
    public String numeroFacture;
    public double montantPaye;
    public double montantDu;
    public int idMonnaie;

    public XX_Echeance(int id, String nom, int idFacture, Date dateInitiale, Date dateFinale, String numeroFacture, double montantPaye, double montantDu, int idMonnaie) {
        this.id = id;
        this.nom = nom;
        this.idFacture = idFacture;
        this.dateInitiale = dateInitiale;
        this.dateFinale = dateFinale;
        this.numeroFacture = numeroFacture;
        this.montantPaye = montantPaye;
        this.montantDu = montantDu;
        this.idMonnaie = idMonnaie;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public Date getDateInitiale() {
        return dateInitiale;
    }

    public void setDateInitiale(Date dateInitiale) {
        this.dateInitiale = dateInitiale;
    }

    public Date getDateFinale() {
        return dateFinale;
    }

    public void setDateFinale(Date dateFinale) {
        this.dateFinale = dateFinale;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public double getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(double montantPaye) {
        this.montantPaye = montantPaye;
    }

    public double getMontantDu() {
        return montantDu;
    }

    public void setMontantDu(double montantDu) {
        this.montantDu = montantDu;
    }

    public int getIdMonnaie() {
        return idMonnaie;
    }

    public void setIdMonnaie(int idMonnaie) {
        this.idMonnaie = idMonnaie;
    }

    @Override
    public String toString() {
        return "XX_Echeance{" + "id=" + id + ", nom=" + nom + ", idFacture=" + idFacture + ", dateInitiale=" + dateInitiale + ", dateFinale=" + dateFinale + ", numeroFacture=" + numeroFacture + ", montantPaye=" + montantPaye + ", montantDu=" + montantDu + ", idMonnaie=" + idMonnaie + '}';
    }
}
