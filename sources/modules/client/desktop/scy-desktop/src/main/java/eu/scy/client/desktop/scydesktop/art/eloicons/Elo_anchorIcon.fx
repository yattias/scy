package eu.scy.client.desktop.scydesktop.art.eloicons;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
/**
 * @author lars
 */
public class Elo_anchorIcon extends AbstractEloIcon {

public override function clone(): Elo_anchorIcon {
Elo_anchorIcon {
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
					content: "M3.61,11.58 C4.72,7.10 11.83,3.64 20.44,3.64 C29.14,3.64 36.32,7.17 37.31,11.73 "
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 5.0
					strokeLineCap: StrokeLineCap.ROUND
					strokeLineJoin: StrokeLineJoin.BEVEL
					content: "M9.04,25.17 C10.35,20.42 14.96,16.92 20.45,16.92 C26.01,16.92 30.67,20.52 31.90,25.37 "
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
         Elo_anchorIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Elo_anchorIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
