/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Interface;

import java.util.Date;

/**
 *
 * @author HP Pavilion
 */
public interface InterfacePaiement {
    //Conatantes - BETA
    public static final int BETA_EXISTANT = 0;
    public static final int BETA_MODIFIE = 1;
    public static final int BETA_NOUVEAU = 2;
    
    public static int MODE_CAISSE = 0;
    public static int MODE_BANQUE = 1;
    
    public abstract int getId();
    public abstract int getIdEleve();
    public abstract int getIdArticle();
    public abstract String getNomEleve();
    public abstract String getNomArticle();
    public abstract double getMontant();
    public abstract Date getDate();
    public abstract String getReferenceTransaction();
    public abstract int getMode();  // 0=caisse & 1=banque
    public abstract int getBeta();  // 0 = Existant, 1 =  Modifié, 2 = Nouveau
    
    
    public abstract void setId(int id);
    public abstract void setIdEleve(int idEleve);
    public abstract void setIdArticle(int idArticle);
    public abstract void setNomEleve(String nomEleve);
    public abstract void setNomArticle(String nomArticle);
    public abstract void setMontant(double montant);
    public abstract void setDate(Date date);
    public abstract void setReferenceTransaction(String reference);
    public abstract void setMode(int mode); // 0=caisse & 1=banque
    public abstract void setBeta(int newbeta);   
    
    
}
