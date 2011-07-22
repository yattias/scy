/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

import java.util.List;
import org.jdom.Element;

/**
 *
 * @author SikkenJ
 */
public interface QuerySelecterFactory
{

   public void registerQuerySelecterCreator(QuerySelecterCreator querySelecterCreator);

   public List<QuerySelecter> createQuerySelecters(QuerySelecterUsage querySelectorUsage);

   public List<QuerySelecter> createQuerySelecters(Element xml, QuerySelecterUsage querySelectorUsage);

   public Element createQuerySelectersXml(List<QuerySelecter> querySelecters);
}
