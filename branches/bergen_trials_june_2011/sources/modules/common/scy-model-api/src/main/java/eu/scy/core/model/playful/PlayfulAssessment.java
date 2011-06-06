package eu.scy.core.model.playful;

import eu.scy.core.model.ELORef;
import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.BaseObject;

import java.sql.Date;

/**
 * @author bjoerge
 * @created 23.feb.2010 18:02:12
 */
public interface PlayfulAssessment extends BaseObject {
	ELORef getELORef();

	void setELORef(ELORef eloRef);

	String getComment();

	void setComment(String comment);

	Date getDate();

	void setDate(Date date);

	User getReviewer();

	void setReviewer(User reviewer);

	Integer getScore();

	void setScore(Integer score);
}
