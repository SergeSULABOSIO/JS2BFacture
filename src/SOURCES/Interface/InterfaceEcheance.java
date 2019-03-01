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
public interface InterfaceEcheance {
    
    //les getters
    public abstract int getId();
    public abstract String getNom();
    public abstract int getIdFacture();
    public abstract Date getDateInitiale();
    public abstract Date getDateFinale();
    public abstract String getNumeroFacture();
    public abstract double getMontantPaye();
    public abstract double getMontantDu();
    public abstract int getIdMonnaie();
    
    //les setters
    public abstract void setId(int id);
    public abstract void setNom(String nom);
    public abstract void setDateInitiale(Date date);
    public abstract void setDateFinale(Date date);
    public abstract void setNumeroFacture(String numeroFacture);
    public abstract void setMontantPaye(double montant);
    public abstract void setMontantDu(double montant);
    public abstract void setIdFacture(int idFacture);
    public abstract void setIdMonnaie(int idMonnaie);
}
