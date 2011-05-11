/*
 * TestImages.fx
 *
 * Created on 31-mrt-2010, 17:50:22
 */
package eu.scy.client.desktop.scydesktop.uicontrols.test;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;
import eu.scy.client.desktop.desktoputils.art.ImageLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import eu.scy.client.desktop.desktoputils.art.FxdImageLoader;
import eu.scy.client.desktop.desktoputils.art.ArtSource;

/**
 * @author sikken
 */
public class TestImages extends CustomNode {



   def imageLoader = ImageLoader.getImageLoader();
   def imageExtension = ".png";
   var selectedFxdImageLoader = FxdImageLoader {
         sourceName: ArtSource.selectedIconsPackage
      }
   var notSelectedFxdImageLoader = FxdImageLoader {
         sourceName: ArtSource.notSelectedIconsPackage
      }
   def testImagesDesign: TestImagesDesign = TestImagesDesign {
         reloadAction: reloadAction
      }
   var selectedImage: Node;
   var notSelectedImage: Node;
   def vectorImageGroup = Group { };
   def scale = bind testImagesDesign.scaleSlider.value on replace {
         updateScale();
      }

   public override function create(): Node {
      testImagesDesign.reloadButton.action = reloadAction;
      return Group {
            content: [
               testImagesDesign.getDesignRootNodes(),
               vectorImageGroup
            ]
         };
   }

   function reloadFXDImageLoaders(){
      selectedFxdImageLoader = null;
      selectedFxdImageLoader = FxdImageLoader {
         sourceName: ArtSource.selectedIconsPackage
         loadedAction:fxdImageLoaderLoaded
      }
      notSelectedFxdImageLoader = null;
      notSelectedFxdImageLoader = FxdImageLoader {
         sourceName: ArtSource.notSelectedIconsPackage
         loadedAction:fxdImageLoaderLoaded
      }
   }

   function fxdImageLoaderLoaded(){
      if (selectedFxdImageLoader.loaded and notSelectedFxdImageLoader.loaded){
         loadButtonImages();
      }
   }

   function reloadAction():Void{
      reloadFXDImageLoaders();
   }


   function loadButtonImages(): Void {
      var imageName = testImagesDesign.imageNameTextBox.rawText.trim();
      testImagesDesign.activeBitmap.image = imageLoader.getImage("{imageName}_act{imageExtension}");
      testImagesDesign.inactiveBitmap.image = imageLoader.getImage("{imageName}_inact{imageExtension}");
      showNodeSize(testImagesDesign.activeBitmap, testImagesDesign.activeBitmapSize);
      showNodeSize(testImagesDesign.inactiveBitmap, testImagesDesign.inactiveBitmapSize);
      selectedImage = selectedFxdImageLoader.getNode(imageName);
      selectedImage.visible = true;
      selectedImage.layoutX = testImagesDesign.activeBitmap.layoutX;
      selectedImage.layoutY = testImagesDesign.selectedImageLabel.layoutY;
      notSelectedImage = notSelectedFxdImageLoader.getNode(imageName);
      notSelectedImage.visible = true;
      notSelectedImage.layoutX = testImagesDesign.activeBitmap.layoutX;
      notSelectedImage.layoutY = testImagesDesign.notSelectedImageLabel.layoutY;
      updateScale();
      delete  vectorImageGroup.content;
      vectorImageGroup.content = [selectedImage, notSelectedImage];
   }

   function updateScale() {
      selectedImage.scaleX = scale;
      selectedImage.scaleY = scale;
      notSelectedImage.scaleX = scale;
      notSelectedImage.scaleY = scale;
      showNodeSize(selectedImage, testImagesDesign.selectedImageSize);
      showNodeSize(notSelectedImage, testImagesDesign.notSelectedImageSize);
      testImagesDesign.scaleValue.text = "{scale}";
   }

   function showImageSize(image: Image, label: Label) {
      var size = "{







                    %2.1g image.width}*{%2.1g image.height}";
      label.text = size;
   }

   function showNodeSize(node: Node, label : Label){
      var size = "{node.boundsInParent.width}*{node.boundsInParent.height}";
      label.text = size;
   }

   function setScaleToSize(width:Number,height:Number){

   }


}

function run() {
   InitLog4JFX.initLog4J();

   Stage {
      title: "Test multi image button"
      scene: Scene {
         width: 300
         height: 280
         content: TestImages{
            }
      }
   }
}
