/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.GenerateurPDF;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gateway
 */
public class DocumentPDF {

    public static String OUPUT_S2B = "Output_S2B";
    private int TypePreuve = -1;
    private PREUVE preuve_caisse = null;
    private BANQUE preuve_Banque = null;
    private String logo = "";
    private boolean isBanque = false;

    private static int numeroPage = 1;
    private String NomfichierPreuve = null;
    private String titre = "";
    private Document document = new Document();
    private static Font Font_Titre1 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.BLACK);
    private static Font Font_Titre2 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
    private static Font Font_Titre3 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK);
    private static Font Font_TexteSimple = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK);
    private static Font Font_TexteSimple_petit = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL, BaseColor.BLACK);
    private static Font Font_TexteSimple_Gras = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK);
    private static Font Font_TexteSimple_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK);
    private static Font Font_TexteSimple_Gras_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLDITALIC, BaseColor.BLACK);

    public DocumentPDF(PREUVE preuve) {
        this.isBanque = false;
        this.TypePreuve = preuve.getType();
        if (Principal.parametre_encours != null) {
            logo = Principal.parametre_encours.getLogo();
        } else {
            logo = "Photos/logo.png";
        }
        this.titre = "Reçu N°" + preuve.getNumero();
        this.NomfichierPreuve = PREUVE.FICHIER_PREUVE;
        this.preuve_caisse = preuve;
        construirePDF();
        try {
            OuvrirFichier();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DocumentPDF(BANQUE preuve) {
        this.isBanque = true;
        this.TypePreuve = preuve.getType();
        if (Principal.parametre_encours != null) {
            logo = Principal.parametre_encours.getLogo();
        } else {
            logo = "Photos/logo.png";
        }
        if (this.TypePreuve == PREUVE.TYPE_BON_ENTREE_CAISSE) {
            this.titre = "BORDEREAU DE VERSEMENT N°" + preuve.getNumero();
        } else {
            this.titre = "BORDEREAU DE RETRAIT N°" + preuve.getNumero();
        }
        this.NomfichierPreuve = BANQUE.FICHIER_BORDEREAU;
        this.preuve_Banque = preuve;
        construirePDF();
        try {
            OuvrirFichier();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OuvrirFichier() {
        File fic = new File(NomfichierPreuve);
        if (fic != null && fic.exists() == true) {
            try {
                Desktop.getDesktop().open(fic);
            } catch (IOException ex) {
                Logger.getLogger(DocumentPDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void ImprimerFichier() {
        File fic = new File(NomfichierPreuve);
        if (fic != null && fic.exists() == true) {
            try {
                Desktop.getDesktop().print(fic);
            } catch (IOException ex) {
                Logger.getLogger(DocumentPDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void construirePDF() {
        try {
            //System.out.println(""+this.NomfichierPreuve+".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(this.NomfichierPreuve));
            document.open();
            addMetaData();
            addPage();
            document.close();
            //updateManifeste();
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de l'initialisation du document pdf. " + NomfichierPreuve);
            e.printStackTrace();
        }
    }

    private void addMetaData() {
        document.addTitle("RAPPORT S2B");
        document.addSubject("Etat");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("S2B. Simple.Intuitif");
        document.addCreator("SULA BOSIO Serge");
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private void addEmptyLine(int number) throws Exception {
        Paragraph paragraphe = new Paragraph();
        for (int i = 0; i < number; i++) {
            paragraphe.add(new Paragraph(" "));
        }
        document.add(paragraphe);
    }

    private void addTitlePage() throws Exception {
        Paragraph preface = new Paragraph();
        if (Principal.parametre_encours != null) {
            if(isBanque == true){
                preface.add(getParagraphe("DATE : " + (preuve_Banque.getDateEnregistrement().split(" ")[0]), Font_Titre3, false, false, true));
            }else{
                preface.add(getParagraphe("DATE : " + (preuve_caisse.getDateEnregistrement().split(" ")[0]), Font_Titre3, false, false, true));
            }
            preface.add(getParagraphe(this.titre, Font_Titre1, false, true, false));
        } else {
            if(isBanque == true){
                preface.add(getParagraphe("DATE : " + (preuve_Banque.getDateEnregistrement().split(" ")[0]), Font_Titre3, false, false, true));
            }else{
                preface.add(getParagraphe("DATE : " + (preuve_caisse.getDateEnregistrement().split(" ")[0]), Font_Titre3, false, false, true));
            }
            preface.add(getParagraphe(this.titre, Font_Titre1, false, true, false));
        }
        addEmptyLine(preface, 1);
        document.add(preface);
        this.numeroPage++;
    }

    private void addPiedPage() throws Exception {
        Paragraph preface = new Paragraph();
        if (isBanque == false) {
            if(preuve_caisse.isBON_SORTIE_CAISSE()==true){
                preface.add(getParagraphe(""
                    + "Signature Coordonnateur                               "
                    + "                                                      "
                    + "Signature Bénéficiaire"
                    + "", Font_TexteSimple_Gras, false, true, false));
            }else{
                preface.add(getParagraphe(""
                    + "Signature Famille                               "
                    + "                                                      "
                    + "Signature Caissier(ère)"
                    + "", Font_TexteSimple_Gras, false, true, false));
            }
            
        } else {
            if(preuve_Banque.isBORDEREAU_RETRAIT()){
                preface.add(getParagraphe(""
                    + "Signature Coordonnateur                               "
                    + "                                                      "
                    + "Signature Caissier(ère)"
                    + "", Font_TexteSimple_Gras, false, true, false));
            }else{
                preface.add(getParagraphe(""
                    + "Signature Famille                               "
                    + "                                                      "
                    + "Signature Caissier(ère)"
                    + "", Font_TexteSimple_Gras, false, true, false));
            }
            
        }

        addEmptyLine(preface, 1);
        document.add(preface);
    }

    private Paragraph getParagraphe(String texte, Font font, boolean isleft, boolean isCenter, boolean isRight) {
        Paragraph par = new Paragraph(texte, font);
        if (isleft == true) {
            par.setAlignment(Element.ALIGN_LEFT);
        } else if (isCenter == true) {
            par.setAlignment(Element.ALIGN_CENTER);
        } else if (isRight == true) {
            par.setAlignment(Element.ALIGN_RIGHT);
        }
        return par;
    }

    private void addImage() {
        try {
            int[] width = new int[2];
            width[0] = 400;
            width[1] = 1460;

            PdfPTable tableEntere = new PdfPTable(2);
            tableEntere.setHorizontalAlignment(Element.ALIGN_CENTER);

            Image Imglogo = Image.getInstance(logo);
            Imglogo.scaleAbsoluteWidth(80);
            Imglogo.scaleAbsoluteHeight(80);
            PdfPCell cell = new PdfPCell(Imglogo);

            cell.setBorderColor(BaseColor.BLACK);
            cell.setBorderWidthRight(5);
            cell.setBorderColorRight(BaseColor.WHITE);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(3);
            tableEntere.addCell(cell);

            PdfPCell cell2 = new PdfPCell();
            if (Principal.parametre_encours != null) {
                String nomEcole = (Principal.parametre_encours.getEntreprise().toUpperCase()).replaceAll(";", "\n") + "";
                String adresse = (Principal.parametre_encours.getSiege().toUpperCase()).replaceAll(";", "\n") + "";

                cell2.addElement(getParagraphe(nomEcole.toUpperCase() + "\n", Font_Titre2, false, true, false));
                cell2.addElement(getParagraphe(adresse.toLowerCase() + "\n", Font_TexteSimple_petit, false, true, false));
            } else {
                cell2.addElement(getParagraphe("COLLEGE CARTESIEN DE KINSHASA\n", Font_Titre2, false, true, false));
                cell2.addElement(getParagraphe("C.C.K\n", Font_Titre1, false, true, false));
                cell2.addElement(getParagraphe("ECOLE INTERNATIONALE BILINGUE\n", Font_Titre3, false, true, false));
                cell2.addElement(getParagraphe("7e RUE LIMETE - Q. INDUSTRIEL\n", Font_TexteSimple_petit, false, true, false));
                cell2.addElement(getParagraphe("E-mail : ecolebilingue@yahoo.fr\n", Font_TexteSimple_petit, false, true, false));
                cell2.addElement(getParagraphe("Tél : 081 508 6526 - 081 508 8711 - 099 897 2146 - 099 020 2744 - 099 994 2280 - 099 993 9650", Font_TexteSimple_petit, false, true, false));
            }

            cell2.setBorderColor(BaseColor.BLACK);
            cell2.setBorderWidthLeft(5);
            cell2.setBorderColorLeft(BaseColor.WHITE);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setPadding(3);
            tableEntere.addCell(cell2);

            tableEntere.setWidths(width);
            document.add(tableEntere);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPage() throws Exception {
        addImage();
        addTitlePage();

        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setLockedWidth(false);

        Paragraph preface = new Paragraph();
        if (preuve_caisse != null) {
            if (this.TypePreuve == PREUVE.TYPE_BON_ENTREE_CAISSE) {
                addLigne(table, "MONTANT : ", "USD " + preuve_caisse.getMontant() + "\n");
                addLigne(table, "RECU DE : ", preuve_caisse.getQuipaye() + "\n");
                addLigne(table, "LA SOMME DE (En lettres) : ", "" + Constantes.getLettres(preuve_caisse.getMontant(), "Dollars Américains") + "\n");
                addLigne(table, "POUR LE PAIEMENT DE : ", preuve_caisse.getMotif().replaceAll(";", ";\n") + "\n");
                addLigne(table, "SOLDE : ", "USD " + preuve_caisse.getSolde() + "\n");
            } else {
                addLigne(table, "MONTANT EN CHIFFRES : ", "USD " + preuve_caisse.getMontant() + "\n");
                addLigne(table, "ORDONNATEUR : ", preuve_caisse.getQuipaye() + "\n");
                addLigne(table, "LA SOMME DE (En lettres) : ", "" + Constantes.getLettres(preuve_caisse.getMontant(), "Dollars Américains") + "\n");
                addLigne(table, "POUR LE PAIEMENT DE : ", preuve_caisse.getMotif().replaceAll(";", ";\n") + "\n");
            }
        } else if (preuve_Banque != null) {
            COMPTE compte = getCompte(preuve_Banque.getIdCompte());
            if (compte == null) {
                return;
            }
            if (this.TypePreuve == BANQUE.TYPE_BORDEREAU_VERSEMENT) {
                addLigne(table, "VERSEMENT : ", "USD " + preuve_Banque.getMontant() + " \n");
                addLigne(table, "N° DU COMPTE : ", compte.getNumero() + "\n");
                addLigne(table, "TITULAIRE : ", compte.getNomTitulaire() + "\n");
                addLigne(table, "DEPOSITAIRE : ", preuve_Banque.getQuipaye() + "\n");
                addLigne(table, "MONTANT (En lettres) : ", "" + Constantes.getLettres(preuve_Banque.getMontant(), "Dollars Américains") + "\n");
                addLigne(table, "MOTIF : ", preuve_Banque.getMotif().replaceAll(";", ";\n") + "\n");
                addLigne(table, "Solde : ", "USD " + preuve_Banque.getSolde() + "\n");
            } else {
                addLigne(table, "RETRAIT : ", "USD " + preuve_Banque.getMontant() + " \n");
                addLigne(table, "N° DU COMPTE : ", compte.getNumero() + "\n");
                addLigne(table, "TITULAIRE : ", compte.getNomTitulaire() + "\n");
                addLigne(table, "BENEFICIAIRE : ", preuve_Banque.getQuiRecoit() + "\n");
                addLigne(table, "MONTANT (En lettres) : ", "" + Constantes.getLettres(preuve_Banque.getMontant(), "Dollars Américains") + "\n");
                addLigne(table, "MOTIF : ", preuve_Banque.getMotif().replaceAll(";", ";\n") + "\n");
            }
        }

        document.add(table);

        //addEmptyLine(1);
        addPiedPage();
        preface.add(getParagraphe("N.B : Les frais versés ne sont ni remboursables ni transférables.\n\n", Font_TexteSimple_Italique, true, false, false));

        //preface.add(getParagraphe("Copyright © S2B  -  2014. Tout droit réservé. (+243) 84 480 3514", Font_TexteSimple_Italique_Spec, false, true, false));
        //preface.add(getParagraphe("Simple.Intuitif", Font_TexteSimple_Italique_Spec, false, true, false));
        //addEmptyLine(preface, 5);
        document.add(preface);
    }

    private COMPTE getCompte(int id) {
        COMPTE compte = Principal.ext.getCOMPTE(id);
        return compte;
    }

    private void addLigne(PdfPTable table, String titreChamp, String ValeurChamp) throws Exception {
        table.addCell(getCellule(titreChamp, Font_TexteSimple, false, false, true));
        PdfPCell cell = getCellule(ValeurChamp, Font_TexteSimple_Gras_Italique, true, false, false);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setBorderColor(BaseColor.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setMinimumHeight(15f);
        cell.setPadding(5);
        table.addCell(cell);
        float[] width = new float[2];
        width[0] = 300;
        width[1] = 700;
        table.setTotalWidth(width);
    }

    private PdfPCell getCellule(String texte, Font font, boolean isLeft, boolean isCenter, boolean isRight) {
        PdfPCell cellule = new PdfPCell(getParagraphe(texte, font, isLeft, isCenter, isRight));
        if (isLeft == true) {
            cellule.setHorizontalAlignment(Element.ALIGN_LEFT);
        } else if (isCenter == true) {
            cellule.setHorizontalAlignment(Element.ALIGN_CENTER);
        } else if (isRight == true) {
            cellule.setHorizontalAlignment(Element.ALIGN_RIGHT);
        }
        cellule.setNoWrap(false);
        cellule.setBorderColor(BaseColor.WHITE);
        cellule.setBorderWidthBottom(1);
        cellule.setBorderWidthLeft(1);
        cellule.setBorderWidthRight(1);
        cellule.setBorderWidthTop(1);
        return cellule;
    }

    public static void main(String[] a) {
        //String nomFichier = "0__2014-09-06__250.0__UAP1406181075335__SULA BOSIO SERGE__1.;2.;3.;4.pdf";
        String nomCeluiQuiRecoit = "INSTITUT SAINT JOSEPH";
        String nomCeluiQuiPaye = "SULA BOSIO Serge";
        String dateEnreg = "06/09/2014 00:00:00";
        double montant = 600;
        String motif = "PREMIER SEMESTRE, ANNEE 2014 - 2015; - FRAIS PREMIER SEMESTRE 2014 = USD 100;";

        //PREUVE preuvePayement = new PREUVE(PREUVE.TYPE_BON_ENTREE_CAISSE, nomEntreprise, dateEnreg, montant, numero, part, motif);
        //System.out.println("FUTURE NOM DU FICHIER = "+preuvePayement.getNomFichier());
        BON_SORTIE_CAISSE Bentree = new BON_SORTIE_CAISSE(nomCeluiQuiRecoit, dateEnreg, montant, nomCeluiQuiPaye, motif, 0);
        DocumentPDF docpdf = new DocumentPDF(Bentree);
    }

}
