/*
 * Main.fx
 *
 * Created on 16-mrt-2010, 10:44:55
 */

package eu.scy.client.desktop.scydesktop.uicontrols.test;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.log4j.InitLog4JFX;
import eu.scy.client.desktop.desktoputils.art.ArtSource;

/**
 * @author sikken
 */

InitLog4JFX.initLog4J();


ArtSource.artSource = "file:/D:/projects/SCY/code/UIControls/images/";
ArtSource.artSource = "file:/n:/Users/IST/Shared/Projects/SCY/Webdesign_marita/UIControls/images/";
ArtSource.artSource = "file:images/";

var mainDesign = mainDesign{};
mainDesign.stack.content = [
      mainDesign.testMultiImageButton,
      mainDesign.testDynamicTypeBackground,
      mainDesign.testLanguageSelector,
      mainDesign.testImages
   ];

//mainDesign.toggleGroup.selectedButton = mainDesign.multiImageButtonRadioButton;


Stage {
    title: "UIControls test"
    scene: mainDesign.getDesignScene();
    }
