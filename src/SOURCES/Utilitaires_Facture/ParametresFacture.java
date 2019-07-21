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
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class ParametresFacture {
    private int idFacture;
    private String numero;
    private int idUtilisateur;
    private String nomUtilisateur;
    private Entreprise entreprise;
    private Exercice exercice;
    private Monnaie monnaieOutPut;
    private Vector<Monnaie> listeMonnaies;
    private Vector<Classe> listeClasse;
    private Vector<Periode> listePeriodes;

    public ParametresFacture(int idFacture, String numero, int idUtilisateur, String nomUtilisateur, Entreprise entreprise, Exercice exercice, Monnaie monnaieOutPut, Vector<Monnaie> listeMonnaies, Vector<Classe> listeClasse, Vector<Periode> listePeriodes) {
        this.idFacture = idFacture;
        this.numero = numero;
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.entreprise = entreprise;
        this.exercice = exercice;
        this.monnaieOutPut = monnaieOutPut;
        this.listeMonnaies = listeMonnaies;
        this.listeClasse = listeClasse;
        this.listePeriodes = listePeriodes;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
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
        return "ParametresFacture{" + "idFacture=" + idFacture + ", numero=" + numero + ", idUtilisateur=" + idUtilisateur + ", nomUtilisateur=" + nomUtilisateur + ", entreprise=" + entreprise + ", exercice=" + exercice + ", monnaieOutPut=" + monnaieOutPut + ", listeMonnaies=" + listeMonnaies + ", listeClasse=" + listeClasse + ", listePeriodes=" + listePeriodes + '}';
    }
}
