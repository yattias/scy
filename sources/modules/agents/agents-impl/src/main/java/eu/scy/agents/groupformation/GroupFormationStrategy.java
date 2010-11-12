package eu.scy.agents.groupformation;

import info.collide.sqlspaces.client.TupleSpace;

import java.util.List;

import roolo.elo.api.IELO;

public interface GroupFormationStrategy {

	public List<String> formGroup(IELO elo, String mission, String user);

	public void setCommandSpace(TupleSpace commandSpace);

	public TupleSpace getCommandSpace();

}
