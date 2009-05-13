package eu.scy.colemo.server.network;

import eu.scy.colemo.server.graphics.MainFrame;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultStyledDocument;

import eu.scy.colemo.contributions.AddLink;
import eu.scy.colemo.contributions.AddMethod;
import eu.scy.colemo.contributions.AssociateClass;
import eu.scy.colemo.contributions.Chat;
import eu.scy.colemo.contributions.DeleteAssociation;
import eu.scy.colemo.contributions.DeleteClass;
import eu.scy.colemo.contributions.DeleteField;
import eu.scy.colemo.contributions.DeleteLink;
import eu.scy.colemo.contributions.DeleteMethod;
import eu.scy.colemo.contributions.Rename;
import eu.scy.colemo.server.agent.AgentMessage;
import eu.scy.colemo.server.agent.EndVote;
import eu.scy.colemo.server.agent.StartVote;
import eu.scy.colemo.server.agent.VoteResult;
import eu.scy.colemo.server.uml.UmlAssociation;
import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.server.uml.UmlDiagram;
import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.colemo.network.Person;
import eu.scy.colemo.network.NetworkMessage;
import eu.scy.colemo.network.LogOn;
import eu.scy.colemo.contributions.*;
import org.apache.log4j.Logger;

/**
 * 
 * Example Client program using TCP.
 *  
 */

public class Client implements Receiver, Runnable {

    private Logger log = Logger.getLogger(Client.class);
	public final int PORT = 8800;
	private Person user;
	private Connection connection;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private MainFrame frame;
	private static String newline = "\n";
	private String serverip;
	Thread t;
	LinkedList queue;
	
	
			
	public Client(MainFrame frame,Person user,String serverip) {
		this.frame=frame;
		this.user=user;
		this.serverip=serverip;
		t = new Thread(this);
		t.start();
		queue = new LinkedList();
				
		try {
			//connection = new Connection(new Socket("127.0.0.1",PORT),this);
			connection = new Connection(new Socket(serverip,PORT),this);
			connection.start();
		
			System.out.println("Connection established..");
		}
		catch(IOException ioe){
			System.out.println("Error - "+ioe);
		}
	}
	

	public void receive(Object o){
        log.debug("Received object: " + o);
		queue.add(o);
	}
	public void processAllObjects(){
		while(!queue.isEmpty()){
				processObject(queue.removeFirst());
		}
	}
	
	public void processObject(Object o){
        System.out.println("PROCESSING OBJECT : " + o.getClass().getName());
		DefaultStyledDocument logDoc = frame.getChatPane().getLogDoc();
		if(o instanceof AddClass){
			AddClass add = (AddClass)o;
			UmlClass umlClass = new UmlClass(add.getName(),add.getType(),add.getAuthor());
			frame.getGraphicsDiagram().getUmlDiagram().addDiagramData(umlClass);
			frame.getGraphicsDiagram().addClass(umlClass);
			frame.getChatPane().addAgentText("Agent >: "+add.getPerson().getUserName()+" added the new class: \""+add.getName().toUpperCase()+"\""+newline);
		}
		if(o instanceof AddLink){
			AddLink add = (AddLink)o;
			UmlLink umlLink = new UmlLink(add.getFrom(),add.getTo(),add.getUser());
			frame.getGraphicsDiagram().getUmlDiagram().addLink(umlLink);
			frame.getGraphicsDiagram().addLink(umlLink);
			frame.getChatPane().addAgentText("Agent >: "+add.getPerson().getUserName()+" made \""+add.getFrom().toUpperCase()+"\" a subclass of \""+add.getTo().toUpperCase()+"\""+newline);
		}
		if(o instanceof AssociateClass){
			AssociateClass add = (AssociateClass)o;
			UmlAssociation umlAssociation = new UmlAssociation(add.getFrom(),add.getTo(),add.getUser());
			frame.getGraphicsDiagram().getUmlDiagram().addAssociation(umlAssociation);
			frame.getGraphicsDiagram().addAssociation(umlAssociation);
		}
		if(o instanceof AddField){
			AddField add = (AddField)o;
			frame.getGraphicsDiagram().getUmlDiagram().addField(add.getUmlClass(),add.getField());
			frame.getGraphicsDiagram().updateClass(add.getUmlClass());
		}
		if(o instanceof AddMethod){
			AddMethod add = (AddMethod)o;
			frame.getGraphicsDiagram().getUmlDiagram().addMethod(add.getUmlClass(),add.getMethod());
			frame.getGraphicsDiagram().updateClass(add.getUmlClass());
		}
		if(o instanceof DeleteField){
			DeleteField delete =(DeleteField)o;
			frame.getGraphicsDiagram().getUmlDiagram().deleteField(delete.getUmlClass(),delete.getField());
			frame.getGraphicsDiagram().updateClass(delete.getUmlClass());
		}
		if(o instanceof DeleteMethod){
			DeleteMethod delete =(DeleteMethod)o;
			frame.getGraphicsDiagram().getUmlDiagram().deleteMethod(delete.getUmlClass(),delete.getMethod());
			frame.getGraphicsDiagram().updateClass(delete.getUmlClass());
		}
		if(o instanceof MoveClass) {
			MoveClass move = (MoveClass)o;
			frame.getGraphicsDiagram().getUmlDiagram().updateClass(move.getUmlClass());
			frame.getGraphicsDiagram().updateClass(move.getUmlClass());
		}
		if(o instanceof DeleteLink) {
			DeleteLink delete= (DeleteLink)o;
			frame.getGraphicsDiagram().getUmlDiagram().deleteLink(delete.getUmlLink());
			frame.getGraphicsDiagram().deleteLink(delete.getUmlLink());
		}
		if(o instanceof DeleteAssociation) {
			DeleteAssociation delete = (DeleteAssociation)o;
			frame.getGraphicsDiagram().getUmlDiagram().deleteAssociation(delete.getUmlAssociation());
			frame.getGraphicsDiagram().deleteAssociation(delete.getUmlAssociation());
		}
		if(o instanceof DeleteClass) {
			DeleteClass delete = (DeleteClass)o;
			frame.getGraphicsDiagram().getUmlDiagram().deleteClass(delete.getUmlClass());
			frame.getGraphicsDiagram().deleteClass(delete.getUmlClass());
		}
		if(o instanceof Rename) {
			Rename rename = (Rename)o;
			if(rename.getType().equals("class")){
				frame.getGraphicsDiagram().getUmlDiagram().renameClass(rename.getUmlClass(),rename.getNewName());
				frame.getGraphicsDiagram().renameClass(rename.getUmlClass());
			}
			else if(rename.getType().equals("field")){
				frame.getGraphicsDiagram().getUmlDiagram().renameField(rename.getUmlClass(),rename.getNewName(),rename.getOldName());
				frame.getGraphicsDiagram().updateClass(rename.getUmlClass());
			}
			else if(rename.getType().equals("method")){
				frame.getGraphicsDiagram().getUmlDiagram().renameMethod(rename.getUmlClass(),rename.getNewName(),rename.getOldName());
				frame.getGraphicsDiagram().updateClass(rename.getUmlClass());
			}
		}
		if(o instanceof ClassMoving){
			ClassMoving classMoving =(ClassMoving)o;
			frame.getGraphicsDiagram().getUmlDiagram().updateClass(classMoving.getUmlClass());
			frame.getGraphicsDiagram().updateClass(classMoving.getUmlClass());
		}
		
		if(o instanceof Chat){
			Chat chat = (Chat)o;
			String input =(String)chat.getInput();
			Person person =(Person)chat.getPerson();
			try{
				frame.getChatPane().addChatText(person.getUserName()+" > "+input+newline);
			}
			catch(Exception e) {
				System.out.println("FEIL: "+e);
			}
		}
		
			
		if(o instanceof NetworkMessage) {
			NetworkMessage message = (NetworkMessage)o;
			System.out.println("NETWORK MESSAGE SENT!!!! ");
			if(message.getObject() instanceof UmlDiagram) {
				UmlDiagram umlDiagram = (UmlDiagram)message.getObject();
				frame.getGraphicsDiagram().updateUmlDiagram(umlDiagram);	
			}
		}
		
		if(o instanceof String){
			String string =(String)o;
			frame.agentMessage(string);
		}
		if(o instanceof LogOn) {
			LogOn logon = (LogOn)o;
			Person person = logon.getPerson();
            log.info("LOGGED ON: " + person.getUserName());
			
			try{
				frame.getChatPane().addServerText("System > "+person.getUserName()+" has logged on\n");
			}
			catch(Exception e) {
			    e.printStackTrace();
			}
		}
	
		if( o instanceof Vector){
			Vector v =(Vector)o;
			frame.updateUsers(v);
		}
		if(o instanceof ObjectList) {
			ObjectList list =(ObjectList)o;
			frame.updateUsers(list.getVector());
		}
		
		if(o instanceof AgentMessage) {
			AgentMessage message = (AgentMessage)o;
			String string =(String)message.getMessage();
			if(message.isDialogMode()){
				frame.agentMessage(string);
			}
			else{
				frame.getChatPane().addAgentText("Agent >: "+string+newline);
			}
		}
		if(o instanceof StartVote) {
			StartVote startVote = (StartVote)o;
			frame.voteMessage(startVote);
		}
		if(o instanceof VoteResult) {
			VoteResult result = (VoteResult)o;
			
			frame.getChatPane().addVoteText("Results for voting of deletion of: "+result.getClas()+newline);
			frame.getChatPane().addVoteText("Suggested by: "+result.getUser()+newline);
			frame.getChatPane().addVoteText("Delete: "+result.getYes()+", Not delete: "+result.getNo()+newline);
			if(result.getYes()>result.getNo()){
				frame.getChatPane().addVoteText("The class will be deleted.."+newline);
			}
			else{
				frame.getChatPane().addVoteText("The class will NOT be deleted.."+newline);
			}
		}
		if(o instanceof EndVote) {
			EndVote endVote =(EndVote)o;
			if(endVote.isDelete()){
				frame.getGraphicsDiagram().removeClass(frame.getGraphicsDiagram().getClass(endVote.getClas()));
			}
			else{
				JOptionPane.showMessageDialog(frame,"The majority of the users decided not to delete"+newline+
						endVote.getClas()+newline+"Please consider discussing this with the others!");
			}
		}
	}
	public Connection getConnection() {
		return connection;
	}
	public void send(Object o) {
		connection.send(o);
	}
	public Person getPerson() {
		return user;
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		for(;;) {
			try {
				Thread.sleep(1000);
				processAllObjects();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		
	}	
}