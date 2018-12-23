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
    public String dateInitiale;
    public String dateFinale;
    public String numeroFacture;
    public double montantPaye;
    public double montantDu;
    public int idMonnaie;
    public String monnaie;

    public TESTEcheance(int id, String nom, int idFacture, String dateInitiale, String dateFinale, String numeroFacture, double montantPaye, double montantDu, int idMonnaie, String monnaie) {
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

    public String getDateInitiale() {
        return dateInitiale;
    }

    public String getDateFinale() {
        return dateFinale;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public int getNbJoursRestant() {
        Date finale = new Date(getDateFinale());
        Date today = new Date();
        return today.compareTo(finale);
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

    public void setDateInitiale(String dateInitiale) {
        this.dateInitiale = dateInitiale;
    }

    public void setDateFinale(String dateFinale) {
        this.dateFinale = dateFinale;
    }
    
    @Override
    public JProgressBar getEtatProgression() {
        JProgressBar prog = new JProgressBar();
        prog.setStringPainted(true);
        prog.setMinimum((int)getMontantDu());
        prog.setString(getMontantPaye()+ " " + getMonnaie());
        return new JProgressBar();
    }

    @Override
    public void setDateInitiale(Date date) {
        this.dateFinale = date.toLocaleString();
    }

    @Override
    public void setDateFinale(Date date) {
        this.dateFinale = date.toLocaleString();
    }

    @Override
    public String toString() {
        return "TESTEcheance{" + "id=" + id + ", nom=" + nom + ", idFacture=" + idFacture + ", dateInitiale=" + dateInitiale + ", dateFinale=" + dateFinale + ", numeroFacture=" + numeroFacture + ", montantPaye=" + montantPaye + ", montantDu=" + montantDu + ", idMonnaie=" + idMonnaie + ", monnaie=" + monnaie + '}';
    }

    

    public static void main(String[] a){
        TESTEcheance echean = new TESTEcheance(-1, "PREMIERE TRANCHE", 12, "30-06-2016 23:12:00", "31-12-2018 23:12:00", "0012236547", 10, 100, 1, "USD");
        System.out.println("nb Jour : " + echean.getNbJoursRestant());
    }
    
}
