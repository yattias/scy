package eu.scy.agents.processguidanceservice;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleID;

public abstract class AbstractRun extends AbstractGuidanceObject {

    protected TupleID myTupleID;

    public AbstractRun(TupleSpace commandSpace, TupleSpace guidanceSpace) {
        super(commandSpace, guidanceSpace);
    }

    protected TupleID getTupleID() {
        return myTupleID;
    }

    protected void setTupleID(TupleID aTupleID) {
        this.myTupleID = aTupleID;
    }

}
