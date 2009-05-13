/*
 * Created on 01.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.graphics;

import java.awt.Font;
import javax.swing.JLabel;

/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClassLabel extends JLabel {
	private GraphicsClass gClass;
		
	public ClassLabel(GraphicsClass gClass,String preName){
		super(preName+(gClass.getUmlClass().getName()).toUpperCase());
		this.setFont(new Font("sansserif", Font.BOLD, 14));
		this.gClass=gClass;
		this.addMouseMotionListener(gClass);
		this.addMouseListener(gClass);
		
		
	}

	
}
