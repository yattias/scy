   /*
 * SimpleScyDesktopEloSaver.fx
 *
 * Created on 26-feb-2010, 12:02:24
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.NewTitleGenerator;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design.SimpleSaveAsNodeDesign;
import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;
import roolo.elo.api.IMetadata;
import eu.scy.client.desktop.scydesktop.utils.i18n.Composer;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

/**
 * @author sikken
 */
public class SimpleScyDesktopEloSaver extends EloSaver {

   public var newTitleGenerator: NewTitleGenerator;
   public var myEloChanged: MyEloChanged;
   public var repository: IRepository;
   public var eloFactory: IELOFactory;
   public var titleKey: IMetadataKey;
   public var technicalFormatKey: IMetadataKey;
   public var window: ScyWindow;
   public var config: Config;
   public var windowStyler: WindowStyler;
   public var scyToolActionLogger: ScyToolActionLogger;

   def authorKey = config.getMetadataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);

   public override function eloSaveAs(elo: IELO, eloSaverCallBack: EloSaverCallBack): Void {
      var forking = elo.getUri() != null;
      var currentEloTitle = elo.getMetadata().getMetadataValueContainer(titleKey).getValue() as String;
      var suggestedEloTitle = currentEloTitle;
      if (suggestedEloTitle == null) {
         suggestedEloTitle = window.title;
      }
      if (forking) {
         suggestedEloTitle = "Copy of {suggestedEloTitle}";
      }
      showEloSaveAsPanel(elo, suggestedEloTitle, true, eloSaverCallBack);
   }

   function showEloSaveAsPanel(elo: IELO, suggestedEloTitle: String, myElo: Boolean, eloSaverCallBack: EloSaverCallBack): Void {
      var eloSaveAsPanel = SimpleSaveAsNodeDesign {
            saveAction: saveAction
            cancelAction: cancelAction
            elo: elo
            myElo: myElo
            eloSaverCallBack: eloSaverCallBack
         }
      eloSaveAsPanel.setTitle(suggestedEloTitle);
      var eloIcon: EloIcon;
      var windowColorScheme: WindowColorScheme;
      if (window.eloUri != null) {
         eloIcon = windowStyler.getScyEloIcon(window.eloUri);
         windowColorScheme = windowStyler.getWindowColorScheme(window.eloUri);
      }
      else {
         eloIcon = windowStyler.getScyEloIcon(window.eloType);
         windowColorScheme = windowStyler.getWindowColorScheme(window.eloType);
      }
      Composer.localizeDesign(eloSaveAsPanel.getDesignRootNodes());
      eloSaveAsPanel.modalDialogBox = ModalDialogBox {
            content: Group {
               content: eloSaveAsPanel.getDesignRootNodes()
            }
            targetScene: window.scene
            title: ##"Enter title"
            eloIcon: eloIcon
            windowColorScheme: windowColorScheme
         }
   }

   function saveAction(eloSaveAsPanel: SimpleSaveAsNodeDesign): Void {
      var elo = eloSaveAsPanel.elo;
      elo.getMetadata().getMetadataValueContainer(titleKey).setValue(eloSaveAsPanel.getTitle());
      if (elo.getUri() != null) {
         eloFactory.detachELO(elo);
      }
      var newMetadata = repository.addNewELO(elo);
      eloFactory.updateELOWithResult(elo, newMetadata);
      if (eloSaveAsPanel.myElo) {
         myEloChanged.myEloChanged(elo);
      }
      eloSaveAsPanel.eloSaverCallBack.eloSaved(elo);
      scyToolActionLogger.eloSaved(elo);
      eloSaveAsPanel.modalDialogBox.close();
   }

   function cancelAction(eloSaveAsPanel: SimpleSaveAsNodeDesign): Void {
      eloSaveAsPanel.eloSaverCallBack.eloSaveCancelled(eloSaveAsPanel.elo);
      eloSaveAsPanel.modalDialogBox.close();
   }

   public override function eloUpdate(elo: IELO, eloSaverCallBack: EloSaverCallBack): Void {
      if (elo.getUri() != null) {
         var newMetadata:IMetadata;
         var eloAuthor = elo.getMetadata().getMetadataValueContainer(authorKey).getValue() as Contribute;
         if (eloAuthor.getVCard()!=config.getToolBrokerAPI().getLoginUserName()){
            // it is not my elo, make a fork of it
            newMetadata = repository.addForkedELO(elo);
         }
         else{
            newMetadata = repository.updateELO(elo);
         }
         eloFactory.updateELOWithResult(elo, newMetadata);
         myEloChanged.myEloChanged(elo);
      } else {
         eloSaveAs(elo, eloSaverCallBack);
      }
   }

   public override function otherEloSaveAs(elo: IELO, eloSaverCallBack: EloSaverCallBack): Void {
      var forking = elo.getUri() != null;
      var currentEloTitle = elo.getMetadata().getMetadataValueContainer(titleKey).getValue() as String;
      var suggestedEloTitle = currentEloTitle;
      if (suggestedEloTitle == null) {
         var eloType = elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue() as String;
         suggestedEloTitle = newTitleGenerator.generateNewTitleFromType(eloType);
      }
      if (forking) {
         suggestedEloTitle = "Copy of {suggestedEloTitle}";
      }
      showEloSaveAsPanel(elo, suggestedEloTitle, false, eloSaverCallBack);
   }

}
