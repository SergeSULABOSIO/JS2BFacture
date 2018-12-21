/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Interface;

/**
 *
 * @author HP Pavilion
 */
public interface ClientFacture {
    
    public abstract int getId();
    public abstract String getNom();
    public abstract String getTelephone();
    public abstract String getAdresse();
    public abstract String getAutresInfos();
    
    public abstract void setId(int id);
    public abstract void setNom(String nom);
    public abstract void setTelephone(String telephone);
    public abstract void setAdresse(String adresse);
    public abstract void setAutresInfos(String autresinfos);
    
    
}
