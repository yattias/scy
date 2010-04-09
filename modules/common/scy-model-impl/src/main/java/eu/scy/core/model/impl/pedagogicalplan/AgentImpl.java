package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Agent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2010
 * Time: 13:55:48
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="agent")
public class AgentImpl extends BaseObjectImpl implements Agent {
}
