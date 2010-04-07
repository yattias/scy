package eu.scy.core.model.impl;

import eu.scy.core.model.ELORef;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.pedagogicalplan.BaseObjectImpl;
import eu.scy.core.model.impl.pedagogicalplan.MissionImpl;
import eu.scy.core.model.pedagogicalplan.Mission;

import javax.persistence.*;

/**
 * @author bjoerge
 * @created 23.feb.2010 18:27:04
 */

@Entity()
@Table(name = "eloref")
public class ELORefImpl extends BaseObjectImpl implements ELORef {
	private Mission mission;
	private String eloURI;
	private String version;
	private String title;
	private String image;
	private String tool;
	private String type;
	private String topic;
	private User author;

	@Override
	@Column(name = "elo_uri")
	public String getELOURI() {
		return this.eloURI;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getImage() {
		return image;
	}

	@Override
	public String getTool() {
		return tool;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getTopic() {
		return topic;
	}

	@ManyToOne(targetEntity = SCYUserImpl.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_primKey")
	@Override
	public User getAuthor() {
		return author;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	@ManyToOne(targetEntity = MissionImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "mission_primKey")
	public Mission getMission() {
		return mission;
	}

	@Override
	public void setMission(Mission mission) {
		this.mission = mission;
	}

	public void setELOURI(String uri) {
		this.eloURI = uri;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setTool(String tool) {
		this.tool = tool;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
}
