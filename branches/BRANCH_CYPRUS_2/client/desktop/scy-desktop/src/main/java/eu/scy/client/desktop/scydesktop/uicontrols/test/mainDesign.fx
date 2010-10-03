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
    public-read var languageSelectorRadioButton: javafx.scene.control.RadioButton;
    public-read var dynamicTypeBackgroundRadioButton: javafx.scene.control.RadioButton;
    public-read var stack: javafx.scene.layout.Stack;
    public-read var imagesRadioButton: javafx.scene.control.RadioButton;
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
        imagesRadioButton = javafx.scene.control.RadioButton {
            layoutX: 419.0
            layoutY: 5.0
            onMouseClicked: imagesRadioButtonOnMouseClicked
            text: "Images"
            toggleGroup: toggleGroup
        };
        dynamicTypeBackgroundRadioButton = javafx.scene.control.RadioButton {
            layoutX: 126.0
            layoutY: 5.0
            onMouseClicked: radioButton3OnMouseClicked
            text: "DynamicTypeBackground"
            toggleGroup: toggleGroup
        };
        languageSelectorRadioButton = javafx.scene.control.RadioButton {
            layoutX: 290.0
            layoutY: 5.0
            onMouseClicked: radioButton2OnMouseClicked
            text: "LanguageSelector"
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
//            createTimeline: function (actual) {
//                null
//            }
        }
    }// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Generated Design Functions">
    public function getDesignRootNodes () : javafx.scene.Node[] {
        [ multiImageButtonRadioButton, languageSelectorRadioButton, dynamicTypeBackgroundRadioButton, stack, imagesRadioButton, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        scene
    }// </editor-fold>//GEN-END:main

   function imagesRadioButtonOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      // image test
      testMultiImageButton.visible = false;
      testDynamicTypeBackground.visible = false;
      testLanguageSelector.visible = false;
      testImages.visible = true;
   }

   function radioButton2OnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      // language selector
      testMultiImageButton.visible = false;
      testDynamicTypeBackground.visible = false;
      testLanguageSelector.visible = true;
      testImages.visible = false;
   }

   function radioButton3OnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      // dynamic background
      testMultiImageButton.visible = false;
      testDynamicTypeBackground.visible = true;
      testLanguageSelector.visible = false;
      testImages.visible = false;
   }

   function radioButtonOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      // multi image button
      testMultiImageButton.visible = true;
      testDynamicTypeBackground.visible = false;
      testLanguageSelector.visible = false;
      testImages.visible = false;
   }

   public def testMultiImageButton = TestMultiImageButton {
         imageLocation: "file:images/"
         visible: false;
      }
   public def testLanguageSelector = TestLanguageSelector {
         visible: false
      }
   public def testDynamicTypeBackground = TestDynamicTypeBackground {
         visible: false
      }
   public def testImages = TestImages {
         visible: false
      }
}
