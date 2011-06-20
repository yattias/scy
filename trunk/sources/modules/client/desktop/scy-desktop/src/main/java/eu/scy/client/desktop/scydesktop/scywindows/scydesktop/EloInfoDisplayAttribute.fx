/*
 * EloInfoDisplayAttribute.fx
 *
 * Created on 6-mrt-2010, 15:32:15
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import java.net.URI;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconFactory;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;

/**
 * @author sikken
 */
public class EloInfoDisplayAttribute extends ScyWindowAttribute {

   public var window: ScyWindow;
   public var repository: IRepository;
   public var metadataTypeManager: IMetadataTypeManager;
   public var tooltipManager: TooltipManager;
   override public var priority = 1;
   def identifierKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
   def technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
   def isForkOfKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_FORK_OF.getId());
   def lasKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.LAS.getId());
   def anchorIdKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.ANCHOR_ID.getId());
   def functionalTypeKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE.getId());

   public override function clone(): EloInfoDisplayAttribute {
      EloInfoDisplayAttribute {
         window: window
         repository: repository
         metadataTypeManager: metadataTypeManager
         tooltipManager: tooltipManager
         priority: priority
      }
   }

   override protected function create(): Node {
      def eloIcon = EloIconFactory {}.createEloIcon("information2");
      eloIcon.windowColorScheme = window.windowColorScheme;
      EloIconButton{
         size: itemSize
         mouseOverSize: mouseOverItemSize
         eloIcon: eloIcon
         tooltipFunction: tooltipFunction
         tooltipManager: tooltipManager
         hideBackground: true
         actionScheme: 1
         action: placeUriOnClipboard
      }
   }

   function tooltipFunction(): String{
      "Click to copy URI to clipboard\n\n{getEloInfo()}"
   }

   function placeUriOnClipboard(): Void {
      //Get the toolkit
      def toolkit = Toolkit.getDefaultToolkit();

      //Get the clipboard
      def clipboard = toolkit.getSystemClipboard();

      //The setContents method of the Clipboard instance takes a Transferable
      //as first parameter. The StringSelection class implements the Transferable
      //interface.
      def stringSel = new StringSelection(window.eloUri.toString());

      //We specify null as the clipboard owner
      clipboard.setContents(stringSel, null);
   }

   function getEloInfo(): String {
      if (window.eloUri == null) {
         return "no elo uri"
      }
      var metadata = repository.retrieveMetadata(window.eloUri);
      var technicalFormat = metadata.getMetadataValueContainer(technicalFormatKey).getValue() as String;
      var functionalType = metadata.getMetadataValueContainer(functionalTypeKey).getValue() as String;
      var lasId = metadata.getMetadataValueContainer(lasKey).getValue() as String;
      var anchorId = metadata.getMetadataValueContainer(anchorIdKey).getValue() as String;
      var rootUri = getRootUri(window.eloUri);
      if (window.eloUri == rootUri) {
         // most likely a static elo, so resource. So the las and anchor ids are probably from specification creation time
         // thus ignore them
         lasId = "";
         anchorId = "";
      }

      "uri: {window.eloUri}\n""tech type: {technicalFormat}\n""func role: {functionalType}\n""las: {lasId}\n""anchorId: {anchorId}\n""def uri: {if (window.eloUri == rootUri) 'same as uri' else rootUri}"
   }

   function getRootUri(uri: URI): URI {
      var metadata = repository.retrieveMetadataFirstVersion(uri);
      var uriFirstVersion = metadata.getMetadataValueContainer(identifierKey).getValue() as URI;
      var isForkOfUri = metadata.getMetadataValueContainer(isForkOfKey).getValue() as URI;
      if (isForkOfUri == null) {
         return uriFirstVersion
      }
      return getRootUri(isForkOfUri);
   }

}
