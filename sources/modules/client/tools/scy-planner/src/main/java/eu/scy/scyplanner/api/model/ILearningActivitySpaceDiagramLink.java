package eu.scy.scyplanner.api.model;

import eu.scy.scymapper.api.diagram.ILinkModel;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 10:48:49
 */
public interface ILearningActivitySpaceDiagramLink extends ILinkModel {
	void setFromLearningActivitySpace(ILearningActivitySpace las);
	ILearningActivitySpace getFromLearningActivitySpace();

	void setToLearningActivitySpace(ILearningActivitySpace las);
	ILearningActivitySpace getToLearningActivitySpace();
}
