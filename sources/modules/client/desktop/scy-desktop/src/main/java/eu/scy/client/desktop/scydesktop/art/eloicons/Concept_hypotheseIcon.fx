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
public class Concept_hypotheseIcon extends AbstractEloIcon {

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: Color.rgb(0xd3,0xe6,0xf6)
					stroke: null
					content: "M231.51,44.86 C229.15,31.35 213.68,28.87 206.92,33.38 C205.96,34.02 205.65,35.31 205.76,36.54 C202.94,37.64 200.21,39.23 197.43,41.03 C193.35,43.67 191.73,47.92 192.26,54.79 C193.49,70.75 207.15,69.89 214.48,68.01 C221.10,66.31 234.05,59.35 231.51,44.86 Z M222.87,57.15 C220.15,59.69 216.85,60.71 213.79,61.61 C208.96,63.03 198.16,64.89 196.44,54.83 C195.43,48.93 197.94,47.73 200.85,45.80 C204.54,43.35 208.41,41.63 212.42,41.48 C213.83,41.43 214.58,39.54 214.58,37.81 C219.99,37.79 226.51,40.45 227.66,47.02 C228.41,51.30 224.78,55.36 222.87,57.15 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M230.23,52.65 C230.29,53.11 230.27,53.56 230.18,54.02 L230.02,54.57 L230.28,55.40 C230.54,56.49 230.21,57.50 229.33,58.19 C229.18,58.31 229.01,58.42 228.83,58.50 L228.81,58.77 C228.75,59.02 228.66,59.27 228.54,59.51 C228.28,59.98 227.85,60.36 227.33,60.57 L226.98,60.71 L226.62,60.80 L226.43,61.02 L226.22,61.21 C225.37,61.96 224.31,62.13 223.24,61.70 L222.98,61.58 L222.71,61.42 L222.64,61.54 C222.56,61.60 222.48,61.67 222.42,61.74 C221.19,62.65 219.69,62.79 218.26,62.17 C218.11,62.09 217.97,62.01 217.83,61.92 L217.68,61.96 L217.52,61.99 C216.54,62.19 215.56,62.01 214.72,61.50 L214.49,61.36 L214.28,61.50 C212.88,62.33 211.34,62.40 209.87,61.70 L209.55,61.81 C207.74,62.41 205.93,62.12 204.41,61.00 L204.07,60.69 L203.68,60.85 C202.01,61.39 200.47,60.99 199.32,59.71 L199.09,59.42 L198.92,59.13 L198.77,58.86 L198.66,58.79 L198.56,58.70 L198.48,58.59 C197.05,57.56 196.46,56.11 196.87,54.51 L197.05,54.13 L196.74,53.54 C196.03,52.08 196.20,50.58 197.27,49.37 L197.40,49.23 L197.22,48.61 C197.07,47.15 197.51,45.74 198.48,44.59 L198.43,44.13 C198.24,42.51 198.96,41.21 200.48,40.53 C201.14,38.91 202.49,38.13 204.28,38.39 L204.90,38.72 Q206.02,37.25 206.84,38.12 C207.36,38.68 207.28,37.45 209.58,37.45 Q211.02,37.45 212.06,38.48 L212.38,38.32 L212.67,38.20 C214.06,37.76 215.40,38.06 216.44,39.04 L216.62,39.21 L216.96,39.21 L217.29,39.24 C218.27,39.37 219.09,39.89 219.61,40.69 L219.71,40.84 L219.81,41.02 L220.26,41.11 L220.68,41.22 C222.07,41.69 222.97,42.69 223.27,44.06 L223.32,44.55 L223.61,44.66 C223.82,44.76 224.02,44.86 224.22,44.97 C224.83,45.35 225.29,45.91 225.54,46.56 L225.59,46.74 L225.62,46.94 L225.96,47.21 L226.27,47.50 C226.73,47.95 227.05,48.50 227.23,49.11 L227.30,49.44 L227.80,49.66 C229.09,50.28 229.94,51.32 230.23,52.65 L230.23,52.65 L230.23,52.65 L230.23,52.65 L230.23,52.65 L230.23,52.65 L230.23,52.65 L230.23,52.65 L230.23,52.65 L230.23,52.65 L230.23,52.65 L230.23,52.65 L230.23,52.65 L230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Q230.29,53.11 230.23,52.65 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M200.18,43.09 Q207.18,43.80 203.75,48.35 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M196.23,51.37 Q197.85,47.53 200.18,50.84 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M203.68,59.90 Q203.49,54.39 207.96,55.04 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M207.46,50.60 Q202.24,45.12 199.86,51.14 C197.89,56.11 202.32,56.64 203.36,56.58 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M206.08,38.53 Q210.94,44.68 205.37,46.05 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M208.19,43.40 Q211.27,39.80 212.24,43.56 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.20,45.28 Q215.29,40.25 217.49,43.09 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M216.52,44.68 Q223.84,41.52 222.35,48.95 "
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					points: [209.06,50.66]
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					points: [213.15,47.70]
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M206.47,51.26 Q210.88,49.60 213.34,51.91 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M214.14,56.70 Q214.51,60.44 219.25,59.67 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M208.55,52.97 Q207.64,57.95 214.06,57.88 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M219.44,48.71 Q216.07,49.06 217.75,51.97 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M228.26,52.80 Q223.78,48.89 221.12,54.16 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M227.80,55.52 Q224.17,53.86 221.25,59.19 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.14,61.14 Q208.29,60.37 211.20,57.12 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.41
					strokeLineCap: StrokeLineCap.BUTT
					content: "M212.31,52.42 Q221.31,46.49 222.22,57.38 "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.52
					strokeLineCap: StrokeLineCap.BUTT
					content: "M215.99,53.52 L208.84,53.52 C208.81,52.58 208.79,52.05 208.79,51.93 C208.79,49.47 209.83,47.22 211.91,45.16 C213.27,43.80 214.14,42.80 214.52,42.13 C214.90,41.47 215.09,40.88 215.09,40.36 C215.09,38.54 213.80,37.63 211.22,37.63 C209.17,37.63 207.34,38.11 205.71,39.06 L203.86,33.98 C206.49,32.69 209.54,32.04 213.02,32.04 C216.25,32.04 218.78,32.71 220.61,34.04 C222.44,35.37 223.35,37.11 223.35,39.25 C223.35,40.32 223.03,41.46 222.38,42.65 C221.74,43.85 220.53,45.28 218.74,46.96 C216.91,48.64 215.99,50.56 215.99,52.73 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.55
					strokeLineCap: StrokeLineCap.BUTT
					content: "M217.36,60.64 C217.36,61.95 216.91,63.02 216.02,63.85 C215.12,64.68 213.96,65.09 212.52,65.09 C211.11,65.09 209.94,64.67 209.04,63.83 C208.14,63.00 207.68,61.93 207.68,60.64 C207.68,59.34 208.14,58.27 209.04,57.44 C209.94,56.61 211.11,56.19 212.52,56.19 C213.94,56.19 215.10,56.61 216.01,57.44 C216.91,58.27 217.36,59.34 217.36,60.64 Z "
},
]
}
}
}
