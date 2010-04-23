package eu.scy.scyplanner.components.titled;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 12:43:35
 */
public class TitledList extends JPanel {
    private JList list = null;

    public TitledList(String title, ListModel model, boolean toggleListModel) {
        this(title, model);
        if (toggleListModel) {
            list.setSelectionModel(new ToggleSelectionModel());
        }
    }

    public TitledList(String title, ListModel model) {
        setLayout(new BorderLayout());
        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setBorder(new TitledBorder(title));
        list = new JList(model);
        list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        innerPanel.add(BorderLayout.CENTER, list);

        add(BorderLayout.CENTER, innerPanel);
    }

    public TitledList(String title, ListModel model, boolean toogleListModel, Border border) {
        this(title, model, toogleListModel);
        setBorder(border);

    }

    public TitledList(String title, ListModel model, Border border) {
        this(title, model, false, border);
    }

    public JList getList() {
        return list;
    }

    class ToggleSelectionModel extends DefaultListSelectionModel {
        boolean gestureStarted = false;

        public void setSelectionInterval(int index0, int index1) {
            if (isSelectedIndex(index0) && !gestureStarted) {
                super.removeSelectionInterval(index0, index1);
            } else {
                super.setSelectionInterval(index0, index1);
            }
            gestureStarted = true;
        }

        public void setValueIsAdjusting(boolean isAdjusting) {
            if (isAdjusting == false) {
                gestureStarted = false;
            }
        }
    }
}
