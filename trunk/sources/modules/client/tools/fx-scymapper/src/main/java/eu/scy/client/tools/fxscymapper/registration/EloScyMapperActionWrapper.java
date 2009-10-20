/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxscymapper.registration;

import eu.scy.scymapper.impl.SCYMapperPanel;
import org.springframework.util.StringUtils;
import roolo.api.IRepository;
import roolo.api.search.IMetadataQuery;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import roolo.elo.JDomStringConversion;
import roolo.elo.api.*;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

import javax.swing.*;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 *
 * @author sikkenj
 */
public class EloScyMapperActionWrapper
{

   private static final Logger logger = Logger.getLogger(EloScyMapperActionWrapper.class.getName());
   public static final String scyMapperType = "scy/conceptmap";
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
   private SCYMapperPanel scymapperPanel;
   private String docName = untitledDocName;
   private IELO elo = null;
   private boolean whiteboardChanged = false;

	public EloScyMapperActionWrapper(SCYMapperPanel scymapperPanel)
   {
      this.scymapperPanel = scymapperPanel;
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

   public void setDocName(String docName)
   {
      this.docName = docName;
      String windowTitle = "Concept map: ";
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

   public void newConceptMapAction()
   {
      // TODO: scymapperPanel.deleteAllWhiteboardContainers();
      elo = null;
      docName = untitledDocName;
   }

   public void loadConceptMapAction()
   {
      IQuery query = null;
      IMetadataQuery metadataQuery = new BasicMetadataQuery(technicalFormatKey,
         BasicSearchOperations.EQUALS, scyMapperType, null);
      query = metadataQuery;
      List<ISearchResult> searchResults = repository.search(query);
      URI[] conceptMapUris = new URI[searchResults.size()];
      int i = 0;
      for (ISearchResult searchResult : searchResults)
      {
         conceptMapUris[i++] = searchResult.getUri();
      }
      URI conceptMapUri = (URI) JOptionPane.showInputDialog(null, "Select concept map", "Select concept map",
         JOptionPane.QUESTION_MESSAGE, null, conceptMapUris, null);
      if (conceptMapUri != null)
      {
         loadElo(conceptMapUri);
      }
   }

   public void loadElo(URI eloUri)
   {
      logger.info("Trying to load elo " + eloUri);
      IELO newElo = repository.retrieveELO(eloUri);
      if (newElo != null)
      {
         String eloType = newElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().toString();
         if (!scyMapperType.equals(eloType))
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
         //TODO: scymapperPanel.getModel().clear();
         //TODO: LOAD CONCEPT MAP FROM XML STRING: scymapperPanel.setContentStatus(jdomStringConversion.stringToXml(newElo.getContent().getXmlString()));
         elo = newElo;
      }
   }

   public void saveScyMapperAction()
   {
      logger.fine("save concept map");
      if (elo == null)
      {
         saveAsConceptMapAction();
      }
      else {


         //TODO: SAVE ELO USING REPOSITORY elo.getContent().setXmlString(
         //   jdomStringConversion.xmlToString(scymapperPanel.getContentStatus()));
         //IMetadata resultMetadata = repository.updateELO(elo);
         //eloFactory.updateELOWithResult(elo, resultMetadata);
      }
   }

   public void saveAsConceptMapAction()
   {
      logger.fine("save as concept map");
      String conceptMapName = JOptionPane.showInputDialog("Enter concept map name:", docName);
      if (StringUtils.hasText(conceptMapName))
      {
         setDocName(conceptMapName);
         elo = eloFactory.createELO();
         elo.setDefaultLanguage(Locale.ENGLISH);
         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.CANADA);
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue("scy/concept map");
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
         //TODO: SERIALIZE THE CONCEPT MAP content.setXmlString(jdomStringConversion.xmlToString(scymapperPanel.getContentStatus()));
         elo.setContent(content);
         IMetadata resultMetadata = repository.addNewELO(elo);
         eloFactory.updateELOWithResult(elo, resultMetadata);
         // updateEloWithNewMetadata(elo, eloMetadata);
         // logger.fine("metadata xml: \n" + elo.getMetadata().getXml());
         //sendELOLoadedChangedListener();
      }
   }
   public void setRepository(IRepository repository)
   {
      this.repository = repository;
   }

}