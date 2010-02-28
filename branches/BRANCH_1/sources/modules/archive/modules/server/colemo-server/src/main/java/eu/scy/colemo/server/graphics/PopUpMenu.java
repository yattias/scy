/*
 * Created on 04.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.Vector;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import eu.scy.colemo.contributions.AddMethod;
import eu.scy.colemo.contributions.AssociateClass;
import eu.scy.colemo.contributions.DeleteAssociation;
import eu.scy.colemo.contributions.DeleteField;
import eu.scy.colemo.contributions.DeleteLink;
import eu.scy.colemo.contributions.*;
import eu.scy.colemo.contributions.Rename;
import eu.scy.colemo.server.uml.UmlAssociation;
import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.colemo.network.Person;

/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PopUpMenu extends JPopupMenu implements ActionListener{
	private JMenu delete;
	private JMenu add;
	private JMenu rename;
	private JMenuItem renameClass;
	private JMenuItem renameField;
	private JMenuItem renameMethod;
	public JMenuItem deleteClass;
	private JMenuItem addField;
	private JMenuItem addMethod;
	private JMenu extendClass;
	private JMenu associateClass;
	private JMenu deleteExtention;
	private JMenu deleteAssociation;
	private JMenu deleteField;
	private JMenu deleteMethod;
	private Vector classNames;
	private Vector fields;
	private Vector methods;
	
	private GraphicsClass gClass;
	
		public PopUpMenu(GraphicsClass gClass) {
	    super();
		
		this.gClass=gClass;
		add = new JMenu ("Add");
		delete = new JMenu ("Delete");
		rename = new JMenu("Rename");
		extendClass = new JMenu("Extend class");
		associateClass = new JMenu("Associate class");
		deleteExtention = new JMenu("Delete extension");
		deleteAssociation = new JMenu("Delete association");
		deleteField = new JMenu("Delete field");
		deleteMethod = new JMenu("Delete method");
		renameField = new JMenu("Rename field");
		renameMethod = new JMenu("Rename method");
				
		renameClass = new JMenuItem("Rename class");
		deleteClass = new JMenuItem("Delete class");
		deleteClass.addActionListener(this);
		delete.add(deleteClass);
		
		addMethod = new JMenuItem("Add method");
		addField = new JMenuItem("Add field");
		addField.addActionListener(this);
		addMethod.addActionListener(this);
		renameField.addActionListener(this);
		renameMethod.addActionListener(this);
		renameClass.addActionListener(this);
		if(!gClass.getUmlClass().getType().equals("i")){
			add.add(addField);
			delete.add(deleteField);
		}
		add.add(addMethod);
		add.add(extendClass);
		add.add(associateClass);
		delete.add(deleteMethod);
		delete.add(deleteExtention);
		delete.add(deleteAssociation);
		rename.add(renameClass);
		rename.add(renameField);
		rename.add(renameMethod);
		
		//Create menuItems for associate class
		classNames = gClass.getGraphicsDiagram().getAllClassNames();
		for (int i=0;i<classNames.size();i++) {
			if(!((String)classNames.elementAt(i)).equals(gClass.getClassName())){
				JMenuItem newItem = new JMenuItem("->"+(String)classNames.elementAt(i));
				associateClass.add(newItem);
				newItem.addActionListener(this);
			}
		}
		
		//create menuItems for extend class
		classNames = gClass.getGraphicsDiagram().getAllClassNames();
		for (int i=0;i<classNames.size();i++) {
			if(!((String)classNames.elementAt(i)).equals(gClass.getClassName())){
				JMenuItem newItem = new JMenuItem((String)classNames.elementAt(i));
				extendClass.add(newItem);
				newItem.addActionListener(this);
			}
		}
		//create menuItems for deleteExtension
		for (int i=0; i<gClass.getGraphicsDiagram().getUmlDiagram().getComponents().size();i++) {
			Object o =gClass.getGraphicsDiagram().getUmlDiagram().getComponents().elementAt(i);
			if(o instanceof UmlLink) {
				UmlLink link =(UmlLink)o;
				if(gClass.getUmlClass().getName().equals(link.getFrom())){
					JMenuItem newItem = new JMenuItem("-->"+(String)link.getTo());
					deleteExtention.add(newItem);
					newItem.addActionListener(this);
				}
				if(gClass.getUmlClass().getName().equals(link.getTo())){
					JMenuItem newItem = new JMenuItem("-->"+(String)link.getFrom());
					deleteExtention.add(newItem);
					newItem.addActionListener(this);
				}
			}
		}
		//create menuItems for deleteAssociation
		for (int i=0; i<gClass.getGraphicsDiagram().getUmlDiagram().getComponents().size();i++) {
			Object o =gClass.getGraphicsDiagram().getUmlDiagram().getComponents().elementAt(i);
			if(o instanceof UmlAssociation) {
				UmlAssociation link =(UmlAssociation)o;
				if(gClass.getUmlClass().getName().equals(link.getFrom())){
					JMenuItem newItem = new JMenuItem(">"+(String)link.getTo());
					deleteAssociation.add(newItem);
					newItem.addActionListener(this);
				}
				if(gClass.getUmlClass().getName().equals(link.getTo())){
					JMenuItem newItem = new JMenuItem(">"+(String)link.getFrom());
					deleteAssociation.add(newItem);
					newItem.addActionListener(this);
				}
			}
		}
		//create menuItems for deleteFields og renameFields
		fields = gClass.getUmlClass().getFields();
		for (int i=0;i<fields.size();i++) {
			JMenuItem newItem = new JMenuItem("-"+(String)fields.elementAt(i));
			deleteField.add(newItem);
			newItem.addActionListener(this);
			JMenuItem rename = new JMenuItem("´"+(String)fields.elementAt(i));
			renameField.add(rename);
			rename.addActionListener(this);
		}
		//create menuItems for deleteMethods og renameMethods
		methods = gClass.getUmlClass().getMethods();
		for (int i=0;i<methods.size();i++) {
			JMenuItem newItem = new JMenuItem("*"+(String)methods.elementAt(i));
			deleteMethod.add(newItem);
			newItem.addActionListener(this);
			JMenuItem rename = new JMenuItem("`"+(String)methods.elementAt(i));
			renameMethod.add(rename);
			rename.addActionListener(this);
		}
		add(add);
		add(delete);
		add(rename);
	}
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent ae) {
		
			MainFrame frame = gClass.getGraphicsDiagram().getMainFrame();
			InetAddress ip = frame.getClient().getConnection().getSocket().getLocalAddress();
			Person person = frame.getClient().getPerson();
			
		
			
			if(ae.getSource()==deleteClass){
			
				//avstemming i mellom her
				gClass.getGraphicsDiagram().deleteClass(gClass);
				//gClass.getGraphicsDiagram().setLocked(false);
				
			}
			else if(ae.getSource()==addField) {
	
				String name = JOptionPane.showInputDialog(this, "Please type name of new field:");
		
				if(name!=null){
					AddField addField = new AddField(gClass.getUmlClass(),name,ip,person);
					frame.getClient().getConnection().send(addField);
				}
				
			}
			else if(ae.getSource()==addMethod) {

				String name = JOptionPane.showInputDialog(this, "Please type name of new method:");
				
				if(name!=null){
					AddMethod addMethod = new AddMethod(gClass.getUmlClass(),name,ip,person);
					frame.getClient().getConnection().send(addMethod);
				}
				
			}
			else if(ae.getSource()==renameClass){
				String s = JOptionPane.showInputDialog(this,"Please type in the new name of the class:");
				Rename rename = new Rename(gClass.getUmlClass(),"class",s,null,ip,person);
				frame.getClient().getConnection().send(rename);
			}
			//Associate class
			else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand().startsWith("->")){
				String className = ae.getActionCommand().substring(2);
				Vector names = gClass.getGraphicsDiagram().getAllClassNames();
				
				for (int i=0;i<names.size();i++) {
					String name = (String)names.elementAt(i);
					if(name.equals(className)) {
						String fromType=gClass.getUmlClass().getType();
						String toType=(gClass.getGraphicsDiagram().getClass(name)).getUmlClass().getType();
						
						//Sjekk at det ikke finnes en extension mellom disse klassene
						if(!checkExtensions(gClass.getClassName(),name)){
							if((fromType.equals("c") & toType.equals("i")) || (fromType.equals("i") & toType.equals("c"))){
								GraphicsTips tip = new GraphicsTips("association1");
							}
							else if(fromType.equals("i") || toType.equals("i")){
								GraphicsTips tip = new GraphicsTips("association2");
							}
							else{
								AssociateClass associateClass = new AssociateClass(gClass.getClassName(),name,person.getUserName(),ip,person);
								frame.getClient().getConnection().send(associateClass);	
							}
							
								
						}
						else{
							JOptionPane.showMessageDialog(this,"This class extends or is extended by another class,"+"\n"+
							"hence it could not associate another class!");
						
						}		
					}
				}
			}
			//Delete extension
			else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand().startsWith("-->")){
	
				String classOne = gClass.getUmlClass().getName();
				String classTwo = ae.getActionCommand().substring(3);
								
				for (int i=0; i<gClass.getGraphicsDiagram().getUmlDiagram().getComponents().size();i++) {
					Object o =gClass.getGraphicsDiagram().getUmlDiagram().getComponents().elementAt(i);
					if(o instanceof UmlLink) {
						UmlLink link =(UmlLink)o;
						if((link.getTo().equals(classOne) || link.getTo().equals(classTwo)) && (link.getFrom().equals(classOne) || link.getFrom().equals(classTwo))) {
							DeleteLink deleteLink = new DeleteLink(link,ip,person);
							frame.getClient().getConnection().send(deleteLink);
							break;
						}
					}
				}
			}
			//Delete association
			else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand().startsWith(">")){
			
				String classOne = gClass.getUmlClass().getName();
				String classTwo = ae.getActionCommand().substring(1);
								
				for (int i=0; i<gClass.getGraphicsDiagram().getUmlDiagram().getComponents().size();i++) {
					Object o =gClass.getGraphicsDiagram().getUmlDiagram().getComponents().elementAt(i);
					if(o instanceof UmlAssociation) {
						UmlAssociation link =(UmlAssociation)o;
						if((link.getTo().equals(classOne) || link.getTo().equals(classTwo)) && (link.getFrom().equals(classOne) || link.getFrom().equals(classTwo))) {
							DeleteAssociation deleteAssociation = new DeleteAssociation(link,ip,person);
							frame.getClient().getConnection().send(deleteAssociation);
							break;
						}
					}
				}
			}
			//Delete fields
			else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand().startsWith("-")){
				
				String field = ae.getActionCommand().substring(1);
								
				for (int i=0; i<gClass.getUmlClass().getFields().size();i++) {
					String s =(String)gClass.getUmlClass().getFields().elementAt(i);
					if(field.equals(s)){
						//Client client = gClass.getGraphicsDiagram().getMainFrame().getClient();
						//Connection connection = client.getConnection();
						//InetAddress address = connection.getSocket().getLocalAddress();
						DeleteField deleteField = new DeleteField(gClass.getUmlClass(),s,ip,person);
						frame.getClient().getConnection().send(deleteField);
					}	
				}
			}
			//Delete methods
			else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand().startsWith("*")){
				String method = ae.getActionCommand().substring(1);
								
				for (int i=0; i<gClass.getUmlClass().getMethods().size();i++) {
					String s =(String)gClass.getUmlClass().getMethods().elementAt(i);
					if(method.equals(s)){
						DeleteMethod deleteMethod = new DeleteMethod(gClass.getUmlClass(),s,ip,person);
						frame.getClient().getConnection().send(deleteMethod);
					}	
				}
			}
			//Rename fields
			else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand().startsWith("´")){
				String field = ae.getActionCommand().substring(1);
				String newName = JOptionPane.showInputDialog(this,"Please type the name of the field:");
				for (int i=0; i<gClass.getUmlClass().getFields().size();i++) {
					String oldName =(String)gClass.getUmlClass().getFields().elementAt(i);
					if(field.equals(oldName)){
						Rename rename = new Rename(gClass.getUmlClass(),"field",newName,oldName,ip,person);
						frame.getClient().getConnection().send(rename);
					}	
				}
				
			}
			//Rename methods
			else if(ae.getSource() instanceof JMenuItem && ae.getActionCommand().startsWith("`")){
				String method = ae.getActionCommand().substring(1);
				String newName = JOptionPane.showInputDialog(this,"Please type the name of the method:");
				
				for (int i=0; i<gClass.getUmlClass().getMethods().size();i++) {
					String oldName =(String)gClass.getUmlClass().getMethods().elementAt(i);
					if(method.equals(oldName)){
						Rename rename = new Rename(gClass.getUmlClass(),"method",newName,oldName,ip,person);
						frame.getClient().getConnection().send(rename);
					}	
				}
			}
			//extend class
			else if(ae.getSource() instanceof JMenuItem) {
				String className = ae.getActionCommand();
				Vector names = gClass.getGraphicsDiagram().getAllClassNames();
				
				for (int i=0;i<names.size();i++) {
					String name = (String)names.elementAt(i);
					if(name.equals(className)) {
						String fromType=gClass.getUmlClass().getType();
						String toType=(gClass.getGraphicsDiagram().getClass(name)).getUmlClass().getType();
						if(fromType.equals("i") && (toType.equals("c") || toType.equals("a"))){
							GraphicsTips tip = new GraphicsTips("interface");
						}
						else if(fromType.equals("a") && toType.equals("c")){
							GraphicsTips tip = new GraphicsTips("abstract");
						}
						else{
							if(!checkAssociations(gClass.getClassName(),name)){
								AddLink addLink = new AddLink(gClass.getClassName(),name,person.getUserName(),ip,person);
								frame.getClient().getConnection().send(addLink);
							}
							else{
								JOptionPane.showMessageDialog(this,"This class is associated with another class,"+"\n"+
										"hence it could not extend another class!");
							
							}
						}
						
					}
			
				}
		
		
			}
			
			
		
		}
		
		public boolean checkExtensions(String from,String to){
			for (int i=0; i<gClass.getGraphicsDiagram().getUmlDiagram().getComponents().size();i++) {
				Object o =gClass.getGraphicsDiagram().getUmlDiagram().getComponents().elementAt(i);
				if(o instanceof UmlLink) {
					UmlLink link =(UmlLink)o;
					if((link.getTo().equals(to)) && (link.getFrom().equals(from))) {
						return true;
					}
					else if((link.getTo().equals(from)) && (link.getFrom().equals(to))) {
						return true;
					}
				}
			}
			return false;
		}
		public boolean checkAssociations(String from,String to){
			for (int i=0; i<gClass.getGraphicsDiagram().getUmlDiagram().getComponents().size();i++) {
				Object o =gClass.getGraphicsDiagram().getUmlDiagram().getComponents().elementAt(i);
				if(o instanceof UmlAssociation) {
					UmlAssociation link =(UmlAssociation)o;
					if((link.getTo().equals(to)) && (link.getFrom().equals(from))) {
						return true;
					}
					else if((link.getTo().equals(from)) && (link.getFrom().equals(to))) {
						return true;
					}
				}
			}
			return false;
		}
}




