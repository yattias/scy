package eu.scy.client.tools.scydynamics.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class DataSetFileChooser extends JFileChooser implements PropertyChangeListener {

	private final static Logger LOGGER = Logger.getLogger(DataSetFileChooser.class.getName());

	public DataSetFileChooser() {
		this.addChoosableFileFilter(new CSVFileFilter());
		this.addChoosableFileFilter(new XMLFileFilter());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		DataSetFileChooser.LOGGER.log(Level.INFO, "{0}", evt);
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
