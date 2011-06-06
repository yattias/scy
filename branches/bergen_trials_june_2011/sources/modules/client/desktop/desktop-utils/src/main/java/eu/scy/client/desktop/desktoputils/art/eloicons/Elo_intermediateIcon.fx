package eu.scy.client.desktop.desktoputils.art.eloicons;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
/**
 * @author lars
 */
public class Elo_intermediateIcon extends AbstractEloIcon {

public override function clone(): Elo_intermediateIcon {
Elo_intermediateIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					x: 0.66
					y: 0.36
					width: 40.0
					height: 39.91
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 6.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 20.42
					startY: 2.14
					endX: 20.42
					endY: 40.14
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 6.01
					strokeLineCap: StrokeLineCap.ROUND
					content: "M3.61,17.01 C4.72,9.46 11.83,3.64 20.44,3.64 C29.14,3.64 36.32,9.59 37.31,17.25 "
},
]
}
}
}
function run(){
   def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   Stage {
	title: 'MyApp'
	onClose: function () {  }
	scene: Scene {
		width: 200
		height: 200
      fill: Color.YELLOW
		content: [
         Elo_intermediateIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Elo_intermediateIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
