package eu.scy.collaborationservice.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ScyMessageCreateDialog extends JDialog implements ActionListener {
    
    private JTextField name = new JTextField(25);
    private JTextField id = new JTextField(25);
    private JTextField description = new JTextField(25);
    
    private JTextField userName = new JTextField(25);
    private JTextField toolName = new JTextField(25);
    private JTextField objectType = new JTextField(25);
    private JTextField to = new JTextField(25);
    private JTextField from = new JTextField(25);
    private JTextField messagePurpose = new JTextField(25);
    private JTextField expiraton = new JTextField(25);
    private JTextField session = new JTextField(25);
    private JPanel allTextsPanel;
    
    public ScyMessageCreateDialog(JFrame parentFrame) {
        super(parentFrame, "Create ScyMessage", true);
        
        initialize();
        
        // Gui stuff
    }
    
    public ScyMessageCreateDialog(JFrame parentFrame, String name, String hardCodedToolName, String messagePurpose, String sessionid) {
        super(parentFrame, "Create ScyMessage", true);
        this.name.setText(name);
        id.setText(hardCodedToolName);
        this.messagePurpose.setText(messagePurpose);
        this.session.setText(sessionid);
        this.toolName.setText(hardCodedToolName);
        initialize();
    }
    
    public void initialize() {
        allTextsPanel = new JPanel(new MigLayout());
        
        allTextsPanel.add(new JLabel("name:"));
        allTextsPanel.add(name, "wrap");
        allTextsPanel.add(new JLabel("id:"));
        allTextsPanel.add(id, "wrap");
        allTextsPanel.add(new JLabel("description:"));
        allTextsPanel.add(description, "wrap");
        allTextsPanel.add(new JLabel("user name:"));
        allTextsPanel.add(userName, "wrap");
        allTextsPanel.add(new JLabel("tool name:"));
        allTextsPanel.add(toolName, "wrap");
        allTextsPanel.add(new JLabel("object type:"));
        allTextsPanel.add(objectType, "wrap");
        allTextsPanel.add(new JLabel("to:"));
        allTextsPanel.add(to, "wrap");
        allTextsPanel.add(new JLabel("from:"));
        allTextsPanel.add(from, "wrap");
        allTextsPanel.add(new JLabel("message purpose:"));
        allTextsPanel.add(messagePurpose, "wrap");
        allTextsPanel.add(new JLabel("expiration:"));
        allTextsPanel.add(expiraton, "wrap");
        allTextsPanel.add(new JLabel("session:"));
        allTextsPanel.add(session, "wrap");
        
        JButton submit = new JButton("Submit");
        submit.addActionListener(this);
        
        allTextsPanel.add(new JLabel(""));
        allTextsPanel.add(submit, "right");
        add(allTextsPanel);
        setResizable(false);
        pack();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Submit")) {
            this.setVisible(false);
        }
    }
    
    public String[] showDialog() {
        setVisible(true);
        String[] s = { name.getText(), id.getText(), description.getText(), userName.getText(), toolName.getText(), objectType.getText(), to.getText(), from.getText(), messagePurpose.getText(), expiraton.getText(), session.getText() };
        return s;
    }
}