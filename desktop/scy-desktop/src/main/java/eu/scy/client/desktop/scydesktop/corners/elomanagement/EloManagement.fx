/*
 * EloManagement.fx
 *
 * Created on 28-feb-2010, 13:49:32
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import java.util.List;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import javafx.util.Sequences;
import java.net.URI;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.api.search.IQuery;
import roolo.api.search.AndQuery;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import java.lang.System;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.scydesktop.uicontrols.MultiImageButton;
import eu.scy.client.desktop.scydesktop.utils.i18n.Composer;
import eu.scy.client.desktop.scydesktop.scywindows.EloInfoControl;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.utils.EmptyBorderNode;
import javafx.scene.Group;

/**
 * @author sikken
 */
public class EloManagement extends CustomNode {

   def logger = Logger.getLogger(this.getClass());
   public var scyDesktop: ScyDesktop;
   public var scyWindowControl: ScyWindowControl;
   public var repository: IRepository;
   public var metadataTypeManager: IMetadataTypeManager;
   public var titleKey: IMetadataKey;
   public var technicalFormatKey: IMetadataKey;
   public var eloFactory: IELOFactory;
   public var templateEloUris: List;
   public var tooltipManager: TooltipManager;
   public var userId:String;
   public var windowStyler: WindowStyler;
   public var eloInfoControl: EloInfoControl;

   def showCreateBlankElo = scyDesktop.initializer.authorMode;

   def newFromEloTemplateColor = Color.BLUE;
   def searchColor = Color.BLUE;
   def createBlankEloColor = Color.BLUE;

   def authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);

//   def newFromEloTemplateButton =  Button {
//      text: "New"
//      action: createNewEloFromTemplateAction
//   }
//   def searchButton =  Button {
//      text: "Search"
//      action: searchEloAction
//   }
//   def createBlankEloButton =  Button {
//      text: "Create (Dev/Author)"
//      action: createNewBlankEloAction
//   }
   def newFromEloTemplateButton:MultiImageButton =  MultiImageButton {
      imageName: "new"
      action: createNewEloFromTemplateAction
   }
   def searchButton:MultiImageButton =  MultiImageButton {
      imageName: "search"
      action: searchEloAction
   }
   def createBlankEloButton:MultiImageButton =  MultiImageButton {
      imageName: "new_a"
      action: createNewBlankEloAction
   }

   init {
//      var image = Image{
//            url:"{__DIR__}images/new.png"
//            backgroundLoading:false;
//         }
//      var swingIcon = SwingIcon{image:image};
//      println("image: url:{image.url}, {image.error}");
//      newFromEloTemplateButton.icon = swingIcon;
      if (eloFactory == null) {
         eloFactory = scyDesktop.config.getEloFactory();
      }
      if (scyWindowControl == null) {
         scyWindowControl = scyDesktop.scyWindowControl;
      }
      if (tooltipManager == null) {
         tooltipManager = scyDesktop.tooltipManager;
      }
      if (windowStyler == null) {
         windowStyler = scyDesktop.windowStyler;
      }
      if (templateEloUris == null) {
         templateEloUris = scyDesktop.config.getTemplateEloUris();
      }
      if (eloInfoControl == null) {
         eloInfoControl = scyDesktop.eloInfoControl;
      }
      //newFromEloTemplateButton.disable = templateEloUris == null or templateEloUris.isEmpty();
      findTemplateEloInformation();
   }

   public override function create(): Node {
      VBox{
         spacing:5;
         content:[
            newFromEloTemplateButton,
            searchButton,
            if (showCreateBlankElo){
               createBlankEloButton
            }
            else{
               null
            }

         ]
      }
   }
   
   var eloTemplateUris:URI[];
   var eloTemplateUriDisplays: UriDisplay[];

   function findTemplateEloInformation(){
      if (templateEloUris != null){
         for (uri in templateEloUris){
            insert uri as URI into eloTemplateUris;
         }
      }
      eloTemplateUriDisplays = for (uri in eloTemplateUris){
         var uriDisplay = createUriDisplay(uri,false);
         if (uriDisplay==null){
            logger.error("Cannot find template elo with uri: {uri}");
         }
         uriDisplay;
      }
      eloTemplateUriDisplays = Sequences.sort(eloTemplateUriDisplays,UriDisplayComparator{}) as UriDisplay[];
      newFromEloTemplateButton.disable = sizeof eloTemplateUris == 0;
   }

   function createUriDisplay(uri:URI, showAuthor: Boolean): UriDisplay{
      if (uri==null){
         return null;
      }
      var metadata = repository.retrieveMetadata(uri);
      if (metadata==null){
         return null;
      }
      var title = eloInfoControl.getEloTitle(uri);
      var technicalFormat = metadata.getMetadataValueContainer(technicalFormatKey).getValue() as String;
      var author = metadata.getMetadataValueContainer(authorKey).getValue() as Contribute;
      var typeName = scyDesktop.newEloCreationRegistry.getEloTypeName(technicalFormat);
      var authorDisplay = "";
      if (showAuthor){
         var authorName = author.getVCard();
         if (authorName==null){
            authorName = ##"unknown"
         }
         authorDisplay = "{authorName}: "
      }

      UriDisplay{
         uri:uri
         display:"{authorDisplay}{title} ({typeName})"
      }
   }

   function createNewEloFromTemplateAction(): Void{
      newFromEloTemplateButton.turnedOn = true;
      var createNewElo = CreateNewElo{
         createAction:createNewEloFromTemplate
         cancelAction:cancelNewElo
      }
      createNewElo.listView.items = eloTemplateUriDisplays;

      createNewElo.label.text = ##"Select template";
      var eloIcon = windowStyler.getScyEloIcon(ImageWindowStyler.generalNew);
      var windowColorScheme = windowStyler.getWindowColorScheme(ImageWindowStyler.generalNew);

      createModalDialog(windowColorScheme,eloIcon,##"Create new",createNewElo);
   }

   function createNewEloFromTemplate(createNewElo: CreateNewElo):Void{
      var uriDisplay = createNewElo.listView.selectedItem as UriDisplay;
      if (uriDisplay!=null){
         var eloTemplateUri = uriDisplay.uri;
         var newElo = repository.retrieveELO(eloTemplateUri);
         if (newElo!=null){
            var titleContainer = newElo.getMetadata().getMetadataValueContainer(titleKey);
            var templateTitle = titleContainer.getValue() as String;
            titleContainer.setValue(scyDesktop.newTitleGenerator.generateNewTitleFromName(templateTitle));
            var metadata = repository.addForkedELO(newElo);
            eloFactory.updateELOWithResult(newElo,metadata);
            scyWindowControl.addOtherScyWindow(newElo.getUri());
         }
         else{
            logger.error("can't find template elo, with uri: {eloTemplateUri}");
         }
      }
      createNewElo.modalDialogBox.close();
      newFromEloTemplateButton.turnedOn = false;
   }

   function createNewBlankEloAction(): Void{
      createBlankEloButton.turnedOn = true;
      var createNewElo = CreateNewElo{
         createAction:createNewBlankElo
         cancelAction:cancelNewElo
      }
      var typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
      createNewElo.listView.items = Sequences.sort(typeNames);
      createNewElo.label.text = ##"Select type";
      var eloIcon = windowStyler.getScyEloIcon(ImageWindowStyler.generalNew);
      var windowColorScheme = windowStyler.getWindowColorScheme(ImageWindowStyler.generalNew);

      createModalDialog(windowColorScheme,eloIcon,##"Create new",createNewElo);
   }

   function createNewBlankElo(createNewElo: CreateNewElo):Void{
      var eloTypeName = createNewElo.listView.selectedItem as String;
      if (eloTypeName!=null){
         var eloType = scyDesktop.newEloCreationRegistry.getEloType(eloTypeName);
         if (eloType!=null){
            var title = scyDesktop.newTitleGenerator.generateNewTitleFromType(eloType);
            var scyWindow = scyWindowControl.addOtherScyWindow(eloType);
            scyWindow.title = title;
         }
      }
      createNewElo.modalDialogBox.close();
   }

   function cancelNewElo(createNewElo: CreateNewElo):Void{
      createNewElo.modalDialogBox.close();
      newFromEloTemplateButton.turnedOn = false;
      createBlankEloButton.turnedOn = false;
   }


   function createModalDialog(windowColorScheme:WindowColorScheme,eloIcon:EloIcon, title:String,modalDialogNode:ModalDialogNode):Void{
      Composer.localizeDesign(modalDialogNode.getContentNodes());
      modalDialogNode.modalDialogBox = ModalDialogBox {
            content: EmptyBorderNode{
               content: Group{
                  content:modalDialogNode.getContentNodes();
               }
            }

            targetScene: scyDesktop.scene
            title: title
            eloIcon: eloIcon
            windowColorScheme: windowColorScheme
            closeAction:function():Void{
               newFromEloTemplateButton.turnedOn = false;
               createBlankEloButton.turnedOn = false;
               searchButton.turnedOn = false;
            }
         }
   }

   function searchEloAction(): Void{
      searchButton.turnedOn = true;
      var searchElos = SearchElos{
         cancelAction:cancelSearchElo
         searchAction:searchForElos
         openAction:openElo
      }
      var typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
      searchElos.typesListView.items = Sequences.sort(typeNames);

      var eloIcon = windowStyler.getScyEloIcon(ImageWindowStyler.generalSearch);
      var windowColorScheme = windowStyler.getWindowColorScheme(ImageWindowStyler.generalSearch);

      createModalDialog(windowColorScheme,eloIcon,##"Search",searchElos);
   }

   function searchForElos(searchElos: SearchElos):Void{
      var searchQuery:AndQuery;
      var typeQuery = createTypeQuery(searchElos);
      var titleQuery = createTitleQuery(searchElos);
      var authorQuery = createAuthorQuery(searchElos);
      if (typeQuery!=null){
         searchQuery = new AndQuery(typeQuery);
      }
      if (titleQuery!=null){
         if (searchQuery==null){
            searchQuery = new AndQuery(titleQuery);
         }
         else{
            searchQuery.addQuery(titleQuery);
         }
      }
      if (authorQuery!=null){
         if (searchQuery==null){
            searchQuery = new AndQuery(authorQuery);
         }
         else{
            searchQuery.addQuery(authorQuery);
         }
      }
      var queryResults = repository.search(searchQuery);
      logger.info("search query: {searchQuery}\nNumber of results: {queryResults.size()}");
      var resultsUris = for (queryResult in queryResults){
         createUriDisplay(queryResult.getUri(),true);
      }
      searchElos.resultsListView.items = resultsUris;
   }

   function createTypeQuery(searchElos: SearchElos):IQuery{
      var eloTypeName = searchElos.typesListView.selectedItem as String;
      if (not searchElos.allTypesCheckBox.selected and eloTypeName!=null){
         var eloType = scyDesktop.newEloCreationRegistry.getEloType(eloTypeName);
         return new BasicMetadataQuery(technicalFormatKey,BasicSearchOperations.EQUALS,eloType,null);
      }
      return null;
   }

   function createTitleQuery(searchElos: SearchElos):IQuery{
      var searchTitle = searchElos.nameTextbox.rawText.trim();
      if (searchTitle.length()>0){
         return new BasicMetadataQuery(titleKey,BasicSearchOperations.REGULAR_EXPRESSIONS,".*{searchTitle}.*",null);
      }
      return null;
   }

   function createAuthorQuery(searchElos: SearchElos):IQuery{
      // if both mine and others and checked or unchecked, nothing to do
      if (searchElos.mineCheckBox.selected or searchElos.othersCheckBox.selected){
         if (not (searchElos.mineCheckBox.selected and searchElos.othersCheckBox.selected)){
            if (searchElos.mineCheckBox.selected){
               var authorValue = new Contribute(userId, System.currentTimeMillis());
               return new BasicMetadataQuery(authorKey,BasicSearchOperations.EQUALS,authorValue,null);
            }
            if (searchElos.othersCheckBox.selected){
               var authorValue = new Contribute(userId, System.currentTimeMillis());
               return new BasicMetadataQuery(authorKey,BasicSearchOperations.NOT_EQUALS,authorValue,null);
            }
         }
      }
      return null;
   }

   function openElo(searchElos: SearchElos):Void{
      var uriDisplay = searchElos.resultsListView.selectedItem as UriDisplay;
      if (uriDisplay!=null){
         scyWindowControl.addOtherScyWindow(uriDisplay.uri);
      }

      searchElos.modalDialogBox.close();
      searchButton.turnedOn = false;
   }

   function cancelSearchElo(searchElos: SearchElos):Void{
      searchElos.modalDialogBox.close();
      searchButton.turnedOn = false;
   }

}
