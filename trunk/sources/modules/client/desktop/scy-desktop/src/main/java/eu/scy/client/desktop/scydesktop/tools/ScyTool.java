/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools;

import java.net.URI;

/**
 * The ScyTool interface is meant for communication between the tools and the ScyDesktop. It informs to tool of relevant system/user actions to the ScyWindow. And it offers the tool an easy way to save/update elos.
 * @author sikken
 */
public interface ScyTool
{
   /**
    * initialise is called after the create() and the properties are injected.
    */
   public void initialize();

   /**
    * this method is called after initialise is called on the content and all drawer tools.
    *
    * Currently the ScyTool interface is only available for the content tool.
    */
   public void postInitialize();

   /**
    * this method is called to ask the tool to "create" a new (empty) elo.
    * 
    * Either this method is called or loadElo
    */
   public void newElo();

   /**
    * this method is called to ask the tool to load the specified elo.
    *
    * Either this method is called or newElo
    */
   public void loadElo(URI eloUri);

   /**
    * this method is called after the ScyWindow has got the focus
    */
   public void onGotFocus();

   /**
    * this method is called after the ScyWindow has lost the focus
    */
   public void onLostFocus();

   /**
    * this method is called after the ScyWindow is minimized
    */
   public void onMinimized();

   /**
    * this method is called after the ScyWindow is unminimized
    */
   public void onUnMinimized();

   /**
    * this method is called after the ScyWindow is closed.
    *
    * This is still under investigation.
    */
   public void onClosed();

   /**
    * this method is called before the ScyWindow is closed. It gives the tool the option to save its content. The tool can block the close action, by returning false.
    *
    * This is still under investigation.
    *
    * @return true if the close can continue or false if the close should be aborted
    */
   public boolean aboutToClose();

   /**
    * supplies the tool with eloSaver object, which can be used to save/update elos.
    * @param eloSaver
    */
   public void setEloSaver(EloSaver eloSaver);

   /**
    * supplies the tool with myEloChanged object, which can be used to inform scy-desktop that you have saved/loaded your elo. The scy-desktop can then update it administration of which window is showing which elo. If you use the eloSaver, you do not have to use myEloChanged.
    * @param myEloChanged
    */
   public void setMyEloChanged(MyEloChanged myEloChanged);
}
