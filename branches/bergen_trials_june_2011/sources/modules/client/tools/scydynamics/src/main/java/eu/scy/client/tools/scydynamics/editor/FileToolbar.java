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
import java.util.logging.Level;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
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
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import java.io.BufferedWriter;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class FileToolbar extends JToolBar implements ActionListener {

	private final static Logger LOGGER = Logger.getLogger(FileToolbar.class.getName());
	private final static String LINE_SEP = ",";
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
		this.addSeparator();
		// testing the modes
		//add(new JLabel("mode: "));
		String[] modes = {"black_box", "clear_box", "modelling"};
		JComboBox modeBox = new JComboBox(modes);
		modeBox.setSelectedItem(editor.getMode().toString().toLowerCase());
		modeBox.addActionListener(this);
		//add(modeBox);
	}

	public void load(String filename) {
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

	private void save(String fileName) {
		LOGGER.info("saving model to "+fileName);
		this.setFilename(fileName);
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(new StringReader(editor.getModelXML()));
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			FileWriter writer = new FileWriter(fileName);
			out.output(doc, writer);
			writer.flush();
			writer.close();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveAs() {
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
			JOptionPane.showMessageDialog(null, "To save a dataset, you need some\nvalues in the table.");
			return;
		}
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(xmlFileFilter);
		fc.addChoosableFileFilter(csvFileFilter);
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fileName = fc.getSelectedFile().getAbsolutePath();
			if (fc.getFileFilter() instanceof XMLFileFilter) {
				saveDatasetAsXml(fileName, dataSet);
			} else if (fc.getFileFilter() instanceof CSVFileFilter) {
				saveDatasetAsCsv(fileName, dataSet);
			}
		}
	}

	private void saveDatasetAsCsv(String fileName, DataSet dataSet) {
		if (!fileName.endsWith(".csv")) {
			fileName = fileName + ".csv";
		}
		LOGGER.log(Level.INFO, "saving dataset(csv) to {0}", fileName);
		try {
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);
			String headerLine = new String();
			for (DataSetColumn column: dataSet.getHeaders().get(0).getColumns()) {
				headerLine = headerLine.concat("\"").concat(column.getSymbol()).concat("\"").concat(LINE_SEP);
			}
			if (headerLine.endsWith(LINE_SEP)) {
				headerLine = headerLine.substring(0, headerLine.length()-1);
			}
			headerLine = headerLine.concat("\n");
			out.write(headerLine);
			String rowLine;
			for (DataSetRow row: dataSet.getValues()) {
				rowLine = new String();
				for (String valueString: row.getValues()) {
					rowLine = rowLine.concat(valueString).concat(LINE_SEP);
				}
				if (rowLine.endsWith(LINE_SEP)) {
					rowLine = rowLine.substring(0, rowLine.length()-1);
				}
				rowLine = rowLine.concat("\n");
				out.write(rowLine);
			}
			out.close();
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
		}
	}

	private void saveDatasetAsXml(String fileName, DataSet dataSet) {
		if (!fileName.endsWith(".xml")) {
			fileName = fileName + ".xml";
		}
		LOGGER.log(Level.INFO, "saving dataset(xml) to {0}", fileName);
		try {
			Document doc = new Document(dataSet.toXML());
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			FileWriter writer = new FileWriter(new File(fileName));
			out.output(doc, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			LOGGER.warning(e.getMessage());
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
		if (evt.getSource() instanceof JComboBox) {
			editor.setMode(((JComboBox)evt.getSource()).getSelectedItem().toString());
		}
		if (evt.getActionCommand().equals("new")) {
			editor.setNewModel();
			this.setFilename(null);
		} else if (evt.getActionCommand().equals("saveas")) {
			saveAs();
		} else if (evt.getActionCommand().equals("saveasdataset")) {
			saveasDataset();
		} else if (evt.getActionCommand().equals("save")) {
			if (getFilename() != null) {
				save(getFilename());
			} else {
				saveAs();
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
			return "CSV dataset";
		}
	}

	public class XMLFileFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.getName().toLowerCase().endsWith(".xml");
		}

		@Override
		public String getDescription() {
			return "XML dataset";
		}
	}
}
