/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Interface;

import SOURCES.Utilitaires.LiaisonPeriodeFrais;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public interface InterfaceArticle {
    
    //Conatantes - BETA
    public static final int BETA_EXISTANT = 0;
    public static final int BETA_MODIFIE = 1;
    public static final int BETA_NOUVEAU = 2;
    
    
    public abstract int getId();
    public abstract String getNom();
    public abstract double getQte();
    public abstract String getUnite();
    public abstract int getIdMonnaie();
    public abstract double getPrixUHT_avant_rabais();
    public abstract double getRabais();
    public abstract double getPrixUHT_apres_rabais();
    public abstract double getTvaPoucentage();
    public abstract double getTvaMontant();
    public abstract double getTotalTTC();
    public abstract Vector<LiaisonPeriodeFrais> getLiaisonsPeriodes();
    public abstract LiaisonPeriodeFrais getLiaisonPeriodes(InterfacePeriode periode);
    public abstract int getBeta();  // 0 = Existant, 1 =  Modifié, 2 = Nouveau
    
    public abstract void setId(int id);
    public abstract void setNom(String nom);
    public abstract void setQte(double Qt);
    public abstract void setUnite(String unite);
    public abstract void setIdMonnaie(int idMonnaie);
    public abstract void setPrixUHT_avant_rabais(double prixUht);
    public abstract void setRabais(double rabais);
    public abstract void setTvaPoucentage(double tvapourc);
    public abstract void setBeta(int newbeta);
}
