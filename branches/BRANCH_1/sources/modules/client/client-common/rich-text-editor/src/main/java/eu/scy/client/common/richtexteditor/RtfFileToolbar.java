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
import org.apache.log4j.Logger;
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
import javax.swing.JOptionPane;
import java.util.Date;

/**
* File toolbar for rich text editor component.
*/
public class RtfFileToolbar extends JToolBar implements ActionListener {
    private static final Logger logger = Logger.getLogger("eu.scy.client.common.richtexteditor.RtfFileToolbar");
    private final String imagesLocation = "/eu/scy/client/common/richtexteditor/images/";
    private ImageIcon saveIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_save_changes.png"));
    private ImageIcon printIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_print.png"));
    private ImageIcon pdfIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_pdf.png"));
    private ResourceBundle messages = ResourceBundle.getBundle("eu.scy.client.common.richtexteditor.RichTextEditor");

    private RichTextEditor editorPanel;
    private FileSaveService fileSaveService = null;
    private PrintService printService = null;
    private JFileChooser fc = new JFileChooser();
    private boolean html;

    public RtfFileToolbar(RichTextEditor richTextEditor) {
        super();
        this.editorPanel = richTextEditor;
        this.html = richTextEditor.isHTML();
        initUI();
    }

    public RichTextEditor getRichTextEditor() {
        return editorPanel;
    }

    private void showError(String messageID, Throwable e) {
        logger. error(messages.getString(messageID), e);
        System.err.println(messages.getString(messageID) + " " + new Date());
        e.printStackTrace(System.err);
        JOptionPane.showMessageDialog(editorPanel, messages.getString(messageID) +
            ":\nMessage: " + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
    }

    private void initUI() {
        this.setOrientation(SwingConstants.HORIZONTAL);
        this.setFloatable(false);

        JButton button = new JButton(saveIcon);
        button.setActionCommand("savefile");
        button.addActionListener(this);
        if (html) {
            button.setToolTipText(messages.getString("saveFile")+" html...");
        } else {
            button.setToolTipText(messages.getString("saveFile")+" rtf...");
        }
        this.add(button);

        button = new JButton(printIcon);
        button.setActionCommand("print");
        button.addActionListener(this);
        button.setToolTipText(messages.getString("print"));
        this.add(button);

        if (!html) {
            button = new JButton(pdfIcon);
            button.setActionCommand("pdf");
            button.addActionListener(this);
            button.setToolTipText(messages.getString("pdf"));
            this.add(button);
        }
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
            logger.info("No JNLP FileSaveService found, trying other method for saving");
            int returnVal = fc.showSaveDialog(editorPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                FileWriter fw = null;
                try {
                    File file = fc.getSelectedFile();
                    fw = new FileWriter(file);
                    fw.write(content);
                } catch (IOException ex) {
                    showError("saveError", ex);
                } finally {
                    try {
                        fw.close();
                    } catch (IOException ex) {
                        showError("saveCloseError", ex);
                    }
                }
            } else {
                logger.info("Save command cancelled by user.");
            }
        } catch(IOException ioe) {
            showError("saveError", ioe);
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
            logger.info("No JNLP FileSaveService found, trying other method for saving");
            int returnVal = fc.showSaveDialog(editorPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(fc.getSelectedFile());
                    content.writeTo(fos);
                } catch (IOException ex) {
                    showError("saveError", ex);
                } finally {
                    try {
                        fos.close();
                    } catch (IOException ex) {
                        showError("saveCloseError", ex);
                    }
                }
            } else {
                logger.info("Save command cancelled by user.");
            }
        } catch(IOException ioe) {
            showError("saveError", ioe);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("savefile")) {
            String[] xtnsRtf = {"rtf"};
            String[] xtnsHtml = {"html"};
            if (html) {
                saveFile(xtnsHtml, editorPanel.getRtfText());
            } else {
                saveFile(xtnsRtf, editorPanel.getRtfText());
            }
            if (editorPanel.getRichTextEditorLogger() != null)
                editorPanel.getRichTextEditorLogger().logFileAction(RichTextEditorLogger.SAVE_RTF);
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
                logger.info("No JNLP PrintService found, trying other method for printing");
                PrinterJob printJob = PrinterJob.getPrinterJob();
                printJob.setPrintable(editorPanel);
                if (printJob.printDialog())
                  try {
                    printJob.print();
                  } catch(PrinterException pe) {
                    showError("printError", pe);
                  }
            }
            if (editorPanel.getRichTextEditorLogger() != null)
                editorPanel.getRichTextEditorLogger().logFileAction(RichTextEditorLogger.PRINT);
        } else if (e.getActionCommand().equals("pdf")) {
            String[] xtns = { "pdf" };
            try {
                Document document = new Document();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                PdfWriter writer = PdfWriter.getInstance(document, bos);
                document.open();
                RtfParser parser = new RtfParser(null);
                parser.convertRtfDocument(new ByteArrayInputStream(editorPanel.getRtfText().getBytes(Charset.forName("UTF-8"))), document);
                document.close();
                savePDF(xtns, bos);
            } catch (Exception ex) {
                showError("pdfError", ex);
            }
            if (editorPanel.getRichTextEditorLogger() != null)
                editorPanel.getRichTextEditorLogger().logFileAction(RichTextEditorLogger.PDF);
        }
    }
}
