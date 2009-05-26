package eu.scy.client.tools.scydynamics.main;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

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
		super("Modelling standalone alpha");
		this.addWindowListener(new WindowEventHandler());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//new JTools(JColab.JCOLABAPP_RESOURCES, JColab.JCOLABSYS_RESOURCES);		
		editor = new ModelEditor();
		this.getContentPane().setLayout(new BorderLayout());
		
		this.getContentPane().add(editor, BorderLayout.CENTER);
		this.setSize(600,400);
		this.pack();
		this.setVisible(true);		
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
