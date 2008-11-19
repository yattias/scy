package eu.scy.tools.simquestviewer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import roolo.api.IRepository;
import roolo.api.search.ISearchResult;
import roolo.elo.JDomBasicELOFactory;
import roolo.elo.JDomStringConversion;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.RooloMetadataKeys;
import roolo.elo.content.DataSetContent;
import roolo.elo.content.dataset.DataSet;
import roolo.elo.content.dataset.DataSetColumn;
import roolo.elo.content.dataset.DataSetHeader;
import roolo.elo.content.dataset.DataSetRow;
import sqv.ISimQuestViewer;
import sqv.ModelVariable;
import sqv.data.IDataClient;
import eu.scy.toolbroker.ToolBrokerImpl;

/**
 * This class collects datapoints from a SimQuest simulation and stores them as
 * an ELO.
 * 
 * @author Lars Bollen
 * 
 */
public class DataCollector extends JPanel implements ActionListener,
IDataClient {
    
    private static final long serialVersionUID = -2306183502112904729L;
    private ISimQuestViewer simquestViewer;
    private JTextArea text = new JTextArea(5, 20);
    private ToolBrokerImpl<IMetadataKey> toolBroker;
    private JDomBasicELOFactory eloFactory;
    private IMetadataTypeManager metadataTypeManager;
    private SCYDataAgent dataAgent;
    private List<ModelVariable> simulationVariables;
    private List<ModelVariable> selectedVariables;
    private JCheckBox checkbox;
    private DataSet dataset;
    private Object eloTitle = "an unnamed SimQuest dataset";
    private IRepository<IELO<IMetadataKey>, IMetadataKey> repository;
    
    public DataCollector(ISimQuestViewer simquestViewer) {
        // setting some often-used variable
        this.simquestViewer = simquestViewer;
        simulationVariables = simquestViewer.getDataServer().getVariables(
        "name is not relevant");
        setSelectedVariables(simquestViewer.getDataServer().getVariables(
        "name is not relevant"));
        
        // register agent
        dataAgent = new SCYDataAgent(this, simquestViewer.getDataServer());
        dataAgent.add(simquestViewer.getDataServer().getVariables(
        "name is not relevant"));
        simquestViewer.getDataServer().register(dataAgent);
        
        // initialize user interface
        initGUI();
    }
    
    private void initGUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory
                .createTitledBorder("SCY Dataset Collector"));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JButton button = new JButton("select relevant variables");
        button.setActionCommand("configure");
        button.addActionListener(this);
        buttonPanel.add(button);
        
        button = new JButton("add current datapoint");
        button.setActionCommand("adddata");
        button.addActionListener(this);
        buttonPanel.add(button);
        
        button = new JButton("save ELO");
        button.setActionCommand("saveelo");
        button.addActionListener(this);
        buttonPanel.add(button);
        
        checkbox = new JCheckBox("add datapoints continuosly");
        checkbox.setSelected(false);
        buttonPanel.add(checkbox);
        
        button = new JButton("clear data");
        button.setActionCommand("cleardata");
        button.addActionListener(this);
        buttonPanel.add(button);
        
        this.add(buttonPanel, BorderLayout.NORTH);
        
        JScrollPane pane = new JScrollPane(text);
        this.add(pane, BorderLayout.CENTER);
    }
    
    public void addCurrentDatapoint() {
        ModelVariable var;
        List<String> values = new LinkedList<String>();
        for (Iterator<ModelVariable> vars = selectedVariables.iterator(); vars
        .hasNext();) {
            var = vars.next();
            values.add(var.getValueString());
            text.append(var.getExternalName() + ":" + var.getValueString()
                    + " / ");
        }
        text.append("\n");
        dataset.addRow(new DataSetRow(values));
    }
    
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("adddata")) {
            addCurrentDatapoint();
        } else if (evt.getActionCommand().equals("configure")) {
            VariableSelectionDialog dialog = new VariableSelectionDialog(
                    simquestViewer.getMainFrame(), this);
            dialog.setVisible(true);
        } else if (evt.getActionCommand().equals("saveelo")) {
            
            
            
            // setup tool-broker-api
            configureSCYConnection();
            
            // some debug outputs
            System.out.println("DataCollector.actionPerformed(). toolBroker: "
                    + toolBroker + "\n");
            List<ISearchResult> searchResult = toolBroker.getRepository()
            .search(null);
            System.out
            .println("DataCollector.actionPerformed(). search results:\n");
            for (ISearchResult result : searchResult) {
                System.out.println(result.getUri().toString() + "\n");
            }
            
            // ask for the title and possibly cancel here
            eloTitle = JOptionPane.showInputDialog(simquestViewer.getMainFrame(), "What would be the ELO title?","...");
            if (eloTitle == null) {
                // user pressed "cancel", stop here
                return;
            }
            
            // create the ELO
            IELO<IMetadataKey> elo = createELO();
            
            // store the ELO
            IMetadata<IMetadataKey> resultMetadata = toolBroker.getRepository().addELO(elo);
            // eloFactory.updateELOWithResult(elo, resultMetadata);
        } else if (evt.getActionCommand().equals("cleardata")) {
            dataset.removeAll();
            text.setText("");
        }
    }
    
    private void configureSCYConnection() {
        toolBroker = new ToolBrokerImpl<IMetadataKey>();
        metadataTypeManager = toolBroker.getMetaDataTypeManager();
        eloFactory = new JDomBasicELOFactory(metadataTypeManager);
        JDomBasicELOFactory<IMetadataKey> eloFactory = new JDomBasicELOFactory<IMetadataKey>(
                metadataTypeManager);
        repository = toolBroker
        .getRepository();
    }
    
    private IELO<IMetadataKey> createELO() {
        IELO<IMetadataKey> elo = eloFactory.createELO();
        fillMetadata(elo);
        elo.setContent(createContent());
        System.out.println(elo.getXml().toString());
        // updateEloWithNewMetadata(elo, eloMetadata);
        // logger.fine("metadata xml: \n" + elo.getMetadata().getXml());
        return elo;
    }
    
    private IContent createContent() {
        IContent content = new DataSetContent();
        content.setXml(new JDomStringConversion().xmlToString(dataset.toXML()));
        content.setLanguages(dataset.getLanguages());
        return content;
    }
    
    private void fillMetadata(IELO<IMetadataKey> elo) {
        // TODO
        // setting some metadata, not complete yet
        
        // setting the default language
        elo.setDefaultLanguage(Locale.ENGLISH);
        
        // key: title
        // the title of the ELO, should be selected by the learner
        IMetadataKey titleKey = metadataTypeManager
        .getMetadataKey(RooloMetadataKeys.TITLE.getId());
        elo.getMetadata().getMetadataValueContainer(titleKey).setValue(eloTitle );
        elo.getMetadata().getMetadataValueContainer(titleKey).setValue(
                eloTitle, Locale.ENGLISH);
        
        // key: format
        // the thechnical format / media type
        IMetadataKey typeKey = metadataTypeManager
        .getMetadataKey(RooloMetadataKeys.TYPE.getId());
        elo.getMetadata().getMetadataValueContainer(typeKey).setValue(
        "text/xml");
        
        // key: contribute_date
        // the creation date/time of this ELO
        IMetadataKey dateCreatedKey = metadataTypeManager
        .getMetadataKey(RooloMetadataKeys.DATE_CREATED.getId());
        elo.getMetadata().getMetadataValueContainer(dateCreatedKey).setValue(
                new Long(System.currentTimeMillis()));
        
        
        // try
        // {
        // // elo.getMetadata().getMetadataValueContainer(missionKey).setValue(
        // // new URI("roolo://somewhere/myMission.mission"));
        // // elo.getMetadata().getMetadataValueContainer(authorKey).setValue(
        // // new Contribute("my vcard", System.currentTimeMillis()));
        // }
        // catch (URISyntaxException e)
        // {
        // System.out.println(e.toString());
        // }
        
    }
    
    @Override
    public void updateClient() {
        if (checkbox.isSelected()) {
            addCurrentDatapoint();
        }
    }
    
    public List<ModelVariable> getSimulationVariables() {
        return simulationVariables;
    }
    
    public List<ModelVariable> getSelectedVariables() {
        return selectedVariables;
    }
    
    public void setSelectedVariables(List<ModelVariable> selection) {
        selectedVariables = selection;
        ModelVariable var;
        List<DataSetColumn> datasetvariables = new LinkedList<DataSetColumn>();
        List<DataSetHeader> datasetheaders = new LinkedList<DataSetHeader>();
        for (Iterator<ModelVariable> vars = selectedVariables.iterator(); vars
        .hasNext();) {
            var = vars.next();
            datasetvariables.add(new DataSetColumn(var.getName(), var
                    .getExternalName(), "double"));
        }
        datasetheaders.add(new DataSetHeader(datasetvariables, Locale.ENGLISH));
        dataset = new DataSet(datasetheaders);
        // TODO is this check neccessary?
        if (text != null) {
            text.setText("");
        }
    }
    
}
