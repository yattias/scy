package eu.scy.scyplanner.components.titled;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 12:43:35
 */
public class TitledList extends JPanel {
    private JList list = null;

    public TitledList(String title, ListModel model) {
        setLayout(new BorderLayout());
        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setBorder(new TitledBorder(title));
        list = new JList(model);
        list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        innerPanel.add(BorderLayout.CENTER, list);

        add(BorderLayout.CENTER, innerPanel);
    }

    public TitledList(String title, ListModel model, Border border) {
        this(title, model);
        setBorder(border);
    }

    public JList getList() {
        return list;
    }
}
