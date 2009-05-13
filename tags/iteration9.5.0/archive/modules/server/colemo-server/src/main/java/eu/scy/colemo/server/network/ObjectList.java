/*
 * Created on 17.nov.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.network;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ObjectList implements Serializable {

	private Object[] elements = new Object[10];
	
	private int size=0;
	
	public void add(Object o){
		if(size>=elements.length){
			Object[] newArray = new Object[elements.length*2];
			System.arraycopy(elements,0,newArray,0,elements.length);
			elements=newArray;
		}
		elements[size++]=o;
	}
	
	public int size() {
		return size;
	}
	public Object elementAt(int i) {
		return elements[i];
	}
	public Vector getVector(){
		Vector v = new Vector();
		for (int i=0;i<size;i++) {
			v.add(elements[i]);
		}
		return v;
	}
	public String toString() {
		String s = new String();
		for (int i=0;i<size;i++) {
			s+=elements[i];
		}
		return s;
	}
}
