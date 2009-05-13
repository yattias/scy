/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.awareness.contact;

/**
 *
 * @author Sven
 */
public enum WindowSize {

     SMALL,HOVER,NORMAL;

     public int getHeight(){
         switch(this) {
             case SMALL: return 64;
             case HOVER: return 100;
             default: return 160;
         }
     }

     public int getWidth(){
         switch(this) {
             case SMALL: return 64;
             case HOVER: return 80;
             default: return 200;
         }
     }
}
