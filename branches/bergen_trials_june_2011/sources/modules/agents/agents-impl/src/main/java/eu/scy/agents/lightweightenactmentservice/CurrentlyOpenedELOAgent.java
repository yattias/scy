package eu.scy.agents.lightweightenactmentservice;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.HashMap;
import java.util.Map;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;

public class CurrentlyOpenedELOAgent implements Callback {

    private static final String TUPLE_INFO = "CurrenlyOpenELO";

    private TupleSpace commandSpace;

    private TupleSpace actionSpace;

    private Map<String, TupleID> tuples;

    public CurrentlyOpenedELOAgent() {
        try {
            commandSpace = new TupleSpace(User.getDefaultUser(), "scy.collide.info", 2525, false, false, "command");
            actionSpace = new TupleSpace(User.getDefaultUser(), "scy.collide.info", 2525, false, false, "actions");
            actionSpace.eventRegister(Command.WRITE, new Tuple("action", Field.createWildCardField()), this, false);
            commandSpace.takeAll(new Tuple(TUPLE_INFO,Field.createWildCardField()));
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        tuples = new HashMap<String, TupleID>();

    }

    @Override
    public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
        if (afterTuple == null) {
            return;
        }
        Action a = (Action) ActionTupleTransformer.getActionFromTuple(afterTuple);
        processAction(a);
    }

    private void processAction(Action a) {
        if (a.getType().equals("tool_opened")) {
            if (tuples.containsKey(a.getUser())) {
                try {
                    commandSpace.update(tuples.get(a.getUser()), new Tuple(TUPLE_INFO,a.getUser(), a.getContext().get(ContextConstants.eloURI)));
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    TupleID tupleID = commandSpace.write(new Tuple(TUPLE_INFO,a.getUser(), a.getContext().get(ContextConstants.eloURI)));
                    tuples.put(a.getUser(), tupleID);
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }
            }
        } else if (a.getType().equals("tool_quit")) {
            Tuple tuple = new Tuple();
            tuple.setTupleID(tuples.get(a.getUser()));
            try {
                commandSpace.delete(tuple);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }


        }else{
            System.out.println(a);
        }

    }

    public static void main(String[] args) {
        new CurrentlyOpenedELOAgent();
    }

}
