/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Facture;

import SOURCES.ModelsTable.ModeleListePaiement;
import Source.Objet.Echeance;
import Source.Objet.Frais;
import Source.Objet.LiaisonFraisPeriode;
import Source.Objet.Paiement;
import Source.Objet.Periode;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class CalculateurLitigesFacture {

    public static Vector<Echeance> getEcheances(Vector<Frais> listeFrais, ModeleListePaiement modeleListePaiement, ParametresFacture parametresFacture) {
        Vector<Echeance> listeEcheances = new Vector<>();
        for (Periode Iperiode : parametresFacture.getListePeriodes()) {

            //Recherche des montants dûs
            double montantDu = 0;
            for (Frais Iarticle : listeFrais) {
                for (LiaisonFraisPeriode liaison : Iarticle.getLiaisonsPeriodes()) {
                    if (liaison.getIdPeriode() == Iperiode.getId() && liaison.getNomPeriode().equals(Iperiode.getNom())) {
                        //Il faut appliquer la conversion selon la monnaie Output définie
                        double montDu = UtilFacture.round((Iarticle.getMontantDefaut() * liaison.getPourcentage()) / 100, 2);
                        montantDu += UtilFacture.getMontantOutPut(parametresFacture, Iarticle.getIdMonnaie(), montDu);
                    }
                }
            }

            //Recherche des montants payes
            double montantPaye = 0;
            for (Paiement Ipaiement : modeleListePaiement.getListeData()) {
                if (Ipaiement != null) {
                    if (Ipaiement.getIdPeriode() == Iperiode.getId()) {
                        //Il faut appliquer la conversion selon la monnaie Output définie
                        Frais Iart = UtilFacture.getFrais(listeFrais, Ipaiement.getIdFrais());
                        if (Iart != null) {
                            montantPaye += UtilFacture.getMontantOutPut(parametresFacture, Iart.getIdMonnaie(), Ipaiement.getMontant());
                        }
                    }
                }

            }
            listeEcheances.add(new Echeance(-1, Iperiode.getNom(), -1, Iperiode.getDebut(), Iperiode.getFin(), "", montantPaye, montantDu, parametresFacture.getMonnaieOutPut().getId()));
        }

        return listeEcheances;
    }

    private static Frais getFrais(Vector<Frais> listeArticle, int id) {
        for (Frais Iart : listeArticle) {
            if (id == Iart.getId()) {
                return Iart;
            }
        }
        return null;
    }
}
