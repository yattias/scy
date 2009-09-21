/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

import org.w3c.dom.Document;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.simple.XHTMLPanel;

/**
 *
 * @author sikkenj
 */
public class MyXhtmlPanel extends XHTMLPanel
{

   public MyXhtmlPanel(UserAgentCallback uac)
   {
      super(uac);
   }

   public MyXhtmlPanel()
   {
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
}
