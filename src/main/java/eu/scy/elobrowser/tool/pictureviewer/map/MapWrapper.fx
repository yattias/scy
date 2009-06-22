/*
 * MapWrapper.fx
 *
 * Created on 16.06.2009, 15:11:41
 */

package eu.scy.elobrowser.tool.pictureviewer.map;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

import javafx.ext.swing.SwingComponent;

import javafx.animation.Interpolator;

import javafx.animation.Timeline;

import javafx.ext.swing.SwingButton;
import javafx.scene.input.MouseEvent;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author pg
 */

public class MapWrapper extends CustomNode {
//todo: MapManager refactorn: variablen im konstruktor iniitialisieren => done
//hier map anzeigen,
//in viewernode beim anklicken einfaden lassen
//&glücklich sein
//größen / bounds von viewernode klauen
//position + text über bekannte funktionen
//&gut ist
// :)

    // 1. create warapper
    // 2. get bounds
    // 3. change map size / create new map
    // 4. add positions
    // 5. fade in

   public var height = 600;
   public var width = 600;
   
    var manager:MapManager = new MapManager();
    var myMap = manager.getMap();


    var myOpacity = 0.0;

    var fade = Timeline {
        keyFrames: [
            at (0s) { this.opacity => 0.0 tween Interpolator.LINEAR} ,
            at (0.5s) { this.opacity => 1.0 tween Interpolator.LINEAR}
        ]
    }
    var fadeButton = SwingButton {
        text: "hide";
        onMouseReleased: function(e:MouseEvent):Void {
            hideMap();
        }

    }

        var goBack: ImageView = ImageView {
        image: Image{
            url: "{__DIR__}back.png";
        }
        translateX: 5;
        translateY: 5;
        onMouseReleased: function(e:MouseEvent):Void {
            hideMap();
        }
    }

     public override function create():Node {
        myMap.setPreferredSize(new java.awt.Dimension(width, height));

        var mapComponent = SwingComponent.wrap(myMap);
        mapComponent.visible = true;
        this.opacity = 0.0;
         manager.addPosition(51.427783, 6.800172, "UDE Scy Headquarters");
         var g = Group {
            content: [
                    mapComponent,
                    goBack
            ]
        };
        return g;
    }

    public function showMap():Void {
        fade.rate = 1.0;
        fade.play();
        this.disable = false;
    }

    public function hideMap():Void {
        fade.rate = -1.0;
        fade.play();
        this.disable = true;
    }



}
