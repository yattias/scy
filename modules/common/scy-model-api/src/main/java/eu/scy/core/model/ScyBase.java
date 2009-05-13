package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 05:49:38
 * Base interface for all model interfaces
 */
public interface ScyBase {

    public String getName();

    public void setName(String name);

    public String getId();

    public String getDescription();
    
    public void setId(String id);

    public void setDescription(String description);

}
