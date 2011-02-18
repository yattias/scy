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
public class Dataset_processingIcon extends AbstractEloIcon {

public override function clone(): Dataset_processingIcon {
Dataset_processingIcon {
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
					content: "M230.17,64.95 C229.18,59.02 229.03,52.72 229.22,46.73 C229.35,42.54 230.08,37.74 229.22,33.60 C228.99,32.50 227.96,32.04 226.94,32.38 C222.66,33.80 216.38,32.76 211.92,32.37 C206.66,31.91 201.63,30.76 196.32,30.82 C194.37,30.84 193.92,33.68 195.60,34.02 C196.01,40.73 194.97,47.92 194.59,54.61 C194.40,58.03 192.08,63.16 195.08,65.80 C196.18,66.78 198.76,66.34 200.04,66.35 C203.12,66.37 206.25,66.93 209.33,67.16 C215.74,67.65 222.41,67.23 228.82,66.81 C229.70,66.75 230.30,65.76 230.17,64.95 Z M197.13,63.05 C197.44,63.06 198.69,40.60 198.78,34.12 C203.78,34.38 208.67,35.35 213.67,35.79 C217.55,36.13 222.24,36.62 226.27,35.85 C226.83,40.57 225.95,45.83 225.89,50.47 C225.83,54.82 226.09,59.33 226.70,63.68 C216.75,64.18 207.07,63.41 197.13,63.05 Z "
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 199.15
					y: 35.21
					width: 27.57
					height: 27.57
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M225.81,36.13 L225.81,61.86 L200.07,61.86 L200.07,36.13 Z M227.65,34.29 L198.23,34.29 L198.23,63.70 L227.65,63.70 Z "
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 220.27
					y: 56.38
					width: 7.35
					height: 7.31
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 220.27
					y: 45.55
					width: 7.35
					height: 7.31
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 220.27
					y: 34.4
					width: 7.35
					height: 7.4
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 209.28
					y: 34.32
					width: 7.37
					height: 7.46
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 198.13
					y: 34.32
					width: 7.37
					height: 7.46
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 209.28
					y: 45.52
					width: 7.37
					height: 7.37
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 198.13
					y: 45.52
					width: 7.37
					height: 7.37
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 209.28
					y: 56.44
					width: 7.37
					height: 7.37
				},
				Rectangle {
					fill: Color.WHITE
					stroke: null
					x: 198.13
					y: 56.44
					width: 7.37
					height: 7.37
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M225.81,36.13 L225.81,61.86 L200.07,61.86 L200.07,36.13 Z M227.64,34.29 L198.23,34.29 L198.23,63.70 L227.64,63.70 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M202.80,38.00 L202.49,38.00 C201.67,38.00 200.94,38.52 200.88,39.39 C200.82,40.15 201.44,41.00 202.26,41.00 L202.57,41.00 C203.39,41.00 204.12,40.48 204.18,39.61 C204.24,38.85 203.62,38.00 202.80,38.00 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M212.73,37.76 C212.70,37.76 212.70,37.76 212.68,37.75 C212.74,37.77 212.80,37.79 212.88,37.82 C212.81,37.79 212.74,37.78 212.67,37.75 C212.48,37.75 212.47,37.71 212.63,37.74 C211.00,37.28 209.95,39.78 211.62,40.55 C212.08,40.76 212.53,40.77 213.02,40.74 C213.84,40.71 214.44,39.89 214.37,39.10 C214.29,38.26 213.54,37.72 212.73,37.76 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M212.68,37.75 C212.66,37.75 212.64,37.74 212.63,37.74 C212.64,37.74 212.66,37.75 212.67,37.75 C212.68,37.75 212.68,37.75 212.68,37.75 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M223.19,37.75 C221.25,37.75 221.25,40.75 223.19,40.75 C225.13,40.75 225.12,37.75 223.19,37.75 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M224.29,48.14 C223.63,47.69 222.68,47.84 222.22,48.50 C222.20,48.51 222.19,48.52 222.14,48.55 C221.46,49.00 221.51,50.11 221.99,50.67 C222.58,51.34 223.42,51.27 224.11,50.82 C224.34,50.67 224.52,50.46 224.67,50.22 C225.12,49.54 224.97,48.61 224.29,48.14 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M213.96,48.55 C213.31,48.21 212.58,48.40 212.13,48.90 C211.66,49.28 211.44,49.91 211.63,50.51 C211.88,51.29 212.74,51.75 213.51,51.49 C214.01,51.33 214.31,51.02 214.57,50.58 C214.99,49.88 214.65,48.92 213.96,48.55 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M202.79,48.88 C200.88,48.88 200.53,51.87 202.46,51.87 C204.37,51.87 204.72,48.88 202.79,48.88 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M203.22,58.19 C201.28,58.19 201.22,61.19 203.16,61.19 C205.09,61.19 205.15,58.19 203.22,58.19 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M214.01,58.67 C213.67,58.04 212.75,57.63 212.07,58.01 C211.45,58.18 210.97,58.68 210.94,59.38 C210.91,60.16 211.56,60.95 212.38,60.94 C212.73,60.93 213.09,60.90 213.39,60.70 C214.07,60.24 214.43,59.45 214.01,58.67 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M223.58,57.75 C221.65,57.75 221.49,60.75 223.42,60.75 C225.35,60.75 225.51,57.75 223.58,57.75 Z "
},
]
}
}
}
