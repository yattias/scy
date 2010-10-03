package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.AgentPropertyValue;
import eu.scy.core.model.pedagogicalplan.AgentPropertyValueLevel;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 28.mai.2010
 * Time: 13:27:15
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "agentpropertyvaluelevel")
public class AgentPropertyValueLevelImpl  extends BaseObjectImpl implements AgentPropertyValueLevel {

    private Integer levelIndex;

    @Override
    public Integer getLevelIndex() {
        return levelIndex;
    }

    @Override
    public void setLevelIndex(Integer levelIndex) {
        this.levelIndex = levelIndex;
    }
}
