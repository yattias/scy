package eu.scy.scymapper.impl.configuration;

import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.scymapper.api.IConceptPrototype;
import eu.scy.scymapper.api.ILinkType;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge
 * Date: 30.okt.2009
 * Time: 11:29:55
 */
public class SCYMapperToolConfiguration implements ISCYMapperToolConfiguration {
    private String name;
    private String description;
    private LearningActivitySpace learningActivitySpace;
    private Tool tool;
    private List<IConceptPrototype> availableConceptPrototypes;
    private List<ILinkType> availableLinkTypes;

    @Override
    public List<IConceptPrototype> getAvailableConceptTypes() {
        return availableConceptPrototypes;
    }

    @Override
    public List<ILinkType> getAvailableLinkTypes() {
        return availableLinkTypes;
    }

    @Override
    public void setAvailableConceptTypes(List<IConceptPrototype> availableConceptPrototypes) {
        this.availableConceptPrototypes = availableConceptPrototypes;
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
}
