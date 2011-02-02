package eu.scy.client.tools.drawing;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessControlException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;


public class DrawingApp extends SingleFrameApplication implements
			ELOLoadedChangedListener
{
	private static final Logger logger = Logger.getLogger(DrawingApp.class.getName());

	private JMenuBar menuBar;
	private JMenuItem newDrawingMenuItem;
	private JMenuItem saveAsDrawingMenuItem;
	private JMenuItem saveDrawingMenuItem;
	private JMenuItem loadDrawingMenuItem;
	private JMenu eloMenu;

	private SpringRoloEloDrawingPanel whiteboardPanel;
	private URI initialUri = null;

	private ActionMap getAppActionMap()
	{
		return Application.getInstance().getContext().getActionMap(this);
	}

	@Override
	protected void initialize(String[] args)
	{
		super.initialize(args);
		if (args.length > 0)
		{
			try
			{
				initialUri = new URI(args[0]);
			}
			catch (URISyntaxException e)
			{
				logger.warning("parameter is not a uri: " + e.getMessage());
			}
		}
	}

	@Override
	protected void startup()
	{
//		printWebStartInfo();
		{
			whiteboardPanel = new SpringRoloEloDrawingPanel();
			URL codeBase = null;
			try
			{
				codeBase = new URL("http://localhost:8080/roolo-mock-server/");
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
//			whiteboardPanel.setupSpringRoolo(getCodeBase());
			whiteboardPanel.setupSpringRoolo(codeBase);
			whiteboardPanel.initGUI();
		}
		menuBar = new JMenuBar();
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
				saveAsDrawingMenuItem = new JMenuItem();
				eloMenu.add(saveAsDrawingMenuItem);
				saveAsDrawingMenuItem.setAction(getAppActionMap().get("saveAsDrawingAction"));
			}
			// {
			// closeDrawingMenuItem = new JMenuItem();
			// eloMenu.add(closeDrawingMenuItem);
			// closeDrawingMenuItem.setAction(getAppActionMap().get("closeDrawingAction"));
			// }
		}
		getMainFrame().setJMenuBar(menuBar);
		whiteboardPanel.addELOLoadedChangedListener(this);
		if (initialUri != null)
			whiteboardPanel.loadElo(initialUri);
		show(whiteboardPanel);
		setWindowTitle();
	}
	
//	private URL getCodeBase()
//	{
//		try
//		{
//			BasicService basicService = (BasicService) ServiceManager.lookup(BasicService.class.getName());
//			return basicService.getCodeBase();
//		}
//		catch (UnavailableServiceException e)
//		{
//			// System.out.println("BasicService not available, " + e.getMessage());
//		}
//		return null;
//	}
//
//	private void printWebStartInfo()
//	{
//		String[] webStartServiceNames = ServiceManager.getServiceNames();
//		if (webStartServiceNames != null)
//		{
//			for (String name : webStartServiceNames)
//				// System.out.println("Web start service name: " + name);
//			BasicService basicService;
//			try
//			{
//				basicService = (BasicService) ServiceManager.lookup(BasicService.class.getName());
//				// System.out.println("getCodeBase" + basicService.getCodeBase());
//			}
//			catch (UnavailableServiceException e)
//			{
//				// System.out.println("BasicService not available, " + e.getMessage());
//			}
//		}
//	}

	public void eloLoadedChanged(ELOLoadedChangedEvent eloLoadedChangedEvent)
	{
		setWindowTitle();
	}

	private void setWindowTitle()
	{
		String docTitle = whiteboardPanel.getELOTitle();
		if (docTitle == null)
			docTitle = "untitled";
		getMainFrame().setTitle("Drawing: " + docTitle);
	}

	@Action
	public void newDrawingAction()
	{
		whiteboardPanel.newDrawingAction();
	}

	@Action
	public void loadDrawingAction()
	{
		whiteboardPanel.loadDrawingAction();
	}

	@Action
	public void saveDrawingAction()
	{
		whiteboardPanel.saveDrawingAction();
	}

	@Action
	public void saveAsDrawingAction()
	{
		whiteboardPanel.saveAsDrawingAction();
	}

	public static void main(String[] args)
	{
		setupJavaLogging();
		// Thread.setDefaultUncaughtExceptionHandler(new DialogUncaughtExceptionHandler());
		launch(DrawingApp.class, args);
//		JOptionPane.showMessageDialog(null, "End main");
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
			Logger setupLogger = Logger.getLogger("");
			setLogLevel(setupLogger, Level.ALL);

			setLogLevel("java", Level.OFF);
			setLogLevel("javax", Level.OFF);
			setLogLevel("sun", Level.OFF);
			setLogLevel("org.springframework", Level.WARNING);

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

	private static void setLogLevel(String path, Level level)
	{
		Logger aLogger = Logger.getLogger(path);
		setLogLevel(aLogger, level);
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

}
