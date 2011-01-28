package eu.scy.client.tools.scydynamics.editor;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import colab.um.xml.model.JxmModel;
import eu.scy.elo.contenttype.dataset.DataSet;

public class FileToolbar extends JToolBar implements ActionListener {

    private static final long serialVersionUID = -5071178017935714682L;
    private ModelEditor editor;
    private String filename = null;
    private Frame parentFrame = new Frame();
    private FileFilter csvFileFilter;
    private FileFilter xmlFileFilter;

    public FileToolbar(ModelEditor editor) {
        super(JToolBar.HORIZONTAL);
        this.editor = editor;
        this.csvFileFilter = new CSVFileFilter();
        this.xmlFileFilter = new XMLFileFilter();
        setFloatable(false);
        add(Util.createJButton("new", "new", "editorNew", this));
        add(Util.createJButton("open", "open", "editorOpen", this));
        add(Util.createJButton("save", "save", "editorSave", this));
        add(Util.createJButton("save as", "saveas", "editorSave", this));
        this.addSeparator();
        add(Util.createJButton("save as dataset", "saveasdataset", "editorSave", this));
    }

    public void load(String filename) {
        // JxmModel xmlModel = JxmModel.readFileXML(filename);
        // editor.setXmModel(xmlModel);
        JxmModel xmlModel = null;
        Document doc = null;
        SAXBuilder sb = new SAXBuilder();
        try {
            doc = sb.build(new File(filename));
            Element modelElement = null;
            if (doc.getRootElement().getName().equals("model")) {
                // root element is <model>, so everything is fine
                modelElement = doc.getRootElement();
            } else {
                // root element is not <model>, try to find it
                modelElement = findTag(doc.getRootElement(), "model");
            }
            if (modelElement != null) {
                xmlModel = JxmModel.readStringXML(new XMLOutputter(Format.getPrettyFormat()).outputString(modelElement));
                editor.setXmModel(xmlModel);
                this.setFilename(filename);
                editor.getActionLogger().logLoadAction(editor.getXmModel().getXML("", true));
            } else {
                throw new JDOMException(
                        "Couldn't find <model> element in file " + filename);
            }
        } catch (JDOMException e) {
            JOptionPane.showMessageDialog(null,
                    "Could load a model from this file.\nPlease check if it really contains one.");
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error while reading from that file.");
            e.printStackTrace();
        }

    }

    private String getFilename() {
        return filename;
    }

    public void setFilename(String newName) {
        this.filename = newName;

    }

    private void save(String file) {
        editor.getActionLogger().logSimpleAction("save_model", editor.getXmModel().getXML("", true));
        this.setFilename(file);
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(new StringReader(editor.getModelXML()));
            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
            FileWriter writer = new FileWriter(file);
            out.output(doc, writer);
            writer.flush();
            writer.close();
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void saveas() {
        FileDialog dialog = new FileDialog(parentFrame, "Save model to a file...", FileDialog.SAVE);
        dialog.setFile("*.xml");
        dialog.setVisible(true);
        if (dialog.getFile() != null) {
            save(dialog.getDirectory() + dialog.getFile());
        }
    }

    private void saveasDataset() {
        DataSet dataSet = editor.getDataSet();
        if (dataSet.getValues() == null || dataSet.getValues().isEmpty()) {
            JOptionPane.showMessageDialog(null,	"To save a dataset, you need some\nvalues in the table.");
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(xmlFileFilter);

        // DataSetFileChooser fc = new DataSetFileChooser();

        // TODO add the csv stuff
        //fc.addChoosableFileFilter(csvFileFilter);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String fileName = fc.getSelectedFile().getAbsolutePath();
            if (!fileName.endsWith(".xml")) {
                fileName = fileName + ".xml";
            }
            saveDatasetAsXml(fileName, dataSet);
        }
    }

    private void saveDatasetAsXml(String fileName, DataSet dataSet) {
        try {
            Document doc = new Document(dataSet.toXML());
            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
            FileWriter writer = new FileWriter(new File(fileName));
            out.output(doc, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Element findTag(Element root, String tag)
            throws IllegalArgumentException {
        if (root == null) {
            throw new IllegalArgumentException("Element is null.");
        }
        List enumChilds = root.getChildren();
        Iterator iter = enumChilds.iterator();
        while (iter.hasNext()) {
            Element childElement = (Element) iter.next();
            if (childElement.getName().equals(tag)) {
                return childElement;
            }
            try {
                Element foundElement = FileToolbar.findTag(childElement, tag);
                if (foundElement != null) {
                    return foundElement;
                }
            } catch (IllegalArgumentException e) {
            }
        }
        return null;
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("new")) {
            editor.setNewModel();
            editor.getActionLogger().logSimpleAction("new_model");
            this.setFilename(null);
        } else if (evt.getActionCommand().equals("saveas")) {
            saveas();
        } else if (evt.getActionCommand().equals("saveasdataset")) {
            saveasDataset();
        } else if (evt.getActionCommand().equals("save")) {
            if (getFilename() != null) {
                save(getFilename());
            } else {
                saveas();
            }
        } else if (evt.getActionCommand().equals("open")) {
            FileDialog dialog = new FileDialog(parentFrame, "Load model...", FileDialog.LOAD);
            // dialog.setFile("*.xml");
            dialog.setVisible(true);
            if (dialog.getFile() != null) {
                load(dialog.getDirectory() + dialog.getFile());
            }
        }
    }

    public class CSVFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".csv");
        }

        @Override
        public String getDescription() {
            return "csv files";
        }
    }

    public class XMLFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".xml");
        }

        @Override
        public String getDescription() {
            return "xml files";
        }
    }
}
