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
public class ReflectionIcon extends AbstractEloIcon {

public override function clone(): ReflectionIcon {
ReflectionIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				Polygon {
					points: [229.56,45.00,194.53,45.00,223.91,31.41]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.96
					strokeLineCap: StrokeLineCap.BUTT
					startX: 193.11
					startY: 46.94
					endX: 230.89
					endY: 47.21
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M227.29,48.44 C226.71,48.44 226.21,48.62 225.79,48.90 C225.52,48.80 225.21,48.74 224.86,48.75 C220.46,48.86 216.21,49.50 211.82,49.12 C207.23,48.73 202.61,48.75 198.00,48.75 C194.37,48.75 193.84,55.09 197.51,55.09 C198.77,55.09 200.03,55.09 201.29,55.11 C200.80,56.87 201.50,59.04 203.61,59.16 C204.59,59.22 205.58,59.38 206.57,59.55 C206.50,60.91 207.16,62.45 208.40,62.76 C211.09,63.44 213.27,65.11 215.69,66.50 C217.05,67.28 218.65,67.25 219.61,65.69 C220.14,64.83 220.24,63.50 219.83,62.47 C221.19,62.43 222.45,62.12 223.50,61.02 C224.22,60.27 224.59,59.24 224.56,58.22 C225.46,57.36 225.93,55.98 225.51,54.81 C225.66,54.74 225.80,54.65 225.93,54.55 C226.20,54.64 226.48,54.71 226.80,54.71 C230.40,54.71 230.92,48.44 227.29,48.44 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: Color.WHITE
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.35,56.17 C212.10,55.88 212.86,55.69 213.66,55.61 C214.73,55.45 215.93,55.56 217.03,55.50 C218.19,55.43 219.59,55.04 220.43,54.20 C221.48,53.16 218.24,52.57 217.94,52.51 C213.71,51.58 209.58,52.07 205.30,51.95 C205.12,51.95 205.09,52.23 205.27,52.23 C209.90,52.36 214.36,51.87 218.90,53.02 C220.20,53.35 220.46,54.13 219.06,54.73 C217.88,55.24 216.60,55.25 215.34,55.28 C213.25,55.33 211.59,55.66 209.73,56.63 C209.64,56.67 209.62,56.77 209.68,56.85 C210.33,57.73 211.59,57.89 212.61,58.07 C213.59,58.24 214.63,58.56 215.63,58.57 C216.40,58.45 216.40,58.81 215.61,59.65 C214.98,59.84 214.25,59.85 213.60,59.95 C213.52,59.97 212.23,60.37 212.48,60.82 C212.98,61.72 216.16,62.27 215.34,63.35 C215.22,63.49 215.46,63.65 215.57,63.51 C216.08,62.85 216.04,62.31 215.17,61.98 C214.21,61.61 211.57,60.56 214.29,60.13 C215.32,59.97 216.55,59.94 217.32,59.15 C217.36,59.10 217.39,59.04 217.35,58.98 C216.91,58.25 216.54,58.32 215.65,58.29 C214.11,58.23 212.42,57.80 210.93,57.38 C210.32,56.66 210.46,56.26 211.35,56.17 Z "
},
]
}
}
}
