/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.LasFX;

/**
 * @author SikkenJ
 */

public mixin class MoreInfoManager extends ShowMoreInfo {

   public var activeLas: LasFX;
   public abstract function getControlNode():Node;

//   public abstract function showMoreInfo(infoUri:URI, type: MoreInfoTypes, eloUri: URI):Void


}
