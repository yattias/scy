/*
 * Created on 30.sep.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.uml;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author �ystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UmlClass implements Serializable{
	private String name;
	private Vector methods=new Vector();
	private Vector fields=new Vector();
	private boolean showMethods=true;
	private boolean showFields=true;
	private int x;
	private int y;
	private String type;
	private String author;
	private boolean move=false;
	
	public UmlClass(String name,String type,String author) {
		this.name=name;
		this.type=type;
		this.author=author;
		setX(100);
		setY(100);
	   
	}
	/**
	 * @return Returns the fields.
	 */
	public Vector getFields() {
		return fields;
	}
	/**
	 * @param fields The fields to set.
	 */
	public void setFields(Vector fields) {
		this.fields = fields;
	}
	/**
	 * @return Returns the methods.
	 */
	public Vector getMethods() {
		return methods;
	}
	/**
	 * @param methods The methods to set.
	 */
	public void setMethods(Vector methods) {
		this.methods = methods;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the showFields.
	 */
	public boolean showFields() {
		return showFields;
	}
	/**
	 * @param showFields The showFields to set.
	 */
	public void setShowFields(boolean showFields) {
		this.showFields = showFields;
	}
	/**
	 * @return Returns the showMethods.
	 */
	public boolean showMethods() {
		return showMethods;
	}
	/**
	 * @param showMethods The showMethods to set.
	 */
	public void setShowMethods(boolean showMethods) {
		this.showMethods = showMethods;
	}
	/**
	 * @return Returns the x.
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x The x to set.
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return Returns the y.
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y The y to set.
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @return Returns the author.
	 */
	public String getAuthor() {
		return author;
	}
	
	
	/**
	 * @return Returns the move.
	 */
	public boolean isMove() {
		return move;
	}
	/**
	 * @param move The move to set.
	 */
	public void setMove(boolean move) {
		this.move = move;
	}
}
