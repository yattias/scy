/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.editor;

import eu.scy.elobrowser.model.mapping.MappingElo;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author sikkenj
 */
public class MappingEloListCellRenderer extends DefaultListCellRenderer
{

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		Object valueDisplay = value;
		if (value instanceof MappingElo)
		{
			MappingElo mappingElo = (MappingElo) value;
			valueDisplay = mappingElo.getName();
		}
		return super.getListCellRendererComponent(list, valueDisplay, index, isSelected, cellHasFocus);
	}
}
