/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.CallBack.EcouteurEnregistrement;
import SOURCES.Interface.InterfacePaiement;
import java.util.Vector;

/**
 *
 * @author user
 */
public class SortiesFacture {
    private Vector<InterfacePaiement> paiements;
    private EcouteurEnregistrement ecouteurEnregistrement;

    public SortiesFacture(Vector<InterfacePaiement> paiements, EcouteurEnregistrement ecouteurEnregistrement) {
        this.paiements = paiements;
        this.ecouteurEnregistrement = ecouteurEnregistrement;
    }

    

    public Vector<InterfacePaiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(Vector<InterfacePaiement> paiements) {
        this.paiements = paiements;
    }

    public EcouteurEnregistrement getEcouteurEnregistrement() {
        return ecouteurEnregistrement;
    }

    public void setEcouteurEnregistrement(EcouteurEnregistrement ecouteurEnregistrement) {
        this.ecouteurEnregistrement = ecouteurEnregistrement;
    }
    
    
}
