/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import eu.scy.common.mission.Las;
import java.lang.String;
import eu.scy.common.mission.LasType;
import java.util.List;
import eu.scy.common.mission.MissionAnchor;
import java.net.URI;
import javafx.scene.paint.Color;

/**
 * @author SikkenJ
 */
public class LasFX extends Las {

   protected var missionUtils: MissionUtils;
   public var las: Las = null on replace { newLas() };
   public var id = "?";
   public var xPos: Number = 0 on replace { las.setXPos(xPos) };
   public var yPos: Number = 0 on replace { las.setYPos(yPos) };
   public var loEloUris: URI[] on replace { las.setLoEloUris(missionUtils.getUriList(loEloUris)) };
   public var nextLasses: LasFX[];
   public var previousLasses: LasFX[];
   public var mainAnchor: MissionAnchorFX;
   public var initialAnchorToOpen: MissionAnchorFX;
   public var intermediateAnchors: MissionAnchorFX[];
   public var otherEloUris: URI[] on replace {
              las.setOtherEloUris(missionUtils.getUriList(otherEloUris))
           };
   public var color = Color.LIGHTGRAY;
   public var toolTip: String;
   public var title: String;
   public var exists = false;
   public var instructionUri: URI;
   public var lasType: LasType;
   public var selectedAnchor: MissionAnchorFX on replace { las.setSelectedMissionAnchor(selectedAnchor.missionAnchor) };
   public var nrOfTimesInstructionShowed = 0;

   public override function toString(): String {
      return "LasFX\{id:{id}\}";
   }

   function newLas(): Void {
      id = las.getId();
      xPos = las.getXPos();
      yPos = las.getYPos();
      loEloUris = missionUtils.getUriSequence(las.getLoEloUris());
      nextLasses = missionUtils.getLasFXSequence(las.getNextLasses());
      previousLasses = missionUtils.getLasFXSequence(las.getPreviousLasses());
      mainAnchor = missionUtils.getMissionAnchorFX(las.getMissionAnchor());
      intermediateAnchors = missionUtils.getMissionAnchorFXSequence(las.getIntermediateAnchors());
      otherEloUris = missionUtils.getUriSequence(las.getOtherEloUris());
      toolTip = las.getToolTip();
      title = las.getTitle();
      exists = las.isExisting();
      instructionUri = las.getInstructionUri();
      lasType = las.getLasType();
      selectedAnchor = missionUtils.getMissionAnchorFX(las.getSelectedMissionAnchor());
      initialAnchorToOpen = if (las.getInitialMissionAnchorToOpen() != null) then missionUtils.getMissionAnchorFX(las.getInitialMissionAnchorToOpen()) else null;
   }

   override public function isExisting(): Boolean {
      return exists;
   }

   override public function getToolTip(): String {
      return toolTip;
   }

   override public function getTitle(): String {
      return title;
   }

   override public function setTitle(title: String): Void {
      las.setTitle(title);
   }

   override public function getXPos(): Number {
      return xPos;
   }

   override public function setYPos(y: Number): Void {
      las.setYPos(y);
      yPos = y;
   }

   override public function getLasType(): LasType {
      return lasType;
   }

   override public function getLoEloUris(): List {
      return las.getLoEloUris();
   }

   override public function getMissionAnchor(): MissionAnchor {
      return mainAnchor.missionAnchor;
   }

   override public function getNextLasses(): List {
      return las.getNextLasses();
   }

   override public function setNextLasses(nextLasses: List): Void {
      las.setNextLasses(nextLasses);
   }

   override public function getSelectedMissionAnchor(): MissionAnchor {
      return selectedAnchor.missionAnchor
   }

   override public function getYPos(): Number {
      return yPos;
   }

   override public function setOtherEloUris(otherEloUris: List): Void {
      las.setOtherEloUris(otherEloUris);
      this.otherEloUris = missionUtils.getUriSequence(otherEloUris);
   }

   override public function setSelectedMissionAnchor(missionAnchor: MissionAnchor): Void {
      las.setSelectedMissionAnchor(missionAnchor);
      selectedAnchor = missionUtils.getMissionAnchorFX(las.getSelectedMissionAnchor());
   }

   override public function getId(): String {
      return id;
   }

   override public function getInstructionUri(): URI {
      return instructionUri;
   }

   override public function getOtherEloUris(): List {
      return las.getOtherEloUris();
   }

   override public function setExisting(existing: Boolean): Void {
      las.setExisting(existing);
      exists = existing;
   }

   override public function setXPos(x: Number): Void {
      las.setXPos(x);
      xPos = x;
   }

   override public function getIntermediateAnchors(): List {
      las.getIntermediateAnchors();
   }

   override public function getPreviousLasses(): List {
      las.getPreviousLasses();
   }

   override public function setLoEloUris(loEloUris: List): Void {
      las.setLoEloUris(loEloUris);
      this.loEloUris = missionUtils.getUriSequence(loEloUris);
   }

   override public function getInitialMissionAnchorToOpen(): MissionAnchor {
      return las.getInitialMissionAnchorToOpen();
   }

}
