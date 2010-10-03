package eu.scy.scyplanner.components.utilities;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 19.jan.2010
 * Time: 11:43:48
 */
public class LabelComponent {
    private JLabel label = null;
    private JComponent component = null;

    public LabelComponent(String text, JComponent component) {
        if (text == null) {
            text = "";
        }

        label = new JLabel(text);
        this.component = component;
    }

    public JLabel getLabel() {
        return label;
    }

    public JComponent getComponent() {
        return component;
    }
}