/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

import eu.scy.common.scyelo.ScyElo;

/**
 *
 * @author SikkenJ
 */
public class ScySearchResult
{

   private final ScyElo scyElo;
   private final double relevance;
   private Object eloIcon;

   public ScySearchResult(ScyElo scyElo, double relevance)
   {
      this.scyElo = scyElo;
      this.relevance = relevance;
   }

   @Override
   public String toString()
   {
      return "ScySearchResult{" + "scyElo=" + scyElo + "relevance=" + relevance + '}';
   }

   public double getRelevance()
   {
      return relevance;
   }

   public ScyElo getScyElo()
   {
      return scyElo;
   }

   public Object getEloIcon()
   {
      return eloIcon;
   }

   public void setEloIcon(Object eloIcon)
   {
      this.eloIcon = eloIcon;
   }
   
}
