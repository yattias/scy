package eu.scy.client.tools.scydynamics.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.editor.menu.edit.CopyAction;
import eu.scy.client.tools.scydynamics.editor.menu.edit.CutAction;
import eu.scy.client.tools.scydynamics.editor.menu.edit.DeleteAction;
import eu.scy.client.tools.scydynamics.editor.menu.edit.PasteAction;

@SuppressWarnings("serial")
public class EditorToolbar extends JToolBar implements ActionListener {

	public static final int CURSOR = 1;
	public static final int DELETE = 2;
	public static final int CONSTANT = 3;
	public static final int STOCK = 4;
	public static final int FLOW = 5;
	public static final int RELATION = 6;
	public static final int AUX = 7;
	public static final int DATASET = 8;
	public static final int HANDLE = 9;
	public static final int OBJECT = 10;
	public static final int CUT = 11;
	public static final int COPY = 12;
	public static final int PASTE = 13;
	public static final int ALL = 14;
	
	private int currentAction;
	private HashMap<String, JToggleButton> buttonMap;
	
	public EditorToolbar(ModelEditor editor, ResourceBundleWrapper bundle) {
		super(JToolBar.VERTICAL);
		setFloatable(false);
		buttonMap = new HashMap<String, JToggleButton>();
		add(createToolbarToggleButton(STOCK, "stock24.png", bundle.getString("EDITOR_STOCK")));
		add(createToolbarToggleButton(AUX, "aux24.png", bundle.getString("EDITOR_AUX")));
		add(createToolbarToggleButton(CONSTANT, "const24.png", bundle.getString("EDITOR_CONSTANT")));
		add(createToolbarToggleButton(FLOW, "flow24.png", bundle.getString("EDITOR_FLOW")));
		add(createToolbarToggleButton(RELATION, "relation24.png", bundle.getString("EDITOR_RELATION")));
		add(new JToolBar.Separator());
		add(new JToolBar.Separator());
		add(createToolbarToggleButton(CURSOR, "pointer24.png", bundle.getString("EDITOR_CURSOR")));
		this.add(new JButton(new CutAction(editor, false)));
		this.add(new JButton(new CopyAction(editor, false)));
		this.add(new JButton(new PasteAction(editor, false)));
		this.add(new JButton(new DeleteAction(editor, false)));
		buttonMap.get(CURSOR+"").setSelected(true);
		currentAction = CURSOR;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		// setting children
		for (Component c: this.getComponents()) {
			c.setEnabled(enabled);
		}
	}
	
	private JToggleButton createToolbarToggleButton(int cmd, String icon, String tooltip) {
		JToggleButton button = new JToggleButton(Util.getImageIcon(icon));
		button.setActionCommand(cmd+"");
		button.setToolTipText(tooltip);
		button.setFocusPainted(false);
		button.addActionListener(this);
		button.setSelected(false);
		buttonMap.put(cmd+"", button);
		return button;
	}

	public int getCurrentAction() {
		return currentAction;
	}
	
	public void actionPerformed(ActionEvent evt) {
		buttonMap.get(currentAction+"").setSelected(false);
		currentAction = Integer.valueOf(evt.getActionCommand()).intValue();		
	}

	public void toCursorAction() {
		buttonMap.get(currentAction+"").setSelected(false);
		buttonMap.get(CURSOR+"").setSelected(true);
		currentAction = CURSOR;
	}
	
}