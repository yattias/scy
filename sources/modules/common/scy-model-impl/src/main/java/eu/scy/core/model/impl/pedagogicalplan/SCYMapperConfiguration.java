package eu.scy.core.model.impl.pedagogicalplan;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.nov.2009
 * Time: 09:58:34
 * To change this template use File | Settings | File Templates.
 */


@Entity
@DiscriminatorValue(value= "SCYMapper")
public class SCYMapperConfiguration extends LearningActivitySpaceToolConfigurationImpl {
}
