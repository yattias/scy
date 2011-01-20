package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import colab.um.draw.JdFigure;
import colab.um.draw.JdLink;
import colab.um.draw.JdNode;
import colab.um.draw.JdRelation;
import eu.scy.client.tools.scydynamics.model.Model;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;

public class VariableDialog extends javax.swing.JDialog implements
        java.awt.event.ActionListener, java.awt.event.MouseListener {

    private final ResourceBundleWrapper bundle;
    private static final long serialVersionUID = 7666973675681502332L;
    private javax.swing.JTextField nameField = new javax.swing.JTextField(23);
    private javax.swing.JTextField valueField = new javax.swing.JTextField(23);
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

    public VariableDialog(java.awt.Frame owner, java.awt.Point position,
            JdFigure figure, ModelEditor editor, ResourceBundleWrapper bundle) {
        super(owner, false);
        this.bundle = bundle;
        editor.getActionLogger().logActivateWindow("specification", figure.getID(), this);
        this.setLocation(position);
        this.figure = figure;
        this.props = figure.getProperties();
        this.editor = editor;
        this.label = (String) props.get("label");
        setTitle(bundle.getString("VARIABLEDIALOG_TITLE")+" '" + this.label + "'");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getSpecsPanel(), BorderLayout.NORTH);
        getContentPane().add(getOkayCancelPanel(), BorderLayout.SOUTH);
        getContentPane().add(getVariablePanel(), BorderLayout.CENTER);
        getContentPane().add(getCalculatorPanel(), BorderLayout.EAST);
        this.setPreferredSize(new Dimension(440, 300));
        updateView();
        pack();
        setLocation(position);
        setVisible(true);
    }

    private Vector<String> getListItems() {
        // gets the name of all JdFigures that are linked to this.figure
        Vector<String> listItems = new Vector<String>();
        Vector<JdLink> links = editor.getModel().getLinks();
        for (JdLink link : links) {
            try {
                // some figures don't have a label (e.g. flows), catching a
                // nullpointer here
                if (link.getFigure2().getProperties().get("label").equals(
                        this.label)) {
                    listItems.add((String) link.getFigure1().getProperties().get("label"));
                }
            } catch (NullPointerException ex) {
                // do nothing
            }
        }
        return listItems;
    }

    private JButton makeButton(String s) {
        JButton b = new JButton(s);
        b.setActionCommand(s);
        b.addActionListener(this);
        return b;
    }

    private JPanel getVariablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("VARIABLEDIALOG_VARIABLES")));
        infoList = new javax.swing.JList(getListItems());
        infoList.addMouseListener(this);
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(
                infoList);
        panel.add(scrollPane);
        return panel;
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
        panel.add(valueField);

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
        panel.setBorder(new TitledBorder(bundle.getString("VARIABLEDIALOG_INPUTPAD")));
        return panel;
    }

    public void updateView() {
        nameField.setText(figure.getProperties().get("label") + "");
        valueField.setText(figure.getProperties().get("expr") + "");
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
            String express = valueField.getText();
            express = express.replaceAll("<", "");
            express = express.replaceAll(">", "");
            express = express.replaceAll("&", "");
            // cleaning some bad chars in unit
            String unit = (String) unitsBox.getSelectedItem();
            unit = unit.replaceAll("<", "");
            unit = unit.replaceAll(">", "");
            unit = unit.replaceAll("&", "");

            props.put("label", newName);
            props.put("expr", express);
            props.put("unit", unit);
            // testing qualitative relations
            /*Vector<JdLink> links = editor.getModel().getLinks();
			for (JdLink link : links) {
				try {
			        if (link.getFigure2().getProperties().get("label").equals(this.label)) {
			            if (link instanceof JdRelation){
			            	System.out.println("got you!");
			            	((JdRelation)link).setRelationType(4);
			            }
			        } 
			    } catch (Exception e) {}
			}*/
            // end test
            
			editor.setFigureProperties(oldName, props);

            if (!newColor.equals(editor.getModel().getObjectOfName((String) figure.getProperties().get("label")).getLabelColor()) || !oldName.equals(newName) || !oldExpr.equals(valueField.getText()) || !oldUnit.equals(unitsBox.getSelectedItem())) {
                // name, expression, unit or color has changed, send a change-specification-logevent
                editor.getActionLogger().logChangeSpecification(figure.getID(), newName, valueField.getText(), (String) unitsBox.getSelectedItem(), editor.getXmModel().getXML("", true));
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
            valueField.setText("");
        } else if (event.getActionCommand() == "color") {
            java.awt.Frame frame = javax.swing.JOptionPane.getFrameForComponent(this);
            ColorDialog cdialog = new ColorDialog(frame, colorButton.getLocationOnScreen(), this, bundle);
            cdialog.setVisible(true);
        } else {
            // here go the clicks of the "calculator panel"
            paste(event.getActionCommand(), valueField);
        }
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
            // if (e.getClickCount()==2) {
            // single click is okay...
            String selected = (infoList.getSelectedValue() == null ? ""
                    : (String) infoList.getSelectedValue());
            paste(selected, valueField);
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
