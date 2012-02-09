package eu.scy.agents.agenda.evaluation.evaluators;

import info.collide.sqlspaces.commons.Tuple;

public interface IEvaluator {

	/**
	 * This method checks whether the tuple does match a specific pattern.
	 * If it does match, the return value will be true.
	 * @param t the tuple to check
	 * @return true if tuple does match
	 */
	public boolean doesMatch(Tuple t);
	
	/**
	 * Returns the name of the tool, that this evaluator belongs to
	 * @return
	 */
	public String getTool();
	
}
