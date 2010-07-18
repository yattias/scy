package eu.scy.core.model.impl;

import eu.scy.core.model.ELORef;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.pedagogicalplan.BaseObjectImpl;
import eu.scy.core.model.impl.pedagogicalplan.MissionImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Mission;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private Date date;
    private String comment;
    private Integer viewings = 0;
    private AnchorELO anchorELO;
    private Boolean hidden = false;

    private List fileRefs ;

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

    @Override
    @Temporal(value = TemporalType.DATE)
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    @Transient
    public String getFormattedDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        if(getDate() != null) {
            return format.format(getDate());
        }

        return "";

    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public Integer getViewings() {
        return viewings;
    }

    @Override
    public void setViewings(Integer viewings) {
        this.viewings = viewings;
    }

    @Override
    @ManyToOne (targetEntity = AnchorELOImpl.class)
    @JoinColumn(name="anchorELO_primKey")
    public AnchorELO getAnchorELO() {
        return anchorELO;
    }

    @Override
    public void setAnchorELO(AnchorELO anchorELO) {
        this.anchorELO = anchorELO;
    }

    @Override
    public Boolean getHidden() {
        return hidden;
    }

    @Override
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }
}
