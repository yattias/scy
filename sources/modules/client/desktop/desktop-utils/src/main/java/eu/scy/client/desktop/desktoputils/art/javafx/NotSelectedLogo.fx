/*
 * NotSelectedLogo.fx
 *
 * Created on 10-mei-2010, 15:49:29
 */

package eu.scy.client.desktop.desktoputils.art.javafx;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

/**
 * @author SikkenJ
 */

public class NotSelectedLogo extends CustomNode {

   public var color = Color.GRAY;

	public override function create(): Node {
		return Group {
			content: [
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M16.50,8.51 C16.50,12.92 12.92,16.50 8.51,16.50 C4.09,16.50 0.51,12.92 0.51,8.51 C0.51,4.10 4.09,0.52 8.51,0.52 C12.92,0.52 16.50,4.10 16.50,8.51 Z "
				},
				SVGPath {
					fill: bind color
					stroke: null
					content: "M16.37,7.17 C15.73,3.39 12.45,0.51 8.49,0.51 C6.32,0.51 4.35,1.38 2.91,2.79 L11.89,7.16 Z M1.41,4.80 C0.83,5.91 0.50,7.16 0.50,8.50 C0.50,9.81 0.82,11.04 1.38,12.13 L8.76,8.43 Z M11.86,9.68 L2.89,14.19 C4.33,15.61 6.31,16.49 8.49,16.49 C12.51,16.49 15.82,13.53 16.39,9.68 Z "
				}
         ]
		};
	}
}
