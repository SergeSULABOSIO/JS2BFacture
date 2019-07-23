/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Facture;


import Source.Objet.Classe;
import Source.Objet.Entreprise;
import Source.Objet.Exercice;
import Source.Objet.Monnaie;
import Source.Objet.Periode;
import Source.Objet.Utilisateur;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class ParametresFacture {
    private Entreprise entreprise;
    private Utilisateur utilisateur;
    private Exercice exercice;
    private Monnaie monnaieOutPut;
    private Vector<Monnaie> listeMonnaies;
    private Vector<Classe> listeClasse;
    private Vector<Periode> listePeriodes;

    public ParametresFacture(Utilisateur utilisateur, Entreprise entreprise, Exercice exercice, Monnaie monnaieOutPut, Vector<Monnaie> listeMonnaies, Vector<Classe> listeClasse, Vector<Periode> listePeriodes) {
        this.utilisateur = utilisateur;
        this.entreprise = entreprise;
        this.exercice = exercice;
        this.monnaieOutPut = monnaieOutPut;
        this.listeMonnaies = listeMonnaies;
        this.listeClasse = listeClasse;
        this.listePeriodes = listePeriodes;
    }
    
    public String getMonnaieS(int idMonnaie){
        for(Monnaie m: listeMonnaies){
            if(m.getId() == idMonnaie){
                return m.getCode();
            }
        }
        return "";
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public Monnaie getMonnaieOutPut() {
        return monnaieOutPut;
    }

    public void setMonnaieOutPut(Monnaie monnaieOutPut) {
        this.monnaieOutPut = monnaieOutPut;
    }

    public Vector<Monnaie> getListeMonnaies() {
        return listeMonnaies;
    }

    public void setListeMonnaies(Vector<Monnaie> listeMonnaies) {
        this.listeMonnaies = listeMonnaies;
    }

    public Vector<Classe> getListeClasse() {
        return listeClasse;
    }

    public void setListeClasse(Vector<Classe> listeClasse) {
        this.listeClasse = listeClasse;
    }

    public Vector<Periode> getListePeriodes() {
        return listePeriodes;
    }

    public void setListePeriodes(Vector<Periode> listePeriodes) {
        this.listePeriodes = listePeriodes;
    }

    @Override
    public String toString() {
        return "ParametresFacture{" + "entreprise=" + entreprise + ", utilisateur=" + utilisateur + ", exercice=" + exercice + ", monnaieOutPut=" + monnaieOutPut + ", listeMonnaies=" + listeMonnaies + ", listeClasse=" + listeClasse + ", listePeriodes=" + listePeriodes + '}';
    }
}
