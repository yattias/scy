/*
 * WindowPositionerCenterMinimized.fx
 *
 * Created on 3-apr-2009, 11:12:00
 */

package eu.scy.scywindows.window_positions;


import eu.scy.scywindows.ScyWindow;
import eu.scy.scywindows.WindowPositioner;
import java.lang.Math;
import javafx.geometry.Rectangle2D;
import javafx.util.Sequences;

/**
 * @author sikkenj
 */

public class WindowPositionerCenterMinimized extends WindowPositioner {
    public override var width = 200.0 on replace {
        calculateInternals()
    };
    public override var height = 200.0 on replace {
        calculateInternals()
    };
//    public override var forbiddenAreas:Rectangle2D[];

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

    var centerWindowPosition:WindowPosition;
    var linkedWindowPositions:WindowPosition[];
    var otherWindowPositions:WindowPosition[];
    var fixedWindows:ScyWindow[];

    def horizontalLeftWindowSpace = 7;
	 def horizontalRightWindowSpace = 13;
	 def verticalAboveWindowSpace = 25.0;
	 def verticalUnderWindowSpace = 10.0;
    def horizontalInterWindowSpace = horizontalLeftWindowSpace + horizontalRightWindowSpace;
    def verticalInterWindowSpace = verticalAboveWindowSpace + verticalUnderWindowSpace;

    def topCompensation = 20;
    def rightCompensation = 10;

    def maxCorrectionTries = 20;
    def maxRandomTries = 50;
    def deltaDirection = Math.PI/100;
    def noIntersection = 10;


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
        centerWindowPosition = null;
        delete linkedWindowPositions;
        delete otherWindowPositions;
        delete fixedWindows;
    }


    public override function setCenterWindow(window:ScyWindow){
       centerWindowPosition = createNewWindowPosition(window);
    }

    public override function addLinkedWindow(window:ScyWindow, direction:Number){
//       println("addLinkedWindow({window.title},{direction})");
       var linkedWindowPosition = createNewWindowPosition(window);
       linkedWindowPosition.preferredDirection=direction;
        insert linkedWindowPosition into linkedWindowPositions;
    }

    public override function addOtherWindow(window:ScyWindow){
//        println("addOtherWindow({window.title})");
        var otherWindowPosition = createNewWindowPosition(window);
        insert otherWindowPosition into otherWindowPositions;
    }

    function createNewWindowPosition(window:ScyWindow):WindowPosition{
       WindowPosition{
            window:window;
            currentDirection:calculateDirection(window);
            x: window.translateX;
            y: window.translateY;
            width: window.width;
            height: window.height;
        };
    }

    public override function setFixedWindows(fixedWindows:ScyWindow[]):Void{
       this.fixedWindows = fixedWindows;
    }

    function isFixedWindow(windowPosition:WindowPosition):Boolean{
       return Sequences.indexOf(fixedWindows, windowPosition.window)>=0;
    }


    public override function positionWindows():Void{
        centerWindowPosition.minimized = false;
        calculateWindowPosition(centerWindowPosition,centerX,centerY,centerWidth, centerHeight);
        for (windowPosition in linkedWindowPositions){
            windowPosition.minimized = true;
            calculateWindowPosition(windowPosition,windowPosition.preferredDirection,outsideWidth,outsideHeight);
        }
        for (windowPosition in otherWindowPositions){
            windowPosition.preferredDirection = calculateDirection(windowPosition.window);
            windowPosition.minimized = true;
            calculateWindowPosition(windowPosition,windowPosition.preferredDirection,outsideWidth,outsideHeight);
        }
        correctWindowPositions();
        applyWindowPositions();
    }

    function calculateWindowPosition(windowPosition:WindowPosition, direction:Number,w:Number,h:Number){
       if (isFixedWindow(windowPosition)){
         calculateWindowPosition(windowPosition,windowPosition.x,windowPosition.y,windowPosition.width,windowPosition.height);
       }
       else{
        var x = xMin;
        var y = yMin;
        if (direction >= topLeftAngle and direction <= topRightAngle){
            // at the top
               x = centerX - (centerY - yMin) * Math.tan(Math.PI   /   2   -   direction);
            y = yMin ;
        }
        else if (direction > topRightAngle and direction <= bottomRightAngle){
            // at the right
            x = xMax;
            y = centerY - (centerX - xMax) * Math.tan(direction);
        }
        else if (direction > bottomRightAngle and direction <= bottomLeftAngle){
            // at the bottom
               x = centerX - (centerY - yMax) * Math.tan(Math.PI   /   2   -   direction);
            y = yMax
        }
        else {
            // at the left
            x = xMin;
            y = centerY - (centerX - xMin) * Math.tan(direction);
        };
        windowPosition.useDirection = direction;
           var checkDirection =  Math.atan2(y-centerY , x-centerX);
   //        println("result angle diff {direction-checkDirection}");
        calculateWindowPosition(windowPosition,x,y,w,h);
    }
    }

    function calculateWindowPosition(windowPosition:WindowPosition, x:Number,y:Number,w:Number,h:Number){
       var rectHeight :Number;
       if (isFixedWindow(windowPosition)){
           windowPosition.width = windowPosition.window.width;
           windowPosition.height = windowPosition.window.height;
           windowPosition.x = windowPosition.window.translateX;
           windowPosition.y = windowPosition.window.translateX;
           rectHeight = windowPosition.height;
       }
       else{
           windowPosition.width = Math.max(w,windowPosition.window.minimumWidth);
           windowPosition.height = Math.max(h,windowPosition.window.minimumHeight);
           windowPosition.x = x - windowPosition.width/2;
           windowPosition.y = y - windowPosition.height/2;
           rectHeight = windowPosition.height;
           if (windowPosition.minimized and (windowPosition.window.isMinimized or windowPosition.window.isClosed)){
              var closedHeight = windowPosition.window.closedHeight;
               rectHeight = closedHeight;
           }
       }
        windowPosition.rectangle = Rectangle2D{
            minX:windowPosition.x-horizontalLeftWindowSpace
            minY:windowPosition.y-verticalAboveWindowSpace
            width:windowPosition.width+horizontalInterWindowSpace
            height:rectHeight+verticalInterWindowSpace
        }
    }

    function calculateDirection(window:ScyWindow):Number{
        var windowCenterX = window.translateX+window.width/2;
        var windowCenterY = window.translateY+window.height/2;
        return Math.atan2(windowCenterY-centerY , windowCenterX-centerX);
    }

    function correctWindowPositions(){
        var usedRects:Rectangle2D[];
        for (node in forbiddenNodes){
            insert node.boundsInParent into usedRects;
            };
        insert centerWindowPosition.rectangle into usedRects;
        for (windowPosition in linkedWindowPositions){
            correctWindowPosition(usedRects,windowPosition);
            insert windowPosition.rectangle into usedRects;
        }
        for (windowPosition in otherWindowPositions){
            correctWindowPosition(usedRects,windowPosition);
            insert windowPosition.rectangle into usedRects;
        }
    }

    function correctWindowPosition(usedRects:Rectangle2D[],windowPosition:WindowPosition):Void{
       if (isFixedWindow(windowPosition)){
          return;
       }

        windowPosition.intersection = calculateIntersection(usedRects,windowPosition.rectangle);
        if (windowPosition.intersection<=noIntersection){
//            println("no intersection for {windowPosition.window.title}");
            return;
        }
        // try near by
        var bestNearByPosition = findBestNearByPosition(usedRects,windowPosition);
        if (bestNearByPosition.intersection<=noIntersection){
//            println("improved intersection of {windowPosition.window.title} form {windowPosition.intersection} to nearby {bestNearByPosition.intersection} ({windowPosition.usedDirection} -> {bestNearByPosition.usedDirection} in {bestNearByPosition.correctionCount} tries)");
            windowPosition.copyFrom(bestNearByPosition);
            return;
        }
        var bestFoundPosition = WindowPosition.clone(bestNearByPosition);
        // try on the current direction
        var bestNearCurrentPosition = calculateNewWindowPosition(usedRects,windowPosition,windowPosition.currentDirection);
        if (bestNearCurrentPosition.intersection<=noIntersection){
//            println("no intersection for {windowPosition.window.title}");
            windowPosition.copyFrom(bestNearCurrentPosition);
            return;
        }
        // try near by the current position
        bestNearCurrentPosition = findBestNearByPosition(usedRects,bestNearCurrentPosition);
        if (bestNearCurrentPosition.intersection<=noIntersection){
//            println("improved intersection of {windowPosition.window.title} form {windowPosition.intersection} to nearby {bestNearByPosition.intersection} ({windowPosition.usedDirection} -> {bestNearByPosition.usedDirection} in {bestNearByPosition.correctionCount} tries)");
            windowPosition.copyFrom(bestNearCurrentPosition);
            return;
        }
        if (bestNearByPosition.intersection>bestNearCurrentPosition.intersection){
           bestFoundPosition = WindowPosition.clone(bestNearCurrentPosition);
        }
        // try the random way
        var bestPosition = findBestRandomPosition(usedRects,bestFoundPosition);
//        println("improved intersection of {windowPosition.window.title} form {windowPosition.intersection} to random {bestPosition.intersection} ({windowPosition.usedDirection} -> {bestPosition.usedDirection} in {bestPosition.correctionCount} tries)");
        windowPosition.copyFrom(bestPosition);
        if (windowPosition.intersection>noIntersection){
            println("failed to position {windowPosition.window.title}, insection {windowPosition.intersection}");
        }
    }

    function findBestNearByPosition(usedRects:Rectangle2D[],windowPosition:WindowPosition):WindowPosition{
        var bestNearByPosition = windowPosition;
        var nearByMinusPosition = calculateNewWindowPosition(usedRects,windowPosition,windowPosition.useDirection-deltaDirection);
        var nearByPlusPosition = calculateNewWindowPosition(usedRects,windowPosition,windowPosition.useDirection+deltaDirection);
        if (nearByMinusPosition.intersection>bestNearByPosition.intersection and nearByPlusPosition.intersection>bestNearByPosition.intersection){
            return bestNearByPosition;
        }

        var deltaFactor:Number = 1.0;
        bestNearByPosition = nearByPlusPosition;
        if (nearByMinusPosition.intersection<nearByPlusPosition.intersection){
            deltaFactor = -1.0;
         bestNearByPosition = nearByMinusPosition;
       }
       var correctCount = maxCorrectionTries;
       while (bestNearByPosition.intersection>noIntersection and correctCount>0){
           var newNearByPosition = calculateNewWindowPosition(usedRects,bestNearByPosition,bestNearByPosition.useDirection+deltaFactor*deltaDirection);
           if (newNearByPosition.intersection>bestNearByPosition.intersection){
               return bestNearByPosition;
           }
           bestNearByPosition = newNearByPosition;
           --correctCount;
       }
       return bestNearByPosition;
    }

    function findBestRandomPosition(usedRects:Rectangle2D[],windowPosition:WindowPosition):WindowPosition{
       var randomCount = maxRandomTries;
       var bestRandomPosition = windowPosition;
       while (bestRandomPosition.intersection>noIntersection and randomCount>0){
           var newRandomPosition = calculateNewWindowPosition(usedRects,bestRandomPosition,-Math.PI+Math.random()*2*Math.PI);
           if (newRandomPosition.intersection<bestRandomPosition.intersection){
               bestRandomPosition = newRandomPosition;
           }
           --randomCount;
       }
       return bestRandomPosition;
    }



    function calculateNewWindowPosition(usedRects:Rectangle2D[],windowPosition:WindowPosition,direction:Number):WindowPosition{
        var newWindowPosition = WindowPosition.clone(windowPosition);
        calculateWindowPosition(newWindowPosition,direction,newWindowPosition.width,newWindowPosition.height);
        newWindowPosition.intersection = calculateIntersection(usedRects,newWindowPosition.rectangle);
        ++newWindowPosition.correctionCount;
        return newWindowPosition;
    }


    function calculateIntersection(usedRects:Rectangle2D[],windowRect:Rectangle2D):Number{
        var maximumIntersection = 0.0;
        for (rect in usedRects){
            var intersection = calculateRectangleIntersection(rect,windowRect);
            if (intersection>maximumIntersection)
                maximumIntersection = intersection;
        }
        return maximumIntersection;
    }

    function calculateRectangleIntersection(rect1:Rectangle2D,rect2:Rectangle2D):Number{
        if (rect1.intersects(rect2)){
            var xLength = calculateIntersectionLength(rect1.minX,rect1.maxX,rect2.minX,rect2.maxX);
            var yLength = calculateIntersectionLength(rect1.minY,rect1.maxY,rect2.minY,rect2.maxY);
            return xLength*yLength;
        }
        return 0.0;
    }

    function calculateIntersectionLength(min1:Number,max1:Number,min2:Number,max2:Number):Number{
        return Math.min(max1, max2)-Math.max(min1,min2);
    }


    function applyWindowPositions(){
        applyWindowPosition(centerWindowPosition,false);
        for (windowPosition in linkedWindowPositions){
            applyWindowPosition(windowPosition,true);
        }
        for (windowPosition in otherWindowPositions){
            applyWindowPosition(windowPosition,true);
        }
    }

    function applyWindowPosition(windowPosition:WindowPosition, minimize:Boolean){
       windowPosition.currentDirection = windowPosition.useDirection;
       if (isFixedWindow(windowPosition)){
          return;
       }
       windowPosition.window.translateX = windowPosition.x;
       windowPosition.window.translateY = windowPosition.y;
       if (not windowPosition.minimized){
           windowPosition.window.openWindow(windowPosition.window.width, windowPosition.window.height);
       }
       else {
          //windowPosition.window.setMinimized(true);
       }

           windowPosition.window.width = windowPosition.width;
           windowPosition.window.height = windowPosition.height;
//       }
       //windowPosition.window.setMinimized(windowPosition.minimized);
//		 if (windowPosition.window instanceof AnchorWindow){
//			var anchorWindow = windowPosition.window as AnchorWindow;
//			anchorWindow.rect = windowPosition.rectangle;
//			var offsetPoint = anchorWindow.localToParent(0, 0);
//			anchorWindow.xRectOffset = offsetPoint.x;
//			anchorWindow.yRectOffset = offsetPoint.y;
		 }
    }
