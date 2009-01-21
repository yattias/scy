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
    
    private Action _openAction = new OpenAction();
    private Action _saveAction = new SaveAction();
    private Action _exitAction = new ExitAction(); 
    private CollaborationService cs;
    
    private static final String HARD_CODED_TOOL_NAME = "Colemo";
    

    public static void main(String[] args) {
        new Nutpad();
    }
    

    public Nutpad() {
        
        cs = CollaborationService.createCollaborationService();
        
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
        fileMenu.add(_openAction);
        fileMenu.add(_saveAction);
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
    

    class OpenAction extends AbstractAction {
        public OpenAction() {
            super("Open...");
            putValue(MNEMONIC_KEY, new Integer('O'));
        }
        
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> result = cs.read(HARD_CODED_TOOL_NAME);
            _editArea.append(result.get(result.size() - 1));
            
//            int retval = _fileChooser.showOpenDialog(NutPad.this);
//            if (retval == JFileChooser.APPROVE_OPTION) {
//                File f = _fileChooser.getSelectedFile();
//                FileReader reader = null;
//                try {
//                    reader = new FileReader(f);
//                    _editArea.read(reader, "");  // Use TextComponent read                    
//                } catch (IOException ioex) {
//                    System.out.println(e);
//                    System.exit(1);
//                }
//            }
        }
    }
    

    class SaveAction extends AbstractAction {

        SaveAction() {
            super("Save...");
            putValue(MNEMONIC_KEY, new Integer('S'));
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
                if (writer != null) {
                    ScyBaseObject sbo = new ScyBaseObject();
                    sbo.setId("12345");
                    sbo.setName("a nice name for the object");
                    sbo.setDescription(_editArea.getText());
                    cs.write(HARD_CODED_TOOL_NAME, sbo);
                }
            }
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
