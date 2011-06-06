/*
 * InterviewIndicator.fx
 *
 * Created on 22.08.2009, 10:11:02
 */

package eu.scy.client.tools.interviewtool;

/**
 * @author kaido
 */

/**
* Indicator for topic
* @see InterviewTopic
*/
public class InterviewIndicator extends InterviewObject {
    public var indicator: String;
    public var answers: InterviewAnswer[];
    public var answerIncludeNamely: Boolean;
    public var formulation: String;
    public override function toString() {
        return indicator;
    }
    public override function setValue(value: String) {
        indicator = value;
    }
}
