/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceExercice;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.ModelsTable.ModeleListePaiement;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class GestionLitiges {

    public static Vector<InterfaceEcheance> getEcheances(Vector<InterfaceArticle> listeArticles, ModeleListePaiement modeleListePaiement, ParametresFacture parametresFacture) {
        Vector<InterfaceEcheance> listeEcheances = new Vector<>();
        int nombreMax = getNbTranchesMax(listeArticles);
        double daysExercice = Util.getNombre_jours(parametresFacture.getExercice().getFin(), parametresFacture.getExercice().getDebut());
        long nbDaysParTrancheLong = (long) ((daysExercice / nombreMax) * 1000 * 60 * 60 * 24);
        long cumulDays = 0;
        for (int i = 0; i < nombreMax; i++) {
            String nomTranche = "1ère Tranche";
            if ((i + 1) > 1) {
                nomTranche = (i + 1) + "ème Tranche";
            }
            Date debutTranche = new Date(parametresFacture.getExercice().getDebut().getTime() + cumulDays);
            Date finTranche = new Date(debutTranche.getTime() + nbDaysParTrancheLong);

            listeEcheances.add(new XX_Echeance(-1, nomTranche, -1, debutTranche, finTranche, "", 0, 0, parametresFacture.getMonnaieOutPut().getId()));
            cumulDays = cumulDays + nbDaysParTrancheLong;
        }
        
        GestionLitiges.setMontantDu(listeArticles, listeEcheances, parametresFacture);
        GestionLitiges.setMontantPaye(listeArticles, listeEcheances, parametresFacture, modeleListePaiement);
        
        return listeEcheances;
    }

    public static int getNbTranchesMax(Vector<InterfaceArticle> listeArticles) {
        int nombreTranches = 0;
        if (listeArticles != null) {
            if (!listeArticles.isEmpty()) {
                for (InterfaceArticle article : listeArticles) {
                    //System.out.println(" * " + article.toString() + ", tranches = " + article.getTranches());
                    //Si et seulement si le rabais reste strictement inférieur au prix unitaire avant remise
                    if (article.getPrixUHT_avant_rabais() > article.getRabais()) {
                        if (article.getTranches() > nombreTranches) {
                            nombreTranches = article.getTranches();
                        }
                    }
                }
            }
        }
        return nombreTranches;
    }
    
    private static void setMontantDu(Vector<InterfaceArticle> listeArticle, Vector<InterfaceEcheance> listeEcheance, ParametresFacture parametresFacture) {
        double nombreTranches = getNbTranchesMax(listeArticle);
        if (!listeEcheance.isEmpty()) {
            for (int indexTranche = 0; indexTranche < nombreTranches; indexTranche++) {
                InterfaceEcheance echeEncours = listeEcheance.elementAt(indexTranche);
                double mont = 0;
                for (InterfaceArticle Iart : listeArticle) {
                    if (indexTranche + 1 <= Iart.getTranches()) {
                        //System.out.println("C");
                        mont += Util.getMontantOutPut(parametresFacture, Iart.getIdMonnaie(), Iart.getTotalTTC()) / Iart.getTranches();
                    }
                }
                echeEncours.setMontantDu(mont);
            }
        }
    }
    
    private static void setMontantPaye(Vector<InterfaceArticle> listeArticle, Vector<InterfaceEcheance> listeEcheance, ParametresFacture parametresFacture, ModeleListePaiement modeleListePaiement) {
        double nombreTranches = getNbTranchesMax(listeArticle);
        //On calcul les montants déjà payés pour cette tranche
        if (!listeEcheance.isEmpty()) {
            for (int i = 0; i < nombreTranches; i++) {
                InterfaceEcheance echeEncours = listeEcheance.elementAt(i);
                double montPaye = 0;
                for (InterfacePaiement paiement : modeleListePaiement.getListeData()) {
                    if (paiement.getDate().compareTo(echeEncours.getDateInitiale()) > 0 && paiement.getDate().compareTo(echeEncours.getDateFinale()) <= 0) {
                        InterfaceArticle Iart = getArticle(listeArticle, paiement.getIdArticle());
                        if (Iart != null) {
                            montPaye = montPaye + Util.getMontantOutPut(parametresFacture, Iart.getIdMonnaie(), paiement.getMontant());
                        }
                    }
                }
                echeEncours.setMontantPaye(montPaye);
            }
        }
    }
    
    private static InterfaceArticle getArticle(Vector<InterfaceArticle> listeArticle, int id){
        for(InterfaceArticle Iart: listeArticle){
            if(id == Iart.getId()){
                return Iart;
            }
        }
        return null;
    }
}
