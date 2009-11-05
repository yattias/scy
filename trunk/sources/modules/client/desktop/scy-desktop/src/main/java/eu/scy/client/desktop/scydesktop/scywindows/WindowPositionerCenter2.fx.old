/*
 * WindowPositionerCenter2.fx
 *
 * Created on 25-mrt-2009, 16:51:40
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import java.lang.Math;

/**
 * @author sikkenj
 */

public class WindowPositionerCenter2 extends WindowPositioner{
    public override var width = 200.0 on replace {
        calculateInternals()
    };
    public override var height = 200.0 on replace {
        calculateInternals()
    };

    var centerX = 0.0;
    var centerY = 0.0;
    var xMin = 0.0;
    var xMax = 0.0;
    var yMin = 0.0;
    var yMax = 0.0;
    var centerWidth = 0.0;
    var outsideWidth = 0.0;
    var centerHeight = 0.0;
    var outsideHeight = 0.0;

    var topLeftAngle = 0.0;
    var topRightAngle = 0.0;
    var bottomRightAngle = 0.0;
    var bottomLeftAngle = 0.0;

    def horizontalInterWindowSpace = 20.0;
    def verticalInterWindowSpace = 30.0;
    def topCompensation = 20;
    def rightCompensation = 10;

    function calculateInternals(){
        var useWidth = width - rightCompensation;
        var useHeight = height - topCompensation;
        centerX = useWidth / 2;
        centerY = topCompensation + useHeight / 2;
        centerWidth = useWidth / 2 - horizontalInterWindowSpace;
        outsideWidth = useWidth / 4 - horizontalInterWindowSpace;
        centerHeight = useHeight / 2 - verticalInterWindowSpace;
        outsideHeight = useHeight / 4 - verticalInterWindowSpace;
        var xBorder = useWidth / 8;
        xMin = xBorder;
        xMax = useWidth - xBorder;
        var yBorder = useHeight / 8;
        yMin = topCompensation + yBorder;
        yMax = topCompensation + useHeight - yBorder;

        var angle = Math.atan2(yMax - yMin,xMax - xMin);
        topLeftAngle = -Math .PI + angle;
        topRightAngle = -angle;
        bottomRightAngle = angle;
        bottomLeftAngle = -topLeftAngle;

//        println("width:{width}, height:{height}, xMin:{xMin}, xMax:{xMax}, yMin:{yMin}, yMax:{yMax}");
//        println("centerWidth:{centerWidth}, centerHeight:{centerHeight}, outsideWidth:{outsideWidth}, outsideHeight:{outsideHeight}");
//        println("topLeftAngle:{topLeftAngle},topRightAngle:{topRightAngle},bottomRightAngle:{bottomRightAngle},bottomLeftAngle:{bottomLeftAngle},");
    }

    public override function clearWindows():Void{

    }


    public override function setCenterWindow(window:ScyWindow){
        positionWindow(window,centerX,centerY,centerWidth, centerHeight);
    }

    public override function addLinkedWindow(window:ScyWindow, direction:Number){
        var x = xMin;
        var y = yMin;
        if (direction >= topLeftAngle and direction <= topRightAngle){
            // at the top
            x = centerX + (centerY - yMin) * - Math.tan(Math.PI   /   2   -   direction);
            y = yMin ;
        }
        else if (direction > topRightAngle and direction <= bottomRightAngle){
            // at the right
            x = xMax;
            y = centerY + (centerX - xMax) * Math.tan(direction);
        }
        else if (direction > bottomRightAngle and direction <= bottomLeftAngle){
            // at the bottom
            x = centerX + (centerY - yMax) * - Math.tan(Math.PI   /   2   -   direction);
            y = yMax
        }
        else {
            // at the left
            x = xMin;
            y = centerY + (centerX - xMin) * Math.tan(direction);
        }
        positionWindow(window,x,y,outsideWidth,outsideHeight);
    }

    public override function addOtherWindow(window:ScyWindow){
        var currentDirection = calculateDirection(window);
        addLinkedWindow(window,currentDirection);
    }

    public override function setFixedWindows(fixedWindows:ScyWindow[]):Void{
    }

    public override function positionWindows():Void{

    }

    function positionWindow(window:ScyWindow,x:Number,y:Number,w:Number,h:Number){
        var realWidth = Math.max(w,window.minimumWidth);
        var realHeight = Math.max(h,window.minimumHeight);
        window.translateX = x - realWidth / 2;
        window.translateY = y - realHeight / 2;
        window.openWindow(realWidth, realHeight);
//        println("positioned window {window.title} at ({window.translateX},{window.translateY}), sized ({window.width},{window.height})");
    }

    function calculateDirection(window:ScyWindow):Number{
        var windowCenterX = window.translateX+window.width/2;
        var windowCenterY = window.translateY+window.height/2;
        return Math.atan2(windowCenterY-centerY , windowCenterX-centerX);
    }

}
