package eu.scy.client.tools.scydynamics.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class DataSetFileChooser extends JFileChooser implements PropertyChangeListener {
	
	public DataSetFileChooser() {
		this.addChoosableFileFilter(new CSVFileFilter());
		this.addChoosableFileFilter(new XMLFileFilter());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// System.out.println(evt);
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
