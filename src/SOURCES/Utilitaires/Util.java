/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceExercice;
import SOURCES.Interface.InterfaceMonnaie;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author user
 */
public class Util {
    
    private static void param_tranches_init_dates(Vector<InterfaceArticle> listeArticles, Vector<InterfaceEcheance> listeEcheances, InterfaceExercice exercice) {
        int nombreTranches = Util.getNbTranchesMax(listeArticles);
        double daysExercice = Util.getNombre_jours(exercice.getFin(), exercice.getDebut());
        double nbDaysParTranche = daysExercice / nombreTranches;
        long nbDaysParTrancheLong = (long) ((nbDaysParTranche) * 1000 * 60 * 60 * 24);
        long cumulDays = 0;
        if (!listeEcheances.isEmpty()) {
            for (int i = 0; i < nombreTranches; i++) {
                InterfaceEcheance echeEncours = listeEcheances.elementAt(i);
                echeEncours.setDateInitiale(new Date(exercice.getDebut().getTime() + cumulDays));
                echeEncours.setDateFinale(new Date(echeEncours.getDateInitiale().getTime() + nbDaysParTrancheLong));
                cumulDays = cumulDays + nbDaysParTrancheLong;
            }
        }
    }
    
    
    public static void param_tranches_creer(Vector<InterfaceArticle> listeArticles, Vector<InterfaceEcheance> listeEcheances, InterfaceExercice exercice, int idMonnaieOutput) {
        if (listeEcheances != null) {
            listeEcheances.removeAllElements();
            int nombreMax = Util.getNbTranchesMax(listeArticles);
            for (int i = 0; i < nombreMax; i++) {
                String nomTranche = "1ère Tranche";
                if ((i + 1) > 1) {
                    nomTranche = (i + 1) + "ème Tranche";
                }
                //Date debut = exercice.getDebut();
                //Date fin = exercice.getFin();
                XX_Echeance trancheTempo = new XX_Echeance(-1, nomTranche, -1, null, null, "", 0, 0, idMonnaieOutput);
                listeEcheances.add(trancheTempo);
            }
            param_tranches_init_dates(listeArticles, listeEcheances, exercice);
        }
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

    public static InterfaceMonnaie getMonnaie(ParametresFacture parametresFacture, int idMonnaie){
        for(InterfaceMonnaie Imonnaie: parametresFacture.getListeMonnaies()){
            if(Imonnaie.getId() == idMonnaie){
                return Imonnaie;
            }
        }
        return null;
    }
    
    public static double getMontantOutPut(ParametresFacture parametresFacture, int idMonnaieInput, double montant) {
        InterfaceMonnaie monnaieOutPut = getMonnaie(parametresFacture, parametresFacture.getMonnaieOutPut().getId());
        InterfaceMonnaie monnaieInPut = getMonnaie(parametresFacture, idMonnaieInput);
        
        if (monnaieOutPut != null && monnaieInPut != null) {
            if (parametresFacture.getMonnaieOutPut().getId() == idMonnaieInput) {
                return montant;
            } else {
                return (montant * monnaieInPut.getTauxMonnaieLocale() / monnaieOutPut.getTauxMonnaieLocale());
            }
        }else{
            return 0;
        }
    }
    
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String getLettres(double montant, String NomMonnaie) {
        String texte = "";
        try {
            texte = Nombre.CALCULATE.getLettres(montant, NomMonnaie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return texte;
    }
    
    public static Date getDate_AjouterAnnee(Date dateActuelle, int nbAnnee) {
        try {
            int plus = dateActuelle.getYear() + nbAnnee;
            dateActuelle.setYear(plus);
            return dateActuelle;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double getNombre_jours(Date dateFin, Date dateDebut) {
        try {
            double nb = (int) ((dateFin.getTime() - dateDebut.getTime()) / (1000 * 60 * 60 * 24));
            nb = Util.round(nb, 0);
            return nb;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double getNombre_jours_from_today(Date dateFin) {
        try {
            double nb = (double) ((dateFin.getTime() - (new Date()).getTime()) / (1000 * 60 * 60 * 24));
            nb = Util.round(nb, 0);
            return nb;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDateFrancais(Date date) {
        String dateS = "";
        try {
            String pattern = "dd MMM yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            dateS = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateS;
    }
    
    public static String getMontantFrancais(double montant) {
        String val = "";
        int ValEntiere = (int) montant;
        char[] valInput = (ValEntiere + "").toCharArray();
        int index = 0;
        for (int i = valInput.length - 1; i >= 0; i--) {
            //System.out.println(" \t *  " + valInput[i]);
            if (index % 3 == 0 && index != 0) {
                val = valInput[i] + "." + val;
            } else {
                val = valInput[i] + val;
            }
            index++;
        }
        int ValApresVirgule = (int)(round(((montant - ValEntiere)*100), 0));
        //System.out.println("Valeur d'origine = " + montant);
        //System.out.println("Partie entière = " + ValEntiere);
        //System.out.println("Partie décimale = " + ValApresVirgule);
        return val+"," + ValApresVirgule;
    }
    
    public static String getMontantLettres(double montant, String NomMonnaie){
        String texte = "";
        try{
            texte = Nombre.CALCULATE.getLettres(montant, NomMonnaie);
        }catch(Exception e){
            System.out.println("Un problème est survenu lors de la conversion des chiffres en nombre.");
            texte = "";
        }
        return texte;
    }

    public static void main(String[] args) {
        double origine = 10000.14;
        
        String res = Util.getMontantFrancais(origine);
        System.out.println("Résultat = " + res);
        System.out.println("Résultat = " + Util.getMontantLettres(origine, "Dollars Américains"));
    }
}
