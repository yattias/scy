/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools;

/**
 *
 * @author sikken
 */
public interface ScyTool
{

   public void onInit();

   public void onStartup();

   public void onFocus();

   public void onHide();

   public void onMinimize();

   public void onClose();
}
