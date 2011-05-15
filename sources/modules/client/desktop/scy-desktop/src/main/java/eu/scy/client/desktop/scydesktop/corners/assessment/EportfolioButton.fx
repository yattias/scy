package eu.scy.client.desktop.scydesktop.corners.assessment;

import javafx.scene.Node;
import javafx.scene.CustomNode;
import eu.scy.client.desktop.desktoputils.BareBonesBrowserLaunch;
import eu.scy.client.desktop.scydesktop.draganddrop.DropTarget2;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;
import eu.scy.common.configuration.Configuration;
import eu.scy.common.scyelo.ScyElo;
import java.lang.Object;
import java.net.URLEncoder;
import java.lang.Void;
import java.net.URI;
import roolo.elo.api.IMetadata;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.desktoputils.log4j.Logger;

public class EportfolioButton extends CustomNode, DropTarget2 {

   public-init var scyDesktop: ScyDesktop;
   public var eportfolioButton: EloIconButton;
   public var turnedOn = false on replace {
      eportfolioButton.turnedOn = turnedOn
   };
   var tbi=scyDesktop.config.getToolBrokerAPI();
   def identifierKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);
   def logger = Logger.getLogger(this.getClass());

   public override function create(): Node {
      eportfolioButton = EloIconButton {
         eloIcon: scyDesktop.windowStyler.getScyEloIcon("e_portfolio")
         size: scyDesktop.desktopButtonSize
         actionScheme: scyDesktop.desktopButtonActionScheme
         disableButton: scyDesktop.initializer.offlineMode
         tooltipManager: scyDesktop.tooltipManager
         tooltip: if (scyDesktop.initializer.offlineMode) "ePortfolio is only available when working online" else "ePortfolio"
         action: function(): Void {
            def conf:Configuration=Configuration.getInstance();
            def eloUriEncoded = URLEncoder.encode(scyDesktop.missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri().toString(),"UTF-8");
            def eportfolioURL = "{conf.getEportfolioProtocol()}://{conf.getEportfolioServer()}:{conf.getEportfolioPort()}{conf.getEportfolioContext()}EPortfolioIndex.html?eloURI={eloUriEncoded}";
            try {
               var basicService = javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService") as javax.jnlp.BasicService;
               if (basicService != null) {
                  var url : java.net.URL = new java.net.URL(eportfolioURL);
                  basicService.showDocument(url);
               }
            } catch (e: javax.jnlp.UnavailableServiceException) {
               BareBonesBrowserLaunch.openURL(eportfolioURL);
            }
//                 eportfolioButton.imageName = "eportfolio";
         }
      }
      eportfolioButton
   }

   override public function dropEntered(object: Object, canAccept: Boolean): Void {
      if (canAccept) {
         eportfolioButton.turnedOn = true;
      }
   }

   override public function dropLeft(object: Object): Void {
      eportfolioButton.turnedOn = false;
   }

   override public function canAcceptDrop(object: Object): Boolean {
      if (object instanceof IMetadata) {
         return true;
      }
      return false;
   }

   override public function acceptDrop(object: Object): Void {
      if (object instanceof IMetadata) {
         def metadata = object as IMetadata;
         def scyElo = ScyElo.loadMetadata(metadata.getMetadataValueContainer(identifierKey).getValue() as URI, tbi);
         def conf:Configuration=Configuration.getInstance();
         def missionEloUriEncoded = URLEncoder.encode(scyDesktop.missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri().toString(),"UTF-8");
         def eloUriEncoded = URLEncoder.encode(scyElo.getUri().toString(),"UTF-8");
         def eportfolioAddURL = "{conf.getEportfolioProtocol()}://{conf.getEportfolioServer()}:{conf.getEportfolioPort()}{conf.getEportfolioContext()}xml/addEloToPortfolio.html?missionURI={missionEloUriEncoded}&eloURI={eloUriEncoded}";
         try {
            var basicService = javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService") as javax.jnlp.BasicService;
            if (basicService != null) {
               var url : java.net.URL = new java.net.URL(eportfolioAddURL);
               basicService.showDocument(url);
            }
         } catch (e: javax.jnlp.UnavailableServiceException) {
            BareBonesBrowserLaunch.openURL(eportfolioAddURL);
         }
      }
   }
}
