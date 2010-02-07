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
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import java.awt.print.PageFormat;
import java.awt.print.Book;
import java.awt.print.PrinterJob;
import java.awt.print.PrinterException;
/*
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.parser.RtfParser;
import java.io.FileOutputStream;
 */

public class RtfFileToolbar extends JToolBar implements ActionListener {

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

		JButton button = new JButton(messages.getString("saveFile"));
		button.setActionCommand("savefile");
		button.addActionListener(this);
		this.add(button);

		button = new JButton(messages.getString("print"));
		button.setActionCommand("print");
		button.addActionListener(this);
		this.add(button);

		button = new JButton(messages.getString("pdf"));
		button.setActionCommand("pdf");
		button.addActionListener(this);
		this.add(button);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("savefile")) {
            String[] xtns = { "rtf" };
            try {
                if (fileSaveService==null) {
                    fileSaveService = (FileSaveService)ServiceManager.
                        lookup("javax.jnlp.FileSaveService");
                }
                String s = editorPanel.getRtfText();
                byte[] b = s.getBytes();
                ByteArrayInputStream bais = new ByteArrayInputStream(b);
                fileSaveService.saveFileDialog(
                    ".", xtns, bais, "example." + xtns[0] );
            } catch(UnavailableServiceException use) {
                logger.log(Level.INFO, "No JavaWebStart FileSaveService found, trying other method for saving");
                int returnVal = fc.showSaveDialog(editorPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    FileWriter fw = null;
                    try {
                        File file = fc.getSelectedFile();
                        fw = new FileWriter(file);
                        fw.write(editorPanel.getRtfText());
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
/*
            Document document = new Document();
            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("c:\\kaido\\proov.pdf"));
                document.open();
                RtfParser parser = new RtfParser(null);
                StringBuffer stringBuffer = new StringBuffer(editorPanel.getRtfText());
                parser.convertRtfDocument(new ByteArrayInputStream(stringBuffer.toString().getBytes("UTF-8")), document);
                document.close();
/*
   5. import java.io.FileInputStream;
   6. import java.io.FileNotFoundException;
   7. import java.io.FileOutputStream;
   8. import java.io.IOException;
   9.
  10. public class ConvertRTFToPDF {
  11.
  12.
  13. public static void main(String[] args) {
  14.  String inputFile = "sample.rtf";
  15.  String outputFile = "sample_converted.pdf";
  16.
  17.  // create a new document
  18.  Document document = new Document();
  19.
  20.  try {
  21.      // create a PDF writer to save the new document to disk
  22.      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile));
  23.      // open the document for modifications
  24.      document.open();
  25.
  26.      // create a new parser to load the RTF file
  27.      RtfParser parser = new RtfParser(null);
  28.      // read the rtf file into a compatible document
  29.      parser.convertRtfDocument(new FileInputStream(inputFile), document);
  30.
  31.      // save the pdf to disk
  32.      document.close();
  33.
  34.      System.out.println("Finished");
  35.
  36.  } catch (DocumentException e) {
  37.      e.printStackTrace();
  38.  } catch (FileNotFoundException e) {
  39.      e.printStackTrace();
  40.  } catch (IOException e) {
  41.      e.printStackTrace();
  42.  }
  43. }
  44.
  45. }
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
            }
 */
        }
	}
}
