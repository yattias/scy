/*
 * ContactTooltip.fx
 *
 * Created on 11.02.2010, 16:24:02
 */
package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * @author svenmaster
 */
public class ContactTooltip extends CustomNode {

    public var contact:Contact;

    def boldFont:Font = Font.font("", FontWeight.BOLD, FontPosture.REGULAR, 16);

    def normalFont:Font = Font{};

    def gap: Integer = 5;
    def nameLabel: Text = Text {
                font:boldFont;
                x: 0, y: 0;
                content: bind contact.name;
            };

    def onlineStateLabel: Text = Text{
        content: bind contact.onlineState.toString();
    }

    def dragInfo: Text = Text{
        content: ##"Drag me over a window to start collaboration!"
    }

    def infoBox:VBox = VBox{
        spacing:gap;
        content: [nameLabel,onlineStateLabel,dragInfo]
    }

    def rectangle: Rectangle = Rectangle {
                fill: Color.LIGHTBLUE;
                x: bind infoBox.boundsInLocal.minX - gap;
                y: bind infoBox.boundsInLocal.minY - gap;
                width: bind infoBox.boundsInLocal.width + 2 * gap;
                height: bind infoBox.boundsInLocal.height + 2 * gap;
            };

    override protected function create(): Node {
        infoBox.layout();
        def group = Group {
                    content: [rectangle, infoBox]
                }
        return group;
    }

}

