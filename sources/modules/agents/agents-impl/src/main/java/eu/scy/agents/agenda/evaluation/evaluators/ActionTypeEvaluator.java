package eu.scy.agents.agenda.evaluation.evaluators;

import info.collide.sqlspaces.commons.Tuple;

import java.util.List;


public class ActionTypeEvaluator implements IEvaluator {

	private final String tool;
	
	private final List<String> actionTypes;
	
	public ActionTypeEvaluator(String tool, List<String> actionTypes) {
		this.tool = tool;
		this.actionTypes = actionTypes;
	}
	
	public boolean checkModified(String tool, String actionType) {
		return (this.tool.equals(tool) && actionTypes.contains(actionType));
	}
	
	@Override
	public boolean doesMatch(Tuple t) {
		// ("action":String, <ID>:String, <Timestamp>:long, <Type>:String, <User>:String, <Tool>:String, <Mission>:String, <Session>:String, <ELOUri>:String, <Key=Value>:String*)
		String type = t.getField(3).getValue().toString();
		String tool = t.getField(5).getValue().toString();
		return checkModified(tool, type);
	}

}
