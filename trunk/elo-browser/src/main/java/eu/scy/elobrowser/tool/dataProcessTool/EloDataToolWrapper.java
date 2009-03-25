/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.tool.dataProcessTool;

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
import roolo.elo.api.metadata.RooloMetadataKeys;
import roolo.elo.metadata.keys.Contribute;

/**
 * This class takes care of the operations that are executed when a button on the menu bar
 * in the top the tool`s window is pressed, like "new", "load", "save"…
 * @author marjolaine bodin
 */
public class EloDataToolWrapper {
	private static final Logger logger = Logger.getLogger(EloDataToolWrapper.class.getName());
	public static final String untitledDocName = "untitled";
    public static final String scyPDSType = "scy/pds";
    public static final String scyDatasetType = "scy/dataset";

	private IRepository<IELO<IMetadataKey>, IMetadataKey> repository;
	private IMetadataTypeManager<IMetadataKey> metadataTypeManager;
	private IELOFactory<IMetadataKey> eloFactory;
	private IMetadataKey uriKey;
	private IMetadataKey titleKey;
	private IMetadataKey typeKey;
	private IMetadataKey dateCreatedKey;
	private IMetadataKey missionKey;
	private IMetadataKey authorKey;
	private JDomStringConversion jdomStringConversion = new JDomStringConversion();

	private DataToolPanel dataToolPanel;
	private String docName = untitledDocName;
	private IELO<IMetadataKey> elo = null;
	private CopyOnWriteArrayList<ELOLoadedChangedListener<IMetadataKey>> eloLoadedChangedListeners = new CopyOnWriteArrayList<ELOLoadedChangedListener<IMetadataKey>>();

	public EloDataToolWrapper(DataToolPanel dataToolPanel){
		this.dataToolPanel = dataToolPanel;
	}

	public void addELOLoadedChangedListener(
				ELOLoadedChangedListener<IMetadataKey> eloLoadedChangedListener)
	{
		if (!eloLoadedChangedListeners.contains(eloLoadedChangedListener))
		{
			eloLoadedChangedListeners.add(eloLoadedChangedListener);
		}
	}

	public void removeELOLoadedChangedListener(
				ELOLoadedChangedListener<IMetadataKey> eloLoadedChangedListener)
	{
		if (eloLoadedChangedListeners.contains(eloLoadedChangedListener))
		{
			eloLoadedChangedListeners.remove(eloLoadedChangedListener);
		}
	}

	private void sendELOLoadedChangedListener()
	{
		ELOLoadedChangedEvent<IMetadataKey> eloLoadedChangedEvent = new ELOLoadedChangedEvent<IMetadataKey>(
					this, elo);
		for (ELOLoadedChangedListener<IMetadataKey> eloLoadedChangedListener : eloLoadedChangedListeners)
		{
			eloLoadedChangedListener.eloLoadedChanged(eloLoadedChangedEvent);
		}
	}
	public IRepository<IELO<IMetadataKey>, IMetadataKey> getRepository()
	{
		return repository;
	}

	public void setMetadataTypeManager(IMetadataTypeManager<IMetadataKey> metadataTypeManager)
	{
		this.metadataTypeManager = metadataTypeManager;
		uriKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.URI.getId());
		logger.info("retrieved key " + uriKey.getId());
		titleKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TITLE.getId());
		logger.info("retrieved key " + titleKey.getId());
		typeKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.TYPE.getId());
		logger.info("retrieved key " + typeKey.getId());
		dateCreatedKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.DATE_CREATED.getId());
		logger.info("retrieved key " + dateCreatedKey.getId());
		missionKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.MISSION.getId());
		logger.info("retrieved key " + missionKey.getId());
		authorKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.AUTHOR.getId());
		logger.info("retrieved key " + authorKey.getId());
	}

	public void setEloFactory(IELOFactory<IMetadataKey> eloFactory)
	{
		this.eloFactory = eloFactory;
	}

   public URI getEloUri()
   {
      if (elo==null)
         return null;
      else
         return elo.getUri();
   }

	public String getELOTitle()
	{
		if (elo == null)
			return null;
		else
			return (String) elo.getMetadata().getMetadataValueContainer(titleKey).getValue(Locale.ENGLISH);
	}

	private void setDocName(String docName)
	{
		this.docName = docName;
		String windowTitle = "Data Process Visualization Tool : ";
		if (StringUtils.hasText(docName))
			windowTitle += docName;
		// setTitle(windowTitle);
	}

   public String getDocName()
   {
      return docName;
   }

	public void newDataProcessAction()
	{
		dataToolPanel.newElo();
		elo = null;
        docName = untitledDocName;
		sendELOLoadedChangedListener();
	}

	public void loadDataProcessAction()
	{
        // TODO
		IQuery query = null;
        IQuery queryDS = null;
		IMetadataQuery<IMetadataKey> metadataQuery = new BasicMetadataQuery<IMetadataKey>(typeKey,
					BasicSearchOperations.EQUALS, scyPDSType, null);
        IMetadataQuery<IMetadataKey> metadataQueryDs = new BasicMetadataQuery<IMetadataKey>(typeKey,
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
		if (pdsUri != null)
		{
			loadElo(pdsUri);
		}
          
	}

	public void loadElo(URI eloUri)
	{
		//TODO
        logger.info("Trying to load elo " + eloUri);
		IELO<IMetadataKey> newElo = repository.retrieveELO(eloUri);
		if (newElo != null)
		{
			String eloType = newElo.getMetadata().getMetadataValueContainer(typeKey).getValue()
						.toString();
			if (!scyPDSType.equals(eloType) && !scyDatasetType.equals(eloType))
				throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
			IMetadata metadata = newElo.getMetadata();
			IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
			// TODO fixe the locale problem!!!
			Object titleObject = metadataValueContainer.getValue();
			Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
			Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);

			setDocName(titleObject3.toString());
			this.dataToolPanel.loadELO(newElo.getContent().getXmlString());
			elo = newElo;
			sendELOLoadedChangedListener();
		}
         
	}

	public void saveDataProcessAction()
	{
		logger.fine("save dataProcess");
		if (elo == null)
		{
			saveAsDataProcessAction();
		}
		else
		{
			elo.getContent().setXmlString(jdomStringConversion.xmlToString(dataToolPanel.getPDS()));
			IMetadata<IMetadataKey> resultMetadata = repository.updateELO(elo);
			eloFactory.updateELOWithResult(elo, resultMetadata);
		}
	}

	public void saveAsDataProcessAction()
	{
		logger.fine("save as dataProcess");
		String drawingName = JOptionPane.showInputDialog("Enter process dataset name:", docName);
		if (StringUtils.hasText(drawingName))
		{
			setDocName(drawingName);
			elo = eloFactory.createELO();
			elo.setDefaultLanguage(Locale.ENGLISH);
			elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName);
			elo.getMetadata().getMetadataValueContainer(titleKey).setValue(docName, Locale.CANADA);
			elo.getMetadata().getMetadataValueContainer(typeKey).setValue("scy/pds");
			elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(
						new Long(System.currentTimeMillis()));
			try
			{
				elo.getMetadata().getMetadataValueContainer(missionKey).setValue(
							new URI("roolo://somewhere/myMission.mission"));
				elo.getMetadata().getMetadataValueContainer(authorKey).setValue(
							new Contribute("my vcard", System.currentTimeMillis()));
			}
			catch (URISyntaxException e)
			{
				logger.log(Level.WARNING, "failed to create uri", e);
			}
			IContent content = eloFactory.createContent();
			content.setXmlString(jdomStringConversion.xmlToString(dataToolPanel.getPDS()));
			elo.setContent(content);
			IMetadata<IMetadataKey> resultMetadata = repository.addELO(elo);
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
	public void setRepository(IRepository<IELO<IMetadataKey>, IMetadataKey> repository)
	{
		this.repository = repository;
	}

	

}
