package eu.scy.client.tools.drawing;

import java.awt.BorderLayout;
import java.net.URI;
import java.security.AccessControlException;
import java.util.List;
import java.util.Locale;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.springframework.util.StringUtils;

import roolo.api.IRepository;
import roolo.elo.JDomStringConversion;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;
import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.events.WhiteboardContainerChangedEvent;
import colab.vt.whiteboard.component.events.WhiteboardContainerChangedListener;
import colab.vt.whiteboard.component.events.WhiteboardContainerListChangedEvent;
import colab.vt.whiteboard.component.events.WhiteboardContainerListChangedListener;
import roolo.search.IQuery;
import roolo.search.ISearchResult;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class DrawingToolFrame extends javax.swing.JFrame implements
			WhiteboardContainerChangedListener, WhiteboardContainerListChangedListener
{
	private static final long serialVersionUID = 5967268619762532141L;
	private static final Logger logger = Logger.getLogger(DrawingToolFrame.class.getName());

	private IRepository repository;
	@SuppressWarnings("unused")
	private IMetadataTypeManager metadataTypeManager;
	private IELOFactory eloFactory;
	private JDomStringConversion jdomStringConversion = new JDomStringConversion();

	private WhiteboardPanel whiteboardPanel;
	private String docName = "untitiled";
	private IELO elo = null;
	private JMenuBar menuBar;
	private JMenuItem closeDrawingMenuItem;
	private JMenuItem savaAsDrawingMenuItem;
	private JMenuItem saveDrawingMenuItem;
	private JMenuItem loadDrawingMenuItem;
	private JMenuItem newDrawingMenuItem;
	private JMenu eloMenu;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args)
	{
		setupJavaLogging();
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				DrawingToolFrame inst = new DrawingToolFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	/**
	 * Setup the java logging.
	 * 
	 * @author Jakob Sikken
	 */
	private static void setupJavaLogging()
	{
		try
		{
			Logger setupLogger = Logger.getLogger("eu.scy");
			setLogLevel(setupLogger, Level.ALL);
			/*
			 * Logger cachedDataBaseAccessLogger = Logger.getLogger("kmquest.cachedDataBaseAccess");
			 * cachedDataBaseAccessLogger.setLevel(Level.INFO); Logger teamStateLogger =
			 * Logger.getLogger("kmquest.cachedDataBaseAccess.TeamState");
			 * teamStateLogger.setLevel(Level.ALL); Logger gameStateLogger =
			 * Logger.getLogger("kmquest.engine.GameState"); gameStateLogger.setLevel(Level.ALL);
			 * Logger worksheetDefinitionSetLogger = Logger.getLogger(
			 * "kmquest.cachedDataBaseAccess.WorksheetDefinitionSet");
			 * worksheetDefinitionSetLogger.setLevel(Level.INFO);
			 */
			// Logger tewnteVt = Logger.getLogger("eu.sc");
			// setLogLevel(tewnteVt, Level.WARNING);
			Handler consoleHandler = new ConsoleHandler();
			setLogLevel(consoleHandler, Level.ALL);
			setupLogger.addHandler(consoleHandler);
			setupLogger.setUseParentHandlers(false);
			setLogLevel(setupLogger, Level.FINEST);
		}
		catch (AccessControlException e)
		{
			// System.out.println("Access denied in setupJavaLogging " + e.getMessage());
		}
	}

	private static void setLogLevel(Logger logger, Level level)
	{
		try
		{
			logger.setLevel(level);
		}
		catch (AccessControlException e)
		{
			// System.out.println("Failed to set level of " + logger.getName() + " to " + level.getName()
			//			+ ": " + e.getMessage());
		}
	}

	private static void setLogLevel(Handler handler, Level level)
	{
		try
		{
			handler.setLevel(level);
		}
		catch (AccessControlException e)
		{
			// System.out.println("Failed to set level of  handler to " + level.getName() + ": "
			//			+ e.getMessage());
		}
	}

	/**
	 * Setup the java logging to a file.
	 * 
	 * @author Jakob Sikken
	 */
	// private void setupJavaLogFile()
	// {
	// try
	// {
	// File logFile = BrokerLogs.getLogFile("java");
	// if (logFile != null)
	// {
	// setupJavaLogFile(logFile.getAbsolutePath());
	// }
	// else
	// {
	// System.out
	// .println("Java Logging does not log to a file, because there is no log directory");
	// }
	// }
	// catch (Exception ie)
	// {
	// logger.log(Level.WARNING, "Exception during setup of the java logging to file", ie);
	// }
	// }
	//
	// private void setupJavaLogFile(String fileName) throws SecurityException, IOException
	// {
	// Logger setupLogger = Logger.getLogger("colab");
	// Handler fileHandler = new FileHandler(fileName);
	// fileHandler.setLevel(Level.ALL);
	// setupLogger.addHandler(fileHandler);
	// // System.out.println("Java Logging setup to log to " + fileName);
	// }
	public DrawingToolFrame()
	{
		super("Drawing Tool");
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				menuBar = new JMenuBar();
				setJMenuBar(menuBar);
				menuBar.setPreferredSize(new java.awt.Dimension(392, 21));
				{
					eloMenu = new JMenu("ELO access");
					menuBar.add(eloMenu);
					eloMenu.setName("eloMenu");
					{
						newDrawingMenuItem = new JMenuItem();
						eloMenu.add(newDrawingMenuItem);
						newDrawingMenuItem.setAction(getAppActionMap().get("newDrawingAction"));
						newDrawingMenuItem.setBounds(0, 0, 35, 23);
					}
					{
						loadDrawingMenuItem = new JMenuItem();
						eloMenu.add(loadDrawingMenuItem);
						loadDrawingMenuItem.setAction(getAppActionMap().get("loadDrawingAction"));
					}
					{
						saveDrawingMenuItem = new JMenuItem();
						eloMenu.add(saveDrawingMenuItem);
						saveDrawingMenuItem.setAction(getAppActionMap().get("saveDrawingAction"));
					}
					{
						savaAsDrawingMenuItem = new JMenuItem();
						eloMenu.add(savaAsDrawingMenuItem);
						savaAsDrawingMenuItem.setAction(getAppActionMap().get("savaAsDrawingAction"));
					}
					{
						closeDrawingMenuItem = new JMenuItem();
						eloMenu.add(closeDrawingMenuItem);
						closeDrawingMenuItem.setAction(getAppActionMap().get("closeDrawingAction"));
					}
				}
			}
			{
				whiteboardPanel = new WhiteboardPanel();
				getContentPane().add(whiteboardPanel, BorderLayout.CENTER);
			}
			pack();
			setSize(400, 300);
			Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(
						getContentPane());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Returns the action map used by this application. Actions defined using the Action annotation
	 * are returned by this method
	 */
	private ApplicationActionMap getAppActionMap()
	{
		return Application.getInstance().getContext().getActionMap(this);
	}

	private void setDocName(String docName)
	{
		this.docName = docName;
		String windowTitle = "Drawing: ";
		if (StringUtils.hasText(docName))
			windowTitle += docName;
		setTitle(windowTitle);
	}

	@Action
	public void newDrawingAction()
	{
		whiteboardPanel.deleteAllWhiteboardContainers();
		elo = null;
	}

	@Action
	public void loadDrawingAction()
	{

		IQuery query = null;
//		IMetadataQuery<IMetadataKey> metadataQuery = new BasicMetadataQuery<IMetadataKey>(titleKey,
//					BasicSearchOperations.LESS, "n", null);
//		query = metadataQuery;
		List<ISearchResult> searchResults = repository.search(query);
		URI[] drawingUris = new URI[searchResults.size()];
		int i = 0;
		for (ISearchResult searchResult : searchResults)
			drawingUris[i++] = searchResult.getUri();
		URI drawingUri = (URI) JOptionPane.showInputDialog(this, "Select drawing", "Select drawing",
					JOptionPane.QUESTION_MESSAGE, null, drawingUris, null);
		if (drawingUri != null)
		{
			IELO newElo = repository.retrieveELO(drawingUri);
			if (newElo != null)
			{
				// URI docUri = elo.getUri();
				// setDocName(docUri.getPath());
				setDocName((String) newElo.getMetadata().getMetadataValueContainer(titleKey).getValue());
				whiteboardPanel.deleteAllWhiteboardContainers();
				whiteboardPanel.setContentStatus(jdomStringConversion.stringToXml(newElo.getContent()
							.getXmlString()));
			}
		}
	}

	@Action
	public void saveDrawingAction()
	{
		logger.fine("save drawing");
		if (elo == null)
		{
			savaAsDrawingAction();
		}
		else
		{
			elo.getContent().setXmlString(
						jdomStringConversion.xmlToString(whiteboardPanel.getContentStatus()));
			IMetadata resultMetadata = repository.updateELO(elo);
			eloFactory.updateELOWithResult(elo, resultMetadata);
		}
	}

	@Action
	public void savaAsDrawingAction()
	{
		logger.fine("save as drawing");
		String drawingName = JOptionPane.showInputDialog("Enter drawing name:", docName);
		if (StringUtils.hasText(drawingName))
		{
			setDocName(drawingName);
			elo = eloFactory.createELO();
			elo.setDefaultLanguage(Locale.getDefault());
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
		}
	}

	// private void updateEloWithNewMetadata(IELO<IMetadataKey> elo, IMetadata<IMetadataKey>
	// eloMetadata)
	// {
	// elo.setMetaData(eloMetadata);
	// URI newUri = (URI) eloMetadata.getMetadataValueContainer(uriKey).getValue();
	// elo.setUri(newUri);
	// }

	@Action
	public void closeDrawingAction()
	{
		this.dispose();
	}

	public void setRepository(IRepository repository)
	{
		this.repository = repository;
	}

	public void whiteboardContainerChanged(WhiteboardContainerChangedEvent arg0)
	{
	}

	public void whiteboardContainerAdded(WhiteboardContainerChangedEvent arg0)
	{
	}

	public void whiteboardContainerDeleted(WhiteboardContainerChangedEvent arg0)
	{
	}

	public void whiteboardContainersCleared(WhiteboardContainerListChangedEvent arg0)
	{
	}

	public void whiteboardContainersLoaded(WhiteboardContainerListChangedEvent arg0)
	{
	}

	public void whiteboardPanelLoaded(WhiteboardContainerListChangedEvent arg0)
	{
	}

	@SuppressWarnings("unused")
	private IMetadataKey uriKey;
	private IMetadataKey titleKey;
	private IMetadataKey typeKey;
	private IMetadataKey dateCreatedKey;
//	private IMetadataKey missionKey;
	private IMetadataKey authorKey;

	public void setMetadataTypeManager(IMetadataTypeManager metadataTypeManager)
	{
		this.metadataTypeManager = metadataTypeManager;
		uriKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
		titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
		typeKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
		dateCreatedKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
//		missionKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.MISSION.getId());
		authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
	}

	public void setEloFactory(IELOFactory eloFactory)
	{
		this.eloFactory = eloFactory;
	}

}
