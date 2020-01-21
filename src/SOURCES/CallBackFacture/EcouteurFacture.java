/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.CallBackFacture;

import SOURCES.Utilitaires_Facture.SortiesFacture;

/**
 *
 * @author HP Pavilion
 */
public abstract class EcouteurFacture {
    public abstract void onEnregistre(SortiesFacture sortiesFacture);
    public abstract void onDetruitPaiement(int idPaiement, long signature);
    public abstract boolean onCanDelete(int idPaiement, long signature);
    public abstract void onDetruitTousLesPaiements(int idEleve, int idExercice);
    
}
