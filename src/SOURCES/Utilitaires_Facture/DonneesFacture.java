/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Facture;


import Source.Objet.Ayantdroit;
import Source.Objet.Eleve;
import Source.Objet.Frais;
import Source.Objet.LiaisonFraisEleve;
import Source.Objet.Paiement;
import java.util.Vector;


/**
 *
 * @author HP Pavilion
 */
public class DonneesFacture {
    private Eleve eleve;
    private Vector<Frais> articles;
    private Vector<Paiement> paiements;
    private Vector<Ayantdroit> ayantdroits;
    
    public DonneesFacture(Eleve eleve, Vector<Frais> articles, Vector<Paiement> paiements, Vector<Ayantdroit> ayantdroits) {
        this.eleve = eleve;
        this.articles = articles;
        this.paiements = paiements;
        this.ayantdroits = ayantdroits;
        initAppliquerAyantDroit();
    }
    
    private void initAppliquerAyantDroit(){
        //Ce bout de code est très important pour
        //Il prend en compte le fait que l'élève soit un ayant droit ou pas
        if(ayantdroits != null){
            if(!ayantdroits.isEmpty()){
                for(Ayantdroit ay: ayantdroits){
                    //System.out.println("XXXXX  TEST de ("+ay.getSignatureEleve()+"," + eleve.getSignature()+"):");
                    if(ay.getSignatureEleve() == eleve.getSignature()){
                        //System.out.println("XXXXX  ---- " + eleve.getNom()+" est un Ayantdroit ----- XXXXXX");
                        for(LiaisonFraisEleve lfe: ay.getListeLiaisons()){
                            for(Frais f: articles){
                                //System.out.println("XXXXX  TEST des IDs (" + lfe.getIdFrais() + "," + f.getId() + "):");
                                if(lfe.getIdFrais() == f.getId()){
                                    f.setMontantDefaut(lfe.getMontant());
                                    f.setIdMonnaie(lfe.getIdMonnaie());
                                    f.setMonnaie(lfe.getMonnaie());
                                    //System.out.println("XXXXX  ---- " + f.getNom()+" modifié !");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Vector<Frais> getArticles() {
        return articles;
    }

    public void setArticles(Vector<Frais> articles) {
        this.articles = articles;
    }

    public Vector<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(Vector<Paiement> paiements) {
        this.paiements = paiements;
    }

    @Override
    public String toString() {
        return "DonneesFacture{" + "eleve=" + eleve + ", articles=" + articles + ", paiements=" + paiements + '}';
    }
}


