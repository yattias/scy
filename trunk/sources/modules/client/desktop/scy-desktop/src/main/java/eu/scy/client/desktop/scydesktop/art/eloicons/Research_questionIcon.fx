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
public class Research_questionIcon extends AbstractEloIcon {

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M198.79,67.32 C196.85,67.32 195.61,65.40 196.26,63.27 C196.47,62.59 197.13,61.99 197.53,61.37 Q198.26,60.22 198.79,59.23 Q199.44,60.32 200.13,61.37 C200.58,62.04 201.09,62.53 201.31,63.27 C201.95,65.40 201.15,67.32 198.79,67.32 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M229.33,43.80 C229.33,50.66 224.59,56.22 218.75,56.22 C212.90,56.22 208.17,50.66 208.17,43.80 Z "
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M198.68,58.11 L210.35,51.38 C212.29,53.89 215.32,55.50 218.73,55.50 C224.58,55.50 229.31,50.76 229.31,44.92 C229.31,39.08 224.58,34.34 218.73,34.34 C212.89,34.34 208.15,39.08 208.15,44.92 C208.15,45.88 208.28,46.81 208.52,47.69 L196.69,55.25 "
				},
				Polyline {
					fill: Color.rgb(0x6a,0xb3,0x30)
					stroke: null
					points: [197.77,54.29]
				},
				Polygon {
					points: [210.44,50.99,198.94,58.44,197.19,55.94,208.84,48.49]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M219.33,54.81 L211.83,54.81 C211.79,53.76 211.78,53.17 211.78,53.04 C211.78,50.31 212.87,47.81 215.05,45.51 C216.48,44.00 217.39,42.88 217.79,42.15 C218.18,41.41 218.38,40.75 218.38,40.17 C218.38,38.14 217.03,37.13 214.33,37.13 C212.18,37.13 210.26,37.67 208.55,38.73 L206.61,33.08 C209.37,31.64 212.57,30.92 216.22,30.92 C219.60,30.92 222.26,31.66 224.17,33.14 C226.09,34.62 227.05,36.55 227.05,38.93 C227.05,40.13 226.71,41.39 226.03,42.72 C225.36,44.05 224.09,45.65 222.22,47.51 C220.29,49.38 219.33,51.52 219.33,53.94 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.25
					strokeLineCap: StrokeLineCap.BUTT
					content: "M219.41,54.88 L211.91,54.88 C211.88,53.84 211.86,53.25 211.86,53.11 C211.86,50.39 212.95,47.88 215.13,45.58 C216.56,44.08 217.47,42.96 217.87,42.22 C218.26,41.48 218.46,40.82 218.46,40.24 C218.46,38.22 217.11,37.21 214.41,37.21 C212.26,37.21 210.34,37.74 208.63,38.80 L206.69,33.15 C209.45,31.71 212.65,30.99 216.30,30.99 C219.68,30.99 222.34,31.73 224.25,33.21 C226.17,34.69 227.13,36.63 227.13,39.00 C227.13,40.20 226.79,41.47 226.11,42.79 C225.44,44.12 224.17,45.72 222.30,47.59 C220.37,49.45 219.41,51.60 219.41,54.01 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M221.04,62.39 C221.04,63.78 220.57,64.91 219.64,65.79 C218.71,66.68 217.49,67.12 215.99,67.12 C214.51,67.12 213.30,66.67 212.35,65.78 C211.41,64.89 210.94,63.76 210.94,62.39 C210.94,61.01 211.41,59.87 212.35,58.99 C213.30,58.11 214.51,57.67 215.99,57.67 C217.47,57.67 218.68,58.11 219.63,58.99 C220.57,59.87 221.04,61.01 221.04,62.39 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M221.06,62.28 C221.06,63.66 220.59,64.80 219.66,65.68 C218.72,66.56 217.51,67.00 216.01,67.00 C214.53,67.00 213.31,66.56 212.37,65.67 C211.43,64.78 210.95,63.64 210.95,62.28 C210.95,60.89 211.43,59.75 212.37,58.87 C213.31,57.99 214.53,57.55 216.01,57.55 C217.49,57.55 218.70,57.99 219.64,58.87 C220.59,59.75 221.06,60.89 221.06,62.28 Z "
},
]
}
}
}
