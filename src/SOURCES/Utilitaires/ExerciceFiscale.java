/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import java.util.Date;

/**
 *
 * @author user
 */
public class ExerciceFiscale {
    public Date debut;
    public Date fin;
    public String nom;

    public ExerciceFiscale(Date debut, Date fin, String nom) {
        this.debut = debut;
        this.fin = fin;
        this.nom = nom;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "ExerciceFiscale{" + "debut=" + debut + ", fin=" + fin + ", nom=" + nom + '}';
    }
}
