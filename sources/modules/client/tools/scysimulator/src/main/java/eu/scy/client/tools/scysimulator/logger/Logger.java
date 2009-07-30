package eu.scy.client.tools.scysimulator.logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import sqv.ModelVariable;
import sqv.data.BasicDataAgent;
import sqv.data.DataAgent;
import sqv.data.DataServer;
import sqv.data.IDataClient;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.logger.Action;
import eu.scy.elo.contenttype.dataset.DataSetRow;

public class Logger implements ActionListener, IDataClient {

	private String username;
	private String toolname;
	private String missionname;
	private XMLOutputter outputter;
	private DataServer dataServer;
	private ArrayList<Double> oldValues;
	private ArrayList<ModelVariable> variables;
	private IAction action;
    private IAction lastValueChangedAction;
    private Timer timer;
    private IAction firstValueChangedAction;
	
	public Logger(DataServer dataServer) {
		this.dataServer = dataServer;
		//TODO: these properties have to be fetched from the SCY environment
		username = System.getProperty("user.name");
		missionname = "mission 1";
		toolname = "scysimulator";
		outputter = new XMLOutputter(Format.getPrettyFormat());
		DataAgent dataAgent = new BasicDataAgent(this, dataServer);
		// find input variables
		variables = getInputVariables();
		// store the values to find the changed value later
		storeOldValues();
		// subscribe to variables
		dataAgent.add(variables);
		// register
		dataAgent.register();
		timer = new Timer(500, new ActionListener() {
                
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        IAction action = createBasicAction("value_changed");
                        action.addAttribute("name", lastValueChangedAction.getAttribute("name"));
                        action.addAttribute("oldValue", firstValueChangedAction.getAttribute("oldValue"));
                        action.addAttribute("newValue", lastValueChangedAction.getAttribute("newValue"));
                        write(action);
                    }
                });
		timer.setRepeats(false);
	}
	
	private ArrayList<ModelVariable> getInputVariables() {
		ArrayList<ModelVariable> variables = new ArrayList<ModelVariable>();
		for (ModelVariable variable : dataServer.getVariables("")) {
			if (variable.getKind() == ModelVariable.VK_INPUT) {
				System.out.println("Logger added variable: "+variable.getName()+" type: "+variable.getKind());
				variables.add(variable);
			}
		}
		return variables;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("ScyDynamics.Logger actionPerformed.");		
	}

	@Override
	public void updateClient() {
		for (int i=0; i<variables.size(); i++) {
			if (variables.get(i).getValue()!=oldValues.get(i)) {
				logValueChanged(variables.get(i).getName(), oldValues.get(i), variables.get(i).getValue());
			}
		}
		storeOldValues();
	}
	
	private void logValueChanged(String name, double oldValue, double newValue) {
		action = createBasicAction("value_changed");
		action.addAttribute("name", name);
		action.addAttribute("oldValue", oldValue+"");
		action.addAttribute("newValue", newValue+"");
		if (!timer.isRunning()) {
		    firstValueChangedAction = action; 
		}
		lastValueChangedAction = action;
		timer.restart();
	}

	private void storeOldValues() {
		oldValues = new ArrayList<Double>();
		for (ModelVariable var : variables) {
			oldValues.add(var.getValue());
		}
	}

	public IAction createBasicAction(String type) {
		IAction action = new Action(type, username);
		action.addContext("tool", toolname);
		action.addContext("mission", missionname);
		return action;
	}

	public void logAddRow(DataSetRow newRow) {
		action = createBasicAction("add_row");
		action.addAttribute("dataRow", "", newRow.toXML());
		write(action);
	}
	
	public void logSelectedVariables(List<ModelVariable> selectedVariables) {
		action = createBasicAction("variables_selected");
		String selection = new String();
		for (ModelVariable var : selectedVariables) {
			selection = selection + var.getName() + ", ";
		}
		if (selection.length()>2) {
			selection = selection.substring(0, selection.length()-2);
		}
		action.addAttribute("selected_variables", selection);
		write(action);		
	}
	
	private void write(IAction action) {
		try {
			outputter.output(action.getXML(), System.out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
// the next method creates a tuple from an xml-element
// and sends it when a tuplespace client is available
	
//	protected void writeAction(Element action) {
//		if (ts != null) {
//			idField = new Field(action.getAttributeValue("id"));
//			timeField = new Field(action.getAttributeValue("time"));
//			typeField = new Field(action.getAttributeValue("type"));
//			userField = new Field(action.getAttributeValue("user"));
//			for (Element elem: ((List<Element>)action.getChild("context").getChildren("property"))) {
//				if (elem.getAttributeValue("name").equals("tool")) {
//					toolField = new Field(elem.getAttributeValue("value"));
//				} else if (elem.getAttributeValue("name").equals("mission")) {
//					missionField = new Field(elem.getAttributeValue("value"));
//				}
//			}
//			xmlField = new Field(new XMLOutputter(Format.getPrettyFormat()).outputString(action)+"");
//			actionTuple = new Tuple(idField, timeField, typeField, userField, toolField, missionField, xmlField);
//			try {
//				ts.write(actionTuple);
//			} catch (TupleSpaceException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
}
