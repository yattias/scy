/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.model.mapping.editor;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import roolo.elo.api.IMetadataKey;

/**
 *
 * @author sikkenj
 */
public class KeyListCellRenderer extends BasicComboBoxRenderer
{

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		Object displayObject = value;
		if (value instanceof IMetadataKey)
		{
			IMetadataKey key = (IMetadataKey)value;
			displayObject = key.getId();
		}
		return super.getListCellRendererComponent(list, displayObject, index, isSelected, cellHasFocus);
	}

}
