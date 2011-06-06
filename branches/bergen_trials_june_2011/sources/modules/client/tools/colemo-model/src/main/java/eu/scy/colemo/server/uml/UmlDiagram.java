/*
 * Created on 30.sep.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.uml;

import java.io.Serializable;
import java.util.Vector;
import eu.scy.colemo.server.exceptions.ClassNameAlreadyExistException;


/**
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UmlDiagram implements Serializable{
	private Vector components=new Vector();
	
	public UmlDiagram(Vector components) {
		this.components=components;
	}
	public UmlDiagram() {}
	
	public void addDiagramData(AbstractDiagramData abstractDiagramData) {
		components.add(abstractDiagramData);
	}
	public void addLink(UmlLink umlLink) {	
		components.add(umlLink);	
	}
	public void addAssociation(UmlAssociation umlAssociation) {	
		components.add(umlAssociation);
	}
	public void addField(UmlClass umlClass,String field){
		for (int i=0;i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlClass) {
				if(((UmlClass)components.elementAt(i)).getName().equals(umlClass.getName())){
					UmlClass denne = (UmlClass)components.elementAt(i);
					denne.getFields().add(field);
				}
			}
		}
	}
	public void deleteField(UmlClass umlClass,String field){
		for (int i=0;i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlClass) {
				if(((UmlClass)components.elementAt(i)).getName().equals(umlClass.getName())){
					UmlClass denne = (UmlClass)components.elementAt(i);
					denne.getFields().remove(field);
				}
			}
		}
	}
	
	public void addMethod(UmlClass umlClass,String method){
		for (int i=0;i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlClass) {
				if(((UmlClass)components.elementAt(i)).getName().equals(umlClass.getName())){
					((UmlClass)components.elementAt(i)).getMethods().add(method);	
				}
			}
		}
	}
	public void deleteMethod(UmlClass umlClass,String method){
		for (int i=0;i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlClass) {
				if(((UmlClass)components.elementAt(i)).getName().equals(umlClass.getName())){
					UmlClass denne = (UmlClass)components.elementAt(i);
					denne.getMethods().remove(method);
				}
			}
		}
	}
	public void renameClass(UmlClass umlClass,String newName){
		for (int i=0;i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlClass) {
				if(((UmlClass)components.elementAt(i)).getName().equals(umlClass.getName())){
					UmlClass denne = (UmlClass)components.elementAt(i);
					denne.setName(newName);
				}
			}
			if(components.elementAt(i) instanceof UmlLink) {
				UmlLink link =(UmlLink)components.elementAt(i);
				if(link.getFrom().equals(umlClass.getName())){
					link.setFrom(newName);
				}
				else if(link.getTo().equals(umlClass.getName())) {
					link.setTo(newName);
					
				}
			}
		}
		
	}
	public void renameField(UmlClass umlClass,String newName,String oldName) {
		for (int i=0;i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlClass) {
				if(((UmlClass)components.elementAt(i)).getName().equals(umlClass.getName())){
					UmlClass denne = (UmlClass)components.elementAt(i);
					Vector v = denne.getFields();
					v.setElementAt(newName,v.indexOf(oldName));
					break;
				}
			}
		}
	}
	public void renameMethod(UmlClass umlClass,String newName,String oldName) {
		for (int i=0;i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlClass) {
				if(((UmlClass)components.elementAt(i)).getName().equals(umlClass.getName())){
					UmlClass denne = (UmlClass)components.elementAt(i);
					Vector v = denne.getMethods();
					v.setElementAt(newName,v.indexOf(oldName));
					break;
				}
			}
		}
	}
	public void updateClass(UmlClass umlClass) {
		for (int i=0;i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlClass) {
				if(((UmlClass)components.elementAt(i)).getName().equals(umlClass.getName())){
					((UmlClass)components.elementAt(i)).setX(umlClass.getX());
					((UmlClass)components.elementAt(i)).setY(umlClass.getY());
					((UmlClass)components.elementAt(i)).setMove(umlClass.isMove());
				}
			}
		}
		
	}
	
	
	public void deleteClass(UmlClass umlClass) {
		for (int i=0;i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlClass) {
				if(((UmlClass)components.elementAt(i)).getName().equals(umlClass.getName())){
					components.remove((UmlClass)components.elementAt(i));
			
				}
			}
		}
	}
	
	public void deleteLink(UmlLink umlLink) {
		for (int i=0;i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlLink) {
				if(((UmlLink)components.elementAt(i)).getFrom().equals(umlLink.getFrom())){
					if(((UmlLink)components.elementAt(i)).getTo().equals(umlLink.getTo())){
						components.remove((UmlLink)components.elementAt(i));
						break;
					}
				}
			}
		}
	}
	public void deleteAssociation(UmlAssociation umlLink) {
		for (int i=0;i<components.size();i++) {
			if(components.elementAt(i) instanceof UmlAssociation) {
				if(((UmlAssociation)components.elementAt(i)).getFrom().equals(umlLink.getFrom())){
					if(((UmlAssociation)components.elementAt(i)).getTo().equals(umlLink.getTo())){
						components.remove((UmlAssociation)components.elementAt(i));
						break;
					}
				}
			}
		}
	}
	
	public boolean nameExist(String name) throws ClassNameAlreadyExistException {
		for (int i=0;i<components.size();i++) {
			if (components.elementAt(i) instanceof UmlClass) {
				if(((UmlClass)components.elementAt(i)).getName().toLowerCase().equals(name.toLowerCase())){
					return true;
				}
				
			}
		}
		return false;
	}
	/**
	 * @return
	 */
	public Vector getComponents() {
		return components;
	}
	
	
}