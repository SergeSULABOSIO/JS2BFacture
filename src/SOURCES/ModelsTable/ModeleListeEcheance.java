/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable;


import SOURCES.Utilitaires_Facture.UtilFacture;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import SOURCES.Utilitaires_Facture.CalculateurLitigesFacture;
import SOURCES.Utilitaires_Facture.ParametresFacture;
import Source.Callbacks.EcouteurValeursChangees;
import Source.Interface.InterfaceEcheance;
import Source.Objet.Echeance;
import Source.Objet.Frais;
import Source.Objet.Paiement;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeEcheance extends AbstractTableModel {

    private String[] titreColonnes = {"N°", "Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Progression"};
    private Vector<Echeance> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private ModeleListePaiement modeleListePaiement;
    private ModeleListeFrais modeleListeArticles;
    private ParametresFacture parametresFacture;
    public int nombreTranches = 1;

    public ModeleListeEcheance(JScrollPane parent, ModeleListePaiement modeleListePaiement, ModeleListeFrais modeleListeArticles, ParametresFacture parametresFacture, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.modeleListePaiement = modeleListePaiement;
        this.modeleListeArticles = modeleListeArticles;
        this.parametresFacture = parametresFacture;
        this.listeData = CalculateurLitigesFacture.getEcheances(modeleListeArticles.getListeData(), modeleListePaiement, parametresFacture);
    }

    public void actualiser() {
        this.listeData = CalculateurLitigesFacture.getEcheances(modeleListeArticles.getListeData(), modeleListePaiement, parametresFacture);
        redessinerTable();
    }

    public Vector<Echeance> getListeData() {
        return listeData;
    }

    public void AjouterEcheance(Echeance echeance) {
        this.listeData.add(echeance);
        redessinerTable();
    }

    public void AjouterEcheanceAutomatique(Echeance echeance) {
        this.listeData.add(echeance);
        redessinerTable();
    }

    public void AjouterEcheancesAutomatique(Vector<Echeance> echeances) {
        this.listeData.addAll(echeances);
        redessinerTable();
    }

    private void deleteLigne(int row) {
        if (row <= listeData.size()) {
            this.listeData.removeElementAt(row);
        }
        redessinerTable();
    }

    public void SupprimerEcheance(int row, boolean mustConfirm) {
        if (row < listeData.size() && row != -1) {
            InterfaceEcheance articl = listeData.elementAt(row);
            if (mustConfirm == true) {
                if (articl != null) {
                    int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir supprimer cette ligne?", "Avertissement", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        deleteLigne(row);
                    }
                }
            } else {
                deleteLigne(row);
            }
        }
    }

    public double getTotalMontantDu() {
        double tot = 0;
        if (modeleListeArticles != null) {
            for (Frais articleApayer : modeleListeArticles.getListeData()) {
                tot = tot + articleApayer.getMontantDefaut();
            }
        }
        return tot;
    }

    public double getTotalMontantPaye() {
        double tot = 0;
        for (Paiement paiement : modeleListePaiement.getListeData()) {
            tot = tot + paiement.getMontant();
        }
        return tot;
    }

    public double getTotalReste() {
        double Tsolde = UtilFacture.round(getTotalMontantDu() - getTotalMontantPaye(), 2);
        return Tsolde;
    }

    public void viderListe() {
        int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir vider cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            this.listeData.removeAllElements();
            redessinerTable();
        }
    }

    public Echeance getEcheance_id(int id) {
        for (Echeance paiem : listeData) {
            if (paiem.getId() == id) {
                return paiem;
            }
        }
        return null;
    }

    public Echeance getEcheance_row(int row) {
        if (row < listeData.size() && row != -1) {
            Echeance art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
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

    public String getStatus(int rowIndex) {
        if (rowIndex <= listeData.size()) {
            Echeance eche = this.listeData.elementAt(rowIndex);
            if (eche != null) {
                int nbjour = (int) UtilFacture.getNombre_jours_from_today(eche.getDateFinale());
                String jourS = " jour";
                if (nbjour > 0 || nbjour < -1) {
                    jourS = " jours";
                } else if (nbjour == 0) {
                    jourS = "";
                }
                String commentaire = "Expire dans " + nbjour + jourS;
                if (nbjour < 0) {
                    commentaire = "Expirée depuis " + (nbjour * -1) + jourS;
                } else if (nbjour == 0) {
                    commentaire = "Expire aujourd'hui";
                }
                return commentaire;
            }
            return "RAS";
        } else {
            return "RAS";
        }
    }

    public double getMontantDu(int row) {
        if (row <= this.listeData.size()) {
            Echeance eche = listeData.elementAt(row);
            if (eche != null) {
                return UtilFacture.round(eche.getMontantDu(), 2);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public double getMontantPaye(int row) {
        if (row <= this.listeData.size()) {
            Echeance eche = listeData.elementAt(row);
            if (eche != null) {
                return UtilFacture.round(listeData.elementAt(row).getMontantPaye(), 2);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //{"N°", "Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1) + "";
            case 1:
                return listeData.elementAt(rowIndex).getNom();
            case 2:
                return listeData.elementAt(rowIndex).getDateInitiale();
            case 3:
                return listeData.elementAt(rowIndex).getDateFinale();
            case 4:
                return getStatus(rowIndex);
            case 5:
                return getMontantDu(rowIndex);
            case 6:
                return getMontantPaye(rowIndex);
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"N°", "Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
        switch (columnIndex) {
            case 0:
                return String.class;//N°
            case 1:
                return String.class;//Nom
            case 2:
                return Date.class;//Date initiale
            case 3:
                return Date.class;//Date finale
            case 4:
                return String.class;//Jrs restant
            case 5:
                return Double.class;//montant paye
            case 6:
                return Double.class;//Progression
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //{"N°", "Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Nom", "Date initiale", "Echéance", "Status", "Montant dû", "Montant payé"};
        Echeance echeance = listeData.get(rowIndex);
        switch (columnIndex) {
            case 1:
                echeance.setNom(aValue + "");
                break;
            case 2:
                echeance.setDateInitiale((Date) aValue);
                break;
            case 3:
                echeance.setDateFinale((Date) aValue);
                break;
            case 5:
                echeance.setMontantDu(Double.parseDouble(aValue + ""));
                break;
            default:
                break;
        }
        listeData.set(rowIndex, echeance);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

}
