/*
 * Created on 05.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.uml;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UmlAdmin {

	private String host;
	private int port;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private UmlDiagram diagram;
	
	public UmlAdmin(String host, int port,UmlDiagram diagram) {
		this.host=host;
		this.port=port;
		
	}

}
