
import javax.swing.*;


import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.ArrayList;


import lpv.LPV;
import lpv.LPVEvent;
import lpv.LPVEventListener;






public class MyFrame extends JFrame 
implements ActionListener, ItemListener, LPVEventListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2572319814082883076L;
	//	private final static String newline = "\n";

	JPanel canvas;
	LPV lpvInstance; 
	JButton butt,butt2 ;
	JTextField textField,textField2,textField3;
	JComboBox actors;
	//PriorityQueue<String> bannedActionsQueue;
	public MyFrame(String title){
		super(title);
		//*************** Add and configure internal elements (City...)
	/*	bannedActionsQueue = new PriorityQueue<String>();
		for (String b : bannedActionsList) {
			bannedActionsQueue.add(b);
		}*/
		lpvInstance = new LPV();
		lpvInstance.addEventListener(this);
        //*************** END Add and configure internal elements (City...)
		//*************** Add all GUI elements
      //setLayout(new FlowLayout());
        //Top Panel
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
		//-Button
        butt = new JButton ( "Load SQLSpaces" ) ; 
		p.add(butt);
		//-Text Field
		p.add(new JLabel("Server:"));
		textField = new JTextField("127.0.0.1",25);
		p.add(textField);
		
		p.add(new JLabel("Port:"));
		textField3 = new JTextField("2525",3);
		p.add(textField3);
		//-Combo Box
		actors = new JComboBox(); 
		MutableComboBoxModel model = (MutableComboBoxModel) actors.getModel();
		model.addElement("No User");
		actors.addItemListener(this);
		p.add(actors);
		//-Button 2
		//butt2 = new JButton ( "Load XML" ) ; 
		butt2 = new JButton ( "Disconnect" ) ;
		p.add(butt2);
		//-Text Field 2
		//textField2 = new JTextField("c:\\data\\doc\\infomedia.uib.no\\Scy project\\logs\\actionLogsUT2(1).xml",33);
		//textField2 = new JTextField("actionLogsUT2(2).xml",33);
		textField2 = new JTextField("c:\\data\\doc\\infomedia.uib.no\\Scy project\\logs\\cyprus_lpv_3.csv",33);
		//textField2 = new JTextField("cyprus_lpv_2.csv",33);
//		p.add(textField2);
		//Top Panel add
		add(p,BorderLayout.PAGE_START);
		//Panel for drawing - canvas
		canvas = lpvInstance.getCanvas();//
		//canvas.setPreferredSize(new Dimension(width,height));
        add(canvas,BorderLayout.CENTER);
        //Set up Frame and GUI properties
        pack();
        setVisible(true);
        //*************** END Add all GUI elements
        //*************** Add action listeners
		butt.addActionListener(this) ;  
		textField.addActionListener(this);
		butt2.addActionListener(this) ;  
		textField2.addActionListener(this);
		
		//*************** END Add action listeners
     }
	
	  
	 /*public void windowClosed(WindowEvent e) {} 
	 public void windowDeactivated(WindowEvent e) {} 
	 public void windowActivated(WindowEvent e) {} 
	 public void windowDeiconified(WindowEvent e) {} 
	 public void windowIconified(WindowEvent e) {} 
	 public void windowOpened(WindowEvent e) {}
	 public void windowClosing(WindowEvent e) { 
		    setVisible(false); 
		    dispose(); 
		    System.exit(0); 
		  } */
	
	public void itemStateChanged(ItemEvent e)
    {
		
        int result = actors.getSelectedIndex();
        if(result>-1){
        	lpvInstance.setActiveUser(result);
        	
		
		if((getExtendedState() & MAXIMIZED_BOTH) == 0)	
			setSize(new Dimension(Math.max(this.getWidth(), 
					Math.max(lpvInstance.getActiveWidth(),butt.getWidth()+actors.getWidth()+textField.getWidth()+40)),
					Math.max(lpvInstance.getActiveHeight()+butt.getHeight(),this.getHeight())));
        }
	
       // validate();
      //  repaint();
    }
	
	public void actionPerformed ( ActionEvent ev )   
	{	
		Object src = ev.getSource();
		
		
		
		
		if (src == butt || src == textField ){
			String uurl = textField.getText();
			int port = Integer.parseInt(textField3.getText());
			textField.selectAll();
			lpvInstance.disconnectSQLSpaces();
			lpvInstance.connectSQLSpaces(uurl, port);
			
		}
		
		if (src == butt2){
			lpvInstance.disconnectSQLSpaces();
		}
		
		
		
		
		actors.removeAllItems();
		ArrayList<String> userNames = lpvInstance.getUserNames();
		for(int i=0; i<userNames.size(); i++)
				actors.addItem(userNames.get(i));
	}


	@Override
	public void handleLPVEvent(LPVEvent e) {
		if(e.isNewUser()){
			actors.removeAllItems();
			ArrayList<String> userNames = lpvInstance.getUserNames();
			for(int i=0; i<userNames.size(); i++)
					actors.addItem(userNames.get(i));
		}
		
	}



	


	
}
