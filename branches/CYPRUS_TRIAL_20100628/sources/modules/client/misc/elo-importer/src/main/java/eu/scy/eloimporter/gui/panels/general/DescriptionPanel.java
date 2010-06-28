package eu.scy.eloimporter.gui.panels.general;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.eloimporter.gui.EloImporterApplication;
import eu.scy.eloimporter.gui.PanelFactory;
import eu.scy.eloimporter.gui.panels.AbstractEloDisplayPanel;

public class DescriptionPanel extends AbstractEloDisplayPanel implements
		PanelFactory {

	class InternalPanel extends JPanel {

		JComboBox localeChooser;
		JTextArea textField;

		public InternalPanel() {
			this.localeChooser = new JComboBox();

			GridBagLayout layout = new GridBagLayout();
			this.setLayout(layout);

			this.localeChooser = new JComboBox(DescriptionPanel.this.locales);
			this.localeChooser.addActionListener(new ActionListener() {

				private Locale oldLocale;

				@Override
				public void actionPerformed(ActionEvent e) {
					if (DescriptionPanel.this.elo != null) {
						IMetadataValueContainer value = DescriptionPanel.this.elo
								.getMetadata().getMetadataValueContainer(
										DescriptionPanel.this.descriptionKey);
						if (this.oldLocale != null) {
							value.deleteLanguage(this.oldLocale);
						}
						Locale locale = (Locale) InternalPanel.this.localeChooser
								.getSelectedItem();
						String text = InternalPanel.this.textField.getText();
						value.setValue(text, locale);
						this.oldLocale = locale;
					}
				}
			});
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0.0;
			c.weighty = 0.0;
			c.fill = GridBagConstraints.NONE;
			layout.setConstraints(this.localeChooser, c);
			this.add(this.localeChooser);

			this.textField = new JTextArea(2, 30);
			this.textField.getDocument().addDocumentListener(
					new DocumentListener() {

						@Override
						public void changedUpdate(DocumentEvent e) {
							// not needed
						}

						@Override
						public void insertUpdate(DocumentEvent e) {
							try {
								this.updateEloTitle(e);
							} catch (BadLocationException e1) {
								e1.printStackTrace();
							}
						}

						private void updateEloTitle(DocumentEvent e)
								throws BadLocationException {
							String text = e.getDocument().getText(0,
									e.getDocument().getLength());
							Locale locale = (Locale) InternalPanel.this.localeChooser
									.getSelectedItem();
							if (DescriptionPanel.this.elo != null) {
								IMetadataValueContainer value = DescriptionPanel.this.elo
										.getMetadata()
										.getMetadataValueContainer(
												DescriptionPanel.this.descriptionKey);
								value.deleteLanguage(locale);
								value.setValue(text, locale);
							}
						}

						@Override
						public void removeUpdate(DocumentEvent e) {
							try {
								this.updateEloTitle(e);
							} catch (BadLocationException e1) {
								e1.printStackTrace();
							}
						}
					});
			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.weightx = 1.0;
			c.weighty = 0.1;
			c.fill = GridBagConstraints.HORIZONTAL;
			layout.setConstraints(this.textField, c);
			this.add(this.textField);
		}

		public void setText(String string) {
			this.textField.setText(string);
			this.textField.repaint();
		}

		public void setSelectedLocaleIndex(int index) {
			this.localeChooser.setSelectedIndex(index);
		}
	}

	final class AddPanelAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JPanel panelToAdd = DescriptionPanel.this.panelFactory.create();
			JButton removeButton = new JButton("-");
			removeButton.addActionListener(new RemovePanelAction());

			JPanel containerPanel = new JPanel();
			GridBagLayout layout = new GridBagLayout();
			containerPanel.setLayout(layout);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = DescriptionPanel.this.button2Panel.size();
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.fill = GridBagConstraints.BOTH;
			layout.setConstraints(panelToAdd, c);
			containerPanel.add(panelToAdd);

			c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = DescriptionPanel.this.button2Panel.size();
			c.weightx = 0.0;
			c.weighty = 0.0;
			c.fill = GridBagConstraints.NONE;
			layout.setConstraints(removeButton, c);
			containerPanel.add(removeButton);

			DescriptionPanel.this.add(containerPanel);
			DescriptionPanel.this.button2Panel
					.put(removeButton, containerPanel);
			DescriptionPanel.this.validate();
		}

	}

	final class RemovePanelAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			JPanel panelToRemove = DescriptionPanel.this.button2Panel
					.get(button);

			DescriptionPanel.this.remove(panelToRemove);
			DescriptionPanel.this.button2Panel.remove(button);
			DescriptionPanel.this.updateLayout();

			Dimension oldSize = DescriptionPanel.this.getParent().getSize();
			DescriptionPanel.this.getParent().setSize(oldSize.width + 1,
					oldSize.height + 1);
			DescriptionPanel.this.getParent().setSize(oldSize.width,
					oldSize.height);
		}
	}

	Locale[] locales;
	LinkedHashMap<JButton, JPanel> button2Panel;
	PanelFactory panelFactory;
	private EloImporterApplication application;
	IMetadataKey descriptionKey;
	IELO<IMetadataKey> elo;

	public DescriptionPanel(EloImporterApplication app) {
		this.application = app;
		this.elo = this.application.getElo();
		this.descriptionKey = this.application.getImporter().getTypeManager()
				.getMetadataKey("description");
		this.panelFactory = this;
		this.setLayout(new GridLayout(0, 1));
		this.button2Panel = new LinkedHashMap<JButton, JPanel>();

		this.fillLocales();

		this.initComponents();
	}

	private void fillLocales() {
		Locale[] tmp_locales = Locale.getAvailableLocales();
		Set<Locale> locale_set = new HashSet<Locale>();
		for (Locale locale : tmp_locales) {
			if ("".equals(locale.getCountry())) {
				locale_set.add(locale);
			}
		}

		this.locales = new Locale[locale_set.size()];
		int i = 0;
		for (Locale locale : locale_set) {
			this.locales[i] = locale;
			i++;
		}
		Arrays.sort(this.locales, new Comparator<Locale>() {

			public int compare(Locale o1, Locale o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
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
		c.gridy = this.button2Panel.size();
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		layout.setConstraints(panel, c);
		containerPanel.add(panel);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = this.button2Panel.size();
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		layout.setConstraints(addButton, c);
		containerPanel.add(addButton);

		this.add(containerPanel);
		this.button2Panel.put(addButton, containerPanel);
	}

	@Override
	public void setElo(IELO<IMetadataKey> elo) {
		this.elo = null;
		this.removeAll();
		this.button2Panel.clear();
		this.initComponents();

		IMetadataValueContainer value = elo.getMetadata()
				.getMetadataValueContainer(this.descriptionKey);
		String title = null;

		JButton addButton = this.button2Panel.keySet().iterator().next();
		InternalPanel titlePanel = (InternalPanel) this.button2Panel.get(
				addButton).getComponent(0);
		for (int i = 0; i < this.locales.length; i++) {
			if (this.locales[i].getLanguage().equals(
					Locale.getDefault().getLanguage())) {
				titlePanel.setSelectedLocaleIndex(i);
				title = (String) value.getValue(this.locales[i]);
				break;
			}
		}

		titlePanel.setText(title);
		this.repaint();

		this.elo = elo;
	}

	@Override
	public JPanel create() {
		return new InternalPanel();
	}

	void updateLayout() {
		this.removeAll();

		this.setLayout(new GridLayout(0, 1));
		for (Entry<JButton, JPanel> entry : this.button2Panel.entrySet()) {
			this.add(entry.getValue());
		}
		this.validate();
	}

}
