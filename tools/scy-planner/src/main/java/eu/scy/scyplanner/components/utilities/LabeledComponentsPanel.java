package eu.scy.scyplanner.components.utilities;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 19.jan.2010
 * Time: 11:45:46
 */
public class LabeledComponentsPanel extends JPanel {
    private GridBagConstraints constraints = null;
    private GridBagLayout layout = null;
    private int y = 0;

    public LabeledComponentsPanel() {
        this((LabelComponent) null);
    }

    public LabeledComponentsPanel(LabelComponent labeledComponent) {
        this(new LabelComponent[]{labeledComponent});
    }

    public LabeledComponentsPanel(LabelComponent[] labeledComponents) {
        super();

        setComponents(labeledComponents);
    }

    public void setComponents(LabelComponent[] labeledComponents) {
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(4, 4, 4, 4);
        layout = new GridBagLayout();
        setLayout(layout);

        for (LabelComponent labeledComponent : labeledComponents) {
            if (labeledComponent != null) {
                addComponent(labeledComponent.getLabel(), labeledComponent.getComponent(), null);
            }
        }
    }

    private void addComponent(JLabel label, JComponent component, JScrollPane imageLabel) {
        boolean specialCase = specialCase(component);

        //the JLabel
        if (specialCase) {
            constraints.anchor = GridBagConstraints.NORTHWEST;
        } else {
            constraints.anchor = GridBagConstraints.WEST;
        }
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = y++;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        layout.setConstraints(label, constraints);
        add(label);

        //the JComponent
        if (component == null) {
            component = new JTextField();
        }
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridx = 1;
        constraints.weightx = 1.0;
        if (specialCase) {
            constraints.weighty = 1.0;
            constraints.fill = GridBagConstraints.BOTH;
        } else {
            constraints.fill = GridBagConstraints.HORIZONTAL;
        }

        if (imageLabel != null) {
            JPanel p = new JPanel(new BorderLayout(4, 4));
            p.add(BorderLayout.CENTER, component);
            p.add(BorderLayout.EAST, imageLabel);
            component = p;
        }
        layout.setConstraints(component, constraints);
        add(component);
    }

    private boolean specialCase(JComponent component) {
        if (component instanceof JTextArea || component instanceof JScrollPane) {
            return true;
        } else {
            return false;
        }
    }
}