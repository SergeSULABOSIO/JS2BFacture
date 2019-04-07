/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable;

import BEAN_BARRE_OUTILS.Bouton;
import BEAN_MenuContextuel.RubriqueSimple;
import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Utilitaires.Util;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Utilitaires.DonneesFacture;
import SOURCES.Utilitaires.ParametresFacture;
import java.awt.Color;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeArticles extends AbstractTableModel {

    private String[] titreColonnes = {"N°", "Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC"};
    private Vector<InterfaceArticle> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private ParametresFacture parametresFacture;
    private DonneesFacture donneesFacture;
    private Bouton btEnreg;
    private RubriqueSimple mEnreg;

    public ModeleListeArticles(JScrollPane parent, Bouton btEnreg, RubriqueSimple mEnreg, ParametresFacture parametresFacture, DonneesFacture donneesFacture, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.parametresFacture = parametresFacture;
        this.donneesFacture = donneesFacture;
        this.ecouteurModele = ecouteurModele;
        this.mEnreg = mEnreg;
        this.btEnreg = btEnreg;
        appliquerAssigetissementTVA();
    }

    public void setListeArticles(Vector<InterfaceArticle> listeData) {
        this.listeData = listeData;
        appliquerAssigetissementTVA();
        redessinerTable();
    }

    public InterfaceArticle getArticle(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceArticle art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public InterfaceArticle getArticle_id(int id) {
        if (id != -1) {
            for (InterfaceArticle art : listeData) {
                //System.out.println(" - id="+art.getId());
                if (id == art.getId()) {
                    return art;
                }
            }
        }
        return null;
    }

    public Vector<InterfaceArticle> getListeData() {
        return listeData;
    }

    private void appliquerAssigetissementTVA() {
        changeVat();
    }

    private void changeVat() {
        listeData.forEach((art) -> {
            art.setTvaPoucentage(0);
        });
    }

    public void AjouterArticle(InterfaceArticle art) {
        this.listeData.add(art);
        mEnreg.setCouleur(Color.blue);
        btEnreg.setCouleur(Color.blue);
        appliquerAssigetissementTVA();
        redessinerTable();
    }

    public void SupprimerArticle(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceArticle articl = listeData.elementAt(row);
            if (articl != null) {
                int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir supprimer cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    if (row <= listeData.size()) {
                        this.listeData.removeElementAt(row);
                    }
                    redessinerTable();
                }
            }
        }
    }

    public double getTotal_Net() {
        double mntTva = 0;
        double mntTTC = 0;
        for (InterfaceArticle art : listeData) {
            mntTva = mntTva + Util.getMontantOutPut(parametresFacture, art.getIdMonnaie(), art.getTvaMontant());
            mntTTC = mntTTC + Util.getMontantOutPut(parametresFacture, art.getIdMonnaie(), art.getTotalTTC());
        }
        return Util.round((mntTTC - mntTva), 2);
    }

    public double getTotal_Net_AvantRabais() {
        double mnt = 0;
        for (InterfaceArticle art : listeData) {
            mnt = mnt + Util.getMontantOutPut(parametresFacture, art.getIdMonnaie(), art.getPrixUHT_avant_rabais());
        }
        return Util.round((mnt), 2);
    }

    public double getTotal_TVA() {
        double a = 0;
        for (InterfaceArticle art : listeData) {
            a = a + Util.getMontantOutPut(parametresFacture, art.getIdMonnaie(), art.getTvaMontant());
        }
        return Util.round(a, 2);
    }

    public double getTotal_Rabais() {
        double a = 0;
        for (InterfaceArticle art : listeData) {
            a = a + Util.getMontantOutPut(parametresFacture, art.getIdMonnaie(), art.getRabais());
        }
        return Util.round(a, 2);
    }

    public double getTotal_TTC() {
        double a = 0;
        for (InterfaceArticle art : listeData) {
            a = a + Util.getMontantOutPut(parametresFacture, art.getIdMonnaie(), art.getTotalTTC());
        }
        return Util.round(a, 2);
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
        //{"N°", "Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC"};
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1) + "";
            case 1:
                return listeData.elementAt(rowIndex).getId();
            case 2:
                return listeData.elementAt(rowIndex).getQte();
            case 3:
                return listeData.elementAt(rowIndex).getPrixUHT_avant_rabais();
            case 4:
                return listeData.elementAt(rowIndex).getRabais();
            case 5:
                return listeData.elementAt(rowIndex).getPrixUHT_apres_rabais();
            case 6:
                return listeData.elementAt(rowIndex).getTvaMontant();
            case 7:
                return listeData.elementAt(rowIndex).getTotalTTC();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"N°", "Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC"};
        switch (columnIndex) {
            case 0:
                return String.class;//N°
            case 1:
                return Integer.class;//Nom
            case 2:
                return Double.class;//Qte
            case 3:
                return Double.class;//Prix U
            case 4:
                return Double.class;//Rabais
            case 5:
                return Double.class;//Prix U
            case 6:
                return Double.class;//Mnt Tva
            case 7:
                return Double.class;//Mnt TTC
            default:
                return Object.class;
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    private void updateArticle(InterfaceArticle newArticle) {
        if (newArticle != null && donneesFacture != null) {
            for (InterfaceArticle Iarticle : donneesFacture.getArticles()) {
                if (Iarticle.getId() == newArticle.getId()) {
                    newArticle.setNom(Iarticle.getNom());
                    newArticle.setPrixUHT_avant_rabais(Iarticle.getPrixUHT_avant_rabais());
                    newArticle.setUnite(Iarticle.getUnite());
                    return;
                }
            }
        }
        redessinerTable();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Article", "Qté", "Prix U.", "Rabais", "Prix U.", "Mnt Tva", "Mnt TTC""};
        InterfaceArticle article = listeData.get(rowIndex);
        String avant = article.toString();
        switch (columnIndex) {
            case 1:
                article.setId(Integer.parseInt(aValue + ""));
                updateArticle(article);
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
        if(!avant.equals(apres)){
            if(article.getBeta() == InterfaceArticle.BETA_EXISTANT){
                article.setBeta(InterfaceArticle.BETA_MODIFIE);
                mEnreg.setCouleur(Color.blue);
                btEnreg.setCouleur(Color.blue);
            }
        }
        listeData.set(rowIndex, article);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }
}
