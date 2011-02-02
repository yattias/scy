/*
 * ScyWindowStyler.fx
 *
 * Created on 31-mrt-2009, 21:28:51
 */

package eu.scy.scywindows;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.paint.Color;
import roolo.elo.api.IELO;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;

/**
 * @author sikkenj
 */

public class ScyWindowStyler {
    public var roolo:Roolo;

    public def drawingType = "scy/drawing";
    public def datasetType = "scy/dataset";
    public def simulationConfigType = "scy/simconfig";
    public def datasetProcessingType = "scy/pds";
    public def imageType = "scy/image";
    public def textType = "scy/text";
    public def mappingType = "scy/mapping";
    public def meloType = "scy/melo";

    public def scyGreen = Color.web("#8db800");
    public def scyPurple = Color.web("#7243db");
    public def scyOrange = Color.web("#ff5400");
    public def scyPink = Color.web("#fb06a2");
    public def scyBlue = Color.web("#0042f1");
    public def scyMagenta = Color.web("#0ea7bf");

    public function getScyColor(type:String):Color{
        if (type==drawingType)
            return scyGreen
        else if (type==datasetType)
            return scyPurple
        else if (type==simulationConfigType)
            return scyPink
        else if (type==mappingType)
            return scyBlue
        else if (type==imageType)
            return scyOrange
        else if (type==meloType)
            return scyOrange
        else if (type==textType)
            return scyGreen
        else
            return scyMagenta;
    }

    public function getScyIconCharacter(type:String):String{
        if (type==drawingType)
            return "D"
          else if (type==datasetType)
            return "V"
        else if (type==simulationConfigType)
            return "S"
        else if (type==datasetProcessingType)
            return "P"
        else if (type==textType)
            return "T"
        else if (type==imageType)
            return "I"
        else if (type==meloType)
            return "I"
        else if (type==mappingType)
            return "M"
      else
            return "?";
    }

    public function getScyColor(uri:URI):Color{
        var type = roolo.extensionManager.getType(uri);
        var scyColor = getScyColor(type);
        return scyColor;
    }

    public function style(window:ScyWindow,uri:URI){
        var type = roolo.extensionManager.getType(uri);
        var color = getScyColor(type);
        var ch = getScyIconCharacter(type);
        window.color = color;
        window.iconCharacter = ch;
//        println("styled {uri} -> {type}, color:{color}, icon:{ch}");
    }

    public function style(window:ScyWindow,elo:IELO){
        style(window,elo.getUri());
    }

}

function run(){

        Stage {
        title : "color test"
        scene: Scene {
            width: 200
            height: 200
            content: [
                Rectangle {
                x: 10, y: 10
                width: 140, height: 90
                fill: Color.web("8db800")
            }

            ]
        }
    }

}

