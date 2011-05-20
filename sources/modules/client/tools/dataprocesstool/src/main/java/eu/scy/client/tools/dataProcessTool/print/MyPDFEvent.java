/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.print;

import com.lowagie.text.Document;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * user Header and Footer on the PDF pages
 * @author Marjolaine
 */
public class MyPDFEvent extends PdfPageEventHelper {
    /* date */
    private String printDate ;
    /* writer */
    private PdfWriter writer;
    /** A template that will hold the total number of pages. */
    private PdfTemplate tpl;
    /** The font that will be used. */
    private BaseFont helv;

    public MyPDFEvent(PdfWriter writer, String printDate) {
        this.writer = writer;
        this.printDate = printDate ;
    }


    /**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onOpenDocument(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
     */
    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        // initialization of the template
        try {
            // initialization of the template
            tpl = writer.getDirectContent().createTemplate(100, 100);
            tpl.setBoundingBox(new Rectangle(-20, -20, 100, 100));
            // initialization of the font
            helv = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
        }
        catch(Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        // compose the footer
        //String text = "Page " + writer.getPageNumber() + " of ";
        String text = printDate+", "+writer.getPageNumber()+" / ";
        float textSize = helv.getWidthPoint(text, 9);
        float textBase = document.bottom() - 20;
        cb.beginText();
        cb.setFontAndSize(helv, 9);
        
        float adjust = helv.getWidthPoint("0", 9);
        cb.setTextMatrix(document.right() - textSize - adjust, textBase);
        cb.showText(text);
        cb.endText();
        cb.addTemplate(tpl, document.right() - adjust, textBase);

    }

   /**
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onCloseDocument(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
     */
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
       tpl.beginText();
       tpl.setFontAndSize(helv, 9);
       tpl.setTextMatrix(0, 0);
       tpl.showText(Integer.toString(writer.getPageNumber() - 1));
       tpl.endText();
    }

}
