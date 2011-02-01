package eu.scy.client.tools.scydynamics.main;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.swing.*;
import colab.um.JColab;
import colab.um.tools.JTools;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;

public class ModellingStandalone extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3066655673856117223L;
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
			// System.out.println("ModellingStandalone.getProperties(). expecting file at "+confFile.getAbsolutePath());
			if (confFile.exists())
				props.load(new FileInputStream(confFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		props.put("show.popouttabs", "true");
		// System.out.println("current props:");
		// System.out.println(props);
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
