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
public class AnalyseIcon extends AbstractEloIcon {

public override function clone(): AnalyseIcon {
AnalyseIcon {
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
					content: "M227.51,51.81 C225.52,49.31 224.86,48.39 225.31,44.99 C225.68,42.18 225.70,39.40 225.34,36.60 C224.81,32.41 222.86,31.29 218.99,31.71 C211.39,32.54 203.29,31.99 195.64,32.01 C194.97,32.01 194.64,32.62 194.68,33.16 C194.28,33.29 193.93,33.65 193.97,34.22 C194.14,37.03 194.54,39.56 194.13,42.40 C193.85,44.39 193.12,47.15 193.73,49.10 C194.52,51.61 195.40,52.24 196.97,52.37 C195.52,53.60 194.42,55.19 194.99,57.20 C196.24,61.59 199.71,64.10 203.84,65.74 C208.50,67.60 213.84,67.10 218.71,66.68 C223.38,66.27 227.84,65.37 230.70,61.39 C233.11,58.02 229.42,54.19 227.51,51.81 Z M218.38,64.71 C215.07,65.03 211.80,65.17 208.49,64.82 C206.62,64.62 204.84,64.04 203.11,63.35 C199.10,61.76 195.20,55.45 200.59,52.29 C201.30,51.87 201.28,50.38 200.27,50.38 C196.12,50.41 194.99,50.29 195.67,45.43 C196.23,41.38 196.02,37.96 195.79,33.99 C202.93,33.97 210.08,33.90 217.22,33.79 C228.98,33.60 220.76,44.66 223.54,50.22 C224.50,52.13 226.83,53.96 228.19,55.70 C233.78,62.87 221.32,64.42 218.38,64.71 Z "
				},
				Polyline {
					fill: Color.WHITE
					stroke: null
					points: [196.52,44.50]
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.04
					points: [195.22,36.13]
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.95
					points: [195.22,36.78,195.22,33.77,223.09,33.77,223.09,36.78]
				},
				Rectangle {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 1.04
					x: 195.26
					y: 37.25
					width: 27.8
					height: 13.07
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.67
					startX: 200.34
					startY: 34.09
					endX: 200.34
					endY: 50.04
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.69
					startX: 216.66
					startY: 44.58
					endX: 216.66
					endY: 49.66
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.7
					startX: 195.7
					startY: 44.34
					endX: 221.94
					endY: 44.34
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.69
					startX: 196.01
					startY: 47.4
					endX: 221.95
					endY: 47.4
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 197.0
					startY: 35.5
					endX: 199.0
					endY: 35.5
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 197.0
					startY: 39.5
					endX: 199.0
					endY: 39.5
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 197.0
					startY: 42.5
					endX: 199.0
					endY: 42.5
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 197.0
					startY: 45.5
					endX: 199.0
					endY: 45.5
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 197.0
					startY: 48.5
					endX: 199.0
					endY: 48.5
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 201.0
					startY: 35.5
					endX: 216.0
					endY: 35.5
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 201.0
					startY: 48.5
					endX: 216.0
					endY: 48.5
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 201.0
					startY: 39.5
					endX: 221.0
					endY: 39.5
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 201.0
					startY: 45.5
					endX: 213.0
					endY: 45.5
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 201.0
					startY: 42.5
					endX: 221.0
					endY: 42.5
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M228.60,57.28 C228.65,57.66 228.63,58.05 228.55,58.43 L228.42,58.90 L228.64,59.60 C228.86,60.52 228.58,61.38 227.84,61.96 C227.71,62.07 227.57,62.16 227.42,62.23 L227.39,62.46 C227.34,62.67 227.27,62.88 227.17,63.08 C226.95,63.48 226.58,63.80 226.14,63.98 L225.85,64.10 L225.54,64.18 L225.38,64.37 L225.21,64.52 C224.48,65.16 223.59,65.30 222.68,64.93 L222.46,64.84 L222.23,64.70 L222.17,64.80 C222.10,64.85 222.04,64.91 221.99,64.97 C220.94,65.74 219.67,65.86 218.46,65.33 C218.33,65.27 218.21,65.20 218.10,65.12 L217.97,65.16 L217.83,65.18 C217.01,65.35 216.17,65.20 215.46,64.76 L215.27,64.65 L215.09,64.76 C213.90,65.47 212.60,65.53 211.35,64.93 L211.09,65.03 C209.55,65.54 208.02,65.29 206.73,64.34 L206.44,64.08 L206.11,64.21 C204.70,64.68 203.39,64.33 202.42,63.25 L202.23,63.00 L202.09,62.76 L201.96,62.53 L201.87,62.47 L201.78,62.40 L201.71,62.30 C200.50,61.43 200.00,60.21 200.34,58.85 L200.50,58.53 L200.24,58.03 C199.64,56.79 199.78,55.53 200.69,54.50 L200.79,54.38 L200.64,53.85 C200.52,52.62 200.89,51.42 201.71,50.45 L201.67,50.06 C201.51,48.69 202.12,47.59 203.41,47.01 C203.96,45.64 205.11,44.98 206.63,45.20 L207.15,45.48 Q208.09,44.23 208.79,44.97 C209.23,45.44 209.17,44.40 211.11,44.40 Q212.33,44.41 213.21,45.27 L213.48,45.14 L213.72,45.04 C214.91,44.67 216.04,44.92 216.92,45.75 L217.07,45.89 L217.36,45.89 L217.63,45.92 C218.47,46.03 219.17,46.46 219.60,47.14 L219.69,47.28 L219.77,47.43 L220.15,47.50 L220.51,47.60 C221.68,47.99 222.45,48.84 222.70,50.00 L222.75,50.42 L222.99,50.51 C223.17,50.59 223.34,50.68 223.51,50.77 C224.03,51.10 224.41,51.56 224.62,52.12 L224.67,52.27 L224.69,52.44 L224.98,52.67 L225.24,52.91 C225.63,53.30 225.91,53.76 226.05,54.27 L226.12,54.56 L226.54,54.74 C227.63,55.26 228.35,56.15 228.60,57.28 L228.60,57.28 L228.60,57.28 L228.60,57.28 L228.60,57.28 L228.60,57.28 L228.60,57.28 L228.60,57.28 L228.60,57.28 L228.60,57.28 L228.60,57.28 L228.60,57.28 L228.60,57.28 L228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Q228.65,57.66 228.60,57.28 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M203.15,49.17 Q209.08,49.78 206.17,53.64 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M199.80,56.19 Q201.18,52.94 203.15,55.74 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M206.12,63.41 Q205.95,58.75 209.74,59.30 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M209.31,55.54 Q204.90,50.90 202.88,55.99 C201.21,60.20 204.96,60.66 205.84,60.61 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M208.15,45.32 Q212.27,50.53 207.54,51.68 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M209.93,49.44 Q212.54,46.39 213.37,49.58 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M212.48,51.03 Q215.95,46.77 217.81,49.17 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M216.99,50.53 Q223.18,47.85 221.93,54.14 "
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					points: [210.67,55.59]
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					points: [214.13,53.09]
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M208.48,56.09 Q212.21,54.69 214.30,56.64 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M214.97,60.70 Q215.29,63.87 219.30,63.22 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M210.24,57.55 Q209.47,61.76 214.90,61.71 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M219.46,53.94 Q216.60,54.24 218.03,56.69 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M226.93,57.40 Q223.14,54.09 220.89,58.55 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M226.54,59.70 Q223.47,58.30 220.99,62.81 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M212.43,64.46 Q210.01,63.81 212.48,61.06 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M213.42,57.08 Q221.05,52.06 221.81,61.28 "
},
]
}
}
}
