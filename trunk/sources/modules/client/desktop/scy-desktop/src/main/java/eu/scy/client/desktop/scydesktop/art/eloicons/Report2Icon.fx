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

public override function clone(): Report2Icon {
Report2Icon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M231.72,38.64 C231.54,37.98 231.01,37.45 230.64,36.70 C227.88,31.00 222.00,30.82 216.07,29.96 C210.38,29.14 202.00,28.52 196.84,31.23 C191.79,33.87 193.01,39.35 193.06,43.81 C193.14,50.12 193.52,56.25 194.69,62.50 C195.60,67.37 199.14,68.51 203.48,68.43 C209.47,68.30 215.53,69.75 221.49,69.27 C226.50,68.86 229.42,66.29 230.04,61.81 C230.40,59.17 229.28,56.17 229.27,53.49 C229.26,49.65 230.29,46.07 231.87,42.54 C232.54,41.03 232.38,39.70 231.72,38.64 Z M214.88,65.88 C212.96,65.73 209.36,66.00 207.44,65.94 C207.17,65.93 199.38,66.13 198.13,66.19 C198.35,60.33 197.57,44.98 197.31,39.13 C197.23,37.18 197.72,32.18 201.38,32.06 C203.29,32.00 213.59,32.93 215.50,33.00 C218.94,33.13 220.90,34.02 223.56,35.81 C230.31,40.88 225.71,60.52 226.44,66.63 C226.39,66.62 218.67,66.07 218.63,66.06 C216.40,65.93 217.10,66.04 214.88,65.88 Z "
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
					fill: bind windowColorScheme.mainColorLight
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
