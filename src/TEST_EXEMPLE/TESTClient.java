/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import SOURCES.Interface.ClientFacture;

/**
 *
 * @author HP Pavilion
 */
public class TESTClient implements ClientFacture{
    
    private int id;
    private String nom;
    private String telephone;
    private String adresse;

    public TESTClient(int id, String nom, String telephone, String adresse) {
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
        this.adresse = adresse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    

    @Override
    public String toString() {
        return "TESTClient{" + "id=" + id + ", nom=" + nom + ", telephone=" + telephone + ", adresse=" + adresse + '}';
    }
    
    
    
    
}
