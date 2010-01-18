/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop;

import roolo.elo.api.metadata.IMetadataKeyIdDefinition;

/**
 *
 * @author sikken
 */
public enum ScyRooloMetadataKeyIds implements IMetadataKeyIdDefinition
{

   LOGICAL_TYPE("logicalType"),
   FUNCTIONAL_TYPE("functionalType");
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
