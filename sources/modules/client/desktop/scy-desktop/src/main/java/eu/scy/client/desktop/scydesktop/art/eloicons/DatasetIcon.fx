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
public class DatasetIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
					x: 0.56
					y: 0.5
					width: 15.0
					height: 15.0
				},
				SVGPath {
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
					content: "M15.06,1.00 L15.06,15.00 L1.06,15.00 L1.06,1.00 Z M16.06,0.00 L0.06,0.00 L0.06,16.00 L16.06,16.00 Z "
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 12.04
					y: 12.02
					width: 4.0
					height: 3.97
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 12.04
					y: 6.13
					width: 4.0
					height: 3.98
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 12.04
					y: 0.06
					width: 4.0
					height: 4.02
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 6.06
					y: 0.02
					width: 4.01
					height: 4.06
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 0.0
					y: 0.02
					width: 4.01
					height: 4.06
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 6.06
					y: 6.11
					width: 4.01
					height: 4.01
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 0.0
					y: 6.11
					width: 4.01
					height: 4.01
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 6.06
					y: 12.05
					width: 4.01
					height: 4.01
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 0.0
					y: 12.05
					width: 4.01
					height: 4.01
				},
				SVGPath {
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
					content: "M15.06,1.00 L15.06,15.00 L1.06,15.00 L1.06,1.00 Z M16.06,0.00 L0.06,0.00 L0.06,16.00 L16.06,16.00 Z "
},
]
}
}
}
