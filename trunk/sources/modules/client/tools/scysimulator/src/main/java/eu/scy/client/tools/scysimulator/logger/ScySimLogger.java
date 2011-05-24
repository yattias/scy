package eu.scy.client.tools.scysimulator.logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import org.jdom.output.XMLOutputter;
import sqv.ModelVariable;
import sqv.data.BasicDataAgent;
import sqv.data.DataAgent;
import sqv.data.DataServer;
import sqv.data.IDataClient;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.DevNullActionLogger;
import eu.scy.actionlogging.SQLSpacesActionLogger;
import eu.scy.actionlogging.SystemOutActionLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import java.util.logging.Logger;

public class ScySimLogger implements ActionListener, IDataClient {

    private final static Logger debugLogger = Logger.getLogger(ScySimLogger.class.getName());

    private String username = "default_username";
    private String toolname = "simulator";
    private String missionname = "n/a";
    private String sessionname = "n/a";
    private String eloURI = "n/a";
    private DataServer dataServer;
    private ArrayList<Double> oldInputValues;
    private ArrayList<Double> oldOutputValues;
    private ArrayList<ModelVariable> inputVariables;
    private ArrayList<ModelVariable> outputVariables;
    private IAction lastInputVariableValueChangedAction;
    private IAction lastOutputVariableValueChangedAction;
    private Timer inputVariableTimer;
    private Timer outputVariableTimer;
    private IAction firstInputVariableValueChangedAction;
    private IAction firstOutputVariableValueChangedAction;
    private IActionLogger actionLogger;
    private XMLOutputter xmlOutputter;
    private static int COUNT = 0;
    public static final String VALUE_CHANGED = "value_changed";
    public static final String ROW_ADDED = "row_added";
    public static final String VARIABLES_CONTAINED = "variables_contained";
    public static final String VARIABLES_SELECTED = "variables_selected";

    public ScySimLogger(DataServer dataServer, IActionLogger logger, String eloURI) {
	COUNT++;
        this.dataServer = dataServer;
        this.eloURI = eloURI;
        xmlOutputter = new XMLOutputter();
        DataAgent dataAgent = new BasicDataAgent(this, dataServer);
        // find input and output variables
        inputVariables = getVariables(ModelVariable.VK_INPUT);
        outputVariables = getVariables(ModelVariable.VK_OUTPUT, "Mtot");

        // store the values to find the changed value later
        storeOldValues();
        // subscribe to variables
        dataAgent.add(inputVariables);
        dataAgent.add(outputVariables);
        // register

        dataAgent.register();
        inputVariableTimer = new Timer(500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IAction action = createBasicAction(VALUE_CHANGED);
                action.addAttribute("name", lastInputVariableValueChangedAction.getAttribute("name"));
                action.addAttribute("oldValue", stripValue(firstInputVariableValueChangedAction.getAttribute("oldValue")));
                action.addAttribute("newValue", stripValue(lastInputVariableValueChangedAction.getAttribute("newValue")));
                write(action);
            }
        });
        inputVariableTimer.setRepeats(false);
        outputVariableTimer = new Timer(500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stripValue(firstInputVariableValueChangedAction.getAttribute("oldValue"));
                stripValue(lastInputVariableValueChangedAction.getAttribute("newValue"));
                IAction action = createBasicAction("value_changed");
                action.addAttribute("name", lastOutputVariableValueChangedAction.getAttribute("name"));
                action.addAttribute("oldValue", stripValue(firstOutputVariableValueChangedAction.getAttribute("oldValue")));
                action.addAttribute("newValue", stripValue(lastOutputVariableValueChangedAction.getAttribute("newValue")));
                write(action);
            }
        });
        outputVariableTimer.setRepeats(false);
        actionLogger = logger;
    }

    public ScySimLogger(DataServer dataServer) {
        this(dataServer, new DevNullActionLogger(), "n/a");
    }

    public ArrayList<ModelVariable> getInputVariables() {
        return inputVariables;
    }

    public void setUsername(String name) {
	this.username = name;
    }

    public void setMissionname(String missionname) {
	this.missionname = missionname;
    }
    
    public String getEloURI() {
        return eloURI;
    }

    public void setEloURI(String eloURI) {
        this.eloURI = eloURI;
    }

    private ArrayList<ModelVariable> getVariables(int mv) {
        return getVariables(mv, null);
    }

    private ArrayList<ModelVariable> getVariables(int mv, String name) {
        ArrayList<ModelVariable> variables = new ArrayList<ModelVariable>();
        for (ModelVariable variable : dataServer.getVariables("")) {
            if (variable.getKind() == mv && (name == null || variable.getName().equals(name))) {
                variables.add(variable);
            }
        }
        return variables;
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void updateClient() {
        for (int i = 0; i < inputVariables.size(); i++) {
            Double d = inputVariables.get(i).getValue();
            if (!d.equals(oldInputValues.get(i))) {
                logValueChanged(inputVariables.get(i).getName(), oldInputValues.get(i), inputVariables.get(i).getValue());
            }
        }
        for (int i = 0; i < outputVariables.size(); i++) {
            Double d = outputVariables.get(i).getValue();
            if (!d.equals(oldOutputValues.get(i))) {
                logValueChanged(outputVariables.get(i).getName(), oldOutputValues.get(i), outputVariables.get(i).getValue());
            }
        }
        storeOldValues();
    }

    private void logValueChanged(String name, double oldValue, double newValue) {
        IAction action = createBasicAction(VALUE_CHANGED);
        action.addAttribute("name", name);
        action.addAttribute("oldValue", stripValue(oldValue + ""));
        action.addAttribute("newValue", stripValue(newValue + ""));
        for (ModelVariable inVar : inputVariables) {
            if (inVar.getName().equals(name)) {
                if (!inputVariableTimer.isRunning()) {
                    firstInputVariableValueChangedAction = action;
                }
                lastInputVariableValueChangedAction = action;
                inputVariableTimer.restart();
                break;
            }

        }
        for (ModelVariable outVar : outputVariables) {
            if (outVar.getName().equals(name)) {
                if (!outputVariableTimer.isRunning()) {
                    firstOutputVariableValueChangedAction = action;
                }
                lastOutputVariableValueChangedAction = action;
                outputVariableTimer.restart();
                break;
            }
        }
    }

    private void storeOldValues() {
        oldInputValues = new ArrayList<Double>();
        oldOutputValues = new ArrayList<Double>();
        for (ModelVariable invar : inputVariables) {
            oldInputValues.add(invar.getValue());
        }
        for (ModelVariable outvar : outputVariables) {
            oldOutputValues.add(outvar.getValue());
        }
    }

    public IAction createBasicAction(String type) {
	IAction action = new Action();
        action.setUser(username);
        action.setType(type);
        action.addContext(ContextConstants.tool, toolname);
        action.addContext(ContextConstants.mission, missionname);
        action.addContext(ContextConstants.session, sessionname);
        action.addContext(ContextConstants.eloURI, eloURI);
        return action;
    }

    public void logAddRow(DataSetRow newRow) {
        IAction action = createBasicAction(ROW_ADDED);
        action.addAttribute("dataRow", xmlOutputter.outputString(newRow.toXML()));
        write(action);
    }

    public void logListOfVariables(String actionType, List<ModelVariable> selectedVariables) {
        IAction action = createBasicAction(actionType);
        String selection = new String();
        for (ModelVariable var : selectedVariables) {
            selection = selection + var.getName() + ", ";
        }
        if (selection.length() > 2) {
            selection = selection.substring(0, selection.length() - 2);
        }
        action.addAttribute("variables", selection);
        write(action);
    }

    private synchronized void write(IAction action) {
        //TODO Dirty workaround for toronto
        if (action.getType() != null && action.getType().equals(VALUE_CHANGED) && action.getAttribute("name") != null && action.getAttribute("name").trim().toLowerCase().equals("mtot")) {
            // no evil mtot logging ...
        } else {
	    debugLogger.info("logging action: "+action.toString());
            actionLogger.log(action);
        }
    }

    public String getToolName() {
        return toolname;
    }

    public String getUserName() {
        return username;
    }

    private String stripValue(String value) {
        // if 0000 or 9999 is detected, round to a meaningful value
        if (value.contains("0000")) {
            value = value.substring(0, value.indexOf("0000"));
        } else if (value.contains("9999")) {
            int pot = value.indexOf("9999") + 1 - value.indexOf(".");
            if (pot > 0) {
                Double newValue = new Double(value) * 10 * pot;
                newValue = java.lang.Math.ceil(newValue) / (10 * pot);
                value = newValue.toString();
            }
        }
        return value;
    }
}
