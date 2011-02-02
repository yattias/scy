package eu.scy.client.tools.drawing;

import java.awt.GridLayout;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.events.WhiteboardContainerChangedEvent;
import colab.vt.whiteboard.component.events.WhiteboardContainerListChangedEvent;
import roolo.search.IQuery;
import roolo.search.ISearchResult;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;
import roolo.search.SearchOperation;

public class EloDrawingPanel extends JPanel
{
	private static final long serialVersionUID = 6762406198522179724L;

	private static final Logger logger = Logger.getLogger(EloDrawingPanel.class.getName());
	private static final String scyDrawType = "scy/drawing";

	private IRepository repository;
	@SuppressWarnings("unused")
	private IMetadataTypeManager metadataTypeManager;
	private IELOFactory eloFactory;
	private IMetadataKey uriKey;
	private IMetadataKey titleKey;
	private IMetadataKey typeKey;
	private IMetadataKey dateCreatedKey;
//	private IMetadataKey missionKey;
	private IMetadataKey authorKey;
	private JDomStringConversion jdomStringConversion = new JDomStringConversion();

	private WhiteboardPanel whiteboardPanel;
	private String docName = "untitiled";
	private IELO elo = null;
	@SuppressWarnings("unused")
	private boolean whiteboardChanged = false;
	private CopyOnWriteArrayList<ELOLoadedChangedListener> eloLoadedChangedListeners = new CopyOnWriteArrayList<ELOLoadedChangedListener>();

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

	public void initGUI()
	{
		whiteboardPanel = new WhiteboardPanel();
		setLayout(new GridLayout(1,1,0,0));
		add(whiteboardPanel);
	}

	public IRepository getRepository()
	{
		return repository;
	}

	public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
	{
		this.metadataTypeManager = metadataTypeManager;
		uriKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
		logger.log(Level.INFO, "retrieved key {0}", uriKey.getId());
		titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
		logger.log(Level.INFO, "retrieved key {0}", titleKey.getId());
		typeKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
		logger.log(Level.INFO, "retrieved key {0}", typeKey.getId());
		dateCreatedKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
		logger.log(Level.INFO, "retrieved key {0}", dateCreatedKey.getId());
//		missionKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.MISSION.getId());
//		logger.info("retrieved key " + missionKey.getId());
		authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
		logger.log(Level.INFO, "retrieved key {0}", authorKey.getId());
	}

	public void setEloFactory(IELOFactory eloFactory)
	{
		this.eloFactory = eloFactory;
	}

	public String getELOTitle()
	{
		if (elo == null)
			return null;
		return (String) elo.getMetadata().getMetadataValueContainer(titleKey).getValue(Locale.ENGLISH);
	}

	private void setDocName(String docName)
	{
		this.docName = docName;
		String windowTitle = "Drawing: ";
		if (StringUtils.hasText(docName))
			windowTitle += docName;
		// setTitle(windowTitle);
	}

	public void newDrawingAction()
	{
		whiteboardPanel.deleteAllWhiteboardContainers();
		elo = null;
		sendELOLoadedChangedListener();
	}

	public void loadDrawingAction()
	{
		MetadataQueryComponent metadataQueryComp = new MetadataQueryComponent(typeKey,
					SearchOperation.EQUALS, scyDrawType);
		IQuery query = new Query(metadataQueryComp);
		List<ISearchResult> searchResults = repository.search(query);
		URI[] drawingUris = new URI[searchResults.size()];
		int i = 0;
		for (ISearchResult searchResult : searchResults)
			drawingUris[i++] = searchResult.getUri();
		URI drawingUri = (URI) JOptionPane.showInputDialog(this, "Select drawing", "Select drawing",
					JOptionPane.QUESTION_MESSAGE, null, drawingUris, null);
		if (drawingUri != null)
		{
			loadElo(drawingUri);
		}
	}

	public void loadElo(URI eloUri)
	{
		logger.log(Level.INFO, "Trying to load elo {0}", eloUri);
		IELO newElo = repository.retrieveELO(eloUri);
		if (newElo != null)
		{
			String eloType = newElo.getMetadata().getMetadataValueContainer(typeKey).getValue()
						.toString();
			if (!scyDrawType.equals(eloType))
				throw new IllegalArgumentException("elo (" + eloUri + ") is of wrong type: " + eloType);
			IMetadata metadata = newElo.getMetadata();
			IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
			// TODO fixe the locale problem!!!
//			Object titleObject = metadataValueContainer.getValue();
//			Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
			Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);

			setDocName(titleObject3.toString());
			whiteboardPanel.deleteAllWhiteboardContainers();
			whiteboardPanel.setContentStatus(jdomStringConversion.stringToXml(newElo.getContent()
						.getXmlString()));
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
			elo.getMetadata().getMetadataValueContainer(typeKey).setValue("scy/drawing");
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
