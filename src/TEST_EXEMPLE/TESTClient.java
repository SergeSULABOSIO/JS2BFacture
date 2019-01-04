/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import SOURCES.Interface.InterfaceClient;

/**
 *
 * @author HP Pavilion
 */
public class TESTClient implements InterfaceClient{
    
    private int id;
    private String type;
    private String nom;
    private String telephone;
    private String adresse;
    private String autresInfos;

    public TESTClient(int id, String type, String nom, String telephone, String adresse, String autresInfos) {
        this.id = id;
        this.type = type;
        this.nom = nom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.autresInfos = autresInfos;
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

    public String getAutresInfos() {
        return autresInfos;
    }

    public void setAutresInfos(String autresInfos) {
        this.autresInfos = autresInfos;
    }
    

    @Override
    public String toString() {
        return "TESTClient{" + "id=" + id + ", nom=" + nom + ", telephone=" + telephone + ", adresse=" + adresse + '}';
    }

    public String getType() {
        return type;
    }


    @Override
    public void setType(String type) {
        this.type = type;
    }
}
