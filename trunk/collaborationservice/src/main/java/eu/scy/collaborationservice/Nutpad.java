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
    
    private final static Logger logger = Logger.getLogger(Nutpad.class.getName());
        
    private JTextArea    _editArea;
    private JFileChooser _fileChooser = new JFileChooser();
    
    private Action _openFileAction = new OpenFromFileAction();
    private Action _openCSAction = new OpenFromCollaborationServiceAction();
    private Action _saveToFileAction = new SaveToFileAction();
    private Action _saveToCollaborationServiceAction = new SaveToCollaborationServiceAction();
    private Action _exitAction = new ExitAction(); 
    private CollaborationService cs;
    
    private static final String HARD_CODED_TOOL_NAME = "Colemo";
    

    public static void main(String[] args) {
        new Nutpad();
    }
    

    public Nutpad() {
        
        cs = CollaborationService.createCollaborationService();
        cs.setUserName("jeremy");
        
        _editArea = new JTextArea(15, 80);
        _editArea.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        _editArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        JScrollPane scrollingText = new JScrollPane(_editArea);
        
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(scrollingText, BorderLayout.CENTER);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = menuBar.add(new JMenu("File"));
        fileMenu.setMnemonic('F');
        fileMenu.add(_openFileAction);
        fileMenu.add(_openCSAction);
        fileMenu.add(_saveToFileAction);
        fileMenu.add(_saveToCollaborationServiceAction);
        fileMenu.addSeparator(); 
        fileMenu.add(_exitAction);
        
        setContentPane(content);
        setJMenuBar(menuBar);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("NutPad");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    

    class OpenFromFileAction extends AbstractAction {
        public OpenFromFileAction() {
            super("Open from File");
            putValue(MNEMONIC_KEY, new Integer('1'));
        }
        
        public void actionPerformed(ActionEvent e) {
            int retval = _fileChooser.showOpenDialog(Nutpad.this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                File f = _fileChooser.getSelectedFile();
                FileReader reader = null;
                try {
                    reader = new FileReader(f);
                    _editArea.read(reader, "");  // Use TextComponent read                    
                } catch (IOException ioex) {
                    System.out.println(e);
                    System.exit(1);
                }
            }
        }
    }
    
    class OpenFromCollaborationServiceAction extends AbstractAction {
        public OpenFromCollaborationServiceAction() {
            super("Open from CS");
            putValue(MNEMONIC_KEY, new Integer('2'));
        }
        
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> result = cs.read(HARD_CODED_TOOL_NAME);
            _editArea.setText("");
            _editArea.append(result.get(result.size() - 1));
        }
    }
    

    class SaveToFileAction extends AbstractAction {

        SaveToFileAction() {
            super("Save to File");
            putValue(MNEMONIC_KEY, new Integer('3'));
        }

        public void actionPerformed(ActionEvent e) {
            int retval = _fileChooser.showSaveDialog(Nutpad.this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                File f = _fileChooser.getSelectedFile();
                FileWriter writer = null;
                try {
                    writer = new FileWriter(f);
                    _editArea.write(writer);
                } catch (IOException ioex) {
                    JOptionPane.showMessageDialog(Nutpad.this, ioex);
                    System.exit(1);
                }
            }
        }         
        
    }
    
    class SaveToCollaborationServiceAction extends AbstractAction {

        SaveToCollaborationServiceAction() {
            super("Save to CS");
            putValue(MNEMONIC_KEY, new Integer('4'));
        }

        public void actionPerformed(ActionEvent e) {
        	ScyBaseObject sbo = new ScyBaseObject();
            sbo.setId("12345");
            sbo.setName("a nice name for the object");
            sbo.setDescription(_editArea.getText());
            cs.write(HARD_CODED_TOOL_NAME, sbo);
        }         
        
    }
    

    class ExitAction extends AbstractAction {
        
        public ExitAction() {
            super("Exit");
            putValue(MNEMONIC_KEY, new Integer('X'));
        }
        
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
