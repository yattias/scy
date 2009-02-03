/*
 * ScyRelation.fx
 *
 * Created on 3-feb-2009, 12:43:45
 */

package eu.scy.scywindows.demos.stylebook;

import eu.scy.scywindows.ScyWindow;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

/**
 * @author JoolingenWR
 */

public class ScyRelation extends CustomNode {
    public var name = "";
    public var window1:ScyWindow;
    public var window2:ScyWindow;
    public var color = Color.GREEN;
    public var strokeWidth = 2;

    bound function startX() {
       return if ((window2.translateX+window2.width) > window1.translateX) {
            //center or right
            if ((window2.translateX+window2.width) > window1.translateX+window1.width) {
                //right
                window1.translateX+window1.width;
            } else {
                //center
                window1.translateX+window1.width/2;
            }
        } else {
            //left
            window1.translateX;
        }
    }

    bound function startY() {
        return if (window2.translateX+window2.width > window1.translateX) {
        //center or right
            if (window2.translateX+window2.width > window1.translateX+window1.width) {
                //right
                window1.translateY + window1.height/2;
                } else {
                //center
                    if (window2.translateY>window1.translateY) {
                            window1.translateY+window1.height;
                    } else {
                            window1.translateY
                    }
                }
        } else {
        //left
            window1.translateY + window1.height/2;
      }
    }

    bound function endX(){
        return window2.translateX + window2.width / 2;
    }
    bound function endY(){
        return if (startY() > window2.translateY) {
                window2.translateY+window2.height;
            } else {
                window2.translateY;
        }
    }

    public override function create(): Node {
        return Group {
            content: [
                Line{
                    startX: bind startX(),
                    startY: bind startY(),
                    endX: bind endX(),
                    endY: bind endY(),
                    strokeWidth: strokeWidth,
                    stroke: color
                }
                Text {
                    font: Font {
                        size: 12

                    }
                    x: bind (window1.translateX+window2.translateX+window2.width/2)/2,
                    y: bind (window1.translateY+window2.translateY+window2.height/2)/2,
                    content: name,
                    fill: color
                }
            ]
        }
    }
}
