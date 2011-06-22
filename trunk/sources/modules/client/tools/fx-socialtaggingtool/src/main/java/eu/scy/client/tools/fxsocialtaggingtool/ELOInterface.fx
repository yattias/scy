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
import roolo.elo.api.IMetadata;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.SyncObject;
import javafx.util.Sequences;

public class ELOInterface extends ISyncListener {

    def tagComparator : TagComparator = TagComparator{};

    public-init var tbi: ToolBrokerAPI;
    public-init var eloUri: URI;
    public-init var view: SocialTaggingDrawer;
    var elo: IELO;
    var syncSession: ISyncSession;
    var socialtagsKey: IMetadataKey;
    def demoMode = false;
    var testTags = [
                Tag {
                    tagname: "ecology";
                    ayevoters: ["Astrid", "Wilhelm"];
                    nayvoters: ["John", "Mary"];
                }
                Tag {
                    tagname: "environment";
                    ayevoters: ["Astrid", "Mary", "Wilhelm"];
                    nayvoters: ["John"];
                }
                Tag {
                    tagname: "language";
                    ayevoters: ["John"];
                    nayvoters: ["Mary", "Wilhelm"];
                }
                Tag {
                    tagname: "temperature";
                    ayevoters: ["John"];
                }
                Tag {
                    tagname: "energy"
                    ayevoters: ["Mary"];
                }
                Tag {
                    tagname: "carbon dioxide"
                    ayevoters: ["Mary"];
                }
            ];

    init {
        if (eloUri != null) {
            socialtagsKey = tbi.getMetaDataTypeManager().getMetadataKey("socialTags");
        }
    }

    /**
     * Interface class for the Tagging GUI to read and write tags to ELO objects
     *
     * The two (or three) top most functions communicate with the ELO. The rest of the functions are internal helper functions and public functions.
     *
     * @author sindre
     */
    public function getCurrentUser(): String {
        if (this.demoMode) return "John" else return tbi.getLoginUserName();
    }

    public function getAllTags(): Tag[] {
        /**
         * Retrieves all tags from all users associated with an IELO.
         *
         * @param elo The IELO to get all tags from
         * @return An array of Tag objects
         */
        if (this.demoMode == true) {
            return this.testTags;
        } else {
            var tags: Tag[] = [];
            elo = tbi.getRepository().retrieveELOLastVersion(eloUri);
            var socialTags: SocialTags = elo.getMetadata().getMetadataValueContainer(socialtagsKey).getValue() as SocialTags;

            if (socialTags != null) {
                for (st in socialTags.getSocialTags()) {
                    var ayevoters: String[];
                    var nayvoters: String[];
                    for (unliker in st.getUnlikingUsers()) {
                        insert unliker into nayvoters;
                    }
                    for (liker in st.getLikingUsers()) {
                        insert liker into ayevoters;
                    }
                    var t = Tag {
                                tagname: st.getTagName();
                                ayevoters: ayevoters;
                                nayvoters: nayvoters;
                            };
                    insert t into tags;
                }
            }
            return Sequences.sort(tags, tagComparator);
        }
    }

    public function addVoteForTag(like: Boolean, tag: Tag): Tag {
        /**
         * This is the central function to update tags
         *
         * The function must let each user have only one vote
         *
         */
        var user = getCurrentUser();
        if (this.demoMode) {
            var tagToUpdate = tag;
            def tags = this.testTags;
            def existingTag = tags[t | t.tagname == tag.tagname][0];
            // println(existingTag);
            if (existingTag != null) {
                // A tag with this name already exists; we'll use this
                tagToUpdate = existingTag;
            } else {
                // We'll use the one that was passed as a parameter
                tagToUpdate = tag;
                insert tagToUpdate into this.testTags;
            }
            if (like) {
                // User likes the tag
                delete user from tagToUpdate.nayvoters;
                if (tagToUpdate.ayevoters[v | v == user][0] != null)
                    // The user has already given this vote. We'll delete the vote instead
                    delete user from tagToUpdate.ayevoters else
                    insert user into tagToUpdate.ayevoters;
            } else {
                // User does not like the tag
                delete user from tagToUpdate.ayevoters;
                if (tagToUpdate.nayvoters[v | v == user][0] != null)
                    // The user has already given this vote. We'll delete the vote instead
                    delete user from tagToUpdate.nayvoters else
                    delete user from tagToUpdate.ayevoters;
                insert user into tagToUpdate.nayvoters;
            }
            if ((tagToUpdate.ayevoters.size() == 0) and (tagToUpdate.nayvoters.size() == 0)) {
                // There are no more voters for the tag. Delete it.
                delete tagToUpdate from this.testTags;
            }
            return tag;
        } else {
        	elo = tbi.getRepository().retrieveELOLastVersion(eloUri);
            var oldMetadata : IMetadata = elo.getMetadata();
            var mvc = oldMetadata.getMetadataValueContainer(socialtagsKey);
            var st: SocialTags = mvc.getValue() as SocialTags;
            if (st == null) {
                st = new SocialTags();
                mvc.setValue(st);
            }
            var userLikes = st.getLikingUsers(tag.tagname).contains(user);
            var userNotLikes = st.getUnlikingUsers(tag.tagname).contains(user);
            var newTag = false;
            if (not userLikes and not userNotLikes) {
                newTag = true;
            }
            if (userLikes and like) {
                st.removeLikingUser(tag.tagname, user);
                delete user from tag.ayevoters;
            } else if (userNotLikes and not like) {
                st.removeUnlikingUser(tag.tagname, user);
                delete user from tag.nayvoters;
            } else if (like) {
                st.addLikingUser(tag.tagname, user);
                insert user into tag.ayevoters;
                st.removeUnlikingUser(tag.tagname, user);
                delete user from tag.nayvoters;
            } else {
                st.addUnlikingUser(tag.tagname, user);
                insert user into tag.nayvoters;
                st.removeLikingUser(tag.tagname, user);
                delete user from tag.ayevoters;
            }
            if (st.getLikeCount(tag.tagname) + st.getUnlikeCount(tag.tagname) == 0) {
                st.removeSocialTag(tag.tagname);
            }
            tbi.getRepository().addMetadata(elo.getUri(), oldMetadata);

            if (syncSession != null and syncSession.isConnected()) {
                var object : ISyncObject = new SyncObject();
                object.setID("{tag.tagname}_{user}");
                object.setToolname("socialtagging");
                object.setProperty("tag", tag.tagname);
                object.setProperty("user", user);
                object.setProperty("like", Boolean.toString(like));
                if (newTag) {
                    syncSession.addSyncObject(object);
                } else {
                    syncSession.changeSyncObject(object);
                }
            }
            return tag;
        }
    }

    public function removeVoteForTag(tag: Tag): Tag {
        if (this.demoMode) {
            insert tag into this.testTags;
            return tag;
        } else {
     		elo = tbi.getRepository().retrieveELOLastVersion(eloUri);
            var mvc = elo.getMetadata().getMetadataValueContainer(socialtagsKey);
            var st: SocialTags = mvc.getValue() as SocialTags;
            st.removeLikingUser(tag.tagname, getCurrentUser());
            st.removeUnlikingUser(tag.tagname, getCurrentUser());
            tbi.getRepository().addMetadata(elo.getUri(), elo.getMetadata());

            if (syncSession != null and syncSession.isConnected()) {
                var object : ISyncObject = new SyncObject();
                object.setID("{tag.tagname}_{getCurrentUser()}");
                object.setToolname("socialtagging");
                object.setProperty("tag", tag.tagname);
                object.setProperty("user", getCurrentUser());
                syncSession.removeSyncObject(object);
            }

            return tag;
        }
    }

    public function addVoteForString(like: Boolean, string: String): Tag {
        def tag = Tag {
                    tagname: string
                }
        return this.addVoteForTag(like, tag);
    }

    public function joinSession(mucId:String) : Void {
        syncSession = tbi.getDataSyncService().joinSession(mucId, this, "socialtagging", false);
    }

    override public function syncObjectRemoved (syncObject : ISyncObject) : Void {
        if(not syncObject.getToolname().equals("socialtagging")) {
            return;
        }
        FX.deferAction(view.updateTagLines);
    }

    override public function syncObjectChanged (syncObject : ISyncObject) : Void {
        if(not syncObject.getToolname().equals("socialtagging")) {
            return;
        }
        FX.deferAction(view.updateTagLines);
    }

    override public function syncObjectAdded (syncObject : ISyncObject) : Void {
        if(not syncObject.getToolname().equals("socialtagging")) {
            return;
        }
        FX.deferAction(view.updateTagLines);
    }

}
