/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.Interface.InterfacePeriode;
import SOURCES.ModelsTable.ModeleListePaiement;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class GestionLitiges {

    public static Vector<InterfaceEcheance> getEcheances(Vector<InterfaceArticle> listeArticles, ModeleListePaiement modeleListePaiement, ParametresFacture parametresFacture) {
        Vector<InterfaceEcheance> listeEcheances = new Vector<>();
        for (InterfacePeriode Iperiode : parametresFacture.getListePeriodes()) {
            
            //Recherche des montants dûs
            double montantDu = 0;
            for (InterfaceArticle Iarticle : listeArticles) {
                for (LiaisonPeriodeFrais liaison : Iarticle.getLiaisonsPeriodes()) {
                    if (liaison.getIdPeriode() == Iperiode.getId() && liaison.getNomPeriode().equals(Iperiode.getNom())) {
                        //Il faut appliquer la conversion selon la monnaie Output définie
                        double montDu = Util.round((Iarticle.getTotalTTC() * liaison.getPourcentage())/100, 2);
                        montantDu +=  Util.getMontantOutPut(parametresFacture, Iarticle.getIdMonnaie(), montDu);
                    }
                }
            }
            
            //Recherche des montants payes
            double montantPaye = 0;
            for(InterfacePaiement Ipaiement: modeleListePaiement.getListeData()){
                if(Ipaiement.getIdPeriode() == Iperiode.getId()){
                    //Il faut appliquer la conversion selon la monnaie Output définie
                    InterfaceArticle Iart = Util.getArticle(listeArticles, Ipaiement.getIdArticle());
                    montantPaye += Util.getMontantOutPut(parametresFacture, Iart.getIdMonnaie(), Ipaiement.getMontant()); 
                }
            }
            listeEcheances.add(new XX_Echeance(-1, Iperiode.getNom(), -1, Iperiode.getDebut(), Iperiode.getFin(), "", montantPaye, montantDu, parametresFacture.getMonnaieOutPut().getId()));
        }
        return listeEcheances;
    }

    private static InterfaceArticle getArticle(Vector<InterfaceArticle> listeArticle, int id) {
        for (InterfaceArticle Iart : listeArticle) {
            if (id == Iart.getId()) {
                return Iart;
            }
        }
        return null;
    }
}
