package eu.scy.core.model;

import eu.scy.core.model.pedagogicalplan.BaseObject;
import eu.scy.core.model.pedagogicalplan.Mission;

/**
 * @author bjoerge
 * @created 23.feb.2010 18:05:52
 * Holds a reference to an ELO
 */
public interface ELORef extends BaseObject {
	String getURI();

	void setURI(String uri);

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
}
