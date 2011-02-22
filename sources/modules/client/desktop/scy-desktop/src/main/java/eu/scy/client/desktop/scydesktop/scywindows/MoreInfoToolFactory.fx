/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.Node;

/**
 * @author SikkenJ
 */
public mixin class MoreInfoToolFactory {

   public abstract function createMoreInfoTool(showMoreInfo: ShowMoreInfo): Node;

}
