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
public class DatasetIcon extends AbstractEloIcon {

public function clone(): DatasetIcon {
DatasetIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M230.17,64.95 C229.18,59.02 229.03,52.72 229.22,46.73 C229.35,42.54 230.08,37.74 229.22,33.60 C228.99,32.50 227.96,32.04 226.94,32.38 C222.66,33.80 216.38,32.76 211.92,32.37 C206.66,31.91 201.63,30.76 196.32,30.82 C194.37,30.84 193.92,33.68 195.60,34.02 C196.01,40.73 194.97,47.92 194.59,54.61 C194.40,58.03 192.08,63.16 195.08,65.80 C196.18,66.78 198.76,66.34 200.04,66.35 C203.12,66.37 206.25,66.93 209.33,67.16 C215.74,67.65 222.41,67.23 228.82,66.81 C229.70,66.75 230.30,65.76 230.17,64.95 Z M197.13,63.05 C197.44,63.06 198.69,40.60 198.78,34.12 C203.78,34.38 208.67,35.35 213.67,35.79 C217.55,36.13 222.24,36.62 226.27,35.85 C226.83,40.57 225.95,45.83 225.89,50.47 C225.83,54.82 226.09,59.33 226.70,63.68 C216.75,64.18 207.07,63.41 197.13,63.05 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 199.15
					y: 35.21
					width: 27.57
					height: 27.57
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M225.81,36.13 L225.81,61.86 L200.07,61.86 L200.07,36.13 Z M227.65,34.29 L198.23,34.29 L198.23,63.70 L227.65,63.70 Z "
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 220.27
					y: 56.38
					width: 7.35
					height: 7.31
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 220.27
					y: 45.55
					width: 7.35
					height: 7.31
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 220.27
					y: 34.4
					width: 7.35
					height: 7.4
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 209.28
					y: 34.32
					width: 7.37
					height: 7.46
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 198.13
					y: 34.32
					width: 7.37
					height: 7.46
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 209.28
					y: 45.52
					width: 7.37
					height: 7.37
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 198.13
					y: 45.52
					width: 7.37
					height: 7.37
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 209.28
					y: 56.44
					width: 7.37
					height: 7.37
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 198.13
					y: 56.44
					width: 7.37
					height: 7.37
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M225.81,36.13 L225.81,61.86 L200.07,61.86 L200.07,36.13 Z M227.64,34.29 L198.23,34.29 L198.23,63.70 L227.64,63.70 Z "
},
]
}
}
}
