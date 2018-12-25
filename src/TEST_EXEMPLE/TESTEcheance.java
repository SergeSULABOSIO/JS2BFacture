/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import SOURCES.Interface.EcheanceFacture;
import java.util.Date;
import javax.swing.JProgressBar;

/**
 *
 * @author HP Pavilion
 */
public class TESTEcheance implements EcheanceFacture {

    public int id;
    public String nom;
    public int idFacture;
    public Date dateInitiale;
    public Date dateFinale;
    public String numeroFacture;
    public double montantPaye;
    public double montantDu;
    public int idMonnaie;
    public String monnaie;

    public TESTEcheance(int id, String nom, int idFacture, Date dateInitiale, Date dateFinale, String numeroFacture, double montantPaye, double montantDu, int idMonnaie, String monnaie) {
        this.id = id;
        this.nom = nom;
        this.idFacture = idFacture;
        this.dateInitiale = dateInitiale;
        this.dateFinale = dateFinale;
        this.numeroFacture = numeroFacture;
        this.montantPaye = montantPaye;
        this.montantDu = montantDu;
        this.idMonnaie = idMonnaie;
        this.monnaie = monnaie;
    }
    

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public Date getDateInitiale() {
        return dateInitiale;
    }

    public Date getDateFinale() {
        return dateFinale;
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

    public void setMontantPaye(double montant) {
        this.montantPaye = montant;
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

    public String getMonnaie() {
        return monnaie;
    }

    public void setMonnaie(String monnaie) {
        this.monnaie = monnaie;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public void setDateInitiale(Date dateInitiale) {
        this.dateInitiale = dateInitiale;
    }

    public void setDateFinale(Date dateFinale) {
        this.dateFinale = dateFinale;
    }

    @Override
    public String toString() {
        return "TESTEcheance{" + "id=" + id + ", nom=" + nom + ", idFacture=" + idFacture + ", dateInitiale=" + dateInitiale + ", dateFinale=" + dateFinale + ", numeroFacture=" + numeroFacture + ", montantPaye=" + montantPaye + ", montantDu=" + montantDu + ", idMonnaie=" + idMonnaie + ", monnaie=" + monnaie + '}';
    }
    
    public static void main(String[] a){
        //System.out.println("nb Jour : " + echean.getNbJoursRestant());
    }
}
