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

    private String logo = "";
    private int numeroPage = 1;
    private String NomfichierPreuve = null;
    private String titre = "";
    private Document document = new Document();
    private Font Font_Titre1 = null;
    private Font Font_Titre2 = null;
    private Font Font_Titre3 = null;
    private Font Font_TexteSimple = null;
    private Font Font_TexteSimple_petit = null;
    private Font Font_TexteSimple_Gras = null;
    private Font Font_TexteSimple_Italique = null;
    private Font Font_TexteSimple_Gras_Italique = null;
    private Date dateFacturation = null;

    public final static int TYPE_FACTURE = 0;
    public final static int TYPE_FACTURE_ET_RELEVE_DE_COMPTE = 1;
    public final static int TYPE_RELEVE_DE_COMPTE = 2;
    public final static int TYPE_FACTURE_PROFORMA = 3;
    
    private int type_doc = TYPE_FACTURE_ET_RELEVE_DE_COMPTE;

    public DocumentPDF(String nomDuDocument, String cheminLogo, String numeroFacture, int type, Date date) {
        try {
            init(nomDuDocument, cheminLogo, numeroFacture, type, date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(String nomDuDocument, String cheminLogo, String numeroFacture, int type, Date date) {
        parametre_initialisation_fichier(nomDuDocument, cheminLogo, numeroFacture, type, date);
        parametre_construire_fichier();
        parametres_ouvrir_fichier();
    }

    private void parametre_initialisation_fichier(String nomDuDocument, String cheminLogo, String numeroFacture, int type, Date date) {
        //Les titres du document
        this.Font_Titre1 = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD, BaseColor.DARK_GRAY);
        this.Font_Titre2 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
        this.Font_Titre3 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);
        
        //Les textes simples
        this.Font_TexteSimple = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK);
        this.Font_TexteSimple_petit = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL, BaseColor.BLACK);
        this.Font_TexteSimple_Gras = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK);
        this.Font_TexteSimple_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK);
        this.Font_TexteSimple_Gras_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLDITALIC, BaseColor.BLACK);

        //initialisation des autres attributs
        this.type_doc = type;
        this.NomfichierPreuve = nomDuDocument+".pdf";
        this.logo = cheminLogo;
        
        //Definition du type du document
        switch (this.type_doc) {
            case TYPE_FACTURE :
                this.titre = "FACTURE N°" + numeroFacture;
                break;
            case TYPE_FACTURE_ET_RELEVE_DE_COMPTE :
                this.titre = "FACTURE (+relevé de compte) N°" + numeroFacture;
                break;
            case TYPE_RELEVE_DE_COMPTE :
                this.titre = "RELEVE DE COMPTE N°" + numeroFacture;
                break;
            case TYPE_FACTURE_PROFORMA :
                this.titre = "FACTURE PRO FORMA N°" + numeroFacture;
                break;
            default:
                break;
        }
        
        //date
        this.dateFacturation = date;
    }
    
    private void parametre_construire_fichier() {
        try {
            PdfWriter.getInstance(document, new FileOutputStream(this.NomfichierPreuve));
            this.document.open();
            this.setDonneesBibliographiques();
            this.setContenuDeLaPage();
            this.document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parametres_ouvrir_fichier() {
        File fic = new File(NomfichierPreuve);
        if (fic.exists() == true) {
            try {
                Desktop.getDesktop().open(fic);
            } catch (IOException ex) {
                Logger.getLogger(DocumentPDF.class.getName()).log(Level.SEVERE, null, ex);
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

    private void setContenuDeLaPage() throws Exception {
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
        //Exemple
        DocumentPDF docpdf = new DocumentPDF("Facture S2B", "F://imgTest.png", "00000CDKIN12", DocumentPDF.TYPE_FACTURE_ET_RELEVE_DE_COMPTE, new Date());
    }

}
