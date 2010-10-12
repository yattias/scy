/*
 * TestMultiImageButtonDesign.fx
 *
 * Created on 11-mrt-2010, 10:40:37
 */

package eu.scy.client.desktop.scydesktop.uicontrols.test;

/**
 * @author sikken
 */
public class TestMultiImageButtonDesign {

    public-read var label: javafx.scene.control.Label;//GEN-BEGIN:main
    public-read var imageNameTextBox: javafx.scene.control.TextBox;
    public-read var label2: javafx.scene.control.Label;
    public-read var disabledCheckbox: javafx.scene.control.CheckBox;
    public-read var label3: javafx.scene.control.Label;
    public-read var mouseOutImageView: javafx.scene.image.ImageView;
    public-read var label4: javafx.scene.control.Label;
    public-read var mouseOverImageView: javafx.scene.image.ImageView;
    public-read var label5: javafx.scene.control.Label;
    public-read var pressedOverImageView: javafx.scene.image.ImageView;
    public-read var label6: javafx.scene.control.Label;
    public-read var pressedOutImageView: javafx.scene.image.ImageView;
    public-read var label7: javafx.scene.control.Label;
    public-read var disabledImageView: javafx.scene.image.ImageView;
    public-read var reloadButton: javafx.scene.control.Button;
    public-read var clickedLabel: javafx.scene.control.Label;
    public-read var turnedOnCheckbox: javafx.scene.control.CheckBox;
    
    public-read var currentState: org.netbeans.javafx.design.DesignState;
    
    // <editor-fold defaultstate="collapsed" desc="Generated Init Block">
    init {
        label = javafx.scene.control.Label {
            layoutX: 6.0
            layoutY: 7.0
            text: "Image name"
        };
        imageNameTextBox = javafx.scene.control.TextBox {
            layoutX: 99.0
            layoutY: 5.0
            width: 196.0
            height: 24.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind imageNameTextBox.width
                height: bind imageNameTextBox.height
            }
        };
        label2 = javafx.scene.control.Label {
            layoutX: 16.0
            layoutY: 251.0
            text: "State"
        };
        disabledCheckbox = javafx.scene.control.CheckBox {
            layoutX: 99.0
            layoutY: 250.0
            text: "disabled"
        };
        label3 = javafx.scene.control.Label {
            layoutX: 6.0
            layoutY: 53.0
            text: "_mouseOut"
        };
        mouseOutImageView = javafx.scene.image.ImageView {
            layoutX: 99.0
            layoutY: 53.0
        };
        label4 = javafx.scene.control.Label {
            layoutX: 5.0
            layoutY: 93.0
            text: "_mouseOver"
        };
        mouseOverImageView = javafx.scene.image.ImageView {
            layoutX: 99.0
            layoutY: 91.0
        };
        label5 = javafx.scene.control.Label {
            layoutX: 8.0
            layoutY: 132.0
            text: "_pressedOver"
        };
        pressedOverImageView = javafx.scene.image.ImageView {
            layoutX: 99.0
            layoutY: 130.0
        };
        label6 = javafx.scene.control.Label {
            layoutX: 10.0
            layoutY: 173.0
            text: "_pressedOut"
        };
        pressedOutImageView = javafx.scene.image.ImageView {
            layoutX: 99.0
            layoutY: 171.0
        };
        label7 = javafx.scene.control.Label {
            layoutX: 9.0
            layoutY: 212.0
            text: "_disabled"
        };
        disabledImageView = javafx.scene.image.ImageView {
            layoutX: 99.0
            layoutY: 212.0
        };
        reloadButton = javafx.scene.control.Button {
            layoutX: 235.0
            layoutY: 51.0
            text: "Reload"
        };
        clickedLabel = javafx.scene.control.Label {
            opacity: 1.0
            layoutX: 241.0
            layoutY: 157.0
            text: "Clicked"
        };
        turnedOnCheckbox = javafx.scene.control.CheckBox {
            visible: true
            layoutX: 226.0
            layoutY: 250.0
            text: "turned on"
        };
        
        currentState = org.netbeans.javafx.design.DesignState {
            names: [ ]
            stateChangeType: org.netbeans.javafx.design.DesignStateChangeType.PAUSE_AND_PLAY_FROM_START
//            createTimeline: function (actual) {
//                null
//            }
        }
    }// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Generated Design Functions">
    public function getDesignRootNodes () : javafx.scene.Node[] {
        [ label, imageNameTextBox, label2, disabledCheckbox, label3, mouseOutImageView, label4, mouseOverImageView, label5, pressedOverImageView, label6, pressedOutImageView, label7, disabledImageView, reloadButton, clickedLabel, turnedOnCheckbox, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }// </editor-fold>//GEN-END:main

}
