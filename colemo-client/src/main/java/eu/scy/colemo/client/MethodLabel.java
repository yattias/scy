/*
 * Created on 03.nov.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.client;

import javax.swing.JLabel;

/**
 * @author �ystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MethodLabel extends JLabel {
	
	private String text;
	private ConceptNode gClass;
	
	public MethodLabel(String text, ConceptNode gClass) {
		super(text);
		this.addMouseListener(gClass);
		this.text=text;
		this.gClass=gClass;
	}
}
