/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.CallBack;

import SOURCES.Interface.ArticleFacture;
import SOURCES.Interface.ClientFacture;
import SOURCES.Interface.PaiementFacture;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public abstract class EcouteurFacture {
    
    public abstract void onEnregistre(ClientFacture client, Vector<ArticleFacture> articles, Vector<PaiementFacture> paiements);
    
}
