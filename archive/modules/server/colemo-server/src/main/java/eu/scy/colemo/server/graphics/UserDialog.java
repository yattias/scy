/*
 * Created on 16.nov.2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.graphics;

import java.awt.*;
import javax.swing.*;
import java.util.*;

class UserDialog {
	String userName, serverip;
	boolean cancel=false;
	UserDialog(){
		getIDandPassword();
	}

	// modal dialog to get user ID and password
	static String[] ConnectOptionNames = { "Login", "Cancel" };

	static String ConnectTitle = "Connect to server";

	void getIDandPassword() {
		JPanel connectionPanel;

		// Create the labels and text fields.
		JLabel userNameLabel = new JLabel("User name   :", JLabel.LEFT);
		JTextField userNameField = new JTextField(getName());
		JLabel serverIp = new JLabel("Server IP   :", JLabel.LEFT);
		//JTextField serverIpField = new JTextField("129.177.34.253");
		JTextField serverIpField = new JTextField("127.0.0.1");
		connectionPanel = new JPanel(false);
		connectionPanel.setLayout(new BoxLayout(connectionPanel,
				BoxLayout.X_AXIS));
		JPanel namePanel = new JPanel(false);
		namePanel.setLayout(new GridLayout(0, 1));
		namePanel.add(userNameLabel);
		namePanel.add(serverIp);
		JPanel fieldPanel = new JPanel(false);
		fieldPanel.setLayout(new GridLayout(0, 1));
		fieldPanel.add(userNameField);
		fieldPanel.add(serverIpField);
		connectionPanel.add(namePanel);
		connectionPanel.add(fieldPanel);
		serverIpField.setEditable(true);

		// Connect or quit
		if (JOptionPane.showOptionDialog(null, connectionPanel, ConnectTitle,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, ConnectOptionNames, ConnectOptionNames[0]) != 0) {
			cancel=true;
		}
		userName = userNameField.getText();
		serverip = serverIpField.getText();
	}
	
	public String getName() {
	    Vector names = new Vector();
	    names.add("Øystein");
	    names.add("Roger");
	    names.add("Espen");
	    names.add("Bjarte");
	    names.add("Jan");
	    names.add("Kim");
	    names.add("Muhammed");
	    names.add("Atle");
	    names.add("Per");
	    names.add("Kristian");
	    names.add("Tim");
	    names.add("Burt");
	    names.add("Paul");
	    names.add("Charlie");
	    names.add("Ragnvald");
	    names.add("Roar");
	    names.add("Morten");
	    names.add("Marius");
	    names.add("Harald");
	    names.add("Rolf");
	    names.add("Bjørnar");
	    names.add("Svein");
	    names.add("Kjetil");
	    names.add("Bjørn");
	    names.add("Bezu");
	    names.add("Robert");
	    
	    int size = names.size();
	    Random generator = new Random();
	    int number = generator.nextInt(size);
	    String s =(String)names.elementAt(number);
	    return s;
	    
	}


}