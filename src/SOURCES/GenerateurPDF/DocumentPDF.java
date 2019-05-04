/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.GenerateurPDF;

import SOURCES.ModelsTable.ModeleListeArticles;
import SOURCES.ModelsTable.ModeleListePaiement;
import SOURCES.UI.Panel;
import SOURCES.Utilitaires.Util;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceClasse;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.Interface.InterfaceEntreprise;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceEleve;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.ModelsTable.ModeleListeEcheance;
import SOURCES.Utilitaires.SortiesFacture;

/**
 *
 * @author Gateway
 */
public class DocumentPDF extends PdfPageEventHelper {

    private Document document = new Document(PageSize.A4);
    private Font Font_Titre1 = null;
    private Font Font_Titre2 = null;
    private Font Font_Titre3 = null;
    private Font Font_TexteSimple = null;
    private Font Font_TexteSimple_petit, Font_TexteSimple_petit_Gras = null;
    private Font Font_TexteSimple_Gras = null;
    private Font Font_TexteSimple_Italique = null;
    private Font Font_TexteSimple_Gras_Italique = null;
    public static final int ACTION_IMPRIMER = 0;
    public static final int ACTION_OUVRIR = 1;
    public boolean isRecu = false;
    public SortiesFacture sortiesFacture = null;
    private Panel gestionnaireFacture;

    public DocumentPDF(Panel panel, int action, boolean isRecu, SortiesFacture sortiesFacture) {
        try {
            this.init(panel, action, isRecu, sortiesFacture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Panel panel, int action, boolean isRecu, SortiesFacture sortiesFacture) {
        this.gestionnaireFacture = panel;
        this.isRecu = isRecu;
        this.sortiesFacture = sortiesFacture;
        parametre_initialisation_fichier();
        parametre_construire_fichier();
        if (action == ACTION_OUVRIR) {
            parametres_ouvrir_fichier();
        } else if (action == ACTION_IMPRIMER) {
            parametres_imprimer_fichier();
        }
    }

    private void parametre_initialisation_fichier() {
        //Les titres du document
        this.Font_Titre1 = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD, BaseColor.DARK_GRAY);
        this.Font_Titre2 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
        this.Font_Titre3 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);

        //Les textes simples
        this.Font_TexteSimple = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK);
        this.Font_TexteSimple_petit = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL, BaseColor.BLACK);
        this.Font_TexteSimple_petit_Gras = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD, BaseColor.BLACK);
        this.Font_TexteSimple_Gras = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK);
        this.Font_TexteSimple_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK);
        this.Font_TexteSimple_Gras_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLDITALIC, BaseColor.BLACK);
    }

    private void parametre_construire_fichier() {
        try {
            String nomFichier = "Facture_S2B.pdf";
            if (this.gestionnaireFacture != null) {
                nomFichier = this.gestionnaireFacture.getNomfichierPreuve();
            }
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nomFichier));
            writer.setPageEvent(new MarqueS2B());
            this.document.open();
            this.setDonneesBibliographiques();
            this.setContenuDeLaPage();
            this.document.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.gestionnaireFacture, "Impossible de produire la facture\nAssurez vous qu'aucun fichier du même nom ne soit actuellement ouvert.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void parametres_ouvrir_fichier() {
        String nomFichier = "Facture_S2B.pdf";
        if (this.gestionnaireFacture != null) {
            nomFichier = this.gestionnaireFacture.getNomfichierPreuve();
        }
        File fic = new File(nomFichier);
        if (fic.exists() == true) {
            try {
                Desktop.getDesktop().open(fic);
                if (sortiesFacture != null) {
                    sortiesFacture.getEcouteurEnregistrement().onDone("PDF ouvert avec succès!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                String message = "Impossible d'ouvrir le fichier !";
                JOptionPane.showMessageDialog(this.gestionnaireFacture, message, "Erreur", JOptionPane.ERROR_MESSAGE);
                if (sortiesFacture != null) {
                    sortiesFacture.getEcouteurEnregistrement().onError(message);
                }
            }
        }
    }

    private void parametres_imprimer_fichier() {
        String nomFichier = "Facture_S2B.pdf";
        if (this.gestionnaireFacture != null) {
            nomFichier = this.gestionnaireFacture.getNomfichierPreuve();
        }
        File fic = new File(nomFichier);
        if (fic.exists() == true) {
            try {
                Desktop.getDesktop().print(fic);
                if (sortiesFacture != null) {
                    sortiesFacture.getEcouteurEnregistrement().onDone("Impression effectuée avec succès!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                String message = "Impossible d'imprimer les données ";
                JOptionPane.showMessageDialog(this.gestionnaireFacture, message, "Erreur", JOptionPane.ERROR_MESSAGE);
                if (sortiesFacture != null) {
                    sortiesFacture.getEcouteurEnregistrement().onError(message);
                }
            }
        }
    }

    private void setDonneesBibliographiques() {
        this.document.addTitle("Document généré par JS2BFacture");
        this.document.addSubject("Etat");
        this.document.addKeywords("Java, PDF, Facture");
        this.document.addAuthor("S2B. Simple.Intuitif");
        this.document.addCreator("SULA BOSIO Serge, S2B, sulabosiog@gmail.com");
    }

    private void ajouterLigne(int number) throws Exception {
        Paragraph paragraphe = new Paragraph();
        for (int i = 0; i < number; i++) {
            paragraphe.add(new Paragraph(" "));
        }
        this.document.add(paragraphe);
    }

    private void setTitreEtDateDocument(boolean isRecu) throws Exception {
        Paragraph preface = new Paragraph();
        String titre = "Reçu";
        if (isRecu == false) {
            titre = "Relevé de compte";
        }

        if (this.gestionnaireFacture != null) {
            preface.add(getParagraphe("Date: " + Util.getDateFrancais(this.gestionnaireFacture.getDateFacture()), Font_Titre3, Element.ALIGN_RIGHT));
            preface.add(getParagraphe(titre + " n°" + this.gestionnaireFacture.getParametres().getNumero(), Font_Titre1, Element.ALIGN_CENTER));
        } else {
            preface.add(getParagraphe("Date: " + Util.getDateFrancais(new Date()), Font_Titre3, Element.ALIGN_RIGHT));
            preface.add(getParagraphe("Facture n°XXXXXXXXX/2018", Font_Titre1, Element.ALIGN_CENTER));
        }
        this.document.add(preface);
    }

    private void setSignataire() throws Exception {
        if (this.gestionnaireFacture != null) {
            this.document.add(getParagraphe(""
                    + "Produit par " + this.gestionnaireFacture.getParametres().getNomUtilisateur() + "\n"
                    + "Validé par :..............................................\n\n"
                    + "Signature", Font_TexteSimple, Element.ALIGN_RIGHT));
        } else {
            this.document.add(getParagraphe(""
                    + "Produit par Serge SULA BOSIO\n"
                    + "Validé par :..............................................\n\n"
                    + "Signature", Font_TexteSimple, Element.ALIGN_RIGHT));
        }

    }

    private void setBasDePage() throws Exception {
        if (this.gestionnaireFacture != null) {
            InterfaceEntreprise entreprise = this.gestionnaireFacture.getParametres().getEntreprise();
            if (entreprise != null) {
                this.document.add(getParagraphe(entreprise.getNom() + "\n" + entreprise.getAdresse() + " | " + entreprise.getTelephone() + " | " + entreprise.getEmail() + " | " + entreprise.getSiteWeb(), Font_TexteSimple, Element.ALIGN_CENTER));
            } else {
                addDefaultEntreprise();
            }
        } else {
            addDefaultEntreprise();
        }
    }

    private void addDefaultEntreprise() throws Exception {
        this.document.add(getParagraphe(""
                + "UAP RDC Sarl. Courtier d’Assurances n°0189\n"
                + "Prins van Luikschool, Av de la Gombe, Gombe, Kinshasa, DRC | (+243) 975 33 88 33 | info@aib-brokers.com", Font_TexteSimple, Element.ALIGN_CENTER));

    }

    private Paragraph getParagraphe(String texte, Font font, int alignement) {
        Paragraph par = new Paragraph(texte, font);
        par.setAlignment(alignement);
        return par;
    }

    private Phrase getPhrase(String texte, Font font) {
        Phrase phrase = new Phrase(texte, font);
        return phrase;
    }

    private void setLogoEtDetailsEntreprise() {
        try {
            PdfPTable tableauEnteteFacture = new PdfPTable(2);
            int[] dimensionsWidthHeight = {320, 1460};
            tableauEnteteFacture.setWidths(dimensionsWidthHeight);
            tableauEnteteFacture.setHorizontalAlignment(Element.ALIGN_LEFT);

            //CELLULE DU LOGO DE L'ENTREPRISE
            PdfPCell celluleLogoEntreprise = null;
            String logo = "";
            if (this.gestionnaireFacture != null) {
                logo = this.gestionnaireFacture.getParametres().getEntreprise().getLogo();
            }
            File ficLogo = new File(logo);
            if (ficLogo.exists() == true) {
                //Chargement du logo et redimensionnement afin que celui-ci convienne dans l'espace qui lui est accordé
                Image Imglogo = Image.getInstance(logo);
                Imglogo.scaleAbsoluteWidth(70);
                Imglogo.scaleAbsoluteHeight(70);
                celluleLogoEntreprise = new PdfPCell(Imglogo);
            } else {
                celluleLogoEntreprise = new PdfPCell();
            }
            celluleLogoEntreprise.setPadding(2);
            celluleLogoEntreprise.setBorderWidth(0);
            celluleLogoEntreprise.setBorderColor(BaseColor.BLACK);
            tableauEnteteFacture.addCell(celluleLogoEntreprise);

            //CELLULE DES DETAILS SUR L'ENTREPRISE - TEXTE (Nom, Adresse, Téléphone, Email, etc)
            PdfPCell celluleDetailsEntreprise = new PdfPCell();
            celluleDetailsEntreprise.setPadding(2);
            celluleDetailsEntreprise.setPaddingLeft(5);
            celluleDetailsEntreprise.setBorderWidth(0);
            celluleDetailsEntreprise.setBorderWidthLeft(1);
            celluleDetailsEntreprise.setBorderColor(BaseColor.BLACK);
            celluleDetailsEntreprise.setHorizontalAlignment(Element.ALIGN_TOP);

            if (this.gestionnaireFacture != null) {
                InterfaceEntreprise entreprise = this.gestionnaireFacture.getParametres().getEntreprise();
                if (entreprise != null) {
                    celluleDetailsEntreprise.addElement(getParagraphe(entreprise.getNom(), Font_Titre2, Element.ALIGN_LEFT));
                    celluleDetailsEntreprise.addElement(getParagraphe(entreprise.getAdresse(), Font_TexteSimple_petit, Element.ALIGN_LEFT));
                    celluleDetailsEntreprise.addElement(getParagraphe(entreprise.getSiteWeb() + " | " + entreprise.getEmail() + " | " + entreprise.getTelephone(), Font_TexteSimple_petit, Element.ALIGN_LEFT));
                    celluleDetailsEntreprise.addElement(getParagraphe("RCC : " + entreprise.getRccm()+ "\nID. NAT : " + entreprise.getIdnat()+ "\nNIF : " + entreprise.getNumeroImpot(), Font_TexteSimple_petit, Element.ALIGN_LEFT));
                }
            } else {
                celluleDetailsEntreprise.addElement(getParagraphe("UAP RDC Sarl, Courtier d'Assurances n°0189", Font_Titre2, Element.ALIGN_LEFT));
                celluleDetailsEntreprise.addElement(getParagraphe("Avenue de la Gombe, Kinshasa/Gombe", Font_TexteSimple_petit, Element.ALIGN_LEFT));
                celluleDetailsEntreprise.addElement(getParagraphe("https://www.aib-brokers.com | info@aib-brokers.com | (+243)84 480 35 14 - (+243)82 87 27 706", Font_TexteSimple_petit, Element.ALIGN_LEFT));
                celluleDetailsEntreprise.addElement(getParagraphe("RCC : CDF/KIN/2015-1245\nID. NAT : 0112487789\nNIF : 012245", Font_TexteSimple_petit, Element.ALIGN_LEFT));
            }
            tableauEnteteFacture.addCell(celluleDetailsEntreprise);

            //On insère le le tableau entete (logo et détails de l'entreprise) dans la page
            document.add(tableauEnteteFacture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDetailsEleves() {
        try {
            PdfPTable tableDetailsClient = new PdfPTable(2);
            int[] dimensionsWidthHeight = {320, 1460};
            tableDetailsClient.setWidths(dimensionsWidthHeight);
            tableDetailsClient.setHorizontalAlignment(Element.ALIGN_LEFT);

            //Colonne des titres
            PdfPCell celluleTitres = new PdfPCell();
            celluleTitres.setPadding(2);
            celluleTitres.setBorderWidth(0);
            celluleTitres.addElement(getParagraphe("Elève:\nClasse: \nContacts:\nAnnée scolaire:", Font_TexteSimple_Gras, Element.ALIGN_RIGHT));

            //Colonne des valeurs = détails sur le client
            PdfPCell celluleDonnees = new PdfPCell();
            celluleDonnees.setPadding(2);
            celluleDonnees.setBorderWidth(0);
            if (this.gestionnaireFacture != null) {
                InterfaceEleve eleve = this.gestionnaireFacture.getDonneesFacture().getEleve();
                InterfaceClasse classe = Util.getClasse(gestionnaireFacture.getParametres(), eleve.getIdClasse());
                String Seleve = eleve.getNom() + " " + eleve.getPostnom() + " " + eleve.getPrenom();
                String Sclasse = classe.getNom() + ", " + classe.getNomLocal();
                String Scontacts = eleve.getTelephonesParents() + ", " + eleve.getAdresse();
                String Sannee = this.gestionnaireFacture.getParametres().getExercice().getNom();
                celluleDonnees.addElement(getParagraphe(Seleve + "\n" + Sclasse + "\n" + Scontacts + "\n" + Sannee, Font_TexteSimple_Italique, Element.ALIGN_LEFT));
            } else {
                celluleDonnees.addElement(getParagraphe("SULA BOSIO SERGE\n(+243)844803514, (+243)828727706\nClasse: 1e A, Ecole 42 - Informatique de Gestion - Université de Kinshasa - RDC", Font_TexteSimple_Italique, Element.ALIGN_LEFT));
            }

            tableDetailsClient.addCell(celluleTitres);
            tableDetailsClient.addCell(celluleDonnees);

            document.add(tableDetailsClient);
            //addEmptyLine(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setTableauDetailsPaiementsRecus(Vector<InterfacePaiement> listePaiement) {
        System.out.println("listePaiement: " + listePaiement.size());
        try {
            document.add(getParagraphe("Détails - Paiements reçus", Font_TexteSimple, Element.ALIGN_CENTER));
            PdfPTable tableReleve = getTableau(
                    -1,
                    new String[]{"N°", "Dates", "Frais", "Référence", "Mode", "Période", "Montant reçu", "Solde période"},
                    new int[]{80, 200, 400, 300, 200, 200, 200, 200},
                    Element.ALIGN_CENTER,
                    0.2f
            );
            double totPaye = 0, totReste = 0;
            if (this.gestionnaireFacture != null) {
                ModeleListePaiement modelPaiement = this.gestionnaireFacture.getModeleListePaiement();
                //Vector<InterfacePaiement> listePaiement = modelPaiement.getListeData();
                int i = 0;
                totPaye = 0;
                for (InterfacePaiement paiement : listePaiement) {
                    //cumuls
                    InterfaceArticle Iarticle = Util.getArticle(gestionnaireFacture.getDonneesFacture(), paiement.getIdArticle());
                    InterfaceMonnaie Imonnaie = Util.getMonnaie(gestionnaireFacture.getParametres(), Iarticle.getIdMonnaie());
                    if (Iarticle != null && Imonnaie != null) {
                        String monnaie = Imonnaie.getCode();
                        totPaye += Util.getMontantOutPut(gestionnaireFacture.getParametres(), Iarticle.getIdMonnaie(), paiement.getMontant());
                        totReste += Util.getMontantOutPut(gestionnaireFacture.getParametres(), Iarticle.getIdMonnaie(), modelPaiement.getReste(paiement.getIdArticle(), -1));
                        String periode = Util.getPeriode(gestionnaireFacture.getParametres(), paiement.getIdPeriode()).getNom();
                        double restePeriode = Util.round((modelPaiement.getReste(paiement.getIdArticle(), paiement.getIdPeriode())), 2);
                        setLigneTabReleve(tableReleve, monnaie, Util.getDateFrancais(paiement.getDate()), paiement.getNomArticle(), paiement.getReferenceTransaction(), periode, paiement.getMode(), i, Util.round(paiement.getMontant(), 2), restePeriode);
                        i++;
                    }
                }
                setDerniereLigneTabReleve(tableReleve, gestionnaireFacture.getParametres().getMonnaieOutPut().getCode(), totPaye, totReste);
            }
            document.add(tableReleve);
            String monnaie = gestionnaireFacture.getParametres().getMonnaieOutPut().getNom();
            document.add(getParagraphe("En lettre : " + (Util.getMontantLettres(totPaye, monnaie)), Font_TexteSimple_petit_Gras, Element.ALIGN_RIGHT));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTableauDetailsPlanPaiementEchelonne() {
        try {
            document.add(getParagraphe("Détails - Plan de paiement par période", Font_TexteSimple, Element.ALIGN_CENTER));
            PdfPTable tableEchances = getTableau(
                    -1,
                    new String[]{"N°", "Période", "Début", "Echéance", "Progession", "Montant dû", "Montant reçu"},
                    new int[]{80, 350, 300, 300, 350, 200, 200},
                    Element.ALIGN_CENTER,
                    0.2f
            );
            if (this.gestionnaireFacture != null) {
                ModeleListeEcheance modeEcheance = this.gestionnaireFacture.getModeleListeEcheance();
                Vector<InterfaceEcheance> listeEcheances = modeEcheance.getListeData();
                int i = 0;
                double totDu = 0;
                double totPaye = 0;
                for (InterfaceEcheance echeance : listeEcheances) {
                    setLigneTabEcheance(tableEchances, echeance.getNom(), Util.getDateFrancais(echeance.getDateInitiale()), Util.getDateFrancais(echeance.getDateFinale()), modeEcheance.getStatus(i), i, Util.round(echeance.getMontantDu(), 2), Util.round(echeance.getMontantPaye(), 2));
                    totDu += echeance.getMontantDu();
                    totPaye += echeance.getMontantPaye();
                    i++;
                }
                setDerniereLigneTabEcheance(tableEchances, Util.round(totDu, 2), Util.round(totPaye, 2));
            } else {
                for (int i = 0; i < 10; i++) {
                    setLigneTabEcheance(tableEchances, "Tranche n°" + (i + 1), Util.getDateFrancais(new Date()), Util.getDateFrancais(new Date()), "Expirée depuis " + (i + 1) + " jour", i, 100, 50);
                }
                setDerniereLigneTabEcheance(tableEchances, 1000, 500);
            }
            document.add(tableEchances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTableauSynthese() {
        try {
            PdfPTable tableSynthese = getTableau(
                    120f,
                    new String[]{"Synthèse", ""},
                    new int[]{120, 120},
                    Element.ALIGN_RIGHT,
                    0
            );
            if (this.gestionnaireFacture != null) {
                ModeleListePaiement mPaiement = this.gestionnaireFacture.getModeleListePaiement();
                ModeleListeArticles mfacture = this.gestionnaireFacture.getModeleListeArticles();
                if (mPaiement != null & mfacture != null) {
                    double Tpaye = mPaiement.getTotalMontant();
                    double Tnet = mfacture.getTotal_Net();
                    double Ttva = mfacture.getTotal_TVA();
                    double Tttc = mfacture.getTotal_TTC();
                    double Trab = mfacture.getTotal_Rabais();
                    double Tsolde = Util.round(Tttc - Tpaye, 2);
                    setLignesTabSynthese(tableSynthese, 0, this.gestionnaireFacture.getParametres().getMonnaieOutPut().getCode(), Trab, Tnet, Ttva, Tttc, Tpaye, Tsolde);
                }
            } else {
                setLignesTabSynthese(tableSynthese, 0, "$", 100, 1000, 160, 1160, 60, 1100);
            }
            document.add(tableSynthese);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTableauDetailsArticles() {
        try {
            document.add(getParagraphe("Détails - Articles", Font_TexteSimple, Element.ALIGN_CENTER));
            PdfPTable tableDetailsArticles = getTableau(
                    -1,
                    new String[]{"N°", "Frais", "Qté", "Prix Unit.", "Rabais", "Prix Unit.", "Tva", "Total TTC"},
                    new int[]{100, 800, 150, 250, 250, 250, 250, 300},
                    Element.ALIGN_CENTER,
                    0.2f
            );

            if (this.gestionnaireFacture != null) {
                ModeleListeArticles modelArticle = this.gestionnaireFacture.getModeleListeArticles();
                Vector<InterfaceArticle> listeArticles = modelArticle.getListeData();
                int i = 0;
                for (InterfaceArticle article : listeArticles) {
                    setLigneTabArticle(tableDetailsArticles, article, i);
                    i++;
                }
                setDerniereLigneTabArticle(tableDetailsArticles, modelArticle.getTotal_Net_AvantRabais(), modelArticle.getTotal_Rabais(), modelArticle.getTotal_Net(), modelArticle.getTotal_TVA(), modelArticle.getTotal_TTC());
            } else {
                for (int i = 0; i < 5; i++) {
                    setLigneTabArticle(tableDetailsArticles, null, i);
                }
                setDerniereLigneTabArticle(tableDetailsArticles, 2400, 400, 200, 1560, 25000);
            }
            document.add(tableDetailsArticles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPTable getTableau(float totalWidth, String[] titres, int[] widths, int alignement, float borderWidth) {
        try {
            PdfPTable tableau = new PdfPTable(widths.length);
            if (totalWidth != -1) {
                tableau.setTotalWidth(totalWidth);
            } else {
                tableau.setTotalWidth(PageSize.A4.getWidth() - 72);
            }
            tableau.setLockedWidth(true);
            tableau.setWidths(widths);
            tableau.setHorizontalAlignment(alignement);
            if (titres != null) {
                tableau.setSpacingBefore(3);
                for (String titre : titres) {
                    tableau.addCell(getCelluleTableau(titre, borderWidth, BaseColor.LIGHT_GRAY, null, Element.ALIGN_CENTER, Font_TexteSimple_Gras));
                }
            }

            return tableau;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setLigneTabArticle(PdfPTable tableDetailsArticles, InterfaceArticle article, int i) {
        InterfaceMonnaie Im = Util.getMonnaie(gestionnaireFacture.parametres, article.getIdMonnaie());
        if (Im != null) {
            String monnaie = Im.getCode();
            tableDetailsArticles.addCell(getCelluleTableau("" + (i + 1), 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
            tableDetailsArticles.addCell(getCelluleTableau(article.getNom(), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
            tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(article.getQte()) + "", 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
            tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(article.getPrixUHT_avant_rabais()) + " " + monnaie, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
            tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(article.getRabais()) + " " + monnaie, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
            tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(article.getPrixUHT_apres_rabais()) + " " + monnaie, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
            tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(article.getTvaMontant()) + " " + monnaie, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
            tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(article.getTotalTTC()) + " " + monnaie, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        }

    }

    private void setLigneTabReleve(PdfPTable tableDetailsArticles, String monnaie, String date, String NomArticle, String reference, String periode, int mode, int i, double montant, double reste) {
        tableDetailsArticles.addCell(getCelluleTableau("" + (i + 1), 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableDetailsArticles.addCell(getCelluleTableau(date, 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableDetailsArticles.addCell(getCelluleTableau(NomArticle, 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableDetailsArticles.addCell(getCelluleTableau(reference, 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        if (mode == InterfacePaiement.MODE_BANQUE) {
            tableDetailsArticles.addCell(getCelluleTableau("BANQUE", 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        } else {
            tableDetailsArticles.addCell(getCelluleTableau("CAISSE", 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        }
        tableDetailsArticles.addCell(getCelluleTableau(periode, 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(montant) + " " + monnaie, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(reste) + " " + monnaie, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
    }

    private void setLigneTabEcheance(PdfPTable tableEchances, String nomEcheance, String dateDebut, String dateFin, String progression, int i, double montantDu, double montantPaye) {
        //new String[]{"N°", "Tranche", "Début", "Echéance", "Progession", "Montant dû", "Montant payé"},
        String monnaie = gestionnaireFacture.getParametres().getMonnaieOutPut().getCode();
        tableEchances.addCell(getCelluleTableau("" + (i + 1), 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableEchances.addCell(getCelluleTableau(nomEcheance, 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableEchances.addCell(getCelluleTableau(dateDebut, 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableEchances.addCell(getCelluleTableau(dateFin, 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableEchances.addCell(getCelluleTableau(progression, 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableEchances.addCell(getCelluleTableau(Util.getMontantFrancais(montantDu) + " " + monnaie, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableEchances.addCell(getCelluleTableau(Util.getMontantFrancais(montantPaye) + " " + monnaie, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
    }

    private void setDerniereLigneTabEcheance(PdfPTable tableEchances, double totalDu, double totalPaye) {
        //{"N°", "Tranche", "Début", "Echéance", "Progession", "Montant dû", "Montant reçu"},
        String monnaie = gestionnaireFacture.getParametres().getMonnaieOutPut().getCode();
        tableEchances.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableEchances.addCell(getCelluleTableau("Total", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableEchances.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableEchances.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableEchances.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableEchances.addCell(getCelluleTableau(Util.getMontantFrancais(totalDu) + " " + monnaie, 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableEchances.addCell(getCelluleTableau(Util.getMontantFrancais(totalPaye) + " " + monnaie, 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
    }

    private void setLignesTabSynthese(PdfPTable tableau, float borderwidth, String monnaie, double totaRabais, double totalHt, double totalTva, double totalTTC, double totalPaye, double totalSolde) {
        if (totaRabais != 0) {
            tableau.addCell(getCelluleTableau("Rabais", borderwidth, BaseColor.WHITE, BaseColor.RED, Element.ALIGN_LEFT, Font_TexteSimple_Italique));
            tableau.addCell(getCelluleTableau("- " + Util.getMontantFrancais(totaRabais) + " " + monnaie, borderwidth, BaseColor.WHITE, BaseColor.RED, Element.ALIGN_RIGHT, Font_TexteSimple_Italique));
        }
        tableau.addCell(getCelluleTableau("Montant HT", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(Util.getMontantFrancais(totalHt) + " " + monnaie, borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        tableau.addCell(getCelluleTableau("Tva (16%)", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(Util.getMontantFrancais(totalTva) + " " + monnaie, borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        tableau.addCell(getCelluleTableau("Montant TTC", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableau.addCell(getCelluleTableau(Util.getMontantFrancais(totalTTC) + " " + monnaie, borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));

        if (totalPaye != 0) {
            tableau.addCell(getCelluleTableau("Montant payé", borderwidth, BaseColor.WHITE, BaseColor.RED, Element.ALIGN_LEFT, Font_TexteSimple_Italique));
            tableau.addCell(getCelluleTableau("- " + Util.getMontantFrancais(totalPaye) + " " + monnaie, borderwidth, BaseColor.WHITE, BaseColor.RED, Element.ALIGN_RIGHT, Font_TexteSimple_Italique));
        }

        tableau.addCell(getCelluleTableau("Solde global", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableau.addCell(getCelluleTableau(Util.getMontantFrancais(totalSolde) + " " + monnaie, borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));

    }

    private void setDetailsBanque(PdfPTable tableau, float borderwidth) {
        if (this.gestionnaireFacture != null) {
            InterfaceEntreprise entreprise = this.gestionnaireFacture.getParametres().getEntreprise();
            if (entreprise != null) {
                if (entreprise.getBanque().trim().length() != 0) {
                    tableau.addCell(getCelluleTableau("Banque :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                    tableau.addCell(getCelluleTableau(entreprise.getBanque() + "", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_petit_Gras));
                }
                if (entreprise.getIntituleCompte().trim().length() != 0) {
                    tableau.addCell(getCelluleTableau("Intitulé du compte :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                    tableau.addCell(getCelluleTableau(entreprise.getIntituleCompte() + "", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_petit_Gras));
                }
                if (entreprise.getNumeroCompte().trim().length() != 0) {
                    tableau.addCell(getCelluleTableau("N° de compte :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                    tableau.addCell(getCelluleTableau(entreprise.getNumeroCompte(), borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_petit_Gras));
                }
                if (entreprise.getCodeSwift().trim().length() != 0) {
                    tableau.addCell(getCelluleTableau("Code Swift :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                    tableau.addCell(getCelluleTableau(entreprise.getCodeSwift(), borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_petit_Gras));
                }
                if (entreprise.getIban().trim().length() != 0) {
                    tableau.addCell(getCelluleTableau("IBAN :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                    tableau.addCell(getCelluleTableau(entreprise.getIban(), borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_petit_Gras));
                }
            } else {
                setDefaultDetailsBancaires(tableau, borderwidth);
            }
        } else {
            setDefaultDetailsBancaires(tableau, borderwidth);
        }
    }

    private void setDefaultDetailsBancaires(PdfPTable tableau, float borderwidth) {
        tableau.addCell(getCelluleTableau("Banque :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau("EquityBank Congo SA", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));

        tableau.addCell(getCelluleTableau("Intitulé du compte :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau("UAP RDC Sarl", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));

        tableau.addCell(getCelluleTableau("N° de compte :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau("00018000010267415120011", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));

        tableau.addCell(getCelluleTableau("Code Swift :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau("PRCBCDKI", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));

    }

    private void setDerniereLigneTabArticle(PdfPTable tableDetailsArticles, double punit1, double rabais, double punit2, double mntTva, double totalTTC) {
        String monnaie = gestionnaireFacture.getParametres().getMonnaieOutPut().getCode();
        tableDetailsArticles.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableDetailsArticles.addCell(getCelluleTableau("Total", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableDetailsArticles.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(punit1) + " " + monnaie, 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(rabais) + " " + monnaie, 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(punit2) + " " + monnaie, 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(mntTva) + " " + monnaie, 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(totalTTC) + " " + monnaie, 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
    }

    private void setDerniereLigneTabReleve(PdfPTable tableDetailsArticles, String monnaie, double montant, double reste) {
        tableDetailsArticles.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableDetailsArticles.addCell(getCelluleTableau("Total", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableDetailsArticles.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableDetailsArticles.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableDetailsArticles.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableDetailsArticles.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(montant) + " " + monnaie, 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableDetailsArticles.addCell(getCelluleTableau(" ", 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
    }

    private PdfPCell getCelluleTableau(String texte, float BorderWidth, BaseColor background, BaseColor textColor, int alignement, Font font) {
        PdfPCell cellule = new PdfPCell();
        cellule.setBorderWidth(BorderWidth);
        if (background != null) {
            cellule.setBackgroundColor(background);
        } else {
            cellule.setBackgroundColor(BaseColor.WHITE);
        }
        if (textColor != null) {
            font.setColor(textColor);
        } else {
            font.setColor(BaseColor.BLACK);
        }
        cellule.setHorizontalAlignment(alignement);
        cellule.setPhrase(getPhrase(texte, font));
        return cellule;
    }

    private void setTableauDetailsBancaires() {
        try {
            PdfPTable tableBanque = getTableau(
                    400f,
                    null,
                    new int[]{100, 300},
                    Element.ALIGN_LEFT,
                    0
            );
            setDetailsBanque(tableBanque, 0);
            Font_TexteSimple_Italique.setColor(BaseColor.BLACK);
            document.add(getParagraphe("Référence bancaire", Font_TexteSimple_petit, Element.ALIGN_LEFT));
            document.add(tableBanque);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    
    Facture
    Facture pro forma
    Bon de Commande
    Bon d'entrée en Stock
    Bon de sortie du Stock
    
     */
    private void setContenuDeLaPage() throws Exception {
        if (sortiesFacture != null) {
            sortiesFacture.getEcouteurEnregistrement().onUploading("Construction du contenu...");
        }
        setLogoEtDetailsEntreprise();
        setTitreEtDateDocument(isRecu);
        setDetailsEleves();
        if (isRecu == false) {
            setTableauDetailsArticles();
            ajouterLigne(1);
            setTableauSynthese();
            if (this.gestionnaireFacture.isImprimerPlanPaiement() == true) {
                if (this.gestionnaireFacture.getModeleListePaiement().getListeData().isEmpty() == false) {
                    setTableauDetailsPlanPaiementEchelonne();
                }
            }
            if (this.gestionnaireFacture.isImprimerRelever() == true) {
                if (this.gestionnaireFacture.getModeleListePaiement().getListeData().isEmpty() == false) {
                    setTableauDetailsPaiementsRecus(this.gestionnaireFacture.getModeleListePaiement().getListeData());
                }
            }
        } else {
            //C'est ici que les détails sur le recu seront affichés
            setTableauDetailsPaiementsRecus(this.gestionnaireFacture.getPaiementsSelected());
        }

        ajouterLigne(1);
        setSignataire();
        setLigneSeparateur();
        setTableauDetailsBancaires();
        setLigneSeparateur();
        setBasDePage();
        if (sortiesFacture != null) {
            sortiesFacture.getEcouteurEnregistrement().onUploading("Finalisation...");
        }
    }

    private void setLigneSeparateur() {
        try {
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            document.add(linebreak);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] a) {
        //Exemple
        DocumentPDF docpdf = new DocumentPDF(null, ACTION_OUVRIR, false, null);
    }

}
