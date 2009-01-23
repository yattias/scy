package eu.scy.colemo.client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2009
 * Time: 06:38:01
 * To change this template use File | Settings | File Templates.
 */
public class ColemoStartup {

    public static void main(String [] args) {
        System.out.println("Starting colemo!");

        JFrame frame = new JFrame();
        frame.setSize(500,500);
        frame.setVisible(true);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(BorderLayout.CENTER, new ColemoPanel());

    }


}
