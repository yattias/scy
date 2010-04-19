/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

import java.net.MalformedURLException;
import java.net.URL;
import org.w3c.dom.Document;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;
import org.xhtmlrenderer.swing.CursorListener;
import org.xhtmlrenderer.swing.HoverListener;

/**
 *
 * @author sikkenj
 */
public class MyXhtmlPanel extends XHTMLPanel
{

   public MyXhtmlPanel(UserAgentCallback uac, boolean addMouseTracking)
   {
      super(uac);
      if (addMouseTracking)
      {
         addMouseTrackingListener(new HoverListener());
         addMouseTrackingListener(new MyLinkListener());
         //addMouseTrackingListener(new LinkListener());
         addMouseTrackingListener(new CursorListener());
         setFormSubmissionListener(new FormSubmissionListener()
         {

            public void submit(String query)
            {
               MyXhtmlPanel.this.setDocumentRelative(query);
            }
         });
      }
   }

   public MyXhtmlPanel()
   {
   }

   // overrride some methods to prevent null pointer exceptions

   @Override
   protected Document loadDocument(String uri)
   {
      XMLResource localXmlResource = sharedContext.getUac().getXMLResource(uri);
      if (localXmlResource != null)
      {
         return super.loadDocument(uri);
      }
      return null;
   }

   @Override
   public void setDocument(Document doc)
   {
      if (doc != null)
      {
         super.setDocument(doc);
      }
   }

   @Override
   public void setDocument(Document doc, String url)
   {
      if (doc != null)
      {
         super.setDocument(doc, url);
      }
   }

   // in case of problems with a misformed url, throw an appropriate exception
   @Override
   public URL getURL()
   {
      try
      {
         return new URL(getSharedContext().getUac().getBaseURL());
      }
      catch (MalformedURLException e)
      {
         throw new IllegalArgumentException(e);
      }
   }
}
