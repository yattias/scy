/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import eu.scy.common.scyelo.ScyElo;
import roolo.elo.api.IELO;
import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.SimpleScyDesktopEloSaver.FunctionalRoleContainer;

/**
 * @author SikkenJ
 */

public mixin class EloSaveAsMixin {

   public var saveAction: function(: EloSaveAsMixin): Void;
   public var cancelAction: function(: EloSaveAsMixin): Void;
   // store information of caller
   public var elo: IELO;
   public var myElo: Boolean;
   public var eloSaverCallBack: EloSaverCallBack;
   public var modalDialogBox: ModalDialogBox;
   public var scyElo: ScyElo;

   var localTitle = "";
   var localFunctionalRole: EloFunctionalRole = null;

   public function getTitle(): String {
      localTitle
   }

   public function setTitle(title:String): Void{
      localTitle = title;
   }

   public function setFunctionalRoleContainers(functionalRoleContainers: FunctionalRoleContainer[]): Void{
   }

   public function setFunctionalRole(functionalRole: EloFunctionalRole): Void{
      this.localFunctionalRole = functionalRole
   }

   public function getFunctionalRole(): EloFunctionalRole{
      return localFunctionalRole;
   }

   public abstract function getDesignNodes (): javafx.scene.Node[];

}
