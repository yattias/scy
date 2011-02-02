package eu.scy.chartviewer;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class ChartViewerFrame extends JFrame {

    private JTabbedPane tabbedPane;

    public ChartViewerFrame() {
        super();
        setSize(800, 600);
        setTitle("SCY Chart Viewer");
        setVisible(true);
        tabbedPane = new JTabbedPane();
        add(tabbedPane);

    }

    public int addTab(JComponent component) {
        tabbedPane.add(component, tabbedPane.getTabCount());
        return tabbedPane.getTabCount() - 1;
    }
}
