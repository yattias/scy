/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.missionmodel;
import eu.scy.common.mission.MissionAnchor;
import java.util.List;
import eu.scy.common.mission.Las;
import java.net.URI;
import java.lang.String;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.mission.ColorSchemeId;
import javafx.scene.paint.Color;

/**
 * @author SikkenJ
 */

public class MissionAnchorFX extends MissionAnchor {
   
   protected var missionUtils: MissionUtils;
   public var missionAnchor:MissionAnchor on replace { newMissionAnchor() };

//   public var id:String;
   public var eloUri: URI on replace { missionAnchor.setEloUri(eloUri)};
   public var scyElo:ScyElo;
   public var iconType:String;
   public var inputAnchors: MissionAnchorFX[];
   public var relationNames: String[];
//   public var mainAnchor = true;
   public var exists = false;
   public var color = Color.LIGHTGRAY;
   public var iconCharacter = "?";
   public var title = "?";
   public var loEloUris: URI[] on replace{ missionAnchor.setLoEloUris(missionUtils.getUriList(loEloUris))};
//   public var metadata: IMetadata;
   public var las:LasFX;

   public var targetDescriptionUri:URI;
   public var assignmentUri: URI;
   public var resourcesUri: URI;
   public var colorScheme: ColorSchemeId;

   function newMissionAnchor() {
        eloUri = missionAnchor.getEloUri();
        scyElo = missionAnchor.getScyElo();
        iconType = missionAnchor.getIconType();
        inputAnchors = missionUtils.getMissionAnchorFXSequence(missionAnchor.getInputMissionAnchors());
        relationNames = missionUtils.getStringSequence(missionAnchor.getRelationNames());
        exists = missionAnchor.isExisting();
        title = scyElo.getTitle();
        loEloUris = missionUtils.getUriSequence(missionAnchor.getLoEloUris());
        las = missionUtils.getLasFX(missionAnchor.getLas());
        targetDescriptionUri = missionAnchor.getTargetDescriptionUri();
        assignmentUri = missionAnchor.getAssignmentUri();
        resourcesUri = missionAnchor.getResourcesUri();
        colorScheme = missionAnchor.getColorSchemeId();
    }

   override public function getInputMissionAnchors () : List {
        return missionAnchor.getInputMissionAnchors();
    }

    override public function getLas () : Las {
        return las.las;
    }

    override public function getAssignmentUri () : URI {
        return assignmentUri;
    }

    override public function getRelationNames () : List {
        return missionAnchor.getRelationNames();
    }

    override public function setLas (las : Las) : Void {
        missionAnchor.setLas(las);
    }

    override public function getResourcesUri () : URI {
        return resourcesUri;
    }

    override public function setExisting (existing : Boolean) : Void {
        missionAnchor.setExisting(existing);
        exists = existing;
    }

    override public function isExisting () : Boolean {
        return exists;
    }

    override public function getIconType () : String {
        return iconType;
    }

    override public function setInputMissionAnchors (inputMissionAnchors : List) : Void {
        missionAnchor.setInputMissionAnchors(inputMissionAnchors);
        inputAnchors = missionUtils.getMissionAnchorFXSequence(inputMissionAnchors);
    }

    override public function setScyElo (scyElo : ScyElo) : Void {
        missionAnchor.setScyElo(scyElo);
        this.scyElo = scyElo;
    }

    override public function getTargetDescriptionUri () : URI {
        return targetDescriptionUri;
    }

    override public function getScyElo () : ScyElo {
        return scyElo;
    }

    override public function getEloUri () : URI {
        return eloUri;
    }

    override public function setEloUri (eloUri : URI) : Void {
        missionAnchor.setEloUri(eloUri);
        this.eloUri = eloUri;
    }

    override public function setAssignmentUri (assignmentUri : URI) : Void {
        missionAnchor.setAssignmentUri(assignmentUri);
        this.assignmentUri = assignmentUri;
    }

    override public function getLoEloUris () : List {
        return missionAnchor.getLoEloUris();
    }

    override public function setLoEloUris (loEloUris : List) : Void {
        missionAnchor.setLoEloUris(loEloUris);
        this.loEloUris = missionUtils.getUriSequence(loEloUris);
    }

    override public function getColorSchemeId () : ColorSchemeId {
        return colorScheme;
    }

}
