package eu.scy.scymapper.impl.configuration;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.scymapper.api.IConceptType;
import eu.scy.scymapper.api.ILinkType;
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
    private List<IConceptType> availableConceptTypes;
    private List<ILinkType> availableLinkTypes;
    private List<INodeModel> predefinedNodes;

    @Override
    public List<IConceptType> getAvailableConceptTypes() {
        return availableConceptTypes;
    }

    @Override
    public List<ILinkType> getAvailableLinkTypes() {
        return availableLinkTypes;
    }

    @Override
    public void setAvailableConceptTypes(List<IConceptType> availableConceptTypes) {
        this.availableConceptTypes = availableConceptTypes;
    }

    @Override
    public void setAvailableLinkTypes(List<ILinkType> availableLinkTypes) {
        this.availableLinkTypes = availableLinkTypes;
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
    public String getDescription() {
        return description;
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
}
