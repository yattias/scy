package eu.scy.client.tools.drawing;

import java.awt.BorderLayout;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;

public class DrawingApplet extends javax.swing.JApplet
{
	private static final long serialVersionUID = -421791817447634562L;

	private static final Logger logger = Logger.getLogger(DrawingApplet.class.getName());
	
	private static final String eloUriName = "eloUri";

	private JMenuBar menuBar;
	private JMenuItem newDrawingMenuItem;
	private JMenuItem saveAsDrawingMenuItem;
	private JMenuItem saveDrawingMenuItem;
	private JMenuItem loadDrawingMenuItem;
	private JMenu eloMenu;

	private SpringRoloEloDrawingPanel whiteboardPanel;
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
				DrawingApplet inst = new DrawingApplet();
				frame.getContentPane().add(inst);
				((JComponent) frame.getContentPane()).setPreferredSize(inst.getSize());
				frame.pack();
				frame.setVisible(true);
			}
		});

	}

	public DrawingApplet()
	{
		super();
		writePropertiesForApplet();
//		writeParametersForApplet();
//		initGUI();
	}
	
	@Override
	public void init()
	{
		super.init();
		writeParametersForApplet();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			whiteboardPanel = new SpringRoloEloDrawingPanel();
			whiteboardPanel.setupSpringRoolo(getAppletCodeBase());
			whiteboardPanel.initGUI();
			URI initialUri = null;
			String initilaUriString = getParameter(eloUriName);
			if (initilaUriString!=null)
			{
				try
				{
					initialUri = new URI(initilaUriString);
				}
				catch (URISyntaxException e)
				{
					logger.warning("parameter is not a uri: " + e.getMessage());
				}
			}
			if (initialUri != null)
				whiteboardPanel.loadElo(initialUri);
			// setSize(new Dimension(400, 300));
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
			}
			{
				getContentPane().add(whiteboardPanel, BorderLayout.CENTER);
			}
			Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private URL getAppletCodeBase()
	{
		try
		{
			return getCodeBase();
		}
		catch (Exception e)
		{
			logger.info("problems during codeBase:" + e.getMessage());
		}
		return null;
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
	 * 
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
		}
		catch (Exception e)
		{
			logger.info("problems during codeBase:" + e.getMessage());
		}
		try
		{
			// System.out.println("documentBase      : " + getDocumentBase());
		}
		catch (Exception e)
		{
			logger.info("problems during documentBase:" + e.getMessage());
		}
		// System.out.println();
	}

}
