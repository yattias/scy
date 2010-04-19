/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.editor;

import eu.scy.elobrowser.model.mapping.DisplayProperty;
import eu.scy.elobrowser.model.mapping.Mapping;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sikkenj
 */
public class MappingsTableModel extends DefaultTableModel
{

	private final String[] columnNames =
	{
		"Property", "Key", "Minimum", "Maximum"
	};
	private List<Mapping> mappings;

	public MappingsTableModel(List<Mapping> mappings)
	{
		super();
		this.mappings = mappings;
		fillModel();
	}

	private void fillModel()
	{
		for (String colunName : columnNames)
		{
			addColumn(colunName);
		}
		for (DisplayProperty displayProperty : DisplayProperty.values())
		{
			Object[] rowData = new Object[columnNames.length];
			rowData[0] = displayProperty;
			Mapping mapping = findMapping(displayProperty);
			if (mapping != null)
			{
				rowData[1] = mapping.getMetadataKey();
				rowData[2] = "";
				rowData[3] = "";
				if (!mapping.isAutoRanging())
				{
					rowData[2] = "" + mapping.getMinimum();
					rowData[3] = "" + mapping.getMaximum();
				}
			} else
			{
				rowData[1] = null;
				rowData[2] = "";
				rowData[3] = "";
			}
			addRow(rowData);
		}
	}

	private Mapping findMapping(DisplayProperty displayProperty)
	{
		for (Mapping mapping : mappings)
		{
			if (displayProperty == mapping.getDisplayPropperty())
			{
				return mapping;
			}
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int column)
	{
		return column > 0;
	}
}
