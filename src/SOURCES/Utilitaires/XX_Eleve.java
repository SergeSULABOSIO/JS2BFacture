/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceEleve;
import java.util.Date;

/**
 *
 * @author HP Pavilion
 */
public class XX_Eleve implements InterfaceEleve{
    
    public int id;
    public int idEntreprise;
    public int idUtilisateur;
    public int idExercice;
    public int idClasse;
    public long signature;
    public String classe;
    public String adresse;
    public String telephonesParents;
    public String nom;
    public String postnom;
    public String prenom;
    public int status;
    public int sexe;
    public Date dateNaissance;
    public int beta;

    public XX_Eleve(int id, int idEntreprise, int idUtilisateur, int idExercice, int idClasse, long signature, String classe, String adresse, String telephonesParents, String nom, String postnom, String prenom, int status, int sexe, Date dateNaissance, int beta) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.idUtilisateur = idUtilisateur;
        this.idExercice = idExercice;
        this.idClasse = idClasse;
        this.signature = signature;
        this.classe = classe;
        this.adresse = adresse;
        this.telephonesParents = telephonesParents;
        this.nom = nom;
        this.postnom = postnom;
        this.prenom = prenom;
        this.status = status;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
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

    public int getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(int idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(int idExercice) {
        this.idExercice = idExercice;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    public long getSignature() {
        return signature;
    }

    public void setSignature(long signature) {
        this.signature = signature;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephonesParents() {
        return telephonesParents;
    }

    public void setTelephonesParents(String telephonesParents) {
        this.telephonesParents = telephonesParents;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPostnom() {
        return postnom;
    }

    public void setPostnom(String postnom) {
        this.postnom = postnom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    

    public int getSexe() {
        return sexe;
    }

    public void setSexe(int sexe) {
        this.sexe = sexe;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "XX_Eleve{" + "id=" + id + ", idEntreprise=" + idEntreprise + ", idUtilisateur=" + idUtilisateur + ", idExercice=" + idExercice + ", idClasse=" + idClasse + ", signature=" + signature + ", classe=" + classe + ", adresse=" + adresse + ", telephonesParents=" + telephonesParents + ", nom=" + nom + ", postnom=" + postnom + ", prenom=" + prenom + ", status=" + status + ", sexe=" + sexe + ", dateNaissance=" + dateNaissance + ", beta=" + beta + '}';
    }

    
    
}
