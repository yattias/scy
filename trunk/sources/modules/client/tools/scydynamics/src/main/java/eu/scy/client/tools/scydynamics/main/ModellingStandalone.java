package eu.scy.client.tools.scydynamics.main;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.*;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class ModellingStandalone extends JFrame implements WindowListener {

	private final static Logger LOGGER = Logger.getLogger(ModellingStandalone.class.getName());
	private ModelEditor editor;

	public ModellingStandalone() {
		super("SCYDynamics - standalone version");
		this.addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editor = new ModelEditor(getProperties());
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(editor, BorderLayout.CENTER);
		this.setSize(600, 400);
		this.pack();
		this.setVisible(true);
	}

	private Properties getProperties() {
		File confFile = new File("scydynamics.properties");
		Properties props = ModelEditor.getDefaultProperties();
		try {
			LOGGER.log(Level.INFO, "ModellingStandalone.getProperties(). expecting file at {0}", confFile.getAbsolutePath());
			if (confFile.exists()) {
				props.load(new FileInputStream(confFile));
			}
		} catch (IOException ex) {
			LOGGER.warning(ex.getMessage());
		}
		props.put("show.popouttabs", "true");
		props.put("editor.qualitative", "false");
		props.put("editor.export_to_sqv", "false");
		LOGGER.info("current props:");
		LOGGER.log(Level.INFO, "{0}", props);
		return props;
	}

	public static void main(String[] args) {
		new ModellingStandalone();
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		//editor.getActionLogger().logSimpleAction("exit_application");
		int n = JOptionPane.showConfirmDialog(this,	"Do you want to save the model\nbefore exiting?",	"exiting...",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			editor.getFileToolbar().saveAs();
			editor.getActionLogger().close();
			System.exit(0);
		}
	}

@Override
public void windowClosed(WindowEvent e) {
	// TODO Auto-generated method stub

}

@Override
public void windowIconified(WindowEvent e) {
	// TODO Auto-generated method stub

}

@Override
public void windowDeiconified(WindowEvent e) {
	// TODO Auto-generated method stub

}

@Override
public void windowActivated(WindowEvent e) {
	// TODO Auto-generated method stub

}

@Override
public void windowDeactivated(WindowEvent e) {
	// TODO Auto-generated method stub

}

//    class WindowEventHandler extends WindowAdapter {
//
//    	
//	@Override
//	public void windowClosing(WindowEvent evt) {
//	    //editor.getActionLogger().logSimpleAction("exit_application");
//		int n = JOptionPane.showConfirmDialog(
//			    this,
//			    "This file already exists.\n"
//			    + "Overwrite?\n",
//			    "File exist",
//			    JOptionPane.YES_NO_OPTION);
//		if (n == JOptionPane.YES_OPTION) {
//			saveSketches(pages, fc.getSelectedFile());
//	    editor.getActionLogger().close();
//	    System.exit(0);
//	}
//    }
}
