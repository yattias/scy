/*
 * Created on 03.nov.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.graphics;

import javax.swing.JLabel;

/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FieldLabel extends JLabel {
	
	private String text;
	private GraphicsClass gClass;
	
	public FieldLabel(String text,GraphicsClass gClass) {
		super(text);
		this.addMouseListener(gClass);
		this.text=text;
		this.gClass=gClass;
	}
}
