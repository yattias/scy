/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.tool.dataProcessTool;

import eu.scy.client.tools.drawing.ELOLoadedChangedEvent;
import eu.scy.client.tools.drawing.ELOLoadedChangedListener;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elobrowser.main.user.User;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
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
    private File lastUsedFile = null;

	private String docName = untitledDocName;
	private IELO<IMetadataKey> elo = null;
    private URI usesEloURI = null;
	private CopyOnWriteArrayList<ELOLoadedChangedListener> eloLoadedChangedListeners = new CopyOnWriteArrayList<ELOLoadedChangedListener>();
    private IMetadataKey usesRelationKey;

	public EloDataToolWrapper(DataToolPanel dataToolPanel){
		this.dataToolPanel = dataToolPanel;
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
        usesRelationKey = metadataTypeManager.getMetadataKey(RooloMetadataKeys.RELATION_USES.getId());
        logger.info("retrieved key " + usesRelationKey.getId());
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
		String windowTitle = "Dataset: ";
		if (StringUtils.hasText(docName))
			windowTitle += docName;
	}

   public String getDocName()
   {
      return docName;
   }

	public void newDataProcessAction()
	{
		//dataToolPanel.newElo();
		elo = null;
        docName = untitledDocName;
		sendELOLoadedChangedListener();
	}

	public URI loadDataProcessAction()
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
//		if (pdsUri != null)
//		{
//			loadElo(pdsUri);
//		}
        return pdsUri;
          
	}

    public void mergeDataProcessAction()
	{
        IQuery queryDS = null;

        IMetadataQuery<IMetadataKey> metadataQueryDs = new BasicMetadataQuery<IMetadataKey>(typeKey,
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
            usesEloURI = elo.getUri(); // needed to store the uses relation when saving
			sendELOLoadedChangedListener();
		}
         
	}

    public void loadElo(DataSet dsElo)
	{
		if (dsElo != null)
		{
			this.dataToolPanel.loadELO(new JDomStringConversion().xmlToString(dsElo.toXML()));
			sendELOLoadedChangedListener();
		}

	}

    public void mergeElo(URI eloUri)
	{
        logger.info("Trying to load elo " + eloUri);
		IELO<IMetadataKey> newElo = repository.retrieveELO(eloUri);
		if (newElo != null)
		{
			String eloType = newElo.getMetadata().getMetadataValueContainer(typeKey).getValue()
						.toString();
			if ( !scyDatasetType.equals(eloType))
				throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
			IMetadata metadata = newElo.getMetadata();
			IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
			// TODO fixe the locale problem!!!
			Object titleObject = metadataValueContainer.getValue();
			Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
			Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);

			//setDocName(titleObject3.toString());
			this.dataToolPanel.mergeELO(newElo.getContent().getXmlString());
			//elo = newElo;
            usesEloURI = elo.getUri(); // needed to store the uses relation when saving
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
							new Contribute(User.instance.getUsername(), System.currentTimeMillis()));
                if (usesEloURI !=null) {
                    elo.getMetadata().getMetadataValueContainer(usesRelationKey).setValue(usesEloURI);
                }
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

	
	public void setRepository(IRepository<IELO<IMetadataKey>, IMetadataKey> repository)
	{
		this.repository = repository;
	}

    public DataSet importCSVFile() {
        JFileChooser aFileChooser = new JFileChooser();
		if (lastUsedFile != null)
			aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
		int userResponse = aFileChooser.showOpenDialog(null);
		if (userResponse == JFileChooser.APPROVE_OPTION){
			File file = aFileChooser.getSelectedFile();
			lastUsedFile = file;
            return dataToolPanel.importCSVFile(file);
		}
        return null;
    }

	

}
