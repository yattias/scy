package eu.scy.actionlogging;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;

import java.util.Set;
import java.util.Map.Entry;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;

public class ActionTupleTransformer {

	public static IAction getActionFromTuple(Tuple actionAsTuple) {
		Action actionAsPojo = new Action((String) actionAsTuple.getField(1).getValue());
		actionAsPojo.setUser((String) actionAsTuple.getField(4).getValue());
		actionAsPojo.setType((String) actionAsTuple.getField(3).getValue());
		actionAsPojo.setTimeInMillis((Long) actionAsTuple.getField(2).getValue());
		// creating the context information
		actionAsPojo.addContext(ContextConstants.tool, (String) actionAsTuple.getField(5).getValue());
		actionAsPojo.addContext(ContextConstants.mission, (String) actionAsTuple.getField(6).getValue());
		actionAsPojo.addContext(ContextConstants.session, (String) actionAsTuple.getField(7).getValue());
		// Datatype and data
		/*actionAsPojo.setDataType((String) actionAsTuple.getField(8).getValue());
		actionAsPojo.setData((String) actionAsTuple.getField(9).getValue());*/

		// creating the attribute list (key/value)

		for (int i = 10; i < actionAsTuple.getNumberOfFields(); i++) {
			String att = (String) actionAsTuple.getField(i).getValue();
			int index = att.indexOf('=');
			String key = att.substring(0, index - 1);
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
		/*if(actionAsPojo.getDataType()!=null) {
			actionAsTuple.add(new Field(actionAsPojo.getDataType()));
		} else {
			actionAsTuple.add(new Field(String.class));
		}
		if(actionAsPojo.getData()!=null) {
			actionAsTuple.add(new Field(actionAsPojo.getData()!=null));
		} else {
			actionAsTuple.add(new Field(String.class));
		}*/

		// creating the attribute list (key/value)
		Set<Entry<String, String>> entrySet = actionAsPojo.getAttributes().entrySet();
		for (Entry<String, String> entry : entrySet) {
			actionAsTuple.add(entry.getKey() + '=' + entry.getValue());
		}
		return actionAsTuple;
	}
}
