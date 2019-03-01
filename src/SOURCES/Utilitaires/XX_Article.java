/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceArticle;

/**
 *
 * @author HP Pavilion
 */
public class XX_Article implements InterfaceArticle {

    public int id;
    public String nom;
    public double qte;
    public String unite;
    public int idMonnaie;
    public double tvaprc;
    public double prixUht;
    public double rabais;
    public int tranches;
    public int beta;

    public XX_Article(int id, String nom, double qte, String unite, int idMonnaie, double tvaprc, double prixUht, double rabais, int tranches, int beta) {
        this.id = id;
        this.nom = nom;
        this.qte = qte;
        this.unite = unite;
        this.idMonnaie = idMonnaie;
        this.tvaprc = tvaprc;
        this.prixUht = prixUht;
        this.rabais = rabais;
        this.tranches = tranches;
        this.beta = beta;
    }

    public int getIdMonnaie() {
        return idMonnaie;
    }

    public void setIdMonnaie(int dMonnaie) {
        this.idMonnaie = dMonnaie;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
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
        mnt = (mnt * getTvaPoucentage()) / 100;
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

    public int getTranches() {
        return tranches;
    }

    public void setTranches(int tranches) {
        this.tranches = tranches;
    }

    @Override
    public String toString() {
        return "TEST_Article{" + "id=" + id + ", nom=" + nom + ", qte=" + qte + ", unite=" + unite + ", idMonnaie=" + idMonnaie + ", tvaprc=" + tvaprc + ", prixUht=" + prixUht + ", rabais=" + rabais + ", tranches=" + tranches + ", beta=" + beta + '}';
    }

}
