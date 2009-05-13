/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.editor;

import java.awt.Component;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import roolo.elo.api.IMetadataKey;

/**
 *
 * @author sikkenj
 */
public class KeyTableCellEditor extends DefaultCellEditor
{

	private List<IMetadataKey> keys;
	private JComboBox keySelector;

	public KeyTableCellEditor(JComboBox keySelector)
	{
		super(keySelector);
		this.keySelector = keySelector;
		keySelector.setRenderer(new KeyListCellRenderer());
	}

	public void setKeys(List<IMetadataKey> keys)
	{
		this.keys = keys;
		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(keys.toArray());
		keySelector.setModel(comboBoxModel);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if (value instanceof IMetadataKey)
		{
			IMetadataKey key = (IMetadataKey) value;
			keySelector.setSelectedItem(key);
			return keySelector;
		}
		else
		{
			return super.getTableCellEditorComponent(table, value, isSelected, row, column);
		}
	}
}
