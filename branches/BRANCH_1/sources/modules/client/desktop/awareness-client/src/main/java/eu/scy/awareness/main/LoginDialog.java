package eu.scy.awareness.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class LoginDialog extends JDialog implements ActionListener {
    
    JTextField usernameField;
    JPasswordField passwordField;
    JButton okButton;
    
    public LoginDialog(JFrame parentFrame) {
        super(parentFrame, "Log in", true);
        
        usernameField = new JTextField(25);
        passwordField = new JPasswordField(25);
        
        JPanel panel = new JPanel(new MigLayout());
        
        panel.add(new JLabel("Username:"));
        panel.add(usernameField, "wrap");
        panel.add(new JLabel("Password:"));
        panel.add(passwordField, "wrap");
        
        usernameField.setText("obama");
        passwordField.setText("obama");
        
        JButton submit = new JButton("Submit");
        submit.addActionListener(this);
        
        panel.add(new JLabel(""));
        panel.add(submit,"right");
        add(panel);
        setResizable(false);
        pack();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Gui stuff
    }
    
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("Submit")){
            this.setVisible(false);
        }
    }
    
    public String[] showLoginDialog() {
        setVisible(true);
        String[] s = { usernameField.getText(), new String(passwordField.getPassword()) };
        return s;
    }
}