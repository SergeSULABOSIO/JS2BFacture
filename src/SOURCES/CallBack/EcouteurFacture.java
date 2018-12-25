/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.CallBack;

import java.util.Vector;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceClient;

/**
 *
 * @author HP Pavilion
 */
public abstract class EcouteurFacture {
    
    public abstract void onEnregistre(InterfaceClient client, Vector<InterfaceArticle> articles, Vector<InterfacePaiement> paiements, Vector<InterfaceEcheance> echeances);
    
}
