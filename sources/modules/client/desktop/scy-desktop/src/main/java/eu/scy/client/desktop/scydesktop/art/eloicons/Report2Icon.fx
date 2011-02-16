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
public class Report2Icon extends AbstractEloIcon {

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: Color.rgb(0xd3,0xe6,0xf6)
					stroke: null
					content: "M231.72,38.64 C231.54,37.98 231.01,37.45 230.64,36.70 C227.88,31.00 222.00,30.82 216.07,29.96 C210.38,29.14 202.00,28.52 196.84,31.23 C191.79,33.87 192.91,39.04 192.97,43.51 C193.05,49.82 191.12,56.72 192.28,62.97 C193.19,67.84 199.14,68.51 203.48,68.43 C209.47,68.30 215.53,69.75 221.49,69.27 C226.50,68.86 229.42,66.29 230.04,61.81 C230.40,59.17 229.28,56.17 229.27,53.49 C229.26,49.65 230.29,46.07 231.87,42.54 C232.54,41.03 232.38,39.70 231.72,38.64 Z M212.12,58.87 C210.20,58.73 208.28,58.66 206.36,58.60 C206.10,58.59 204.40,58.52 203.14,58.58 C203.37,52.71 203.95,46.85 203.70,40.99 C203.61,39.04 201.70,39.51 205.35,39.39 C207.26,39.33 209.20,39.43 211.11,39.50 C214.55,39.64 218.01,39.81 220.67,41.61 C218.57,47.40 218.18,53.29 218.91,59.40 C218.86,59.40 218.83,59.40 218.78,59.39 C216.56,59.26 214.34,59.03 212.12,58.87 Z "
				},
				Rectangle {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.32
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					x: 199.5
					y: 33.0
					width: 25.6
					height: 32.56
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 3.97
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 202.3
					startY: 37.12
					endX: 212.3
					endY: 37.12
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.65
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 202.3
					startY: 42.26
					endX: 222.48
					endY: 42.26
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 2.65
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 202.3
					startY: 47.11
					endX: 222.48
					endY: 47.11
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.65
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 202.3
					startY: 51.95
					endX: 222.48
					endY: 51.95
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 2.65
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 202.3
					startY: 56.8
					endX: 222.48
					endY: 56.8
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.65
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 202.3
					startY: 61.65
					endX: 222.48
					endY: 61.65
				},
				Rectangle {
					fill: Color.rgb(0xd3,0xe6,0xf6)
					stroke: null
					x: 220.71
					y: 32.0
					width: 5.66
					height: 5.45
				},
				Polygon {
					points: [224.74,37.93,220.22,37.93,220.22,33.42]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.72
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.7
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 220.48
					startY: 32.73
					endX: 225.36
					endY: 37.68
},
]
}
}
}
