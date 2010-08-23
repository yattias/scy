/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop;

import eu.scy.client.desktop.scydesktop.config.Config;
import org.apache.log4j.Logger;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.MissionLocatorUtils.Missions;

/**
 * @author SikkenJ
 */
public class MissionLocator {

   def logger = Logger.getLogger(this.getClass());
   public-init var config: Config;
   public-init var userName: String;
   public-read var missionRuntimeElo: MissionRuntimeElo;

   var askUserForMissionNode: AskUserForMissionNode;

   public function locateMission(): Void {
      var missions = MissionLocatorUtils.findMissions(config, userName);
      if (missions.isEmpty()) {
         return;
      }
      if (missions.size() == 1) {
         if (missions.missionRuntimeElos.isEmpty()) {
            startNewMission(missions.missionSpecificationElos.get(0));
         } else {
            missionRuntimeElo = missions.missionRuntimeElos.get(0);
         }
         return;
      }
      // multiple missions possible, ask the user which one
      askUserForMission(missions);
   }

   function startNewMission(missionSpecificationElo: MissionSpecificationElo) {

   }

   function askUserForMission(missions: Missions) {
       askUserForMissionNode = AskUserForMissionNode{

       }
       askUserForMissionNode.goButton.action = missionSelected;
       var modelDialogBox = ModalDialogBox{
          closeAction:closeAction
          content:Group{
             content:askUserForMissionNode.getDesignRootNodes()
          }

          title:##"Select mission"
       }

   }

   function closeAction():Void{

   }

   function missionSelected():Void{

   }

}
