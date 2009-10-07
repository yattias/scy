/*
 * FlyingSaucerContentCreator.fx
 *
 * Created on 17-sep-2009, 11:26:46
 */

package eu.scy.client.tools.fxflyingsaucer.registration;



import javafx.scene.Node;
import java.net.URI;


import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;

import eu.scy.client.tools.fxflyingsaucer.EloFlyingSaucerPanel;

import roolo.elo.api.IELOFactory;

import java.util.HashMap;

import javafx.ext.swing.SwingComponent;
import eu.scy.client.tools.fxflyingsaucer.EloSavedListener;

import eu.scy.client.tools.fxflyingsaucer.EloSavedEvent;


/**
 * @author sikkenj
 */

// place your code here
public class FlyingSaucerContentCreator extends WindowContentCreatorFX, EloSavedListener {

   public var repository:IRepository;
   public var metadataTypeManager:IMetadataTypeManager;
   public var eloFactory:IELOFactory;

   def createdEloFlyingSaucerPanels = new HashMap();

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      setWindowProperties(scyWindow);
      var flyingSaucerPanel = createEloFlyingSaucerPanel(scyWindow);
      flyingSaucerPanel.setHomeElo(eloUri);
      return SwingComponent.wrap(flyingSaucerPanel);
   }

   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      setWindowProperties(scyWindow);
      var flyingSaucerPanel = createEloFlyingSaucerPanel(scyWindow);
      return SwingComponent.wrap(flyingSaucerPanel);
   }

   function createEloFlyingSaucerPanel(scyWindow:ScyWindow):EloFlyingSaucerPanel{
      var flyingSaucerPanel = new EloFlyingSaucerPanel();
      flyingSaucerPanel.setRepository(repository);
      flyingSaucerPanel.setMetadataTypeManager(metadataTypeManager);
      flyingSaucerPanel.setEloFactory(eloFactory);
      createdEloFlyingSaucerPanels.put(flyingSaucerPanel,scyWindow);
      flyingSaucerPanel.addEloSavedListener(this);
      return flyingSaucerPanel;
   }

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 320;
      scyWindow.minimumHeight = 100;
   }

   public override function eloSaved(eloSavedEvent: EloSavedEvent):Void{
      var window = createdEloFlyingSaucerPanels.get(eloSavedEvent.getSource()) as ScyWindow;
      if (window!=null){
         window.title = eloSavedEvent.getTitle();
         window.eloUri = eloSavedEvent.getElo().getUri();
      }
   }

}
