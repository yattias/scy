/*
 * InterviewObject.fx
 *
 * Created on 22.08.2009, 20:27:06
 */

package eu.scy.client.tools.interviewtool;

/**
 * @author kaido
 */

/**
* Base class for topics, indicators and answers which are used in tree and table
*/
abstract public class InterviewObject {
    public var id: Integer;
    abstract public function setValue(value: String);
}
