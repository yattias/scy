package eu.scy.client.tools.scysimulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class NotificationGUI extends JDialog implements ActionListener {

    private final class NotificationCellRenderer implements ListCellRenderer {

        private List<Entry<String, String>> texts;
        private Frame parentFrame;

        public NotificationCellRenderer(List<Entry<String, String>> texts, Frame parentFrame) {
           this.texts = texts;
           this.parentFrame=parentFrame;
        }

        @Override
        public Component getListCellRendererComponent(final JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            String text = (String) value;
            JButton but = new JButton(text);
            but.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(final ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
//                      
                      @Override
                      public void run() {
                          Entry<String, String> entry = texts.get(list.getSelectedIndex());
                          JOptionPane.showMessageDialog(parentFrame, entry.getValue());
                      
                      }
                  });
                
                }
            });
            if (isSelected) {
                but.setBackground(Color.RED);
//                but.setForeground(Color.RED);
                but.setFont(but.getFont().deriveFont(Font.BOLD));
            } else {
                Color defaultColor = (Color) UIManager.get("button.background");
                but.setForeground(defaultColor);
                but.setBackground(defaultColor);
                but.setFont(but.getFont().deriveFont(Font.PLAIN));
            }
            return but;
        }
    }

    private JButton okButton;

//    private JList hintList;

    //private DefaultListModel model;

    private JTextField annotationTF;

    private JCheckBox showMessageAgain;

    private JPanel hintPanel;

    /**
     * The constructor for a ScenarioSaveDialog. This Dialog extends JDialog to provide a custom save dialog
     * 
     * @param parentFrame
     *            The parent Frame to show this Dialog in
     * @param title
     *            The title of this Dialog
     */
    public NotificationGUI(final Frame parentFrame, String title, final Map<String,String> texts, int highlightPos) {
        super(parentFrame, title, true);
        setResizable(false);
        setLocationRelativeTo(parentFrame);
        JPanel inputPanel = new JPanel();
        JPanel controlPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        inputPanel.setLayout(gbl);
        
        hintPanel =new JPanel(new GridLayout(texts.size(), 1));
        int count = 0;
        //hintList = new JList();
       // model = new DefaultListModel();
        for (final String text : texts.keySet()) {
            
            JButton hintButton = new JButton(text);
            hintButton.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    String value = texts.get(text);
                    JOptionPane.showMessageDialog(parentFrame,value);
                
                }
            });
            if (count==highlightPos){
                hintButton.setForeground(Color.RED);
            }
            count++;
            hintPanel.add(hintButton);
            //model.addElement(text.getKey());
        }
        //hintList.setCellRenderer(new NotificationCellRenderer(texts,parentFrame));

        //hintList.setSelectionBackground(Color.RED);
        // hintList.setEnabled(false);

//        hintList.setModel(model);
//        if (highlightPos != -1) {
//            hintList.setSelectedIndex(highlightPos);
//        }
//        hintList.addListSelectionListener(new ListSelectionListener() {
//
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                final JList source = (JList) e.getSource();
//                int componentCount = source.getComponentCount();
//                SwingUtilities.invokeLater(new Runnable() {
//                    
//                    @Override
//                    public void run() {
//                        Entry<String, String> entry = texts.get(source.getSelectedIndex());
//                        JOptionPane.showMessageDialog(parentFrame, entry.getValue());
//                    
//                    }
//                });
//
//            }
//
//            
//        });
        JScrollPane scrollPane = new JScrollPane(hintPanel);
        annotationTF = new JTextField(12);
        annotationTF.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    setVisible(false);
                }

            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

        });
        gbl.setConstraints(annotationTF, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 5, 5));
        gbl.setConstraints(scrollPane, new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 5, 5));
        inputPanel.add(scrollPane);
        // Buttons
        controlPanel.setLayout(new FlowLayout());
        showMessageAgain = new JCheckBox("Show message again?", true);
        okButton = new JButton("OK");
        controlPanel.add(showMessageAgain);
        controlPanel.add(okButton);

        okButton.addActionListener(this);
        getContentPane().add(inputPanel, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        pack();
        annotationTF.requestFocus();
    }

    /**
     * This Method prompts this Dialog on the parentFram
     * 
     * @return if the user filled the fields correctly
     */
    public boolean prompt() {
        setVisible(true);
        return true;
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == okButton) {
            setVisible(false);
        } else {
            setVisible(false);
        }
    }

    public boolean showMessageAgain() {
        return showMessageAgain.isSelected();
    }

}