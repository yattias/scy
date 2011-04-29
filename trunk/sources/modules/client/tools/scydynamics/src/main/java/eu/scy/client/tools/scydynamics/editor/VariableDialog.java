package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;

import colab.um.draw.JdAux;
import colab.um.draw.JdFigure;
import colab.um.draw.JdLink;
import colab.um.draw.JdNode;
import colab.um.draw.JdRelation;
import colab.um.tools.JTools;
import eu.scy.client.tools.scydynamics.model.Model;
import eu.scy.client.tools.scydynamics.model.ModelUtils;
import eu.scy.client.tools.scydynamics.model.ModelUtils.QualitativeInfluenceType;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class VariableDialog extends javax.swing.JDialog implements
java.awt.event.ActionListener, java.awt.event.MouseListener {

	private final static Logger LOGGER = Logger.getLogger(VariableDialog.class.getName());

	private final ResourceBundleWrapper bundle;
	private javax.swing.JTextField nameField = new javax.swing.JTextField(23);
	private javax.swing.JTextField valueQuantitative = new javax.swing.JTextField(23);
	private JSlider valueQualitative = new JSlider();
	private FlowLayout flowRight = new FlowLayout(FlowLayout.RIGHT);
	private javax.swing.JList infoList;
	private JdFigure figure;
	private Hashtable<String, Object> props;
	private ModelEditor editor;
	private String label;
	String[] units = {"?", "items", "m", "m/s", "kg", "kg*m/s", "s", "A", "V", "W", "K", "C", "mol", "cd", "J", "Hz", "N", "N*m", "Pa"};
	private JComboBox unitsBox;
	private JLabel colorLabel;
	private JButton colorButton;
	private Color newColor;
	private ArrayList<JComboBox> qualitativeComboboxes;

	public VariableDialog(java.awt.Frame owner, java.awt.Point position,
			JdFigure figure, ModelEditor editor, ResourceBundleWrapper bundle) {
		super(owner, false);
		this.bundle = bundle;
		editor.getActionLogger().logActivateWindow("specification", figure.getID(), this);
		//this.setLocation(position);
		this.figure = figure;
		this.props = figure.getProperties();
		this.editor = editor;
		this.label = (String) props.get("label");
		setTitle(bundle.getString("VARIABLEDIALOG_TITLE")+" '" + this.label + "'");
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getSpecsPanel(), BorderLayout.NORTH);
		getContentPane().add(getOkayCancelPanel(), BorderLayout.SOUTH);
		if (editor.isQualitative()) {
			if (this.figure.getType() == JdFigure.AUX) {
				getContentPane().add(createQualitativeVariablePanel(), BorderLayout.CENTER);
			}
		} else {
			if (this.figure.getType() == JdFigure.AUX) {
				getContentPane().add(getQuantitativeVariablePanel(), BorderLayout.CENTER);
			}
			getContentPane().add(getCalculatorPanel(), BorderLayout.EAST);
		}	
		this.setPreferredSize(new Dimension(440, 300));
		updateView();
		pack();
		setLocation(Math.max(0, position.x-this.getWidth()/2), Math.max(0, position.y-this.getHeight()/2));
		setVisible(true);
	}

	private JButton makeButton(String s) {
		JButton b = new JButton(s);
		b.setEnabled(!editor.isQualitative());
		b.setActionCommand(s);
		b.addActionListener(this);
		return b;
	}

	private JPanel getQuantitativeVariablePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("VARIABLEDIALOG_VARIABLES")));
		infoList = new javax.swing.JList(ModelUtils.getInputVariableNames(editor.getModel(), this.label));
		infoList.addMouseListener(this);
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(
				infoList);
		panel.add(scrollPane);
		return panel;
	}

	private JPanel createQualitativeVariablePanel() {
		JPanel panel = new JPanel();
		JPanel listPanel = new JPanel();
		panel.setBorder(javax.swing.BorderFactory.createTitledBorder("qualitative relations"));
		listPanel.setLayout(new GridLayout(0,3));
		qualitativeComboboxes = new ArrayList<JComboBox>();
		for (String varName: ModelUtils.getInputVariableNames(editor.getModel(), this.label)) {
			listPanel.add(new JLabel(varName));
			JComboBox box = new JComboBox(createQualitativeRelationsIcons());
			box.setName(varName);
			JdRelation relation = ModelUtils.getRelationBetween(editor.getModel(), varName, this.label);
			if (relation != null) {
				if (relation.getRelationType() == 6) {
					box.setSelectedIndex(0);
				} else if (relation.getRelationType() == 4) {
					box.setSelectedIndex(3);
				} else if (relation.getRelationType() == 5) {
					box.setSelectedIndex(4);
				} else {
					box.setSelectedIndex(relation.getRelationType());
				}
			} else box.setSelectedIndex(0);
			qualitativeComboboxes.add(box);
			listPanel.add(box);
			listPanel.add(new JLabel(" "+props.get("label")));
		} 
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(listPanel);
		panel.add(scrollPane);
		return panel;
	}

	private Vector<ImageIcon> createQualitativeRelationsIcons() {
		Vector<ImageIcon> icons = new Vector<ImageIcon>();
		ImageIcon icon;
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel6a"));
		icon.setDescription("6");
		icons.add(icon);
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel1a"));
		icon.setDescription("1");
		icons.add(icon);
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel2a"));
		icon.setDescription("2");
		icons.add(icon);
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel3a"));
		icon.setDescription("3");
		icons.add(icon);
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel4a"));
		icon.setDescription("4");
		icons.add(icon);
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel5a"));
		icon.setDescription("5");
		icons.add(icon);
		return icons;
	}

	private JPanel getSpecsPanel() {
		JLabel nameLabel = new javax.swing.JLabel(bundle.getString("VARIABLEDIALOG_LABEL")+": ");
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);

		String valueLabelString = bundle.getString("VARIABLEDIALOG_VALUE")+": ";
		switch (figure.getType()) {
		case JdFigure.AUX:
			valueLabelString = bundle.getString("VARIABLEDIALOG_EXPRESSION")+": ";
			break;
		case JdFigure.STOCK:
			valueLabelString = bundle.getString("VARIABLEDIALOG_STARTVALUE")+": ";
			break;
		case JdFigure.CONSTANT:
			valueLabelString = bundle.getString("VARIABLEDIALOG_CONSTANTVALUE")+": ";
			break;
		}
		JLabel valueLabel = new javax.swing.JLabel(valueLabelString);
		valueLabel.setHorizontalAlignment(JLabel.RIGHT);

		JPanel panel = new JPanel();
		panel.setLayout(new java.awt.GridLayout(4, 2));
		panel.add(nameLabel);
		panel.add(nameField);
		panel.add(valueLabel);
		if (editor.isQualitative()) {
			if (figure.getType() == JdFigure.AUX) {
				// qualitative aux
				valueQuantitative.setEditable(false);
				panel.add(valueQuantitative);
			} else {
				// qualitative stock or const
				valueQuantitative.setText("0");
				panel.add(createQualitativeValueSlider());
			}
		} else {
			// quantitative
			panel.add(valueQuantitative);
		}

		JLabel unitLabel = new javax.swing.JLabel(bundle.getString("VARIABLEDIALOG_UNIT")+": ");
		unitLabel.setHorizontalAlignment(JLabel.RIGHT);
		panel.add(unitLabel);
		unitsBox = new JComboBox(units);
		unitsBox.setEditable(true);
		unitsBox.setSelectedItem(figure.getProperties().get("unit"));
		panel.add(unitsBox);

		JPanel colorLabelPanel = new JPanel();
		colorLabelPanel.setLayout(flowRight);
		colorLabelPanel.add(new JLabel(bundle.getString("VARIABLEDIALOG_COLOR")+": "));
		colorLabel = new JLabel("\u2588");
		colorLabel.setForeground(editor.getModel().getObjectOfName((String) figure.getProperties().get("label")).getLabelColor());
		newColor = colorLabel.getForeground();
		colorLabelPanel.add(colorLabel);
		panel.add(colorLabelPanel);
		colorButton = new JButton(bundle.getString("VARIABLEDIALOG_CHOOSE"));
		colorButton.setActionCommand("color");
		colorButton.addActionListener(this);
		panel.add(colorButton);
		return panel;
	}

	private JSlider createQualitativeValueSlider() {
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		switch (figure.getType()) {
		case JdFigure.AUX:
		case JdFigure.CONSTANT:
			valueQualitative = new JSlider(JSlider.HORIZONTAL,-3,3,0);
			labelTable.put( new Integer(-3), new JLabel("large negative") );
			//labelTable.put( new Integer(-2), new JLabel("large negative") );
			//labelTable.put( new Integer(-1), new JLabel("negative") );
			labelTable.put( new Integer(0), new JLabel("zero") );
			//labelTable.put( new Integer(1), new JLabel("positive") );
			//labelTable.put( new Integer(2), new JLabel("large positive") );
			labelTable.put( new Integer(3), new JLabel("large positive") );
			break;
		case JdFigure.STOCK:
			valueQualitative = new JSlider(JSlider.HORIZONTAL,-1,1,0);
			labelTable.put( new Integer(-1), new JLabel("negative") );
			labelTable.put( new Integer(0), new JLabel("zero") );
			labelTable.put( new Integer(1), new JLabel("positive") );
			break;
		}
		valueQualitative.setLabelTable( labelTable );
		valueQualitative.setPaintLabels(true);  	
		valueQualitative.setMajorTickSpacing(1);
		valueQualitative.setPaintTicks(true);
		valueQualitative.setSnapToTicks(true); 	
		return valueQualitative;
	}

	private int getQualitativeValue() {
		int value = 0;
		switch (figure.getType()) {
		case JdFigure.AUX:
			value = Integer.MAX_VALUE;
		case JdFigure.CONSTANT:
			switch (valueQualitative.getValue()) {
			case -3: value = -10; break;
			case -2: value = -5; break;
			case -1: value = -1; break;
			case -0: value = 0; break;
			case 1: value = 1; break;
			case 2: value = 5; break;
			case 3: value = 10; break;
			}
			break;
		case JdFigure.STOCK:
			switch (valueQualitative.getValue()) {
			case -1: value = -1; break;
			case -0: value = 0; break;
			case 1: value = 1; break;
			}
			break;
		}
		return value;
	}

	private JPanel getOkayCancelPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new java.awt.FlowLayout());
		javax.swing.JButton okayButton = new javax.swing.JButton(bundle.getString("VARIABLEDIALOG_OKAY"));
		okayButton.setActionCommand("okay");
		javax.swing.JButton cancelButton = new javax.swing.JButton(bundle.getString("VARIABLEDIALOG_CANCEL"));
		cancelButton.setActionCommand("cancel");
		okayButton.addActionListener(this);
		cancelButton.addActionListener(this);
		panel.add(okayButton);
		panel.add(cancelButton);
		return panel;
	}

	private JPanel getCalculatorPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 5));
		panel.add(makeButton("7"));
		panel.add(makeButton("8"));
		panel.add(makeButton("9"));
		panel.add(makeButton("+"));
		panel.add(makeButton("-"));
		panel.add(makeButton("4"));
		panel.add(makeButton("5"));
		panel.add(makeButton("6"));
		panel.add(makeButton("*"));
		panel.add(makeButton("/"));
		panel.add(makeButton("1"));
		panel.add(makeButton("2"));
		panel.add(makeButton("3"));
		panel.add(makeButton("("));
		panel.add(makeButton(")"));
		panel.add(makeButton("0"));
		panel.add(makeButton("."));
		panel.add(makeButton("C"));
		if (editor.isQualitative()) {
			panel.setBorder(new TitledBorder(bundle.getString("VARIABLEDIALOG_INPUTPAD")+" (disabled/qualitative)"));        	
		} else {
			panel.setBorder(new TitledBorder(bundle.getString("VARIABLEDIALOG_INPUTPAD")));
		}
		return panel;
	}

	public void updateView() {
		nameField.setText(figure.getProperties().get("label") + "");
		valueQuantitative.setText(figure.getProperties().get("expr") + "");
		if (editor.isQualitative()) {
			try {
				// for the aux
				valueQuantitative.setText("-qualitative-");
				// for stocks and consts
				valueQualitative.setValue(Integer.parseInt(figure.getProperties().get("expr")+""));
			} catch (NumberFormatException ex) {
				valueQualitative.setValue(0);
			}
		}
	}

	/*
	 * This method inserts the String "s" at the right position into the
	 * JTextField "field", taking a selection of text into account.
	 */
	private void paste(String s, JTextField field) {
		int start = field.getSelectionStart();
		int end = field.getSelectionEnd();
		String oldText = field.getText();
		String newText = oldText.substring(0, start) + s
		+ oldText.substring(end, oldText.length());
		field.setText(newText);
	}

	public void actionPerformed(java.awt.event.ActionEvent event) {
		if (event.getActionCommand() == "okay") {

			if (editor.isQualitative() && this.figure.getType() == JdFigure.AUX) {
				setQualitativeRelations();
			}

			String oldName = (String) props.get("label");
			String oldExpr = (String) props.get("expr");
			String oldUnit = (String) props.get("unit");

			// removing spaces and special chars in variable name
			// (as they may crash the simulation engine
			// or xml serialising)
			String newName = nameField.getText();
			newName = newName.replaceAll("\\s+", "_");
			newName = newName.replaceAll("<", "");
			newName = newName.replaceAll(">", "");
			newName = newName.replaceAll("&", "");
			// cleaning some bad chars in expression
			String express = valueQuantitative.getText();
			express = express.replaceAll("<", "");
			express = express.replaceAll(">", "");
			express = express.replaceAll("&", "");
			// cleaning some bad chars in unit
			String unit = (String) unitsBox.getSelectedItem();
			unit = unit.replaceAll("<", "");
			unit = unit.replaceAll(">", "");
			unit = unit.replaceAll("&", "");

			props.put("label", newName);
			
			if (editor.isQualitative()) {
				if (this.figure.getType() == JdFigure.AUX) {
					System.out.println("+++ generating expression for "+this.label);
					props.put("expr", ModelUtils.getQualitativeExpression(getQualitativeRelations(), editor.getModel()));
				} else {
					props.put("expr", getQualitativeValue()+"");
				}
			} else {
				props.put("expr", express);
			}
			props.put("unit", unit);            
			editor.setFigureProperties(oldName, props);

			if (!newColor.equals(editor.getModel().getObjectOfName((String) figure.getProperties().get("label")).getLabelColor()) || !oldName.equals(newName) || !oldExpr.equals(valueQuantitative.getText()) || !oldUnit.equals(unitsBox.getSelectedItem())) {
				// name, expression, unit or color has changed, send a change-specification-logevent
				editor.getActionLogger().logChangeSpecification(figure.getID(), newName, valueQuantitative.getText(), (String) unitsBox.getSelectedItem(), editor.getXmModel().getXML("", true));
				// and set the (possibly new) color of the object
				editor.getModel().getObjectOfName((String) figure.getProperties().get("label")).setLabelColor(newColor);
			}

			setVisible(false);
			setEnabled(false);
			dispose();
		} else if (event.getActionCommand() == "cancel") {
			setVisible(false);
			setEnabled(false);
			dispose();
		} else if (event.getActionCommand() == "C") {
			valueQuantitative.setText("");
		} else if (event.getActionCommand() == "color") {
			java.awt.Frame frame = javax.swing.JOptionPane.getFrameForComponent(this);
			ColorDialog cdialog = new ColorDialog(frame, colorButton.getLocationOnScreen(), this, bundle);
			cdialog.setVisible(true);
		} else {
			// here go the clicks of the "calculator panel"
			paste(event.getActionCommand(), valueQuantitative);
		}
	}

	private void setQualitativeRelations() {
		for (JComboBox box: qualitativeComboboxes) {
			JdRelation relation = ModelUtils.getRelationBetween(editor.getModel(), box.getName(), this.label);
			int relationType = 6;
			try {
				relationType = Integer.parseInt(((ImageIcon)box.getSelectedItem()).getDescription());
			} catch (NumberFormatException ex) {}
			if (relation != null) {
				relation.setRelationType(relationType);
			}
		}
	}
	
	private HashMap<JdFigure, QualitativeInfluenceType> getQualitativeRelations() {
		HashMap<JdFigure, QualitativeInfluenceType> relations = new HashMap<JdFigure, QualitativeInfluenceType>();
		
		for (JComboBox box: qualitativeComboboxes) {
			JdFigure figure = editor.getModel().getObjectOfName(box.getName());			
			JdRelation relation = ModelUtils.getRelationBetween(editor.getModel(), box.getName(), this.label);
			int relationType = 6;
			try {
				relationType = Integer.parseInt(((ImageIcon)box.getSelectedItem()).getDescription());
			} catch (NumberFormatException ex) {}
			if (relation != null) {
				relation.setRelationType(relationType);
			}
			QualitativeInfluenceType type;
			switch (relationType) {
			case 1: type = QualitativeInfluenceType.LINEAR_UP; break;
			case 2: type = QualitativeInfluenceType.CURVE_DOWN; break;
			case 3: type = QualitativeInfluenceType.LINEAR_DOWN; break;
			case 4: type = QualitativeInfluenceType.CURVE_UP; break;
			case 5: type = QualitativeInfluenceType.ASYMPTOTE_UP; break;
			case 6: type = QualitativeInfluenceType.UNSPECIFIED; break;			
			default: type = QualitativeInfluenceType.UNSPECIFIED; break;
			}
			relations.put(figure, type);			
		}
		return relations;
	}

	protected void setNewColor(Color newColor) {
		this.newColor = newColor;
		colorLabel.setForeground(newColor);
	}

	/**
	 * Invoked when the mouse button has been clicked (pressed and released) on
	 * a component.
	 */
	public void mouseClicked(MouseEvent e) {
		if ((javax.swing.JList) e.getSource() == infoList) {
			String selected = (infoList.getSelectedValue() == null ? ""
					: (String) infoList.getSelectedValue());
			paste(selected, valueQuantitative);
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
