/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.scyelo;

import java.net.URI;

import roolo.elo.api.IELO;

/**
 * 
 * @author SikkenJ
 */
public class ContentTypedScyElo<E> extends ScyElo
{
   private final String technicalFormat;
   private ScyEloContentCreator<E> scyEloContentCreator;
   private E typedContent;

   public ContentTypedScyElo(IELO elo, RooloServices rooloServices,
            ScyEloContentCreator<E> scyEloContentCreator, String technicalFormat)
   {
      super(elo, rooloServices);
      assert scyEloContentCreator != null;
      this.scyEloContentCreator = scyEloContentCreator;
      this.technicalFormat = technicalFormat;
      verifyTechnicalFormat();
      eloContentChanged();
   }

   @Override
   protected void verifyTechnicalFormat()
   {
      if (technicalFormat != null)
      {
         if (!technicalFormat.equals(getTechnicalFormat()))
         {
            throw new IllegalStateException("elo is should have the technical format '"
                     + technicalFormat + "', but it is '" + getTechnicalFormat() + ", uri="
                     + getUri());
         }
      }
   }

   @Override
   public boolean reloadFrom(URI eloUri)
   {
      final boolean reloaded = super.reloadFrom(eloUri);
      if (reloaded)
      {
         eloContentChanged();
      }
      return reloaded;
   }

   @Override
   public IELO getUpdatedElo()
   {
      typedContentChanged();
      return super.getUpdatedElo();
   }

   public final void eloContentChanged()
   {
      typedContent = scyEloContentCreator.createScyEloContent(this);
   }

   public final void typedContentChanged()
   {
      scyEloContentCreator.updateEloContent(this);
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
