package eu.scy.client.tools.scydynamics.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.editor.menu.EditorMenuBar;
import eu.scy.client.tools.scydynamics.editor.menu.file.ExitAction;
import eu.scy.client.tools.scydynamics.model.ModelUtils;
import eu.scy.client.tools.scydynamics.store.FileStore;

@SuppressWarnings("serial")
public abstract class AbstractModellingStandalone extends JFrame implements WindowListener {
	
	private ModelEditor editor;
	private EditorMenuBar menuBar;
	@SuppressWarnings("unused")
	private final static Logger debugLogger = Logger.getLogger(AbstractModellingStandalone.class.getName());
	
	public AbstractModellingStandalone(String title) {
		super(title);
		Locale.setDefault(new Locale("en"));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		editor = new ModelEditor(getProperties(), getUsername(getProperties()), this);
		editor.setSCYDynamicsStore(new FileStore(editor));
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(editor, BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(900, 700));
		this.pack();
		this.setVisible(true);
		editor.updateTitle();
		menuBar = new EditorMenuBar(editor);
		
		if (Boolean.parseBoolean(editor.getProperties().getProperty("editor.showMenu"))) {
			this.setJMenuBar(menuBar);
		}
		// trick to get the menu showing up 100%
		this.setSize(this.getWidth()+1, this.getHeight());
		this.setSize(this.getWidth()-1, this.getHeight());
	}
	
	public EditorMenuBar getMenu() {
		return this.menuBar;
	}
	
	public static String getUsername(Properties props) {
		String username = "";
		//asking for the username
		if (props != null && props.getProperty("askUsername", "false").equalsIgnoreCase("true")) {	
			username = JOptionPane.showInputDialog("Please enter a username:", System.getProperty("user.name"));
			username = ModelUtils.removeSpecialCharacters(username);
		}
		if (username == null || username.isEmpty()) {
			username = System.getProperty("user.name");
		}
		if (username == null || username.isEmpty()) {
			username = "unknow_user";
		}
		return username;
	}
	
	public abstract Properties getProperties();
	
	public ModelEditor getEditor() {
		return this.editor;
	}
	
	protected void loadOnStart() {
		String loadOnStart = this.getProperties().getProperty("loadOnStart");
		if (loadOnStart == null || loadOnStart.isEmpty()) {
			return;
		} else {
			try {
				getEditor().getSCYDynamicsStore().loadModel(loadOnStart);
				getEditor().getSCYDynamicsStore().setModelName(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		new ExitAction(editor).actionPerformed(null);
	}
	
	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

}
