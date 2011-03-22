package eu.scy.agents.queryexpansion.importer.gui;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Space;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class SQLSpacesConnectionDialog extends JDialog implements
		ActionListener, DocumentListener, WindowListener {

	private JTextField tfHost;

	private JTextField tfPort;

	private JTextField tfUsername;

	private JPasswordField tfPassword;

	private JComboBox cbSpace;

	private JButton btOk;

	private JButton btCancel;

	private JButton btSelector;

	private JPanel pnCenter;
	
	private SpaceNameSelector spaceNameSelector;

	private int port;

	private User user;

	private String host;

	private String space;
	
	private File ontologyFile;

	private boolean canceled;

	public static void main(String[] args) {
		new SQLSpacesConnectionDialog(null, false, true, User.getDefaultUser(), null);
	}

	public SQLSpacesConnectionDialog(Frame owner, boolean editCredentials,
			boolean createNewSpace, User defaultUser, SpaceNameSelector spaceNameSel) {
		super(owner, "Connection", true);
		this.spaceNameSelector = spaceNameSel;
		this.canceled = false;
		this.tfHost = new JTextField("scy.collide.info");
		this.tfPort = new JTextField("2525");
		this.tfUsername = new JTextField(defaultUser.getUsername());
		this.tfPassword = new JPasswordField(defaultUser.getPassword());
		this.cbSpace = new JComboBox();
		this.cbSpace.addActionListener(this);
		this.btSelector = new JButton(this.spaceNameSelector.getButtonText());
		this.btSelector.addActionListener(this);
		this.btCancel = new JButton("Cancel");
		this.btOk = new JButton("OK");
		this.btCancel.addActionListener(this);
		this.btOk.addActionListener(this);
		this.btOk.setEnabled(false);
		addWindowListener(this);

		JPanel pnSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnSouth.add(btOk);
		pnSouth.add(btCancel);
		
		JLabel lbHost = new JLabel("Host");
		JLabel lbPort = new JLabel("Port");
		JLabel lbSpace = new JLabel("Space");
		JLabel lbUsername = new JLabel("Username");
		JLabel lbPassword = new JLabel("Password");
		
		GridBagLayout gbl = new GridBagLayout();
		pnCenter = new JPanel(gbl);
		GridBagConstraints gbc = null;
		gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);
		gbl.setConstraints(lbHost, gbc);
		gbc = new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);
		gbl.setConstraints(lbPort, gbc);
		gbc = new GridBagConstraints(0, editCredentials ? 4 : 2, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0);
		gbl.setConstraints(lbSpace, gbc);
		gbc = new GridBagConstraints(1, 0, 2, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		gbl.setConstraints(tfHost, gbc);
		gbc = new GridBagConstraints(1, 1, 2, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		gbl.setConstraints(tfPort, gbc);
		gbc = new GridBagConstraints(1, editCredentials ? 4 : 2, 1, 1, 1, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		gbl.setConstraints(cbSpace, gbc);
		gbc = new GridBagConstraints(2, editCredentials ? 4 : 2, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0);
		gbl.setConstraints(btSelector, gbc);
		
		pnCenter.add(lbHost);
		pnCenter.add(lbPort);
		pnCenter.add(lbSpace);
		pnCenter.add(tfHost);
		pnCenter.add(tfPort);
		pnCenter.add(cbSpace);
		pnCenter.add(btSelector);

		if (editCredentials) {
			gbc = new GridBagConstraints(0, 2, 1, 1, 0, 0,
					GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(5, 5, 5, 5), 0, 0);
			gbl.setConstraints(lbUsername, gbc);
			gbc = new GridBagConstraints(0, 3, 1, 1, 0, 0,
					GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(5, 5, 5, 5), 0, 0);
			gbl.setConstraints(lbPassword, gbc);
			gbc = new GridBagConstraints(1, 2, 2, 1, 1, 0,
					GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
					new Insets(5, 5, 5, 5), 0, 0);
			gbl.setConstraints(tfUsername, gbc);
			gbc = new GridBagConstraints(1, 3, 2, 1, 1, 0,
					GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
					new Insets(5, 5, 5, 5), 0, 0);
			gbl.setConstraints(tfPassword, gbc);
			
			pnCenter.add(lbUsername);
			pnCenter.add(lbPassword);
			pnCenter.add(tfUsername);
			pnCenter.add(tfPassword);
		}

		if (createNewSpace) {
			cbSpace.setEditable(createNewSpace);
			JTextField editor = (JTextField) cbSpace.getEditor()
					.getEditorComponent();
			editor.getDocument().addDocumentListener(this);
		}

		setLayout(new BorderLayout(5, 5));
		add(pnCenter, BorderLayout.CENTER);
		add(pnSouth, BorderLayout.SOUTH);
		pack();
		setSize(300, getHeight());
		setLocationRelativeTo(owner);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btOk)) {

			try {
				this.port = Integer.parseInt(tfPort.getText().trim());
				this.host = tfHost.getText();
				this.user = new User(tfUsername.getText(), new String(
						tfPassword.getPassword()));
				this.space = cbSpace.getSelectedItem().toString();
				
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this,
						"The port you entered is not numerical", "Error",
						JOptionPane.ERROR_MESSAGE);
				btOk.setEnabled(false);
				return;
			}
			setVisible(false);
		} else if (e.getSource().equals(btCancel)) {
			canceled = true;
			setVisible(false);
		} else if (e.getSource().equals(cbSpace)) {
			btOk.setEnabled(cbSpace.getSelectedItem() != null);
		} else if (e.getSource().equals(btSelector)) {
			
			TupleSpace ts = null;
			try {
				this.port = Integer.parseInt(tfPort.getText().trim());
				this.host = tfHost.getText();				
				this.user = new User(tfUsername.getText(), new String(
						tfPassword.getPassword()));
				ts = new TupleSpace(this.user, tfHost.getText(), port);

				DefaultComboBoxModel model = ((DefaultComboBoxModel) (cbSpace
						.getModel()));
				model.removeAllElements();
				
				String[] spaceNames = this.spaceNameSelector.getAllSpaceNames(ts);
				for (String spaceName : spaceNames) {
					model.addElement(spaceName);
				}

				if (model.getSize() > 0) {
					btOk.setEnabled(true);
				} else {
					btOk.setEnabled(false);
				}

			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this,
						"The port you entered is not numerical", "Error",
						JOptionPane.ERROR_MESSAGE);
				btOk.setEnabled(false);
				return;
			} catch (TupleSpaceException e1) {
				JOptionPane.showMessageDialog(this,
						"The following error occurred:\n" + e1.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
				btOk.setEnabled(false);
				return;
			} finally {
				if (ts != null) {
					try {
						ts.disconnect();
					} catch (TupleSpaceException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
		
	public static String[] readAvailableSpaces(TupleSpace ts, String spaceNamePrefix) throws TupleSpaceException {
		Space[] allSpaces = ts.getAllSpaces();
		List<String> spaceList = new ArrayList<String>();
		for (Space space : allSpaces) {
			if (spaceNamePrefix == null || space.getName().startsWith(spaceNamePrefix)) {
				spaceList.add(space.getName());
			}
		}
		String[] spaces = new String[spaceList.size()];
		return spaceList.toArray(spaces);
	}

	private void processDocumentEvent(
			@SuppressWarnings("unused") DocumentEvent e) {
		btOk.setEnabled(false);
	}

	public void changedUpdate(DocumentEvent e) {
		processDocumentEvent(e);
	}

	public void insertUpdate(DocumentEvent e) {
		processDocumentEvent(e);
	}

	public void removeUpdate(DocumentEvent e) {
		processDocumentEvent(e);
	}

	public TupleSpace getTuplespace() throws TupleSpaceException {
		return new TupleSpace(user, host, port, space);
	}

	public String getHost() {
		return host;
	}

	public String getSpace() {
		return space;
	}

	public User getUser() {
		return user;
	}

	public int getPort() {
		return port;
	}
	
	public File getOntologyFile() {
		return ontologyFile;
	}

	public boolean wasCanceled() {
		return canceled;
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		this.canceled = true;
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}
}
