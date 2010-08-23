/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.scyelo;

import java.net.URI;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

/**
 *
 * @author SikkenJ
 */
public class ScyElo
{

   private IELO elo;
   private IMetadataTypeManager metadataTypemanager;
   private IMetadataKey titleKey;
   private IMetadataKey technicalFormatKey;

   public ScyElo(IELO elo, IMetadataTypeManager metadataTypemanager)
   {
      assert elo != null;
      assert metadataTypemanager != null;
      this.elo = elo;
      this.metadataTypemanager = metadataTypemanager;
      findMetadataKeys();
   }

   private void findMetadataKeys()
   {
      titleKey = findMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
      technicalFormatKey = findMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
   }

   private IMetadataKey findMetadataKey(String id)
   {
      IMetadataKey key = metadataTypemanager.getMetadataKey(id);
      if (key == null)
      {
         throw new IllegalStateException("the metadata key cannot be found, id: " + id);
      }
      return key;
   }

   public IELO getElo()
   {
      return elo;
   }

   public IMetadata getMetatadata()
   {
      return elo.getMetadata();
   }

   public URI getUri()
   {
      return elo.getUri();
   }

   public String getTechnicalFormat()
   {
      return (String) elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue();
   }

   public void setTechnicalFormat(String technicalFormat)
   {
      elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(technicalFormat);
   }

   public String getTitle()
   {
      return (String) elo.getMetadata().getMetadataValueContainer(titleKey).getValue();
   }

   public void setTitle(String title)
   {
      elo.getMetadata().getMetadataValueContainer(titleKey).setValue(title);
   }
}
