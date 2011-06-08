package eu.scy.client.tools.scydynamics.editor;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import colab.um.draw.JdFigure;
import colab.um.draw.JdObject;
import eu.scy.client.tools.scydynamics.editor.ModelEditor.Mode;
import eu.scy.client.tools.scydynamics.model.Model;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class VariableSelectionPanel extends JPanel {

    private final static Logger LOGGER = Logger.getLogger(VariableSelectionPanel.class.getName());

	private Map<String, JCheckBox> variables;
	private HashSet<JTextField> textFields;
	private ModelEditor editor;
    private final ResourceBundleWrapper bundle;
	private boolean showTime;
	private Model model;
	private FlowLayout leftFlow;
	private FlowLayout rightFlow;

	public VariableSelectionPanel(ModelEditor editor, ResourceBundleWrapper bundle, boolean showTime) {
		super();
		leftFlow = new FlowLayout();
		leftFlow.setAlignment(FlowLayout.LEFT);
		rightFlow = new FlowLayout();
		rightFlow.setAlignment(FlowLayout.RIGHT);
		this.editor = editor;
		this.bundle = bundle;
		this.showTime = showTime;
		variables = new HashMap<String, JCheckBox>();
		textFields = new HashSet<JTextField>();
		this.setLayout(new BorderLayout());
		if (editor.getModel() != null) {
			updateVariables();
		}
	}
	
	public void checkModel() {
		Enumeration<JdObject> objects = editor.getModel().getObjects().elements();
		JdObject object;
		while (objects.hasMoreElements()) {
			object = objects.nextElement();		
			if (!object.isSpecified()) {
				LOGGER.log(Level.INFO, "{0} is not specified", object.getLabel());
			}
		}	
	}

	public Map<String, JCheckBox> getVariables() {
		return variables;
	}

	public void updateVariables() {
		model = editor.getModel();
		List<String> oldSelection = this.getSelectedVariables();
		variables.clear();
		// cleaning the panel
		this.removeAll();
		// counting number of relevant variables
		int variablecount = 0;
		if (showTime) {
			variablecount++;
		}
		variablecount = variablecount + model.getAuxs().size()
				+ model.getStocks().size();
		if (variablecount == 0) {
			// no relevant variables in model
			this.add(new JLabel(bundle.getString("PANEL_NOVARIABLES")), BorderLayout.NORTH);
		} else {
			JPanel variablesPanel = new JPanel();
			variablesPanel.setLayout(new GridLayout(variablecount, 1));
			JCheckBox box;
			JdObject object;
			JLabel colorLabel;
			JPanel vPanel;
			if (showTime) {
				vPanel = new JPanel(leftFlow);			
				colorLabel = new JLabel("\u2588");
				colorLabel.setForeground(Color.BLACK);
				vPanel.add(colorLabel);
				box = new JCheckBox(" (time) ");
				if (oldSelection.contains("time")) {
					box.setSelected(true);
				}
				vPanel.add(box);
				variablesPanel.add(vPanel);
				variables.put("time", box);
			}
			Enumeration<JdObject> objects = model.getNodes().elements();
			while (objects.hasMoreElements()) {
				object = objects.nextElement();			
				if (object.getType() == JdFigure.STOCK) {
					vPanel = new JPanel(leftFlow);
					colorLabel = new JLabel("\u2588");
					colorLabel.setForeground(object.getLabelColor());
					//vPanel.setBackground(object.getLabelColor());
					vPanel.add(colorLabel);
					box = new JCheckBox(" ("+bundle.getString("EDITOR_STOCK")+") "+object.getLabel());
					if (oldSelection.contains(object.getLabel())) {
						box.setSelected(true);
					}
					vPanel.add(box);
					variablesPanel.add(vPanel);
					variables.put(object.getLabel(), box);
				} else if (object.getType() == JdFigure.AUX) {
					vPanel = new JPanel(leftFlow);
					colorLabel = new JLabel("\u2588");
					colorLabel.setForeground(object.getLabelColor());
					vPanel.add(colorLabel);
					box = new JCheckBox(" ("+bundle.getString("EDITOR_AUX")+") "+object.getLabel());
					if (oldSelection.contains(object.getLabel())) {
						box.setSelected(true);
					}
					vPanel.add(box);
					variablesPanel.add(vPanel);
					variables.put(object.getLabel(), box);
				}
			}
			variablesPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("PANEL_VARIABLESELECTION")));
			
/*			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			splitPane.setOneTouchExpandable(true);
			splitPane.setTopComponent(variablesPanel);
			splitPane.setBottomComponent(getValuesPanel());			
			this.add(splitPane, BorderLayout.NORTH);*/
			
			JPanel contentPanel = new JPanel();
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(variablesPanel, BorderLayout.NORTH);
			contentPanel.add(getValuesPanel(), BorderLayout.SOUTH);
			this.add(contentPanel, BorderLayout.NORTH);
		}
	}
	
	private JPanel getValuesPanel() {
		textFields.clear();
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("values"));
		int variablecount = model.getConstants().size() + model.getStocks().size();
		panel.setLayout(new GridLayout(variablecount, 2));
		JdObject object;
		JPanel vPanel;
		JLabel colorLabel;
		JTextField textField;
		Enumeration<JdObject> objects = model.getNodes().elements();
		while (objects.hasMoreElements()) {
			object = objects.nextElement();			
			if (object.getType() == JdFigure.STOCK || object.getType() == JdFigure.CONSTANT) {
				vPanel = new JPanel(leftFlow);
				colorLabel = new JLabel("\u2588");
				colorLabel.setForeground(object.getLabelColor());
				vPanel.add(colorLabel);
				vPanel.add(new JLabel(object.getLabel()));
				textField = new JTextField(6);
				textField.setHorizontalAlignment(JTextField.RIGHT);
				textField.setName(object.getLabel());
				textField.setText(object.getExpr());
				textFields.add(textField);
				//vPanel.add(textField);
				if (editor.getMode()!=Mode.QUALITATIVE_MODELLING) {
					panel.add(vPanel);
					panel.add(textField);
				}
			}
		}
		return panel;
	}

	public List<String> getSelectedVariables() {
		List<String> selected = new ArrayList<String>();
		for (String name : variables.keySet()) {
			if (variables.get(name).isSelected()) {
				selected.add(name);
			}
		}
		return selected;
	}
	
	public HashMap<String, Double> getValues() throws NumberFormatException {
		HashMap<String, Double> map = new HashMap<String, Double>();
		for (JTextField field: textFields) {
			map.put(field.getName(), Double.parseDouble(field.getText()));
		}
		return map;
	}

}
