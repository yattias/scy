/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.scyelo;

import roolo.elo.api.IELO;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 *
 * @author SikkenJ
 */
public class ContentTypedScyElo<E> extends ScyElo
{

   private ScyEloContentCreator<E> scyEloContentCreator;
   private E typedContent;

   public ContentTypedScyElo(IELO elo, ToolBrokerAPI tbi, ScyEloContentCreator<E> scyEloContentCreator)
   {
      super(elo, tbi);
      assert scyEloContentCreator!=null;
      this.scyEloContentCreator = scyEloContentCreator;
      eloContentChanged();
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
