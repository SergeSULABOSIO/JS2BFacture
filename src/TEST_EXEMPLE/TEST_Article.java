/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import SOURCES.Utilitaires.*;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfacePeriode;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class TEST_Article implements InterfaceArticle {

    public int id;
    public String nom;
    public double qte;
    public String unite;
    public int idMonnaie;
    public double tvaprc;
    public double prixUht;
    public double rabais;
    public Vector<LiaisonPeriodeFrais> liaisonsPeriodes = new Vector<LiaisonPeriodeFrais>();
    public int beta;

    public TEST_Article(int id, String nom, double qte, String unite, int idMonnaie, double tvaprc, double prixUht, double rabais, Vector<LiaisonPeriodeFrais> liaisonsPeriodes, int beta) {
        this.id = id;
        this.nom = nom;
        this.qte = qte;
        this.unite = unite;
        this.idMonnaie = idMonnaie;
        this.tvaprc = tvaprc;
        this.prixUht = prixUht;
        this.rabais = rabais;
        this.liaisonsPeriodes = liaisonsPeriodes;
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

    public double getTvaprc() {
        return tvaprc;
    }

    public void setTvaprc(double tvaprc) {
        this.tvaprc = tvaprc;
    }

    public double getPrixUht() {
        return prixUht;
    }

    public void setPrixUht(double prixUht) {
        this.prixUht = prixUht;
    }


    public Vector<LiaisonPeriodeFrais> getLiaisonsPeriodes() {
        return liaisonsPeriodes;
    }

    public void setLiaisonsPeriodes(Vector<LiaisonPeriodeFrais> liaisonsPeriodes) {
        this.liaisonsPeriodes = liaisonsPeriodes;
    }


    @Override
    public LiaisonPeriodeFrais getLiaisonPeriodes(InterfacePeriode periode) {
        for (LiaisonPeriodeFrais liaisonPeriodeFrais : liaisonsPeriodes) {
            if (liaisonPeriodeFrais.getIdPeriode() == periode.getId() && liaisonPeriodeFrais.getNomPeriode().equals(periode.getNom())) {
                return liaisonPeriodeFrais;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "TEST_Article{" + "id=" + id + ", nom=" + nom + ", qte=" + qte + ", unite=" + unite + ", idMonnaie=" + idMonnaie + ", tvaprc=" + tvaprc + ", prixUht=" + prixUht + ", rabais=" + rabais + ", liaisonsPeriodes=" + liaisonsPeriodes + ", beta=" + beta + '}';
    }
}
