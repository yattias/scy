/*
 * MapWrapper.fx
 *
 * Created on 16.06.2009, 15:11:41
 */

package eu.scy.client.tools.fxformauthor.viewer.element.gps;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

import javafx.ext.swing.SwingComponent;

import javafx.animation.Interpolator;

import javafx.animation.Timeline;

import javafx.scene.input.MouseEvent;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import eu.scy.client.tools.fxformauthor.viewer.element.AbstractElementView;

/**
 * @author pg
 */

public class MapWrapper extends CustomNode {

   public var height = 450;
   public var width = 450;

   public var viewX:Double;
   public var viewY:Double;
   
   public-init var abstractView:AbstractElementView;

    var manager:MapManager = new MapManager();
    var myMap = manager.getMap();


    var myOpacity = 0.0;

    var fade = Timeline {
        keyFrames: [
            at (0s) { this.opacity => 0.0 tween Interpolator.LINEAR} ,
            at (0.5s) { this.opacity => 1.0 tween Interpolator.LINEAR}
        ]
    }
    var goBackButton = Button {
        text: "hide";
        translateX: 45;
        translateY: 5;
        onMouseReleased: function(e:MouseEvent):Void {
            hideMap();
        }

    }

    var centerButton = Button {
        translateX: 105;
        translateY: 5;
        text: "center view";
        onMouseReleased: function(e:MouseEvent):Void {
            centerView(viewX, viewY);
        }
    }

    var zoomOutButton = Button {
        text: "-";
        translateX: 5;
        translateY: 5;
        onMouseReleased: function(e:MouseEvent):Void {
            manager.zoom(manager.getZoom()+1);
            zoomSlider.value = manager.getZoom();
        }
    }

    var zoomInButton = Button {
        text: "+";
        translateX: 5;
        translateY: 165;
        onMouseReleased: function(e:MouseEvent):Void {
            manager.zoom(manager.getZoom()-1);
            zoomSlider.value = manager.getZoom();
        }
    }

    function updateSlider():Void {
        manager.zoom(zoomSlider.value);
    }

    var zoomSlider:Slider = Slider {
            translateX: 15;
            translateY: 30;
            height: 130;
            min: 1,
            max: 15,
            value: 1;
            vertical: true;
            onMouseDragged:function(e:MouseEvent):Void {
                updateSlider();
            }
    }

    /*
    var zoomSlider:SwingSlider = SwingSlider {
            translateX: 15;
            translateY: 30;
            height: 130;
            maximum: 15,
            minimum: 1,
            value: 1;
            vertical: true;
            //use onMouseDragged instead of onMouseReleased to have a live update
            onMouseDragged:function(e:MouseEvent):Void {
                updateSlider();
            }
    }
    */

    var hideControlsButton:Button = Button {
        translateX: 120;
        translateY: 5;
        text: "hide controls";
        onMouseReleased:function(e:MouseEvent):Void {

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

    var centerViewButton:ImageView = ImageView {
        image: Image{
            url: "{__DIR__}back.png";
        }
        rotate: -90;
        translateY: 5;
        translateX: 50;
        onMouseReleased: function(e:MouseEvent):Void {
            centerView(viewX, viewY);
        }
    }

    var mapComponent:SwingComponent;
     public override function create():Node {
        //myMap.setSize(width, height);
        myMap.setPreferredSize(new java.awt.Dimension(width, height));
        mapComponent = SwingComponent.wrap(myMap);
        mapComponent.visible = true;
        this.opacity = 0.0;
         //manager.addPosition(51.427783, 6.800172, "UDE Scy Headquarters");
         var g = Group {
            content: [
                    mapComponent,
                    //goBack,
                    //centerViewButton,
                    centerButton,
                    goBackButton,
                    zoomInButton,
                    zoomOutButton,
                    zoomSlider
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
        abstractView.removeMap();
    }

    public function addPosition(x:Number, y:Number, text:String):Void {
        manager.addPosition(x, y, text);
    }

    public function centerView(x:Number, y:Number):Void {
        manager.centerPosition(x,y);
        this.viewX = x;
        this.viewY = y;
    }

    public function setSize(width:Number, height:Number) {
        myMap.setPreferredSize(new java.awt.Dimension(width, height));
        //manager.setSize(width, height);
        //TODO: HELL THIS SAKKZ!!!
        mapComponent.height = height;
        mapComponent.width = width;
        //println("setting size to {width}|{height}");
    }


}
