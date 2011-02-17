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
public class Design_of_artifactIcon extends AbstractEloIcon {

public function clone(): Design_of_artifactIcon {
Design_of_artifactIcon {
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
					content: "M230.36,55.50 C227.37,53.51 225.01,50.94 222.42,48.50 C220.19,46.39 217.36,45.02 214.95,43.11 C212.35,41.05 210.43,38.24 208.55,35.58 C206.81,33.13 203.45,31.73 201.15,29.88 C199.58,28.63 197.93,29.04 196.86,30.14 C194.43,29.46 191.13,31.93 192.25,35.12 C194.24,40.76 194.59,46.77 195.46,52.64 C195.91,55.68 196.24,58.64 196.00,61.72 C195.84,63.90 196.17,65.14 197.46,66.86 C199.85,70.07 207.34,68.76 210.68,68.60 C216.73,68.32 222.84,67.61 228.80,66.55 C231.94,65.99 232.37,62.56 230.88,60.67 C232.18,59.24 232.46,56.89 230.36,55.50 Z M209.46,61.56 C209.06,61.59 205.03,61.93 203.24,61.71 C203.14,53.96 202.52,45.93 200.68,38.33 C202.08,39.39 203.39,40.52 204.45,42.10 C206.19,44.69 208.56,46.83 210.88,48.89 C213.56,51.27 216.74,52.76 219.31,55.30 C220.99,56.96 222.67,58.57 224.53,60.00 C221.44,60.46 218.34,60.80 215.22,61.07 C213.30,61.25 211.38,61.42 209.46,61.56 Z "
				},
				Polygon {
					points: [224.23,61.04,200.35,64.58,196.11,35.10]
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 2.47
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [205.85,49.43,207.27,59.29,215.25,58.11]
					fill: Color.WHITE
					stroke: Color.WHITE
					strokeWidth: 2.3
					strokeLineCap: StrokeLineCap.BUTT
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.81
					strokeLineCap: StrokeLineCap.BUTT
					startX: 214.04
					startY: 53.72
					endX: 220.13
					endY: 53.63
				},
				Polygon {
					points: [228.16,66.24,225.53,63.60,227.39,62.66]
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.53
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [207.17,67.24,208.61,62.29,209.66,62.66]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 3.43
					strokeLineCap: StrokeLineCap.BUTT
					points: [226.06,62.65,216.64,43.56,209.19,62.59]
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.18
					strokeLineCap: StrokeLineCap.ROUND
					startX: 217.3
					startY: 52.31
					endX: 217.36
					endY: 56.32
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.52
					strokeLineCap: StrokeLineCap.ROUND
					startX: 216.72
					startY: 51.68
					endX: 217.86
					endY: 51.67
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.52
					strokeLineCap: StrokeLineCap.ROUND
					startX: 216.74
					startY: 53.0
					endX: 217.88
					endY: 52.99
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.52
					strokeLineCap: StrokeLineCap.ROUND
					startX: 216.76
					startY: 54.33
					endX: 217.9
					endY: 54.31
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.52
					strokeLineCap: StrokeLineCap.ROUND
					startX: 216.78
					startY: 55.65
					endX: 217.92
					endY: 55.63
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeWidth: 0.52
					strokeLineCap: StrokeLineCap.ROUND
					startX: 216.8
					startY: 56.97
					endX: 217.94
					endY: 56.95
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 3.74
					strokeLineCap: StrokeLineCap.ROUND
					startX: 216.42
					startY: 33.94
					endX: 216.42
					endY: 37.06
				},
				Polygon {
					points: [213.52,34.20,200.59,68.17,195.24,66.08,195.56,65.23,208.17,32.11]
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.06
					strokeLineCap: StrokeLineCap.ROUND
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 199.01
					startY: 61.77
					endX: 197.32
					endY: 61.11
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 200.01
					startY: 59.14
					endX: 198.32
					endY: 58.47
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 201.01
					startY: 56.51
					endX: 199.32
					endY: 55.84
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 202.01
					startY: 53.88
					endX: 200.32
					endY: 53.21
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 203.71
					startY: 51.52
					endX: 201.37
					endY: 50.6
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 198.74
					startY: 64.57
					endX: 196.4
					endY: 63.65
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 204.01
					startY: 48.61
					endX: 202.32
					endY: 47.95
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 205.01
					startY: 45.98
					endX: 203.32
					endY: 45.32
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 206.02
					startY: 43.35
					endX: 204.33
					endY: 42.69
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 207.02
					startY: 40.72
					endX: 205.33
					endY: 40.06
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 208.71
					startY: 38.36
					endX: 206.37
					endY: 37.45
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.81
					strokeLineCap: StrokeLineCap.ROUND
					startX: 209.02
					startY: 35.46
					endX: 207.33
					endY: 34.8
},
]
}
}
}
