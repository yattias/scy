/*
 * Created on 01.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.client;

import java.util.Vector;

import javax.swing.JList;

/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MethodList extends JList {
	ConceptNode gClass;
	public MethodList(ConceptNode gClass, Vector methods){
		super(methods);
		this.gClass=gClass;
		this.addMouseListener(gClass);
		this.addMouseMotionListener(gClass);
	}
}
