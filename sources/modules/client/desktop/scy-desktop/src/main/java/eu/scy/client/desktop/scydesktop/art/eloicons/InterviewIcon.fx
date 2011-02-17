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
public class InterviewIcon extends AbstractEloIcon {

public function clone(): InterviewIcon {
InterviewIcon {
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
					stroke: null
					content: "M226.78,33.80 C221.81,31.05 213.15,28.89 207.40,30.97 C207.33,30.96 207.27,30.94 207.18,30.96 C200.36,32.12 191.66,36.39 193.51,44.98 C194.19,48.12 197.07,52.41 199.81,54.07 C202.08,55.44 198.76,59.85 197.88,61.82 C197.17,63.38 195.92,65.27 196.62,66.82 Q196.88,67.38 197.81,67.68 Q204.04,64.69 206.52,62.27 C208.11,60.72 209.93,57.64 212.64,58.12 C214.38,58.42 216.00,58.39 217.77,58.26 C223.37,57.85 229.58,54.81 231.69,49.23 C233.79,43.64 232.06,36.72 226.78,33.80 Z M230.32,47.68 C229.66,51.33 225.10,54.35 222.06,55.43 C219.24,56.44 216.05,56.61 213.11,56.18 C212.29,56.06 210.70,55.56 209.88,56.20 C208.48,57.29 207.42,58.67 206.22,59.97 C204.11,62.26 202.64,64.09 199.88,65.44 C200.58,61.68 202.03,58.08 203.35,54.56 C203.49,54.18 203.26,53.67 202.91,53.49 C199.60,51.83 195.45,47.19 195.31,43.30 C195.10,37.13 200.63,34.34 205.81,33.19 C206.07,33.40 206.44,33.48 206.84,33.26 C211.94,30.43 220.84,33.53 225.84,35.57 C230.34,37.40 231.08,43.45 230.32,47.68 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M213.00,32.77 C203.43,32.77 195.67,37.85 195.67,44.13 Q195.67,48.17 203.73,53.72 L200.90,63.95 L210.91,55.40 C211.60,55.45 212.29,55.48 213.00,55.48 C222.57,55.48 230.33,50.40 230.33,44.13 C230.33,37.85 222.57,32.77 213.00,32.77 Z "
},
]
}
}
}
