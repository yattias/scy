/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxchattool.registration;

import org.springframework.util.StringUtils;
import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.events.WhiteboardContainerChangedEvent;
import colab.vt.whiteboard.component.events.WhiteboardContainerListChangedEvent;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.client.tools.drawing.ELOLoadedChangedEvent;
import eu.scy.client.tools.drawing.ELOLoadedChangedListener;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JOptionPane;
import java.util.logging.Logger;
import roolo.api.IRepository;
import roolo.api.search.IMetadataQuery;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import org.roolo.rooloimpljpa.repository.search.BasicMetadataQuery;
import org.roolo.rooloimpljpa.repository.search.BasicSearchOperations;
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
 * @author jeremyt
 */
public class EloChatActionWrapper {
   private static final Logger logger = Logger.getLogger(EloChatActionWrapper.class.getName());
   public static final String scyDrawType = "scy/drawing";
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
   private WhiteboardPanel whiteboardPanel;
   private String docName = untitledDocName;
   private IELO elo = null;
   private boolean whiteboardChanged = false;
   private CopyOnWriteArrayList<ELOLoadedChangedListener> eloLoadedChangedListeners = new CopyOnWriteArrayList<ELOLoadedChangedListener>();

   public EloChatActionWrapper(WhiteboardPanel whiteboardPanel)
   {
      this.whiteboardPanel = whiteboardPanel;
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
      String windowTitle = "Drawing: ";
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

   public void newDrawingAction()
   {
      whiteboardPanel.deleteAllWhiteboardContainers();
      elo = null;
      docName = untitledDocName;
      sendELOLoadedChangedListener();
   }

   public void loadDrawingAction()
   {
      IQuery query = null;
      IMetadataQuery metadataQuery = new BasicMetadataQuery(technicalFormatKey,
         BasicSearchOperations.EQUALS, scyDrawType);
      query = metadataQuery;
      List<ISearchResult> searchResults = repository.search(query);
      URI[] drawingUris = new URI[searchResults.size()];
      int i = 0;
      for (ISearchResult searchResult : searchResults)
      {
         drawingUris[i++] = searchResult.getUri();
      }
      URI drawingUri = (URI) JOptionPane.showInputDialog(null, "Select drawing", "Select drawing",
         JOptionPane.QUESTION_MESSAGE, null, drawingUris, null);
      if (drawingUri != null)
      {
         loadElo(drawingUri);
      }
   }

   public void loadElo(URI eloUri)
   {
      logger.info("Trying to load elo " + eloUri);
      IELO newElo = repository.retrieveELO(eloUri);
      if (newElo != null)
      {
         String eloType = newElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().toString();
         if (!scyDrawType.equals(eloType))
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
         whiteboardPanel.deleteAllWhiteboardContainers();
         whiteboardPanel.setContentStatus(jdomStringConversion.stringToXml(newElo.getContent().getXmlString()));
         elo = newElo;
         sendELOLoadedChangedListener();
      }
   }

   public void saveDrawingAction()
   {
      logger.fine("save drawing");
      if (elo == null)
      {
         saveAsDrawingAction();
      }
      else
      {
         elo.getContent().setXmlString(
            jdomStringConversion.xmlToString(whiteboardPanel.getContentStatus()));
         IMetadata resultMetadata = repository.updateELO(elo);
         eloFactory.updateELOWithResult(elo, resultMetadata);
      }
   }

   public void saveAsDrawingAction()
   {
      logger.fine("save as drawing");
      String drawingName = JOptionPane.showInputDialog("Enter drawing name:", docName);
      if (StringUtils.hasText(drawingName))
      {
         setDocName(drawingName);
         elo = eloFactory.createELO();
         elo.setDefaultLanguage(Locale.ENGLISH);
         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.CANADA);
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue("scy/drawing");
         elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(
            new Long(System.currentTimeMillis()));
//			try
//			{
//				elo.getMetadata().getMetadataValueContainer(missionKey).setValue(
//							new URI("roolo://somewhere/myMission.mission"));
//			}
//			catch (URISyntaxException e)
//			{
//				logger.log(Level.WARNING, "failed to create uri", e);
//			}
         elo.getMetadata().getMetadataValueContainer(authorKey).setValue(
            new Contribute("my vcard", System.currentTimeMillis()));
         IContent content = eloFactory.createContent();
         content.setXmlString(jdomStringConversion.xmlToString(whiteboardPanel.getContentStatus()));
         elo.setContent(content);
         IMetadata resultMetadata = repository.addNewELO(elo);
         eloFactory.updateELOWithResult(elo, resultMetadata);
         // updateEloWithNewMetadata(elo, eloMetadata);
         // logger.fine("metadata xml: \n" + elo.getMetadata().getXml());
         sendELOLoadedChangedListener();
      }
   }

   // @Action
   // public void closeDrawingAction()
   // {
   // this.dispose();
   // }
   //
   public void setRepository(IRepository repository)
   {
      this.repository = repository;
   }

   public void whiteboardContainerChanged(WhiteboardContainerChangedEvent arg0)
   {
      whiteboardChanged = true;
   }

   public void whiteboardContainerAdded(WhiteboardContainerChangedEvent arg0)
   {
      whiteboardChanged = true;
   }

   public void whiteboardContainerDeleted(WhiteboardContainerChangedEvent arg0)
   {
      whiteboardChanged = true;
   }

   public void whiteboardContainersCleared(WhiteboardContainerListChangedEvent arg0)
   {
      whiteboardChanged = true;
   }

   public void whiteboardContainersLoaded(WhiteboardContainerListChangedEvent arg0)
   {
      whiteboardChanged = false;
   }

   public void whiteboardPanelLoaded(WhiteboardContainerListChangedEvent arg0)
   {
      whiteboardChanged = false;
   }
}
