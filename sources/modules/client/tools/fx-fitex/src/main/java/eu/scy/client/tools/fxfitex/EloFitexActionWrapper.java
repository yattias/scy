/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxfitex;

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
import eu.scy.elo.contenttype.dataset.DataSet;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Marjolaine
 */
public class EloFitexActionWrapper {
   private static final Logger logger = Logger.getLogger(EloFitexActionWrapper.class.getName());
   public static final String scyPDSType = "scy/pds";
    public static final String scyDatasetType = "scy/dataset";
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
   private FitexPanel fitexPanel;
   private File lastUsedFile = null;
   private String docName = untitledDocName;
   private IELO elo = null;
   private CopyOnWriteArrayList<ELOLoadedChangedListener> eloLoadedChangedListeners = new CopyOnWriteArrayList<ELOLoadedChangedListener>();

   public EloFitexActionWrapper(FitexPanel fitexPanel)
   {
      this.fitexPanel = fitexPanel;
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
      String windowTitle = "Data processing: ";
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

   public void newFitexAction()
   {
        fitexPanel.newElo();
		elo = null;
        docName = untitledDocName;
		sendELOLoadedChangedListener();
   }

   public URI loadFitexAction()
   {
      IQuery query = null;
        IQuery queryDS = null;
		IMetadataQuery metadataQuery = new BasicMetadataQuery(technicalFormatKey,
         BasicSearchOperations.EQUALS, scyPDSType, null);
        IMetadataQuery metadataQueryDs = new BasicMetadataQuery(technicalFormatKey,
         BasicSearchOperations.EQUALS, scyDatasetType, null);
        
		query = metadataQuery;
        queryDS = metadataQueryDs ;
		List<ISearchResult> searchResults = repository.search(query);
        List<ISearchResult> searchResultsDs = repository.search(queryDS);
		URI[] pdsUris = new URI[searchResults.size()+searchResultsDs.size()];
		int i = 0;
		for (ISearchResult searchResult : searchResults)
			pdsUris[i++] = searchResult.getUri();
        for (ISearchResult searchResult : searchResultsDs)
			pdsUris[i++] = searchResult.getUri();
		URI pdsUri = (URI) JOptionPane.showInputDialog(null, "Select ds/pds", "Select ds/pds",
					JOptionPane.QUESTION_MESSAGE, null, pdsUris, null);
//		if (pdsUri != null)
//		{
//			loadElo(pdsUri);
//		}
        return pdsUri;
   }

   public void loadElo(URI eloUri)
   {
        logger.info("Trying to load elo " + eloUri);
		IELO newElo = repository.retrieveELO(eloUri);
		if (newElo != null)
		{
			String eloType = newElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().toString();
            if (!scyPDSType.equals(eloType) && !scyDatasetType.equals(eloType))
				throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
			IMetadata metadata = newElo.getMetadata();
			IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
			// TODO fixe the locale problem!!!
			Object titleObject = metadataValueContainer.getValue();
			Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
			Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);

			setDocName(titleObject3.toString());
			this.fitexPanel.loadELO(newElo.getContent().getXmlString());
			elo = newElo;
			sendELOLoadedChangedListener();
		}
   }

   public void loadElo(DataSet dsElo)
	{
		if (dsElo != null)
		{
			this.fitexPanel.loadELO(new JDomStringConversion().xmlToString(dsElo.toXML()));
			sendELOLoadedChangedListener();
		}

	}

   
   public void mergeFitexAction()
	{
        IQuery queryDS = null;

        IMetadataQuery metadataQueryDs = new BasicMetadataQuery(technicalFormatKey,
         BasicSearchOperations.EQUALS, scyDatasetType, null);
        queryDS = metadataQueryDs ;
        List<ISearchResult> searchResultsDs = repository.search(queryDS);
		URI[] dsUris = new URI[searchResultsDs.size()];
		int i = 0;
        for (ISearchResult searchResult : searchResultsDs)
			dsUris[i++] = searchResult.getUri();
		URI dsUri = (URI) JOptionPane.showInputDialog(null, "Select ds", "Select ds",
					JOptionPane.QUESTION_MESSAGE, null, dsUris, null);
		if (dsUri != null)
		{
			mergeElo(dsUri);
		}

	}


public void mergeElo(URI eloUri)
	{
        logger.info("Trying to load elo " + eloUri);
		IELO newElo = repository.retrieveELO(eloUri);
		if (newElo != null)
		{
			String eloType = newElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue().toString();
            if ( !scyDatasetType.equals(eloType))
				throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
			IMetadata metadata = newElo.getMetadata();
			IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
			// TODO fixe the locale problem!!!
			Object titleObject = metadataValueContainer.getValue();
			Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
			Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);

			//setDocName(titleObject3.toString());
			this.fitexPanel.mergeELO(newElo.getContent().getXmlString());
			//elo = newElo;
			sendELOLoadedChangedListener();
		}

	}

public DataSet importFitexAction() {
        JFileChooser aFileChooser = new JFileChooser();
		if (lastUsedFile != null)
			aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
		int userResponse = aFileChooser.showOpenDialog(null);
		if (userResponse == JFileChooser.APPROVE_OPTION){
			File file = aFileChooser.getSelectedFile();
			lastUsedFile = file;
            return fitexPanel.importCSVFile(file);
		}
        return null;
    }


   public void saveFitexAction()
   {
      logger.fine("save fitex");
		if (elo == null)
		{
			saveAsFitexAction();
		}
		else
		{
			elo.getContent().setXmlString(
            jdomStringConversion.xmlToString(fitexPanel.getPDS()));
         IMetadata resultMetadata = repository.updateELO(elo);
         eloFactory.updateELOWithResult(elo, resultMetadata);
		}
   }

   public void saveAsFitexAction()
   {
      logger.fine("save as dataProcess");
		String fitexName = JOptionPane.showInputDialog("Enter process dataset name:", docName);
        if (StringUtils.hasText(fitexName))
		{
			setDocName(fitexName);
			elo = eloFactory.createELO();
			elo.setDefaultLanguage(Locale.ENGLISH);
			elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
			elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.CANADA);
            elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue("scy/pds");
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
         content.setXmlString(jdomStringConversion.xmlToString(fitexPanel.getPDS()));
         elo.setContent(content);
         IMetadata resultMetadata = repository.addNewELO(elo);
         eloFactory.updateELOWithResult(elo, resultMetadata);
         // updateEloWithNewMetadata(elo, eloMetadata);
         // logger.fine("metadata xml: \n" + elo.getMetadata().getXml());
         sendELOLoadedChangedListener();
		}
   }

   // @Action
   // public void closeFitexAction()
   // {
   // this.dispose();
   // }
   //
   public void setRepository(IRepository repository)
   {
      this.repository = repository;
   }

   
}
