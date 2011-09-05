/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

/**
 *
 * @author SikkenJ
 */
public interface QuerySelecterCreator
{

   public String getId();

   public QuerySelecter createQuerySelecter(QuerySelecterUsage querySelectorUsage);

}
