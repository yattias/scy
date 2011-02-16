/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoTypes;
import eu.scy.client.desktop.scydesktop.scywindows.ShowMoreInfo;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xhtmlrenderer.render.Box;
import org.xhtmlrenderer.swing.BasicPanel;
import org.xhtmlrenderer.swing.LinkListener;

/**
 *
 * @author sikken
 */
public class MyLinkListener extends LinkListener
{

   private final static Logger logger = Logger.getLogger(MyLinkListener.class);
   private final static String EXTERNAL_TARGET = "_external";
   private final static String EXTERNAL_MORE_ASSIGNMENT = "_moreAssignment";
   private final static String EXTERNAL_MORE_RESOURCES = "_moreResources";
   private final static String EXTERNAL_CSS = "external";
   private ShowMoreInfo showMoreInfo;
   private URI eloUri;
   private String baseUrl;

   public void setShowMoreInfo(ShowMoreInfo showMoreInfo)
   {
      this.showMoreInfo = showMoreInfo;
   }

   public void setEloUri(URI eloUri)
   {
      this.eloUri = eloUri;
   }

   public void setBaseUrl(String baseUrl)
   {
      int lastSlashPos = baseUrl.lastIndexOf('/');
      if (lastSlashPos >= 0)
      {
         this.baseUrl = baseUrl.substring(0, lastSlashPos + 1);
      }
      else
      {
         this.baseUrl = baseUrl;
      }
   }

   @Override
   public void onMouseUp(BasicPanel panel, Box box)
   {
      checkForLink(panel, box);
   }

   // tests whether the element associated with the Box has an associated URI (e.g. is an anchor), and if so, calls
   // back to the panel to navigate to that URI
   private void checkForLink(BasicPanel panel, Box box)
   {
      if (box == null || box.getElement() == null)
      {
         return;
      }

      String uri = findLink(panel, box.getElement());

      if (uri != null)
      {
         try
         {
            linkClicked(panel, uri);
         }
         catch (Exception e)
         {
            logger.info("exception during loading of " + uri, e);
         }
      }
   }

   // looks to see if the given element has a link URI associated with it; if so, returns the URI as a string, if
   // not, returns null
   private String findLink(BasicPanel panel, Element e)
   {
      String uri = null;

      for (Node node = e; node.getNodeType() == Node.ELEMENT_NODE; node = node.getParentNode())
      {
         uri = panel.getSharedContext().getNamespaceHandler().getLinkUri((Element) node);

         if (uri != null)
         {
            try
            {
               URI linkUri = getLinkUri(uri);
               String target = panel.getSharedContext().getNamespaceHandler().getAttributeValue((Element) node, "target");
               logger.debug("link target: " + target);
               if (EXTERNAL_TARGET.equalsIgnoreCase(target))
               {
                  logger.info("launch external browser for " + uri);
                  BareBonesBrowserLaunch.openURL(uri);
                  uri = null;
               }
               else if (EXTERNAL_MORE_ASSIGNMENT.equalsIgnoreCase(target))
               {
                  if (showMoreInfo != null)
                  {
                     logger.info("show more assignment with " + uri);
                     showMoreInfo.showMoreInfo(linkUri, MoreInfoTypes.ASSIGNMENT, eloUri);
                  }
                  else
                  {
                     logger.info("could not show more assignment (showMoreInfo==null) with " + uri);
                  }
                  uri = null;
               }
               else if (EXTERNAL_MORE_RESOURCES.equalsIgnoreCase(target))
               {
                  if (showMoreInfo != null)
                  {
                     logger.info("show more resources with " + uri);
                     showMoreInfo.showMoreInfo(linkUri, MoreInfoTypes.RESOURCES, eloUri);
                  }
                  else
                  {
                     logger.info("could not show more resources (showMoreInfo==null) with " + uri);
                  }
                  uri = null;
               }
               break;
            }
            catch (URISyntaxException ex)
            {
               logger.error("error in uri", ex);
            }
         }
      }

      return uri;
   }

   private URI getLinkUri(String uri) throws URISyntaxException
   {
      URI linkUri = new URI(uri);
      try
      {
         // check if the linkUri is a complete uri, including http:// and so
         URL linkUrl = linkUri.toURL();
         return linkUri;
      }
      catch (Exception ex)
      {
         logger.debug("uri (" + uri + ") is not complete: " + ex.getMessage());
      }
      linkUri = new URI(baseUrl + uri);
      return linkUri;
   }
}
