/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EditPanel.java
 *
 * Created on 5-dec-2008, 15:23:20
 */
package eu.scy.elobrowser.model.mapping.editor;

import eu.scy.elobrowser.model.mapping.DisplayProperty;
import eu.scy.elobrowser.model.mapping.Mapping;
import eu.scy.elobrowser.model.mapping.MappingElo;
import eu.scy.elobrowser.model.mapping.MappingTypes;
import eu.scy.elobrowser.model.mapping.impl.BasicMapping;
import eu.scy.elobrowser.model.mapping.impl.BasicMetadataDisplayMapping;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.springframework.util.StringUtils;
import roolo.elo.api.IMetadataKey;

/**
 *
 * @author sikkenj
 */
public class MetadataDisplayMappingEditorPanel extends javax.swing.JPanel
{

	private static final Logger logger = Logger.getLogger(MetadataDisplayMappingEditorPanel.class);
	private KeyTableCellRenderer keyTableCellRenderer = new KeyTableCellRenderer();
	private KeyTableCellEditor keyTableCellEditor = new KeyTableCellEditor(new JComboBox());
//	private ComboKeyTableCellRenderer comboKeyTableCellRenderer = new ComboKeyTableCellRenderer();
	private List<IMetadataKey> keys;
	private MappingElo mappingElo;

	/** Creates new form EditPanel */
	public MetadataDisplayMappingEditorPanel(MappingElo mappingElo, List<IMetadataKey> keys)
	{
		this.mappingElo = mappingElo;
		this.keys = keys;
		keys.add(0, null);
		keyTableCellRenderer.setKeys(keys);
		keyTableCellEditor.setKeys(keys);
		initComponents();
//		mappingTable.setDefaultRenderer(IMetadataKey.class, keyTableCellRenderer);
		mappingTable.getColumnModel().getColumn(1).setCellRenderer(keyTableCellRenderer);
		mappingTable.getColumnModel().getColumn(1).setCellEditor(keyTableCellEditor);
//		mappingTable.setDefaultEditor(IMetadataKey.class, keyTableCellEditor);
//		mappingTable.setCellEditor(keyTableCellEditor);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      nameLabel = new JLabel();
      nameField = new JTextField();
      descriptionLabel = new JLabel();
      descriptionScrollPane = new JScrollPane();
      descriptionField = new JTextArea();
      mappingTypeLabel = new JLabel();
      mappingTypeField = new JComboBox();
      mappingScrollPane = new JScrollPane();
      mappingTable = new JTable();

      nameLabel.setText("Name");

      nameField.setText(mappingElo.getName());

      descriptionLabel.setText("Description");

      descriptionField.setColumns(20);
      descriptionField.setRows(5);
      descriptionField.setText(mappingElo.getDescription());
      descriptionScrollPane.setViewportView(descriptionField);

      mappingTypeLabel.setText("Mapping type");

      mappingTypeField.setModel(new DefaultComboBoxModel(MappingTypes.values()));
      mappingTypeField.setSelectedItem(mappingElo.getMetadataDisplayMapping().getMappingType());

      mappingTable.setModel(new MappingsTableModel(mappingElo.getMetadataDisplayMapping().getMappings()));
      mappingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      mappingScrollPane.setViewportView(mappingTable);

      GroupLayout layout = new GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(GroupLayout.LEADING)
         .add(layout.createSequentialGroup()
            .addContainerGap()
            .add(layout.createParallelGroup(GroupLayout.LEADING)
               .add(mappingScrollPane, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
               .add(layout.createSequentialGroup()
                  .add(mappingTypeLabel)
                  .addPreferredGap(LayoutStyle.RELATED)
                  .add(mappingTypeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
               .add(layout.createSequentialGroup()
                  .add(layout.createParallelGroup(GroupLayout.LEADING)
                     .add(descriptionLabel)
                     .add(nameLabel))
                  .addPreferredGap(LayoutStyle.RELATED)
                  .add(layout.createParallelGroup(GroupLayout.TRAILING)
                     .add(nameField, GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                     .add(descriptionScrollPane, GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE))))
            .addContainerGap())
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(GroupLayout.LEADING)
         .add(layout.createSequentialGroup()
            .addContainerGap()
            .add(layout.createParallelGroup(GroupLayout.TRAILING)
               .add(mappingTypeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
               .add(layout.createSequentialGroup()
                  .add(layout.createParallelGroup(GroupLayout.LEADING)
                     .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(GroupLayout.LEADING)
                           .add(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                           .add(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(GroupLayout.LEADING)
                           .add(descriptionLabel)
                           .add(descriptionScrollPane, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)))
                     .add(nameLabel))
                  .add(14, 14, 14)
                  .add(mappingTypeLabel)))
            .addPreferredGap(LayoutStyle.UNRELATED)
            .add(mappingScrollPane, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
            .addContainerGap())
      );
   }// </editor-fold>//GEN-END:initComponents
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private JTextArea descriptionField;
   private JLabel descriptionLabel;
   private JScrollPane descriptionScrollPane;
   private JScrollPane mappingScrollPane;
   private JTable mappingTable;
   private JComboBox mappingTypeField;
   private JLabel mappingTypeLabel;
   private JTextField nameField;
   private JLabel nameLabel;
   // End of variables declaration//GEN-END:variables

	public void updateMappingElo()
	{
		mappingElo.setName(nameField.getText().trim());
		mappingElo.setDescription(descriptionField.getText().trim());
		MappingTypes mappingType = (MappingTypes) mappingTypeField.getSelectedItem();
		List<Mapping> mappings = new ArrayList<Mapping>();
		mappings.clear();
		TableModel mappingTableModel = mappingTable.getModel();
		for (int r = 0; r < mappingTableModel.getRowCount(); r++)
		{
			DisplayProperty displayProperty = (DisplayProperty) mappingTableModel.getValueAt(r, 0);
			IMetadataKey key = (IMetadataKey) mappingTableModel.getValueAt(r, 1);
			if (key != null)
			{
				Mapping mapping;
				String minimum = (String) mappingTableModel.getValueAt(r, 2);
				String maximum = (String) mappingTableModel.getValueAt(r, 3);
				if (StringUtils.hasText(minimum) && StringUtils.hasText(maximum))
				{
					float minimumValue = Float.parseFloat(minimum);
					float maximumValue = Float.parseFloat(maximum);
					mapping = new BasicMapping(displayProperty, key, minimumValue, maximumValue);
				}
				else
				{
					mapping = new BasicMapping(displayProperty, key);
				}
				mappings.add(mapping);
			}
		}
		BasicMetadataDisplayMapping metadataDisplayMapping = new BasicMetadataDisplayMapping(mappingType, mappings);
		mappingElo.setMetadataDisplayMapping(metadataDisplayMapping);
	}
}
