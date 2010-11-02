package eu.scy.agents.groupformation;

import java.util.List;

import roolo.elo.api.IELO;

public interface GroupFormationStrategy {

	List<String> formGroup(IELO elo, String user);

}
