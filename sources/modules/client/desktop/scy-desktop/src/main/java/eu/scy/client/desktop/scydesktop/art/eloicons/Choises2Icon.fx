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
public class Choises2Icon extends AbstractEloIcon {

public function clone(): Choises2Icon {
Choises2Icon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.22
					content: "M223.95,29.96 C216.32,30.44 208.64,30.10 201.00,30.16 C190.52,30.26 190.32,48.37 200.79,48.27 C208.43,48.21 216.11,48.55 223.74,48.07 C228.16,47.79 231.90,44.43 231.96,39.21 C232.02,34.57 228.36,29.68 223.95,29.96 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.22
					content: "M227.17,51.14 C227.14,51.11 227.12,51.09 227.09,51.07 C225.12,49.58 223.10,49.28 221.25,49.78 C221.27,49.73 221.28,49.68 221.29,49.63 C220.12,49.84 215.82,49.16 213.66,49.01 C209.37,48.71 205.09,48.10 200.80,47.76 C190.62,46.96 188.66,65.87 198.83,66.67 C203.13,67.00 207.40,67.42 211.69,67.92 C215.49,68.36 219.74,68.99 223.57,68.28 C230.29,67.02 232.91,55.49 227.17,51.14 Z "
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.76
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [218.72,46.26]
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.35
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [206.79,50.54]
				},
				Polyline {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.22
					points: [228.18,65.12,228.18,51.11,201.35,51.11,196.74,58.32,201.35,65.52,213.29,65.52,224.73,65.41,228.18,65.41]
				},
				Polygon {
					points: [223.13,32.64,227.74,39.85,223.13,47.05,196.30,47.05,196.30,32.64]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.22
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.35
					strokeLineCap: StrokeLineCap.BUTT
					content: "M213.12,42.19 L210.47,42.19 C210.46,41.82 210.45,41.61 210.45,41.56 C210.45,40.60 210.83,39.71 211.61,38.90 C212.11,38.36 212.43,37.97 212.58,37.71 C212.72,37.45 212.79,37.21 212.79,37.01 C212.79,36.29 212.31,35.93 211.35,35.93 C210.59,35.93 209.91,36.12 209.31,36.50 L208.62,34.50 C209.60,33.99 210.73,33.74 212.02,33.74 C213.22,33.74 214.16,34.00 214.83,34.52 C215.51,35.05 215.85,35.73 215.85,36.57 C215.85,36.99 215.73,37.44 215.49,37.91 C215.25,38.38 214.80,38.95 214.14,39.61 C213.46,40.27 213.12,41.02 213.12,41.88 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.44
					strokeLineCap: StrokeLineCap.BUTT
					content: "M213.15,42.21 L210.50,42.21 C210.48,41.84 210.48,41.63 210.48,41.59 C210.48,40.62 210.86,39.74 211.64,38.92 C212.14,38.39 212.46,37.99 212.60,37.73 C212.74,37.47 212.81,37.24 212.81,37.03 C212.81,36.32 212.34,35.96 211.38,35.96 C210.62,35.96 209.94,36.15 209.34,36.52 L208.65,34.52 C209.63,34.02 210.76,33.76 212.05,33.76 C213.25,33.76 214.19,34.02 214.86,34.55 C215.54,35.07 215.88,35.76 215.88,36.60 C215.88,37.02 215.76,37.47 215.52,37.94 C215.28,38.41 214.83,38.97 214.17,39.63 C213.49,40.29 213.15,41.05 213.15,41.91 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.35
					strokeLineCap: StrokeLineCap.BUTT
					content: "M213.73,44.87 C213.73,45.36 213.56,45.76 213.23,46.08 C212.90,46.39 212.47,46.54 211.94,46.54 C211.42,46.54 210.99,46.39 210.65,46.07 C210.32,45.75 210.15,45.35 210.15,44.87 C210.15,44.38 210.32,43.98 210.65,43.67 C210.99,43.35 211.42,43.20 211.94,43.20 C212.46,43.20 212.89,43.35 213.23,43.67 C213.56,43.98 213.73,44.38 213.73,44.87 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.35
					strokeLineCap: StrokeLineCap.BUTT
					content: "M213.73,44.83 C213.73,45.32 213.57,45.72 213.24,46.03 C212.91,46.35 212.48,46.50 211.95,46.50 C211.42,46.50 210.99,46.34 210.66,46.03 C210.32,45.71 210.16,45.31 210.16,44.83 C210.16,44.34 210.32,43.94 210.66,43.63 C210.99,43.31 211.42,43.16 211.95,43.16 C212.47,43.16 212.90,43.31 213.23,43.63 C213.57,43.94 213.73,44.34 213.73,44.83 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.35
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.38,60.35 L214.03,60.35 C214.05,59.98 214.05,59.77 214.05,59.72 C214.05,58.76 213.67,57.87 212.89,57.06 C212.39,56.53 212.07,56.13 211.93,55.87 C211.79,55.61 211.72,55.38 211.72,55.17 C211.72,54.46 212.19,54.10 213.15,54.10 C213.91,54.10 214.59,54.29 215.19,54.66 L215.88,52.66 C214.91,52.15 213.77,51.90 212.48,51.90 C211.28,51.90 210.35,52.16 209.67,52.68 C208.99,53.21 208.65,53.89 208.65,54.73 C208.65,55.16 208.77,55.61 209.01,56.08 C209.25,56.54 209.70,57.11 210.36,57.77 C211.04,58.43 211.38,59.19 211.38,60.04 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.44
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.35,60.38 L214.01,60.38 C214.02,60.01 214.03,59.80 214.03,59.75 C214.03,58.79 213.64,57.90 212.87,57.09 C212.36,56.55 212.04,56.16 211.90,55.90 C211.76,55.64 211.69,55.40 211.69,55.20 C211.69,54.48 212.17,54.12 213.12,54.12 C213.88,54.12 214.56,54.31 215.17,54.69 L215.85,52.69 C214.88,52.18 213.74,51.93 212.45,51.93 C211.26,51.93 210.32,52.19 209.64,52.71 C208.96,53.23 208.62,53.92 208.62,54.76 C208.62,55.18 208.74,55.63 208.98,56.10 C209.22,56.57 209.67,57.14 210.33,57.80 C211.01,58.46 211.35,59.21 211.35,60.07 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.35
					strokeLineCap: StrokeLineCap.BUTT
					content: "M210.78,63.04 C210.78,63.53 210.94,63.93 211.27,64.24 C211.60,64.55 212.03,64.71 212.56,64.71 C213.09,64.71 213.51,64.55 213.85,64.23 C214.18,63.92 214.35,63.52 214.35,63.04 C214.35,62.54 214.18,62.14 213.85,61.83 C213.51,61.52 213.09,61.36 212.56,61.36 C212.04,61.36 211.61,61.52 211.28,61.83 C210.94,62.14 210.78,62.54 210.78,63.04 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.35
					strokeLineCap: StrokeLineCap.BUTT
					content: "M210.77,62.99 C210.77,63.48 210.93,63.89 211.26,64.20 C211.60,64.51 212.03,64.67 212.56,64.67 C213.08,64.67 213.51,64.51 213.84,64.19 C214.18,63.88 214.34,63.48 214.34,62.99 C214.34,62.50 214.18,62.10 213.84,61.79 C213.51,61.48 213.08,61.32 212.56,61.32 C212.03,61.32 211.60,61.48 211.27,61.79 C210.94,62.10 210.77,62.50 210.77,62.99 Z "
},
]
}
}
}
