/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.hacks;

import java.util.List;
import roolo.search.AndQuery;
import roolo.search.IQuery;
import roolo.search.IQueryComponent;
import roolo.search.MetadataQueryComponent;
import roolo.search.OrQuery;
import roolo.search.Query;
import roolo.search.StringQuery;

/**
 *
 * @author SikkenJ
 */
public class QueryUtils
{

   public String queryToString(IQuery query)
   {
      StringBuilder builder = new StringBuilder();
      if (query instanceof Query)
      {
         addQuery(builder, (Query) query);
      }
      else
      {
         builder.append(query);
      }
      return builder.toString();
   }

   private void addQuery(StringBuilder builder, Query query)
   {
      builder.append("Query{");
      builder.append("queryComponent");
      addQueryComponent(builder, query.getQueryComponent());
      builder.append("}");
   }

   private void addQueryComponent(StringBuilder builder, IQueryComponent queryComponent)
   {
      if (queryComponent instanceof MetadataQueryComponent)
      {
         addMetadataQueryComponent(builder, (MetadataQueryComponent) queryComponent);
      }
      else if (queryComponent instanceof StringQuery)
      {
         addStringQuery(builder, (StringQuery) queryComponent);
      }
      else if (queryComponent instanceof AndQuery)
      {
         addAndQuery(builder, (AndQuery) queryComponent);
      }
      else if (queryComponent instanceof OrQuery)
      {
         addStringQuery(builder, (StringQuery) queryComponent);
      }
      else
      {
         builder.append(queryComponent);
      }
   }

   private void addMetadataQueryComponent(StringBuilder builder, MetadataQueryComponent metadataQueryComponent)
   {
      builder.append("MetadataQueryComponent{");
      builder.append("key=");
      builder.append(metadataQueryComponent.getKey());
      builder.append(",searchOperation=");
      builder.append(metadataQueryComponent.getSearchOperation());
      builder.append(",value=");
      builder.append(metadataQueryComponent.getValue());
      builder.append("}");
   }

   private void addQueries(StringBuilder builder, List<IQueryComponent> queries)
   {
      boolean firstQuery = true;
      for (IQueryComponent queryComponent : queries)
      {
         if (firstQuery)
         {
            firstQuery = false;
         }
         else
         {
            builder.append(",");
         }
         addQueryComponent(builder, queryComponent);
      }
   }

   private void addStringQuery(StringBuilder builder, StringQuery stringQuery)
   {
      builder.append("StringQuery{");
      builder.append("queryString=");
      builder.append(stringQuery.getQueryString());
      builder.append("}");
   }

   private void addAndQuery(StringBuilder builder, AndQuery andQuery)
   {
      builder.append("AndQuery{");
      addQueries(builder, andQuery.getQueries());
      builder.append("}");
   }

   private void addOrQuery(StringBuilder builder, OrQuery orQuery)
   {
      builder.append("OrQuery{");
      addQueries(builder, orQuery.getQueries());
      builder.append("}");
   }
}
