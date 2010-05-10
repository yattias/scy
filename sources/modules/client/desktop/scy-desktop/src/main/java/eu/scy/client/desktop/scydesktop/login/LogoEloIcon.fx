/*
 * LogoEloIcon.fx
 *
 * Created on 10-mei-2010, 15:46:48
 */

package eu.scy.client.desktop.scydesktop.login;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.paint.Color;

/**
 * @author SikkenJ
 */

public class LogoEloIcon extends EloIcon {

   public var color = Color.GRAY;

    override protected function create () : Node {
        Group{
           content:[
              SelectedLogo{
                 color:bind color
                 visible: bind selected
              }
              NotSelectedLogo{
                 color:bind color
                 visible: bind not selected
              }
           ]
        }
    }
}
