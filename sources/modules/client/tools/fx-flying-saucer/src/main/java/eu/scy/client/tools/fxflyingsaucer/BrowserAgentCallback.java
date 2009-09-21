/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxflyingsaucer;

import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.swing.NaiveUserAgent;

/**
 *
 * @author sikkenj
 */
public class BrowserAgentCallback extends NaiveUserAgent {

   private String latestUri;
   private FlyingSaucerPanel flyingSaucerPanel;

   public BrowserAgentCallback(FlyingSaucerPanel flyingSaucerPanel)
   {
      this.flyingSaucerPanel = flyingSaucerPanel;
   }

   @Override
   public String resolveURI(String uri)
   {
      latestUri = super.resolveURI(uri);
      System.out.println("resolveURI(" + uri + "): " + latestUri);
      return latestUri;
   }

   @Override
   public XMLResource getXMLResource(String uri)
   {
      try{
      return super.getXMLResource(uri);
      }
      catch (Exception ex)
      {
         flyingSaucerPanel.handlePageLoadFailed(uri,ex);
         return null;
      }
   }


}
