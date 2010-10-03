package eu.scy.scyplanner;

import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.scyplanner.components.application.SCYPlannerFrame;
import eu.scy.scyplanner.components.application.SCYPlannerSplashWindow;

import javax.swing.*;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 10:57:50
 */
public class SCYPlannerMain {

    public static void main(String[] args) {
        //SCYPlannerSplashWindow splashWindow = new SCYPlannerSplashWindow();
        //splashWindow.setVisible(true);

        //updateUIManagerWithSensibleValues();

        SCYPlannerFrame frame = new SCYPlannerFrame();
        SCYPlannerApplicationManager.getApplicationManager().setScyPlannerFrame(frame);
        frame.setVisible(true);

        //splashWindow.setVisible(false);
        //splashWindow.dispose();
    }

    private static void updateUIManagerWithSensibleValues() {
        UIManager.put("Label.font", "Arial,0,12");
        UIManager.put("Label.foreground", "0");

        //buttons
        UIManager.put("Button.font", "Arial,0,12");
        UIManager.put("Button.foreground", "0");

        UIManager.put("ToggleButton.font", "Arial,0,12");
        UIManager.put("ToggleButton.foreground", "0");

        //tabbed pane
        UIManager.put("TabbedPane.font", "Arial,0,12");
        UIManager.put("TabbedPane.foreground", "0");

        //MenuItems
        UIManager.put("MenuItem.font", "Arial,0,12");
        UIManager.put("MenuItem.foreground", "0");
        UIManager.put("Menu.font", "Arial,0,12");
        UIManager.put("Menu.foreground", "0");
        UIManager.put("MenuBar.font", "Arial,0,12");
        UIManager.put("MenuBar.foreground", "0");
        UIManager.put("CheckBoxMenuItem.font", "Arial,0,12");
        UIManager.put("CheckBoxMenuItem.foreground", "0");
        UIManager.put("RadioButtonMenuItem.font", "Arial,0,12");
        UIManager.put("RadioButtonMenuItem.foreground", "0");
        UIManager.put("PopupMenu.font", "Arial,0,12");
        UIManager.put("PopupMenu.foreground", "0");

        //CheckBoxes
        UIManager.put("CheckBox.font", "Arial,0,12");
        UIManager.put("CheckBox.foreground", "0");

        //RadioButtons
        UIManager.put("RadioButton.font", "Arial,0,12");
        UIManager.put("RadioButton.foregrround", "0");

        //Borders
        UIManager.put("TitledBorder.font", "Arial,0,12");
        UIManager.put("TitledBorder.foreground", "0");

        //comboBoxes
        UIManager.put("ComboBox.font", "Arial,0,12");
        UIManager.put("ComboBox.foreground", "0");

        //Lists
        UIManager.put("List.font", "Arial,0,12");
        UIManager.put("List.foreground", "0");

        //Tables
        UIManager.put("Table.font", "Arial,0,12");
        UIManager.put("Table.foreground", "0");

        //text fields
        UIManager.put("PasswordField.font", "Arial,0,12");
        UIManager.put("PasswordField.foreground", "0");
        UIManager.put("TextField.font", "Arial,0,12");
        UIManager.put("TextField.foreground", "0");
        UIManager.put("TextArea.font", "Arial,0,12");
        UIManager.put("TextArea.foreground", "0");
        UIManager.put("TextPane.font", "Arial,0,12");
        UIManager.put("TextPane.foreground", "0");
        UIManager.put("EditorPane.font", "Arial,0,12");
        UIManager.put("EditorPane.foreground", "0");

    }
}
