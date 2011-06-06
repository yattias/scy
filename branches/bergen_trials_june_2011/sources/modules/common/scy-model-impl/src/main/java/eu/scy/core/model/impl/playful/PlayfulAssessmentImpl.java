package eu.scy.core.model.impl.playful;

import eu.scy.core.model.ELORef;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.ELORefImpl;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.pedagogicalplan.BaseObjectImpl;
import eu.scy.core.model.playful.PlayfulAssessment;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author bjoerge
 * @created 23.feb.2010 18:02:44
 */

@Entity
@Table(name = "playful_assessment")
public class PlayfulAssessmentImpl extends BaseObjectImpl implements PlayfulAssessment {
	private User reviewer;
	private ELORef eloRef;
	private String comment;
	private Date date;
	private Integer score;

	@ManyToOne(targetEntity = ELORefImpl.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
	@JoinColumn(name = "eloref_primKey")
	@Override
	public ELORef getELORef() {
		return eloRef;
	}

	@Override
	public void setELORef(ELORef eloRef) {
		this.eloRef = eloRef;
	}

	@Override
	public String getComment() {
		return this.comment;
	}

	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
	}

	@ManyToOne(targetEntity = SCYUserImpl.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewer_primKey")
	@Override
	public User getReviewer() {
		return reviewer;
	}

	@Override
	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	@Override
	public Integer getScore() {
		return score;
	}

	@Override
	public void setScore(Integer score) {
		this.score = score;
	}
}
