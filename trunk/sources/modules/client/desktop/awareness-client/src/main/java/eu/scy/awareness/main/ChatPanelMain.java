package eu.scy.awareness.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import roolo.elo.api.IMetadataKey;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.chat.controller.ChatController;
import eu.scy.toolbroker.ToolBrokerImpl;


public class ChatPanelMain extends JPanel {

    
    protected JList buddyList;
    JTextPane chatArea;
    protected DefaultListModel buddlyListModel;
    protected JTextField sendMessageTextField;
    protected ChatController chatController;
    private String username ;
    private String password;
    private IAwarenessService awarenessService;
    
    public ChatPanelMain() {
  
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } 
        catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //disable login right now
//        LoginDialog loginDialog = new LoginDialog(null);
//        
//        String[] loginStrings = loginDialog.showLoginDialog();
//        
//        if( !ArrayUtils.isEmpty(loginStrings) ) {
//            this.username = loginStrings[0];
//            this.password = loginStrings[1];
//        }
//        
        
        
       ToolBrokerImpl<IMetadataKey> tbi = new ToolBrokerImpl<IMetadataKey>();
       awarenessService = tbi.getAwarenessService();
       awarenessService.init(tbi.getConnection("obama", "obama"));
        
        chatController = new ChatController(awarenessService);
        initGUI();
        chatController.populateBuddyList();
    }
    
    protected void initGUI() {
        this.add(createBuddyListPanel(), BorderLayout.WEST);

        chatArea = new JTextPane();
        
        chatArea.setEditable(false);
        this.registerChatArea(chatArea);
        
        this.add(createChatArea(), BorderLayout.CENTER);
        
        initListeners();
    }
    
    protected JPanel createChatArea() {
        
        JPanel chatAreaPanel = new JPanel(new MigLayout("wrap 1"));
        
        JScrollPane chatAreaScroll = new JScrollPane(chatArea);
        chatAreaScroll.setPreferredSize(new Dimension(225,250));
        
        chatAreaPanel.add(chatAreaScroll);
        
        
        sendMessageTextField = new JTextField();
        sendMessageTextField.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                JTextField textfield = (JTextField)e.getSource();

                String oldText = chatArea.getText();
                
                if( buddyList.getSelectedValue() == null ) {
                    chatArea.setText(oldText + "me: " + textfield.getText() + "\n");
                    chatController.sendMessage(null, textfield.getText());
                } else {
                    chatController.sendMessage(buddyList.getSelectedValue(), textfield.getText());
                    chatArea.setText(oldText + "me: " + textfield.getText() + "\n");
                }
            }});
        
        chatAreaPanel.add(sendMessageTextField, "growx");
        
        
        return chatAreaPanel;
        
    }
    
    protected JPanel createBuddyListPanel() {
        
        JPanel buddyPanel = new JPanel(new MigLayout("wrap 1"));
        
        buddyList = new JList(chatController.getBuddyList());
        buddyList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        JScrollPane buddyListScroll = new JScrollPane(buddyList);
        buddyListScroll.setPreferredSize(new Dimension(150,250));
        
        buddyPanel.add(buddyListScroll);
        
        JButton addButton = new JButton(" + ");
       
        addButton.setBorder(BorderFactory.createEtchedBorder());
        addButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                String buddy = JOptionPane.showInputDialog(null, "whos your buddy?");
                chatController.addBuddy(buddy);
            }});
        
        JButton deleteButton = new JButton(" - ");
        
        deleteButton.setBorder(BorderFactory.createEtchedBorder());
        deleteButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
          
                Object selectionValues[] = buddyList.getSelectedValues();
                for (Object buddy : selectionValues) {
                    chatController.removeBuddy((String) buddy);
                    
                }
                System.out.println("delete");
                
            }});
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        
        buddyPanel.add(buttonPanel,"center");
        return buddyPanel;
        
        
    }
    
    protected void initListeners() {
        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
              System.out.print("First index: " + listSelectionEvent.getFirstIndex());
              System.out.print(", Last index: " + listSelectionEvent.getLastIndex());
              boolean adjust = listSelectionEvent.getValueIsAdjusting();
              System.out.println(", Adjusting? " + adjust);
              if (!adjust) {
                JList list = (JList) listSelectionEvent.getSource();
                int selections[] = list.getSelectedIndices();
                Object selectionValues[] = list.getSelectedValues();
                for (int i = 0, n = selections.length; i < n; i++) {
                  if (i == 0) {
                    System.out.print("  Selections: ");
                  }
                  System.out.print(selections[i] + "/" + selectionValues[i] + " ");
                }
                System.out.println();
              }
            }
          };
          buddyList.addListSelectionListener(listSelectionListener);

          MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
              JList theList = (JList) mouseEvent.getSource();
              if (mouseEvent.getClickCount() == 2) {
                int index = theList.locationToIndex(mouseEvent.getPoint());
                if (index >= 0) {
                  Object o = theList.getModel().getElementAt(index);
                  System.out.println("Double-clicked on: " + o.toString());
                }
              }
            }
          };
          buddyList.addMouseListener(mouseListener);
    }

    public JFrame runInFrame() {
        
        JFrame frame = new JFrame("my awareness");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.setSize(450, 350);
        frame.setVisible(true);
        return frame;
    }
    
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Selecting JList");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JButton pop = new JButton("pop");
        pop.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                ChatPanelMain cmp = new ChatPanelMain();
                cmp.runInFrame();
                
            }});
        
        frame.getContentPane().add(pop);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }
    
    private void registerChatArea(final JTextPane chatArea) {
    	awarenessService.addAwarenessMessageListener(new IAwarenessMessageListener() {
			
			@Override
			public void handleAwarenessMessageEvent(IAwarenessEvent awarenessEvent) {
				String oldText = chatArea.getText();
                
                chatArea.setText(oldText+awarenessEvent.getUser() +": " + awarenessEvent.getMessage() + "\n");
				
			}
		});

    	
    }
}
