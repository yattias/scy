package eu.scy.client.tools.scydynamics.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import colab.um.tools.JTools;


public class EditorToolbar extends JToolBar implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5071178017935714682L;
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
	
	private int currentAction;
	private HashMap<String, JToggleButton> buttonMap;
	
	public EditorToolbar() {
		super(JToolBar.VERTICAL);
		setFloatable(false);
		buttonMap = new HashMap<String, JToggleButton>();
		add(createButton(CURSOR, "EditorCursorTB", "cursor"));
		add(createButton(DELETE, "EditorDeleteTB", "delete"));
		add(createButton(CONSTANT, "EditorConstantTB", "constant"));
		add(createButton(STOCK, "EditorStockTB", "stock"));
		add(createButton(FLOW, "EditorFlowTB", "flow"));
		add(createButton(RELATION, "EditorRelationTB", "relation"));
		add(createButton(AUX, "EditorAuxTB", "auxiliary"));
		//add(createButton(DATASET, "EdDataset.gif"));
		buttonMap.get(CURSOR+"").setSelected(true);
		currentAction = CURSOR;
	}

	private JToggleButton createButton(int cmd, String icon, String tooltip) {
		URL url = JTools.getSysResourceImage(icon);
		JToggleButton button = new JToggleButton(new ImageIcon(url));
		button.setActionCommand(cmd+"");
		button.setToolTipText(tooltip);
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