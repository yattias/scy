package eu.scy.scymapper.impl.configuration;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.scymapper.api.IConceptFactory;
import eu.scy.scymapper.api.ILinkFactory;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.model.INodeModel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 30.okt.2009
 * Time: 11:29:55
 */
public class SCYMapperToolConfiguration implements ISCYMapperToolConfiguration {
	private String name;
	private String description;
	private LearningActivitySpace learningActivitySpace;
	private Tool tool;
	private List<IConceptFactory> availableConceptFactories;
	private List<ILinkFactory> availableLinkFactories;
	private List<INodeModel> predefinedNodes;
	private static ISCYMapperToolConfiguration INSTANCE;
	private boolean debugMode;
	private String id;

	private SCYMapperToolConfiguration() {

	}

	public static ISCYMapperToolConfiguration getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SCYMapperToolConfiguration();
		}
		return INSTANCE;
	}

	@Override
	public List<IConceptFactory> getNodeFactories() {
		return availableConceptFactories;
	}

	@Override
	public List<ILinkFactory> getLinkFactories() {
		return availableLinkFactories;
	}

	@Override
	public void setNodeFactories(List<IConceptFactory> availableConceptFactories) {
		this.availableConceptFactories = availableConceptFactories;
	}

	@Override
	public void setLinkFactories(List<ILinkFactory> availableLinkFactories) {
		this.availableLinkFactories = availableLinkFactories;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setLearningActivitySpace(LearningActivitySpace learningActivitySpace) {
		this.learningActivitySpace = learningActivitySpace;
	}

	@Override
	public LearningActivitySpace getLearningActivitySpace() {
		return learningActivitySpace;
	}

	@Override
	public void setTool(Tool tool) {
		this.tool = tool;
	}

	@Override
	public Tool getTool() {
		return tool;
	}

	@Override
	public Activity getActivity() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setActivity(Activity activity) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setPredefinedNodes(List<INodeModel> predefinedNodes) {
		this.predefinedNodes = predefinedNodes;
	}

	@Override
	public List<INodeModel> getPredefinedNodes() {
		return predefinedNodes;
	}

	@Override
	public boolean isDebug() {
		return debugMode;
	}

	public void setDebug(boolean b) {
		debugMode = b;
	}
}
