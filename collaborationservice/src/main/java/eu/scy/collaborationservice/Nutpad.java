package eu.scy.collaborationservice;


import java.awt.BorderLayout;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import eu.scy.core.model.impl.ScyBaseObject;



public class Nutpad extends JFrame {
    private static final long serialVersionUID = -7511012297227857853L;
    private final static Logger logger = Logger.getLogger(Nutpad.class.getName());
    private static final String HARD_CODED_TOOL_NAME = "Colemo";
    private static final String HARD_CODED_USER_NAME = "thomasd";
        
    private JTextArea    editArea;
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
    
    

    public static void main(String[] args) {
        new Nutpad();
    }
    

    public Nutpad() {
        
        cs = CollaborationService.createCollaborationService(HARD_CODED_USER_NAME, CollaborationService.COLLABORATION_SERVICE_SPACE);
        
        editArea = new JTextArea(15, 80);
        editArea.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        editArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        JScrollPane scrollingText = new JScrollPane(editArea);
        
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(scrollingText, BorderLayout.CENTER);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = menuBar.add(new JMenu("File"));
        fileMenu.setMnemonic('F');
        fileMenu.add(openFileAction);
        fileMenu.add(openCSAction);
        fileMenu.add(saveToFileAction);
        fileMenu.add(saveToCollaborationServiceAction);
        fileMenu.addSeparator(); 
        fileMenu.add(exitAction);
        
        JMenu connectMenu = menuBar.add(new JMenu("Connect"));
        connectMenu.add(openAwarenessClientAction);
        
        setContentPane(content);
        setJMenuBar(menuBar);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("NutPad");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        // make sure that we have a documentSqlSpaceId
        write();
        // remove it right away so we don't clutter the db with empyt tuples
        cs.takeById(documentSqlSpaceId);
    }
    
    
    class OpenAwarenessClientAction extends AbstractAction {

        private static final long serialVersionUID = -5424901729682590512L;

        public OpenAwarenessClientAction() {
            super("Awareness client");
            putValue(MNEMONIC_KEY, new Integer('1'));
        }
        
        public void actionPerformed(ActionEvent event) {
            awarenessClient = AwarenessClient.createAwarenessClient(HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME);
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
            super("Open from CS");
            putValue(MNEMONIC_KEY, new Integer('2'));
        }
        
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> result = cs.take(HARD_CODED_TOOL_NAME);
            editArea.setText("");
            editArea.append(result.get(result.size() - 1));
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
        documentSqlSpaceId = cs.write(documentSqlSpaceId, HARD_CODED_TOOL_NAME, sbo); // if documentSqlSpaceId != null this will update the tuple
    }         
}
