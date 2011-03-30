/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxsocialtaggingtool;

/**
 * @author sindre
 */

public class Tag {
    /**
     * A tag is here considered a string with a number of voters.
     *
     * Right now users are strings, but they may change to user ids or similar.
     *
     * To find the "strength" of the tag the nayvoters are subtracted from the ayevoters.
     *
     */
    public var tagname: String;
    public var ayevoters: String[];
    public var nayvoters: String[];

    public override function toString() : String {
        return tagname;
    }

}
