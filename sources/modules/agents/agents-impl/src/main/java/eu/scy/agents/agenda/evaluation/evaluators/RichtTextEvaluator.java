package eu.scy.agents.agenda.evaluation.evaluators;

import info.collide.sqlspaces.commons.Tuple;

import java.util.List;

public class RichtTextEvaluator extends ActionTypeEvaluator {

	public RichtTextEvaluator(String tool, List<String> actionTypes) {
		super(tool, actionTypes);
	}
	
	@Override
	public boolean doesMatch(Tuple t) {
		// ("action":String, <ID>:String, <Timestamp>:long, <Type>:String, <User>:String, <Tool>:String, <Mission>:String, <Session>:String, <ELOUri>:String, <Key=Value>:String*)
		String type = t.getField(3).getValue().toString();
		String tool = t.getField(5).getValue().toString();
		if(type.equals("text_inserted") && t.getFields().length == 11) {
			// this hack filters out tuples with no text field.
			return false;
		}
		return checkModified(tool, type);
	}
}
