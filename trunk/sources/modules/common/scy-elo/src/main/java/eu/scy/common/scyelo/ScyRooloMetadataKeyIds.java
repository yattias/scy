/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.scyelo;

import roolo.elo.api.metadata.IMetadataKeyIdDefinition;

/**
 *
 * @author sikken
 */
public enum ScyRooloMetadataKeyIds implements IMetadataKeyIdDefinition
{

   LEARNING_ACTIVITY("learningActivity"),
   ACCESS("access"),
   MISSION_RUNTIME("missionRuntime"),
   MISSION("mission"),
   MISSION_RUNNING("missionRunning"),
   MISSION_SPECIFICATION_ELO("missionSpecificationElo"),
   LAS("las"),
   ANCHOR_ID("anchorId"),
   ACTIVE_ANCHOR_ELO("activeAnchorElo"),
   CONTAINS_ASSIGMENT_ELO("containsAssignment"),
   LOGICAL_TYPE("logicalType"),
   FUNCTIONAL_TYPE("functionalType"),
   EXTERNAL_DOC("externalDoc"),
   ICON_TYPE("iconType"),
   COLOR_SCHEME_ID("colorSchemeId"),
   MUC_ID("mucId"),
   ASSIGNMENT_URI("assignmentUri"),
   RESOURCES_URI("resourcesUri"),
   OBLIGATORY_IN_PORTFOLIO("obligatoryInPortfolio"),
   FEEDBACK_ON("feedbackOn"),
   DATE_FIRST_USER_SAVE("dateFirstUserSave"),
   CREATOR("creator"),
   USER_RUNNING_MISSION("userRunningMission");

   private final String id;

   private ScyRooloMetadataKeyIds(String id)
   {
      this.id = id;
   }

   @Override
   public String getId()
   {
      return id;
   }
}
