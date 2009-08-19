/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxcopex;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.springframework.util.StringUtils;
import roolo.api.IRepository;
import roolo.api.search.IMetadataQuery;
import roolo.api.search.IQuery;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
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
import eu.scy.client.tools.drawing.ELOLoadedChangedEvent;
import eu.scy.client.tools.drawing.ELOLoadedChangedListener;
import java.io.File;


/**
 *
 * @author Marjolaine
 */
public class EloCopexActionWrapper {
   private static final Logger logger = Logger.getLogger(EloCopexActionWrapper.class.getName());
   public static final String scyxprocType = "scy/xproc";
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
   private CopexPanel copexPanel;
   private File lastUsedFile = null;
   private String docName = untitledDocName;
   private IELO elo = null;
   private CopyOnWriteArrayList<ELOLoadedChangedListener> eloLoadedChangedListeners = new CopyOnWriteArrayList<ELOLoadedChangedListener>();

   public EloCopexActionWrapper(CopexPanel copexPanel)
   {
      this.copexPanel = copexPanel;
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
      String windowTitle = "Experimental procedure: ";
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

   public void newCopexAction()
   {
        copexPanel.newElo();
		elo = null;
        docName = untitledDocName;
		sendELOLoadedChangedListener();
   }

   public void loadCopexAction()
   {
      IQuery query = null;
      IMetadataQuery metadataQuery = new BasicMetadataQuery(technicalFormatKey,
         BasicSearchOperations.EQUALS, scyxprocType, null);
      query = metadataQuery;
      List<ISearchResult> searchResults = repository.search(query);
      URI[] drawingUris = new URI[searchResults.size()];
      int i = 0;
      for (ISearchResult searchResult : searchResults)
      {
         drawingUris[i++] = searchResult.getUri();
      }
      URI drawingUri = (URI) JOptionPane.showInputDialog(null, "Select experimental procedure", "Select experimental procedure",
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
            if (!scyxprocType.equals(eloType) )
				throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
			IMetadata metadata = newElo.getMetadata();
			IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
			// TODO fixe the locale problem!!!
			Object titleObject = metadataValueContainer.getValue();
			Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
			Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);

			setDocName(titleObject3.toString());
			this.copexPanel.loadELO(newElo.getContent().getXmlString());
			elo = newElo;
			sendELOLoadedChangedListener();
		}
   }

   


   public void saveCopexAction()
   {
        logger.fine("save copex");
		if (elo == null)
		{
			saveAsCopexAction();
		}
		else
		{
			elo.getContent().setXmlString(
            jdomStringConversion.xmlToString(copexPanel.getExperimentalProcedure()));
         IMetadata resultMetadata = repository.updateELO(elo);
         eloFactory.updateELOWithResult(elo, resultMetadata);
		}
   }

   public void saveAsCopexAction()
   {
        logger.fine("save as copexs");
		String copexName = JOptionPane.showInputDialog("Enter experimental procedure name:", docName);
        if (StringUtils.hasText(copexName))
		{
			setDocName(copexName);
			elo = eloFactory.createELO();
			elo.setDefaultLanguage(Locale.ENGLISH);
			elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
			elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.CANADA);
            elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue("scy/xproc");
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
         content.setXmlString(jdomStringConversion.xmlToString(copexPanel.getExperimentalProcedure()));
         elo.setContent(content);
         IMetadata resultMetadata = repository.addNewELO(elo);
         eloFactory.updateELOWithResult(elo, resultMetadata);
         // updateEloWithNewMetadata(elo, eloMetadata);
         // logger.fine("metadata xml: \n" + elo.getMetadata().getXml());
         sendELOLoadedChangedListener();
		}
   }

   // @Action
   // public void closeCopexAction()
   // {
   // this.dispose();
   // }
   //
   public void setRepository(IRepository repository)
   {
      this.repository = repository;
   }



}
