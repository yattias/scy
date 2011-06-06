package eu.scy.core.model;

import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.BaseObject;
import eu.scy.core.model.pedagogicalplan.Mission;

import java.util.Date;

/**
 * @author bjoerge
 * @created 23.feb.2010 18:05:52
 * Holds a reference to an ELO
 */
public interface ELORef extends BaseObject {
	String getELOURI();

	void setELOURI(String uri);

	String getTitle();

	void setTitle(String title);

	String getImage();

	void setImage(String image);

	String getTool();

	void setTool(String tool);

	String getType();

	void setType(String type);

	String getTopic();

	void setTopic(String topic);

	User getAuthor();

	void setAuthor(User author);

	String getVersion();

	void setVersion(String version);

	Mission getMission();

	void setMission(Mission mission);

    Date getDate();

    void setDate(Date date);

    String getFormattedDate();

    String getComment();

    void setComment(String comment);

    Integer getViewings();

    void setViewings(Integer viewings);

    AnchorELO getAnchorELO();

    void setAnchorELO(AnchorELO anchorELO);

    Boolean getHidden();

    void setHidden(Boolean hidden);
}
