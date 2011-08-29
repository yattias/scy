package eu.scy.agents.processguidanceservice;

import info.collide.sqlspaces.commons.TupleID;

public abstract class AbstractRun extends AbstractGuidanceObject {

    protected TupleID myTupleID;

    protected TupleID getTupleID() {
        return myTupleID;
    }

    protected void setTupleID(TupleID aTupleID) {
        this.myTupleID = aTupleID;
    }

}
