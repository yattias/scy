/*
 * WindowColorSchemeEditor.fx
 *
 * Created on 14-feb-2011, 12:11:59
 */

package eu.scy.client.desktop.scydesktop.tools.colorscheme;

/**
 * @author SikkenJ
 */
public class WindowColorSchemeEditor {

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:main
    public-read def label: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 13.0
        layoutY: 12.0
        text: "Elo Icons"
    }
    
    def __layoutInfo_eloIconListview: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 379.0
        height: 82.0
    }
    public-read def eloIconListview: javafx.scene.control.ListView = javafx.scene.control.ListView {
        layoutX: 89.0
        layoutY: 0.0
        layoutInfo: __layoutInfo_eloIconListview
        items: [ "Item 1", "Item 2", "Item 3", ]
        vertical: false
    }
    
    public-read def label2: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 12.0
        layoutY: 95.0
        text: "Color schemes"
    }
    
    def __layoutInfo_colorSchemeListview: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 379.0
        height: 75.0
    }
    public-read def colorSchemeListview: javafx.scene.control.ListView = javafx.scene.control.ListView {
        layoutX: 89.0
        layoutY: 89.0
        layoutInfo: __layoutInfo_colorSchemeListview
        items: [ "Item 1", "Item 2", "Item 3", ]
        vertical: false
    }
    
    public-read def label3: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 13.0
        layoutY: 170.0
        text: "Colors"
    }
    
    public-read def label4: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 13.0
        layoutY: 260.0
        text: "Red"
    }
    
    public-read def label5: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 13.0
        layoutY: 280.0
        text: "Green"
    }
    
    public-read def label6: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 13.0
        layoutY: 300.0
        text: "Blue"
    }
    
    public-read def label7: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 13.0
        layoutY: 320.0
        text: "Alpha"
    }
    
    def __layoutInfo_redSlider: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 256.0
        height: 15.0
    }
    public-read def redSlider: javafx.scene.control.Slider = javafx.scene.control.Slider {
        layoutX: 89.0
        layoutY: 262.0
        layoutInfo: __layoutInfo_redSlider
        max: 255.0
    }
    
    def __layoutInfo_redValue: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 32.0
    }
    public-read def redValue: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 410.0
        layoutY: 260.0
        layoutInfo: __layoutInfo_redValue
        text: "Label"
    }
    
    def __layoutInfo_greenSlider: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 256.0
        height: 15.0
    }
    public-read def greenSlider: javafx.scene.control.Slider = javafx.scene.control.Slider {
        layoutX: 89.0
        layoutY: 282.0
        layoutInfo: __layoutInfo_greenSlider
        max: 255.0
    }
    
    public-read def greenValue: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 410.0
        layoutY: 281.0
        text: "Label"
    }
    
    def __layoutInfo_blueSlider: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 256.0
        height: 15.0
    }
    public-read def blueSlider: javafx.scene.control.Slider = javafx.scene.control.Slider {
        layoutX: 89.0
        layoutY: 302.0
        layoutInfo: __layoutInfo_blueSlider
        max: 255.0
    }
    
    public-read def blueValue: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 410.0
        layoutY: 301.0
        text: "Label"
    }
    
    def __layoutInfo_alphaSlider: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 256.0
        height: 15.0
    }
    public-read def alphaSlider: javafx.scene.control.Slider = javafx.scene.control.Slider {
        layoutX: 89.0
        layoutY: 322.0
        layoutInfo: __layoutInfo_alphaSlider
        max: 255.0
    }
    
    public-read def alphaValue: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 410.0
        layoutY: 320.0
        text: "Label"
    }
    
    public-read def redDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 367.0
        layoutY: 260.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 30.0
        height: 15.0
    }
    
    public-read def greenDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 367.0
        layoutY: 280.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 30.0
        height: 15.0
    }
    
    public-read def blueDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 367.0
        layoutY: 300.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 30.0
        height: 15.0
    }
    
    public-read def colorDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 437.0
        layoutY: 260.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 30.0
        height: 75.0
    }
    
    public-read def alphaDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 367.0
        layoutY: 320.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 30.0
        height: 15.0
    }
    
    public-read def mainDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 190.0
        layoutY: 173.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 50.0
        height: 15.0
    }
    
    public-read def mainLightDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 416.0
        layoutY: 173.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 50.0
        height: 15.0
    }
    
    public-read def secondDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 190.0
        layoutY: 193.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 50.0
        height: 15.0
    }
    
    public-read def thirdDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 190.0
        layoutY: 213.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 50.0
        height: 15.0
    }
    
    public-read def backgroundDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 190.0
        layoutY: 233.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 50.0
        height: 15.0
    }
    
    public-read def secondLightDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 416.0
        layoutY: 193.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 50.0
        height: 15.0
    }
    
    public-read def thirdLightDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 416.0
        layoutY: 213.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 50.0
        height: 15.0
    }
    
    public-read def emptyBackgroundDisplay: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 416.0
        layoutY: 233.0
        stroke: javafx.scene.paint.Color.BLACK
        width: 50.0
        height: 15.0
    }
    
    public-read def toggleGroup: javafx.scene.control.ToggleGroup = javafx.scene.control.ToggleGroup {
    }
    
    public-read def emptyBackgroundColorRadioButton: javafx.scene.control.RadioButton = javafx.scene.control.RadioButton {
        layoutX: 281.0
        layoutY: 233.0
        text: "Empty background"
        toggleGroup: toggleGroup
        value: "emptyBackground"
    }
    
    public-read def thirdColorLightRadioButton: javafx.scene.control.RadioButton = javafx.scene.control.RadioButton {
        layoutX: 281.0
        layoutY: 213.0
        text: "Third light"
        toggleGroup: toggleGroup
        value: "thirdLight"
    }
    
    public-read def secondColorLightRadioButton: javafx.scene.control.RadioButton = javafx.scene.control.RadioButton {
        layoutX: 281.0
        layoutY: 193.0
        text: "Second light"
        toggleGroup: toggleGroup
        value: "secondLight"
    }
    
    public-read def BackgroundColorRadioButton: javafx.scene.control.RadioButton = javafx.scene.control.RadioButton {
        layoutX: 89.0
        layoutY: 233.0
        text: "Background"
        toggleGroup: toggleGroup
        value: "background"
    }
    
    public-read def thirdColorRadioButton: javafx.scene.control.RadioButton = javafx.scene.control.RadioButton {
        layoutX: 89.0
        layoutY: 213.0
        text: "Third"
        toggleGroup: toggleGroup
        value: "third"
    }
    
    public-read def secondColorRadioButton: javafx.scene.control.RadioButton = javafx.scene.control.RadioButton {
        layoutX: 89.0
        layoutY: 193.0
        text: "Second"
        toggleGroup: toggleGroup
        value: "second"
    }
    
    public-read def mainColorLightRadioButton: javafx.scene.control.RadioButton = javafx.scene.control.RadioButton {
        layoutX: 281.0
        layoutY: 173.0
        text: "Main light"
        toggleGroup: toggleGroup
        value: "mainLight"
    }
    
    public-read def mainColorRadioButton: javafx.scene.control.RadioButton = javafx.scene.control.RadioButton {
        layoutX: 89.0
        layoutY: 173.0
        text: "Main"
        toggleGroup: toggleGroup
        value: "main"
    }
    
    public-read def color: javafx.scene.paint.Color = javafx.scene.paint.Color {
        opacity: 0.08627451
    }
    
    public-read def rectangle: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 468.0
        layoutY: 335.0
        fill: color
        width: 10.0
        height: 10.0
    }
    
    public-read def currentState: org.netbeans.javafx.design.DesignState = org.netbeans.javafx.design.DesignState {
    }
    
    public function getDesignRootNodes (): javafx.scene.Node[] {
        [ label, eloIconListview, label2, colorSchemeListview, label3, mainColorRadioButton, mainColorLightRadioButton, secondColorRadioButton, thirdColorRadioButton, BackgroundColorRadioButton, secondColorLightRadioButton, thirdColorLightRadioButton, emptyBackgroundColorRadioButton, label4, label5, label6, label7, redSlider, redValue, greenSlider, greenValue, blueSlider, blueValue, alphaSlider, alphaValue, redDisplay, greenDisplay, blueDisplay, colorDisplay, alphaDisplay, mainDisplay, mainLightDisplay, secondDisplay, thirdDisplay, backgroundDisplay, secondLightDisplay, thirdLightDisplay, emptyBackgroundDisplay, rectangle, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }
    // </editor-fold>//GEN-END:main

}

function run (): Void {
    var design = WindowColorSchemeEditor {};

    javafx.stage.Stage {
        title: "WindowColorSchemeEditor"
        scene: design.getDesignScene ()
    }
}
