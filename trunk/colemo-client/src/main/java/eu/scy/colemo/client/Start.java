/*
 * Created on 30.sep.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.client;

//import javax.swing.UIManager;



/**
 * @author �ystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Start {

	public static void main(String[] args) {
	    /**
				    try {
		        UIManager.setLookAndFeel(
		        		"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		    } catch (Exception e) { }*/
		MainFrame frame = new MainFrame();
        frame.setUsername("scy");
        frame.setPassword("scy");
        frame.setHost("localhost");
	}
	
}
