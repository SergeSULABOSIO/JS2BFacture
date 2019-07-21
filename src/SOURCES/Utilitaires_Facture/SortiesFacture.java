/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Facture;

import Source.Callbacks.EcouteurEnregistrement;
import Source.Objet.Paiement;
import java.util.Vector;

/**
 *
 * @author user
 */
public class SortiesFacture {
    private Vector<Paiement> paiements;
    private EcouteurEnregistrement ecouteurEnregistrement;

    public SortiesFacture(Vector<Paiement> paiements, EcouteurEnregistrement ecouteurEnregistrement) {
        this.paiements = paiements;
        this.ecouteurEnregistrement = ecouteurEnregistrement;
    }

    

    public Vector<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(Vector<Paiement> paiements) {
        this.paiements = paiements;
    }

    public EcouteurEnregistrement getEcouteurEnregistrement() {
        return ecouteurEnregistrement;
    }

    public void setEcouteurEnregistrement(EcouteurEnregistrement ecouteurEnregistrement) {
        this.ecouteurEnregistrement = ecouteurEnregistrement;
    }
    
    
}
