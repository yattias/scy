package eu.scy.core.model;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 04.feb.2010
 * Time: 06:56:04
 * To change this template use File | Settings | File Templates.
 */
public interface StudentUserDetails extends UserDetails{
    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastname);

    Integer getGender();

    void setGender(Integer gender);

    Date getBirthday();

    void setBirthday(Date birthday);

    Date getLastLoginTime();

    void setLastLoginTime(Date lastLoginTime);

    String getAccountQuestion();

    void setAccountQuestion(String accountQuestion);

    String getAccountAnswer();

    void setAccountAnswer(String accountAnswer);

    Integer getNumberOfLogins();

    void setNumberOfLogins(Integer numberOfLogins);

    Date getSignupdate();

    void setSignupdate(Date signupdate);

    ImageRef getProfilePicture();

    void setProfilePicture(ImageRef profilePicture);
}
