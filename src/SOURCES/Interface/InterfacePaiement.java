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
    
    public abstract int getId();
    public abstract int getIdClient();
    public abstract int getIdArticle();
    public abstract String getNomClient();
    public abstract String getNomArticle();
    public abstract double getMontant();
    public abstract Date getDate();
    public abstract String getNomDepositaire();
    
    
    public abstract void setId(int id);
    public abstract void setIdClient(int idClient);
    public abstract void setIdArticle(int idArticle);
    public abstract void setNomClient(String nomClient);
    public abstract void setNomArticle(String nomArticle);
    public abstract void setMontant(double montant);
    public abstract void setDate(Date date);
    public abstract void setNomDepositaire(String NomDepositaire);
    
    
    
}
