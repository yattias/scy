/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import eu.scy.common.scyelo.ScyElo;

/**
 *
 * @author SikkenJ
 */
public class ScySearchResult
{

   private final ScyElo scyElo;
   private final double relevance;

   public ScySearchResult(ScyElo scyElo, double relevance)
   {
      this.scyElo = scyElo;
      this.relevance = relevance;
   }

   public double getRelevance()
   {
      return relevance;
   }

   public ScyElo getScyElo()
   {
      return scyElo;
   }
}
