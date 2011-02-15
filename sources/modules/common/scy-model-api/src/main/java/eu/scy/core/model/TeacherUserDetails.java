package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.feb.2010
 * Time: 07:15:27
 * To change this template use File | Settings | File Templates.
 */
public interface TeacherUserDetails extends UserDetails{

    ImageRef getProfilePicture();

    void setProfilePicture(ImageRef profilePicture);


}
