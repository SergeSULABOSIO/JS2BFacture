/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.GenerateurPDF;

import SOURCES.Utilitaires.Util;
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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gateway
 */
public class DocumentPDF {

    public static String OUPUT_S2B = "Output_S2B";
    //private PREUVE preuve_caisse = null;
    private String logo = "";

    private static int numeroPage = 1;
    private String NomfichierPreuve = null;
    private String titre = "";
    private Document document = new Document();
    private static Font Font_Titre1 = null;
    private static Font Font_Titre2 = null;
    private static Font Font_Titre3 = null;
    private static Font Font_TexteSimple = null;
    private static Font Font_TexteSimple_petit = null;
    private static Font Font_TexteSimple_Gras = null;
    private static Font Font_TexteSimple_Italique = null;
    private static Font Font_TexteSimple_Gras_Italique = null;

    public DocumentPDF() {
        initFont();
        this.logo = "Photos/logo.png";
        this.titre = "Reçu N°001245454CVDCDKIN";
        this.NomfichierPreuve = "Facture S2B";
        //this.preuve_caisse = preuve;
        construirePDF();
        try {
            OuvrirFichier();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFont() {
        Font_Titre1 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.BLACK);
        Font_Titre2 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
        Font_Titre3 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK);
        Font_TexteSimple = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK);
        Font_TexteSimple_petit = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL, BaseColor.BLACK);
        Font_TexteSimple_Gras = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK);
        Font_TexteSimple_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK);
        Font_TexteSimple_Gras_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLDITALIC, BaseColor.BLACK);
    }

    public void OuvrirFichier() {
        File fic = new File(NomfichierPreuve);
        if (fic.exists() == true) {
            try {
                Desktop.getDesktop().open(fic);
            } catch (IOException ex) {
                Logger.getLogger(DocumentPDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void ImprimerFichier() {
        File fic = new File(NomfichierPreuve);
        if (fic.exists() == true) {
            try {
                Desktop.getDesktop().print(fic);
            } catch (IOException ex) {
                Logger.getLogger(DocumentPDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void construirePDF() {
        try {
            PdfWriter.getInstance(document, new FileOutputStream(this.NomfichierPreuve));
            document.open();
            addMetaData();
            addPage();
            document.close();
        } catch (Exception e) {
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
        preface.add(getParagraphe("DATE : " + (new Date().toLocaleString()), Font_Titre3, false, false, true));
        preface.add(getParagraphe(this.titre, Font_Titre1, false, true, false));
        addEmptyLine(preface, 1);
        document.add(preface);
        this.numeroPage++;
    }

    private void addPiedPage() throws Exception {
        Paragraph preface = new Paragraph();
        preface.add(getParagraphe(""
                + "Signature Famille                               "
                + "                                                      "
                + "Signature Caissier(ère)"
                + "", Font_TexteSimple_Gras, false, true, false));
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

            File ficLogo = new File(logo);
            if (ficLogo.exists() == true) {
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
            }

            PdfPCell cell2 = new PdfPCell();
            cell2.addElement(getParagraphe("COLLEGE CARTESIEN DE KINSHASA\n", Font_Titre2, false, true, false));
            cell2.addElement(getParagraphe("C.C.K\n", Font_Titre1, false, true, false));
            cell2.addElement(getParagraphe("ECOLE INTERNATIONALE BILINGUE\n", Font_Titre3, false, true, false));
            cell2.addElement(getParagraphe("7e RUE LIMETE - Q. INDUSTRIEL\n", Font_TexteSimple_petit, false, true, false));
            cell2.addElement(getParagraphe("E-mail : ecolebilingue@yahoo.fr\n", Font_TexteSimple_petit, false, true, false));
            cell2.addElement(getParagraphe("Tél : 081 508 6526 - 081 508 8711 - 099 897 2146 - 099 020 2744 - 099 994 2280 - 099 993 9650", Font_TexteSimple_petit, false, true, false));

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
        addLigne(table, "MONTANT : ", "USD 100\n");
        addLigne(table, "RECU DE : ", "SERGE SULA BOSIO\n");
        addLigne(table, "LA SOMME DE (En lettres) : ", "" + Util.getLettres(100, "Dollars Américains") + "\n");
        addLigne(table, "POUR LE PAIEMENT DE : ", "MINERVALE ET INSCRIPTION" + "\n");
        addLigne(table, "SOLDE : ", "USD 15\n");
        document.add(table);
        addEmptyLine(1);
        addPiedPage();
        preface.add(getParagraphe("N.B : Les frais versés ne sont ni remboursables ni transférables.\n\n", Font_TexteSimple_Italique, true, false, false));
        document.add(preface);
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

        //BON_SORTIE_CAISSE Bentree = new BON_SORTIE_CAISSE(nomCeluiQuiRecoit, dateEnreg, montant, nomCeluiQuiPaye, motif, 0);
        DocumentPDF docpdf = new DocumentPDF();
    }

}
