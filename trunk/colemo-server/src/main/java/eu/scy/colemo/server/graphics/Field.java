/*
 * Created on 03.nov.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.graphics;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import eu.scy.colemo.server.network.Client;
import eu.scy.colemo.server.network.Connection;

import eu.scy.colemo.contributions.DeleteField;

/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Field extends JLabel implements MouseListener{

	private String field;
	private GraphicsClass gClass;
	
	public Field(String field,GraphicsClass gClass) {
		super(field);
		this.addMouseListener(this);
		this.field=field;
		this.gClass=gClass;
	}

	public void mouseClicked(MouseEvent arg0) {}

	public void mouseEntered(MouseEvent arg0) {}
	
	public void mouseExited(MouseEvent arg0) {}
	
	public void mousePressed(MouseEvent e) {
		if(e.getModifiers() ==InputEvent.BUTTON3_MASK){
			int i = JOptionPane.showConfirmDialog(this,"Do you want to delete this field?");
			if(i==0) {
				Client client = gClass.getGraphicsDiagram().getMainFrame().getClient();
				Connection connection = client.getConnection();
				InetAddress address = connection.getSocket().getLocalAddress();
				DeleteField deleteField = new DeleteField(gClass.getUmlClass(),field,address,client.getPerson());
				gClass.getGraphicsDiagram().getMainFrame().getClient().getConnection().send(deleteField);
			}
		}
		else{
			gClass.setFieldsMaximized(!gClass.getUmlClass().showFields());
		}
	}

	public void mouseReleased(MouseEvent arg0) {}

	public String getField() {
		return field;
	}
}
