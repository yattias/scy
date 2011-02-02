/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.content.eloImporter;

import eu.scy.common.mission.impl.jdom.JDomStringConversion;
import java.net.URI;
import java.util.Locale;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.util.StringUtils;
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
 *
 */
public class EloImporterActionWrapper
{

   private static final Logger logger = Logger.getLogger(EloImporterActionWrapper.class.getName());
   public static final String scyTextType = "scy/ppt";
   public static final String untitledDocName = "untitled";
   private static final String textTagName = "file";
   private EloImporterModel eloImporterModel;
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

   public EloImporterActionWrapper(EloImporterModel eloImporterModel)
   {
       this.eloImporterModel = eloImporterModel;
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
      String windowTitle = "File: ";
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

   public void newELOAction()
   {
      elo = null;
      docName = untitledDocName;
   }

   /*public void loadTextAction()
   {
      IQuery query = null;
      IMetadataQuery metadataQuery = new BasicMetadataQuery(technicalFormatKey,
         BasicSearchOperations.EQUALS, scyTextType, null);
      query = metadataQuery;
      List<ISearchResult> searchResults = repository.search(query);
      URI[] drawingUris = new URI[searchResults.size()];
      int i = 0;
      for (ISearchResult searchResult : searchResults)
      {
         drawingUris[i++] = searchResult.getUri();
      }
      URI textUri = (URI) JOptionPane.showInputDialog(null, "Select text", "Select text",
         JOptionPane.QUESTION_MESSAGE, null, drawingUris, null);
      if (textUri != null)
      {
         loadElo(textUri);
      }
   }

   public void loadElo(URI eloUri)
   {
      logger.info("Trying to load elo " + eloUri);
      IELO newElo = repository.retrieveELO(eloUri);
      if (newElo != null)
      {
         String eloType = newElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().toString();
         if (!scyTextType.equals(eloType))
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
         String text = eloContentXmlToText(newElo.getContent().getXmlString());
         textEditor.setText(text);
         logger.info("elo text content: '" + text + "'");
         elo = newElo;
      }
   }*/

   public void saveImportedELOAction()
   {
      logger.info("save ELO");
      if (elo == null)
      {
         saveImportedELOAsAction();
      }
      else
      {
         logger.info("Filename: "+eloImporterModel.getFilename());
         logger.info("File encoded: "+eloImporterModel.getFileEncoded());
         elo.getContent().setXmlString(textToEloContentXml(eloImporterModel.getFileEncoded()));
         IMetadata resultMetadata = repository.updateELO(elo);
         eloFactory.updateELOWithResult(elo, resultMetadata);
      }
   }

   public void saveImportedELOAsAction()
   {
      logger.info("save imported ELO as");
      String drawingName = JOptionPane.showInputDialog("Enter text name:", docName);
      if (StringUtils.hasText(drawingName))
      {
         setDocName(drawingName);
         elo = eloFactory.createELO();
         elo.setDefaultLanguage(Locale.ENGLISH);
         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
         elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.CANADA);
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyTextType);
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
         IContent content = eloFactory.createContent();
         logger.info("Filename: "+eloImporterModel.getFilename());
         logger.info("File encoded: "+eloImporterModel.getFileEncoded());
         content.setXmlString(textToEloContentXml(eloImporterModel.getFileEncoded()));
         elo.setContent(content);
         IMetadata resultMetadata = repository.addNewELO(elo);
         eloFactory.updateELOWithResult(elo, resultMetadata);
         // updateEloWithNewMetadata(elo, eloMetadata);
         // logger.fine("metadata xml: \n" + elo.getMetadata().getXml());
      }
   }

   public void setRepository(IRepository repository)
   {
      this.repository = repository;
   }

   private String textToEloContentXml(String text)
   {
      Element textElement= new Element(textTagName);
      textElement.setText(text);
      return jdomStringConversion.xmlToString(textElement);
   }

   private String eloContentXmlToText(String text)
   {
      Element textElement=jdomStringConversion.stringToXml(text);
      if (!textTagName.equals(textElement.getName())){
         logger.error("wrong tag name, expected " + textTagName + ", but got " + textElement.getName());
      }
      return textElement.getTextTrim();
   }
}
