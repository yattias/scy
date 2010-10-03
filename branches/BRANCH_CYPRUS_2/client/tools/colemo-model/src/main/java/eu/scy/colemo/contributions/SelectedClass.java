/*
 * Created on 10.nov.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.contributions;

import java.io.Serializable;

import eu.scy.colemo.server.uml.UmlClass;

/**
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SelectedClass implements Serializable{

	private UmlClass umlClass;
	
	public SelectedClass(UmlClass umlClass) {
		this.umlClass=umlClass;
	}

	/**
	 * @return Returns the umlClass.
	 */
	public UmlClass getUmlClass() {
		return umlClass;
	}
}
