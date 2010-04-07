/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

/**
 *
 * @author Sven
 */
public enum WindowSize {

     SMALL,HOVER,NORMAL;

     public static final int DEFAULT_WIDTH = 64;
     public static final int DEFAULT_HEIGHT = 64;
     public static final int DEFAULT_ITEM_WIDTH = 200;

     public int getHeight(){
         switch(this) {
             case SMALL: return 64;
             case HOVER: return 100;
             default: return 160;
         }
     }

     public int getWidth(){
         switch(this) {
             case SMALL: return 80;
             case HOVER: return 160;
             default: return 200;
         }
     }

     public int getImageHeight(){
         switch(this){
             case SMALL: return 20;
             case HOVER: return 32;
             default: return 64;
         }
     }

     public int getImageWidth(){
         switch(this){
             case SMALL: return 20;
             case HOVER: return 32;
             default: return 64;
         }
     }

     /**
      * This method should be used for getting the default image size, which should be used for specifying
      * the size at loading-time.
      * @return loading-time-size of the image
      */
     public static int getDefaultImageWidth(){
         return 64;
     }

     /**
      * This method should be used for getting the default image size, which should be used for specifying
      * the size at loading-time.
      * @return loading-time-size of the image
      */
     public static int getDefaultImageHeight(){
         return 64;
     }
}
