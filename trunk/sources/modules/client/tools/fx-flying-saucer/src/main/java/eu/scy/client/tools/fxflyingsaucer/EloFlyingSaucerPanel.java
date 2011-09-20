/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

import eu.scy.client.common.scyi18n.UriLocalizer;
import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import eu.scy.client.desktop.scydesktop.tools.ScyTool;
import eu.scy.client.desktop.scydesktop.tools.RuntimeSettingsRetriever;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.jdom.Element;
import roolo.api.IRepository;
import roolo.elo.JDomStringConversion;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.tools.DrawerUIIndicator;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleKey;

/**
 *
 * @author sikkenj
 */
public class EloFlyingSaucerPanel extends FlyingSaucerPanel implements ScyTool
{

   private static final Logger logger = Logger.getLogger(EloFlyingSaucerPanel.class);
   private static final String urlTagName = "url";
   private static final String scyUrlType = "scy/url";
   private JDomStringConversion jdomStringConversion = new JDomStringConversion();
   private ToolBrokerAPI toolBrokerAPI;
   private IRepository repository;
   private IELOFactory eloFactory;
   private IMetadataTypeManager metadataTypeManager;
   private IMetadataKey titleKey;
   private IMetadataKey technicalFormatKey;
   private EloSaver eloSaver;
   private List<EloSavedListener> eloSavedListeners = new CopyOnWriteArrayList<EloSavedListener>();
   private UriLocalizer uriLocalizer = new UriLocalizer();
   private UrlSource urlSource;
   private DrawerUIIndicator drawerUIIndicator = null;
   private BubbleKey bubbleKey = null;

   public EloFlyingSaucerPanel(UrlSource urlSource, DrawerUIIndicator drawerUIIndicator, BubbleKey bubbleKey)
   {
      super(!(UrlSource.ASSIGNMENT == urlSource || UrlSource.RESOURCES == urlSource));
      this.urlSource = urlSource;
      this.drawerUIIndicator = drawerUIIndicator;
      this.bubbleKey = bubbleKey;
      if (UrlSource.ASSIGNMENT == urlSource || UrlSource.RESOURCES == urlSource)
      {
         Dimension preferredSize = new Dimension(200, getPreferredSize().height);
         this.setPreferredSize(preferredSize);
      }
   }

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

   public void setToolBrokerAPI(ToolBrokerAPI toolBrokerAPI)
   {
      this.toolBrokerAPI = toolBrokerAPI;
      setRepository(toolBrokerAPI.getRepository());
      setMetadataTypeManager(toolBrokerAPI.getMetaDataTypeManager());
      setEloFactory(toolBrokerAPI.getELOFactory());
   }

   public void setRepository(IRepository repository)
   {
//      logger.debug("repository set");
      this.repository = repository;
   }

   public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
   {
//      logger.debug("metadataTypeManager set");
      this.metadataTypeManager = metadataTypeManager;
      titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
//      logger.debug("retrieved key " + titleKey.getId());
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
   }

   public void setEloFactory(IELOFactory eloFactory)
   {
//      logger.debug("eloFactory set");
      this.eloFactory = eloFactory;
   }

   @Override
   public void setEloSaver(EloSaver eloSaver)
   {
      this.eloSaver = eloSaver;
   }

   @Override
   public void setRuntimeSettingsRetriever(RuntimeSettingsRetriever runtimeSettingsRetriever)
   {
   }

   @Override
   public DrawerUIIndicator getDrawerUIIndicator(){
      return drawerUIIndicator;
   }

   @Override
   public BubbleKey getBubbleKey()
   {
      return bubbleKey;
   }

   public void setHomeElo(URI uri)
   {
      ScyElo scyElo = ScyElo.loadElo(uri, toolBrokerAPI);
      if (scyElo == null)
      {
         logger.error("the home elo does not exists: " + uri);
         return;
      }
      setEloUri(uri);
      String homeUrl = null;
      URI homeUri = null;
      switch (urlSource)
      {
         case ASSIGNMENT:
            homeUri = scyElo.getAssignmentUri();
            break;
         case RESOURCES:
            homeUri = scyElo.getResourcesUri();
            break;
         default:
            homeUrl = getUrlFromContent(scyElo.getContent());
      }
      if (homeUrl == null && homeUri != null)
      {
         homeUrl = homeUri.toString();
      }
      if (homeUrl != null)
      {
         String localizedHomeUrl = getLocalizedUri(homeUrl);
         setHomeUrl(localizedHomeUrl);
      }
   }

   @Override
   public void loadUrl(final String url)
   {
      String localizedUrl = url;
      if (url!=null){
         localizedUrl = getLocalizedUri(url);
      }
      super.loadUrl(localizedUrl);
   }

   private String getLocalizedUri(String urlString)
   {
      try
      {
         long startNanos = System.nanoTime();
         URL url = uriLocalizer.localizeUrlwithChecking(new URL(urlString));
         long millisUsed = (System.nanoTime()-startNanos)/1000000;
         logger.debug("found localized url for " + urlString + " in " + millisUsed + " ms");
         return url.toString();
      }
      catch (MalformedURLException ex)
      {
         java.util.logging.Logger.getLogger(EloFlyingSaucerPanel.class.getName()).log(Level.SEVERE, null, ex);
      }
      return urlString;
   }

   @Override
   protected void saveUrlAsHome(URL url)
   {
      super.saveUrlAsHome(url);
      if (url != null)
      {
         IELO newHomeElo = eloFactory.createELO();
         newHomeElo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyUrlType);
         setUrlInContent(newHomeElo.getContent(), url.toString());
         if (eloSaver != null)
         {
            eloSaver.eloSaveAs(newHomeElo, null);
         }
         else
         {
            String name = JOptionPane.showInputDialog("Enter url home name:");
            if (name != null && name.length() > 0)
            {
               newHomeElo.getMetadata().getMetadataValueContainer(titleKey).setValue(name);
               IMetadata newMetadata = repository.addNewELO(newHomeElo);
               eloFactory.updateELOWithResult(newHomeElo, newMetadata);
               sendEloSavedMessage(newHomeElo, name);
            }
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

   @Override
   public void initialize(boolean windowContent)
   {
       repository = toolBrokerAPI.getRepository();
       eloFactory = toolBrokerAPI.getELOFactory();
       metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
   }

   @Override
   public void postInitialize()
   {
   }

   @Override
   public void newElo()
   {
   }

   @Override
   public void loadElo(URI eloUri)
   {
      setHomeElo(eloUri);
   }

   @Override
   public void loadedEloChanged(URI eloUri)
   {
   }

   @Override
   public void onGotFocus()
   {
   }

   @Override
   public void onLostFocus()
   {
   }

   @Override
   public void onMinimized()
   {
   }

   @Override
   public void onUnMinimized()
   {
   }

   @Override
   public boolean aboutToClose()
   {
      return true;
   }

   @Override
   public void onOpened()
   {
   }

   @Override
   public void onClosed()
   {
   }

   @Override
   public void onQuit()
   {
   }

   @Override
   public void setMyEloChanged(MyEloChanged myEloChanged)
   {
   }

   @Override
   public boolean canAcceptDrop(Object object)
   {
      return false;
   }

   @Override
   public void acceptDrop(Object object)
   {
   }

   @Override
   public BufferedImage getThumbnail(int width, int height)
   {
      return null;
   }

   @Override
   public void setReadOnly(boolean readOnly)
   {
   }
}
