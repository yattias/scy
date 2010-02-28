/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.model.mapping.editor;

import java.awt.Component;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import roolo.elo.api.IMetadataKey;

/**
 *
 * @author sikkenj
 */
public class ComboKeyTableCellRenderer extends JComboBox implements TableCellEditor
{

	private transient List<CellEditorListener> cellEditorListeners = new CopyOnWriteArrayList<CellEditorListener>();
	private transient IMetadataKey original;

	public void setKeys(List<IMetadataKey> keys)
	{
		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(keys.toArray());
		setModel(comboBoxModel);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		IMetadataKey key = (IMetadataKey) value;
		setSelectedItem(key);
		return this;
	}

	@Override
	public void addCellEditorListener(CellEditorListener l)
	{
		cellEditorListeners.add(l);
	}

	@Override
	public void cancelCellEditing()
	{
		fireEditingCancelled();
	}

	@Override
	public Object getCellEditorValue()
	{
		return getSelectedItem();
	}

	@Override
	public boolean isCellEditable(EventObject anEvent)
	{
		return true;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l)
	{
		cellEditorListeners.remove(l);
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent)
	{
		return true;
	}

	@Override
	public boolean stopCellEditing()
	{
		fireEditingStopped();
		return true;
	}

	private void fireEditingCancelled()
	{
		setSelectedItem(original);
		ChangeEvent changeEvent = new ChangeEvent(this);
		for (CellEditorListener cellEditorListener : cellEditorListeners)
		{
			cellEditorListener.editingCanceled(changeEvent);
		}
	}

	private void fireEditingStopped()
	{
		ChangeEvent changeEvent = new ChangeEvent(this);
		for (CellEditorListener cellEditorListener : cellEditorListeners)
		{
			cellEditorListener.editingStopped(changeEvent);
		}
	}
}
