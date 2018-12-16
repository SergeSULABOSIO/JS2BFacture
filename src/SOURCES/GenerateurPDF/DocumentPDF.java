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
        this.NomfichierPreuve = nomDuDocument + ".pdf";
        this.logo = cheminLogo;

        //Definition du type du document
        switch (this.type_doc) {
            case TYPE_FACTURE:
                this.titre = "FACTURE N°" + numeroFacture;
                break;
            case TYPE_FACTURE_ET_RELEVE_DE_COMPTE:
                this.titre = "FACTURE N°" + numeroFacture;
                break;
            case TYPE_RELEVE_DE_COMPTE:
                this.titre = "RELEVE DE COMPTE N°" + numeroFacture;
                break;
            case TYPE_FACTURE_PROFORMA:
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

    private void setTitreEtDateDocument() throws Exception {
        Paragraph preface = new Paragraph();
        preface.add(getParagraphe("Date: " + this.dateFacturation.toLocaleString(), Font_Titre3, Element.ALIGN_RIGHT));
        preface.add(getParagraphe(this.titre, Font_Titre1, Element.ALIGN_CENTER));
        document.add(preface);
        this.numeroPage++;
    }

    private void addPiedPage() throws Exception {
        Paragraph preface = new Paragraph();
        preface.add(getParagraphe(""
                + "Signature Famille                               "
                + "                                                      "
                + "Signature Caissier(ère)"
                + "", Font_TexteSimple_Gras, Element.ALIGN_CENTER));
        addEmptyLine(preface, 1);
        document.add(preface);
    }

    private Paragraph getParagraphe(String texte, Font font, int alignement) {
        Paragraph par = new Paragraph(texte, font);
        par.setAlignment(alignement);
        return par;
    }

    private void setLogoEtDetailsEntreprise() {
        try {
            int nbColonnes = 2;
            PdfPTable tableauEnteteFacture = new PdfPTable(nbColonnes);
            int[] dimensionsWidthHeight = {320, 1460};
            tableauEnteteFacture.setWidths(dimensionsWidthHeight);
            tableauEnteteFacture.setHorizontalAlignment(Element.ALIGN_LEFT);

            //CELLULE DU LOGO DE L'ENTREPRISE
            PdfPCell celluleLogoEntreprise = null;
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

            celluleDetailsEntreprise.addElement(getParagraphe("UAP RDC Sarl, Courtier d'Assurances n°0189", Font_Titre2, Element.ALIGN_LEFT));
            celluleDetailsEntreprise.addElement(getParagraphe("Avenue de la Gombe, Kinshasa/Gombe", Font_TexteSimple_petit, Element.ALIGN_LEFT));
            celluleDetailsEntreprise.addElement(getParagraphe("https://www.aib-brokers.com | info@aib-brokers.com | (+243)84 480 35 14 - (+243)82 87 27 706", Font_TexteSimple_petit, Element.ALIGN_LEFT));
            celluleDetailsEntreprise.addElement(getParagraphe("RCC : CDF/KIN/2015-1245\nID. NAT : 0112487789\nNIF : 012245", Font_TexteSimple_petit, Element.ALIGN_LEFT));

            tableauEnteteFacture.addCell(celluleDetailsEntreprise);

            //On insère le le tableau entete (logo et détails de l'entreprise) dans la page
            document.add(tableauEnteteFacture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setClientEtSesCoordonnees() {
        try {
            PdfPTable tableDetailsClient = new PdfPTable(2);
            int[] dimensionsWidthHeight = {320, 1460};
            tableDetailsClient.setWidths(dimensionsWidthHeight);
            tableDetailsClient.setHorizontalAlignment(Element.ALIGN_LEFT);
            
            //Colonne des titres
            PdfPCell celluleTitres = new PdfPCell();
            celluleTitres.setPadding(2);
            celluleTitres.setBorderWidth(0);
            //celluleTitres.setBorderColor(BaseColor.BLACK);
            celluleTitres.addElement(getParagraphe("Nom du Client :\nContacts :\nAutres détails :", Font_TexteSimple_Gras, Element.ALIGN_RIGHT));
            
            //Colonne des valeurs = détails sur le client
            PdfPCell celluleDonnees = new PdfPCell();
            celluleDonnees.setPadding(2);
            celluleDonnees.setBorderWidth(0);
            //celluleDonnees.setBorderColor(BaseColor.BLACK);
            celluleDonnees.addElement(getParagraphe("SULA BOSIO SERGE\n(+243)844803514, (+243)828727706\nClasse : 1e A, Ecole 42 - Informatique de Gestion - Université de Kinshasa - RDC", Font_TexteSimple_Italique, Element.ALIGN_LEFT));
            
            tableDetailsClient.addCell(celluleTitres);
            tableDetailsClient.addCell(celluleDonnees);
            
            document.add(tableDetailsClient);
            addEmptyLine(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setContenuDeLaPage() throws Exception {
        setLogoEtDetailsEntreprise();//ok
        setTitreEtDateDocument();//ok
        setClientEtSesCoordonnees();
        
        addPiedPage();
        Paragraph preface = new Paragraph();
        preface.add(getParagraphe("N.B : Les frais versés ne sont ni remboursables ni transférables.\n\n", Font_TexteSimple_Italique, Element.ALIGN_LEFT));
        document.add(preface);
    }

    private void addLigne(PdfPTable table, String titreChamp, String ValeurChamp) throws Exception {
        table.addCell(getCellule(titreChamp, Font_TexteSimple, Element.ALIGN_RIGHT));
        PdfPCell cell = getCellule(ValeurChamp, Font_TexteSimple_Gras_Italique, Element.ALIGN_LEFT);
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

    private PdfPCell getCellule(String texte, Font font, int alignement) {
        PdfPCell cellule = new PdfPCell(getParagraphe(texte, font, alignement));
        cellule.setHorizontalAlignment(alignement);
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
