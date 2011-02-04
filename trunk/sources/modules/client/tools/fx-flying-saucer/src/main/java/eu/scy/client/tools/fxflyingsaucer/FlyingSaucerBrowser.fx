/*
 * FlyingSaucerBrowser.fx
 *
 * Created on 17-sep-2009, 11:13:33
 */

package eu.scy.client.tools.fxflyingsaucer;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.layout.VBox;
import javafx.scene.control.TextBox;


import javafx.ext.swing.SwingComponent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.layout.Resizable;

import java.awt.Dimension;


/**
 * @author sikkenj
 */

// place your code here
public class FlyingSaucerBrowser extends CustomNode, Resizable {

   public override var width on replace{ resizeBrowser() };
   public override var height on replace{ resizeBrowser() };

   def browser = new FlyingSaucerPanel(true);

   function resizeBrowser(){
      browser.setPreferredSize(new Dimension(width,height));
   }


   public override function create(): Node {
      var urlField:TextBox;
      return Group {
         content: [
            VBox{
               content:[
//                  HBox{
//                     content:[
//                        urlField = TextBox {
//                           text: "http://www.scy-net.eu"
//                           columns: 40
//                           selectOnFocus: true
//                        }
//                        Button {
//                           text: "Load"
//                           action: function() {
//                              browser.loadUrl(urlField.text);
//                           }
//                        }
//                     ]
//                  },
                  createBrowserComponent()
               ]
            }

         ]
      };
   }

   function createBrowserComponent():Node{
//      var browserScrollPane = new JScrollPane(browser,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      SwingComponent.wrap(browser)
   }

   override function getPrefWidth(width: Number) : Number{
      return browser.getPreferredSize().width;
   }

   override function getPrefHeight(width: Number) : Number{
      return browser.getPreferredSize().height;
   }


}

function run(){
   var scene:Scene;
   Stage {
      title : "Flying saucer browser test"
      scene: scene = Scene {
         width: 400
         height: 400
         content: [
            FlyingSaucerBrowser{
               width:bind scene.width;
               height:bind scene.height;
            }

         ]
      }
   }


}

