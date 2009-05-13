package eu.scy.tools.drawing.impl;

import javax.swing.JApplet;
import javax.swing.JLabel;

import eu.scy.tools.drawing.client.TestApplet;

public class TestAppletImpl extends JApplet implements TestApplet{

    private static final long serialVersionUID = -6067632718747913094L;

    public void init() {
        add(new JLabel("I'm an Applet. Hooray!"));
    }
}
