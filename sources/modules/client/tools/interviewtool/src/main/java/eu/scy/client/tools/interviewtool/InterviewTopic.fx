/*
 * InterviewTopic.fx
 *
 * Created on 22.08.2009, 10:14:25
 */

package eu.scy.client.tools.interviewtool;

/**
 * @author kaido
 */

/**
* Interview topic. Topic can contain many indicators and each indicator can contain many answer options.
* @see InterviewIndicator
* @see InterviewAnswer
*/
public class InterviewTopic extends InterviewObject {
    public var topic: String;
    public var indicators: InterviewIndicator[];
    public override function toString() {
        return topic;
    }
    public override function setValue(value: String) {
        topic = value;
    }
}
