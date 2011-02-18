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
public class DataIcon extends AbstractEloIcon {

public override function clone(): DataIcon {
DataIcon {
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
					content: "M229.13,61.81 C227.92,56.46 229.40,51.76 229.50,46.31 C229.56,42.71 231.03,36.58 228.69,33.44 C226.27,30.19 222.73,30.70 219.11,30.47 C212.19,30.03 205.31,29.21 198.38,28.87 C195.82,28.75 194.28,30.47 193.89,32.40 C192.95,33.01 192.27,34.02 192.20,35.50 C191.96,40.40 192.18,45.48 192.36,50.38 C192.54,55.08 193.49,59.24 195.94,63.44 C198.70,68.17 203.29,66.24 207.63,65.94 C210.44,65.74 213.37,66.40 216.19,66.25 C219.24,66.08 222.75,65.83 225.69,66.69 C228.65,67.55 229.88,65.14 229.13,61.81 Z M214.93,58.98 C212.10,59.10 209.29,59.39 206.47,59.56 C205.52,59.62 198.63,58.83 197.00,59.06 C196.27,55.73 195.91,51.08 195.81,48.31 C195.67,44.39 196.38,37.20 196.50,33.25 C200.45,33.47 210.87,33.52 214.81,33.88 C217.82,34.14 219.04,38.71 222.21,38.99 C222.14,40.13 222.25,41.66 222.25,41.98 C222.23,44.58 222.13,47.17 222.03,49.77 C221.92,52.57 222.18,55.61 222.62,58.57 C220.05,58.60 217.47,58.86 214.93,58.98 Z "
				},
				Polyline {
					fill: Color.rgb(0x6a,0xb3,0x30)
					stroke: null
					points: [198.44,50.18]
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.2
					points: [197.23,38.15]
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 1.2
					points: [197.23,38.95,197.23,33.75,223.22,33.75,223.22,38.95]
				},
				Rectangle {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 1.2
					x: 197.27
					y: 39.75
					width: 25.92
					height: 18.8
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.8
					startX: 202.0
					startY: 33.72
					endX: 202.0
					endY: 58.12
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.8
					startX: 217.22
					startY: 50.3
					endX: 217.22
					endY: 57.61
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.8
					startX: 198.2
					startY: 49.95
					endX: 222.16
					endY: 49.95
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.8
					startX: 198.2
					startY: 54.35
					endX: 222.16
					endY: 54.35
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 198.0
					startY: 37.5
					endX: 201.0
					endY: 37.5
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 198.0
					startY: 42.5
					endX: 201.0
					endY: 42.5
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 198.0
					startY: 47.5
					endX: 201.0
					endY: 47.5
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 198.0
					startY: 52.5
					endX: 201.0
					endY: 52.5
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 198.0
					startY: 56.5
					endX: 201.0
					endY: 56.5
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 203.0
					startY: 37.5
					endX: 216.0
					endY: 37.5
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 203.0
					startY: 56.5
					endX: 216.0
					endY: 56.5
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 203.0
					startY: 42.5
					endX: 223.0
					endY: 42.5
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 203.0
					startY: 44.5
					endX: 223.0
					endY: 44.5
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 203.0
					startY: 52.5
					endX: 215.0
					endY: 52.5
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 203.0
					startY: 47.5
					endX: 222.0
					endY: 47.5
				},
				Polyline {
					fill: Color.rgb(0x6a,0xb3,0x30)
					stroke: null
					points: [124.51,78.95]
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.13
					content: "M223.64,53.05 C225.04,57.00 226.30,60.75 226.30,61.52 C226.30,63.31 225.27,63.98 223.60,63.98 C221.88,63.98 213.16,63.98 212.04,63.93 C211.03,63.89 209.19,63.15 209.19,61.56 C209.19,59.78 210.69,55.21 211.44,53.04 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.0
					content: "M223.37,52.11 C221.80,47.46 220.06,42.52 219.91,41.74 Q219.64,40.30 219.91,37.00 L214.99,37.00 Q215.37,40.74 215.13,41.88 Q214.86,43.13 212.07,50.93 Q211.92,51.39 211.68,52.10 Z "
},
]
}
}
}
