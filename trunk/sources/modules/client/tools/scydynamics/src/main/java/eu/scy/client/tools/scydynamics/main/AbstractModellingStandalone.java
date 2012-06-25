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

import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.model.ModelUtils;
import eu.scy.client.tools.scydynamics.store.FileStore;
import eu.scy.client.tools.scydynamics.store.SCYDynamicsStore.StoreType;

@SuppressWarnings("serial")
public abstract class AbstractModellingStandalone extends JFrame implements WindowListener {
	
	private ModelEditor editor;
	private final static Logger debugLogger = Logger.getLogger(AbstractModellingStandalone.class.getName());
	
	public AbstractModellingStandalone(String title) {
		super(title);
		Locale.setDefault(new Locale("en"));
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		editor = new ModelEditor(getProperties(), getUsername(getProperties()), this);
		editor.setSCYDynamicsStore(new FileStore(editor));
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(editor, BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(900, 700));
		this.pack();
		this.setVisible(true);
		editor.updateTitle();
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
		int n = JOptionPane.showConfirmDialog(this, "Do you want to save the model\nbefore exiting?", "exiting...", JOptionPane.YES_NO_CANCEL_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			try {
				editor.getSCYDynamicsStore().saveAsModel();	
			} catch (Exception ex) {
				debugLogger.severe(ex.getMessage());
				JOptionPane.showMessageDialog(javax.swing.JOptionPane.getFrameForComponent(editor),
					    "The model could not be stored:\n"+ex.getMessage(),
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
			}
		} else if (n == JOptionPane.NO_OPTION){
			// doing nothing
		} else if (n == JOptionPane.CANCEL_OPTION) {
			return;
		}
		editor.getActionLogger().logSimpleAction(ModellingLogger.EXIT_APPLICATION);
		editor.getActionLogger().close();
		editor.doAutosave(StoreType.ON_EXIT);
		this.setVisible(false);
		this.dispose();
		System.exit(0);
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
