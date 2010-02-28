/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

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
   private final static String EXTERNAL_CSS = "external";

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
            boolean useExternalBrowser = false;
            String target = panel.getSharedContext().getNamespaceHandler().getAttributeValue((Element) node, "target");
            logger.debug("link target: " + target);
            if (EXTERNAL_TARGET.equalsIgnoreCase(target))
            {
               useExternalBrowser = true;
            }
            if (useExternalBrowser)
            {
               logger.info("launch external browser for " + uri);
               BareBonesBrowserLaunch.openURL(uri);
               uri = null;
            }
            break;
         }
      }

      return uri;
   }
}
