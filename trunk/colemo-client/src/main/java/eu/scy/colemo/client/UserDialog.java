/*
 * Created on 16.nov.2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.client;

import java.awt.*;
import javax.swing.*;
import java.util.*;

class UserDialog {
	private String userName, serverip;
    private String password;
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
		JLabel passwordLabel = new JLabel("Password   :", JLabel.LEFT);
		JTextField userNameField = new JTextField();
		JTextField passwordField = new JTextField();
		JLabel serverIp = new JLabel("Server IP   :", JLabel.LEFT);
		JTextField serverIpField = new JTextField("127.0.0.1");
        connectionPanel = new JPanel(false);

        connectionPanel.setLayout(new GridLayout(3,2));
        connectionPanel.add(userNameLabel);
        connectionPanel.add(userNameField);
        connectionPanel.add(passwordLabel);
        connectionPanel.add(passwordField);
        connectionPanel.add(serverIp);
        connectionPanel.add(serverIpField);


		/*connectionPanel.setLayout(new BoxLayout(connectionPanel, BoxLayout.X_AXIS));
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
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new GridLayout(0,1));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        connectionPanel.add(passwordPanel);
         */
		// Connect or quit
		if (JOptionPane.showOptionDialog(null, connectionPanel, ConnectTitle,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, ConnectOptionNames, ConnectOptionNames[0]) != 0) {
			cancel=true;
		}
		this.userName = userNameField.getText();
		this.serverip = serverIpField.getText();
        this.password = passwordField.getText();
	}
	
	public String getUserName() {
	    return this.userName;
	}

    public String getPassword() {
        return this.password;
    }

    public String getServerIp() {
        return this.serverip;
    }


}