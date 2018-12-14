/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.CallBack;

import SOURCES.ModelsTable.ModeleListeArticles;
import SOURCES.ModelsTable.ModeleListePaiement;

/**
 *
 * @author HP Pavilion
 */
public abstract class EcouteurAjout {
    
    public abstract void setAjoutArticle(ModeleListeArticles modeleListeArticles);
    public abstract void setAjoutPaiement(ModeleListePaiement modeleListePaiement);
    
}
