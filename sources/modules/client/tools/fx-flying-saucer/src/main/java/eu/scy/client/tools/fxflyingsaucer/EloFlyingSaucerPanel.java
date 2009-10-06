/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import java.net.URI;
import java.net.URL;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.jdom.Element;
import roolo.api.IRepository;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

/**
 *
 * @author sikkenj
 */
public class EloFlyingSaucerPanel extends FlyingSaucerPanel
{

   private static final Logger logger = Logger.getLogger(EloFlyingSaucerPanel.class);
   private static final String urlTagName = "url";
   private static final String scyUrlType = "scy/url";
   private JDomStringConversion jdomStringConversion = new JDomStringConversion();
   private IRepository repository;
   private IELOFactory eloFactory;
   private IMetadataTypeManager metadataTypeManager;
   private IMetadataKey titleKey;
   private IMetadataKey technicalFormatKey;

   public void setRepository(IRepository repository)
   {
      this.repository = repository;
   }

   public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
   {
      this.metadataTypeManager = metadataTypeManager;
      titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
      logger.info("retrieved key " + titleKey.getId());
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
   }

   public void setHomeElo(URI uri)
   {
      IELO elo = repository.retrieveELO(uri);
      if (elo == null)
      {
         logger.error("the home elo does not exists: " + uri);
         return;
      }
      String homeUrl = getUrlFromContent(elo.getContent());
      setHomeUrl(homeUrl);
   }

   @Override
   protected void saveUrlAsHome(URL url)
   {
      super.saveUrlAsHome(url);
      if (url != null)
      {
         String name = JOptionPane.showInputDialog("Enter url home name:");
         if (name != null && name.length() > 0)
         {
            IELO newHomeElo = eloFactory.createELO();
            newHomeElo.getMetadata().getMetadataValueContainer(titleKey).setValue(name);
            newHomeElo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyUrlType);
            setUrlInContent(newHomeElo.getContent(), url.toString());
            repository.addNewELO(newHomeElo);
         }
      }
   }

   private String getUrlFromContent(IContent content)
   {
      Element contentXml = jdomStringConversion.stringToXml(content.getXmlString());
      return contentXml.getTextTrim();
   }

   private void setUrlInContent(IContent content, String url)
   {
      Element contentXml = new Element(urlTagName);
      contentXml.setText(url);
      content.setXmlString(jdomStringConversion.xmlToString(contentXml));
   }
}
