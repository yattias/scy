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
   MUC_ID("mucId");

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
