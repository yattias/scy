/*
 * ScyEdge.fx
 *
 * Created on 27.03.2009, 12:49:46
 */

package eu.scy.scywindows;

import eu.scy.scywindows.ScyEdge;
import eu.scy.scywindows.ScyWindow;
import java.lang.Math;
import javafx.scene.shape.Line;

/**
 * @author pg
 */


public class ScyEdge extends Line{

//    public var node1: ScyWindow;
//    public var node2: ScyWindow;
    public var node1: ScyWindow on replace{repaint()};
    public var node2: ScyWindow on replace{repaint()};
    
    def node1CenterX:Number = bind (node1.translateX + node1.width / 2);
    def node1CenterY:Number = bind (node1.translateY + node1.height / 2);
    def node2CenterX:Number = bind (node2.translateX + node2.width / 2);
    def node2CenterY:Number = bind (node2.translateY + node2.height / 2);

    public var scyEdgeLayer: ScyEdge; 
    public var gradient:Number;
//    Calculating atan costs performance
    public var gradientAngle:Number;

    //stupid workarround:
    var test1 = bind node1.translateX on replace { repaint() };
    var test2 = bind node1.translateY on replace { repaint() };
    var test3 = bind node2.translateX on replace { repaint() };
    var test4 = bind node2.translateY on replace { repaint() };
    postinit{
        repaint();
    }

    /**
     * repaints the whole edge, used to snap it into nodes & repainting while
     * moving nodes
     */
    public function repaint():Void {


//        //this doesnt cover all cases
//        //buggy
//        this.stroke = Color.BLACK;
//        if (startX - endX !=0){
//        gradient = (startY - endY)/(startX - endX);
//        gradientAngle = Math.atan(gradient);
//        }
//
//        //case1: perhaps gradient couldnt be calculated, line could be orthogonal
//        if(node1CenterX==node2CenterX){
//           //node 2 superior
//           if (node1.translateY>node2.translateY+node2.height){
//                startX = node1.translateX + node1.width/2;
//                startY = node1.translateY;
//                endX = node2.translateX + node2.width/2;
//                endY = node2.translateY + node2.height;
//           } else{
//               //node1 superior
//               if (node1.translateY+node1.height<node2.translateY){
//                    startX = node1.translateX + node1.width/2;
//                    startY = node1.translateY+ node1.height;
//                    endX = node2.translateX + node2.width/2;
//                    endY = node2.translateY;
//               }
//               else {
//                 this.stroke = Color.TRANSPARENT}
//
//               }
//
//        }
//
//        //case2: node2 below node1
//        if (node1.translateY+node1.height < node2.translateY){
//            //node1 left
//            if(node1.translateX<node2.translateX){
//
//                if(Math.abs(gradient) > 1){
//                    startX = node1.translateX + node1.width/2 + Math.abs(node1.height/(2*gradient));
//                    startY = node1.translateY + node1.height;
//                    endX = node2.translateX + node2.width/2 - Math.abs(node2.height/(2*gradient));
//                    endY = node2.translateY;
//                }
//                else {
//                    startX = node1.translateX + node1.width;
//                    startY = node1.translateY + node1.height/2 + Math.abs(gradient * node1.width/2);
//                    endX = node2.translateX;
//                    endY = node2.translateY + node2.height/2 - Math.abs(gradient * node2.width/2);
//                }
//
//
//            } else {
//                //node1 right
//                if(node1.translateX>node2.translateX){
//                    if(Math.abs(gradient) > 1){
//                    startX = node1.translateX + (node1.width/2) - Math.abs(node1.height/(2*gradient));
//                    startY = node1.translateY + node1.height;
//                    endX = node1.translateX + (node1.width/2) + Math.abs(node2.height/(2*gradient));
//                    endY = node2.translateY;
//                }
//                else {
//                    startX = node1.translateX;
//                    startY = node1.translateY + (node1.height/2) + Math.abs(node1.width*gradient/2);
//                    endX = node2.translateX + node2.width;
//                    endY = node2.translateY + (node2.height/2) - Math.abs(node2.width*gradient/2);
//                }
//
//                } else {
//                    stroke = Color.TRANSPARENT;
//                }
//
//
//            }
//
//
//        }
//
//        //case3: node1 below node2
//        if (node2.translateY+node2.height < node1.translateY){
//            //node1 left
//            if(node1.translateX<node2.translateX){
//
//                if(Math.abs(gradient) > 1){
//                    startX = node1.translateX + node1.width/2 + Math.abs(node1.height/(2*gradient));
//                    startY = node1.translateY;
//                    endX = node2.translateX + node2.width/2 - Math.abs(node2.height/(2*gradient));
//                    endY = node2.translateY + node2.height;
//                }
//                else {
//                    startX = node1.translateX + node1.width;
//                    startY = node1.translateY + node1.height/2 + Math.abs(gradient * node1.width/2);
//                    endX = node2.translateX;
//                    endY = node2.translateY + node2.height/2 - Math.abs(gradient * node2.width/2);
//                }
//
//
//            } else {
//                //node1 right
//                if(node1.translateX>node2.translateX){
//                    if(Math.abs(gradient) > 1){
//                    startX = node1.translateX + (node1.width/2) - Math.abs(node1.height/(2*gradient));
//                    startY = node1.translateY;
//                    endX = node1.translateX + (node1.width/2) + Math.abs(node2.height/(2*gradient));
//                    endY = node2.translateY + node2.height;
//                }
//                else {
//                    startX = node1.translateX;
//                    startY = node1.translateY + (node1.height/2) - Math.abs(node1.width*gradient/2);
//                    endX = node2.translateX + node2.width;
//                    endY = node2.translateY + (node2.height/2) + Math.abs(node2.width*gradient/2);
//                }
//
//                } else {
//                    stroke = Color.TRANSPARENT;
//                }
//
//
//            }
//
//}
// OLD WORKING CODE IS BELOW:
        
        if (startX - endX !=0){
        gradient = (startY - endY)/(startX - endX);
        gradientAngle = Math.atan(gradient);
        }
        
        //case1: perhaps gradient couldnt be calculated, line could be orthogonal
        if (Math.abs(node1.translateX-node2.translateX)<Math.max(node1.width, node2.width)){
                //start at node1, and at node2
            //node 2 superior
            if (node1.translateY > node2.translateY){
//                if (node2.translateY+node2.height<=node2.translateY){
                startX = node1.translateX + node1.width / 2;
                startY = node1.translateY;
                endX = node2.translateX + node2.width / 2;
                endY = node2.translateY + node2.height;

//                }

            } else {
                //node 1 superior
//                if(node1.translateY+node1.height <= node2.translateY){

                startX = node1.translateX + node1.width / 2;
                startY = node1.translateY + node1.height;
                endX = node2.translateX + node2.width / 2;
                endY = node2.translateY;

//                }
            }


        } else {
            //node 1 left;
            if (node1.translateX < node2.translateX){
                startX = node1.translateX + node1.width;
                startY = node1.translateY + node1.height/2;
                endX = node2.translateX;
                endY = node2.translateY + node2.height/2;
            }
            //node 1 right;
            else{
                startX = node1.translateX;
                startY = node1.translateY + node1.height/2;
                endX = node2.translateX + node2.width;
                endY = node2.translateY +node2.height/2;
            }

        }



     }



}