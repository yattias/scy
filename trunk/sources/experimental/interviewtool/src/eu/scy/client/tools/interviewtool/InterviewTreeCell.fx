/*
 * InterviewTreeCell.fx
 *
 * Created on 16.08.2009, 18:40:30
 */

package eu.scy.client.tools.interviewtool;

/**
 * @author kaido
 */

/**
* Tree cell.
* <ul>
* <li>All tree cells use variables text, cells and value.
* <li>Topic and indicator tree cells use variables topic and topicNo.
* <li>Indicator tree cell uses variables indicator and indicatorNo.
* </ul>
* @see InterviewTopic
* @see InterviewIndicator
*/
public class InterviewTreeCell {
    public var text: String;
    public var cells: InterviewTreeCell[];
    public var value: Integer;
    public var topic: InterviewTopic;
    public var topicNo: Integer;
    public var indicator: InterviewIndicator;
    public var indicatorNo: Integer;
    override function toString():String {
        return text;
    }
}
