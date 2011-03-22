package eu.scy.agents.queryexpansion.importer.gui;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.swat.SWATClient;
import info.collide.swat.agent.io.IOService;
import info.collide.swat.model.Class;
import info.collide.swat.model.DatatypeAnnotation;
import info.collide.swat.model.Entity;
import info.collide.swat.model.ID;
import info.collide.swat.model.OWLType;
import info.collide.swat.model.SWATException;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import eu.scy.agents.queryexpansion.importer.KeywordImporter;

public class KeywordImporterGUI extends JFrame implements ActionListener, ListSelectionListener, TableModelListener {

    private static final String TITLE = "Onto Keyword Importer";

    private JButton btLink;

    private JButton btUnlink;

    private JButton btNextSuggestion;

    private JMenuItem miConnect;

    private JMenuItem miSave;

    private JMenuItem miLoad;

    private JMenuItem ontoImport;
    
    private JMenuItem ontoExport;
    
    private JMenuItem miQuit;

    private JTable tbClouds;

    private JTable tbKeywords;

    private JButton btPlusKeywords;

    private JButton btMinusKeywords;

    private JButton btPlusClouds;

    private JButton btMinusClouds;

    private SWATClient sc;
    
    private JTree trOntology;

    private KeywordImporter keywordImporter;

    private JList lsCloudLinks;

    private JRadioButtonMenuItem miEdit;

    private JRadioButtonMenuItem miGraph;

    private CardLayout cards;

    private GraphPanel graphCard;

    private String language = "en";

    private String lastDir = ".";

    public static void main(String[] args) {
        LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
        for (LookAndFeelInfo laf : installedLookAndFeels) {
            if (laf.getName().toLowerCase().contains("nimbus")) {
                try {
                    UIManager.setLookAndFeel(laf.getClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
            }
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new KeywordImporterGUI();
            }
        });
    }

    public KeywordImporterGUI() {
        super(TITLE + " - Edit Mode");
        cards = new CardLayout();
        setLayout(cards);

        JMenuBar menuBar = new JMenuBar();
        JMenu mnFile = new JMenu("File");
        miConnect = new JMenuItem("Connect to Ontology");
        miLoad = new JMenuItem("Load Term Files");
        miSave = new JMenuItem("Save Term Files");
        ontoImport = new JMenuItem("Import Ontology file");
        ontoExport = new JMenuItem("Export Ontology file");
        miQuit = new JMenuItem("Quit");
        miConnect.addActionListener(this);
        ontoImport.addActionListener(this);
        ontoExport.addActionListener(this);
        miLoad.addActionListener(this);
        miSave.addActionListener(this);
        miQuit.addActionListener(this);
        mnFile.add(miConnect);
        mnFile.add(ontoImport);
        mnFile.add(ontoExport);
        mnFile.add(miLoad);
        mnFile.add(miSave);
        mnFile.addSeparator();
        mnFile.add(miQuit);
        menuBar.add(mnFile);

        JMenu mnView = new JMenu("View");
        miEdit = new JRadioButtonMenuItem("Edit Mode", true);
        miGraph = new JRadioButtonMenuItem("Graph Mode");
        ButtonGroup bg = new ButtonGroup();
        bg.add(miEdit);
        bg.add(miGraph);
        miEdit.addActionListener(this);
        miGraph.addActionListener(this);
        mnView.add(miEdit);
        mnView.add(miGraph);

        menuBar.add(mnView);

        setJMenuBar(menuBar);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);

        // Tree-Display left
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Not connected yet");
        trOntology = new JTree(top);

        ImageIcon leafIcon = new ImageIcon(KeywordImporterGUI.class.getResource("/instanceicon.gif"));
        ImageIcon nodeIcon = new ImageIcon(KeywordImporterGUI.class.getResource("/classicon.gif"));
        if (leafIcon != null) {
            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
            renderer.setLeafIcon(leafIcon);
            renderer.setOpenIcon(nodeIcon);
            renderer.setClosedIcon(nodeIcon);
            trOntology.setCellRenderer(renderer);
        }

        JScrollPane treeView = new JScrollPane(trOntology);
        splitPane.add(treeView);

        // Lists right
        JPanel pnLists = new JPanel(new GridLayout(1, 2, 5, 5));
        tbClouds = new JTable(0, 1);
        tbKeywords = new JTable(0, 1);
        lsCloudLinks = new JList(new DefaultListModel());
        lsCloudLinks.setVisibleRowCount(4);
        lsCloudLinks.setCellRenderer(new SWATListCellRenderer(nodeIcon, leafIcon));
        tbClouds.setTableHeader(null);
        tbKeywords.setTableHeader(null);
        tbClouds.setShowGrid(false);
        tbKeywords.setShowGrid(false);
        tbClouds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbKeywords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbClouds.getModel().addTableModelListener(this);
        tbKeywords.getModel().addTableModelListener(this);
        tbClouds.getColumnModel().getSelectionModel().addListSelectionListener(this);
        tbClouds.getSelectionModel().addListSelectionListener(this);
        JScrollPane spClouds = new JScrollPane(tbClouds);
        JScrollPane spCloudLinks = new JScrollPane(lsCloudLinks);
        JScrollPane spKeywords = new JScrollPane(tbKeywords);
        btMinusClouds = new JButton("-");
        btPlusClouds = new JButton("+");
        btMinusKeywords = new JButton("-");
        btPlusKeywords = new JButton("+");
        btMinusClouds.setToolTipText("Remove cloud");
        btPlusClouds.setToolTipText("Add new cloud");
        btMinusKeywords.setToolTipText("Remove keyword");
        btPlusKeywords.setToolTipText("Add new keyword");
        btMinusClouds.addActionListener(this);
        btPlusClouds.addActionListener(this);
        btMinusKeywords.addActionListener(this);
        btPlusKeywords.addActionListener(this);
        JPanel pnKeywords = new JPanel(new BorderLayout(5, 5));
        JPanel pnClouds = new JPanel(new BorderLayout(5, 5));
        JPanel pnCloudList = new JPanel(new BorderLayout(5, 5));
        JPanel pnCloudLinks = new JPanel(new BorderLayout(5, 5));
        JPanel pnKeywordButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JPanel pnCloudButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        pnCloudLinks.add(new JLabel("Connections"), BorderLayout.NORTH);
        pnCloudLinks.add(spCloudLinks, BorderLayout.CENTER);
        pnKeywordButtons.add(btPlusKeywords);
        pnKeywordButtons.add(btMinusKeywords);
        pnCloudButtons.add(btPlusClouds);
        pnCloudButtons.add(btMinusClouds);
        pnKeywords.add(spKeywords, BorderLayout.CENTER);
        pnKeywords.add(pnKeywordButtons, BorderLayout.SOUTH);
        pnCloudList.add(spClouds, BorderLayout.CENTER);
        pnCloudList.add(pnCloudButtons, BorderLayout.SOUTH);
        pnClouds.add(pnCloudList, BorderLayout.CENTER);
        pnClouds.add(pnCloudLinks, BorderLayout.SOUTH);
        pnClouds.setBorder(BorderFactory.createTitledBorder("Clouds"));
        pnKeywords.setBorder(BorderFactory.createTitledBorder("Keywords"));
        pnLists.add(pnClouds);
        pnLists.add(pnKeywords);
        splitPane.add(pnLists);

        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btLink = new JButton("Create Link");
        btLink.addActionListener(this);
        pnButtons.add(btLink);
        btUnlink = new JButton("Remove Link");
        btUnlink.addActionListener(this);
        pnButtons.add(btUnlink);
        btNextSuggestion = new JButton("Suggest");
        btNextSuggestion.addActionListener(this);
        pnButtons.add(btNextSuggestion);

        JPanel editCard = new JPanel(new BorderLayout(0, 0));
        editCard.add(splitPane, BorderLayout.CENTER);
        editCard.add(pnButtons, BorderLayout.SOUTH);

        graphCard = new GraphPanel();

        add(editCard, miEdit.getActionCommand());
        add(graphCard, miGraph.getActionCommand());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            public void run() {
                try {
                    if (sc != null) {
                        sc.finishSession();
                    }
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }
            }
        }));
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btLink)) {
            doLinking();
        } else if (e.getSource().equals(miConnect)) {
            connect();
        } else if (e.getSource().equals(ontoImport)) {
            importOntology();
        } else if (e.getSource().equals(ontoExport)) {
            exportOntology();
        } else if (e.getSource().equals(miLoad)) {
            load();
        } else if (e.getSource().equals(miSave)) {
            save();
        } else if (e.getSource().equals(miQuit)) {
            System.exit(0);
        } else if (e.getSource().equals(btNextSuggestion)) {
            nextSuggestion();
        } else if (e.getSource().equals(btUnlink)) {
            if (!preconditionsFulfilled()) {
                return;
            }
            // TODO Verbindung entfernen und Graph neu laden
        } else if (e.getSource().equals(btMinusClouds)) {
            if (keywordImporter == null || tbClouds.getSelectedRowCount() == 0) {
                return;
            }
            int index = tbClouds.getSelectedRow();
            String cloudName = (String) tbClouds.getValueAt(tbClouds.getSelectedRow(), 0);
            int cloud = keywordImporter.getLabelsMap().get(cloudName);
            keywordImporter.getKeywordMap().remove(cloud);
            keywordImporter.getLabelsMap().remove(cloudName);
            updateClouds();
            tbClouds.getSelectionModel().setSelectionInterval(index, index);
            updateConnections();
            updateKeywords();
        } else if (e.getSource().equals(btPlusClouds)) {
            if (keywordImporter == null) {
                return;
            }
            String newCloud = JOptionPane.showInputDialog(this, "Please insert the name of the new cloud!");
            if (newCloud != null) {
                int max = 0;
                for (Integer i : keywordImporter.getLabelsMap().values()) {
                    max = Math.max(max, i);
                }
                keywordImporter.getLabelsMap().put(newCloud, max + 1);
                keywordImporter.getKeywordMap().put(max + 1, new HashSet<String>());
                updateClouds();
                for (int i = 0; i < keywordImporter.getLabelsMap().size(); i++) {
                    String rowContent = (String) tbClouds.getValueAt(i, 0);
                    if (rowContent.equals(newCloud)) {
                        tbClouds.getSelectionModel().setSelectionInterval(i, i);
                    }
                }
                updateConnections();
                updateKeywords();
            }

        } else if (e.getSource().equals(btMinusKeywords)) {
            if (keywordImporter == null || tbClouds.getSelectedRowCount() == 0 || tbKeywords.getSelectedRowCount() == 0) {
                return;
            }
            String cloudName = (String) tbClouds.getValueAt(tbClouds.getSelectedRow(), 0);
            int cloud = keywordImporter.getLabelsMap().get(cloudName);
            String keyword = (String) tbKeywords.getValueAt(tbKeywords.getSelectedRow(), 0);
            keywordImporter.getKeywordMap().get(cloud).remove(keyword);
            updateKeywords();
        } else if (e.getSource().equals(btPlusKeywords)) {
            if (keywordImporter == null || tbClouds.getSelectedRowCount() == 0) {
                return;
            }
            String cloudName = (String) tbClouds.getValueAt(tbClouds.getSelectedRow(), 0);
            int cloud = keywordImporter.getLabelsMap().get(cloudName);
            String newKeyword = JOptionPane.showInputDialog(this, "Please insert the name of the new keyword!");
            if (newKeyword != null) {
                keywordImporter.getKeywordMap().get(cloud).add(newKeyword);
                updateKeywords();
            }
        } else if (e.getSource().equals(miEdit) || e.getSource().equals(miGraph)) {
            cards.show(getContentPane(), e.getActionCommand());
            setTitle(TITLE + " - " + e.getActionCommand());
        }
    }
    
    private void load() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(lastDir));
        fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        fc.setDialogTitle("Choose Keyword File");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File keywordsFile = fc.getSelectedFile();
            lastDir = keywordsFile.getParent() + "/.";
            fc = new JFileChooser();
            fc.setSelectedFile(new File(lastDir));
            fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            fc.setDialogTitle("Choose Labels File");
            returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File labelsFile = fc.getSelectedFile();
                lastDir = labelsFile.getParent() + "/.";
                keywordImporter = new KeywordImporter();
                try {
                    keywordImporter.parseFiles(keywordsFile, labelsFile);
                    if (sc != null) {
                        keywordImporter.prepareOntology(sc);
                        HashSet<ID> ignoredEntities = new HashSet<ID>();
                        ignoredEntities.add(keywordImporter.getKeywordConcept().getId());
                        ignoredEntities.add(keywordImporter.getKeywordListConcept().getId());
                        HashSet<ID> displayedProperties = new HashSet<ID>();
                        displayedProperties.add(keywordImporter.getIsConnectedTo().getId());
                        displayedProperties.add(keywordImporter.getBelongsTo().getId());
                        HashSet<ID> nodesToFold = new HashSet<ID>();
                        graphCard.setSWATClient(sc, ignoredEntities, displayedProperties, nodesToFold);
                    }
                    updateClouds();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(this, "The following error occured while parsing the files:\n" + e1.getMessage());
                    e1.printStackTrace();
                } catch (SWATException e2) {
                    JOptionPane.showMessageDialog(this, "The following error occured while parsing the files:\n" + e2.getMessage());
                    e2.printStackTrace();
                }
            }
        }
    }

    private void save() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(lastDir));
        fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        fc.setDialogTitle("Save Keyword File");
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File keywordsFile = fc.getSelectedFile();
            lastDir = keywordsFile.getParent() + "/.";
            fc = new JFileChooser();
            fc.setSelectedFile(new File(lastDir));
            fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            fc.setDialogTitle("Save Labels File");
            returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File labelsFile = fc.getSelectedFile();
                lastDir = labelsFile.getParent() + "/.";
                try {
                    keywordImporter.saveFiles(keywordsFile, labelsFile);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "The following error occured while saving the files:\n" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void connect() {
    	SpaceNameSelector sns = new AvailableOntologySelector("Verify", "http://");
        SQLSpacesConnectionDialog connDialog = new SQLSpacesConnectionDialog(this, false, false, User.getDefaultUser(), sns);
        if (connDialog.wasCanceled()) {
            return;
        }
        connectToTS(connDialog.getHost(), connDialog.getPort(), connDialog.getUser(), connDialog.getSpace());
    }
    
    private void connectToTS(String host, int port, User user, String space) {
    	try {
    		sc = new SWATClient(space, host, port, OWLType.OWL_DL, user, false);
    		Class[] rootClasses = sc.getOntology().listHierarchyRootClasses(true);
    		trOntology.setModel(new SWATTreeModel(rootClasses, sc));
    		if (keywordImporter != null) {
    			keywordImporter.prepareOntology(sc);
    			HashSet<ID> ignoredEntities = new HashSet<ID>();
    			ignoredEntities.add(keywordImporter.getKeywordConcept().getId());
    			ignoredEntities.add(keywordImporter.getKeywordListConcept().getId());
    			HashSet<ID> displayedProperties = new HashSet<ID>();
    			displayedProperties.add(keywordImporter.getIsConnectedTo().getId());
    			displayedProperties.add(keywordImporter.getBelongsTo().getId());
    			HashSet<ID> nodesToFold = new HashSet<ID>();
    			graphCard.setSWATClient(sc, ignoredEntities, displayedProperties, nodesToFold);
    		}
    	} catch (TupleSpaceException e1) {
    		JOptionPane.showMessageDialog(this, "The following error occured while parsing the files:\n" + e1.getMessage());
    		e1.printStackTrace();
    	} catch (SWATException e2) {
    		JOptionPane.showMessageDialog(this, "The following error occured while parsing the files:\n" + e2.getMessage());
    		e2.printStackTrace();
    	}
    }

    private void importOntology() {
    	try
    	{
    		JFileChooser fc = new JFileChooser();
    		fc.setFileFilter(new FileNameExtensionFilter("OWL Ontology files", "owl"));
    		fc.setDialogTitle("Choose Ontology");
    		int returnVal = fc.showOpenDialog(this);
    		if (returnVal != JFileChooser.APPROVE_OPTION) {
    			return;
    		}
    		String fileNameSpace = readSpaceNameFromFile(fc.getSelectedFile()); 

    		// get the TS
    		SpaceNameSelector sns = new OntologyImportSelector(this, "Verify", fileNameSpace);
    		SQLSpacesConnectionDialog connDialog = new SQLSpacesConnectionDialog(this, false, false, User.getDefaultUser(), sns);
    		if (connDialog.wasCanceled()) {
    			return;
    		}

    		// read the file
    		FileInputStream fis = new FileInputStream(fc.getSelectedFile());
    		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String buffer;
			StringBuffer sb = new StringBuffer();
			while ((buffer = br.readLine()) != null) {
				sb.append(buffer);
			}
			br.close();

			// Delete old tuples
            TupleSpace ontologySpace = new TupleSpace(connDialog.getHost(), connDialog.getPort(), fileNameSpace);
            ontologySpace.deleteAll(new Tuple());
            ontologySpace.disconnect();

			// Write to TS
            IOService ioService = new IOService(connDialog.getHost(), connDialog.getPort(), "bla");
            ioService.importOwl(sb.toString(), connDialog.getSpace(), "bla", null);
            
            TupleSpace commandSpace = new TupleSpace();
            commandSpace.takeAll(new Tuple());
            commandSpace.disconnect();
            
            // load the data
            connectToTS(connDialog.getHost(), connDialog.getPort(), connDialog.getUser(), connDialog.getSpace());
    	}
    	catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "The following error occurred:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);    		
    	}
    }
    
	private String readSpaceNameFromFile(File file) throws IOException {
		// scan the file for the xmlns entry
		String space = null;
		BufferedReader br = new BufferedReader(new FileReader(file));
		String buffer;
		while ((buffer = br.readLine()) != null) {
			int startIndex = buffer.indexOf("xmlns=");
			if (startIndex > -1) {
				int endIndex = buffer.indexOf('"', startIndex + 7);
				if (endIndex != -1) {
					space = buffer.substring(startIndex + 7, endIndex);
					break;
				} else {
					System.err
							.println("ODF file format is corrupted: xmlns entry has no ending quote sign");
					return null;
				}

			}
		}
		br.close();
		return space;
	}

    private void exportOntology() {
    	try
    	{
    		// get the TS
    		SpaceNameSelector sns = new OntologyExportSelector("Verify", "http://");
    		SQLSpacesConnectionDialog connDialog = new SQLSpacesConnectionDialog(this, false, false, User.getDefaultUser(), sns);
    		if (connDialog.wasCanceled()) {
    			return;
    		}

    		JFileChooser fc = new JFileChooser();
    		fc.setSelectedFile(new File(lastDir));
    		fc.setFileFilter(new FileNameExtensionFilter("OWL Ontology", "owl"));
    		fc.setDialogTitle("Save Ontology File");
    		int returnVal = fc.showSaveDialog(this);
    		if (returnVal != JFileChooser.APPROVE_OPTION) {
    			return;
    		}

    		TupleSpace ts = new TupleSpace(connDialog.getHost(), connDialog.getPort(), connDialog.getSpace());
    		IOService ioService = new IOService(connDialog.getHost(), connDialog.getPort(), "bla");
    		String buffer = ioService.exportTupleSpace(ts);

    		File owlFile = fc.getSelectedFile();
    		if(!fc.getSelectedFile().getName().endsWith(".owl")) {
    			owlFile = new File(fc.getSelectedFile().getAbsolutePath() + ".owl");
    		}
    		BufferedWriter bw = new BufferedWriter(new FileWriter(owlFile));
    		bw.write(buffer);
    		bw.close();
            JOptionPane.showMessageDialog(this, "The ontology has been successfully exported!", "Export successful", JOptionPane.INFORMATION_MESSAGE);    		
    	}
    	catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "The following error occurred:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);    		
    	}
    }

    private void doLinking() {
        if (!preconditionsFulfilled()) {
            return;
        }
        final String entityName = ((SWATTreeNode) trOntology.getSelectionPath().getLastPathComponent()).getSWATClass().getId().getFullId();
        final String cloudName = (String) tbClouds.getModel().getValueAt(tbClouds.getSelectedRow(), 0);
        DefaultTableModel keywordModel = (DefaultTableModel) tbKeywords.getModel();

        final String[] keywords = new String[keywordModel.getRowCount()];
        for (int i = 0; i < keywords.length; i++) {
            keywords[i] = (String) keywordModel.getValueAt(i, 0);
        }

        final ProgressDialog progressDialog = new ProgressDialog(this, "Please wait", "Linking the keywords to the ontology ...");
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    keywordImporter.importIntoOntology(sc, entityName, cloudName, keywords, progressDialog.getProgressBar());
                } catch (SWATException e) {
                    JOptionPane.showMessageDialog(KeywordImporterGUI.this, "The following error occured:\n" + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                updateConnections();
                graphCard.refresh();
                progressDialog.setVisible(false);
                progressDialog.dispose();
            }
        };
        t.start();
        progressDialog.setVisible(true);
    }

    private void nextSuggestion() {
        new Thread() {

            @Override
            public void run() {

                try {
                    HashSet<String> ontologyEntities = new HashSet<String>();

                    for (DatatypeAnnotation da : sc.getOntology().listLabels(language)) {
                        ontologyEntities.add(da.getValue());
                    }
                    Map<String, Integer> labelsMap = keywordImporter.getLabelsMap();
                    for (Entry<String, Integer> e : labelsMap.entrySet()) {
                        for (int i = 0; i < tbClouds.getModel().getRowCount(); i++) {
                            if (tbClouds.getModel().getValueAt(i, 0).equals(e.getKey())) {
                                final int x = i;
                                try {
                                    SwingUtilities.invokeAndWait(new Runnable() {

                                        public void run() {
                                            tbClouds.getSelectionModel().setSelectionInterval(x, x);
                                        }
                                    });
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                } catch (InvocationTargetException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                        if (ontologyEntities.contains(e.getKey())) {
                            System.out.println(e.getKey() + " -> " + e.getKey());
                            ExpandRunnable r = new ExpandRunnable(e.getKey(), -1);
                            try {
                                SwingUtilities.invokeAndWait(r);
                                if (r.getAnswer() == JOptionPane.YES_OPTION) {
                                    doLinking();
                                } else if (r.getAnswer() == JOptionPane.CANCEL_OPTION) {
                                    return;
                                }
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            } catch (InvocationTargetException e1) {
                                e1.printStackTrace();
                            }
                        }
                        for (final String keyword : keywordImporter.getKeywordMap().get(e.getValue())) {
                            if (ontologyEntities.contains(keyword)) {
                                System.out.println(e.getKey() + " -> " + keyword);
                                for (int i = 0; i < tbKeywords.getModel().getRowCount(); i++) {
                                    if (tbKeywords.getModel().getValueAt(i, 0).equals(keyword)) {
                                        try {
                                            ExpandRunnable r = new ExpandRunnable(keyword, i);
                                            SwingUtilities.invokeAndWait(r);
                                            if (r.getAnswer() == JOptionPane.YES_OPTION) {
                                                doLinking();
                                            } else if (r.getAnswer() == JOptionPane.CANCEL_OPTION) {
                                                return;
                                            }
                                        } catch (InterruptedException e1) {
                                            e1.printStackTrace();
                                        } catch (InvocationTargetException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (SWATException e1) {
                    e1.printStackTrace();
                }

            }

        }.start();
    }

    private boolean preconditionsFulfilled() {
        if (sc == null) {
            JOptionPane.showMessageDialog(this, "First you have to connect to an ontology!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (keywordImporter == null) {
            JOptionPane.showMessageDialog(this, "First you have to load the keyword files!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (trOntology.getSelectionCount() < 1 || tbClouds.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "First you have to select a cloud and an ontology node!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void updateKeywords() {
        int index = tbClouds.getSelectedRow();
        DefaultTableModel keywordModel = (DefaultTableModel) tbKeywords.getModel();
        while (keywordModel.getRowCount() > 0) {
            keywordModel.removeRow(0);
        }
        if (index != -1) {
            DefaultTableModel cloudsModel = (DefaultTableModel) tbClouds.getModel();
            String listEntry = (String) cloudsModel.getValueAt(index, 0);
            if (keywordImporter.getLabelsMap().containsKey(listEntry)) {
                int cloudNumber = keywordImporter.getLabelsMap().get(listEntry);
                for (String keyword : keywordImporter.getKeywordMap().get(cloudNumber)) {
                    keywordModel.addRow(new String[] { keyword });
                }
            }
        }
        updateConnections();
    }

    private void updateConnections() {
        if (sc != null) {
            DefaultListModel model = (DefaultListModel) lsCloudLinks.getModel();
            if (tbClouds.getSelectedRow() != -1) {
                try {
                    model.clear();
                    Object valueAt = tbClouds.getValueAt(tbClouds.getSelectedRow(), 0);
                    Entity[] connections = keywordImporter.getConnections(sc, sc.getOntology().getNameSpace() + valueAt.toString());
                    for (Entity e : connections) {
                        model.addElement(e);
                    }
                } catch (SWATException e) {
                    e.printStackTrace();
                }
            } else {
                model.clear();
            }
        }
    }

    private void updateClouds() {
        Map<String, Integer> labelMap = keywordImporter.getLabelsMap();
        DefaultTableModel cloudsModel = (DefaultTableModel) tbClouds.getModel();
        while (cloudsModel.getRowCount() > 0) {
            cloudsModel.removeRow(0);
        }
        for (String key : labelMap.keySet()) {
            cloudsModel.addRow(new String[] { key });
        }
        tbClouds.clearSelection();
    }

    public void valueChanged(ListSelectionEvent e) {
        updateKeywords();
    }

    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            if (e.getSource() == tbKeywords.getModel()) {
                int index = tbClouds.getSelectedRow();
                Set<String> keywords = keywordImporter.getKeywordMap().get(index);
                keywords.clear();
                DefaultTableModel keywordModel = (DefaultTableModel) tbKeywords.getModel();
                for (int i = 0; i < keywordModel.getRowCount(); i++) {
                    keywords.add(keywordModel.getValueAt(i, 0).toString());
                }
            } else if (e.getSource() == tbClouds.getModel()) {
                int index = tbClouds.getSelectedRow();
                DefaultTableModel model = (DefaultTableModel)tbClouds.getModel();
                String newString = model.getValueAt(index, 0).toString();

                // dirty, but necessary ...
                Map<String, Integer> m = keywordImporter.getLabelsMap();
                Set<String> oldClouds = new HashSet<String>(m.keySet());
                for (int i = 0; i < model.getRowCount(); i++) {
                    oldClouds.remove(model.getValueAt(i, 0));
                }
                String oldString = oldClouds.iterator().next();
                int internalId = m.get(oldString);
                
                m.remove(oldString);
                m.put(newString, internalId);
            }
        }
    }

    static class SWATListCellRenderer extends DefaultListCellRenderer {

        private Icon classIcon;

        private Icon instanceIcon;

        public SWATListCellRenderer(Icon classIcon, Icon instanceIcon) {
            this.classIcon = classIcon;
            this.instanceIcon = instanceIcon;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Entity e = (Entity) value;
            if (e instanceof Class) {
                label.setIcon(classIcon);
            } else {
                label.setIcon(instanceIcon);
            }
            label.setText(e.getId().getName());
            return label;

        }
    }

    class ExpandRunnable implements Runnable {

        private String keyword;
        
        private int selectedRow;

        public ExpandRunnable(String keyword, int selectedRow) {
            this.keyword = keyword;
            this.selectedRow = selectedRow;
        }
        
        private int answer;
        
        public void run() {
            expandToNode(keyword);
            if (selectedRow != -1) {
                tbKeywords.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
            }
            answer = JOptionPane.showConfirmDialog(KeywordImporterGUI.this, "Do you want to link this cloud to that ontology entity?", "Match found!", JOptionPane.YES_NO_CANCEL_OPTION);
        }

        public int getAnswer() {
            return answer;
        }
        
        private void expandToNode(String label) {
            SWATTreeModel model = (SWATTreeModel) trOntology.getModel();
            for (int row = trOntology.getRowCount() - 1; row >= 0; row--) {
                trOntology.collapseRow(row);
            }
            SWATTreeNode node = model.getNodeForLabel(label, language);
            trOntology.getSelectionModel().setSelectionPath(new TreePath(node.getPath()));
        };
        
    }

}
