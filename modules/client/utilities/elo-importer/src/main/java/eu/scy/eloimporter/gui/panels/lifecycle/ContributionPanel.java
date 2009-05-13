package eu.scy.eloimporter.gui.panels.lifecycle;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.eloimporter.ContributeRoleValues;
import eu.scy.eloimporter.gui.EloImporterApplication;
import eu.scy.eloimporter.gui.PanelFactory;
import eu.scy.eloimporter.gui.panels.AbstractEloDisplayPanel;

public class ContributionPanel extends AbstractEloDisplayPanel implements
		PanelFactory {

	class InternalContributionPanel extends JPanel {

		private JComboBox roleComboBox;
		private JTextField entityTextField;
		private JTextField dateTextField;
		private JButton randomEntityButton;

		public InternalContributionPanel() {
			roleComboBox = new JComboBox(ContributeRoleValues.values());
			entityTextField = new JTextField();
			randomEntityButton = new JButton("Random");
			randomEntityButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String randomEntity = generateRandomEntity();
					entityTextField.setText(randomEntity);
				}

			});
			dateTextField = new JTextField();

			GridBagLayout layout = new GridBagLayout();
			setLayout(layout);

			{
				JLabel roleLabel = new JLabel("Role:");
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.weightx = 0.0;
				// c.gridwidth = 2;
				c.insets = new Insets(2, 2, 2, 2);
				c.anchor = GridBagConstraints.WEST;
				c.fill = GridBagConstraints.NONE;
				layout.setConstraints(roleLabel, c);
				add(roleLabel);
			}
			{
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 1;
				c.gridy = 0;
				c.gridwidth = 2;
				c.weightx = 1.0;
				c.insets = new Insets(2, 2, 2, 2);
				c.fill = GridBagConstraints.HORIZONTAL;
				layout.setConstraints(roleComboBox, c);
				add(roleComboBox);
			}
			{
				JLabel entityLabel = new JLabel("Entity:");
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 1;
				c.weightx = 0.0;
				c.insets = new Insets(2, 2, 2, 2);
				c.anchor = GridBagConstraints.WEST;
				c.fill = GridBagConstraints.NONE;
				layout.setConstraints(entityLabel, c);
				add(entityLabel);
			}
			{
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 0.9;
				c.insets = new Insets(2, 2, 2, 2);
				c.fill = GridBagConstraints.HORIZONTAL;
				layout.setConstraints(entityTextField, c);
				add(entityTextField);
			}
			{
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 2;
				c.gridy = 1;
				c.weightx = 0.1;
				c.insets = new Insets(2, 2, 2, 2);
				c.fill = GridBagConstraints.HORIZONTAL;
				layout.setConstraints(randomEntityButton, c);
				add(randomEntityButton);
			}
			{
				JLabel dateLabel = new JLabel("Date:");
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 2;
				c.weightx = 0.0;
				c.insets = new Insets(2, 2, 2, 2);
				c.anchor = GridBagConstraints.WEST;
				c.fill = GridBagConstraints.NONE;
				layout.setConstraints(dateLabel, c);
				add(dateLabel);
			}
			{
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 1;
				c.gridy = 2;
				c.weightx = 0.1;
				c.gridwidth = 2;
				c.insets = new Insets(2, 2, 2, 2);
				c.fill = GridBagConstraints.HORIZONTAL;
				layout.setConstraints(dateTextField, c);
				add(dateTextField);
			}
			setBorder(BorderFactory.createTitledBorder("Contribution"));
		}

		public void setRole(ContributeRoleValues role) {
			roleComboBox.setSelectedItem(role);
		}

		public void setEntity(String entity) {
			entityTextField.setText(entity);
		}

		public void setDate(long date) {
			DateFormat format = SimpleDateFormat.getDateTimeInstance();
			dateTextField.setText(format.format(new Date(date)));
		}
	}

	final class AddPanelAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JPanel panelToAdd = panelFactory.create();
			JButton removeButton = new JButton("-");
			removeButton.addActionListener(new RemovePanelAction());

			JPanel containerPanel = new JPanel();
			GridBagLayout layout = new GridBagLayout();
			containerPanel.setLayout(layout);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = button2Panel.size();
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.fill = GridBagConstraints.BOTH;
			layout.setConstraints(panelToAdd, c);
			containerPanel.add(panelToAdd);

			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = button2Panel.size();
			c.weightx = 0.0;
			c.weighty = 0.0;
			c.fill = GridBagConstraints.NONE;
			layout.setConstraints(removeButton, c);
			containerPanel.add(removeButton);

			add(containerPanel);
			button2Panel.put(removeButton, containerPanel);
			validate();
		}
	}

	final class RemovePanelAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			JPanel panelToRemove = button2Panel.get(button);

			remove(panelToRemove);
			button2Panel.remove(button);
			updateLayout();

			Dimension oldSize = getParent().getSize();
			getParent().setSize(oldSize.width + 1, oldSize.height + 1);
			getParent().setSize(oldSize.width, oldSize.height);
		}
	}

	private EloImporterApplication application;
	private IELO<IMetadataKey> elo;
	private ContributionPanel panelFactory;
	private LinkedHashMap<JButton, JPanel> button2Panel;
	private IMetadataKey contributeRoleKey;
	private IMetadataKey contributeEntityKey;
	private IMetadataKey contributeDateKey;

	public ContributionPanel(EloImporterApplication app) {
		this.application = app;
		this.elo = this.application.getElo();
		contributeRoleKey = this.application.getImporter().getTypeManager()
				.getMetadataKey("contribute/role/value");
		contributeEntityKey = this.application.getImporter().getTypeManager()
				.getMetadataKey("contribute/entity");
		contributeDateKey = this.application.getImporter().getTypeManager()
				.getMetadataKey("contribute/date");

		this.panelFactory = this;
		this.setLayout(new GridLayout(0, 1));
		this.button2Panel = new LinkedHashMap<JButton, JPanel>();

		this.initComponents();
	}

	private void initComponents() {
		JPanel panel = this.panelFactory.create();
		JButton addButton = new JButton("+");
		addButton.addActionListener(new AddPanelAction());

		JPanel containerPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		containerPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = button2Panel.size();
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		layout.setConstraints(panel, c);
		containerPanel.add(panel);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = button2Panel.size();
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		layout.setConstraints(addButton, c);
		containerPanel.add(addButton);

		this.add(containerPanel);
		this.button2Panel.put(addButton, containerPanel);
	}

	void updateLayout() {
		this.removeAll();

		this.setLayout(new GridLayout(0, 1));
		for (Entry<JButton, JPanel> entry : this.button2Panel.entrySet()) {
			this.add(entry.getValue());
		}
		this.validate();
	}

	@Override
	public void setElo(IELO<IMetadataKey> e) {
		this.elo = null;
		this.removeAll();
		this.button2Panel.clear();
		this.initComponents();

		JButton addButton = this.button2Panel.keySet().iterator().next();
		InternalContributionPanel contributionPanel = (InternalContributionPanel) this.button2Panel
				.get(addButton).getComponent(0);

		ContributeRoleValues role = ContributeRoleValues.valueOf((String) e
				.getMetadata().getMetadataValueContainer(contributeRoleKey)
				.getValue());
		contributionPanel.setRole(role);

		String entity = (String) e.getMetadata().getMetadataValueContainer(
				contributeEntityKey).getValue();
		contributionPanel.setEntity(entity);

		long date = (Long) e.getMetadata().getMetadataValueContainer(
				contributeDateKey).getValue();
		contributionPanel.setDate(date);

		this.elo = e;

	}

	@Override
	public JPanel create() {
		return new InternalContributionPanel();
	}

	String generateRandomEntity() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

}
