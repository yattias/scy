/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

import eu.scy.common.mission.impl.jdom.JDomConversionUtils;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author SikkenJ
 */
public class ScySearchResultXmlUtils
{

   public static final String SCYSEARCHRESULTS = "scySearchResults";
   private static final String SCYSEARCHRESULT = "scySearchResult";
   private static final String ELO_URI = "eloUri";
   private static final String RELEVANCE = "relevance";

   private final ToolBrokerAPI tbi;

   public ScySearchResultXmlUtils(ToolBrokerAPI tbi)
   {
      this.tbi = tbi;
   }

   public ScySearchResult[] scySearchResultsFromXml(final Element root) throws URISyntaxException
   {
      final List<ScySearchResult> scySearchResults = new ArrayList<ScySearchResult>();
      final List<Element> scySearchResultChildren = root.getChildren(SCYSEARCHRESULT);
      for (Element scySearchResultChild : scySearchResultChildren)
      {
         URI eloUri = JDomConversionUtils.getUriValue(scySearchResultChild, ELO_URI);
         double relevance = JDomConversionUtils.getDoubleValue(scySearchResultChild,RELEVANCE);
         ScyElo scyElo = ScyElo.loadElo(eloUri, tbi);
         scySearchResults.add(new ScySearchResult(scyElo, relevance));
      }
      return scySearchResults.toArray(new ScySearchResult[0]);
   }

   public Element scySearchResultsToXml(final ScySearchResult[] scySearchResults)
   {
      final Element scySearchResultsXml = new Element(SCYSEARCHRESULTS);
      for (ScySearchResult scySearchResult : scySearchResults)
      {
         final Element scySearchResultXml = new Element(SCYSEARCHRESULT);
         scySearchResultXml.addContent(JDomConversionUtils.createElement(ELO_URI, scySearchResult.getScyElo().getUri()));
         scySearchResultXml.addContent(JDomConversionUtils.createElement(RELEVANCE, scySearchResult.getRelevance()));
         scySearchResultsXml.addContent(scySearchResultXml);
      }
      return scySearchResultsXml;
   }
}
