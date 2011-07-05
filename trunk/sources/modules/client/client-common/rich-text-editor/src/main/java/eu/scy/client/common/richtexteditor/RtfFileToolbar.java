package eu.scy.client.common.richtexteditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.print.Printable;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.jnlp.ServiceManager;
import javax.jnlp.FileSaveService;
import javax.jnlp.FileOpenService;
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
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.parser.RtfParser;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.util.Date;
import javax.jnlp.FileContents;

/**
* File toolbar for rich text editor component.
*/
public class RtfFileToolbar extends JToolBar implements ActionListener {
    private static final Logger logger = Logger.getLogger("eu.scy.client.common.richtexteditor.RtfFileToolbar");
    private final String imagesLocation = "/eu/scy/client/common/richtexteditor/images/";
    private ImageIcon saveIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_save_changes.png"));
    private ImageIcon printIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_print.png"));
    private ImageIcon pdfIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_pdf.png"));
    private ImageIcon openIcon = new ImageIcon(this.getClass().getResource(imagesLocation+"Button_open-qt.png"));
    private ResourceBundleWrapper messages = new ResourceBundleWrapper(this);
    public JButton openButton = new JButton(openIcon);
    public JButton saveButton = new JButton(saveIcon);
    public JButton printButton = new JButton(printIcon);
    public JButton pdfButton = new JButton(pdfIcon);

    private RichTextEditor editorPanel;
    private FileSaveService fileSaveService = null;
    private FileOpenService fileOpenService = null;
    private PrintService printService = null;
    private JFileChooser fc = new JFileChooser();
    private boolean html;

    /**
    * Creates file toolbar for rich text editor component
    * @param richTextEditor reference to main component
    * @param authoring is rich text editor component in authoring mode (open button showed/not showed)
    */
    public RtfFileToolbar(RichTextEditor richTextEditor, boolean authoring) {
        super();
        this.editorPanel = richTextEditor;
        this.html = richTextEditor.isHTML();
        initUI(authoring);
    }

    /**
     * Gets rich text editor main component
     * @return rich text editor main component
     */
    public RichTextEditor getRichTextEditor() {
        return editorPanel;
    }

    /*
     * Show error if it occurs in some place in rich text editor component.
     */
    private void showError(String messageID, Throwable e) {
        logger. error(messages.getString(messageID), e);
        System.err.println(messages.getString(messageID) + " " + new Date());
        e.printStackTrace(System.err);
        JOptionPane.showMessageDialog(editorPanel, messages.getString(messageID) +
            ":\nMessage: " + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
    }

    /*
     * Initializes user interface.
     */
    private void initUI(boolean authorMode) {
        this.setOrientation(SwingConstants.HORIZONTAL);
        this.setFloatable(false);

        openButton.setActionCommand("openfile");
        openButton.addActionListener(this);
        openButton.setToolTipText(messages.getString("open"));
        if (authorMode) {
            this.add(openButton);
        }

        saveButton.setActionCommand("savefile");
        saveButton.addActionListener(this);
        if (html) {
            saveButton.setToolTipText(messages.getString("saveFile")+" html...");
        } else {
            saveButton.setToolTipText(messages.getString("saveFile")+" rtf...");
        }
        this.add(saveButton);

        printButton.setActionCommand("print");
        printButton.addActionListener(this);
        printButton.setToolTipText(messages.getString("print"));
        this.add(printButton);

        // no good converter from RTF to PDF - so removed PDF generation
        if (!html && false) {
            pdfButton.setActionCommand("pdf");
            pdfButton.addActionListener(this);
            pdfButton.setToolTipText(messages.getString("pdf"));
            this.add(pdfButton);
        }
    }

    /*
     * Reads bytes from file.
     * @param file file from where to read bytes
     */
    private byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = null;
        try{
           is = new FileInputStream(file);
           long length = file.length();
           if (length > Integer.MAX_VALUE) {
               new Exception ("Maximum filesize reached.");
           }
           byte[] bytes = new byte[(int) length];
           int offset = 0;
           int numRead = 0;
           while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
               offset += numRead;
           }
           if (offset < bytes.length) {
               throw new IOException("Could not completely read file " + file.getName());
           }
           return bytes;
        }
        finally{
          if (is!=null){
             is.close();
          }
        }
    }

    /*
     * Assignes loaded text to main rich text editor component.
     */
    private void bytesToEditor(byte[] bytes, String extension) {
        String content = new String(bytes);
        if (extension.equals("rtf")) {
            editorPanel.setText(content);
        } else {
            editorPanel.setPlainText(content);
        }
    }

    /*
     * Load a file into the editing area.
     * @param is An input stream pointing to the desired file.
     * @param extension rtf or txt or html file to load
     */
    public void loadFile(InputStream is, String extension) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int length;
        while ( (length = is.read(buffer)) > 0 ) {
            baos.write(buffer, 0, length);
        }
        baos.flush();
        baos.close();
        bytesToEditor(baos.toByteArray(), extension);
    }

    /*
     * Opens file and sets text in main rich text editor component.
     */
    private void openFile() {
        try {
            if (fileOpenService==null) {
                fileOpenService = (FileOpenService)ServiceManager.
                    lookup("javax.jnlp.FileOpenService");
            }
            String[] extensions = {"rtf","txt"};
            FileContents fileContents =
                fileOpenService.openFileDialog(".", extensions);
            if (fileContents!=null) {
                String extension = "txt";
                if (fileContents.getName().endsWith(".rtf"))
                    extension = "rtf";
                loadFile( fileContents.getInputStream(), extension );
            }
        } catch(UnavailableServiceException use) {
            logger.info("No JNLP FileOpenService found, trying other method for opening");
            int returnVal = fc.showOpenDialog(editorPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String extension = "txt";
                if (fc.getSelectedFile().getName().endsWith(".rtf"))
                    extension = "rtf";
                try {
                    bytesToEditor( getBytesFromFile(fc.getSelectedFile()), extension );
                } catch(IOException ioe) {
                    showError("openError", ioe);
                }
            } else {
                logger.info("Open command cancelled by user.");
            }
        } catch(IOException ioe) {
            showError("openError", ioe);
        }
    }

    /*
     * Saves file.
     * @param extensions possible extensions for saving (txt, rtf, html)
     * @param content text to save into file
     */
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

    /*
     * Saves PDF file.
     * @param extensions possible extensions for saving
     * @param content content to save into file
     */
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

    /*
     * Opens file. Can be invoked also from tool.
     * In SCY Lab there is button in tool title bar which executes this command.
     */
    public void openFileAction() {
        openFile();
        if (editorPanel.getRichTextEditorLogger() != null)
            editorPanel.getRichTextEditorLogger().logFileAction(RichTextEditorLogger.OPEN);

    }

    /*
     * Saves file. Can be invoked also from tool.
     * In SCY Lab there is button in tool title bar which executes this command.
     */
    public void saveFileAction() {
        String[] xtnsRtf = {"rtf"};
        String[] xtnsHtml = {"html"};
        if (html) {
            saveFile(xtnsHtml, editorPanel.getRtfText());
        } else {
            saveFile(xtnsRtf, editorPanel.getRtfText());
        }
        if (editorPanel.getRichTextEditorLogger() != null)
            editorPanel.getRichTextEditorLogger().logFileAction(RichTextEditorLogger.SAVE_RTF);
    }

    /*
     * Prints text. Can be invoked also from tool.
     * In SCY Lab there is button in tool title bar which executes this command.
     */
    public void printAction() {
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
    }

    /*
     * Saves PDF file. Can be invoked also from tool.
     * In SCY Lab there is button in tool title bar which executes this command.
     */
    public void pdfAction() {
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

    /*
     * Implementation of ActionListener interface.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("openfile")) {
            openFileAction();
        } else if (e.getActionCommand().equals("savefile")) {
            saveFileAction();
        } else if (e.getActionCommand().equals("print")) {
            printAction();
        } else if (e.getActionCommand().equals("pdf")) {
            pdfAction();
        }
    }
}
