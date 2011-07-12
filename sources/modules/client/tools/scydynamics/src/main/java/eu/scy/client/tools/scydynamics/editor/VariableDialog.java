package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import colab.um.draw.JdFigure;
import colab.um.draw.JdRelation;
import colab.um.tools.JTools;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.editor.ModelEditor.Mode;
import eu.scy.client.tools.scydynamics.model.ModelUtils;
import eu.scy.client.tools.scydynamics.model.ModelUtils.QualitativeInfluenceType;

@SuppressWarnings("serial")
public class VariableDialog extends JDialog {

	@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(VariableDialog.class.getName());
	private static final int firstColumnWidth = 100;

	private final ResourceBundleWrapper bundle;
	private javax.swing.JTextField nameField = new javax.swing.JTextField(23);
	private javax.swing.JTextField quantitativeExpressionTextField = new javax.swing.JTextField(23);
	private JSlider valueQualitative = new JSlider();
	private FlowLayout flowRight = new FlowLayout(FlowLayout.RIGHT);
	private FlowLayout flowLeft = new FlowLayout(FlowLayout.LEFT);
	private JList infoList;
	private JdFigure figure;
	private Hashtable<String, Object> props;
	private ModelEditor editor;
	private String label;
	private String[] units = {"?", "items", "m", "m/s", "kg", "kg*m/s", "s", "A", "V", "W", "K", "C", "mol", "cd", "J", "Hz", "N", "N*m", "Pa"};
	private JComboBox unitsBox;
	private JLabel colorLabelBox;
	private JButton colorButton;
	private Color newColor;
	private ArrayList<JComboBox> qualitativeComboboxes;
	private VariableDialogListener listener;

	public VariableDialog(java.awt.Frame owner, java.awt.Point position,
			JdFigure figure, ModelEditor editor, ResourceBundleWrapper bundle) {
		super(owner, false);
		this.bundle = bundle;
		this.figure = figure;
		this.props = figure.getProperties();
		this.editor = editor;
		this.label = (String) props.get("label");
		listener = new VariableDialogListener(this);
		setTitle(bundle.getString("VARIABLEDIALOG_TITLE")+" '" + this.label + "'");
		editor.getActionLogger().logActivateWindow("specification", figure.getID(), this);
		getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(getSpecsPanel());
		
		if (editor.getMode()!=Mode.MODEL_SKETCHING) {
			if (editor.getMode()==Mode.QUALITATIVE_MODELLING) {
				if (this.figure.getType() == JdFigure.AUX) {
					getContentPane().add(createQualitativeVariablePanel());
				}
			} else {
				if (this.figure.getType() == JdFigure.AUX) {
					getContentPane().add(getQuantitativeVariablePanel());
				}
				getContentPane().add(getCalculatorPanel());
			}
		}
		getContentPane().add(getOkayCancelPanel());
		
		updateView();
		pack();
		this.setMinimumSize(this.getPreferredSize());
		setLocation(Math.max(0, position.x-this.getWidth()/2), Math.max(0, position.y-this.getHeight()/2));
		setVisible(true);
	}
	
	public Object getFigureProperty(String prop) {
		return props.get(prop);
	}
	
	public ResourceBundleWrapper getBundle() {
		return bundle;
	}
	
	public JTextField getQuantitativeExpressionTextField() {
		return quantitativeExpressionTextField;
	}
	
	public void setFigureProperty(String key, Object value) {
		props.put(key, value);
	}
	
	public void submitFigureProperties(String oldName) {
		editor.setFigureProperties(oldName, props);
	}
	
	public JdFigure getFigure() {
		return figure;
	}
	
	public ModelEditor getEditor() {
		return editor;
	}
	
	public String getNewName() {
		return nameField.getText();
	}
	
	public Color getNewColor() {
		return newColor;
	}
	
	public String getQuantitativeExpression() {
		return quantitativeExpressionTextField.getText();
	}
	
	public void setQuantitativeExpression(String newExpression) {
		quantitativeExpressionTextField.setText(newExpression);
	}
	
	public String getQualitativeExpression() {
		switch (figure.getType()) {
			case JdFigure.AUX:
				return Integer.MAX_VALUE+"";
			case JdFigure.CONSTANT:
			case JdFigure.STOCK:
				return valueQualitative.getValue()+"";
			default:
				return Integer.MIN_VALUE+"";
		}
	}

//	public String getQualitativeExpression() {
//		int value = 0;
//		switch (figure.getType()) {
//		case JdFigure.AUX:
//			value = Integer.MAX_VALUE;
//		case JdFigure.CONSTANT:
//			switch (valueQualitative.getValue()) {
//			case -3: value = -10; break;
//			case -2: value = -5; break;
//			case -1: value = -1; break;
//			case -0: value = 0; break;
//			case 1: value = 1; break;
//			case 2: value = 5; break;
//			case 3: value = 10; break;
//			}
//			break;
//		case JdFigure.STOCK:
//			switch (valueQualitative.getValue()) {
//			case -1: value = -1; break;
//			case -0: value = 0; break;
//			case 1: value = 1; break;
//			}
//			break;
//		}
//		return value+"";
//	}
	
	public String getUnit() {
		return (String) unitsBox.getSelectedItem();
	}

	private JButton makeButton(String s) {
		JButton b = new JButton(s);
		b.setActionCommand(s);
		b.addActionListener(listener);
		return b;
	}

	private JPanel getQuantitativeVariablePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("VARIABLEDIALOG_VARIABLES")));
		infoList = new javax.swing.JList(ModelUtils.getInputVariableNames(editor.getModel(), this.label));
		infoList.addMouseListener(listener);
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(
				infoList);
		panel.add(scrollPane);
		return panel;
	}

	private JPanel createQualitativeVariablePanel() {
		JPanel panel = new JPanel();
		JPanel listPanel = new JPanel();
		panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Qualitative relations"));
		listPanel.setLayout(new GridLayout(0,3));
		qualitativeComboboxes = new ArrayList<JComboBox>();
		for (String varName: ModelUtils.getInputVariableNames(editor.getModel(), this.label)) {
			listPanel.add(new JLabel(varName));
			JComboBox box = new JComboBox(createQualitativeRelationsIcons());
			box.setName(varName);
			JdRelation relation = ModelUtils.getRelationBetween(editor.getModel(), varName, this.label);
			if (relation != null) {
				if (relation.getRelationType() == 6) {
					System.out.println("*** 6");
					box.setSelectedIndex(0);
				} else {
					box.setSelectedIndex(relation.getRelationType());
				}
			}
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
		//panel.setLayout(new java.awt.GridLayout(4, 2));
		BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxLayout); 
		panel.add(Box.createRigidArea(new Dimension(10,10)));
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalStrut(10));
		nameLabel.setPreferredSize(new Dimension(firstColumnWidth, nameLabel.getPreferredSize().height));
		box.add(nameLabel);
		box.add(nameField);
		box.add(Box.createHorizontalStrut(10));
		panel.add(box);

		box = Box.createHorizontalBox();
		box.add(Box.createHorizontalStrut(10));
		if (editor.getMode()!=Mode.MODEL_SKETCHING) {
			box.add(valueLabel);
			valueLabel.setPreferredSize(new Dimension(firstColumnWidth, valueLabel.getPreferredSize().height));
			
			//box.add(Box.createHorizontalStrut(firstColumnWidth-valueLabel.getWidth()));
			if (editor.getMode()==Mode.QUALITATIVE_MODELLING) {
				if (figure.getType() == JdFigure.AUX) {
					// qualitative aux
					quantitativeExpressionTextField.setEditable(false);
					box.add(quantitativeExpressionTextField);
				} else {
					// qualitative stock or const
					quantitativeExpressionTextField.setText("0");
					box.add(createQualitativeValueSlider());
				}
			} else {
				// quantitative
				box.add(quantitativeExpressionTextField);
			}
		} else {
			// MODEL_SKETCHING
		}
		box.add(Box.createHorizontalStrut(10));
		panel.add(box);

		JLabel unitLabel = new javax.swing.JLabel(bundle.getString("VARIABLEDIALOG_UNIT")+": ");
		unitLabel.setHorizontalAlignment(JLabel.RIGHT);
		unitLabel.setPreferredSize(new Dimension(firstColumnWidth, unitLabel.getPreferredSize().height));
		box = Box.createHorizontalBox();
		box.add(Box.createHorizontalStrut(10));
		box.add(unitLabel);
		unitsBox = new JComboBox(units);
		unitsBox.setEditable(true);
		unitsBox.setSelectedItem(figure.getProperties().get("unit"));
		box.add(unitsBox);
		box.add(Box.createHorizontalStrut(10));
		panel.add(box);
		
		box = Box.createHorizontalBox();
		box.add(Box.createHorizontalStrut(10));
		JLabel colorLabel = new JLabel(bundle.getString("VARIABLEDIALOG_COLOR")+": ");
		colorLabel.setHorizontalAlignment(JLabel.RIGHT);
		colorLabel.setPreferredSize(new Dimension(firstColumnWidth, colorLabel.getPreferredSize().height));
		box.add(colorLabel);
		colorLabelBox = new JLabel("\u2588");
		colorLabelBox.setForeground(editor.getModel().getObjectOfName((String) figure.getProperties().get("label")).getLabelColor());
		colorLabelBox.setPreferredSize(new Dimension(20, colorLabelBox.getPreferredSize().height));
		
		newColor = colorLabelBox.getForeground();
		box.add(colorLabelBox);
		colorButton = new JButton(bundle.getString("VARIABLEDIALOG_CHOOSE"));
		colorButton.setActionCommand("color");
		colorButton.addActionListener(listener);
		
		//box.add(Box.createHorizontalStrut(10));
		box.add(colorButton);
		box.add(Box.createHorizontalGlue());
		box.add(Box.createHorizontalStrut(10));
		panel.add(box);
		return panel;
	}

	private JSlider createQualitativeValueSlider() {
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		switch (figure.getType()) {
		case JdFigure.AUX:
		case JdFigure.CONSTANT:
		case JdFigure.STOCK:
			valueQualitative = new JSlider(JSlider.HORIZONTAL,-2,2,0);
			labelTable.put(ModelEditor.LARGE_NEGATIVE, new JLabel("negative") );
			labelTable.put(ModelEditor.SMALL_NEGATIVE, new JLabel("") );
			labelTable.put(ModelEditor.ZERO, new JLabel("zero") );
			labelTable.put(ModelEditor.SMALL_POSITIVE, new JLabel("") );
			labelTable.put(ModelEditor.LARGE_POSITIVE, new JLabel("positive") );
			break;
		}
		valueQualitative.setLabelTable( labelTable );
		valueQualitative.setPaintLabels(true);  	
		valueQualitative.setMajorTickSpacing(1);
		valueQualitative.setPaintTicks(true);
		valueQualitative.setSnapToTicks(true); 	
		return valueQualitative;
	}

	private JPanel getOkayCancelPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new java.awt.FlowLayout());
		javax.swing.JButton okayButton = new javax.swing.JButton(bundle.getString("VARIABLEDIALOG_OKAY"));
		okayButton.setActionCommand("okay");
		if (editor.getMode()==Mode.BLACK_BOX || editor.getMode()==Mode.CLEAR_BOX) {
			okayButton.setEnabled(false);
		}
		javax.swing.JButton cancelButton = new javax.swing.JButton(bundle.getString("VARIABLEDIALOG_CANCEL"));
		cancelButton.setActionCommand("cancel");
		okayButton.addActionListener(listener);
		cancelButton.addActionListener(listener);
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
		if (editor.getMode()==Mode.QUALITATIVE_MODELLING) {
			panel.setBorder(new TitledBorder(bundle.getString("VARIABLEDIALOG_INPUTPAD")+" (disabled/qualitative)"));        	
		} else {
			panel.setBorder(new TitledBorder(bundle.getString("VARIABLEDIALOG_INPUTPAD")));
		}
		return panel;
	}

	public void updateView() {
		nameField.setText(figure.getProperties().get("label") + "");
		quantitativeExpressionTextField.setText(figure.getProperties().get("expr") + "");
		if (editor.getMode()==Mode.QUALITATIVE_MODELLING) {
			try {
				// for the aux
				quantitativeExpressionTextField.setText("-qualitative-");
				// for stocks and consts
				valueQualitative.setValue(Integer.parseInt(figure.getProperties().get("expr")+""));
			} catch (NumberFormatException ex) {
				valueQualitative.setValue(0);
			}
		}
	}

	void setQualitativeRelations() {
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

	HashMap<JdFigure, QualitativeInfluenceType> getQualitativeRelations() {
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
		colorLabelBox.setForeground(newColor);
	}

	public void closeDialog() {
		setVisible(false);
		setEnabled(false);
		dispose();
	}
}
