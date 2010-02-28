/*
 * Created on 01.okt.2004
 *
 * 
 */
package eu.scy.colemo.server.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import eu.scy.colemo.server.exceptions.ClassNameAlreadyExistException;
import eu.scy.colemo.server.agent.StartVote;
import eu.scy.colemo.server.network.Client;

import eu.scy.colemo.server.test.XMLFileViewer;
import eu.scy.colemo.server.uml.UmlDiagram;
import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.network.Person;
import eu.scy.colemo.network.LogOn;
import eu.scy.colemo.network.NetworkMessage;

/**
 * @author Øystein
 *
 * 
 */
public class MainFrame extends JFrame implements ActionListener, WindowListener, TextListener,MouseListener{
	private JToolBar toolbar;
	private JButton addClass, save, load, connect,addAbstract,addInterface,disconnect;
	private GraphicsDiagram gDiagram;
	private JTextArea textArea;
	private Selectable selected;
	private final JFileChooser fc=new JFileChooser();
	private String user;
	private Client client;
	private JPanel southPanel, chatPanel, northPanel;
	private JScrollPane scrollPane;
	//private TextField inputField;
	private ChatPane chatPane;
	private JList userList;
	private boolean typing=false;
	private int emptyText=0;  
	private JMenuItem connectToServer;
	private JMenuItem disconnectFromServer;
	
	/**
	 * 
	 */
	public MainFrame() {
		//Avslutting av programmet styres nå av WindowClosing
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		//Adder windowListener
		this.addWindowListener(this);
		
		//Rammen som alt skal ligge på
		this.setTitle("UML CLASS MODULE");
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setLocation(250,150);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
		
		//Lage meny oppe
		createMenu();
		
		URL url=null;		
		Class MainFrame=this.getClass();
		
		url =MainFrame.getResource("icon.png");
		
		ImageIcon image = new ImageIcon(url);
	    this.setIconImage(image.getImage());
	    
		//Panelet som vi skal tegne på
		gDiagram = new GraphicsDiagram(new UmlDiagram(),this);
		gDiagram.setBackground(Color.white);
		gDiagram.setPreferredSize(new Dimension(1800,1200));
																		
		//Panelet som skal ligge i sør(skal ha JTextArea og JList i seg
		southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		southPanel.setPreferredSize(new Dimension(1000,300));
		
        //Chatpanelet
		chatPanel=new JPanel();
		chatPanel.setLayout(new BorderLayout());
					
		//Inputfield
		//inputField = new TextField("Enter your chat messages here! Press enter to send!");
		//inputField.setBackground(Color.LIGHT_GRAY);
		//inputField.addMouseListener(this);
		
		//User list
		Vector v= new Vector();
		userList = new JList(v);
		userList.setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
		userList.setBackground(new Color(232,215,176));
					
		//Legger chatPanelet på panelet i sør
		southPanel.add(chatPanel,BorderLayout.CENTER);
		southPanel.add(userList,BorderLayout.EAST);
										
		//Legger textArea og textField til i chatpanelet
		chatPane=new ChatPane();
		chatPanel.add(chatPane,BorderLayout.CENTER);
		chatPanel.add(new JPanel());//inputField,BorderLayout.SOUTH);
		
		//Toolbaren med hvor du kan velge hva du vil gjøre
		toolbar = new JToolBar();
		northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		northPanel.setPreferredSize(new Dimension(0,30));
		northPanel.add(toolbar);
		
		//Lager knappene med tilhørende icon		
		url=MainFrame.getResource("addClass.png");
		addClass=new JButton(new ImageIcon(url));
		
		url=null;
		url=MainFrame.getResource("save.png");
		save=new JButton(new ImageIcon(url));
		
		url=null;
		url=MainFrame.getResource("open.png");
		load=new JButton(new ImageIcon(url));
		
		url=null;
		url=MainFrame.getResource("connect.png");
		connect=new JButton(new ImageIcon(url));
		
		url=null;
		url=MainFrame.getResource("disconnect.png");
		disconnect=new JButton(new ImageIcon(url));
		
		url=null;
		url=MainFrame.getResource("addAbstract.png");
		addAbstract=new JButton(new ImageIcon(url));
		
		url=null;
		url=MainFrame.getResource("addInterface.png");
		addInterface=new JButton(new ImageIcon(url));
		
		//Tool tip
		addClass.setToolTipText("Adds a class");
		save.setToolTipText("Saves current diagram");
		load.setToolTipText("Loads a diagram");
		addAbstract.setToolTipText("Adds an abstract class");
		addInterface.setToolTipText("Adds an interface");
		connect.setToolTipText("Connects to the server");
		disconnect.setToolTipText("Disconnect & quit application");
		
		//Legger actionListener til på knappene
		addClass.addActionListener(this);
		save.addActionListener(this);
		load.addActionListener(this);
		connect.addActionListener(this);
		disconnect.addActionListener(this);
		//inputField.addActionListener(this);
		addAbstract.addActionListener(this);
		addInterface.addActionListener(this);
		//inputField.addTextListener(this);
	
		//Lager shortcuts til knappene (alt+x)
		addClass.setMnemonic(KeyEvent.VK_A);
		save.setMnemonic(KeyEvent.VK_S);
		load.setMnemonic(KeyEvent.VK_L);
		
		//Legger knappene til toolbaren
		toolbar.add(addClass);
		toolbar.add(addAbstract);
		toolbar.add(addInterface);
		toolbar.add(save);
		toolbar.add(load);
		toolbar.add(connect);
		
		scrollPane= new JScrollPane(gDiagram);
		
		//Legger toolbaren, panelet og textfeltet til på rammen
		this.getContentPane().add(northPanel,BorderLayout.NORTH);
		this.getContentPane().add(scrollPane,BorderLayout.CENTER);
		this.getContentPane().add(southPanel,BorderLayout.SOUTH);
		
		this.setVisible(true);
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		UmlDiagram diagram=gDiagram.getUmlDiagram();
		
		if(ae.getSource() instanceof JMenuItem && ae.getActionCommand()=="Save diagram"){
			save();
		}
		else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand()=="Open diagram"){
			load();
		}
		else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand()=="Open XML"){
			XMLFileViewer xml = new XMLFileViewer();
		}
		else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand()=="Exit"){
			int i =JOptionPane.showConfirmDialog(this,"Are you sure you want to quit the session?");
			if(i==0) {
				System.exit(0);
			}
		}
		else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand()=="Add new class"){
			addClass(diagram,"c");
		}
		else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand()=="Add new interface"){
			addClass(diagram,"i");
		}
		else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand()=="Add new abstract class"){
			addClass(diagram,"a");
		}
		else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand()=="Created by"){
			JOptionPane.showMessageDialog(this,"Øystein \"The Man\" Pettersen \n"+
					"Roger \"The Killer\" Pedersen \n"+
					"@University of Bergen");
		}
		else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand()=="Version"){
			JOptionPane.showMessageDialog(this,"UML: ULTIMATE CLASS MODELLING TOOL \n" +
					"Version: 0.94 \n"+"License: 4353 3536 3259 3091");
		}
		else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand()=="Connect to server"){
			connect();
			connectToServer.setEnabled(false);
			disconnectFromServer.setEnabled(true);
		}
		else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand()=="Disconnect from server"){
			int i =JOptionPane.showConfirmDialog(this,"Are you sure you want to quit the session?");
			if(i==0) {
				System.exit(0);
			}
		}
		
		else if(ae.getSource()==connect) {
			connectToServer.setEnabled(false);
			disconnectFromServer.setEnabled(true);
			connect();
		}
		/*else if(ae.getSource()==inputField) {
			String input = inputField.getText();
			Chat chat = new Chat(input,client.getConnection().getSocket().getLocalAddress(),client.getPerson());
			client.getConnection().send(chat);
			inputField.setText("");
		}*/
		else if(ae.getSource()==save) {
			System.out.println("save");
			save();
		}
		else if(ae.getSource()==disconnect) {
			int i =JOptionPane.showConfirmDialog(this,"Are you sure you want to quit the session?");
			if(i==0) {
				System.exit(0);
			}
		}
		else if(ae.getSource()==load) {
			load();
		}
		
		else{
			
			if(ae.getSource()==addClass){
			  	String type=new String("c");
			  	addClass(diagram,type);
			}
			if(ae.getSource()==addAbstract){
				String type=new String("a");
				addClass(diagram,type);
			}
			if(ae.getSource()==addInterface){
				String type=new String("i");
				addClass(diagram,type);
			}		
		}
	}
	public void addClass(UmlDiagram diagram,String type){
			String name =JOptionPane.showInputDialog(this, "Please type name of new class:");
			if(name!=null){
				try {
					if(!gDiagram.getUmlDiagram().nameExist(name)){
						AddClass addClass = new AddClass(name,type,client.getPerson().getUserName(),client.getConnection().getSocket().getLocalAddress(),client.getPerson());
					
						client.getConnection().send(addClass);
					}
					else{
						JOptionPane.showMessageDialog(this,"This class already exists!");
					}
				} catch (ClassNameAlreadyExistException e) {
					e.printStackTrace();
				}	
			}	
	}
	
	public void createMenu(){
		//Where the GUI is created:
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		
		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);

		//a group of JMenuItems
		JMenuItem saveFile = new JMenuItem("Save diagram",KeyEvent.VK_S);
		saveFile.addActionListener(this);
		menu.add(saveFile);

		JMenuItem openFile = new JMenuItem("Open diagram",KeyEvent.VK_O);
		openFile.addActionListener(this);
		menu.add(openFile);
		
		JMenuItem openXMLFile = new JMenuItem("Open XML");
		openXMLFile.addActionListener(this);
		menu.add(openXMLFile);
		
		menu.addSeparator();

		JMenuItem exit = new JMenuItem("Exit",KeyEvent.VK_X);
		exit.addActionListener(this);
		menu.add(exit);

		//Build second menu in the menu bar.
		menu = new JMenu("Edit");
		menu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(menu);
		
		//a group of JMenuItems for the edit menu
		JMenuItem newClass = new JMenuItem("Add new class",KeyEvent.VK_C);
		newClass.addActionListener(this);
		menu.add(newClass);

		JMenuItem newAbstract = new JMenuItem("Add new abstract class",KeyEvent.VK_A);
		newAbstract.addActionListener(this);
		menu.add(newAbstract);

		JMenuItem newInterface = new JMenuItem("Add new interface",KeyEvent.VK_I);
		newInterface.addActionListener(this);
		menu.add(newInterface);
		
		//Build third menu in the menu bar.
		menu = new JMenu("Connect");
		menu.setMnemonic(KeyEvent.VK_C);
		menuBar.add(menu);
		
		//a group of JMenuItems for the edit menu
		connectToServer = new JMenuItem("Connect to server",KeyEvent.VK_C);
		connectToServer.addActionListener(this);
		menu.add(connectToServer);
		
		disconnectFromServer = new JMenuItem("Disconnect from server",KeyEvent.VK_C);
		disconnectFromServer.setEnabled(false);
		disconnectFromServer.addActionListener(this);
		menu.add(disconnectFromServer);
		
		//Build fourth menu in the menu bar.
		menu = new JMenu("About");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);
		
		//a group of JMenuItems for the edit menu
		JMenuItem createdBy = new JMenuItem("Created by",KeyEvent.VK_C);
		createdBy.addActionListener(this);
		menu.add(createdBy);
		
		JMenuItem version = new JMenuItem("Version",KeyEvent.VK_V);
		version.addActionListener(this);
		menu.add(version);


		this.setJMenuBar(menuBar);
	}
	
	//Tegner ut alle elementene på nytt
	public void revalidate(){
		this.invalidate();
		this.validate();
	    this.repaint();
	}
	
	public void windowActivated(WindowEvent arg0) {}
	
	public void windowClosed(WindowEvent arg0) {}
	
	public void windowClosing(WindowEvent arg0) {
		int i =JOptionPane.showConfirmDialog(this,"Are you sure you want to quit the session?");
		if(i==0) {
			System.exit(0);
		}
	}

	public void windowDeactivated(WindowEvent arg0) {}

	public void windowDeiconified(WindowEvent arg0) {}
	
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
	
	public void setSelected(Selectable selected) {
		if(this.selected!=null) {
			this.selected.setSelected(false);
		}
		this.selected=selected;
		this.selected.setSelected(true);
		this.revalidate();
	}
	public Selectable getSelected(){
		return selected;
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	public Client getClient() {
		return client;
	}
	public GraphicsDiagram getGraphicsDiagram() {
		return gDiagram;
	}
	
	public ChatPane getChatPane() {
		return chatPane;
	}
	public void agentMessage(String message){
		JOptionPane.showMessageDialog(this,message);
	}
	public void voteMessage(StartVote start){
	
		int answer = JOptionPane.showConfirmDialog(
			    this,start.getUser()+" would like to delete this class: "+start.getClas()+"\n"+
				"Do you want to delete this class?",
			    "Vote for deletion of class",
			    JOptionPane.YES_NO_OPTION);
		Integer a = new Integer(answer);
		client.getConnection().send(a);
	}
	
	public void updateUsers(Vector v) {
		v.add(0,"Online Users:    ");
		userList.setListData(v);
	}
	public void save(){
		int returnVal = fc.showSaveDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION){			
			File saveFile=null;
			saveFile = fc.getSelectedFile();
			String project = fc.getSelectedFile().getName();
					
				try {
					FileOutputStream fileOut = new FileOutputStream(saveFile);
					ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
					objectOut.writeObject(this.gDiagram.getUmlDiagram());
					objectOut.flush();
					this.setTitle("UML Class Module - Logged in as "+user+". Project: "+project);
				} 	
					catch (FileNotFoundException e) {
						e.printStackTrace();
					} 	
					catch (IOException e) {
						e.printStackTrace();
					}
				}
		}
	
	public void load(){
		int returnVal = fc.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File fil = fc.getSelectedFile();	
			String project = fc.getSelectedFile().getName();
	    	    
				try {
					FileInputStream fileIn = new FileInputStream(fil);
					ObjectInputStream objectIn = new ObjectInputStream(fileIn);
					gDiagram.updateUmlDiagram((UmlDiagram)objectIn.readObject());
			
					this.setTitle("UML Class Module - Logged in as "+user+". Project: "+project);
					gDiagram.revalidate();
					gDiagram.repaint();
				} 
					catch (FileNotFoundException e) {
						e.printStackTrace();
					} 		
					catch (IOException e) {
						e.printStackTrace();
					} 
					catch (ClassNotFoundException e) {
					e.printStackTrace();
					}
				}
		}
	
	public void connect() {
		UserDialog login = new UserDialog();
		user=login.userName;
		String serverip=login.serverip;
		if(!user.equals("")){
		    if(!login.cancel){
				Person person = new Person(user);
				client = new Client(this,person,serverip);
				NetworkMessage message = new NetworkMessage(client.getConnection().getSocket().getLocalAddress(),client.getPerson(),new LogOn(client.getPerson()),"connect");
				client.getConnection().send(message);
				this.setTitle("UML Class Module - Logged in as "+user);
				toolbar.remove(connect);
				toolbar.add(disconnect);
			}
			
		}
		else{
			JOptionPane.showMessageDialog(this,"Surely you must come up with an user name!");
		}
	}
	/* (non-Javadoc)
	 * @see java.awt.event.TextListener#textValueChanged(java.awt.event.TextEvent)
	 */
	public void textValueChanged(TextEvent te) {
		/*if(inputField.getText().length()!=0 && typing==false){
			//Send at denne bruker skriver
			UserTyping typing = new UserTyping(client.getPerson().getUserName(),true);
			client.getConnection().send(typing);
			this.typing = true;
		}
		else if(inputField.getText().length()==0 && typing ==true){
			//Send at denne bruker IKKE skriver
			this.typing=false;
			UserTyping typing = new UserTyping(client.getPerson().getUserName(),false);
			client.getConnection().send(typing);
		}
		*/
	}
	
	public void mouseClicked(MouseEvent arg0) {}
	
	public void mouseEntered(MouseEvent arg0) {}
	
	public void mouseExited(MouseEvent arg0) {}
	
	public void mousePressed(MouseEvent arg0) {
		if(emptyText==0){
			//inputField.setText("");
			emptyText=1;
		}
	}
	
	public void mouseReleased(MouseEvent arg0) {}
}