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

public class ModellingApplet extends JApplet {

	private ModelEditor editor;

	public void init() {
		JPanel panel = new JPanel();
		editor = new ModelEditor(getProperties());
		panel.setLayout(new BorderLayout());
		panel.add(editor, BorderLayout.CENTER);
		panel.setSize(600, 400);
		panel.setVisible(true);
		this.getContentPane().add(panel);
		
		if (getParameter("file") != null) {
			editor.getFileToolbar().load(getParameter("file"));
		}
	}

	private Properties getProperties() {
		File confFile = new File("scydynamics.properties");
		Properties props = ModelEditor.getDefaultProperties();
		try {
			System.out.println("ModellingStandalone.getProperties(). expecting file at "+confFile.getAbsolutePath());
			if (confFile.exists())
				props.load(new FileInputStream(confFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}

//	public static void main(String[] args) {
//		new ModellingApplet();
//	}
//
//	class WindowEventHandler extends WindowAdapter {
//		public void windowClosing(WindowEvent evt) {
//			editor.getActionLogger().logSimpleAction("exit_application");
//			editor.getActionLogger().close();
//			System.exit(0);
//		}
//	}
}
