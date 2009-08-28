package eu.scy.scyplanner.api.model;

import eu.scy.scymapper.api.diagram.INodeModel;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.aug.2009
 * Time: 12:12:55
 */
public interface ILearningActivitySpaceDiagramNode extends INodeModel {
	ILearningActivitySpace getLearningActivitySpace();
	void setLearningActivitySpace(ILearningActivitySpace las);
}
