package eu.scy.actionlogging;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;

import java.util.Set;
import java.util.Map.Entry;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;

public class ActionTupleTransformer {

	private static final int ATTRIBUTES_START = 9;

	public static IAction getActionFromTuple(Tuple actionAsTuple) {
		Action actionAsPojo = new Action((String) actionAsTuple.getField(1).getValue());
		actionAsPojo.setUser((String) actionAsTuple.getField(4).getValue());
		actionAsPojo.setType((String) actionAsTuple.getField(3).getValue());
		actionAsPojo.setTimeInMillis((Long) actionAsTuple.getField(2).getValue());

		// creating the context information
		actionAsPojo.addContext(ContextConstants.tool, (String) actionAsTuple.getField(5).getValue());
		actionAsPojo.addContext(ContextConstants.mission, (String) actionAsTuple.getField(6).getValue());
		actionAsPojo.addContext(ContextConstants.session, (String) actionAsTuple.getField(7).getValue());
		actionAsPojo.addContext(ContextConstants.eloURI, (String) actionAsTuple.getField(8).getValue());

		// creating the attribute list (key/value)
		for (int i = ATTRIBUTES_START; i < actionAsTuple.getNumberOfFields(); i++) {
			String att = (String) actionAsTuple.getField(i).getValue();
			int index = att.indexOf('=');
			String key = att.substring(0, index);
			String value = att.substring(index + 1, att.length());
			actionAsPojo.addAttribute(key, value);
		}
		return actionAsPojo;
	}

	public static Tuple getActionAsTuple(IAction actionAsPojo) {
		Tuple actionAsTuple = new Tuple();
		actionAsTuple.add(new Field("action"));
		actionAsTuple.add(new Field(actionAsPojo.getId()));
		actionAsTuple.add(new Field(actionAsPojo.getTimeInMillis()));
		actionAsTuple.add(new Field(actionAsPojo.getType()));
		actionAsTuple.add(new Field(actionAsPojo.getUser()));

		actionAsTuple.add(new Field(actionAsPojo.getContext(ContextConstants.tool)));
		actionAsTuple.add(new Field(actionAsPojo.getContext(ContextConstants.mission)));
		actionAsTuple.add(new Field(actionAsPojo.getContext(ContextConstants.session)));
		actionAsTuple.add(new Field(actionAsPojo.getContext(ContextConstants.eloURI)));

		// creating the attribute list (key/value)
		Set<Entry<String, String>> entrySet = actionAsPojo.getAttributes().entrySet();
		for (Entry<String, String> entry : entrySet) {
			actionAsTuple.add(entry.getKey() + '=' + entry.getValue());
		}
		return actionAsTuple;
	}
}
