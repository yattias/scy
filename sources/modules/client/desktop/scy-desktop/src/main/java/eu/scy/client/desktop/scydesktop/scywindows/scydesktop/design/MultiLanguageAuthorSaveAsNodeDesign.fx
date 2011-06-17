/*
 * MultiLanguageAuthorSaveAsNodeDesign.fx
 *
 * Created on 17-jun-2011, 11:19:30
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design;

import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.client.desktop.desktoputils.art.ImageLoader;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.SimpleScyDesktopEloSaver.FunctionalRoleContainer;

/**
 * @author SikkenJ
 */
public class MultiLanguageAuthorSaveAsNodeDesign extends EloSaveAsMixin {

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:main
    public-read def rectangle: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: -2.0
        layoutY: -2.0
        fill: javafx.scene.paint.Color.WHITE
        width: 313.0
        height: 280.0
    }
    
    public-read def titleLabel: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 6.0
        layoutY: 6.0
        text: "##Title"
    }
    
    public-read def flag_en: javafx.scene.image.ImageView = javafx.scene.image.ImageView {
        layoutX: 13.0
        layoutY: 27.0
        fitWidth: 30.0
        fitHeight: 20.0
        preserveRatio: true
    }
    
    def __layoutInfo_title_en: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 244.0
    }
    public-read def title_en: javafx.scene.control.TextBox = javafx.scene.control.TextBox {
        layoutX: 56.0
        layoutY: 26.0
        layoutInfo: __layoutInfo_title_en
        onKeyTyped: title_enOnKeyTyped
    }
    
    public-read def flag_nl: javafx.scene.image.ImageView = javafx.scene.image.ImageView {
        layoutX: 13.0
        layoutY: 57.0
        fitWidth: 30.0
        fitHeight: 20.0
        preserveRatio: true
    }
    
    def __layoutInfo_title_nl: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 244.0
    }
    public-read def title_nl: javafx.scene.control.TextBox = javafx.scene.control.TextBox {
        layoutX: 56.0
        layoutY: 57.0
        layoutInfo: __layoutInfo_title_nl
        onKeyTyped: title_nlOnKeyTyped
    }
    
    public-read def cancelButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 233.0
        layoutY: 247.0
        text: "##Cancel"
        action: cancelButtonAction
    }
    
    public-read def saveButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 145.0
        layoutY: 247.0
        text: "##Save"
        action: saveButtonAction
    }
    
    public-read def roleLabel: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 6.0
        layoutY: 218.0
        text: "##Role"
    }
    
    def __layoutInfo_roleChoiceBox: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 244.0
    }
    public-read def roleChoiceBox: javafx.scene.control.ChoiceBox = javafx.scene.control.ChoiceBox {
        layoutX: 56.0
        layoutY: 215.0
        layoutInfo: __layoutInfo_roleChoiceBox
        items: null
    }
    
    public-read def flag_et: javafx.scene.image.ImageView = javafx.scene.image.ImageView {
        layoutX: 13.0
        layoutY: 87.0
        fitWidth: 30.0
        fitHeight: 20.0
    }
    
    public-read def flag_fr: javafx.scene.image.ImageView = javafx.scene.image.ImageView {
        layoutX: 13.0
        layoutY: 117.0
        fitWidth: 30.0
        fitHeight: 20.0
    }
    
    public-read def flag_el: javafx.scene.image.ImageView = javafx.scene.image.ImageView {
        layoutX: 13.0
        layoutY: 147.0
        fitWidth: 30.0
        fitHeight: 20.0
    }
    
    public-read def flag_no: javafx.scene.image.ImageView = javafx.scene.image.ImageView {
        layoutX: 13.0
        layoutY: 177.0
        fitWidth: 30.0
        fitHeight: 20.0
    }
    
    def __layoutInfo_title_et: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 244.0
    }
    public-read def title_et: javafx.scene.control.TextBox = javafx.scene.control.TextBox {
        layoutX: 56.0
        layoutY: 87.0
        layoutInfo: __layoutInfo_title_et
        onKeyTyped: title_etOnKeyTyped
    }
    
    def __layoutInfo_title_fr: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 244.0
    }
    public-read def title_fr: javafx.scene.control.TextBox = javafx.scene.control.TextBox {
        layoutX: 56.0
        layoutY: 117.0
        layoutInfo: __layoutInfo_title_fr
        onKeyTyped: title_frOnKeyTyped
    }
    
    def __layoutInfo_title_el: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 244.0
    }
    public-read def title_el: javafx.scene.control.TextBox = javafx.scene.control.TextBox {
        layoutX: 56.0
        layoutY: 147.0
        layoutInfo: __layoutInfo_title_el
        onKeyTyped: title_elOnKeyTyped
    }
    
    def __layoutInfo_title_no: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 244.0
    }
    public-read def title_no: javafx.scene.control.TextBox = javafx.scene.control.TextBox {
        layoutX: 56.0
        layoutY: 177.0
        layoutInfo: __layoutInfo_title_no
        onKeyTyped: title_noOnKeyTyped
    }
    
    public-read def color: javafx.scene.paint.Color = javafx.scene.paint.Color {
    }
    
    public-read def currentState: org.netbeans.javafx.design.DesignState = org.netbeans.javafx.design.DesignState {
    }
    
    public function getDesignRootNodes (): javafx.scene.Node[] {
        [ rectangle, titleLabel, flag_en, title_en, flag_nl, title_nl, cancelButton, saveButton, roleLabel, roleChoiceBox, flag_et, flag_fr, flag_el, flag_no, title_et, title_fr, title_el, title_no, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }
    // </editor-fold>//GEN-END:main

   function title_noOnKeyTyped(event: javafx.scene.input.KeyEvent): Void {
      updateSaveState()
   }

   function title_elOnKeyTyped(event: javafx.scene.input.KeyEvent): Void {
      updateSaveState()
   }

   function title_frOnKeyTyped(event: javafx.scene.input.KeyEvent): Void {
      updateSaveState()
   }

   function title_etOnKeyTyped(event: javafx.scene.input.KeyEvent): Void {
      updateSaveState()
   }

   function title_nlOnKeyTyped(event: javafx.scene.input.KeyEvent): Void {
      updateSaveState()
   }

   function title_enOnKeyTyped(event: javafx.scene.input.KeyEvent): Void {
      updateSaveState()
   }

   def selectedFunctionalRole = bind (roleChoiceBox.selectedItem as FunctionalRoleContainer).functionalRole on replace {
              updateSaveState();
           };

   public override function getDesignNodes(): javafx.scene.Node[] {
      return getDesignRootNodes()
   }

   function cancelButtonAction(): Void {
      cancelAction(this);
   }

   function saveButtonAction(): Void {
      saveAction(this)
   }

   function updateSaveState(): Void {
      var saveDisabled = true;
      for (titleField in titleFields) {
         if (titleField.rawText.trim().length() > 0) {
            saveDisabled = false;
         }
      }
      if (sizeof roleChoiceBox.items > 1) {
         if (selectedFunctionalRole == null) {
            saveDisabled = true;
         }
      }
      saveButton.disable = saveDisabled;
   }

   public override function getTitle(): String {
      //      titleTextBox.rawText.trim();
      ""
   }

   public override function setTitle(title: String): Void {
      //      titleTextBox.text = title;
      updateSaveState();
   }

   public override function setFunctionalRoleContainers(functionalRoleContainers: FunctionalRoleContainer[]): Void {
      roleChoiceBox.items = functionalRoleContainers;
      roleChoiceBox.disable = sizeof roleChoiceBox.items <= 1;
   }

   public override function setFunctionalRole(functionalRole: EloFunctionalRole): Void {
      for (item in roleChoiceBox.items) {
         def functionalRoleContainer = item as FunctionalRoleContainer;
         if (functionalRoleContainer.functionalRole == functionalRole) {
            roleChoiceBox.select(indexof item);
            updateSaveState();
            return;
         }
      }
      roleChoiceBox.clearSelection();
      updateSaveState();
   }

   public override function getFunctionalRole(): EloFunctionalRole {
      return selectedFunctionalRole
   }

   public-init var imageLoader: ImageLoader;
   public-init var imageExtension = ".png";
   public-init var tbi: ToolBrokerAPI;
   def languageList = ["en", "nl", "et", "fr", "el", "no"];
   def flagViews = [flag_en, flag_nl, flag_et, flag_fr, flag_el, flag_no];
   def titleFields = [title_en, title_nl, title_et, title_fr, title_el, title_no];
   def titleKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TITLE);

   init {
      if (imageLoader == null) {
         imageLoader = ImageLoader.getImageLoader();
      }
      FX.deferAction(fillInTitles);
   }

   function fillInTitles(): Void {
      def titles = elo.getMetadata().getMetadataValueContainer(titleKey).getValuesI18n();
      for (language in languageList) {
         def flagImage = imageLoader.getImage("flags/{language}{imageExtension}");
         flagViews[indexof language].image = flagImage;
         def languageLocale = new Locale(language);
         def title = titles.get(languageLocale) as String;
         titleFields[indexof language].text = title;
      }
      updateSaveState();
   }

   public override function setTitleAndLanguagesInElo(): Void{
      def titles = elo.getMetadata().getMetadataValueContainer(titleKey).getValuesI18n() as Map;
      titles.clear();
      def eloContentLanguageList = new ArrayList();
      for (language in languageList) {
         def languageLocale = new Locale(language);
         def title = titleFields[indexof language].rawText.trim();
         if (title.length()>0){
            titles.put(languageLocale,title);
            eloContentLanguageList.add(languageLocale);
         }
      }
      elo.getContent().setLanguages(eloContentLanguageList);
   }

}

function run (): Void {
    var design = MultiLanguageAuthorSaveAsNodeDesign {};

    javafx.stage.Stage {
        title: "MultiLanguageAuthorSaveAsNodeDesign"
        scene: design.getDesignScene ()
    }
}
