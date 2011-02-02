package colab.vt.whiteboard.application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.PrintException;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.events.WhiteboardContainerChangedEvent;
import colab.vt.whiteboard.component.events.WhiteboardContainerChangedListener;
import colab.vt.whiteboard.component.events.WhiteboardContainerListChangedEvent;
import colab.vt.whiteboard.component.events.WhiteboardContainerListChangedListener;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class WhiteboardApp extends javax.swing.JFrame implements
			WhiteboardContainerChangedListener, WhiteboardContainerListChangedListener
{
	private static final long serialVersionUID = 3865830959160769161L;
	private static final Logger logger = Logger.getLogger(WhiteboardApp.class.getName());

	private JMenuItem helpMenuItem;
	private JMenu jMenu5;
	private WhiteboardPanel whiteboardPanel;
	private JMenuItem deleteMenuItem;
	private JSeparator jSeparator1;
	private JMenuItem pasteMenuItem;
	private JMenuItem copyMenuItem;
	private JMenuItem cutMenuItem;
	private JMenu jMenu4;
	private JMenuItem exitMenuItem;
	private JSeparator jSeparator2;
	private JSeparator jSeparator3;
	private JMenuItem closeFileMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem openFileMenuItem;
	private JMenuItem newFileMenuItem;
	private JMenuItem printFileMenuItem;
	private JMenu jMenu3;
	private JMenuBar jMenuBar1;

	private transient SAXBuilder builder = new SAXBuilder(false);
	private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
	private File lastUsedFile = new File("store/text.wb");

	/**
	 * Setup the java logging.
	 * 
	 * @author Jakob Sikken
	 */
	private static void setupJavaLogging()
	{
		try
		{
			Logger setupLogger = Logger.getLogger("colab");
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
			// Logger tewnteVt = Logger.getLogger("colab.utwente.vt");
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
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args)
	{
		setupJavaLogging();
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				WhiteboardApp inst = new WhiteboardApp();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public WhiteboardApp()
	{
		super();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			{
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				this.setTitle("Whiteboard test app");
			}
			{
				{
					whiteboardPanel = new WhiteboardPanel();
					getContentPane().add(whiteboardPanel, BorderLayout.CENTER);
					whiteboardPanel.addWhiteboardContainerChangedListener(this);
					whiteboardPanel.addWhiteboardContainerListChangedListener(this);
				}
			}
			setSize(400, 300);
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu3 = new JMenu();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("File");
					{
						newFileMenuItem = new JMenuItem();
						jMenu3.add(newFileMenuItem);
						newFileMenuItem.setText("New");
						newFileMenuItem.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								whiteboardNew();
							}
						});
					}
					{
						openFileMenuItem = new JMenuItem();
						jMenu3.add(openFileMenuItem);
						openFileMenuItem.setText("Open");
						openFileMenuItem.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								whiteboardOpen();
							}
						});
					}
					{
						saveMenuItem = new JMenuItem();
						jMenu3.add(saveMenuItem);
						saveMenuItem.setText("Save");
						saveMenuItem.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								whiteboardSave();
							}
						});
					}
					{
						saveAsMenuItem = new JMenuItem();
						jMenu3.add(saveAsMenuItem);
						saveAsMenuItem.setText("Save As ...");
						saveAsMenuItem.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								whiteboardSaveAs();
							}
						});
					}
					{
						closeFileMenuItem = new JMenuItem();
						jMenu3.add(closeFileMenuItem);
						closeFileMenuItem.setText("Close");
						closeFileMenuItem.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								whiteboardClose();
							}
						});
					}
					{
						jSeparator2 = new JSeparator();
						jMenu3.add(jSeparator2);
					}
					{
						printFileMenuItem = new JMenuItem();
						jMenu3.add(printFileMenuItem);
						printFileMenuItem.setText("Print");
						printFileMenuItem.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								// whiteboardPrint();
								whiteboardPrint12();
							}
						});
					}
					{
						jSeparator3 = new JSeparator();
						jMenu3.add(jSeparator3);
					}
					{
						exitMenuItem = new JMenuItem();
						jMenu3.add(exitMenuItem);
						exitMenuItem.setText("Exit");
					}
				}
				{
					jMenu4 = new JMenu();
					jMenuBar1.add(jMenu4);
					jMenu4.setText("Edit");
					{
						cutMenuItem = new JMenuItem();
						jMenu4.add(cutMenuItem);
						cutMenuItem.setText("Cut");
					}
					{
						copyMenuItem = new JMenuItem();
						jMenu4.add(copyMenuItem);
						copyMenuItem.setText("Copy");
					}
					{
						pasteMenuItem = new JMenuItem();
						jMenu4.add(pasteMenuItem);
						pasteMenuItem.setText("Paste");
					}
					{
						jSeparator1 = new JSeparator();
						jMenu4.add(jSeparator1);
					}
					{
						deleteMenuItem = new JMenuItem();
						jMenu4.add(deleteMenuItem);
						deleteMenuItem.setText("Delete");
					}
				}
				{
					jMenu5 = new JMenu();
					jMenuBar1.add(jMenu5);
					jMenu5.setText("Help");
					{
						helpMenuItem = new JMenuItem();
						jMenu5.add(helpMenuItem);
						helpMenuItem.setText("Help");
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void whiteboardNew()
	{
		whiteboardPanel.deleteAllWhiteboardContainers();
	}

	protected void whiteboardOpen()
	{
		JFileChooser fileChooser = new JFileChooser();
		if (lastUsedFile != null)
			fileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
		int userResponse = fileChooser.showOpenDialog(this);
		if (userResponse == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			lastUsedFile = file;
			FileReader fileReader = null;
			try
			{
				fileReader = new FileReader(file);
				Document doc = builder.build(fileReader, file.getAbsolutePath());
				whiteboardPanel.deleteAllWhiteboardContainers();
				whiteboardPanel.setStatus(doc.getRootElement());
			}
			catch (Exception e)
			{
				logger.log(Level.WARNING, "problems during opening", e);
			}
			finally
			{
				if (fileReader != null)
					try
					{
						fileReader.close();
					}
					catch (IOException e)
					{
						logger.log(Level.WARNING, "problems during closing", e);
					}
			}
		}
	}

	protected void whiteboardSave()
	{
		JFileChooser fileChooser = new JFileChooser();
		if (lastUsedFile != null)
			fileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
		int userResponse = fileChooser.showSaveDialog(this);
		if (userResponse == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			lastUsedFile = file;
			FileWriter fileWriter = null;
			try
			{
				fileWriter = new FileWriter(file);
				Element whiteboardRoot = whiteboardPanel.getStatus();
				xmlOutputter.output(whiteboardRoot, fileWriter);
			}
			catch (IOException e)
			{
				logger.log(Level.WARNING, "problems during saving", e);
			}
			finally
			{
				if (fileWriter != null)
					try
					{
						fileWriter.close();
					}
					catch (IOException e)
					{
						logger.log(Level.WARNING, "problems during closing", e);
					}
			}
		}

	}

	protected void whiteboardSaveAs()
	{
		// TODO Auto-generated method stub

	}

	protected void whiteboardClose()
	{
		// TODO Auto-generated method stub

	}

	protected void whiteboardPrint()
	{
		try
		{
			whiteboardPanel.printGraphicsDoc();
		}
		catch (PrintException e)
		{
			logger.log(Level.WARNING, "problems during printing", e);
		}
	}

	protected void whiteboardPrint12()
	{
		try
		{
			whiteboardPanel.printGraphics12();
		}
		catch (PrinterException e)
		{
			logger.log(Level.WARNING, "problems during printing", e);
		}
	}

	@Override
	public void whiteboardContainerChanged(
				WhiteboardContainerChangedEvent whiteboardContainerChangedEvent)
	{
//		XMLOutputter xmlOutputterCompact = new XMLOutputter(Format.getCompactFormat());
//		XMLOutputter xmlOutputterPretty = new XMLOutputter(Format.getPrettyFormat());
//		XMLOutputter xmlOutputterRaw = new XMLOutputter(Format.getRawFormat());
//		long startTime = System.currentTimeMillis();
//		Element status = whiteboardContainerChangedEvent.getWhiteboardContainer().getStatus();
//		long statusTime = System.currentTimeMillis();
//		xmlOutputter = xmlOutputterCompact;
//		String data = xmlToString(status);
//		int compactLength = data.length();
//		long dataCompactTime = System.currentTimeMillis();
//		xmlOutputter = xmlOutputterPretty;
//		data = xmlToString(status);
//		int prettyLength = data.length();
//		long dataPrettyTime = System.currentTimeMillis();
//		xmlOutputter = xmlOutputterRaw;
//		data = xmlToString(status);
//		int rawLength = data.length();
//		long dataRawTime = System.currentTimeMillis();
//		long statusMillis = statusTime - startTime;
//		long dataCompactMillis = dataCompactTime - statusTime;
//		long dataPrettyMillis = dataPrettyTime - dataCompactTime;
//		long dataRawMillis = dataRawTime - dataPrettyTime;
//		// System.out.println("status:" + statusMillis + ", dataCompact(" + compactLength + "):"
//					+ dataCompactMillis + ", dataPretty(" + prettyLength + "):" + dataPrettyMillis
//					+ ", dataRaw(" + rawLength + "):" + dataRawMillis);
	}

	@Override
	public void whiteboardContainerAdded(
				WhiteboardContainerChangedEvent whiteboardContainerChangedEvent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void whiteboardContainerDeleted(
				WhiteboardContainerChangedEvent whiteboardContainerChangedEvent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void whiteboardContainersCleared(
				WhiteboardContainerListChangedEvent whiteboardContainerListChangedEvent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void whiteboardContainersLoaded(
				WhiteboardContainerListChangedEvent whiteboardContainerListChangedEvent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void whiteboardPanelLoaded(
				WhiteboardContainerListChangedEvent whiteboardContainerListChangedEvent)
	{
		// TODO Auto-generated method stub

	}

}
