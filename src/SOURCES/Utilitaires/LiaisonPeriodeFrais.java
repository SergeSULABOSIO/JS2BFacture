/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

/**
 *
 * @author HP Pavilion
 */
public class LiaisonPeriodeFrais {
    private int idPeriode;
    private String nomPeriode;
    private double montant;

    public LiaisonPeriodeFrais(int idPeriode, String nomPeriode, double montant) {
        this.idPeriode = idPeriode;
        this.nomPeriode = nomPeriode;
        this.montant = montant;
    }

    public String getNomPeriode() {
        return nomPeriode;
    }

    public void setNomPeriode(String nomPeriode) {
        this.nomPeriode = nomPeriode;
    }
    
    public int getIdPeriode() {
        return idPeriode;
    }

    public void setIdPeriode(int idPeriode) {
        this.idPeriode = idPeriode;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "LiaisonPeriodeFrais{" + "idPeriode=" + idPeriode + ", montant=" + montant + '}';
    }
}
