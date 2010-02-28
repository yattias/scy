package eu.scy.scyplanner.components.table;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 09.des.2009
 * Time: 13:05:07
 */

public class Table extends JTable {
    private final static Border cellBorder = BorderFactory.createEmptyBorder(0, 5, 0, 0);
    private final MyTableHeaderCellRenderer headerRenderer = new MyTableHeaderCellRenderer();
    private int lastSortIndex = 0;
    private boolean lastSortValue = false;

    public Table() {
        ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        getTableHeader().setReorderingAllowed(false);
        setShowGrid(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setIntercellSpacing(new Dimension(0, 0));

        getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                int index = getColumnModel().getColumnIndexAtX(event.getX());
                if (index == lastSortIndex) {
                    sortAllRowsBy(index, !lastSortValue);
                } else {
                    sortAllRowsBy(index, true);
                }
            }
        });
    }

    public Component prepareRenderer(TableCellRenderer tableCellRenderer, int i, int i1) {
        JComponent component = (JComponent) super.prepareRenderer(tableCellRenderer, i, i1);
        component.setBorder(cellBorder);

        return component;
    }

    public void columnAdded(TableColumnModelEvent event) {
        super.columnAdded(event);
        getColumnModel().getColumn(event.getToIndex()).setHeaderRenderer(headerRenderer);
    }

    private class MyTableHeaderCellRenderer extends DefaultTableCellRenderer {
        private final JLabel label = new JLabel();

        public MyTableHeaderCellRenderer() {
            //frame.setBorder(BorderFactory.createEmptyBorder());
        }

        public Component getTableCellRendererComponent(JTable jTable, Object o, boolean b, boolean b1, int i, int i1) {
            label.setText(o.toString());
            return label;
        }
    }

    public void sortAllRowsBy(int colIndex, boolean ascending) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        Object selectedObjectColumn1Object = null;
        if (getSelectedRow() != -1) {
            selectedObjectColumn1Object = model.getValueAt(getSelectedRow(), 0);
        }
        Collections.sort(model.getDataVector(), new ColumnSorter(colIndex, ascending));
        model.fireTableStructureChanged();

        lastSortIndex = colIndex;
        lastSortValue = ascending;

        if (selectedObjectColumn1Object != null) {
            getSelectionModel().setLeadSelectionIndex(getRowIndexForColumnObject(selectedObjectColumn1Object));
        }
    }

    public int getRowIndexForColumnObject(Object object) {
        Vector v = ((DefaultTableModel) getModel()).getDataVector();
        for (int count = 0; count < v.size(); count++) {
            Vector row = (Vector) v.get(count);
            if (row.indexOf(object) != -1) {
                return count;
            }
        }
        return -1;
    }

    private class ColumnSorter implements Comparator {
        int colIndex;
        boolean ascending;

        ColumnSorter(int colIndex, boolean ascending) {
            this.colIndex = colIndex;
            this.ascending = ascending;
        }

        public int compare(Object a, Object b) {
            Vector v1 = (Vector) a;
            Vector v2 = (Vector) b;
            Object o1 = v1.get(colIndex);
            Object o2 = v2.get(colIndex);

            // Treat empty strains like nulls
            if (o1 instanceof String && ((String) o1).length() == 0) {
                o1 = null;
            }
            if (o2 instanceof String && ((String) o2).length() == 0) {
                o2 = null;
            }

            // Sort nulls so they appear last, regardless
            // of sort order
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return 1;
            } else if (o2 == null) {
                return -1;
            } else if (o1 instanceof Comparable) {
                if (ascending) {
                    return ((Comparable) o1).compareTo(o2);
                } else {
                    return ((Comparable) o2).compareTo(o1);
                }
            } else {
                if (ascending) {
                    return o1.toString().compareTo(o2.toString());
                } else {
                    return o2.toString().compareTo(o1.toString());
                }
            }
        }
    }
}