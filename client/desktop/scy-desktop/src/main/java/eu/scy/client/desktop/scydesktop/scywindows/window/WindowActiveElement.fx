/*
 * WindowActiveElement.fx
 *
 * Created on 3-sep-2009, 17:41:34
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.paint.Color;

/**
 * @author sikkenj
 */

// place your code here
public abstract class WindowActiveElement extends WindowElement {

   public-init var size = 10.0;
   public var active = true;

   public-init var borderWidth = 2.0;
   public-init var separatorLength = 7.0;
   public-init var secondLineSeparation = 2+borderWidth;
   public-init var borderWidthSecondLine = borderWidth/2.0;

   protected var highLighted = false on replace {updateHighLighting()};

   protected def transparentColor = Color.TRANSPARENT;
//   protected def transparentColor = Color.color(0,1,0,0.25);
   
//   protected var mainColor = windowColorScheme.mainColor;

   protected def highLightScale = 1.25;
   protected var scaledTranslateXCorrection2 = 0.0;
   protected var scaledTranslateYCorrection2 = 0.0;

   function updateHighLighting(){
      if (highLighted){
//         mainColor = windowColorScheme.highliteMainColor;
//         mainColor = windowColorScheme.mainColor;
         scaleX = highLightScale;
         scaleY = highLightScale;
         translateX = (highLightScale-1.0)*scaledTranslateXCorrection2;
         translateY = (highLightScale-1.0)*scaledTranslateYCorrection2;
      }
      else{
//         mainColor = windowColorScheme.mainColor;
         scaleX = 1.0;
         scaleY = 1.0;
         translateX = 0;
         translateY = 0;
      }
   }

   init{
      updateHighLighting();
   }



}

