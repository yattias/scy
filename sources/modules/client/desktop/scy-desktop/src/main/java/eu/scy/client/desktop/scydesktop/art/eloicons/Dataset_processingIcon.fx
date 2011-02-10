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
public class Dataset_processingIcon extends CustomNode {

public var windowColorScheme: WindowColorScheme;

public function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 0.05
					y: 0.02
					width: 16.0
					height: 16.0
				},
				SVGPath {
					fill: null
					stroke: null
					content: "M15.05,1.02 L15.05,15.02 L1.05,15.02 L1.05,1.02 Z M16.05,0.02 L0.05,0.02 L0.05,16.02 L16.05,16.02 Z "
				},
				Rectangle {
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
					x: 12.04
					y: 12.03
					width: 4.01
					height: 4.01
				},
				Rectangle {
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
					x: 12.04
					y: 6.09
					width: 4.01
					height: 4.01
				},
				Rectangle {
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
					x: 12.04
					y: 0.03
					width: 4.01
					height: 4.01
				},
				Rectangle {
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
					x: 6.07
					y: 0.05
					width: 4.01
					height: 4.01
				},
				Rectangle {
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
					x: 0.01
					y: 0.05
					width: 4.01
					height: 4.01
				},
				SVGPath {
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
					content: "M9.08,7.09 L9.08,9.10 L7.07,9.10 L7.07,7.09 Z M10.08,6.09 L6.07,6.09 L6.07,10.10 L10.08,10.10 Z "
				},
				SVGPath {
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
					content: "M3.02,7.09 L3.02,9.10 L1.01,9.10 L1.01,7.09 Z M4.02,6.09 L0.01,6.09 L0.01,10.10 L4.02,10.10 Z "
				},
				SVGPath {
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
					content: "M9.08,13.03 L9.08,15.04 L7.07,15.04 L7.07,13.03 Z M10.08,12.03 L6.07,12.03 L6.07,16.04 L10.08,16.04 Z "
				},
				SVGPath {
					fill: Color.rgb(0x8d,0xb8,0x0)
					stroke: null
					content: "M3.02,13.03 L3.02,15.04 L1.01,15.04 L1.01,13.03 Z M4.02,12.03 L0.01,12.03 L0.01,16.04 L4.02,16.04 Z "
},
]
}
}
}
