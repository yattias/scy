/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxsocialtaggingtool;

/**
 * @author sindre
 */

public class TagVote {

    /**
     * This is a vote for a Tag
     * The value is binary, as users can select to vote yes or no to an existing tag.
     *
     * Adding a tag entails creating a tag and voting yes to it.
     */
    public var username: String;
    public var value: Boolean;
}

public class Tag {

    public var tagname: String;
    public var ayevoters: String[];
    public var nayvoters: String[];
}
