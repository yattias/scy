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
public class DrawingIcon extends AbstractEloIcon {

public function clone(): DrawingIcon {
DrawingIcon {
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
					content: "M223.64,48.21 C221.99,51.88 221.05,55.84 219.48,59.55 C218.75,61.36 218.56,61.49 218.91,59.94 C218.21,59.25 217.56,58.50 216.98,57.69 C215.47,55.75 214.40,53.40 213.41,51.10 C210.06,43.33 206.90,37.44 199.87,33.23 C197.52,31.82 190.44,35.55 193.75,37.54 C199.31,40.88 202.47,45.45 205.02,51.79 C206.88,56.41 209.24,62.13 213.37,64.77 C216.52,66.79 220.52,66.96 223.48,64.31 C227.82,60.41 228.50,53.82 230.86,48.53 C232.59,44.68 224.83,45.54 223.64,48.21 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M194.88,39.73 C198.85,40.84 201.75,47.05 203.83,50.16 C206.76,54.54 209.79,59.50 214.66,61.90 C218.49,63.78 223.53,61.89 226.46,59.24 C230.51,55.57 229.68,47.99 229.69,43.01 C229.69,41.61 223.69,42.60 223.69,44.62 C223.68,48.67 224.41,54.85 222.01,58.36 C219.88,61.49 216.23,57.05 213.92,54.25 C211.10,50.81 208.83,46.94 206.30,43.30 C204.66,40.94 202.68,37.96 199.74,37.14 C198.17,36.70 192.78,39.15 194.88,39.73 Z "
},
]
}
}
}
