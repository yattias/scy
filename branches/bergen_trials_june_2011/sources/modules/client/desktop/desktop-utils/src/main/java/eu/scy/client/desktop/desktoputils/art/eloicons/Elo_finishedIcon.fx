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
public class Elo_finishedIcon extends AbstractEloIcon {

public override function clone(): Elo_finishedIcon {
Elo_finishedIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 0.42
					y: 0.14
					width: 40.0
					height: 40.0
				},
				Rectangle {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					x: 3.65
					y: 2.72
					width: 33.77
					height: 34.0
				},
				Rectangle {
					fill: null
					stroke: null
					x: 0.3
					y: 0.14
					width: 40.0
					height: 40.0
				},
				Line {
					fill: null
					stroke: null
					startX: 7.92
					startY: 23.14
					endX: 18.42
					endY: 32.89
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 2.98
					strokeLineCap: StrokeLineCap.BUTT
					content: "M28.68,22.44 C28.68,28.42 23.83,33.28 17.84,33.28 C11.85,33.28 6.99,28.42 6.99,22.44 C6.99,16.45 11.85,11.59 17.84,11.59 C23.83,11.59 28.68,16.45 28.68,22.44 Z "
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 5.27
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [8.26,12.86,16.39,29.40,33.80,8.21]
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
         Elo_finishedIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Elo_finishedIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
