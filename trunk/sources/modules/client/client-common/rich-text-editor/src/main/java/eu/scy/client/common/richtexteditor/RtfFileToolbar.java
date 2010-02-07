package eu.scy.client.common.richtexteditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.print.Printable;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import java.util.ResourceBundle;
import javax.jnlp.ServiceManager;
import javax.jnlp.FileSaveService;
import javax.jnlp.PrintService;
import javax.jnlp.UnavailableServiceException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import java.awt.print.PageFormat;
import java.awt.print.Book;
import java.awt.print.PrinterJob;
import java.awt.print.PrinterException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.parser.RtfParser;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import javax.swing.ImageIcon;

public class RtfFileToolbar extends JToolBar implements ActionListener {
    private final String imagesLocation = "/eu/scy/client/common/richtexteditor/images/";
    private ImageIcon saveIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_save_changes.png"));
    private ImageIcon printIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_print.png"));
    private ImageIcon pdfIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_pdf.png"));
    private static final Logger logger = Logger.getLogger(RtfFileToolbar.class.getSimpleName());
    private ResourceBundle messages = ResourceBundle.getBundle("eu.scy.client.common.richtexteditor.RtfFileToolbar");

    private RichTextEditor editorPanel;
    private FileSaveService fileSaveService = null;
    private PrintService printService = null;
    private JFileChooser fc = new JFileChooser();

    public RtfFileToolbar(RichTextEditor richTextEditor) {
        super();
        this.editorPanel = richTextEditor;
        initUI();
    }

    public RichTextEditor getRichTextEditor() {
        return editorPanel;
    }

    private void initUI() {
        this.setOrientation(SwingConstants.HORIZONTAL);
        this.setFloatable(false);

        JButton button = new JButton(saveIcon);
        button.setActionCommand("savefile");
        button.addActionListener(this);
        button.setToolTipText(messages.getString("saveFile"));
        this.add(button);

        button = new JButton(printIcon);
        button.setActionCommand("print");
        button.addActionListener(this);
        button.setToolTipText(messages.getString("print"));
        this.add(button);

        button = new JButton(pdfIcon);
        button.setActionCommand("pdf");
        button.addActionListener(this);
        button.setToolTipText(messages.getString("pdf"));
        this.add(button);
    }

    private void saveFile(String[] extensions, String content) {
        try {
            if (fileSaveService==null) {
                fileSaveService = (FileSaveService)ServiceManager.
                    lookup("javax.jnlp.FileSaveService");
            }
            byte[] b = content.getBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            fileSaveService.saveFileDialog(
                ".", extensions, bais, "SCY_Lab_Text." + extensions[0] );
        } catch(UnavailableServiceException use) {
            logger.log(Level.INFO, "No JavaWebStart FileSaveService found, trying other method for saving");
            int returnVal = fc.showSaveDialog(editorPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                FileWriter fw = null;
                try {
                    File file = fc.getSelectedFile();
                    fw = new FileWriter(file);
                    fw.write(content);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        fw.close();
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                logger.log(Level.INFO, "Save command cancelled by user.");
            }
        } catch(IOException ioe) {
            logger.log(Level.SEVERE, null, ioe);
        }
    }

    private void savePDF(String[] extensions, ByteArrayOutputStream content) {
        try {
            if (fileSaveService==null) {
                fileSaveService = (FileSaveService)ServiceManager.
                    lookup("javax.jnlp.FileSaveService");
            }
            fileSaveService.saveFileDialog(
                ".", extensions, new ByteArrayInputStream(content.toByteArray()),
                "SCY_Lab_Text." + extensions[0] );
        } catch(UnavailableServiceException use) {
            logger.log(Level.INFO, "No JavaWebStart FileSaveService found, trying other method for saving");
            int returnVal = fc.showSaveDialog(editorPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(fc.getSelectedFile());
                    content.writeTo(fos);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        fos.close();
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                logger.log(Level.INFO, "Save command cancelled by user.");
            }
        } catch(IOException ioe) {
            logger.log(Level.SEVERE, null, ioe);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("savefile")) {
            String[] xtns = { "rtf" };
            saveFile(xtns, editorPanel.getRtfText());
        } else if (e.getActionCommand().equals("print")) {
            try {
                if (printService==null) {
                    printService = (PrintService)ServiceManager.
                        lookup("javax.jnlp.PrintService");
                    PageFormat pf = printService.showPageFormatDialog(printService.getDefaultPage());
                    Book book = new Book();
                    book.append((Printable) editorPanel,pf);
                    printService.print(book);
                }
            } catch (UnavailableServiceException doh) {
                logger.log(Level.INFO, "No JavaWebStart PrintService found, trying other method for saving");
                PrinterJob printJob = PrinterJob.getPrinterJob();
                printJob.setPrintable(editorPanel);
                if (printJob.printDialog())
                  try {
                    printJob.print();
                  } catch(PrinterException pe) {
                    logger.log(Level.SEVERE, null, pe);
                  }
            }
        } else if (e.getActionCommand().equals("pdf")) {
            String[] xtns = { "pdf" };
            Document document = new Document();
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                PdfWriter writer = PdfWriter.getInstance(document, bos);
                document.open();
                RtfParser parser = new RtfParser(null);
                parser.convertRtfDocument(new ByteArrayInputStream(editorPanel.getRtfText().getBytes(Charset.forName("UTF-8"))), document);
                document.close();
                savePDF(xtns, bos);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }
}
