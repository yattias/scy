/**
 * 
 */
package eu.scy.eloimporter.gui.panels.general;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.eloimporter.gui.EloImporterApplication;
import eu.scy.eloimporter.gui.panels.AbstractEloDisplayPanel;

public class TitlePanel extends AbstractEloDisplayPanel {

	private Locale[] locales;
	protected int currentY;
	LinkedList<JComboBox> comboBoxes;
	LinkedList<JTextField> textFields;
	LinkedList<JButton> buttons;
	private EloImporterApplication application;
	IELO<IMetadataKey> elo;
	IMetadataKey titleKey;

	public TitlePanel(EloImporterApplication eloImporterApplication) {
		this.application = eloImporterApplication;
		IMetadataTypeManager<IMetadataKey> typeManager = this.application.getImporter()
				.getTypeManager();
		this.titleKey = typeManager.getMetadataKey("title");

		this.comboBoxes = new LinkedList<JComboBox>();
		this.textFields = new LinkedList<JTextField>();
		this.buttons = new LinkedList<JButton>();

		this.currentY = 0;
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		this.currentY = 0;

		this.locales = Locale.getAvailableLocales();
		Arrays.sort(this.locales, new Comparator<Locale>() {

			public int compare(Locale o1, Locale o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});

		this.addComponents(true, layout);
	}

	void addComponents(boolean add, final GridBagLayout layout) {
		final JComboBox box = new JComboBox(this.locales);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = this.currentY;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		layout.setConstraints(box, c);
		this.add(box);
		this.comboBoxes.add(box);

		JTextField titleField = new JTextField();
		titleField.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent e) {
				// not needed
			}

			public void insertUpdate(DocumentEvent e) {
				IMetadataValueContainer titleContainer = TitlePanel.this.elo.getMetadata()
						.getMetadataValueContainer(TitlePanel.this.titleKey);
				try {
					titleContainer.setValue(
							e.getDocument().getText(0, e.getDocument().getLength()), (Locale) box
									.getSelectedItem());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}

			public void removeUpdate(DocumentEvent e) {
				IMetadataValueContainer titleContainer = TitlePanel.this.elo.getMetadata()
						.getMetadataValueContainer(TitlePanel.this.titleKey);
				try {
					titleContainer.setValue(
							e.getDocument().getText(0, e.getDocument().getLength()), (Locale) box
									.getSelectedItem());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
		});
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = this.currentY;
		c.weightx = 0.5;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		layout.setConstraints(titleField, c);
		this.add(titleField);
		this.textFields.add(titleField);

		if (add) {
			JButton button = new JButton("+");
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					TitlePanel.this.currentY++;
					TitlePanel.this.addComponents(false, layout);
					TitlePanel.this.validate();
				}

			});
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = this.currentY;
			c.weightx = 0.0;
			c.weighty = 0.0;
			c.fill = GridBagConstraints.NONE;
			layout.setConstraints(button, c);
			this.add(button);
			this.buttons.add(button);
		} else {
			JButton button = new JButton("-");
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					TitlePanel.this
							.remove(TitlePanel.this.comboBoxes.get(TitlePanel.this.currentY));
					TitlePanel.this.comboBoxes.removeLast();
					TitlePanel.this
							.remove(TitlePanel.this.textFields.get(TitlePanel.this.currentY));
					TitlePanel.this.textFields.removeLast();
					TitlePanel.this.remove(TitlePanel.this.buttons.get(TitlePanel.this.currentY));
					TitlePanel.this.buttons.removeLast();
					Dimension oldSize = TitlePanel.this.getParent().getSize();
					TitlePanel.this.getParent().setSize(oldSize.width + 1, oldSize.height + 1);
					TitlePanel.this.getParent().setSize(oldSize.width, oldSize.height);

					TitlePanel.this.currentY--;
				}

			});
			c = new GridBagConstraints();
			c.gridx = 2;
			c.gridy = this.currentY;
			c.weightx = 0.0;
			c.weighty = 0.0;
			c.fill = GridBagConstraints.NONE;
			layout.setConstraints(button, c);
			this.add(button);
			this.buttons.add(button);
		}
	}

	@Override
	public void setElo(IELO<IMetadataKey> elo) {
		this.elo = elo;
		IMetadataValueContainer values = elo.getMetadata().getMetadataValueContainer(this.titleKey);
		String title = (String) values.getValue();

		this.comboBoxes.clear();
		this.textFields.clear();
		this.buttons.clear();
		this.removeAll();
		this.currentY = 0;
		this.addComponents(true, (GridBagLayout) this.getLayout());
		this.textFields.get(this.currentY).setText(title);
	}
}
