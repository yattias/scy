package nl.utwente.gw.modelling.main;
import javax.swing.*;
import nl.utwente.gw.modelling.editor.ModelEditor;
import colab.um.JColab;
import colab.um.tools.JTools;

public class ModellingStandalone extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3066655673856117223L;
	private ModelEditor editor;

	public ModellingStandalone() {
		super("Modelling standalone alpha");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//new JTools(JColab.JCOLABAPP_RESOURCES, JColab.JCOLABSYS_RESOURCES);
		
		editor = new ModelEditor();
		
		this.getContentPane().add(editor);
		this.setSize(600,400);
		this.setVisible(true);		
	}

	public static void main(String[] args) {
		new ModellingStandalone();
	}

}
