package nl.utwente.gw.modelling.editor;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;

import nl.utwente.gw.modelling.model.Model;

import colab.um.draw.JdFigure;
import colab.um.draw.JdLink;

public class VariableDialog extends javax.swing.JDialog implements java.awt.event.ActionListener, java.awt.event.MouseListener {
  
 	private static final long serialVersionUID = 7666973675681502332L;
//private String oldName;
//  private double oldValue;
  private javax.swing.JTextField nameField = new javax.swing.JTextField(23);
  private javax.swing.JTextField valueField = new javax.swing.JTextField(23);
  private javax.swing.JList infoList;
  private JdFigure figure;
private Hashtable<String, Object> props;
private Model model;
private Object label;
  
  public VariableDialog(java.awt.Frame owner, java.awt.Point position, JdFigure figure, Model model) {
      super(owner, false);
      this.figure = figure;
      this.props = figure.getProperties();
      this.model = model;
      this.label = props.get("label");
      
      javax.swing.JLabel nameLabel=new javax.swing.JLabel("Name: ");
      javax.swing.JLabel valueLabel=new javax.swing.JLabel("Value: ");

      //addWindowListener(new DynaWindowListener());
      getContentPane().setLayout(new BorderLayout());
      setTitle("Settings for '"+this.label+"'");
      JPanel northPanel = new JPanel();
      northPanel.setLayout(new java.awt.GridLayout(2,2));
      northPanel.add(nameLabel);
      northPanel.add(nameField);
      northPanel.add(valueLabel);
      northPanel.add(valueField);
      getContentPane().add(northPanel,BorderLayout.NORTH);
      
      JPanel southPanel = new JPanel();
      southPanel.setLayout(new java.awt.FlowLayout());
      javax.swing.JButton okayButton = new javax.swing.JButton("ok");
      okayButton.setActionCommand("okay");
      javax.swing.JButton cancelButton = new javax.swing.JButton("cancel");
      cancelButton.setActionCommand("cancel");
      okayButton.addActionListener(this);
      cancelButton.addActionListener(this);
      southPanel.add(okayButton);
      southPanel.add(cancelButton);
      getContentPane().add(southPanel,BorderLayout.SOUTH);
      
      JPanel midPanel = new JPanel();
      midPanel.setLayout(new BorderLayout());
      midPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("variables"));
      infoList = new javax.swing.JList(getListItems());    
      infoList.addMouseListener(this);
      javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(infoList);
      midPanel.add(scrollPane);
      getContentPane().add(midPanel,BorderLayout.CENTER);
      
      getContentPane().add(getCalculatorPanel(), BorderLayout.EAST);
      
      updateView();
      pack();
      setLocation(position);
      setVisible(true);
  }
  
  private Vector<String> getListItems() {
	  // gets the name of all JdFigures that are linked to this.figure
	  Vector<String> listItems = new Vector<String>();
	  Vector<JdLink> links = model.getLinks();
	  for (JdLink link : links) {
		  if (link.getFigure2().getProperties().get("label").equals(this.label)) {
			  listItems.add((String) link.getFigure1().getProperties().get("label"));
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
  
  private JPanel getCalculatorPanel() {
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(4,5));
      
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
      
      panel.setBorder(new TitledBorder("calculator"));
      return panel;
  }

  public void updateView() {
      nameField.setText(figure.getProperties().get("label")+"");
      valueField.setText(figure.getProperties().get("expr")+"");
  }
  
  /* This method inserts the String "s" at the right position into the JTextField "field",
   * taking a selection of text into account.
   */
  private void paste(String s, JTextField field) {
    int start = field.getSelectionStart();
    int end = field.getSelectionEnd();
    String oldText = field.getText();
    String newText = oldText.substring(0,start)+s+oldText.substring(end,oldText.length());
    field.setText(newText);
  }
  
  public void actionPerformed(java.awt.event.ActionEvent event) {
      if (event.getActionCommand()=="okay") {
          //try {
              //node.setName(nameField.getText());        
              //node.setFunction(valueField.getText());
              //props.remove("label");
              props.put("label", nameField.getText());
              figure.setProperties(props);
              setVisible(false);
              setEnabled(false);
              dispose();
              
              
          //} catch (org.gjt.fredgc.function.ParseException except) {
          //    updateView();
          //    javax.swing.JOptionPane.showMessageDialog(this, DynaProp.getProp("parsing.exception"), "parsing error", javax.swing.JOptionPane.INFORMATION_MESSAGE);   
          //}
      } else if (event.getActionCommand()=="cancel") {
          setVisible(false);
          setEnabled(false);
          dispose();
      } else if (event.getActionCommand()=="C") {
          valueField.setText("");
      } else {
          paste(event.getActionCommand(), valueField);
      }
  }
  
  /** Invoked when the mouse button has been clicked (pressed
   * and released) on a component.
   */
  public void mouseClicked(MouseEvent e) {
      if ((javax.swing.JList)e.getSource() == infoList) {                           
          //if (e.getClickCount()==2) {                                         
          // single click is okay...
          String selected = (infoList.getSelectedValue()==null?"":(String)infoList.getSelectedValue());
          paste(selected, valueField);
      }
  }
  
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  
}