package eu.scy.scymapper.api.configuration;

import eu.scy.core.model.pedagogicalplan.LearningActivitySpaceToolConfiguration;
import eu.scy.scymapper.api.IConceptPrototype;
import eu.scy.scymapper.api.ILinkType;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 30.okt.2009
 * Time: 12:06:18
 * To change this template use File | Settings | File Templates.
 */
public interface ISCYMapperToolConfiguration extends LearningActivitySpaceToolConfiguration {
    List<IConceptPrototype> getAvailableConceptTypes();
    List<ILinkType> getAvailableLinkTypes();

    void setAvailableConceptTypes(List<IConceptPrototype> availableConceptShapes);

    void setAvailableLinkTypes(List<ILinkType> availableLinkShapes);
}
