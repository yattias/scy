package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import colab.um.draw.JdFigure;
import colab.um.draw.JdRelation;
import colab.um.tools.JTools;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.editor.ModelEditor.Mode;
import eu.scy.client.tools.scydynamics.model.ModelUtils;
import eu.scy.client.tools.scydynamics.model.QualitativeInfluenceType;

@SuppressWarnings("serial")
public class VariableDialog extends JDialog {

	@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(VariableDialog.class.getName());
	private static final int firstColumnWidth = 100;

	private final ResourceBundleWrapper bundle;
	private javax.swing.JTextField nameField = new javax.swing.JTextField(23);
	private javax.swing.JTextField quantitativeExpressionTextField = new javax.swing.JTextField(23);
	private JSlider qualitativeValueSlider = new JSlider();
	private JList infoList;
	private JdFigure figure;
	private Hashtable<String, Object> props;
	private ModelEditor editor;
	private String[] units = {"?", "items", "m", "m/s", "kg", "kg*m/s", "s", "A", "V", "W", "K", "C", "mol", "cd", "J", "Hz", "N", "N*m", "Pa"};
	private JComboBox unitsBox;
	private JLabel colorLabelBox;
	private JButton colorButton;
	private Color newColor;
	private ArrayList<JComboBox> qualitativeComboboxes;
	private VariableDialogListener listener;
	private int tempValue;

	public VariableDialog(java.awt.Frame owner, java.awt.Point position,
			JdFigure figure, ModelEditor editor, ResourceBundleWrapper bundle) {
		super(owner, false);
		this.bundle = bundle;
		this.figure = figure;
		this.props = figure.getProperties();
		this.editor = editor;
		listener = new VariableDialogListener(this);
		setTitle(bundle.getString("VARIABLEDIALOG_TITLE")+" '" + props.get("label") + "'");
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
	
	public double getQualitativeValue() {
		switch (figure.getType()) {
			case JdFigure.AUX:
				return Integer.MAX_VALUE;
			case JdFigure.CONSTANT:
			case JdFigure.STOCK:
				return qualitativeValueSlider.getValue();
			default:
				return Integer.MIN_VALUE;
		}
	}
	
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
		infoList = new javax.swing.JList(ModelUtils.getInputVariableNames(editor.getModel(), (String)props.get("label")));
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
		for (String varName: ModelUtils.getInputVariableNames(editor.getModel(), (String)props.get("label"))) {
			listPanel.add(new JLabel(varName));
			JComboBox box = new JComboBox(createQualitativeRelationsIcons());
			box.setName(varName);
			box.setSelectedIndex(0);
			JdRelation relation = ModelUtils.getRelationBetween(editor.getModel(), varName, (String)props.get("label"));
			if (relation != null) {
				QualitativeInfluenceType relationType = QualitativeInfluenceType.fromInt(relation.getRelationType());
				// find and select the according item
				for (int i=0; i<box.getItemCount(); i++) {
					if ( ((ImageIcon)box.getItemAt(i)).getDescription().equals(relationType.toString())) {
						box.setSelectedIndex(i);
					}
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
		// this "unspecified relation" is always needed
		// at index position 0 (as a default)
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel6a"));
		icon.setDescription(QualitativeInfluenceType.UNSPECIFIED.toString());
		icons.add(icon);
		// linear up
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel1a"));
		icon.setDescription(QualitativeInfluenceType.LINEAR_UP.toString());
		icons.add(icon);
		// linear down
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel3a"));
		icon.setDescription(QualitativeInfluenceType.LINEAR_DOWN.toString());
		icons.add(icon);
		// constant low
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel9a"));
		icon.setDescription(QualitativeInfluenceType.CONSTANT.toString());
		icons.add(icon);
		// s-shaped up
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel8a"));
		icon.setDescription(QualitativeInfluenceType.S_SHAPED.toString());
		icons.add(icon);
		// curve up
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel4a"));
		icon.setDescription(QualitativeInfluenceType.CURVE_UP.toString());
		icons.add(icon);
		// asymptote up
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel5a"));
		icon.setDescription(QualitativeInfluenceType.ASYMPTOTE_UP.toString());
		icons.add(icon);
		// curve down
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel2a"));
		icon.setDescription(QualitativeInfluenceType.CURVE_DOWN.toString());
		icons.add(icon);

		// icon = new ImageIcon(JTools.getSysResourceImage("ESQrel7a"));
		// icon.setDescription("7");
		// icons.add(icon);

		// bell curve
//		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel10a"));
//		icon.setDescription(QualitativeInfluenceType.BELL.toString());
//		icons.add(icon);
		return icons;
	}
	
	private Vector<ImageIcon> _createQualitativeRelationsIcons() {
		Vector<ImageIcon> icons = new Vector<ImageIcon>();
		ImageIcon icon;
		// this "unspecified relation" is always needed
		// at index position 0 (as a default)
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel6a"));
		icon.setDescription(QualitativeInfluenceType.UNSPECIFIED.toString());
		icons.add(icon);
		// linear up
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel1a"));
		icon.setDescription(QualitativeInfluenceType.LINEAR_UP.toString());
		icons.add(icon);
		// curve down
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel2a"));
		icon.setDescription(QualitativeInfluenceType.CURVE_DOWN.toString());
		icons.add(icon);
		// linear down
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel3a"));
		icon.setDescription(QualitativeInfluenceType.LINEAR_DOWN.toString());
		icons.add(icon);
		// curve up
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel4a"));
		icon.setDescription(QualitativeInfluenceType.CURVE_UP.toString());
		icons.add(icon);
		// asymptote up
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel5a"));
		icon.setDescription(QualitativeInfluenceType.ASYMPTOTE_UP.toString());
		icons.add(icon);
		// icon = new ImageIcon(JTools.getSysResourceImage("ESQrel7a"));
		// icon.setDescription("7");
		// icons.add(icon);
		// s-shaped up
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel8a"));
		icon.setDescription(QualitativeInfluenceType.S_SHAPED.toString());
		icons.add(icon);
		// constant low
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel9a"));
		icon.setDescription(QualitativeInfluenceType.CONSTANT.toString());
		icons.add(icon);
		// bell curve
		icon = new ImageIcon(JTools.getSysResourceImage("ESQrel10a"));
		icon.setDescription(QualitativeInfluenceType.BELL.toString());
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
	
	private void setSliderValue(String valueString) {
		// default value
		int value = 50;
		try {
			// 
			value = (int)Math.round(Double.valueOf(valueString));
			if (value < 0 || value >= 100) {
				// value is out of range; possibly after switching from quantitative to qualitative?
				value = 50;
			}
		} catch (Exception ex) {
		}
		setSliderValue(value);
	}
	
	private void setSliderValue(int value) {
		this.tempValue = value;
		SwingUtilities.invokeLater( new Runnable() 
		{ 
		  public void run() { 
			  qualitativeValueSlider.setValue(tempValue);
		  } 
		} );
	}

	private JSlider createQualitativeValueSlider() {
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(0, new JLabel("low"));
		labelTable.put(50, new JLabel("medium"));
		labelTable.put(100, new JLabel("high"));
		qualitativeValueSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		qualitativeValueSlider.setLabelTable(labelTable);
		qualitativeValueSlider.setPaintLabels(true);
		qualitativeValueSlider.setMajorTickSpacing(50);
		qualitativeValueSlider.setMinorTickSpacing(10);
		qualitativeValueSlider.setPaintTicks(true);
		qualitativeValueSlider.setSnapToTicks(false);
		//System.out.print("setting slider value to: " + figure.getProperties().get("expr"));
		setSliderValue(figure.getProperties().get("expr").toString());
		return qualitativeValueSlider;
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
				qualitativeValueSlider.setValue(Integer.parseInt(figure.getProperties().get("expr")+""));
			} catch (NumberFormatException ex) {
				qualitativeValueSlider.setValue(0);
			}
		}
	}

	void setQualitativeRelations() {
		for (JComboBox box: qualitativeComboboxes) {
			JdRelation relation = ModelUtils.getRelationBetween(editor.getModel(), box.getName(), (String)props.get("label"));
			// default value
			int relationType = 6;
			try {
				relationType = Integer.parseInt(((ImageIcon)box.getSelectedItem()).getDescription());
			} catch (NumberFormatException ex) {}
			if (relation != null) {
				relation.setRelationType(relationType);
			}
		}
	}

	public HashMap<JdFigure, QualitativeInfluenceType> getQualitativeRelations() {
		HashMap<JdFigure, QualitativeInfluenceType> relations = new HashMap<JdFigure, QualitativeInfluenceType>();
		for (JComboBox box: qualitativeComboboxes) {
			JdFigure figure = editor.getModel().getObjectOfName(box.getName());			
			JdRelation relation = ModelUtils.getRelationBetween(editor.getModel(), box.getName(), (String)props.get("label"));
			QualitativeInfluenceType type = QualitativeInfluenceType.fromValue(((ImageIcon)box.getSelectedItem()).getDescription());
			relation.setRelationType(QualitativeInfluenceType.getInt(type));
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
