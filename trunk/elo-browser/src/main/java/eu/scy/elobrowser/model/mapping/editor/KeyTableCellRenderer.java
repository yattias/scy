/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.editor;

import java.awt.Component;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import roolo.elo.api.IMetadataKey;

/**
 *
 * @author sikkenj
 */
public class KeyTableCellRenderer extends DefaultTableCellRenderer
{

	private List<IMetadataKey> keys;
	private JComboBox keySelector = new JComboBox();

	public void setKeys(List<IMetadataKey> keys)
	{
		this.keys = keys;
		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(keys.toArray());
		keySelector.setModel(comboBoxModel);
		keySelector.setRenderer(new KeyListCellRenderer());
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Object displayObject = value;
		if (value instanceof IMetadataKey)
		{
			IMetadataKey key = (IMetadataKey) value;
			keySelector.setSelectedItem(key);
			return keySelector;
//			displayObject = key.getId();
		}
		return super.getTableCellRendererComponent(table, displayObject, isSelected, hasFocus, row, column);
	}
	
	
}
