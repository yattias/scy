/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxsocialtaggingtool;

import roolo.elo.api.IELO;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import roolo.elo.api.IMetadataKey;
import roolo.elo.metadata.keys.SocialTags;
import java.util.Map;

public class ELOInterface {

    public-init var tbi: ToolBrokerAPI;
    public-init var eloUri: URI;
    var elo: IELO;
    var socialtagsKey: IMetadataKey;

    init {
        elo = tbi.getRepository().retrieveELO(eloUri);
        socialtagsKey = tbi.getMetaDataTypeManager().getMetadataKey("socialTags");
    }

    /**
     * Interface class for the Tagging GUI to read and write tags to ELO objects
     *
     * The two (or three) top most functions communicate with the ELO. The rest of the functions are internal helper functions and public functions.
     *
     * @author sindre
     */
    public function getCurrentUser(): String {
        return tbi.getLoginUserName();
    }

    function getAllTags(): Tag[] {
        /**
         * Retrieves all tags from all users associated with an IELO.
         *
         * @param elo The IELO to get all tags from
         * @return An array of Tag objects
         */
        var tags: Tag[] = [];
        var socialTags: SocialTags = elo.getMetadata().getMetadataValueContainer(socialtagsKey).getValue() as SocialTags;

        if (socialTags != null) {
            for (st in socialTags.getSocialTags()) {
                var ayevoters: String[];
                var nayvoters: String[];
                var conts: Map = st.getContributions() as Map;
                for (contributor in st.getContributorNames()) {
                    var value: Number = conts.get(contributor) as Number;
                    if (value > 0) {
                        insert contributor into ayevoters;
                    } else {
                        insert contributor into nayvoters;
                    }
                }
                var t = Tag {
                            tagname: st.getTagName();
                            ayevoters: ayevoters;
                            nayvoters: nayvoters;
                        };
                insert t into tags;
            }
        }
        return tags;
    }

    public function addTag(tag: Tag): Tag {
        var mvc = elo.getMetadata().getMetadataValueContainer(socialtagsKey);
        if (mvc.getValue() == null) {
            mvc.setValue(new SocialTags());
        }
        var st: SocialTags = mvc.getValue() as SocialTags;
        st.addSocialTag(tag.tagname, getCurrentUser());
        tbi.getRepository().updateELO(elo);
        return tag;
    }

    public function removeTag(tag: Tag): Tag {
        var mvc = elo.getMetadata().getMetadataValueContainer(socialtagsKey);
        if (mvc.getValue() == null) {
            mvc.setValue(new SocialTags());
        }
        var st: SocialTags = mvc.getValue() as SocialTags;
        st.removeContributeFromTag(tag.tagname, getCurrentUser());
        tbi.getRepository().updateELO(elo);
        return tag;
    }

    public function addVoteForTag(tag: Tag, user): Tag {
        // TODO: inspect existing tags and update by calling setTag
        return tag;
    }

    public function removeVoteForTag(tag: Tag): Tag {
        // TODO: inspect existing tags and update by calling setTag
        return tag;
    }

}
