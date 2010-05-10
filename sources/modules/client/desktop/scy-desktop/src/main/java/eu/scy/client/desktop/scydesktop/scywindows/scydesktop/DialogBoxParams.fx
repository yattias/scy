/*
 * DialogBoxParams.fx
 *
 * Created on 07.05.2010, 11:50:39
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;
import eu.scy.client.desktop.scydesktop.ScyDesktop;

/**
 * @author sven
 */

public class DialogBoxParams {

        public-init var dialogWidth:Double = DialogBox.DEFAULT_WIDTH;
        public-init var scyDesktop:ScyDesktop;
        public-init var modal:Boolean = true;
        public-init var indicateFocus:Boolean = true;
        public-init var text:String;
        public-init var title:String;
        /**
        * For use in MessageBoxes (OK_DIALOG)
        */
        public-init var okAction:function() = function() {};

        /**
        * For use in OptionBoxes (YES_NO_DIALOG, OK_CANCEL_DIALOG)
        */
        public-init var yesAction:function() = function() {};
        public-init var noAction:function() = function() {};
        public-init var cancelAction:function() = function() {};
        
        public-init var dialogType:DialogType = DialogType.OK_DIALOG;
}
