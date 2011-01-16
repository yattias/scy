package eu.scy.client.tools.formauthor;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class ConfigDialog extends JDialog {
	JLabel jlRepositoryURL = new JLabel("URL:");
	JTextField jtRepositoryURL = new JTextField();
	JLabel jlAuthor = new JLabel(Localizer.getString("AUTHOR"));
	JTextField jtAuthor = new JTextField();
	JLabel jlLanguage = new JLabel(Localizer.getString("LANGUAGE") + ":");
	JComboBox jcbLanguage;
	JButton jbtnClose = new JButton("OK");
	JButton jbtnCancel = new JButton(Localizer.getString("CANCEL"));
	JDialog dialog = this;
	final Configuration config = new Configuration();
	String oldLang = config.getLanguage();

	public ConfigDialog(Frame owner, boolean modal) {
		super(owner, modal);
		init();
	}

	private void init() {

		jbtnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				config.setServerUrl(jtRepositoryURL.getText());
				config.setLanguage((String) jcbLanguage.getSelectedItem());
				config.setGroupname((String) jtAuthor.getText());
				config.saveConfigurationToFile();
				if (!oldLang.equals(config.getLanguage()))
					JOptionPane.showMessageDialog(dialog, Localizer
							.getString("CHANGE_LANGUAGE"));
				dialog.dispose();
			}
		});

		jbtnCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		this.setTitle(Localizer.getString("CONFIGURATION"));
		jtRepositoryURL.setText(config.getServerUrl());
		jtAuthor.setText(config.getGroupname());
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JLabel jlInfoText = new JLabel(Localizer.getString("URL_INFO_LABEL")
				+ ":");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipadx = 0;
		c.weightx = 0;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10, 10, 10, 10);
		this.add(jlInfoText, c);

		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.1;
		c.insets = new Insets(0, 10, 0, 0);
		this.add(jlRepositoryURL, c);

		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.9;
		c.insets = new Insets(0, 0, 0, 10);
		this.add(jtRepositoryURL, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		c.insets = new Insets(10, 10, 10, 10);
		this.add(jlLanguage, c);

		jcbLanguage = new JComboBox();

		jcbLanguage.addItem("Deutsch");
		jcbLanguage.addItem("English");

		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 0;
		c.insets = new Insets(0, 0, 0, 10);
		this.add(jcbLanguage, c);

		if (config.getLanguage().equals("de"))
			jcbLanguage.setSelectedItem("Deutsch");
		if (config.getLanguage().equals("en"))
			jcbLanguage.setSelectedItem("English");

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0.1;
		c.insets = new Insets(10, 10, 10, 10);
		this.add(jlAuthor, c);

		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 0.9;
		c.insets = new Insets(0, 0, 0, 10);
		this.add(jtAuthor, c);

		JPanel jpButtonPanel = new JPanel();
		jpButtonPanel.add(jbtnCancel);
		jpButtonPanel.add(jbtnClose);

		c.weightx = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(10, 10, 10, 10);
		this.add(jpButtonPanel, c);

	}
}
