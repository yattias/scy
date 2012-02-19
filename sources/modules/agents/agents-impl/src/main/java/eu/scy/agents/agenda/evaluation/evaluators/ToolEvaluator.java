package eu.scy.agents.agenda.evaluation.evaluators;

import info.collide.sqlspaces.commons.Tuple;

public class ToolEvaluator implements IEvaluator {

	private final String tool;
	
	public ToolEvaluator(String tool) {
		this.tool = tool;
	}
	@Override
	public boolean doesMatch(Tuple t) {
		// ("action":String, <ID>:String, <Timestamp>:long, <Type>:String, <User>:String, <Tool>:String, <Mission>:String, <Session>:String, <ELOUri>:String, <Key=Value>:String*)
		String tupleTool = t.getField(5).getValue().toString();
		return this.tool.equals(tupleTool);
	}

	@Override
	public String getTool() {
		return this.tool;
	}
	
}
