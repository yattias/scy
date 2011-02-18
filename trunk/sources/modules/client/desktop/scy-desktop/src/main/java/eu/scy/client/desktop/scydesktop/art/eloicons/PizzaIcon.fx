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
public class PizzaIcon extends AbstractEloIcon {

public override function clone(): PizzaIcon {
PizzaIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColorLight
					strokeWidth: 5.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M228.64,44.23 C227.41,40.11 224.58,36.92 220.81,34.38 C215.95,31.10 210.30,32.03 204.87,33.37 C204.31,33.51 203.85,33.74 203.48,34.02 C202.72,34.13 201.96,34.44 201.34,35.09 C195.25,41.41 191.72,50.40 197.38,57.98 C202.36,64.65 211.49,67.06 219.91,64.57 C229.13,61.84 230.77,51.40 228.64,44.23 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.72,48.86 L200.40,37.54 C203.53,34.41 207.31,32.85 211.72,32.85 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.72,48.86 L195.72,48.86 C195.72,44.44 197.28,40.66 200.40,37.54 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.72,48.86 L200.40,60.18 C197.28,57.05 195.72,53.28 195.72,48.86 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.72,48.86 L211.72,64.87 C207.31,64.87 203.53,63.30 200.40,60.18 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.72,48.86 L223.05,60.18 C219.92,63.30 216.15,64.87 211.72,64.87 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.72,48.86 L227.73,48.86 C227.73,53.28 226.17,57.05 223.05,60.18 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.72,48.86 L223.05,37.54 C226.17,40.66 227.73,44.44 227.73,48.86 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.1
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.72,48.86 L211.72,32.85 C216.15,32.85 219.92,34.41 223.05,37.54 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.22
					strokeLineCap: StrokeLineCap.BUTT
					content: "M200.21,44.92 C201.04,45.60 201.52,44.77 202.37,45.54 C203.10,46.50 203.30,47.50 204.28,48.45 C205.20,47.28 206.30,46.86 207.29,45.98 C206.18,45.19 205.70,44.36 204.41,43.59 C206.69,38.86 197.09,41.55 200.21,44.92 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.2
					strokeLineCap: StrokeLineCap.BUTT
					content: "M213.92,37.76 C214.33,38.59 215.12,38.04 215.50,38.95 C215.70,39.99 215.42,40.89 215.83,42.00 C217.16,41.30 218.28,41.28 219.53,40.85 C218.93,39.85 218.89,39.00 218.13,37.95 C222.21,34.68 212.75,33.96 213.92,37.76 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.2
					strokeLineCap: StrokeLineCap.BUTT
					content: "M222.62,50.69 C222.26,51.54 223.19,51.78 222.76,52.66 C222.10,53.49 221.24,53.88 220.68,54.91 C222.07,55.45 222.83,56.29 223.97,56.95 C224.33,55.83 224.95,55.25 225.24,53.99 C230.38,54.92 224.72,47.31 222.62,50.69 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M217.96,60.99 C222.55,61.70 219.35,56.01 215.91,56.14 C211.59,56.31 216.29,60.74 217.96,60.99 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M224.73,47.40 C229.42,45.09 222.77,41.53 219.69,43.92 C215.82,46.93 223.03,48.24 224.73,47.40 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M206.12,51.31 C205.12,49.98 201.70,50.47 201.24,52.63 C200.47,56.25 205.52,54.50 206.11,53.25 C206.51,52.40 206.45,51.75 206.12,51.31 "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.WHITE
					strokeWidth: 0.21
					strokeLineCap: StrokeLineCap.BUTT
					content: "M206.96,56.89 C206.90,57.57 207.55,57.94 207.74,58.51 C207.36,59.33 206.98,60.16 206.59,60.98 C207.21,61.65 208.68,61.13 209.77,61.71 C210.27,60.50 209.90,59.68 210.11,58.61 C210.76,58.85 211.18,58.36 211.59,57.79 C211.82,55.84 207.70,54.22 206.96,56.89 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M211.60,40.18 C209.98,42.86 205.05,40.78 204.77,38.23 C206.27,35.27 211.43,37.70 211.60,40.18 Z "
},
]
}
}
}
