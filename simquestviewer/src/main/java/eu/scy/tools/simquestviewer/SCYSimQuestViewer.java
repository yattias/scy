package eu.scy.tools.simquestviewer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Frame;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import sqv.Application;
import sqv.Header;
import sqv.ISimQuestViewer;
import sqv.Interface;
import sqv.Model;
import sqv.SimQuestViewer;
import sqv.TopicBrowser;
import sqv.data.DataServer;
import sqv.parser.ApplicationParser;
import sqv.parser.HeaderParser;
import utils.FileName;
import utils.ResourceManager;

/**
 * This class is very similar to {@link sqv.SimQuestViewer} (which is
 * unfortunately final). It adds the {@link eu.scy.tools.simquestviewer.DataCollector}
 * panel to the user interface.
 * 
 * @author P.M. Visser, Lars Bollen
 * 
 */
public final class SCYSimQuestViewer implements Runnable, ISimQuestViewer {
    /**
     * Used to store the command line arguments
     */
    private static String[] m_args = null;
    
    /**
     * Reference to the mainframe of the application
     */
    JFrame m_mainframe;
    
    /**
     * Holds the amount of memory that is in use when starting the application
     */
    double m_startmem;
    
    // Logging stuff
    public static Logger logger = Logger.getLogger(SimQuestViewer.class.getName());
    // private static FileHandler logFile = new FileHandler("sqv.log");
    
    public Application g_application;
    private Interface g_interface;
    private DataServer g_dataServer;
    
    /**
     * The main function, will set the system look and feel and puts the
     * application on the event queue
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        m_args = args;
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            System.err.println("Couldn't use system look and feel.");
        }
        EventQueue.invokeLater(new SCYSimQuestViewer());
    }
    
    public void run() {
        // Used to get an idea on memory usage of this application
        m_startmem = Runtime.getRuntime().totalMemory()
        - Runtime.getRuntime().freeMemory();
        
        // The fileName that is specified, may by a zip file or the sqv file
        FileName fileName = null;
        
        // The sqv file
        FileName sqvFile = null;
        
        // Parse the command line arguments
        // 1 argument can be passed, the name of the sqv file (zipped or
        // unzipped)
        if (m_args.length > 0) {
            System.out.println("Opening file : " + m_args[0]);
            fileName = new FileName(m_args[0]);
            
            if (fileName.fileExists() == false) {
                System.err.println("File does not exist : "
                        + fileName.getFullPath());
                System.exit(1);
            }
        } else {
            System.err.println("No file specified!");
            System.exit(1);
        }
        
        // Check if the fileName is a zip-file
        // If it is a zip file, extract to the temporary directory and test if
        // there is a sqv file
        if (isZip(fileName.getFullPath()) == true) {
            System.out.println("Zipfile detected");
            
            // Get the temp directory for compressed files
            String extractDirectory = System.getProperty("java.io.tmpdir")
            + File.separator + fileName.getName() + File.separator;
            System.out.println(extractDirectory);
            
            if (unZip(fileName.getFullPath(), extractDirectory) == true) {
                System.out.println(fileName.getFullName()
                        + " was successfully unzipped");
                sqvFile = new FileName(extractDirectory + fileName.getName()
                        + ".sqx");
                
                // Check if the sqvFile exists
                if (sqvFile.fileExists() == false) {
                    System.err.println("File does not exist : "
                            + sqvFile.getFullPath());
                    System.exit(1);
                }
            } else {
                System.err.println(fileName.getFullName() + " error unzipping");
                System.exit(1);
            }
        } else {
            // Set the sqvfile to the fileName
            sqvFile = fileName;
            System.out.println("regular file detected");
        }
        
        // Change the working directory (to make sure the xsd can be found)
        System.setProperty("user.dir", sqvFile.getPath());
        
        createAndShowGUI();
        
        // The root logger's handlers default to INFO. We have to
        // crank them up. We could crank up only some of them
        // if we wanted, but we will turn them all up.
        Handler[] handlers = Logger.getLogger("").getHandlers();
        for (int index = 0; index < handlers.length; index++) {
            handlers[index].setLevel(Level.OFF);
        }
        
        logger.setLevel(Level.ALL);
        logger.info("Simquest Viewer run method");
        
        // Create the dataServer
        // TODO remove (this).. is a hack for notify mechanism
        g_dataServer = new DataServer();
        
        g_application = null;
        Model g_model = null;
        g_interface = null;
        
        // Read the xml file
        try {
            SAXBuilder builder = new SAXBuilder(true);
            builder.setFeature(
                    "http://apache.org/xml/features/validation/schema", true);
            // builder.setFeature("http://apache.org/xml/features/validation/schema-full-checking",
            // true);
            
            Document doc = builder.build(sqvFile.getFullPath());
            
            Element elRoot = doc.getRootElement();
            
            // Parse the application
            g_application = ApplicationParser.process(elRoot);
            
            // ######## Create the model (parse the variables &
            // computationalModel) ######
            Element elModel;
            elModel = findFirst(elRoot, "model", 0);
            g_model = new Model(elModel, g_dataServer);
            
            // Create the interface
            Element elInterface;
            elInterface = findFirst(elRoot, "interface", 0);
            //g_interface = new Interface(elInterface, g_dataServer);
            // dummy variable to get things compiled
            g_interface = new Interface(elInterface, g_dataServer, new ResourceManager(null, false));
        } catch (JDOMException e) {
            System.out.println("Error loading XML: " + e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // your code to catch the exception
            e.printStackTrace();
        }
        
        // Set the interface into the main frame
        // m_mainframe.setContentPane( g_interface.m_panel );
        Container pane = m_mainframe.getContentPane();
        pane.add(g_interface.m_panel, BorderLayout.CENTER);
        
        TopicBrowser topicBrowser = new TopicBrowser(this);
        pane.add(topicBrowser, BorderLayout.EAST);
        
        DataCollector dataCollector = new DataCollector(this);
        pane.add(dataCollector, BorderLayout.SOUTH);
        
        // Show the expression browser
        // *
        // ExpressionBrowser expressionBrowser = new ExpressionBrowser(
        // g_model.getComputationalModel() );
        // expressionBrowser.createAndShowGUI();
        // */
        
        // g_model.release();
        // g_model = null;
        
        // printUsedMemory();
        // Runtime.getRuntime().gc();
        // printUsedMemory();
    }
    
    public void setInterface(Element elInterface) {
        if (g_interface != null) {
            Container pane = m_mainframe.getContentPane();
            pane.remove(g_interface.m_panel);
            g_interface.release();
            g_interface = null;
            
            //dummy variables to get things compiled
            //g_interface = new Interface(elInterface, g_dataServer);
            g_interface = new Interface(elInterface, g_dataServer, new ResourceManager(null, false));
            pane.add(g_interface.m_panel, BorderLayout.CENTER);
            
            pane.validate();
        }
        
        Element elHeader = elInterface.getChild("header");
        Header header = HeaderParser.process(elHeader);
        System.out.println(header.getName());
    }
    
    /**
     * Returns the first element with the specified name
     */
    @SuppressWarnings("unchecked")
    private Element findFirst(Element element, String name, int depth) {
        
        Element result = null;
        Boolean nodeFound = false;
        String elementName = element.getName();
        
        List elChilds = element.getChildren();
        Iterator iterator = elChilds.iterator();
        while (iterator.hasNext() && (nodeFound == false)) {
            if (elementName.equalsIgnoreCase(name) == false) {
                Element elChild = (Element) iterator.next();
                result = findFirst(elChild, name, depth + 1);
                if (result != null) {
                    nodeFound = true;
                }
            } else {
                logger.fine(name + "node has been found");
                nodeFound = true;
                result = element;
            }
        }
        
        return result;
    }
    
    /**
     * Prints the memory that is currently in use and the memory that is used by
     * the SimQuestViewer
     */
    public void printUsedMemory() {
        double finishmem = Runtime.getRuntime().totalMemory()
        - Runtime.getRuntime().freeMemory();
        double printmem = (finishmem - m_startmem);
        System.out.format("Current used memory %g (MB)\n", finishmem
                / (1024 * 1024));
        System.out.format("Used memory by the SCYSimQuestViewer %g (MB)\n",
                printmem / (1024 * 1024));
    }
    
    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event dispatch thread.
     */
    private void createAndShowGUI() {
        // Create and set up the window.
        m_mainframe = new JFrame("SCYSimQuest Viewer");
        m_mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Display the window.
        m_mainframe.pack();
        m_mainframe.setVisible(true);
        
        m_mainframe.setSize(1024, 768);
    }
    
    /**
     * Check if a file is a zip file
     * 
     * @param path
     *            the path to the zip file to check
     * @return true if file is a zipfile otherwise false
     */
    public static boolean isZip(String path) {
        boolean returnValue = true;
        
        try {
            ZipFile zf = new ZipFile(path);
            if (zf.size() == 0) {
                returnValue = false;
            }
        } catch (ZipException e) {
            returnValue = false;
        } catch (IOException e) {
            returnValue = false;
        }
        
        return returnValue;
    }
    
    /**
     * unZip a zip file, and delete files on exit
     * 
     * @param inputPath
     *            path to the zipFile
     * @param outputDir
     *            output directory where the zipfile will be unzipped
     * @return true on success otherwise false
     */
    public static boolean unZip(String inputPath, String outputDir) {
        File tmpFile;
        
        // set the outputDir
        tmpFile = new File(outputDir);
        
        // Delete the output directory if it exists
        deleteDirectory(tmpFile);
        
        if (tmpFile.mkdir() == false) {
            System.err.println("Could not create output directory : "
                    + outputDir);
            return false;
        }
        
        try {
            String destination = "";
            ZipFile zipFile = new ZipFile(inputPath);
            
            @SuppressWarnings("unchecked")
            Enumeration entries = zipFile.entries();
            
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                
                destination = outputDir + entry.getName();
                
                if (entry.isDirectory()) {
                    // Assume directories are stored parents first then
                    // children.
                    System.out.println("Extracting directory: " + destination);
                    
                    tmpFile = new File(destination);
                    if (tmpFile.mkdir() == false) {
                        System.err.println("Could not create directory : "
                                + outputDir);
                        return false;
                    }
                } else {
                    // In some zip files the directories may not have been
                    // added, hence make the directory if it does not exist.
                    FileName outputPath = new FileName(destination);
                    if (FileName.dirExists(outputPath.getPath()) == false) {
                        tmpFile = new File(outputPath.getPath());
                        
                        // Make directory with parents
                        if (tmpFile.mkdirs() == true) {
                            System.out.println("Creating Directory: "
                                    + outputPath.getPath());
                        } else {
                            System.err.println("Could not create directory : "
                                    + outputPath.getPath());
                            return false;
                        }
                    }
                    
                    System.out.println("Extracting file: " + destination);
                    copyInputStream(zipFile.getInputStream(entry),
                            new BufferedOutputStream(new FileOutputStream(
                                    destination)));
                }
            }
            
            zipFile.close();
        } catch (IOException ioe) {
            return false;
        }
        
        return true;
    }
    
    public static final void copyInputStream(InputStream in, OutputStream out)
    throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        
        in.close();
        out.close();
    }
    
    /**
     * Delete a directory
     * 
     * @param path
     *            directory to delete
     * @return true on success otherwise false
     */
    static public boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
    
    /**
     * Returns the SimQuestViewer�s application
     * 
     * @param
     * @return Application
     */
    public Application getApplication() {
        return g_application;
    }
    
    /**
     * Returns the SimQuestViewer�s dataserver
     * 
     * @param
     * @return DataServer
     */
    public DataServer getDataServer() {
        return g_dataServer;
    }
    
    @Override
    public Frame getMainFrame() {
        // TODO Auto-generated method stub
        return m_mainframe;
    }
}
