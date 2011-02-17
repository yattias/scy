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
public class PresentationIcon extends AbstractEloIcon {

public function clone(): PresentationIcon {
PresentationIcon {
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
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					content: "M231.42,38.58 C230.42,36.58 228.68,35.19 226.12,34.40 C224.58,33.93 223.16,33.67 221.54,33.40 C220.35,33.20 219.27,32.12 218.18,31.50 C212.62,28.30 206.22,29.00 200.37,30.86 C193.77,32.95 193.44,37.56 193.19,42.68 C193.03,45.75 193.64,48.82 193.77,51.88 C193.90,54.78 192.50,57.52 192.48,60.31 C192.42,66.09 197.90,67.06 204.08,67.68 C208.86,68.16 219.22,70.66 223.01,67.37 C225.07,65.59 225.00,63.39 225.72,61.14 C226.67,58.19 226.74,55.34 226.39,52.32 C226.27,51.25 225.76,50.46 225.03,49.91 C229.07,47.41 233.49,42.69 231.42,38.58 Z M204.99,59.42 C204.47,59.37 203.95,59.31 203.43,59.26 C203.41,59.25 203.40,59.25 203.38,59.25 C204.57,53.96 203.61,48.80 203.56,43.40 C203.52,42.45 203.64,41.51 203.90,40.58 C204.00,40.15 204.10,39.72 204.20,39.29 C204.30,38.94 204.37,38.67 204.41,38.45 C207.02,37.52 209.49,37.10 212.27,38.33 C214.81,39.45 215.82,41.10 219.04,41.48 C219.57,41.54 220.10,41.60 220.63,41.66 C220.66,41.67 220.67,41.68 220.70,41.69 C219.87,42.73 218.65,43.43 217.38,44.11 C214.10,45.89 215.12,48.83 217.64,50.32 C216.61,51.13 215.97,52.28 216.13,53.72 C216.39,55.97 215.95,58.16 215.39,60.34 C213.68,60.19 211.97,60.03 210.26,59.89 C208.51,59.74 206.75,59.59 204.99,59.42 Z "
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.12
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [219.47,51.56,219.47,60.32,195.55,60.32,195.55,35.15,219.47,35.15,219.47,45.78]
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 1.0
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [219.91,52.05]
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 1.0
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [219.91,42.33]
				},
				Polygon {
					points: [221.84,41.90,222.08,42.41,222.44,42.90,222.79,43.24,223.18,43.54,223.67,43.73,224.21,43.88,224.72,43.88,225.26,43.78,225.70,43.58,226.16,43.29,226.60,42.95,226.90,42.56,227.14,42.05,227.24,41.51,227.34,40.97,227.29,40.43,227.14,39.94,226.90,39.43,226.60,38.99,226.26,38.59,225.80,38.30,225.31,38.09,224.77,38.05,224.28,37.99,223.77,38.09,223.28,38.30,222.84,38.59,222.44,38.94,222.15,39.38,221.89,39.87,221.79,40.37,221.69,40.92,221.74,41.41]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
				},
				Rectangle {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					x: 198.18
					y: 38.7
					width: 3.48
					height: 16.93
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					x: 203.39
					y: 41.21
					width: 2.9
					height: 14.42
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					x: 207.8
					y: 46.12
					width: 2.66
					height: 9.51
				},
				Rectangle {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
					x: 212.25
					y: 41.1
					width: 2.66
					height: 14.53
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [214.00,66.50]
				},
				Polygon {
					points: [229.28,63.86,227.70,55.32,229.28,53.73,229.28,49.45,227.04,44.48,222.15,44.81,218.83,46.25,217.31,46.91,213.99,45.71,212.99,47.15,216.55,50.27,218.83,50.12,221.15,49.87,220.33,66.83,223.41,66.83,224.38,58.63,227.24,66.83,229.28,66.83]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
				},
				Polygon {
					points: [212.38,44.03,210.12,42.34,209.61,42.99,210.71,43.83,210.40,44.81,213.08,46.40,213.84,45.12,212.99,42.99]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 0.44
},
]
}
}
}
