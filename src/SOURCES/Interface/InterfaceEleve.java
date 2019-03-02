/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Interface;

import java.util.Date;

/**
 *
 * @author user
 */
public interface InterfaceEleve {
    //Constantes - SEXE
    public static final int SEXE_MASCULIN = 0;
    public static final int SEXE_FEMININ = 1;
    //Constantes - STATUS
    public static final int STATUS_ACTIF = 0;
    public static final int STATUS_INACTIF = 1;
    //Conatantes - BETA
    public static final int BETA_EXISTANT = 0;
    public static final int BETA_MODIFIE = 1;
    public static final int BETA_NOUVEAU = 2;
    //les getters
    public abstract int getId();
    public abstract int getIdEntreprise();
    public abstract int getIdUtilisateur();
    public abstract int getIdExercice();
    public abstract int getIdClasse();
    public abstract String getAdresse();
    public abstract long getSignature();
    public abstract String getClasse();
    public abstract String getTelephonesParents();
    public abstract String getNom();
    public abstract String getPostnom();
    public abstract String getPrenom();
    public abstract int getSexe();
    public abstract int getStatus();
    public abstract Date getDateNaissance();
    public abstract int getBeta();  // 0 = Existant, 1 =  Modifié, 2 = Nouveau
    
    
    //Les setters
    public abstract void setId(int id);
    public abstract void setIdEntreprise(int idEntreprise);
    public abstract void setIdUtilisateur(int idUtilisateur);
    public abstract void setIdExercice(int idExercice);
    public abstract void setIdClasse(int idClasse);
    public abstract void setSignature(long signature);
    public abstract void setClasse(String classe);
    public abstract void setAdresse(String adresse);
    public abstract void setTelephonesParents(String telephonesParents);
    public abstract void setNom(String nom);
    public abstract void setPostnom(String postnom);
    public abstract void setPrenom(String prenom);
    public abstract void setSexe(int sexe);
    public abstract void setDateNaissance(Date dateNaissance);
    public abstract void setStatus(int status);
    public abstract void setBeta(int newbeta);
}
