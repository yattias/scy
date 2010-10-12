/*
 * TestImages.fx
 *
 * Created on 31-mrt-2010, 17:34:19
 */
package eu.scy.client.desktop.scydesktop.uicontrols.test;

/**
 * @author sikken
 */
public class TestImagesDesign {

    public-read var label: javafx.scene.control.Label;//GEN-BEGIN:main
    public-read var imageNameTextBox: javafx.scene.control.TextBox;
    public-read var label2: javafx.scene.control.Label;
    public-read var label3: javafx.scene.control.Label;
    public-read var selectedImageLabel: javafx.scene.control.Label;
    public-read var notSelectedImageLabel: javafx.scene.control.Label;
    public-read var activeBitmap: javafx.scene.image.ImageView;
    public-read var inactiveBitmap: javafx.scene.image.ImageView;
    public-read var activeBitmapSize: javafx.scene.control.Label;
    public-read var inactiveBitmapSize: javafx.scene.control.Label;
    public-read var reloadButton: javafx.scene.control.Button;
    public-read var selectedImageSize: javafx.scene.control.Label;
    public-read var notSelectedImageSize: javafx.scene.control.Label;
    public-read var scaleSlider: javafx.scene.control.Slider;
    public-read var label4: javafx.scene.control.Label;
    public-read var scaleValue: javafx.scene.control.Label;
    public-read var button: javafx.scene.control.Button;
    public-read var image: javafx.scene.image.Image;
    
    public-read var currentState: org.netbeans.javafx.design.DesignState;
    
    // <editor-fold defaultstate="collapsed" desc="Generated Init Block">
    init {
        label = javafx.scene.control.Label {
            layoutX: 6.0
            layoutY: 11.0
            text: "Image name"
        };
        imageNameTextBox = javafx.scene.control.TextBox {
            layoutX: 91.0
            layoutY: 6.0
            width: 188.0
            height: 24.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind imageNameTextBox.width
                height: bind imageNameTextBox.height
            }
            onKeyTyped: imageNameTextBoxOnKeyTyped
        };
        label2 = javafx.scene.control.Label {
            layoutX: 6.0
            layoutY: 67.0
            text: "_act"
        };
        label3 = javafx.scene.control.Label {
            layoutX: 6.0
            layoutY: 108.0
            text: "_inact"
        };
        selectedImageLabel = javafx.scene.control.Label {
            layoutX: 6.0
            layoutY: 150.0
            text: "selected"
        };
        notSelectedImageLabel = javafx.scene.control.Label {
            layoutX: 6.0
            layoutY: 202.0
            text: "not selected"
        };
        activeBitmap = javafx.scene.image.ImageView {
            layoutX: 91.0
            layoutY: 65.0
        };
        inactiveBitmap = javafx.scene.image.ImageView {
            layoutX: 91.0
            layoutY: 108.0
        };
        activeBitmapSize = javafx.scene.control.Label {
            layoutX: 149.0
            layoutY: 67.0
            width: 82.0
            height: 16.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind activeBitmapSize.width
                height: bind activeBitmapSize.height
            }
            text: "0*0"
        };
        inactiveBitmapSize = javafx.scene.control.Label {
            layoutX: 149.0
            layoutY: 108.0
            width: 75.0
            height: 16.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind inactiveBitmapSize.width
                height: bind inactiveBitmapSize.height
            }
            text: "0*0"
        };
        reloadButton = javafx.scene.control.Button {
            layoutX: 219.0
            layoutY: 65.0
            text: "Reload"
        };
        selectedImageSize = javafx.scene.control.Label {
            layoutX: 149.0
            layoutY: 150.0
            width: 130.0
            height: 16.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind selectedImageSize.width
                height: bind selectedImageSize.height
            }
            text: "0*0"
        };
        notSelectedImageSize = javafx.scene.control.Label {
            layoutX: 149.0
            layoutY: 202.0
            width: 130.0
            height: 16.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind notSelectedImageSize.width
                height: bind notSelectedImageSize.height
            }
            text: "0*0"
        };
        scaleSlider = javafx.scene.control.Slider {
            layoutX: 91.0
            layoutY: 259.0
            min: 0.5
            value: 1.0
            max: 2.5
            blockIncrement: 0.1
            showTickLabels: true
            showTickMarks: true
            clickToPosition: false
        };
        label4 = javafx.scene.control.Label {
            layoutX: 4.0
            layoutY: 258.0
            text: "Scale"
        };
        scaleValue = javafx.scene.control.Label {
            layoutX: 248.0
            layoutY: 253.0
            width: 57.0
            height: 21.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind scaleValue.width
                height: bind scaleValue.height
            }
            text: ""
        };
        button = javafx.scene.control.Button {
            layoutX: 53.0
            layoutY: 256.0
            onMouseClicked: buttonOnMouseClicked
            text: "1"
        };
        image = javafx.scene.image.Image {
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
        [ label, imageNameTextBox, label2, label3, selectedImageLabel, notSelectedImageLabel, activeBitmap, inactiveBitmap, activeBitmapSize, inactiveBitmapSize, reloadButton, selectedImageSize, notSelectedImageSize, scaleSlider, label4, scaleValue, button, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }// </editor-fold>//GEN-END:main

   function buttonOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      scaleSlider.value = 1.0;
   }

   public var reloadAction: function(): Void;

   function imageNameTextBoxOnKeyTyped(event: javafx.scene.input.KeyEvent): Void {
      //println("key: {event}");
      if (event.char == "\n") {
         reloadAction();
      }
   }

}
