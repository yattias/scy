/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.scyelo;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;

/**
 *
 * @author SikkenJ
 */
public class ContentTypedScyElo<E> extends ScyElo
{

   private ScyEloContentCreator<E> scyEloContentCreator;
   private E typedContent;

   public ContentTypedScyElo(IELO elo, IMetadataTypeManager metadataTypemanager, ScyEloContentCreator<E> scyEloContentCreator)
   {
      super(elo, metadataTypemanager);
      assert scyEloContentCreator!=null;
      this.scyEloContentCreator = scyEloContentCreator;
      eloContentChanged();
   }

   public final void eloContentChanged()
   {
      typedContent = scyEloContentCreator.createScyEloContent(this);
   }

   public final void typedContentChanged()
   {
      scyEloContentCreator.updateEloContent(this);
   }

   @Override
   public IELO getElo()
   {
      typedContentChanged();
      return super.getElo();
   }

   public E getTypedContent()
   {
      return typedContent;
   }

   public void setTypeContent(E typedContent)
   {
      this.typedContent = typedContent;
   }
}
