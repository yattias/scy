/*
 * mainDesign.fx
 *
 * Created on 16-mrt-2010, 10:21:02
 */
package eu.scy.client.desktop.scydesktop.uicontrols.test;

/**
 * @author sikken
 */
public class mainDesign {

    public-read var multiImageButtonRadioButton: javafx.scene.control.RadioButton;//GEN-BEGIN:main
    public-read var backgroundRadioButton: javafx.scene.control.RadioButton;
    public-read var dynamicTypeBackgroundRadioButton: javafx.scene.control.RadioButton;
    public-read var stack: javafx.scene.layout.Stack;
    public-read var scene: javafx.scene.Scene;
    public-read var toggleGroup: javafx.scene.control.ToggleGroup;
    
    public-read var currentState: org.netbeans.javafx.design.DesignState;
    
    // <editor-fold defaultstate="collapsed" desc="Generated Init Block">
    init {
        stack = javafx.scene.layout.Stack {
            layoutX: 0.0
            layoutY: 35.0
            width: 480.0
            height: 285.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind stack.width
                height: bind stack.height
            }
            content: [ ]
            nodeHPos: javafx.geometry.HPos.LEFT
            nodeVPos: javafx.geometry.VPos.TOP
        };
        toggleGroup = javafx.scene.control.ToggleGroup {
        };
        dynamicTypeBackgroundRadioButton = javafx.scene.control.RadioButton {
            layoutX: 268.0
            layoutY: 5.0
            onMouseClicked: radioButton3OnMouseClicked
            text: "DynamicTypeBackground"
            toggleGroup: toggleGroup
        };
        backgroundRadioButton = javafx.scene.control.RadioButton {
            layoutX: 155.0
            layoutY: 5.0
            onMouseClicked: radioButton2OnMouseClicked
            text: "Background"
            toggleGroup: toggleGroup
        };
        multiImageButtonRadioButton = javafx.scene.control.RadioButton {
            layoutX: 6.0
            layoutY: 5.0
            onMouseClicked: radioButtonOnMouseClicked
            text: "MultiImageButton"
            toggleGroup: toggleGroup
        };
        scene = javafx.scene.Scene {
            width: 480.0
            height: 320.0
            content: javafx.scene.layout.Panel {
                content: getDesignRootNodes ()
            }
        };
        
        currentState = org.netbeans.javafx.design.DesignState {
            names: [ ]
            stateChangeType: org.netbeans.javafx.design.DesignStateChangeType.PAUSE_AND_PLAY_FROM_START
            createTimeline: function (actual) {
                null
            }
        }
    }// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Generated Design Functions">
    public function getDesignRootNodes () : javafx.scene.Node[] {
        [ multiImageButtonRadioButton, backgroundRadioButton, dynamicTypeBackgroundRadioButton, stack, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        scene
    }// </editor-fold>//GEN-END:main

   function radioButton2OnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      testMultiImageButton.visible = false;
      testDynamicTypeBackground.visible = false;
         }

   function radioButton3OnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      testMultiImageButton.visible = false;
      testDynamicTypeBackground.visible = true;
   }

   function radioButtonOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      testMultiImageButton.visible = true;
      testDynamicTypeBackground.visible = false;
   }

   public def testMultiImageButton = TestMultiImageButton {
         imageLocation: "file:images/"
         visible:false;
      }
   public def testDynamicTypeBackground = TestDynamicTypeBackground {
         visible:false
      }
}
