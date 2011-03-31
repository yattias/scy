/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools;

import java.awt.image.BufferedImage;
import java.net.URI;

/**
 * The ScyTool interface is meant for communication between the tools and the ScyDesktop. It informs to tool of relevant system/user actions to the ScyWindow. And it offers the tool an easy way to save/update elos.
 * @author sikken
 */
public interface ScyTool
{

   /**
    * initialise is called after the create() and the properties are injected.
    * @param windowContent - true if tool is placed in the window, false if the tool is placed in a drawer
    */
   public void initialize(boolean windowContent);

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
    * this method is called to report to the tool that the elo loaded in the window content tool has changed. This will happen after an elo has been loaded, saved or save as.
    *
    * Either this method is called or newElo
    */
   public void loadedEloChanged(URI eloUri);

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
    * this method is called before the ScyWindow is closed. It gives the tool the option to save its content. The tool can block the close action, by returning false.
    *
    * This is still under investigation.
    *
    * @return true if the close can continue or false if the close should be aborted
    */
   public boolean aboutToClose();

   /**
    * this method is called after the ScyWindow is opened.
    */
   public void onOpened();

   /**
    * this method is called after the ScyWindow is closed.
    *
    * This is still under investigation.
    */
   public void onClosed();

   /**
    * this method is called when SCY-Lab is going to close. It gives the tool the option to save its content.
    *
    * It is not possible to ask the user anything, the GUI is not visible anymore.
    */
   public void onQuit();

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

   /**
    * this method is called when an object is dragged above the window, return true if the object can be accepted
    */
   public boolean canAcceptDrop(Object object);

   /**
    * this method is called when an object is dropped on the window. This method will only be called after canAcceptDrop has returned true
    */
   public void acceptDrop(Object object);

   /**
    * supplies the tool with runtimeSettingsRetriever object, which can be used to retrieve runtime settings.
    * @param runtimeSettingsRetriever
    */
   public void setRuntimeSettingsRetriever(RuntimeSettingsRetriever runtimeSettingsRetriever);

   /**
    * this method can be called by SCY-Desktop at undefined moments, in order to get the thumbnail of the current ELO.
    *
    * Currently a null return value is being accepted
    *
    * @Param width the width of the thumbnail in pixels
    * @Param width the height of the thumbnail in pixels
    * @returns the thumbnail image
    */
   public BufferedImage getThumbnail(int width, int height);

   /**
    * this method returns the DrawerUIIndicator to specify indicator of the drawer GUI
    *
    * a null value, means no specific indicator is desired
    *
    * @returns the DrawerUIIndicator
    */
   public DrawerUIIndicator getDrawerUIIndicator();
}
