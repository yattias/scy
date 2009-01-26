package eu.scy.collaborationservice;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import eu.scy.core.model.impl.ScyBaseObject;



public class Nutpad extends JFrame {
    private static final long serialVersionUID = -7511012297227857853L;
    private final static Logger logger = Logger.getLogger(Nutpad.class.getName());
    private static final String HARD_CODED_TOOL_NAME = "Colemo";
    private static final String INIT_USER_NAME = "<username>";

    private JTextArea editArea;
    private JTextField userNameField;
    private JButton awarenessConnectButton;
    private JFileChooser fileChooser = new JFileChooser();
    private Action openFileAction = new OpenFromFileAction();
    private Action openCSAction = new OpenFromCollaborationServiceAction();
    private Action saveToFileAction = new SaveToFileAction();
    private Action saveToCollaborationServiceAction = new SaveToCollaborationServiceAction();
    private Action exitAction = new ExitAction(); 
    private Action openAwarenessClientAction = new OpenAwarenessClientAction();

    private CollaborationService cs;
    private AwarenessClient awarenessClient;
    private String documentSqlSpaceId = null;
    private String userName = INIT_USER_NAME;
    private boolean connected = false;
    
    

    public static void main(String[] args) {
        new Nutpad();
    }
    

    public Nutpad() {

        userNameField = new JTextField(20);
        userNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        userNameField.setFont(new Font("monospaced", Font.PLAIN, 14));
        userNameField.setText(userName);
        
        awarenessConnectButton = new JButton();
        awarenessConnectButton.setAction(openAwarenessClientAction);
        awarenessConnectButton.setText("Connect");
        
        editArea = new JTextArea(15, 80);
        editArea.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        editArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        JScrollPane scrollingText = new JScrollPane(editArea);
        
        JPanel userNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        userNamePanel.add(userNameField);
        userNamePanel.add(awarenessConnectButton);
        contentPanel.add(userNamePanel, BorderLayout.NORTH);
        contentPanel.add(scrollingText, BorderLayout.CENTER);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = menuBar.add(new JMenu("File"));
        fileMenu.setMnemonic('F');
        fileMenu.add(openFileAction);
        fileMenu.add(openCSAction);
        fileMenu.add(saveToFileAction);
        fileMenu.add(saveToCollaborationServiceAction);
        fileMenu.addSeparator(); 
        fileMenu.add(exitAction);
                
        setContentPane(contentPanel);
        setJMenuBar(menuBar);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("NutPad");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
    }
    
    
    class OpenAwarenessClientAction extends AbstractAction {

        private static final long serialVersionUID = -5424901729682590512L;

        public OpenAwarenessClientAction() {
            super("Connect awareness client");
            putValue(MNEMONIC_KEY, new Integer('1'));
        }
        
        public void actionPerformed(ActionEvent event) {
        	if(connected) {
        		awarenessClient.shutDown();
        		userNameField.setEditable(true);
        		awarenessConnectButton.setText("Connect");
        		connected = false;
        		awarenessClient = null;
        	}
        	else {
        		userName = userNameField.getText();
        		if (userName == null || userName.trim().length() == 0 || userName.equals(INIT_USER_NAME)) {
        		    JOptionPane.showMessageDialog(Nutpad.this, "Dear sir. Please enter your username.");
        		} else {
        		    userNameField.setEditable(false);
        		    awarenessConnectButton.setText("Disconnect");
        		    awarenessClient = AwarenessClient.createAwarenessClient(userName, HARD_CODED_TOOL_NAME);
        		    connected = true;        		        		    
        		}
        	}
        }
    }
    

    class OpenFromFileAction extends AbstractAction {
        private static final long serialVersionUID = 2214397309885399070L;

        public OpenFromFileAction() {
            super("Open from File");
            putValue(MNEMONIC_KEY, new Integer('1'));
        }
        
        public void actionPerformed(ActionEvent event) {
            int retval = fileChooser.showOpenDialog(Nutpad.this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                FileReader reader = null;
                try {
                    reader = new FileReader(f);
                    editArea.read(reader, "");                    
                } catch (IOException e2) {
                    logger.error("Trouble while reading from file " + e2);
                    JOptionPane.showMessageDialog(Nutpad.this, "Trouble while reading from file " + e2);
                    System.exit(1);
                }
            }
        }
    }
    
    class OpenFromCollaborationServiceAction extends AbstractAction {
        private static final long serialVersionUID = -5599432544551421021L;

        public OpenFromCollaborationServiceAction() {
            super("Open random from CS");
            putValue(MNEMONIC_KEY, new Integer('2'));
        }
        
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> result = getCS().read(null, HARD_CODED_TOOL_NAME);                
            editArea.setText("");
            if (result == null) {
                JOptionPane.showMessageDialog(Nutpad.this, "I still havn't found what you're looking for");
                logger.error("Trouble while finding a Tuple. Is the space empty?");
            } else {
                logger.debug("AAAA: " + result.toString());
                documentSqlSpaceId = result.get(0); // get id
                editArea.append(result.get(result.size() - 1));                
            }
        }
    }
    

    class SaveToFileAction extends AbstractAction {

        private static final long serialVersionUID = 7773471019357144532L;

        SaveToFileAction() {
            super("Save to File");
            putValue(MNEMONIC_KEY, new Integer('3'));
        }

        public void actionPerformed(ActionEvent event) {
            int retval = fileChooser.showSaveDialog(Nutpad.this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                FileWriter writer = null;
                try {
                    writer = new FileWriter(f);
                    editArea.write(writer);
                } catch (IOException e2) {
                    logger.error("Trouble while saving to file " + e2);
                    JOptionPane.showMessageDialog(Nutpad.this, "Trouble while saving to file " + e2);
                    System.exit(1);
                }
            }
        }         
        
    }
    
    class SaveToCollaborationServiceAction extends AbstractAction {
        private static final long serialVersionUID = 2570708232031173971L;

        SaveToCollaborationServiceAction() {
            super("Save to CS");
            putValue(MNEMONIC_KEY, new Integer('4'));
        }

        public void actionPerformed(ActionEvent e) {
            write();
        }
        
    }
    

    class ExitAction extends AbstractAction {
        private static final long serialVersionUID = -7603073618047398002L;

        public ExitAction() {
            super("Exit");
            putValue(MNEMONIC_KEY, new Integer('5'));
        }
        
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
    
    
    public void write() {
        ScyBaseObject sbo = new ScyBaseObject();
        sbo.setId("12345");
        sbo.setName("a nice name for the object");
        sbo.setDescription(editArea.getText());
        documentSqlSpaceId = getCS().write(documentSqlSpaceId, HARD_CODED_TOOL_NAME, sbo); // if documentSqlSpaceId != null this will update the tuple
        if (documentSqlSpaceId != null) {
            JOptionPane.showMessageDialog(Nutpad.this, "Save OK");                
        } else {
            JOptionPane.showMessageDialog(Nutpad.this, "Call home. Something bad has happened.");  
            logger.error("Trouble while writing to CS");
        }
    } 
   
    
    private CollaborationService getCS() {
        if (cs == null || !userName.equals(userNameField.getText())) {
            userName = userNameField.getText();
            cs = CollaborationService.createCollaborationService(userName, CollaborationService.COLLABORATION_SERVICE_SPACE);                
        }
        return cs;
    }
}
