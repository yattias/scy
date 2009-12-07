package eu.scy.client.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

public class SyncMessageCreateDialog extends JDialog implements ActionListener {

    private final static Logger logger = Logger.getLogger(SyncMessageCreateDialog.class.getName());
    
    private JTextField content = new JTextField(25);
    private JTextField from = new JTextField(25);
    private JTextField to = new JTextField(25);
    private JTextField event = new JTextField(25);
    private JTextField toolId = new JTextField(25);
    private JTextField toolSessionId = new JTextField(25);
    private JTextField expiration = new JTextField(25);
    private JPanel allTextsPanel;
    
    public SyncMessageCreateDialog(JFrame parentFrame) {
        super(parentFrame, "Create ScyMessage", true);   
        initialize();
    }
    

    public SyncMessageCreateDialog(JFrame parentFrame, String userName, String toolId, String event, String toolSessionId) {
        super(parentFrame, "Create SyncMessage", true);
        this.toolSessionId.setText(toolSessionId);
        this.toolId.setText(toolId);
        this.from.setText(userName);
        this.event.setText(event);
        this.expiration.setText("1000");
        initialize();
    }
    
    
    public void initialize() {
        allTextsPanel = new JPanel(new MigLayout());        
        allTextsPanel.add(new JLabel("toolSessionId:"));
        allTextsPanel.add(toolSessionId, "wrap");
        allTextsPanel.add(new JLabel("toolId:"));
        allTextsPanel.add(toolId, "wrap");
        allTextsPanel.add(new JLabel("from:"));
        allTextsPanel.add(from, "wrap");
        allTextsPanel.add(new JLabel("to:"));
        allTextsPanel.add(to, "wrap");
        allTextsPanel.add(new JLabel("content:"));
        allTextsPanel.add(content, "wrap");
        allTextsPanel.add(new JLabel("event:"));
        allTextsPanel.add(event, "wrap");
        allTextsPanel.add(new JLabel("expiration:"));
        allTextsPanel.add(expiration, "wrap");
        
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
        String[] s = { toolSessionId.getText(), toolId.getText(), from.getText(), to.getText(), content.getText(), event.getText(), null, expiration.getText() };
        return s;
    }
}