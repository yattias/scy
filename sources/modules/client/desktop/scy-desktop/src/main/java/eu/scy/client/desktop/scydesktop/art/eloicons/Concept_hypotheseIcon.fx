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

public function clone(): Concept_hypotheseIcon {
Concept_hypotheseIcon {
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
					content: "M231.51,44.86 C229.15,31.35 213.68,28.87 206.92,33.38 C205.96,34.02 205.65,35.31 205.76,36.54 C202.94,37.64 200.21,39.23 197.43,41.03 C193.35,43.67 191.73,47.92 192.26,54.79 C193.49,70.75 207.15,69.89 214.48,68.01 C221.10,66.31 234.05,59.35 231.51,44.86 Z M222.87,57.15 C220.15,59.69 216.85,60.71 213.79,61.61 C208.96,63.03 198.16,64.89 196.44,54.83 C195.43,48.93 197.94,47.73 200.85,45.80 C204.54,43.35 208.41,41.63 212.42,41.48 C213.83,41.43 214.58,39.54 214.58,37.81 C219.99,37.79 226.51,40.45 227.66,47.02 C228.41,51.30 224.78,55.36 222.87,57.15 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M227.61,53.45 C227.67,53.88 227.65,54.29 227.56,54.71 L227.42,55.23 L227.66,55.99 C227.90,57.00 227.60,57.93 226.78,58.57 C226.64,58.68 226.49,58.78 226.33,58.86 L226.30,59.11 C226.24,59.34 226.16,59.57 226.06,59.79 C225.81,60.23 225.42,60.58 224.94,60.78 L224.61,60.90 L224.27,60.98 L224.10,61.19 L223.91,61.36 C223.12,62.06 222.14,62.22 221.15,61.81 L220.91,61.71 L220.67,61.56 L220.60,61.67 C220.52,61.72 220.45,61.79 220.40,61.85 C219.25,62.70 217.87,62.82 216.55,62.25 C216.41,62.18 216.28,62.11 216.15,62.02 L216.01,62.06 L215.86,62.08 C214.96,62.27 214.05,62.11 213.28,61.63 L213.06,61.50 L212.87,61.63 C211.57,62.40 210.15,62.47 208.79,61.81 L208.50,61.92 C206.82,62.47 205.14,62.20 203.75,61.17 L203.43,60.88 L203.07,61.03 C201.52,61.53 200.10,61.16 199.04,59.98 L198.82,59.70 L198.67,59.44 L198.53,59.19 L198.43,59.13 L198.33,59.05 L198.26,58.94 C196.94,57.99 196.39,56.65 196.77,55.17 L196.94,54.82 L196.65,54.28 C196.00,52.92 196.16,51.54 197.14,50.42 L197.26,50.29 L197.09,49.71 C196.96,48.37 197.37,47.06 198.26,46.00 L198.21,45.57 C198.04,44.08 198.70,42.87 200.11,42.25 C200.72,40.75 201.97,40.02 203.63,40.26 L204.19,40.57 Q205.23,39.21 205.99,40.01 C206.47,40.53 206.40,39.40 208.53,39.40 Q209.85,39.40 210.82,40.35 L211.11,40.20 L211.38,40.09 C212.67,39.68 213.91,39.96 214.87,40.87 L215.03,41.03 L215.35,41.03 L215.65,41.05 C216.56,41.17 217.32,41.65 217.79,42.39 L217.89,42.53 L217.98,42.70 L218.40,42.78 L218.79,42.88 C220.07,43.32 220.91,44.24 221.18,45.51 L221.23,45.96 L221.50,46.06 C221.69,46.15 221.87,46.25 222.06,46.35 C222.62,46.71 223.05,47.22 223.28,47.82 L223.32,47.98 L223.35,48.17 L223.67,48.42 L223.95,48.68 C224.38,49.11 224.68,49.61 224.84,50.17 L224.91,50.48 L225.37,50.68 C226.56,51.26 227.34,52.22 227.61,53.45 L227.61,53.45 L227.61,53.45 L227.61,53.45 L227.61,53.45 L227.61,53.45 L227.61,53.45 L227.61,53.45 L227.61,53.45 L227.61,53.45 L227.61,53.45 L227.61,53.45 L227.61,53.45 L227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Q227.67,53.88 227.61,53.45 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M199.84,44.61 Q206.31,45.27 203.13,49.48 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M196.18,52.27 Q197.68,48.71 199.84,51.78 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M203.07,60.15 Q202.89,55.06 207.03,55.66 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M206.56,51.55 Q201.74,46.49 199.53,52.05 C197.71,56.65 201.81,57.14 202.77,57.09 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M205.29,40.39 Q209.79,46.09 204.63,47.34 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M207.24,44.90 Q210.08,41.57 210.98,45.05 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M210.02,46.63 Q213.80,41.98 215.84,44.61 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M214.94,46.09 Q221.70,43.16 220.33,50.03 "
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					points: [208.05,51.61]
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					points: [211.82,48.88]
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M205.65,52.16 Q209.73,50.63 212.00,52.76 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M212.74,57.19 Q213.08,60.66 217.46,59.94 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M207.57,53.75 Q206.73,58.35 212.66,58.29 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M217.64,49.81 Q214.52,50.14 216.08,52.82 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M225.79,53.58 Q221.65,49.97 219.20,54.84 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M225.37,56.10 Q222.01,54.57 219.31,59.49 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M209.96,61.30 Q207.33,60.59 210.02,57.58 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M211.05,53.24 Q219.37,47.75 220.21,57.82 "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M208.64,53.93 C208.62,53.22 208.60,52.82 208.60,52.71 C208.60,50.19 209.66,47.92 211.82,45.77 C213.33,44.28 214.27,43.18 214.72,42.41 C215.17,41.60 215.40,40.90 215.40,40.25 C215.40,38.68 214.59,36.81 210.73,36.81 C208.66,36.81 206.76,37.26 205.06,38.13 L203.42,33.64 C206.08,32.41 209.19,31.79 212.67,31.79 C216.02,31.79 218.67,32.48 220.55,33.85 C222.40,35.20 223.29,36.90 223.29,39.05 C223.29,40.12 222.96,41.28 222.31,42.48 C221.65,43.71 220.36,45.23 218.49,47.00 C216.42,48.89 215.36,51.10 215.36,53.58 L215.36,53.93 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M212.67,32.29 C215.91,32.29 218.47,32.95 220.25,34.25 C221.96,35.50 222.79,37.07 222.79,39.05 C222.79,40.04 222.48,41.11 221.87,42.25 C221.23,43.43 219.98,44.90 218.16,46.62 C216.01,48.58 214.91,50.86 214.86,53.43 L209.13,53.43 C209.11,53.03 209.10,52.79 209.10,52.71 C209.10,50.33 210.11,48.18 212.18,46.13 C213.71,44.60 214.68,43.47 215.15,42.66 C215.65,41.77 215.90,40.98 215.90,40.25 C215.90,38.78 215.22,36.31 210.73,36.31 C208.78,36.31 206.98,36.69 205.34,37.44 L204.05,33.91 C206.55,32.84 209.45,32.29 212.67,32.29 M212.67,31.29 C208.92,31.29 205.63,31.99 202.80,33.38 L204.79,38.85 C206.54,37.83 208.52,37.31 210.73,37.31 C213.50,37.31 214.90,38.29 214.90,40.25 C214.90,40.81 214.69,41.45 214.28,42.16 C213.87,42.88 212.93,43.96 211.47,45.42 C209.22,47.64 208.10,50.07 208.10,52.71 C208.10,52.84 208.12,53.41 208.16,54.43 L215.86,54.43 L215.86,53.58 C215.86,51.24 216.85,49.17 218.83,47.36 C220.75,45.55 222.06,44.01 222.75,42.72 C223.45,41.44 223.79,40.21 223.79,39.05 C223.79,36.75 222.81,34.88 220.84,33.44 C218.87,32.01 216.15,31.29 212.67,31.29 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M212.13,66.40 C210.72,66.40 209.60,66.00 208.72,65.17 C207.84,64.36 207.42,63.36 207.42,62.10 C207.42,60.83 207.84,59.82 208.72,59.02 C209.60,58.20 210.71,57.81 212.13,57.81 C213.54,57.81 214.66,58.20 215.54,59.02 C216.41,59.82 216.84,60.83 216.84,62.10 C216.84,63.37 216.42,64.38 215.56,65.19 C214.68,66.00 213.56,66.40 212.13,66.40 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M212.13,58.31 C213.41,58.31 214.42,58.66 215.20,59.38 C215.97,60.09 216.34,60.97 216.34,62.10 C216.34,63.23 215.97,64.12 215.22,64.82 C214.44,65.54 213.43,65.90 212.13,65.90 C210.85,65.90 209.85,65.54 209.06,64.80 C208.29,64.09 207.92,63.21 207.92,62.10 C207.92,60.98 208.29,60.09 209.06,59.38 C209.84,58.66 210.85,58.31 212.13,58.31 M212.13,57.31 C210.60,57.31 209.35,57.75 208.38,58.65 C207.40,59.54 206.92,60.69 206.92,62.10 C206.92,63.49 207.40,64.64 208.38,65.54 C209.35,66.44 210.60,66.90 212.13,66.90 C213.68,66.90 214.93,66.45 215.90,65.55 C216.85,64.66 217.34,63.50 217.34,62.10 C217.34,60.69 216.85,59.54 215.88,58.65 C214.90,57.75 213.66,57.31 212.13,57.31 Z "
},
]
}
}
}
