package eu.scy.client.common.datasync;

/**
 *
 * @author lars
 */
public interface ISynchronizable {

    public Object getScyWindow();

    public void join(String mucID);

    public void join(String mudID, Object datasyncEdge);

    public void leave(String mucID);

    public String getSessionID();

    public String getToolName();

}
