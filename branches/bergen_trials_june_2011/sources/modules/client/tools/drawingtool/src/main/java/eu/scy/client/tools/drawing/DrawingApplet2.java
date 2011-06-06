package eu.scy.client.tools.drawing;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.net.URI;
import java.security.AccessControlException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
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
public class DrawingApplet2 extends javax.swing.JApplet implements
			WhiteboardContainerChangedListener, WhiteboardContainerListChangedListener
{
	private static final long serialVersionUID = -421791817447634562L;

	private static final Logger logger = Logger.getLogger(DrawingApplet2.class.getName());
	private final String CONTEXT_CONFIG_FILE_LOCATION = "";
	private final String CONTEXT_CONFIG_CLASS_PATH_LOCATION = "eu/scy/client/tools/drawing/rooloConfig.xml";

	private ApplicationContext springApplicationContext;

	private JMenuBar menuBar;
	private JMenuItem newDrawingMenuItem;
	private JMenuItem savaAsDrawingMenuItem;
	private JMenuItem saveDrawingMenuItem;
	private JMenuItem loadDrawingMenuItem;
	private JMenu eloMenu;

	private IRepository repository;
	private IMetadataTypeManager metadataTypeManager;
	private IELOFactory eloFactory;
	@SuppressWarnings("unused")
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
	/**
	 * Auto-generated main method to display this JApplet inside a new JFrame.
	 */
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame frame = new JFrame();
				DrawingApplet2 inst = new DrawingApplet2();
				frame.getContentPane().add(inst);
				((JComponent) frame.getContentPane()).setPreferredSize(inst.getSize());
				frame.pack();
				frame.setVisible(true);
			}
		});

	}

	public DrawingApplet2()
	{
		super();
		writePropertiesForApplet();
		writeParametersForApplet();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			setupRoolo();
//			setSize(new Dimension(400, 300));
			{
				menuBar = new JMenuBar();
				setJMenuBar(menuBar);
				menuBar.setPreferredSize(new java.awt.Dimension(392, 20));
				{
					eloMenu = new JMenu();
					menuBar.add(eloMenu);
					eloMenu.setName("eloMenu");
					{
						newDrawingMenuItem = new JMenuItem();
						eloMenu.add(newDrawingMenuItem);
						newDrawingMenuItem.setName("newDrawingMenuItem");
						newDrawingMenuItem.setAction(getAppActionMap().get("newDrawingAction"));
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
//					{
//						closeDrawingMenuItem = new JMenuItem();
//						eloMenu.add(closeDrawingMenuItem);
//						closeDrawingMenuItem.setAction(getAppActionMap().get("closeDrawingAction"));
//					}
				}
			}
			{
				whiteboardPanel = new WhiteboardPanel();
				getContentPane().add(whiteboardPanel, BorderLayout.CENTER);
			}
			Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void setupRoolo()
	{
		springApplicationContext = getSpringApplicationContext();
		if (springApplicationContext == null)
			throw new IllegalStateException("failed to find spring context");
		repository = (IRepository) getSpringBean("repository");
		eloFactory = (IELOFactory) getSpringBean("eloFactory");
		metadataTypeManager = (IMetadataTypeManager) getSpringBean("metadataTypeManager");
		uriKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
		titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
		typeKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
		dateCreatedKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED.getId());
//		missionKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.MISSION.getId());
		authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId());
	}

	protected ApplicationContext getSpringApplicationContext()
	{
		ApplicationContext springContext = null;
		try
		{
			springContext = new FileSystemXmlApplicationContext(CONTEXT_CONFIG_FILE_LOCATION);
		}
		catch (AccessControlException e)
		{
			// System.out.println("Could not access file " + CONTEXT_CONFIG_FILE_LOCATION
			//			+ ", trying on class path");
		}
		catch (BeanDefinitionStoreException e)
		{
			if (!(e.getRootCause() instanceof FileNotFoundException))
				throw e;
			// System.out.println("Could not find file " + CONTEXT_CONFIG_FILE_LOCATION
			//			+ ", trying on class path");
		}
		if (springContext == null)
			springContext = new ClassPathXmlApplicationContext(CONTEXT_CONFIG_CLASS_PATH_LOCATION);
		return springContext;
	}

	private Object getSpringBean(String name)
	{
		try
		{
			Object bean = springApplicationContext.getBean(name);
			return bean;
		}
		catch (NoSuchBeanDefinitionException e)
		{
			throw new IllegalStateException("failed to find repository");
		}
	}

	private void setDocName(String docName)
	{
		this.docName = docName;
		String windowTitle = "Drawing: ";
		if (StringUtils.hasText(docName))
			windowTitle += docName;
		// setTitle(windowTitle);
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
		// query = metadataQuery;
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

	/**
	 * Returns the action map used by this application. Actions defined using the Action annotation
	 * are returned by this method
	 */
	private ApplicationActionMap getAppActionMap()
	{
		return Application.getInstance().getContext().getActionMap(this);
	}

	/** 
	* Writes the value of some system properties accessible to applets to the Java console.
	* @see writePropertiesForApplication()
	*/
	public static void writePropertiesForApplet()
	{
		// System.out.println("System properties");
		// System.out.println("java.version      : " + System.getProperty("java.version"));
		// System.out.println("java.vendor       : " + System.getProperty("java.vendor"));
		// System.out.println("java.vendor.url   : " + System.getProperty("java.vendor.url"));
		// System.out.println("java.class.version: " + System.getProperty("java.class.version"));
		// System.out.println("os.name           : " + System.getProperty("os.name"));
		// System.out.println("os.arch           : " + System.getProperty("os.arch"));
		// System.out.println("os.version        : " + System.getProperty("os.version"));
		// System.out.println("file.separator    : " + System.getProperty("file.separator"));
		// System.out.println("path.separator    : " + System.getProperty("path.separator"));
		// System.out.println("line.separator    : " + System.getProperty("line.separator"));
		// System.out.println();
	}  

	public void writeParametersForApplet()
	{
		// System.out.println("Parameters");
		try
		{
			// System.out.println("codeBase          : " + getCodeBase());
			// System.out.println("documentBase      : " + getDocumentBase());
		}
		catch (Exception e)
		{
			logger.info("problems during getting applet info:" + e.getMessage());
		}
		// System.out.println();
	}
	

}
