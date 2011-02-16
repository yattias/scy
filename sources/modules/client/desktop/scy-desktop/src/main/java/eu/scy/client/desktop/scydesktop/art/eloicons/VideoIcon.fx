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
public class VideoIcon extends AbstractEloIcon {

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: null
					content: "M227.29,37.84 C224.42,31.08 220.47,29.62 215.14,30.29 C206.46,28.45 194.30,26.40 192.42,39.97 C190.28,55.44 199.56,63.66 209.28,67.30 C216.83,70.13 227.99,71.30 231.38,59.23 C233.33,52.30 229.75,43.64 227.29,37.84 Z M223.84,59.10 C219.35,62.89 212.84,59.92 208.29,58.00 C202.46,55.54 196.79,49.75 198.87,40.53 C199.92,35.85 207.21,36.88 212.94,38.22 C213.52,38.75 214.25,39.04 215.13,38.89 C215.23,38.88 215.33,38.86 215.44,38.84 C217.07,39.26 218.42,39.63 219.21,39.76 C219.77,39.85 220.25,39.77 220.67,39.57 C221.89,40.85 222.90,44.88 223.50,46.56 C224.75,50.06 227.13,56.33 223.84,59.10 Z "
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 197.0
					y: 47.0
					width: 22.0
					height: 11.0
				},
				Polygon {
					points: [221.74,48.79,226.42,45.41,229.03,45.41,229.03,58.45,225.77,58.45,221.66,54.50]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M208.38,37.51 C208.38,39.99 206.37,42.00 203.88,42.00 C201.40,42.00 199.39,39.99 199.39,37.51 C199.39,35.02 201.40,33.01 203.88,33.01 C206.37,33.01 208.38,35.02 208.38,37.51 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M219.44,37.51 C219.44,39.99 217.42,42.00 214.94,42.00 C212.46,42.00 210.44,39.99 210.44,37.51 C210.44,35.02 212.46,33.01 214.94,33.01 C217.42,33.01 219.44,35.02 219.44,37.51 Z "
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.8
					strokeLineCap: StrokeLineCap.BUTT
					startX: 196.98
					startY: 56.16
					endX: 196.98
					endY: 47.38
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.8
					strokeLineCap: StrokeLineCap.BUTT
					startX: 219.5
					startY: 56.16
					endX: 219.5
					endY: 47.38
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 6.73
					strokeLineCap: StrokeLineCap.BUTT
					content: "M218.48,56.16 C218.48,56.87 218.02,57.45 217.46,57.45 L198.90,57.45 C198.34,57.45 197.89,56.87 197.89,56.16 "
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 6.74
					strokeLineCap: StrokeLineCap.BUTT
					content: "M197.90,47.38 C197.90,46.67 198.35,46.09 198.92,46.09 L217.54,46.09 C218.10,46.09 218.55,46.67 218.55,47.38 "
},
]
}
}
}
