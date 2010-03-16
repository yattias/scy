/*
 * Main.fx
 *
 * Created on 16-mrt-2010, 10:44:55
 */

package eu.scy.client.desktop.scydesktop.uicontrols.test;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

/**
 * @author sikken
 */

InitLog4JFX.initLog4J();

var mainDesign = mainDesign{};
mainDesign.stack.content = [
      mainDesign.testMultiImageButton,
      mainDesign.testDynamicTypeBackground
   ];

mainDesign.toggleGroup.selectedButton = mainDesign.multiImageButtonRadioButton;

Stage {
    title: "UIControls test"
    scene: mainDesign.getDesignScene();
    }
