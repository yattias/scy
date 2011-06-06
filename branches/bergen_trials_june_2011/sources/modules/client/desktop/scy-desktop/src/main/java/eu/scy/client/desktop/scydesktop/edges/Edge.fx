/*
 * Edge.fx
 *
 * Created on 08.01.2010, 11:53:28
 */
package eu.scy.client.desktop.scydesktop.edges;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import javafx.scene.shape.Line;
import javafx.util.Math;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;

/**
 * @author pg, lars
 */
public class Edge extends CustomNode {

    public-init var manager: EdgesManager;
    public-init var start:ScyWindow;
    public-init var end:ScyWindow;
    public-init var text:String;
    //public-init var opacity = 0.3;
    var nodes:Node[];
    public-read var line:Line = Line {
            startX: bind (start as StandardScyWindow).layoutX + (start.width/2);
            startY: bind (start as StandardScyWindow).layoutY + (start.height/2);
            endX:  (end as StandardScyWindow).layoutX + (end.width / 2);
            endY:  (end as StandardScyWindow).layoutY + (end.height / 2);
            strokeWidth: 3.0;
            //opacity: bind opacity;
             stroke: start.windowColorScheme.mainColor;
    }
    var watchmenStartX = bind line.startX on replace { update() };
    var watchmenStartY = bind line.startY on replace { update() };
    //var watchmenEndX = bind line.endX on replace { update() };
    //var watchmenEndY = bind line.endY on replace { update() };
    var arrow:Arrow = Arrow {
            color: end.windowColorScheme.mainColor;
            //opacity: 0.3;
    }
    def label: EdgeLabel = EdgeLabel {
                edge: this;
                labelText: bind text;
                opacity:0.2
                onMouseEntered: function (e: MouseEvent) {
                    Timeline {
                        keyFrames: [at (0.5s) {label.opacity => 1.0 tween Interpolator.EASEIN}]
                    }.play();
                }
                onMouseExited: function (e: MouseEvent) {
                    Timeline {
                        keyFrames: [at (0.5s) {label.opacity => 0.2 tween Interpolator.EASEOUT}]
                    }.play();
                }
    }

    function myContains(target:TestBlock, x:Number, y:Number):Boolean {

        if(((x > target.translateX) and ( x < (target.translateX + target.width)))
            and
           (( y > target.translateY) and (y < (target.translateY + target.height)))) {
               return true
           }
        return false;
    }


   function approxCut(repeats:Integer, target:StandardScyWindow, sX:Number, sY:Number, eX:Number, eY:Number):Number[] {
           var tester:TestBlock = TestBlock {
               translateX: target.layoutX-19;
               translateY: target.layoutY-20;
               width: target.width+37;
               height: target.height+45;
           }
            var startX = sX;
            var startY = sY;
            var endX = eX;
            var endY = eY;
            var centerX:Number;
            var centerY:Number;
            for(i in [1..repeats]) {
                    //first: calculate center
                    centerX = (startX + endX) / 2;
                    centerY = (startY + endY) / 2;
                    //second: check if inside
                    if(myContains(tester, centerX, centerY)) {
                        //inside
                        endX = centerX;
                        endY = centerY;
                    }
                    else {
                        //ouside
                        startX = centerX;
                        startY = centerY;
                    }
                    //insert Text { content: i.toString(); translateX: centerX; translateY: centerY; } into nodes;
            }
            return [centerX, centerY];
    }

    function update() {
            var endCut = approxCut(10, (end as StandardScyWindow), line.startX, line.startY, ((end as StandardScyWindow).layoutX + (end.width / 2)),((end as StandardScyWindow).layoutY + (end.height / 2)));
            var startCut = approxCut(10, (start as StandardScyWindow), line.endX, line.endY, line.startX, line.startY);
            //align the label
            label.x = (endCut[0] + startCut[0]) / 2;
            label.y = (endCut[1] + startCut[1]) / 2;
            //rotate the arrow:
            arrow.translateX = endCut[0]+1;
            arrow.translateY = endCut[1];
            line.endX = endCut[0];
            line.endY = endCut[1];
            var rotate = 0.0;
            if(line.endX > line.startX) {
                rotate = Math.toDegrees(Math.atan((line.endY - line.startY) / (line.endX - line.startX)));
            } else {
                rotate = Math.toDegrees(Math.atan((line.startY- line.endY)/(line.startX-line.endX)))+180;
            }
            arrow.rotate = rotate;
    }

    override protected function create () : Node {
       // this.opacity = 0.3;
        var g:Group = Group {
            content: bind [
                    line,
                    label,
                    arrow,
                    nodes
                    ]
            ;
        }
    }

}
