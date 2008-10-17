package eu.scy.tools.drawing.impl;

import javax.swing.JApplet;
import javax.swing.JLabel;

import eu.scy.tools.drawing.client.TestApplet;


public class TestAppletImpl extends JApplet implements TestApplet{
    public void init() {
        add(new JLabel("Foo"));
    }
}
