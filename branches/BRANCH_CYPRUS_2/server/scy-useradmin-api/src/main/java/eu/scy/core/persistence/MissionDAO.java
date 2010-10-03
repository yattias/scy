package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.Mission;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 12:10:53
 * To change this template use File | Settings | File Templates.
 */
public interface MissionDAO extends SCYBaseDAO{
    public Mission getMission(String parameter);
}
