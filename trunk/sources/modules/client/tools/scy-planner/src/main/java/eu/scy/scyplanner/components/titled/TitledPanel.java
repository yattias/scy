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
public class TitledPanel extends JPanel {
    public TitledPanel(String title, Border border) {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(title));
        setBorder(border);
    }
}