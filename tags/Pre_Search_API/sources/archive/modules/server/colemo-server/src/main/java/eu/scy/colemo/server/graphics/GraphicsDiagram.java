/*
 * Created on 01.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import eu.scy.colemo.server.agent.StartVote;
import eu.scy.colemo.server.network.Client;
import eu.scy.colemo.server.network.Connection;
import eu.scy.colemo.server.uml.UmlAssociation;
import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.server.uml.UmlDiagram;
import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.colemo.contributions.DeleteAssociation;
import eu.scy.colemo.contributions.DeleteLink;
import eu.scy.colemo.contributions.DeleteClass;

/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GraphicsDiagram extends JPanel implements MouseListener,ActionListener {
	private UmlDiagram umlDiagram;
	private MainFrame frame;
	private Vector extend;
	private Vector associate;
	private Hashtable components;
	
	public GraphicsDiagram(UmlDiagram umlDiagram, MainFrame frame){
		this.umlDiagram=umlDiagram;
		this.frame=frame;
		this.setLayout(null);
		addMouseListener(this);
		extend = new Vector();
		associate = new Vector();
		components = new Hashtable();
	}
	public void addClass(UmlClass umlClass) {
		GraphicsClass gClass = new GraphicsClass(umlClass,this);
	    components.put(gClass.getUmlClass().getName(),gClass);
	    this.add(gClass);
	    updatePopUpMenus();
	    gClass.invalidate();
	    gClass.validate();
	    gClass.repaint();
	}
	public void addLink(UmlLink umlLink){
		GraphicsLink gLink = new GraphicsLink(umlLink,this);
		components.put(gLink.getFrom().getUmlClass().getName()+gLink.getTo().getUmlClass().getName(),gLink);
		extend.add(gLink);
		gLink.paint(getGraphics());
	}
	public void addAssociation(UmlAssociation umlAssociation) {
		GraphicsAssociation gAss = new GraphicsAssociation(umlAssociation,this);
		components.put(gAss.getFrom().getUmlClass().getName()+gAss.getTo().getUmlClass().getName(),gAss);
		associate.add(gAss);
		gAss.paint(getGraphics());
	}
	public void deleteClass(UmlClass umlClass){
		GraphicsClass gClass = (GraphicsClass)components.remove(umlClass.getName());
		updatePopUpMenus();
		this.remove(gClass);
		this.repaint();
	}
	public void deleteLink(UmlLink umlLink){
		GraphicsLink gLink=(GraphicsLink)components.remove(umlLink.getFrom()+umlLink.getTo());
		extend.remove(gLink);
		this.repaint();
	}
	public void deleteAssociation(UmlAssociation umlAssociation){
		GraphicsAssociation gAss=(GraphicsAssociation)components.remove(umlAssociation.getFrom()+umlAssociation.getTo());
		associate.remove(gAss);
		this.repaint();
	}
	public void updateClass(UmlClass umlClass){
		GraphicsClass gClass =(GraphicsClass)components.get(umlClass.getName());
		if(umlClass.isMove()){
			gClass.setBackground(new Color(236,236,236));
			gClass.setToolTipText("<html>"+umlClass.getName()+" created by "+umlClass.getAuthor()+".<br>"+
					"A faded class represents a class being moved by another user."+"<br>"+
					"You can still manipulate this class, but you should not try to move it"+"<br>"+
					"since this can interfer with the other user!"+"</html>");
		}
		if(!umlClass.isMove()){
			gClass.setBackground(new Color(212,208,200));
			gClass.setToolTipText(umlClass.getName()+" created by "+umlClass.getAuthor());
		}
		gClass.layoutComponents();
		gClass.invalidate();
	    gClass.validate();
	    gClass.repaint();
	    this.repaint();
	}
	public void renameClass(UmlClass umlClass){
		//Hente ut evt linker og rename de
		GraphicsClass gClass =(GraphicsClass)components.remove(umlClass.getName());
		String oldName =umlClass.getName();
	    
	    components.put(gClass.getUmlClass().getName(),gClass);
	    findLink(oldName,gClass.getUmlClass().getName());
	    
	    gClass.layoutComponents();
	    updatePopUpMenus();
	  	gClass.invalidate();
	  	gClass.validate();
	  	gClass.repaint();
	    
	    this.repaint();
	    
	}
	
	//Kalles kun fra renameClass, sjekker om klassen som er renamet har linker
	//Om den har det må alle tilhørende linker renames
	//Tar seg av både extensions og associations
	public void findLink(String oldName,String newName){
		 for (Enumeration e = components.elements() ; e.hasMoreElements() ;) {
	         Object o =e.nextElement();
	         if(o instanceof GraphicsClass){
	         	GraphicsClass gClass = (GraphicsClass)o;
	         	if(components.get(oldName+gClass.getUmlClass().getName())!=null){
	         		if(components.get(oldName+gClass.getUmlClass().getName()) instanceof GraphicsLink){
	         			GraphicsLink gLink =(GraphicsLink)components.remove(oldName+gClass.getUmlClass().getName());
	         			extend.remove(gLink);
		         		
		        		components.put(newName+gClass.getUmlClass().getName(),gLink);
		        		extend.add(gLink);
		        		gLink.paint(getGraphics());
	         		}
	         		else if(components.get(oldName+gClass.getUmlClass().getName()) instanceof GraphicsAssociation){
	         			GraphicsAssociation gAss =(GraphicsAssociation)components.remove(oldName+gClass.getUmlClass().getName());
	         			associate.remove(gAss);
		         		
		        		components.put(newName+gClass.getUmlClass().getName(),gAss);
		        		associate.add(gAss);
		        		gAss.paint(getGraphics());
	         		}
	         	}		
	         	else if(components.get(gClass.getUmlClass().getName()+oldName)!=null){
	         		if(components.get(gClass.getUmlClass().getName()+oldName) instanceof GraphicsLink){
	         			GraphicsLink gLink =(GraphicsLink)components.remove(gClass.getUmlClass().getName()+oldName);
		         		extend.remove(gLink);
		         		
		         		components.put(gClass.getUmlClass().getName()+newName,gLink);
		        		extend.add(gLink);
		        		gLink.paint(getGraphics());
	         		}
	         		else if(components.get(gClass.getUmlClass().getName()+oldName) instanceof GraphicsAssociation){
	         			GraphicsAssociation gAss =(GraphicsAssociation)components.remove(gClass.getUmlClass().getName()+oldName);
	         			associate.remove(gAss);
		         		
		         		components.put(gClass.getUmlClass().getName()+newName,gAss);
		         		associate.add(gAss);
		        		gAss.paint(getGraphics());
	         		}
	         	}	
	         	gClass.layoutComponents();
	         	gClass.createPopUpMenu();
     			gClass.invalidate();
     			gClass.validate();
     			gClass.repaint();
	         }
	     }
	}
	
	public void updatePopUpMenus(){
		 for (Enumeration e = components.elements() ; e.hasMoreElements() ;) {
	         Object o =e.nextElement();
	         if(o instanceof GraphicsClass){
	         	GraphicsClass gClass = (GraphicsClass)o;
	         	gClass.createPopUpMenu();
	         }
		 }
		
	}
	public void paint(Graphics g) {
		super.paint(g);
		paintLinks(g);
		paintAssociations(g);
	}
	
	public void paintLinks(Graphics g){
		for (int i=0;i<extend.size();i++) {
			GraphicsLink current = (GraphicsLink)extend.elementAt(i);
			current.paint(g);
		}
	}
	public void paintAssociations(Graphics g){
		for (int i=0;i<associate.size();i++) {
			GraphicsAssociation current = (GraphicsAssociation)associate.elementAt(i);
			current.paint(g);
		}
	}
	
	public void createPopUpMenus() {
		Component[] components = getComponents();
		for (int i=0; i<components.length;i++) {
			if(components[i] instanceof GraphicsClass) {
				((GraphicsClass)components[i]).createPopUpMenu();
			}
		}
	}
	public GraphicsClass getClass(String name) {
		Component[] components = getComponents();
		for (int i=0; i<components.length;i++) {
			if(components[i] instanceof GraphicsClass) {
				GraphicsClass gc = (GraphicsClass)components[i];
				if(((GraphicsClass)components[i]).getClassName().equals(name)) {
					return (GraphicsClass)components[i];
				}
			}
		}
		return null;
	}
	
	public Vector getAllClassNames() {
		Vector classNames= new Vector();
	
		Component[] components = getComponents();
		for (int i=0; i<components.length;i++) {
			if(components[i] instanceof GraphicsClass) {
				classNames.add(((GraphicsClass)components[i]).getClassName());
			}
		}
		return classNames;
	}
	
	public UmlDiagram getUmlDiagram() {
		return umlDiagram;
	}

	/**
	 * called locally
	 */
	public void setUmlDiagram(UmlDiagram umlDiagram, String action) {
		this.umlDiagram = umlDiagram;		
	}
	/**
	 * called from server
	 */
	public void updateUmlDiagram(UmlDiagram umlDiagram) {
		this.umlDiagram = umlDiagram;	
		Vector v =umlDiagram.getComponents();
		for (int i=0;i<v.size();i++){
			Object o = v.elementAt(i);
			if(o instanceof UmlClass){
				addClass((UmlClass)o);
			}
			if(o instanceof UmlLink){
				addLink((UmlLink)o);
			}
			if(o instanceof UmlAssociation){
				addAssociation((UmlAssociation)o);
			}
		}
	}
	
	public void deleteClass(GraphicsClass gClass) {
		//Kalle opp alle klienter og spørre om de vil slette klassen
		//frame.getClient().getConnection().send(new StartVote());
		int n=JOptionPane.showConfirmDialog(frame,"The deletion of this class will affect"+"\n"+
				"other classes and links with a relation to it."+"\n"+
				"To continue press \"yes\" or to discuss it further press \"no\"","DELETION WARNING!",JOptionPane.YES_NO_OPTION);
		if(n==0){
			if(frame.getClient()!=null){
				frame.getClient().getConnection().send(new StartVote(frame.getClient().getConnection().getSocket().getLocalAddress(),frame.getClient().getPerson().getUserName(),gClass.getUmlClass().getName()));
			}
			else{
				removeClass(gClass);
			}
		}
	}
	public void removeClass(GraphicsClass gClass){
		Client client = gClass.getGraphicsDiagram().getMainFrame().getClient();
		Connection connection = client.getConnection();
		InetAddress ip = connection.getSocket().getLocalAddress();
		
		Vector components = umlDiagram.getComponents();
		for (int i=0; i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlLink) {
				UmlLink current = (UmlLink)components.elementAt(i);
				
				if(current.getFrom().equals(gClass.getClassName()) || current.getTo().equals(gClass.getClassName())){
					DeleteLink deleteLink = new DeleteLink(current,ip,client.getPerson());
					connection.send(deleteLink);
				}
			}
			if(components.elementAt(i) instanceof UmlAssociation) {
				UmlAssociation current = (UmlAssociation)components.elementAt(i);
				
				if(current.getFrom().equals(gClass.getClassName()) || current.getTo().equals(gClass.getClassName())){
					DeleteAssociation deleteAssociation = new DeleteAssociation(current,ip,client.getPerson());
					connection.send(deleteAssociation);
				}
			}
		}
		
		DeleteClass deleteClass = new DeleteClass(gClass.getUmlClass(),ip,client.getPerson());
		connection.send(deleteClass);	
	}
	public void deleteLinks(GraphicsClass gClass) {
		Client client = gClass.getGraphicsDiagram().getMainFrame().getClient();
		Connection connection = client.getConnection();
		InetAddress ip = connection.getSocket().getLocalAddress();
	
		Vector components = umlDiagram.getComponents();
		for (int i=0; i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlLink) {
				UmlLink current = (UmlLink)components.elementAt(i);
				if(current.getFrom()==gClass.getClassName() || current.getTo()==gClass.getClassName()){
					
					DeleteLink deleteLink = new DeleteLink(current,ip,client.getPerson());
					gClass.getGraphicsDiagram().getMainFrame().getClient().getConnection().send(deleteLink);
				}
			}
		}
	}
	public MainFrame getMainFrame() {
		return frame;
	}
	public void createMenu(MouseEvent e) {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem addClass = new JMenuItem("Add class");
		JMenuItem addAbstract = new JMenuItem("Add abstract class");
		JMenuItem addInterface = new JMenuItem("Add interface");
		addClass.addActionListener(this);
		addAbstract.addActionListener(this);
		addInterface.addActionListener(this);
		
		menu.add(addClass);
		menu.add(addAbstract);
		menu.add(addInterface);
		menu.show(e.getComponent(),
                e.getX(), e.getY());
		
	}
	public void actionPerformed(ActionEvent ae) {

		if(ae.getActionCommand().equals("Add class")){
			getMainFrame().addClass(umlDiagram,"c");
		}
		else if(ae.getActionCommand().equals("Add abstract class")){
			getMainFrame().addClass(umlDiagram,"a");
		}
		else if(ae.getActionCommand().equals("Add interface")){
			getMainFrame().addClass(umlDiagram,"i");
		}	
	}
	
	public void mousePressed(MouseEvent ae) {
		if(ae.getModifiers() ==InputEvent.BUTTON3_MASK){
			createMenu(ae);
		}
	}
		
	public void mouseClicked(MouseEvent arg0) {}
	
	public void mouseEntered(MouseEvent arg0) {}
	
	public void mouseExited(MouseEvent arg0) {}
	
	public void mouseReleased(MouseEvent arg0) {}
	
}