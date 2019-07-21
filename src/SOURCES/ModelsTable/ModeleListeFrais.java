/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable;

import BEAN_BARRE_OUTILS.Bouton;
import BEAN_MenuContextuel.RubriqueSimple;
import SOURCES.Utilitaires_Facture.UtilFacture;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import SOURCES.Utilitaires_Facture.ParametresFacture;
import Source.Callbacks.EcouteurSuppressionElement;
import Source.Callbacks.EcouteurValeursChangees;
import Source.Objet.CouleurBasique;
import Source.Objet.Frais;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeFrais extends AbstractTableModel {

    private String[] titreColonnes = {"N°", "Frais", "Montant"};
    private Vector<Frais> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private ParametresFacture parametresFacture;
    private Bouton btEnreg;
    private RubriqueSimple mEnreg;
    private CouleurBasique colBasique;

    public ModeleListeFrais(CouleurBasique colBasique, JScrollPane parent, Bouton btEnreg, RubriqueSimple mEnreg, ParametresFacture parametresFacture, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.parametresFacture = parametresFacture;
        this.ecouteurModele = ecouteurModele;
        this.mEnreg = mEnreg;
        this.btEnreg = btEnreg;
        this.colBasique = colBasique;
    }

    public void setListeFrais(Vector<Frais> listeData) {
        this.listeData = listeData;
        redessinerTable();
    }

    public Frais getFrais(int row) {
        if (row < listeData.size() && row != -1) {
            Frais art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Frais getFrais_id(int id) {
        if (id != -1) {
            for (Frais art : listeData) {
                //System.out.println(" - id="+art.getId());
                if (id == art.getId()) {
                    return art;
                }
            }
        }
        return null;
    }

    public Vector<Frais> getListeData() {
        return listeData;
    }

    
    public void AjouterFrais(Frais art) {
        this.listeData.add(art);
        //mEnreg.setCouleur(Color.blue);
        //btEnreg.setForeground(Color.blue);
        //appliquerAssigetissementTVA();
        mEnreg.setCouleur(colBasique.getCouleur_foreground_objet_nouveau());                                        //mEnreg.setCouleur(Color.blue);
        btEnreg.setForeground(colBasique.getCouleur_foreground_objet_nouveau());                                   //btEnreg.setForeground(Color.blue);
        redessinerTable();
    }

    public void SupprimerFrais(int row, EcouteurSuppressionElement ecouteurSuppressionElement) {
        if (row < listeData.size() && row != -1) {
            Frais articl = listeData.elementAt(row);
            if (articl != null) {
                int idASupp = articl.getId();
                int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir supprimer cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    if (row <= listeData.size()) {
                        this.listeData.removeElementAt(row);
                        ecouteurSuppressionElement.onSuppressionConfirmee(idASupp);
                    }
                    redessinerTable();
                }
            }
        }
    }

    public double getTotal_TTC() {
        double a = 0;
        for (Frais art : listeData) {
            a = a + UtilFacture.getMontantOutPut(parametresFacture, art.getIdMonnaie(), art.getMontantDefaut());
        }
        return UtilFacture.round(a, 2);
    }

    public void viderListe() {
        int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir vider cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            this.listeData.removeAllElements();
            redessinerTable();
        }
    }

    public void redessinerTable() {
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return listeData.size();
    }

    @Override
    public int getColumnCount() {
        return titreColonnes.length;
    }

    @Override
    public String getColumnName(int column) {
        return titreColonnes[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //{"N°", "Frais", "Montant"};
        if (rowIndex < listeData.size()) {
            Frais Icharge = listeData.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return (rowIndex + 1) + "";
                case 1:
                    return Icharge.getId();
                case 2:
                    return Icharge.getIdMonnaie();
                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"N°", "Frais", "Montant"};
        switch (columnIndex) {
            case 0:
                return String.class;//N°
            case 1:
                return Integer.class;//Frais
            case 2:
                return Integer.class;//Montant (mais on envoi uniquement le ID du Frais afin de récupérer plus de détails)
            default:
                return Object.class;
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        /*
        //{"N°", "Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC""};
        Frais article = listeData.get(rowIndex);
        String avant = article.toString();
        switch (columnIndex) {
            case 1:
                article.setId(Integer.parseInt(aValue + ""));
                break;
            case 2:
                article.setQte(Double.parseDouble(aValue + ""));
                break;
            case 4:
                article.setRabais(Double.parseDouble(aValue + ""));
                break;
            default:
                break;
        }
        String apres = article.toString();
        if (!avant.equals(apres)) {
            if (article.getBeta() == InterfaceFrais.BETA_EXISTANT) {
                article.setBeta(InterfaceFrais.BETA_MODIFIE);
                //mEnreg.setCouleur(Color.blue);
                //btEnreg.setForeground(Color.blue);
                mEnreg.setCouleur(colBasique.getCouleur_foreground_objet_nouveau());                                        //mEnreg.setCouleur(Color.blue);
                btEnreg.setForeground(colBasique.getCouleur_foreground_objet_nouveau());                                   //btEnreg.setForeground(Color.blue);
            }
        }
        listeData.set(rowIndex, article);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
        
         */
    }
}
