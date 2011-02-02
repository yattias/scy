package eu.scy.client.tools.scydynamics.main;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.*;
import colab.um.JColab;
import colab.um.tools.JTools;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import java.util.logging.Logger;

public class ModellingStandalone extends JFrame {

        private final static Logger LOGGER = Logger.getLogger(ModellingStandalone.class.getName());
	private ModelEditor editor;

	public ModellingStandalone() {
		super("SCYDynamics - standalone version");
		this.addWindowListener(new WindowEventHandler());
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
			if (confFile.exists())
				props.load(new FileInputStream(confFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		props.put("show.popouttabs", "true");
		LOGGER.info("current props:");
		LOGGER.log(Level.INFO, "{0}", props);
		return props;
	}

	public static void main(String[] args) {
		new ModellingStandalone();
	}

	class WindowEventHandler extends WindowAdapter {
		public void windowClosing(WindowEvent evt) {
			editor.getActionLogger().logSimpleAction("exit_application");
			editor.getActionLogger().close();
			System.exit(0);
		}
	}
}