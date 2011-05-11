/*
 * SelectedLogo.fx
 *
 * Created on 10-mei-2010, 15:51:30
 */

package eu.scy.client.desktop.desktoputils.art.javafx;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Rectangle;

/**
 * @author SikkenJ
 */

public class SelectedLogo extends CustomNode {

   public var color = Color.GRAY;

	public override function create(): Node {
		return Group {
			content: [
				Rectangle {
					fill: bind color
					stroke: null
					x: 0.53
					y: 0.5
					width: 16.0
					height: 16.0
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M16.45,7.18 C15.81,3.40 12.53,0.52 8.57,0.52 C6.40,0.52 4.43,1.39 2.99,2.80 L11.97,7.17 Z M11.94,9.69 L2.97,14.20 C4.41,15.62 6.39,16.50 8.57,16.50 C12.59,16.50 15.90,13.54 16.47,9.69 Z M1.49,4.81 C0.91,5.92 0.58,7.18 0.58,8.51 C0.58,9.82 0.90,11.05 1.46,12.14 L8.84,8.44 Z "
				}
         ]
		};
	}
}
