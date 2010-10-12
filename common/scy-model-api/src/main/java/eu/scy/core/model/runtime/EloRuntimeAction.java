package eu.scy.core.model.runtime;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.apr.2010
 * Time: 08:42:00
 * To change this template use File | Settings | File Templates.
 */
public interface EloRuntimeAction extends AbstractRuntimeAction{
    
    String getEloUri();

    void setEloUri(String eloUri);
}
