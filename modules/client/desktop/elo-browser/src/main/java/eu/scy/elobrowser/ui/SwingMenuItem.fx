/*
 * SwingMenuItem.fx
 *
 * Created on 23-dec-2008, 18:54:31
 */

package eu.scy.elobrowser.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javafx.ext.swing.SwingComponent;
import javax.swing.JMenuItem;

/**
 * @author sikken
 */

 // place your code here

public class SwingMenuItem extends SwingComponent{

	public-read var menuItem: JMenuItem;

	public var label: String on replace{
		menuItem.setText(label);
    }


	public var action: function();

	public override function createJComponent(){
		menuItem = new JMenuItem();
		menuItem.addActionListener(
		ActionListener{
			public override function actionPerformed(e:ActionEvent){
				//println("menus: {e.getActionCommand()}");
				if (action != null){
					 action()
				} else {
					println("warning action undefined for SwingMenuItem {e.getActionCommand()}");
				};
			}
		}
		);
		return menuItem;
	}
}
