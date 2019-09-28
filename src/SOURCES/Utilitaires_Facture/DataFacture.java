/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Facture;

/**
 *
 * @author user
 */
public class DataFacture {
    public DonneesFacture donneesFacture;
    public ParametresFacture parametresFacture;

    public DataFacture(DonneesFacture donneesFacture, ParametresFacture parametresFacture) {
        this.donneesFacture = donneesFacture;
        this.parametresFacture = parametresFacture;
    }

    public DonneesFacture getDonneesFacture() {
        return donneesFacture;
    }

    public void setDonneesFacture(DonneesFacture donneesFacture) {
        this.donneesFacture = donneesFacture;
    }

    public ParametresFacture getParametresFacture() {
        return parametresFacture;
    }

    public void setParametresFacture(ParametresFacture parametresFacture) {
        this.parametresFacture = parametresFacture;
    }

    @Override
    public String toString() {
        return "DataFacture{" + "donneesFacture=" + donneesFacture + ", parametresFacture=" + parametresFacture + '}';
    }
}
















