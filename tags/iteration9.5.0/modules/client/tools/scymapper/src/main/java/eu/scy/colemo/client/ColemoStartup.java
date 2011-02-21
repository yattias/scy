package eu.scy.colemo.client;

import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2009
 * Time: 06:38:01
 */
public class ColemoStartup {

    public static void main(String [] args) {
        System.out.println("Starting colemo!");

        //PropertyConfigurator.configure("resources/log4j.properties");

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,700);
        frame.setVisible(true);

        frame.setContentPane(new ColemoPanel());

    }


}