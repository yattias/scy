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
public class Home2Icon extends AbstractEloIcon {

public function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 202.0
					y: 49.0
					width: 8.0
					height: 8.0
				},
				SVGPath {
					fill: Color.rgb(0xd3,0xe6,0xf6)
					stroke: null
					content: "M230.11,45.38 C230.46,43.65 229.87,41.78 227.82,40.75 C225.38,39.53 222.83,38.55 220.39,37.32 C218.08,36.15 216.66,33.13 214.63,31.49 C210.79,28.38 206.21,29.84 202.79,32.76 C199.27,35.76 194.87,37.87 192.61,42.06 C189.95,47.00 193.38,52.83 192.83,57.95 C191.49,70.46 211.32,67.59 218.92,68.85 C223.34,69.59 228.31,68.69 229.79,63.89 C231.26,59.15 229.85,53.39 230.65,48.43 C230.85,47.23 230.61,46.21 230.11,45.38 Z M221.23,59.23 C221.49,62.07 221.30,60.14 219.01,59.75 C215.10,59.10 211.12,59.14 207.18,58.85 C205.58,58.73 204.00,58.57 202.41,58.34 C201.26,58.18 202.22,56.55 202.23,55.02 C202.26,52.36 199.04,47.03 201.50,44.96 C203.75,43.07 206.26,41.70 208.45,39.67 C209.84,38.38 209.46,38.71 210.59,40.08 C211.68,41.39 212.71,42.68 213.97,43.83 C216.05,45.73 218.99,46.74 221.67,47.85 C221.27,50.64 221.06,53.44 221.01,56.27 C220.99,57.25 221.14,58.25 221.23,59.23 Z "
				},
				Polyline {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.44
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 1.0
					strokeDashArray: [
					1.25,
					]
					points: [201.70,45.53]
				},
				Polygon {
					points: [227.18,64.97,221.66,64.97,221.66,51.68,214.08,51.68,214.08,64.97,199.19,64.97,199.19,44.61,213.18,32.30,227.18,44.61]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 218.64
					y: 32.99
					width: 4.42
					height: 9.17
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 202.45
					y: 50.34
					width: 7.64
					height: 6.72
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 213.95
					y: 51.25
					width: 7.64
					height: 13.72
				},
				Polygon {
					points: [196.31,47.14,213.18,32.30,230.06,47.14]
					fill: bind windowColorScheme.thirdColor
					stroke: Color.WHITE
					strokeWidth: 0.63
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
				},
				Polyline {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.44
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 1.0
					strokeDashArray: [
					1.25,
					]
					points: [201.70,45.53,213.50,35.15,225.30,45.53]
},
]
}
}
}
