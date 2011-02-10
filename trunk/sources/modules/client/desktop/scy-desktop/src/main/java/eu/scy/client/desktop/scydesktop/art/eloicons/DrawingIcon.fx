package eu.scy.client.desktop.scydesktop.art.eloicons;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
/**
 * @author lars
 */
public class DrawingIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M1.59,3.81 C4.39,4.37 8.58,16.46 12.09,10.69 C12.69,9.73 12.77,8.16 12.77,6.95 "
				},
				SVGPath {
					fill: Color.rgb(0x0,0x42,0xf1)
					stroke: null
					content: "M0.63,4.32 C2.04,4.73 3.06,6.68 3.84,7.79 C4.95,9.37 6.05,11.15 7.68,12.27 C8.88,13.08 10.30,12.84 11.55,12.27 C13.66,11.29 13.92,8.65 13.94,6.63 C13.95,6.09 11.60,6.47 11.59,7.26 C11.57,8.61 11.53,9.95 10.77,11.10 C10.60,11.35 10.40,11.56 10.18,11.75 C10.58,11.41 10.52,11.77 9.88,11.41 C8.81,10.80 8.01,9.65 7.29,8.69 C6.05,7.04 4.69,3.93 2.54,3.30 C1.92,3.13 -0.19,4.08 0.63,4.32 Z "
},
]
}
}
}
