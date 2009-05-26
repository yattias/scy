package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import colab.um.draw.JdFigure;
import colab.um.draw.JdObject;
import eu.scy.client.tools.scydynamics.model.Model;


public class VariableSelectionPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 694812595907870835L;
	private Map<String, JCheckBox> variables;
	private ModelEditor editor;

	public VariableSelectionPanel(ModelEditor editor) {
		super();
		this.editor = editor;
		variables = new HashMap<String, JCheckBox>();
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("variable selection"));
		if (editor.getModel() != null) {
			updateVariables();
		}
	}

	public void updateVariables() {
		Model model = editor.getModel();
		List<String> oldSelection = this.getSelectedVariables();
		variables.clear();
		// cleaning the panel
		this.removeAll();
		// counting number of relevant variables
		int variablecount = 0;
		variablecount = variablecount + model.getAuxs().size()
				+ model.getStocks().size();
		if (variablecount == 0) {
			// no relevant variables in model
			this.add(new JLabel("<html><body>There are no plottable variables<br>(i.e. stocks or aux's) in the model.</body></html>"), BorderLayout.NORTH);
		} else {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(variablecount, 1));
			JCheckBox box;
			JdObject object;
			Enumeration<JdObject> objects = model.getNodes().elements();
			while (objects.hasMoreElements()) {
				object = objects.nextElement();
				if (object.getType() == JdFigure.STOCK) {
					box = new JCheckBox("stock: "+object.getLabel());
					if (oldSelection.contains(object.getLabel())) {
						box.setSelected(true);
					}
					panel.add(box);
					variables.put(object.getLabel(), box);
				} else if (object.getType() == JdFigure.AUX) {
					box = new JCheckBox("aux: "+object.getLabel());
					if (oldSelection.contains(object.getLabel())) {
						box.setSelected(true);
					}
					panel.add(box);
					variables.put(object.getLabel(), box);
				}
			}
			this.add(panel, BorderLayout.NORTH);
		}

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

}
