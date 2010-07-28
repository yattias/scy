package eu.scy.core.model.pedagogicalplan;

import eu.scy.core.model.FileRef;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.jul.2010
 * Time: 07:48:39
 * To change this template use File | Settings | File Templates.
 */
public interface AssessmentScoreDefinition extends BaseObject{

    Assessment getAssessment();

    void setAssessment(Assessment assessment);

    String getHeading();

    void setHeading(String heading);

    Integer getScore();

    void setScore(Integer score);

    FileRef getFileRef();

    void setFileRef(FileRef fileRef);
}
