/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.search.queryselecters;

import eu.scy.client.desktop.desktoputils.StringUtils;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecter;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterCreator;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterFactory;
import eu.scy.client.desktop.scydesktop.tools.search.QuerySelecterUsage;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 *
 * @author SikkenJ
 */
public class QuerySelecterFactoryImpl implements QuerySelecterFactory {

   public final static String createQuerySelectersTagName = "createQuerySelecters";
   public final static String createQuerySelecterTagName = "createQuerySelecter";
   private final static String idTagName = "id";
   private final static Logger logger = Logger.getLogger(QuerySelecterFactoryImpl.class);

   private List<QuerySelecterCreator> querySelecterCreators = new ArrayList<QuerySelecterCreator>();

   @Override
   public void registerQuerySelecterCreator(QuerySelecterCreator querySelecterCreator)
   {
      if (!querySelecterCreators.contains(querySelecterCreator)){
         if (StringUtils.isEmpty(querySelecterCreator.getId())){
            throw new IllegalArgumentException("querySelecterCreator must have an id");
         }
         if (findQuerySelecterCreator(querySelecterCreator.getId())!=null){
            throw new IllegalArgumentException("there is allready a querySelecterCreator with id " + querySelecterCreator.getId());
         }
         querySelecterCreators.add(querySelecterCreator);
      }
   }

   private QuerySelecterCreator findQuerySelecterCreator(String id){
      for (QuerySelecterCreator querySelecterCreator : querySelecterCreators){
         if (id.equals(querySelecterCreator.getId())){
            return querySelecterCreator;
         }
      }
      return null;
   }

   @Override
   public List<QuerySelecter> createQuerySelecters(QuerySelecterUsage querySelectorUsage)
   {
      List<QuerySelecter> querySelecters = new ArrayList<QuerySelecter>();
      for (QuerySelecterCreator querySelecterCreator: querySelecterCreators){
         QuerySelecter querySelecter = querySelecterCreator.createQuerySelecter(querySelectorUsage);
         if (querySelecter!=null){
            querySelecters.add(querySelecter);
         }
      }
      return querySelecters;
   }

   @Override
   public List<QuerySelecter> createQuerySelecters(Element root, QuerySelecterUsage querySelectorUsage)
   {
      List<QuerySelecter> querySelecters = new ArrayList<QuerySelecter>();
      Element createQuerySelectersElement = root.getChild(createQuerySelectersTagName);
      final List<Element> querySelecterChildren = createQuerySelectersElement.getChildren(createQuerySelecterTagName);
      for (Element querySelecterChild : querySelecterChildren)
      {
         String id = querySelecterChild.getAttributeValue(idTagName);
         QuerySelecterCreator querySelecterCreator = findQuerySelecterCreator(id);
         if (querySelecterCreator!=null){
            QuerySelecter querySelecter = querySelecterCreator.createQuerySelecter(querySelectorUsage);
            if (querySelecter!=null){
               querySelecter.setState(querySelecterChild);
            }else{
               logger.warn("no QuerySelecter created for if " + id + " and QuerySelecterUsage " + querySelectorUsage);
            }
         } else {
            logger.warn("failed to find QuerySelecter with id " + id);
         }
      }
      return querySelecters;
   }

   @Override
   public Element createQuerySelectersXml(List<QuerySelecter> querySelecters)
   {
      Element root = new Element(createQuerySelectersTagName);
      for (QuerySelecter querySelecter : querySelecters){
         Element querySelecterXml = new Element(createQuerySelecterTagName);
            querySelecter.addState(querySelecterXml);
         querySelecterXml.setAttribute(idTagName, querySelecter.getId());
         root.addContent(querySelecterXml);
      }
      return root;
   }



}
