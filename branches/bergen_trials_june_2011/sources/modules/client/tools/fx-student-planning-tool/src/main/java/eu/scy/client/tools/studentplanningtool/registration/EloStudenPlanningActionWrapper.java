/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.studentplanningtool.registration;

import eu.scy.client.tools.drawing.ELOLoadedChangedEvent;
import eu.scy.client.tools.drawing.ELOLoadedChangedListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.springframework.util.StringUtils;
import roolo.api.IRepository;
import roolo.elo.JDomStringConversion;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

/**
 *
 * @author sikkenj
 */
public class EloStudenPlanningActionWrapper
{

   private static final Logger logger = Logger.getLogger(EloStudenPlanningActionWrapper.class.getName());
   public static final String scySPT = "scy/studentplanningtool";
   public static final String untitledDocName = "untitled";
   private IRepository repository;
   private IMetadataTypeManager metadataTypeManager;
   private IELOFactory eloFactory;
   private IMetadataKey identifierKey;
   private IMetadataKey titleKey;
   private IMetadataKey technicalFormatKey;
   private IMetadataKey dateCreatedKey;
//	private IMetadataKey missionKey;
   private IMetadataKey authorKey;
   private JDomStringConversion jdomStringConversion = new JDomStringConversion();
   private String docName = untitledDocName;
   private IELO elo = null;
   
   private CopyOnWriteArrayList<ELOLoadedChangedListener> eloLoadedChangedListeners = new CopyOnWriteArrayList<ELOLoadedChangedListener>();

   public EloStudenPlanningActionWrapper() {
     
   }

   public void addELOLoadedChangedListener(
      ELOLoadedChangedListener eloLoadedChangedListener)
   {
      if (!eloLoadedChangedListeners.contains(eloLoadedChangedListener))
      {
         eloLoadedChangedListeners.add(eloLoadedChangedListener);
      }
   }

   public void removeELOLoadedChangedListener(
      ELOLoadedChangedListener eloLoadedChangedListener)
   {
      if (eloLoadedChangedListeners.contains(eloLoadedChangedListener))
      {
         eloLoadedChangedListeners.remove(eloLoadedChangedListener);
      }
   }

   private void sendELOLoadedChangedListener()
   {
      ELOLoadedChangedEvent eloLoadedChangedEvent = new ELOLoadedChangedEvent(
         this, elo);
      for (ELOLoadedChangedListener eloLoadedChangedListener : eloLoadedChangedListeners)
      {
         eloLoadedChangedListener.eloLoadedChanged(eloLoadedChangedEvent);
      }
   }

   public IRepository getRepository()
   {
      return repository;
   }

   public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
   {
      this.metadataTypeManager = metadataTypeManager;
      identifierKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
      logger.info("retrieved key " + identifierKey.getId());
      titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
      logger.info("retrieved key " + titleKey.getId());
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
      logger.info("retrieved key " + technicalFormatKey.getId());
      dateCreatedKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
      logger.info("retrieved key " + dateCreatedKey.getId());
//		missionKey = metadataTypeManager.getMetadataKey("mission");
//		logger.info("retrieved key " + missionKey.getId());
      authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
      logger.info("retrieved key " + authorKey.getId());
   }

   public void setEloFactory(IELOFactory eloFactory)
   {
      this.eloFactory = eloFactory;
   }

   public URI getEloUri()
   {
      if (elo == null)
      {
         return null;
      }
      else
      {
         return elo.getUri();
      }
   }

   public String getELOTitle()
   {
      if (elo == null)
      {
         return null;
      }
      else
      {
         return (String) elo.getMetadata().getMetadataValueContainer(titleKey).getValue(Locale.ENGLISH);
      }
   }

   private void setDocName(String docName)
   {
      this.docName = docName;
      String windowTitle = "Student Planning Tool: ";
      if (StringUtils.hasText(docName))
      {
         windowTitle += docName;
      }
      // setTitle(windowTitle);
   }

   public String getDocName()
   {
      return docName;
   }

  

   public void loadElo(URI eloUri)
   {
      logger.info("Trying to load elo " + eloUri);
      IELO newElo = repository.retrieveELO(eloUri);
      if (newElo != null)
      {
         String eloType = newElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().toString();
         if (!scySPT.equals(eloType))
         {
            throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
         }
         IMetadata metadata = newElo.getMetadata();
         IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
         // TODO fixe the locale problem!!!
         Object titleObject = metadataValueContainer.getValue();
         Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
         Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);

         setDocName(titleObject3.toString());
        
         elo = newElo;
         sendELOLoadedChangedListener();
      }
   }
}
