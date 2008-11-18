package eu.scy.colemo.network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.*;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import org.dom4j.Document;
import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.contributions.AddField;
import eu.scy.colemo.contributions.AddLink;
import eu.scy.colemo.contributions.AddMethod;
import eu.scy.colemo.contributions.AssociateClass;
import eu.scy.colemo.contributions.Chat;
import eu.scy.colemo.contributions.ClassMoving;
import eu.scy.colemo.contributions.DeleteAssociation;
import eu.scy.colemo.contributions.DeleteClass;
import eu.scy.colemo.contributions.DeleteField;
import eu.scy.colemo.contributions.DeleteLink;
import eu.scy.colemo.contributions.DeleteMethod;
import eu.scy.colemo.contributions.MoveClass;
import eu.scy.colemo.contributions.Rename;
import eu.scy.colemo.contributions.UserTyping;
import eu.scy.colemo.agent.EndVote;
import eu.scy.colemo.agent.StartVote;
import eu.scy.colemo.agent.VoteResult;
import eu.scy.colemo.test.EvaluateRules;
import eu.scy.colemo.test.XMLActionLogWriter;
import eu.scy.colemo.test.ManageUsers;
import eu.scy.colemo.server.uml.UmlDiagram;
import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.colemo.server.uml.UmlAssociation;

/**
 * Example Server program using TCP.
 */

public class Server implements Receiver, WindowListener, ActionListener{
	public static final int PORT = 8800;
	private ServerSocket server;
	private Vector clients = new Vector();
	private UmlDiagram umlDiagram = new UmlDiagram();
	private long serverStarted;
	
	private static String NEW_LINE="\n";
	private boolean dialogMode=true;
	private VoteResult result;
	private OnlineUsers users= new OnlineUsers();
	private Vector userInfo;
	
	private XMLActionLogWriter xml;
	private ManageUsers xmlusers;
	private EvaluateRules evaluate;
		
	//Grafiske variabler
	private JFrame myFrame;
	private JTextArea text;
	private JToolBar toolbar;
	private JButton mode;
	

	public static void main(String args[]) {
		 
		Server s = new Server();
		s.run();
				
	}
	public void run() {
	    createGraphics();
				
		try{
			server = new ServerSocket(PORT);
			text.append("Server is started and running on port "+ PORT+NEW_LINE);
						
			serverStarted=System.currentTimeMillis();

			xml = new XMLActionLogWriter();
			xmlusers = new ManageUsers();
		    evaluate = new EvaluateRules(this);
		    userInfo=new Vector();
			
			//Sjekker for klienter som vil koble på
			for(;;) {
				Socket client = server.accept();
				
				text.append("Client connected from: "+client.getInetAddress()+NEW_LINE);
				Connection handler = new Connection(client,this);
				clients.add(handler);
			
				clientLoggedOn(handler.getLogOn(),client.getInetAddress());
				
				NetworkMessage message = new NetworkMessage(null,null,umlDiagram,null);
				handler.start();
				
				handler.send(message);
				
			}
		}
		catch(IOException ioe) {
			text.append("Error - "+ioe+NEW_LINE);	
		}
		
	}
	private void clientLoggedOn(LogOn logon,InetAddress ip) {
	  	
	    xmlusers.writeToXML(logon.getPerson().getUserName(),"loggedOn",ip,System.currentTimeMillis());
	    
	    users.addUser(logon.getPerson());
		users = new OnlineUsers(users.getAll(),users.getSize());
		Vector v = new Vector();
		for(int i=0;i<users.getSize();i++) {
			v.add(users.personAt(i).getUserName());
		}
		for(int i=0;i<clients.size();i++) {
			Connection connection = (Connection)clients.elementAt(i);
			
			connection.send(logon);
			connection.send(v);
		}
		userInfo.add(new UserInfo(logon.getPerson().getUserName(),ip));
	
	}
	/* (non-Javadoc)
	 * @see eu.scy.colemo.server.network.Receiver#receive(java.lang.Object)
	 */
	synchronized public void receive(Object o) {
        System.out.println("SERVER RECEIVING: " + o.getClass().getName());
	    long receivedTime=System.currentTimeMillis();
	    
		if (o instanceof NetworkMessage) {
		    NetworkMessage message = (NetworkMessage)o;	
			
		}
		if (o instanceof StartVote) {
			StartVote voting = (StartVote)o;
			result = new VoteResult(voting.getIp(),voting.getUser(),voting.getClas());
			result.setMax(clients.size());
			for(int i=0;i<clients.size();i++) {
				Connection connection = (Connection)clients.elementAt(i);
				connection.send(voting);
			}		
			
		}
		if(o instanceof Chat) {
			sendObject(o);
			Chat chat =(Chat)o;
		
			xml.writeActionToXML("chat",chat.getPerson().getUserName(),chat.getInput(),chat.getIp(),receivedTime);
			xmlusers.updateUser("chat",chat.getPerson().getUserName(),receivedTime);
		
			updatePlot(receivedTime);
		}
		if(o instanceof Integer){
			receive((Integer)o);
		}
		if(o instanceof ClientDisconnected){
			receive((ClientDisconnected)o);
		}
		if(o instanceof AddClass) {
			AddClass add = (AddClass)o;
			UmlClass umlClass = new UmlClass(add.getName(),add.getType(),add.getAuthor());
			umlDiagram.addDiagramData(umlClass);
			
			sendObject(add);
		
			xml.writeActionToXML("addClass",add.getAuthor(),add.getName(),add.getIp(),receivedTime);
			xmlusers.updateUser("addClass",add.getAuthor(),receivedTime);
			
			updatePlot(receivedTime);
			
			//Finne om en bruker har chattet relevant
			String user = xml.findStuff(add.getName());
			if(user!=null){
			    xmlusers.updateUser("relevantChat",user,receivedTime);
			}
		}
		if(o instanceof AddLink) {
			AddLink add= (AddLink)o;
			UmlLink umlLink = new UmlLink(add.getFrom(),add.getTo(),add.getUser());
			umlDiagram.addLink(umlLink);
			
			sendObject(add);

			xml.writeActionToXML("addLink",add.getUser(),add.getFrom()+add.getTo(),add.getIp(),receivedTime);
			xmlusers.updateUser("addLink",add.getUser(),receivedTime);
			
			updatePlot(receivedTime);
		}
		
		if(o instanceof AssociateClass){
			AssociateClass add =(AssociateClass)o;
			UmlAssociation umlAssociation = new UmlAssociation(add.getFrom(),add.getTo(),add.getUser());
			umlDiagram.addAssociation(umlAssociation);
			
			sendObject(add);

			xml.writeActionToXML("addLink",add.getUser(),add.getFrom()+add.getTo(),add.getIp(),receivedTime);
			xmlusers.updateUser("addLink",add.getUser(),receivedTime);
			
			updatePlot(receivedTime);
		}
		
		if(o instanceof AddField) {
			AddField add = (AddField)o;
			umlDiagram.addField(add.getUmlClass(),add.getField());
			
			sendObject(add);

			xml.writeActionToXML("addField",add.getPerson().getUserName(),add.getField(),add.getIp(),receivedTime);
			xmlusers.updateUser("addField",add.getPerson().getUserName(),receivedTime);
			
			updatePlot(receivedTime);
		}
		if(o instanceof AddMethod) {
		    AddMethod add = (AddMethod)o;
			umlDiagram.addMethod(add.getUmlClass(),add.getMethod());
			
			sendObject(add);
		
			xml.writeActionToXML("addMethod",add.getPerson().getUserName(),add.getMethod(),add.getIp(),receivedTime);
			xmlusers.updateUser("addMethod",add.getPerson().getUserName(),receivedTime);
			
			updatePlot(receivedTime);
		}
		if(o instanceof DeleteField) {
			DeleteField delete = (DeleteField)o;
			umlDiagram.deleteField(delete.getUmlClass(),delete.getField());
			
			sendObject(delete);

			xml.writeActionToXML("deleteField",delete.getPerson().getUserName(),delete.getField(),delete.getIp(),receivedTime);
			xmlusers.updateUser("deleteField",delete.getPerson().getUserName(),receivedTime);
			
			updatePlot(receivedTime);
		}

		if(o instanceof DeleteMethod) {
			DeleteMethod delete= (DeleteMethod)o;
			umlDiagram.deleteMethod(delete.getUmlClass(),delete.getMethod());
			
			sendObject(delete);

			xml.writeActionToXML("deleteMethod",delete.getPerson().getUserName(),delete.getMethod(),delete.getIp(),receivedTime);
			xmlusers.updateUser("deleteMethod",delete.getPerson().getUserName(),receivedTime);
			
			updatePlot(receivedTime);
		}
		
		if(o instanceof MoveClass) {
			MoveClass move = (MoveClass)o;
			umlDiagram.updateClass(move.getUmlClass());
			
			sendObject(move);

			xml.writeActionToXML("move",move.getPerson().getUserName(),move.getUmlClass().getName(),move.getIp(),receivedTime);
			xmlusers.updateUser("move",move.getPerson().getUserName(),receivedTime);
			
			updatePlot(receivedTime);
		}
		
		if(o instanceof DeleteLink) {
			DeleteLink delete= (DeleteLink)o;
			umlDiagram.deleteLink(delete.getUmlLink());
			
			sendObject(delete);

			xml.writeActionToXML("deleteLink",delete.getPerson().getUserName(),delete.getUmlLink().getFrom()+delete.getUmlLink().getTo(),delete.getIp(),receivedTime);
			xmlusers.updateUser("deleteLink",delete.getPerson().getUserName(),receivedTime);
			
			updatePlot(receivedTime);
		}
		
		if(o instanceof DeleteAssociation) {
			DeleteAssociation delete= (DeleteAssociation)o;
			umlDiagram.deleteAssociation(delete.getUmlAssociation());
			
			sendObject(delete);

			xml.writeActionToXML("deleteLink",delete.getPerson().getUserName(),delete.getUmlAssociation().getFrom()+delete.getUmlAssociation().getTo(),delete.getIp(),receivedTime);
			xmlusers.updateUser("deleteLink",delete.getPerson().getUserName(),receivedTime);
			
			updatePlot(receivedTime);
		}
		
		if(o instanceof Rename) {
			Rename rename = (Rename)o;
			if(rename.getType().equals("class")){
				umlDiagram.renameClass(rename.getUmlClass(),rename.getNewName());
			}
			else if(rename.getType().equals("field")){
				umlDiagram.renameField(rename.getUmlClass(),rename.getNewName(),rename.getOldName());
			}
			else if(rename.getType().equals("method")){
				umlDiagram.renameMethod(rename.getUmlClass(),rename.getNewName(),rename.getOldName());
			}
			
			sendObject(o);	
		}
		
		if(o instanceof UserTyping) {
			UserTyping userTyping =(UserTyping)o;
			
			for(int i=0;i<users.getSize();i++) {
				if(users.personAt(i).getUserName().equals(userTyping.getUser())){
					users.personAt(i).setTyping(userTyping.isTyping());
				}
			}
			ObjectList list = new ObjectList();
			for(int i=0;i<users.getSize();i++) {
				if(users.personAt(i).isTyping()){
					list.add(users.personAt(i).getUserName()+" [Typing]");
				}
				else{
					list.add(users.personAt(i).getUserName());
				}
			}
			sendObject(list);
		}
		
		if(o instanceof DeleteClass){
			DeleteClass delete = (DeleteClass)o;
			umlDiagram.deleteClass(delete.getUmlClass());
			
			sendObject(delete);

			xml.writeActionToXML("deleteClass",delete.getPerson().getUserName(),delete.getUmlClass().getName(),delete.getIp(),receivedTime);
			xmlusers.updateUser("deleteClass",delete.getPerson().getUserName(),receivedTime);
			
			updatePlot(receivedTime);
			
		}
		
		if(o instanceof ClassMoving){
			ClassMoving classMoving =(ClassMoving)o;
			umlDiagram.updateClass(classMoving.getUmlClass());
			
			sendObject(classMoving,classMoving.getIp());
			
		}
	}
	
	
	public void receive(Integer a){
		int answer=a.intValue();
		
		result.add(answer);
		if(result.getNo()+result.getYes()==result.getMax()){
			text.append("Resultatet ble, Ja:"+result.getYes()+" Nei: "+result.getNo()+NEW_LINE);
			//	Popup boks hos den som startet vote
			if(result.getYes()>result.getNo()){
				getClient(result.getIp()).send(new EndVote(result.getClas(),true));
			}
			else{
				getClient(result.getIp()).send(new EndVote(result.getClas(),false));
			}
			for(int i=0;i<clients.size();i++) {
				Connection connection = (Connection)clients.elementAt(i);
				connection.send(result);
			}
		}
		
	}
	
	public void sendObject(Object o) {
		//Send obejct til alle clientene
		for(int i=0;i<clients.size();i++) {
			Connection connection = (Connection)clients.elementAt(i);
			connection.send(o);
		}
	}
	public void sendObject(Object o,InetAddress ip) {
		//Send obejct til alle clientene
		for(int i=0;i<clients.size();i++) {
			Connection connection = (Connection)clients.elementAt(i);
			if(!connection.equals((Connection)getClient(ip))){
				connection.send(o);
			}
		}
	}
	
	public void receive(ClientDisconnected client){
		Connection connection = client.getConnection();
		System.out.println("IP DISCONNECTED: "+connection.getSocket().getInetAddress());
		
		clients.remove(connection);
		connection.close();
		
		//Gi beskjed til resten
	}
	
	public Connection getClient(InetAddress ip) {
		for (int i=0;i<clients.size();i++) {
			Connection connection =(Connection)clients.elementAt(i);
			InetAddress address = connection.getSocket().getInetAddress();
			String first = ip.toString();
			String second = address.toString();
			if (first.equals(second)){
				return connection;
			}		
		}
			return null;
	}
	
	//Lager et grafisk vindu for server
	public void createGraphics() {
		myFrame = new JFrame("My JFrame");
		toolbar = new JToolBar();
		text = new JTextArea(10,40);
		mode = new JButton("Agent: change mode");
		mode.addActionListener(this);
				
		toolbar.add(mode);
				
		myFrame.setSize(400,250);
		myFrame.setLocation(0,100);
		myFrame.getContentPane().add("Center", text);
		myFrame.getContentPane().add("North",toolbar);
		myFrame.setVisible(true);
		
	}
	
	/**
	 * @return Returns the clients.
	 */
	public Vector getClients() {
		return clients;
	}
	
	public boolean isDialogMode() {
		return dialogMode;
	}
	
	public void setDialogMode(boolean dialogMode) {
		this.dialogMode = dialogMode;
	}
	
	public void windowClosed(WindowEvent arg0) {
		try {
			server.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==mode) {
			setDialogMode(!isDialogMode());
			myFrame.repaint();
			if(isDialogMode()){
				text.append("Agent mode: dialog"+NEW_LINE);
			}
			else {
				text.append("Agent mode: chat"+NEW_LINE);
			}
		}
		
	}
	public void windowActivated(WindowEvent arg0) {}
	
	public void windowClosing(WindowEvent arg0) {}
	
	public void windowDeactivated(WindowEvent arg0) {}
	
	public void windowDeiconified(WindowEvent arg0) {}
	
	public void windowIconified(WindowEvent arg0) {}
	
	public void windowOpened(WindowEvent arg0) {}	
	
	
	public Document getUserDoc() {
	    return xmlusers.getDocument();
	}
	
	public Vector getUserInfo(){
	    return userInfo;
	}
	public ManageUsers getXmlUsers(){
	    return xmlusers;
	}
	
	public void updatePlot(long receivedTime){
	}
	
}