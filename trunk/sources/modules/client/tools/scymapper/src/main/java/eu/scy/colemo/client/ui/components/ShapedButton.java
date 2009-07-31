package eu.scy.colemo.client.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 18.jun.2009
 * Time: 16:33:18
 * To change this template use File | Settings | File Templates.
 */
public class ShapedButton extends JButton {
    ShapedComponent shapedComponent;

    public ShapedButton(String text, Shape shape) {
        super(text);
        shapedComponent  = new ShapedComponent(shape);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        shapedComponent.paint(g);
    }
}
