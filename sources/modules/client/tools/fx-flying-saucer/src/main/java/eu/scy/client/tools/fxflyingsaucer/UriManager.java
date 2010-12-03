/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.swing.NaiveUserAgent;

/**
 *
 * @author sikkenj
 */
public class UriManager extends NaiveUserAgent
{

   private final static Logger logger = Logger.getLogger(UriManager.class);
   private String latestUri;
   private FlyingSaucerPanel flyingSaucerPanel;
   private List<String> history = new ArrayList<String>();
   private int historyIndex = -1;

   public UriManager(FlyingSaucerPanel flyingSaucerPanel)
   {
      this.flyingSaucerPanel = flyingSaucerPanel;
   }

   @Override
   public void setBaseURL(String url)
   {
      logger.debug("setBaseURL: " + url + ", old: " + super.getBaseURL());
      if (url == null)
      {
         return;
      }
      String displayedUrl = super.getBaseURL();
      super.setBaseURL(url);

      // setBaseURL is called by view when document is loaded
      if (displayedUrl == null)
      {
         return;
      }
      if (historyIndex >= 0)
      {
         String historic = history.get(historyIndex);
         if (historic.equals(url))
         {
            flyingSaucerPanel.updatePreviousNextButtonState();
            return; //moved in history
         }
      }
      historyIndex++;
      // delete all uris from history after the current uri
      for (int i = historyIndex; i < history.size(); history.remove(i))
      {
      }
      history.add(historyIndex, url);
      //logger.debug("history updated, index: " + historyIndex + ", history:\n" + getHistoryDisplay());
      flyingSaucerPanel.updatePreviousNextButtonState();
   }

   private String getHistoryDisplay()
   {
      StringBuilder display = new StringBuilder();
      for (int i = 0; i < history.size(); i++)
      {
         if (i > 0)
         {
            display.append("\n");
         }
         display.append(i);
         display.append(": ");
         display.append(history.get(i));
      }
      return display.toString();
   }

   @Override
   public String resolveURI(String uri)
   {
      latestUri = super.resolveURI(uri);
      //System.out.println("resolveURI(" + uri + "): " + latestUri);
      return latestUri;
   }

   @Override
   public XMLResource getXMLResource(String uri)
   {

      try
      {
         // just to force a check on a valid url
         URL url = new URL(uri);
         return super.getXMLResource(uri);
      }
      catch (Exception ex)
      {
         flyingSaucerPanel.handlePageLoadFailed(uri, ex);
         return null;
      }
   }

   public boolean hasPrevious()
   {
      return (historyIndex >= 0) && (historyIndex > 0);
   }

   public String getPrevious()
   {
      historyIndex--;
      return history.get(historyIndex);
   }

   public boolean hasNext()
   {
      return (historyIndex >= 0) && (historyIndex + 1 < history.size());
   }

   public String getNext()
   {
      historyIndex++;
      return history.get(historyIndex);
   }

   public void clear(){
      history.clear();
      historyIndex = -1;
   }
}
