package eu.scy.client.tools.scydynamics.editor;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import eu.scy.client.tools.scydynamics.model.SimquestModelQuantitative;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;


import sqv.Interface;
import sqv.ModelVariable;
import sqv.data.DataServer;
import sqv.data.ScreenOutputDataClient;
import sqv.widgets.Curve;
import sqv.widgets.GraphWidget;
import sqv.widgets.VariableRef;

public class TableTab extends JPanel implements ChangeListener, ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = 2337114562359454914L;
    private ModelEditor editor;
    private VariableSelectionPanel variablePanel;
    private JPanel tablePanel;
    private DataServer dataServer;
    private SimulationSettingsPanel simulationPanel;
    private SimquestModelQuantitative sqModel;
    private JTable table;
    private SimulationTableModel tableModel;
    private JScrollPane scrollPane;
    private final ResourceBundleWrapper bundle;

    public TableTab(ModelEditor editor, ResourceBundleWrapper bundle) {
        super();
        this.editor = editor;
        this.bundle = bundle;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout());
        westPanel.add(variablePanel = new VariableSelectionPanel(editor, bundle, true), BorderLayout.NORTH);
        westPanel.add(simulationPanel = new SimulationSettingsPanel(editor, this), BorderLayout.CENTER);
        JScrollPane scroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setViewportView(westPanel);
		this.add(scroller, BorderLayout.WEST);
        this.add(createTable(), BorderLayout.CENTER);
    }

    private JPanel createTable() {
        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("PANEL_TABLE")));

        table = new JTable();
        scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        dataServer = new DataServer();
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        variablePanel.updateVariables();
        simulationPanel.updateSettings();
        editor.getActionLogger().logActivateWindow("table", null, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("run")) {
        	editor.checkModel();
			// can the model be parsed?
			if (editor.getModelCheckMessages().size() > 0) {
				String messages = new String(
						bundle.getString("PANEL_CANNOTEXECUTE") + "\n");
				for (String msg : editor.getModelCheckMessages()) {
					messages = messages + msg + "\n";
				}
				JOptionPane.showMessageDialog(null, messages);
				return;
			}
			// can the variable values be parsed?
			try {
				variablePanel.getValues();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null,"The variable values cannot be parsed correctly.\n Please check and try again.");
				return;
			}
			// can the simulation settings be parsed?
			try {
				injectSimulationSettings();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null,
						bundle.getString("PANEL_CANNOTPARSE"));
				return;
			}
            // create the SimQuest model from the CoLab model
            sqModel = new SimquestModelQuantitative(editor.getModel(), variablePanel.getValues());
            sqv.Model model = new sqv.Model(sqModel, dataServer);

            // building the tablemodel
            ArrayList<ModelVariable> selectedVariables = new ArrayList<ModelVariable>();
            ModelVariable time = new ModelVariable();
            for (ModelVariable var : model.getVariables()) {
                // getting a reference to the time variable
                if (variablePanel.getSelectedVariables().contains(var.getName())) {
                    selectedVariables.add(var);
                }
            }

            tableModel = null;
            tablePanel.remove(scrollPane);
            tableModel = new SimulationTableModel(selectedVariables, dataServer);
            table = new JTable(tableModel);
            table.setFillsViewportHeight(true);
            scrollPane = new JScrollPane(table);
            tablePanel.add(scrollPane, BorderLayout.CENTER);

            String variableIdList = new String();
            for (String varname : variablePanel.getSelectedVariables()) {
            	if (varname.equals("time")) {
            		variableIdList = variableIdList.concat("time, ");
            	} else {
            		variableIdList = variableIdList.concat(editor.getModel().getObjectOfName(varname).getID() + ", ");
            	}
            }

            if (variableIdList.length() > 0) {
                // simulate
                model.getSimulation().Simulate();
                // tune table
                tableModel.deleteFirstAndLast();
                tablePanel.updateUI();
                // log
		String injectedVariables = "";
		for (String varName: variablePanel.getValues().keySet()) {
		    injectedVariables = injectedVariables + editor.getModel().getObjectOfName(varName).getID() + "=" + variablePanel.getValues().get(varName)+"; ";
		}
		editor.getActionLogger().logModelRan(editor.getXmModel().getXML("", true), injectedVariables.substring(0, injectedVariables.length()-2));
                editor.getActionLogger().logInspectVariablesAction(ModellingLogger.TABLE_VIEWED, variableIdList.substring(0, variableIdList.length()-2));
            } else {
                JOptionPane.showMessageDialog(null, bundle.getString("PANEL_SELECTVARIABLE"));
            }
        } else if (e.getActionCommand().equals("export")) {
            export();
        }
    }

    private void export() {
        editor.checkModel();
        if (editor.getModelCheckMessages().size() > 0) {
            String messages = new String(bundle.getString("PANEL_CANNOTEXECUTE"));
            for (String msg : editor.getModelCheckMessages()) {
                messages = messages + msg + "\n";
            }
            JOptionPane.showMessageDialog(null, messages);
            return;
        }
        try {
            injectSimulationSettings();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, bundle.getString("PANEL_CANNOTPARSE"));
            return;
        }
        // create the SimQuest model from the CoLab model
        sqModel = new SimquestModelQuantitative(editor.getModel());
        sqv.Model model = new sqv.Model(sqModel, dataServer);
        FileDialog dialog = new FileDialog((Frame) editor.getRootPane().getParent(), bundle.getString("PANEL_SAVESQX"), FileDialog.SAVE);
        dialog.setFile("*.sqx");
        dialog.setVisible(true);
        if (dialog.getFile() != null) {
            Element application = new Element("application");
            Element topics = new Element("topics");
            Element topic = new Element("topic");
            topic.setAttribute("id", java.util.UUID.randomUUID().toString());
            topic.addContent(sqModel);
            topics.addContent(topic);
            application.addContent(topics);

            Document doc = new Document(application);

            try {
                XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
                FileWriter writer = new FileWriter(dialog.getDirectory() + dialog.getFile());
                out.output(doc, writer);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void injectSimulationSettings() throws NumberFormatException {
        editor.getModel().setStart(Double.parseDouble(simulationPanel.getStart()));
        editor.getModel().setStop(Double.parseDouble(simulationPanel.getStop()));
        editor.getModel().setStep(Double.parseDouble(simulationPanel.getStep()));
        editor.getModel().setMethod(simulationPanel.getMethod());
    }

	public DataSet getDataSet() {
		ArrayList<DataSetColumn> columns = new ArrayList<DataSetColumn>();
		for (String varName: variablePanel.getSelectedVariables()) {
			columns.add(new DataSetColumn(varName, "", "double"));
		}
		ArrayList<DataSetHeader> headers = new ArrayList<DataSetHeader>();
		headers.add(new DataSetHeader(columns, Locale.ENGLISH));
		DataSet dataset = new DataSet(headers);
		DataSetRow datarow;
		if (tableModel != null) {
			for (int row=0; row<tableModel.getRowCount(); row++) {
				ArrayList<String> values = new ArrayList<String>();
				for (int col=0; col<tableModel.getColumnCount(); col++) {
					values.add(tableModel.getValueAt(row, col).toString());
				}
				datarow = new DataSetRow(values);
				dataset.addRow(datarow);
			}
		}
		return dataset;
	}
}
