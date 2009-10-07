/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.jdom.Element;
import roolo.api.IRepository;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadata;
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
   private List<EloSavedListener> eloSavedListeners = new CopyOnWriteArrayList<EloSavedListener>();

   public void addEloSavedListener(EloSavedListener eloSavedListener)
   {
      if (!eloSavedListeners.contains(eloSavedListener))
      {
         eloSavedListeners.add(eloSavedListener);
      }
   }

   public void removeEloSavedListener(EloSavedListener eloSavedListener)
   {
      if (eloSavedListeners.contains(eloSavedListener))
      {
         eloSavedListeners.remove(eloSavedListener);
      }
   }

   private void sendEloSavedMessage(IELO elo, String name)
   {
      EloSavedEvent eloSavedEvent = new EloSavedEvent(this, elo, name);
      for (EloSavedListener eloSavedListener : eloSavedListeners)
      {
         eloSavedListener.eloSaved(eloSavedEvent);
      }
   }

   public void setRepository(IRepository repository)
   {
      logger.info("repository set");
      this.repository = repository;
   }

   public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
   {
      logger.info("metadataTypeManager set");
      this.metadataTypeManager = metadataTypeManager;
      titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
      logger.info("retrieved key " + titleKey.getId());
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
   }

   public void setEloFactory(IELOFactory eloFactory)
   {
      logger.info("eloFactory set");
      this.eloFactory = eloFactory;
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
            IMetadata newMetadata = repository.addNewELO(newHomeElo);
            eloFactory.updateELOWithResult(newHomeElo, newMetadata);
            sendEloSavedMessage(newHomeElo, name);
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
