package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.PeerToPeerAssessmentStrategy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.des.2009
 * Time: 06:38:58
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue("peerToPeerStrategy")
public class PeerToPeerAssessmentStrategyImpl extends AssessmentStrategyImpl implements PeerToPeerAssessmentStrategy{
}
