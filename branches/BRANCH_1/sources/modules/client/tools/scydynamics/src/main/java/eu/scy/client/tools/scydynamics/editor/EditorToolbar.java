package eu.scy.client.tools.scydynamics.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import colab.um.tools.JTools;


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
	private ActionListener actionListener;
	
	public EditorToolbar(ActionListener actionListener) {
		super(JToolBar.VERTICAL);
		this.actionListener = actionListener;
		setFloatable(false);
		buttonMap = new HashMap<String, JToggleButton>();
		add(createToggleButton(CURSOR, "EditorCursorTB", "cursor"));	
		add(createToggleButton(CONSTANT, "EditorConstantTB", "constant"));
		add(createToggleButton(STOCK, "EditorStockTB", "stock"));
		add(createToggleButton(FLOW, "EditorFlowTB", "flow"));
		add(createToggleButton(RELATION, "EditorRelationTB", "relation"));
		add(createToggleButton(AUX, "EditorAuxTB", "auxiliary"));
		//add(createButton(DATASET, "EdDataset.gif"));
		add(new JToolBar.Separator());
		add(new JToolBar.Separator());
		//add(createButton(DELETE, "EditorDeleteTB", "delete"));
		add(createButton(DELETE, "delete", "delete"));
		add(createButton(CUT, "cut", "cut"));
		add(createButton(COPY, "copy", "copy"));
		add(createButton(PASTE, "paste", "paste"));
		buttonMap.get(CURSOR+"").setSelected(true);
		currentAction = CURSOR;
	}

	private JToggleButton createToggleButton(int cmd, String icon, String tooltip) {
		URL url = JTools.getSysResourceImage(icon);
		JToggleButton button = new JToggleButton(new ImageIcon(url));
		button.setActionCommand(cmd+"");
		button.setToolTipText(tooltip);
		button.addActionListener(this);
		button.setSelected(false);
		buttonMap.put(cmd+"", button);
		return button;
	}
	
	private JButton createButton(int cmd, String icon, String tooltip) {
		URL url = JTools.getSysResourceImage(icon);
		JButton button = new JButton(new ImageIcon(url));
		button.setActionCommand(cmd+"");
		button.setToolTipText(tooltip);
		button.addActionListener(actionListener);
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