/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterUsage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author SikkenJ
 */
public enum SimpleQueryOption
{

   TEST;
   private final Set<QuerySelecterUsage> validQuerySelecterUsage = new HashSet<QuerySelecterUsage>();

   private SimpleQueryOption()
   {
      validQuerySelecterUsage.addAll(Arrays.asList(QuerySelecterUsage.values()));
   }

   private SimpleQueryOption(QuerySelecterUsage querySelectorUsage)
   {
      validQuerySelecterUsage.add(querySelectorUsage);
   }

   public boolean validFor(QuerySelecterUsage querySelectorUsage)
   {
      return validQuerySelecterUsage.contains(querySelectorUsage);
   }
}
