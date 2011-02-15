/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.colorscheme;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.art.WindowColorSchemes;
import javafx.util.Math;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import javafx.scene.control.ListCell;

/**
 * @author SikkenJ
 */
public class WindowColorSchemeEditorNode extends CustomNode {

   def windowColorSchemeEditor = WindowColorSchemeEditor {
      }
   public var windowColorSchemes = WindowColorSchemes.getStandardWindowColorSchemes() on replace {windowColorSchemesChanged()};
   def selectedWindowColorScheme = bind windowColorSchemeEditor.colorSchemeListview.selectedItem as WindowColorScheme on replace { selectedWindowColorSchemeChanged() };
   def selectedColorPart = bind windowColorSchemeEditor.toggleGroup.selectedToggle.value as String on replace { selectedColorPartChanged() };
   def rawRedValue = bind windowColorSchemeEditor.redSlider.value on replace { redValueChanged() };
   def rawGreenValue = bind windowColorSchemeEditor.greenSlider.value on replace { greenValueChanged() };
   def rawBlueValue = bind windowColorSchemeEditor.blueSlider.value on replace { blueValueChanged() };
   def rawAlphaValue = bind windowColorSchemeEditor.alphaSlider.value on replace { alphaValueChanged() };
   var redValue: Integer;
   var greenValue: Integer;
   var blueValue: Integer;
   var alphaValue: Integer;
   var selectedColor: Color;

   public override function create(): Node {
      setupWindowColorSchemeEditor();
      FX.deferAction(selectDefaults);
      Group {
         content: [
            windowColorSchemeEditor.getDesignRootNodes()
         ]
      }
   }

   function selectDefaults():Void{
      if (selectedWindowColorScheme==null){
         windowColorSchemeEditor.colorSchemeListview.select(0);
      }
      if (selectedColorPart==null){
         windowColorSchemeEditor.mainColorRadioButton.selected = true;
      }
   }


   function setupWindowColorSchemeEditor() {
      windowColorSchemeEditor.colorSchemeListview.items = windowColorSchemes.getAllWindowColorSchemes();
      windowColorSchemeEditor.colorSchemeListview.cellFactory = windowColorSchemeCellFactory
   }

   public function windowColorSchemeCellFactory(): ListCell {
      var listCell: ListCell;
      listCell = ListCell {
            node: WindowColorSchemeDisplay {
               windowColorScheme: bind listCell.item as WindowColorScheme
            }
         }
   }

   function windowColorSchemesChanged(){
      setupWindowColorSchemeEditor();
   }


   function selectedColorChanged() {
      selectedColor = Color.rgb(redValue, greenValue, blueValue, alphaValue / 255.0);
      windowColorSchemeEditor.colorDisplay.fill = selectedColor;
      if ("main" == selectedColorPart) {
         selectedWindowColorScheme.mainColor = selectedColor;
         windowColorSchemeEditor.mainDisplay.fill = selectedColor;
      } else if ("mainLight" == selectedColorPart) {
         selectedWindowColorScheme.mainColorLight = selectedColor;
         windowColorSchemeEditor.mainLightDisplay.fill = selectedColor;
      } else if ("second" == selectedColorPart) {
         selectedWindowColorScheme.secondColor = selectedColor;
         windowColorSchemeEditor.secondDisplay.fill = selectedColor;
      } else if ("secondLight" == selectedColorPart) {
         selectedWindowColorScheme.secondColorLight = selectedColor;
         windowColorSchemeEditor.secondLightDisplay.fill = selectedColor;
      } else if ("third" == selectedColorPart) {
         selectedWindowColorScheme.thirdColor = selectedColor;
         windowColorSchemeEditor.thirdDisplay.fill = selectedColor;
      } else if ("thirdLight" == selectedColorPart) {
         selectedWindowColorScheme.thirdColorLight = selectedColor;
         windowColorSchemeEditor.thirdLightDisplay.fill = selectedColor;
      } else if ("background" == selectedColorPart) {
         selectedWindowColorScheme.backgroundColor = selectedColor;
         windowColorSchemeEditor.mainLightDisplay.fill = selectedColor;
      } else if ("emptyBackground" == selectedColorPart) {
         selectedWindowColorScheme.emptyBackgroundColor = selectedColor;
         windowColorSchemeEditor.emptyBackgroundDisplay.fill = selectedColor;
      }

   }

   function selectedWindowColorSchemeChanged() {
      windowColorSchemeEditor.mainDisplay.fill = selectedWindowColorScheme.mainColor;
      windowColorSchemeEditor.mainLightDisplay.fill = selectedWindowColorScheme.mainColorLight;
      windowColorSchemeEditor.secondDisplay.fill = selectedWindowColorScheme.secondColor;
      windowColorSchemeEditor.secondLightDisplay.fill = selectedWindowColorScheme.secondColorLight;
      windowColorSchemeEditor.thirdDisplay.fill = selectedWindowColorScheme.thirdColor;
      windowColorSchemeEditor.thirdLightDisplay.fill = selectedWindowColorScheme.thirdColorLight;
      windowColorSchemeEditor.backgroundDisplay.fill = selectedWindowColorScheme.backgroundColor;
      windowColorSchemeEditor.emptyBackgroundDisplay.fill = selectedWindowColorScheme.emptyBackgroundColor;
      selectedColorPartChanged();
   }

   function selectedColorPartChanged() {
      if ("main" == selectedColorPart) {
         updateColorParts(selectedWindowColorScheme.mainColor);
      } else if ("mainLight" == selectedColorPart) {
         updateColorParts(selectedWindowColorScheme.mainColorLight);
      } else if ("second" == selectedColorPart) {
         updateColorParts(selectedWindowColorScheme.secondColor);
      } else if ("secondLight" == selectedColorPart) {
         updateColorParts(selectedWindowColorScheme.secondColorLight);
      } else if ("third" == selectedColorPart) {
         updateColorParts(selectedWindowColorScheme.thirdColor);
      } else if ("thirdLight" == selectedColorPart) {
         updateColorParts(selectedWindowColorScheme.thirdColorLight);
      } else if ("background" == selectedColorPart) {
         updateColorParts(selectedWindowColorScheme.backgroundColor);
      } else if ("emptyBackground" == selectedColorPart) {
         updateColorParts(selectedWindowColorScheme.emptyBackgroundColor);
      }
   }

   function updateColorParts(color: Color) {
      windowColorSchemeEditor.redSlider.value = 255 * color.red;
      windowColorSchemeEditor.greenSlider.value = 255 * color.green;
      windowColorSchemeEditor.blueSlider.value = 255 * color.blue;
      windowColorSchemeEditor.alphaSlider.value = 255 * color.opacity;
   }

   function redValueChanged() {
      redValue = getColorValue(rawRedValue);
      windowColorSchemeEditor.redValue.text = "{redValue}";
      windowColorSchemeEditor.redDisplay.fill = Color.rgb(redValue, 0, 0, 1);
      selectedColorChanged();
   }

   function greenValueChanged() {
      greenValue = getColorValue(rawGreenValue);
      windowColorSchemeEditor.greenValue.text = "{greenValue}";
      windowColorSchemeEditor.greenDisplay.fill = Color.rgb(0, greenValue, 0, 1);
      selectedColorChanged();
   }

   function blueValueChanged() {
      blueValue = getColorValue(rawBlueValue);
      windowColorSchemeEditor.blueValue.text = "{blueValue}";
      windowColorSchemeEditor.blueDisplay.fill = Color.rgb(0, 0, blueValue, 1);
      selectedColorChanged();
   }

   function alphaValueChanged() {
      alphaValue = getColorValue(rawAlphaValue);
      windowColorSchemeEditor.alphaValue.text = "{alphaValue}";
      windowColorSchemeEditor.alphaDisplay.fill = Color.rgb(0, 0, 0, alphaValue / 255.0);
      selectedColorChanged();
   }

   function getColorValue(value: Number): Integer {
      var intValue = Math.round(value);
      intValue = Math.min(255, intValue);
      Math.max(0, intValue);
   }

   function getAlphaValue(value: Number): Number {
      Math.min(1.0, Math.max(0, value));
   }

}

function run() {
   Stage {
      title: "Test window color scheme editor"
      onClose: function() {
      }
      scene: Scene {
         width: 400
         height: 400
         content: [
            WindowColorSchemeEditorNode {
            }
         ]
      }
   }

}
