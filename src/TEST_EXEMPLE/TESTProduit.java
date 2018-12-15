/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import SOURCES.Interface.ArticleFacture;
import SOURCES.Utilitaires.Util;

/**
 *
 * @author HP Pavilion
 */
public class TESTProduit implements ArticleFacture{
    
    public int id;
    public String nom;
    public double qte;
    public String unite;
    public double tvaprc;
    public double prixUht;
    public double rabais;

    public TESTProduit(int id, String nom, double qte, String unite, double tvaprc, double prixUht, double rabais) {
        this.id = id;
        this.nom = nom;
        this.qte = qte;
        this.unite = unite;
        this.tvaprc = tvaprc;
        this.prixUht = prixUht;
        this.rabais = rabais;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getNom() {
        return this.nom;
    }

    @Override
    public double getQte() {
        return this.qte;
    }

    @Override
    public String getUnite() {
        return this.unite;
    }

    @Override
    public double getPrixUHT_avant_rabais() {
        return this.prixUht;
    }

    @Override
    public double getRabais() {
        return this.rabais;
    }

    @Override
    public double getPrixUHT_apres_rabais() {
        return Util.round((this.prixUht - this.rabais), 2);
    }

    @Override
    public double getTvaPoucentage() {
        return this.tvaprc;
    }

    @Override
    public double getTvaMontant() {
        double mnt = getPrixUHT_apres_rabais() * getQte();
        mnt = (mnt * getTvaPoucentage())/100;
        return Util.round(mnt, 2);
    }

    @Override
    public double getTotalTTC() {
        double mnt = getPrixUHT_apres_rabais() * getQte();
        mnt = mnt + getTvaMontant();
        return Util.round(mnt, 2);
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public void setQte(double Qt) {
        this.qte = Qt;
    }

    @Override
    public void setUnite(String unite) {
        this.unite = unite;
    }

    @Override
    public void setPrixUHT_avant_rabais(double prixUht) {
        this.prixUht = prixUht;
    }

    @Override
    public void setRabais(double rabais) {
        this.rabais = rabais;
    }

    @Override
    public void setTvaPoucentage(double tvapourc) {
        this.tvaprc = tvapourc;
    }

    @Override
    public String toString() {
        return "TESTProduit{" + "id=" + id + ", nom=" + nom + ", qte=" + qte + ", unite=" + unite + ", tvaprc=" + tvaprc + ", prixUht=" + prixUht + ", rabais=" + rabais + '}';
    }

    
}