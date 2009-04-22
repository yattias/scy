package nl.utwente.gw.modelling.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;


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
		add(createButton(CURSOR, "EdCursor.gif"));
		add(createButton(DELETE, "EdDelete.gif"));
		add(createButton(CONSTANT, "EdConstant.gif"));
		add(createButton(STOCK, "EdStock.gif"));
		add(createButton(FLOW, "EdFlow.gif"));
		add(createButton(RELATION, "EdRelation.gif"));
		add(createButton(AUX, "EdAux.gif"));
		add(createButton(DATASET, "EdDataset.gif"));
		buttonMap.get(CURSOR+"").setSelected(true);
		currentAction = CURSOR;
	}

	private JToggleButton createButton(int cmd, String icon) {
		JToggleButton button = new JToggleButton(EditorIcons.getImageIcon(icon));
		button.setActionCommand(cmd+"");
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
	
}