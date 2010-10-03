package eu.scy.scyplanner.components.text;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 25.sep.2009
 * Time: 09:54:56
 */
public class SCYPlannerTextArea extends JTextArea {

    /**
     * Creates a new instance of TextArea
     */
    public SCYPlannerTextArea() {
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    public SCYPlannerTextArea(String text) {
        this();
        setText(text);
    }
}
