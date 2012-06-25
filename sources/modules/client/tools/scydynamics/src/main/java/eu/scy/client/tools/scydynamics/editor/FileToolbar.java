package eu.scy.client.tools.scydynamics.editor;

import java.awt.Dimension;
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

import javax.swing.Box;
import javax.swing.JButton;
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

import colab.um.draw.JdLink;
import colab.um.xml.model.JxmModel;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.store.SCYDynamicsStore.StoreType;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import java.io.BufferedWriter;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class FileToolbar extends JToolBar implements ActionListener {

	private final static Logger debugLogger = Logger.getLogger(FileToolbar.class.getName());
	private final static String LINE_SEP = ",";
	private ModelEditor editor;
	private String filename = null;
	private Frame parentFrame = new Frame();
	private FileFilter csvFileFilter;
	private FileFilter xmlFileFilter;
	private JComboBox modeBox;
	private ResourceBundleWrapper bundle;
	private JButton phaseButton = null;

	public FileToolbar(ModelEditor editor, ResourceBundleWrapper newBundle) {
		super(JToolBar.HORIZONTAL);
		this.bundle = newBundle;
		this.editor = editor;
		this.csvFileFilter = new CSVFileFilter();
		this.xmlFileFilter = new XMLFileFilter();
		setFloatable(false);
		add(Util.createJButton(bundle.getString("EDITOR_NEW"), "new", "new16.png", this));
		add(Util.createJButton(bundle.getString("EDITOR_OPEN"), "open", "open16.png", this));
		add(Util.createJButton(bundle.getString("EDITOR_SAVE"), "save", "save16.png", this));
		add(Util.createJButton(bundle.getString("EDITOR_SAVEAS"), "saveas", "saveas16.png", this));
		if (Boolean.parseBoolean(editor.getProperties().getProperty("editor.saveasdataset"))) {
			this.addSeparator();
			add(Util.createJButton(bundle.getString("EDITOR_SAVEAS_DATASET"), "saveasdataset", "saveas16.png", this));
		}
		this.addSeparator();
		modeBox = new JComboBox(ModelEditor.Mode.values());
		modeBox.setSelectedItem(editor.getMode());
		modeBox.addActionListener(this);
		if (Boolean.parseBoolean(editor.getProperties().getProperty("editor.modes_selectable"))) {
			add(new JLabel("Phase: "));
			add(modeBox);
		}
		
		if (Boolean.parseBoolean(editor.getProperties().getProperty("showPhaseChangeButton"))) {
			this.add(Box.createHorizontalGlue());
			ActionListener phaseListener = new PhaseListener(editor);
			phaseButton = Util.createJButton("next phase", "nextPhase", "nextphase.png", phaseListener);
			add(phaseButton);
			add(Util.createJButton("reset model", "reset", "resetmodel.png", phaseListener));
		}
	}
	
	public JButton getPhaseButton() {
		return this.phaseButton;
	}

	private String getFilename() {
		return filename;
	}

	public void setFilename(String newName) {
		this.filename = newName;
	}
	

	public void updateMode() {
		modeBox.setSelectedItem(editor.getMode());
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
		debugLogger.log(Level.INFO, "saving dataset(csv) to {0}", fileName);
		try {
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);
			String headerLine = new String();
			for (DataSetColumn column : dataSet.getHeaders().get(0).getColumns()) {
				headerLine = headerLine.concat("\"").concat(column.getSymbol()).concat("\"").concat(LINE_SEP);
			}
			if (headerLine.endsWith(LINE_SEP)) {
				headerLine = headerLine.substring(0, headerLine.length() - 1);
			}
			headerLine = headerLine.concat("\n");
			out.write(headerLine);
			String rowLine;
			for (DataSetRow row : dataSet.getValues()) {
				rowLine = new String();
				for (String valueString : row.getValues()) {
					rowLine = rowLine.concat(valueString).concat(LINE_SEP);
				}
				if (rowLine.endsWith(LINE_SEP)) {
					rowLine = rowLine.substring(0, rowLine.length() - 1);
				}
				rowLine = rowLine.concat("\n");
				out.write(rowLine);
			}
			out.close();
		} catch (Exception e) {
			debugLogger.warning(e.getMessage());
		}
	}

	private void saveDatasetAsXml(String fileName, DataSet dataSet) {
		if (!fileName.endsWith(".xml")) {
			fileName = fileName + ".xml";
		}
		debugLogger.log(Level.INFO, "saving dataset(xml) to {0}", fileName);
		try {
			Document doc = new Document(dataSet.toXML());
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			FileWriter writer = new FileWriter(new File(fileName));
			out.output(doc, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			debugLogger.warning(e.getMessage());
		}
	}
	
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() instanceof JComboBox) {
			editor.setMode(((JComboBox) evt.getSource()).getSelectedItem().toString());
		}
		if (evt.getActionCommand().equals("new")) {
			editor.setNewModel();
			editor.doAutosave(StoreType.ON_NEW);
			editor.getActionLogger().logSimpleAction(ModellingLogger.MODEL_NEW);
			this.setFilename(null);
		} else if (evt.getActionCommand().equals("saveas")) {
			try {
				editor.getSCYDynamicsStore().saveAsModel();
			} catch (Exception ex) {
				debugLogger.severe(ex.getMessage());
				JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(editor),
					    "The model could not be stored:\n"+ex.getMessage(),
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
			}
		} else if (evt.getActionCommand().equals("saveasdataset")) {
			saveasDataset();
		} else if (evt.getActionCommand().equals("save")) {
			try {
				editor.getSCYDynamicsStore().saveModel();
			} catch (Exception ex) {
				debugLogger.severe(ex.getMessage());
				JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(editor),
					    "The model could not be stored:\n"+ex.getMessage(),
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
			}
			
		} else if (evt.getActionCommand().equals("open")) {
			try {
				editor.getSCYDynamicsStore().loadModel();
			} catch (Exception ex) {
				debugLogger.severe(ex.getMessage());
				JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(editor),
					    "The model could not be loaded:\n"+ex.getMessage(),
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
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
