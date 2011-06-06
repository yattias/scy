/*
 * InterviewAnswer.fx
 *
 * Created on 22.08.2009, 10:00:53
 */

package eu.scy.client.tools.interviewtool;

/**
 * @author kaido
 */

/**
* Answer option for indicator
* @see InterviewIndicator
*/
public class InterviewAnswer extends InterviewObject {
    public var answer: String;
    public override function toString() {
        return answer;
    }
    public override function setValue(value: String) {
        answer = value;
    }
}
