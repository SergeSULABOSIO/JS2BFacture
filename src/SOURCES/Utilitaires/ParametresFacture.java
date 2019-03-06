/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceClasse;
import SOURCES.Interface.InterfaceEntreprise;
import SOURCES.Interface.InterfaceExercice;
import SOURCES.Interface.InterfaceMonnaie;
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
    private InterfaceEntreprise entreprise;
    private InterfaceExercice exercice;
    private InterfaceMonnaie monnaieOutPut;
    private Vector<InterfaceMonnaie> listeMonnaies;
    private Vector<InterfaceClasse> listeClasse;

    public ParametresFacture(int idFacture, String numero, int idUtilisateur, String nomUtilisateur, InterfaceEntreprise entreprise, InterfaceExercice exercice, InterfaceMonnaie monnaieOutPut, Vector<InterfaceMonnaie> listeMonnaies, Vector<InterfaceClasse> listeClasse) {
        this.idFacture = idFacture;
        this.numero = numero;
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.entreprise = entreprise;
        this.exercice = exercice;
        this.monnaieOutPut = monnaieOutPut;
        this.listeMonnaies = listeMonnaies;
        this.listeClasse = listeClasse;
    }

    public Vector<InterfaceClasse> getListeClasse() {
        return listeClasse;
    }

    public void setListeClasse(Vector<InterfaceClasse> listeClasse) {
        this.listeClasse = listeClasse;
    }
    
    public Vector<InterfaceMonnaie> getListeMonnaies() {
        return listeMonnaies;
    }

    public void setListeMonnaies(Vector<InterfaceMonnaie> listeMonnaies) {
        this.listeMonnaies = listeMonnaies;
    }
    
    public InterfaceExercice getExercice() {
        return exercice;
    }

    public void setExercice(InterfaceExercice exercice) {
        this.exercice = exercice;
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

    
    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }
    
    public void setEntreprise(InterfaceEntreprise entreprise) {
        this.entreprise = entreprise;
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

    public InterfaceMonnaie getMonnaieOutPut() {
        return monnaieOutPut;
    }

    public void setMonnaieOutPut(InterfaceMonnaie monnaieOutPut) {
        this.monnaieOutPut = monnaieOutPut;
    }
}
