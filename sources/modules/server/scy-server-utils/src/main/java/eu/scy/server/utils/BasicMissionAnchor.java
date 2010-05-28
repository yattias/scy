package eu.scy.server.utils;

import roolo.elo.api.IMetadata;

import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.feb.2010
 * Time: 13:38:41
 * To change this template use File | Settings | File Templates.
 */
public class BasicMissionAnchor {
    private URI uri;
    private IMetadata metadata;
    private String id;
    private List<URI> loEloUris;
    private List<String> inputMissionAnchorIds;
    private List<String> relationNames;
    private Integer xPos;
    private Integer yPos;

    public String getId()
    {
       return id;
    }

    public List<String> getInputMissionAnchorIds()
    {
       return inputMissionAnchorIds;
    }

    public void setInputMissionAnchorIds(List<String> inputMissionAnchorIds)
    {
       this.inputMissionAnchorIds = inputMissionAnchorIds;
    }

    public void setId(String id)
    {
       this.id = id;
    }

    public URI getUri()
    {
       return uri;
    }

    public void setUri(URI eloUri)
    {
       this.uri = eloUri;
    }

    public List<String> getRelationNames()
    {
       return relationNames;
    }

    public void setRelationNames(List<String> relationNames)
    {
       this.relationNames = relationNames;
    }

    public List<URI> getLoEloUris()
    {
       return loEloUris;
    }

    public void setLoEloUris(List<URI> loEloUris)
    {
       this.loEloUris = loEloUris;
    }

    public void setMetadata(IMetadata metadata)
    {
       this.metadata = metadata;
    }

    public IMetadata getMetadata()
    {
       return metadata;
    }

    public Integer getxPos() {
        return xPos;
    }

    public void setxPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getyPos() {
        return yPos;
    }

    public void setyPos(Integer yPos) {
        this.yPos = yPos;
    }
}
