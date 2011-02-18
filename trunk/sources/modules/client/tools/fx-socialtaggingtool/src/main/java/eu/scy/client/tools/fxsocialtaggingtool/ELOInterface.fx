/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxsocialtaggingtool;

import roolo.elo.api.IELO;

public class ELOInterface {

    /**
     * Interface class for the Tagging GUI to read and write tags to ELO objects
     *
     * The two (or three) top most functions communicate with the ELO. The rest of the functions are internal helper functions and public functions.
     *
     * @author sindre
     */
    function setTag(elo: IELO, tag: Tag): Tag {
        /**
         * Sets a tag from the tagging interface in the ELO object
         *
         * @param elo The IELO to set the tag in
         * @param tag A tag to be saved in elo
         * @return A Tag when successful
         */
        // TODO connect to ELO
        return Tag {};
    }

    function getAllTags(elo: IELO): Tag[] {
        /**
         * Retrieves all tags from all users associated with an IELO.
         *
         * @param elo The IELO to get all tags from
         * @return An array of Tag objects
         */
        [// TODO connect to ELO
            Tag {
            }]
    }

    function addTag(elo: IELO, tag: Tag): Tag {
        return tag;
    }

    function removeTag(elo: IELO, tag: Tag): Tag {
        // TODO connect to ELO
        return tag;
    }

    public function addVoteForTag(elo: IELO, tag: Tag, user): Tag {
        return tag;
    }

    public function removeVoteForTag(elo: IELO, tag: Tag): Tag {
        return tag;
    }

}
