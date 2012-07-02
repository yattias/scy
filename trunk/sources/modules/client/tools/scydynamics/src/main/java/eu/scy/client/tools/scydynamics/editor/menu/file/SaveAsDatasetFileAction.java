package eu.scy.client.tools.scydynamics.editor.menu.file;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.editor.Util;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetRow;

@SuppressWarnings("serial")
public class SaveAsDatasetFileAction extends AbstractAction {
	
	private final static Logger debugLogger = Logger.getLogger(SaveAsDatasetFileAction.class.getName());
	private final Icon smallIcon = Util.getImageIcon("saveas16.png");
	private ModelEditor editor;
	private final static String LINE_SEP = ",";

	public SaveAsDatasetFileAction(ModelEditor editor) {
		super();
		this.editor = editor;
		putValue(Action.NAME, "Save as dataset...");
		putValue(Action.SMALL_ICON, smallIcon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			DataSet dataSet = editor.getDataSet();
			if (dataSet.getValues() == null || dataSet.getValues().isEmpty()) {
				JOptionPane.showMessageDialog(null, "To save a dataset, you need some\nvalues in the table.");
				return;
			}
			JFileChooser fc = new JFileChooser();
			fc.addChoosableFileFilter(new XMLFileFilter());
			fc.addChoosableFileFilter(new CSVFileFilter());
			int returnVal = fc.showSaveDialog(editor);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String fileName = fc.getSelectedFile().getAbsolutePath();
				if (fc.getFileFilter() instanceof XMLFileFilter) {
					saveDatasetAsXml(fileName, dataSet);
				} else if (fc.getFileFilter() instanceof CSVFileFilter) {
					saveDatasetAsCsv(fileName, dataSet);
				}
			}
		} catch (Exception ex) {
			debugLogger.severe(ex.getMessage());
			JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(editor),
				    "The model could not be stored:\n"+ex.getMessage(),
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
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
