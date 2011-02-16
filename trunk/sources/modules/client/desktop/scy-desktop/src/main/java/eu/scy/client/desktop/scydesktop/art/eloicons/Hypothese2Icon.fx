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
public class Hypothese2Icon extends AbstractEloIcon {

public function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: Color.rgb(0xff,0x0,0x97)
					strokeWidth: 0.44
					content: "M213.41,29.39 C211.93,28.72 210.64,28.97 209.70,29.71 C200.69,29.68 189.16,33.88 192.63,45.13 C193.41,47.64 194.67,49.89 195.71,52.29 C197.13,55.53 196.02,58.89 196.57,62.19 C197.49,67.65 205.16,67.89 209.28,68.48 C213.53,69.10 219.56,70.01 222.43,65.79 C224.08,63.38 225.21,61.58 227.40,59.57 C229.29,57.83 230.51,55.73 231.37,53.33 C235.61,41.50 222.00,33.32 213.41,29.39 Z M221.09,53.92 C219.90,55.01 218.45,55.96 217.49,57.29 C216.51,58.65 216.21,60.55 214.74,60.55 C212.98,60.55 207.28,60.46 204.86,59.40 C204.80,55.25 204.48,51.39 202.73,47.38 C201.78,45.21 199.16,42.02 201.35,39.89 C203.99,37.33 208.59,37.94 211.87,38.32 C212.12,38.35 212.35,38.34 212.58,38.33 C218.61,41.37 228.06,47.52 221.09,53.92 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: Color.rgb(0xff,0x0,0x97)
					strokeWidth: 0.44
					content: "M220.77,66.02 L203.93,66.02 Q204.34,60.33 204.23,59.79 C204.12,59.24 203.36,55.06 202.49,54.51 C201.62,53.97 202.45,54.48 199.24,51.24 C196.02,48.00 196.13,42.98 197.42,40.54 C198.71,38.10 201.89,34.79 206.13,33.37 Q210.36,31.96 216.61,33.19 Q220.96,34.63 221.81,35.46 Q224.19,36.42 225.56,43.17 C225.66,43.67 224.58,43.67 224.36,44.90 Q224.74,46.41 225.34,46.77 Q225.93,47.13 227.06,48.72 Q227.43,49.37 227.21,49.73 C226.98,50.09 225.26,50.59 225.19,50.88 C225.11,51.17 224.92,51.72 225.04,51.96 C225.22,52.36 225.58,52.63 225.60,52.84 Q225.65,53.23 223.84,53.86 Q225.55,53.83 225.56,54.12 C225.60,54.93 224.94,55.43 225.07,56.00 C225.26,56.79 225.45,56.68 225.41,57.47 C225.40,57.80 225.26,58.23 225.19,58.45 C225.11,58.67 224.66,59.53 222.64,59.82 Q220.61,60.11 220.46,60.83 Q219.79,61.91 220.39,62.48 Q220.99,63.06 220.88,66.02 L220.85,66.02 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: Color.rgb(0xff,0x0,0x97)
					strokeWidth: 0.44
					content: "M221.43,46.72 C221.43,51.95 217.03,56.18 211.59,56.18 C206.16,56.18 201.76,51.95 201.76,46.72 C201.76,41.50 206.16,37.27 211.59,37.27 C217.03,37.27 221.43,41.50 221.43,46.72 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.rgb(0xff,0x0,0x97)
					strokeWidth: 0.44
					content: "M209.11,39.71 Q208.96,38.59 210.68,38.45 C211.92,38.34 214.71,38.48 214.92,39.24 Q215.11,39.92 212.67,42.66 Q212.07,43.13 211.28,42.63 Q210.50,42.12 209.11,39.71 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.rgb(0xff,0x0,0x97)
					strokeWidth: 0.44
					content: "M217.17,41.32 Q218.18,40.75 219.00,42.22 C219.58,43.27 220.54,45.79 219.89,46.27 Q219.32,46.71 215.74,45.63 Q215.06,45.28 215.23,44.39 Q215.40,43.49 217.17,41.32 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.rgb(0xff,0x0,0x97)
					strokeWidth: 0.44
					content: "M219.31,48.15 Q220.33,48.69 219.45,50.11 C218.81,51.13 217.06,53.23 216.29,52.94 Q215.61,52.69 214.73,49.18 Q214.69,48.44 215.57,48.13 Q216.46,47.81 219.31,48.15 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.rgb(0xff,0x0,0x97)
					strokeWidth: 0.44
					content: "M206.87,51.97 Q205.94,52.65 204.95,51.29 C204.24,50.32 202.96,47.93 203.54,47.37 Q204.06,46.87 207.74,47.52 Q208.46,47.78 208.40,48.70 Q208.34,49.60 206.87,51.97 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.rgb(0xff,0x0,0x97)
					strokeWidth: 0.44
					content: "M214.80,53.80 Q214.97,54.91 213.25,55.09 C212.02,55.22 209.22,55.14 209.00,54.38 Q208.80,53.70 211.18,50.91 Q211.77,50.43 212.56,50.92 Q213.36,51.41 214.80,53.80 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.rgb(0xff,0x0,0x97)
					strokeWidth: 0.44
					content: "M204.70,45.22 Q203.62,44.78 204.35,43.28 C204.87,42.20 206.39,39.94 207.18,40.15 Q207.89,40.33 209.14,43.73 Q209.26,44.46 208.41,44.87 Q207.57,45.27 204.70,45.22 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: Color.rgb(0xff,0x0,0x97)
					strokeWidth: 0.44
					content: "M213.26,46.55 C213.26,47.39 212.54,48.08 211.66,48.08 C210.78,48.08 210.06,47.39 210.06,46.55 C210.06,45.70 210.78,45.01 211.66,45.01 C212.54,45.01 213.26,45.70 213.26,46.55 Z "
},
]
}
}
}
