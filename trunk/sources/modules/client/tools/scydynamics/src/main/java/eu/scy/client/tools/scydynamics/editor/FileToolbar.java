package eu.scy.client.tools.scydynamics.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.menu.file.NewAction;
import eu.scy.client.tools.scydynamics.menu.file.OpenFileAction;
import eu.scy.client.tools.scydynamics.menu.file.SaveAsDatasetFileAction;
import eu.scy.client.tools.scydynamics.menu.file.SaveAsFileAction;
import eu.scy.client.tools.scydynamics.menu.file.SaveFileAction;

import java.util.logging.Logger;

@SuppressWarnings("serial")
public class FileToolbar extends JToolBar implements ActionListener {

	@SuppressWarnings("unused")
	private final static Logger debugLogger = Logger.getLogger(FileToolbar.class.getName());
	private ModelEditor editor;
	private JComboBox modeBox;
	private JButton phaseButton = null;

	public FileToolbar(ModelEditor editor, ResourceBundleWrapper bundle) {
		super(SwingConstants.HORIZONTAL);
		this.editor = editor;
		setFloatable(false);
		this.add(new JButton(new NewAction(editor)));
		this.add(new JButton(new OpenFileAction(editor)));
		this.add(new JButton(new SaveFileAction(editor)));
		this.add(new JButton(new SaveAsFileAction(editor)));

		if (Boolean.parseBoolean(editor.getProperties().getProperty("editor.saveasdataset"))) {
			this.addSeparator();
			this.add(new JButton(new SaveAsDatasetFileAction(editor)));
		}
		this.addSeparator();
		modeBox = new JComboBox(ModelEditor.Mode.values());
		modeBox.setSelectedItem(editor.getMode());
		modeBox.addActionListener(this);
		if (Boolean.parseBoolean(editor.getProperties().getProperty("editor.modes_selectable"))) {
			add(new JLabel("Phase: "));
			add(modeBox);
		}
		
		if (Boolean.parseBoolean(editor.getProperties().getProperty("showPhaseChangeButton"))) {
			this.add(Box.createHorizontalGlue());
			ActionListener phaseListener = new PhaseListener(editor);
			phaseButton = Util.createJButton("next phase", "nextPhase", "nextphase.png", phaseListener);
			add(phaseButton);
			add(Util.createJButton("reset model", "reset", "resetmodel.png", phaseListener));
		}
	}
	
	public JButton getPhaseButton() {
		return this.phaseButton;
	}	

	public void updateMode() {
		modeBox.setSelectedItem(editor.getMode());
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() instanceof JComboBox) {
			editor.setMode(((JComboBox) evt.getSource()).getSelectedItem().toString());
		}
	}

}
