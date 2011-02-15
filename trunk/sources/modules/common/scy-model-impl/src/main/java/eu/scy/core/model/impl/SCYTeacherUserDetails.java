package eu.scy.core.model.impl;

import eu.scy.core.model.ImageRef;
import eu.scy.core.model.TeacherUserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.feb.2010
 * Time: 07:16:05
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "teacher_user_details")
public class SCYTeacherUserDetails extends SCYUserDetails implements TeacherUserDetails, Serializable {

    private String city;
    private String country;
    private String [] curriculumsubjects;
    private String displayName;
    private String firstName;
    private String lastName;
    private Date lastLoginTime;
    private Integer numberOfLogins;
    private Integer schoolLevel;
    private String schoolName;
    private Date signupDate;
    private String state;
    private ImageRef profilePicture;

    @Column(name="city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column (name="country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name="curriculumsubjects")
    public String[] getCurriculumsubjects() {
        return curriculumsubjects;
    }

    public void setCurriculumsubjects(String[] curriculumsubjects) {
        this.curriculumsubjects = curriculumsubjects;
    }

    @Column(name="displayName")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Column (name="firstname")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column (name="lastname")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column (name="lastlogintime")
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Column(name="numberoflogins")
    public Integer getNumberOfLogins() {
        return numberOfLogins;
    }

    public void setNumberOfLogins(Integer numberOfLogins) {
        this.numberOfLogins = numberOfLogins;
    }

    @Column (name="schoollevel")
    public Integer getSchoolLevel() {
        return schoolLevel;
    }

    public void setSchoolLevel(Integer schoolLevel) {
        this.schoolLevel = schoolLevel;
    }

    @Column(name="schoolname")
    public String getSchoolName() {
        return schoolName;
    }


    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Column(name="signupdate")
    public Date getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(Date signupDate) {
        this.signupDate = signupDate;
    }

    @Column(name="state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    @OneToOne(targetEntity = ImageRefImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "profilePicture")
    public ImageRef getProfilePicture() {
        return profilePicture;
    }

    @Override
    public void setProfilePicture(ImageRef profilePicture) {
        this.profilePicture = profilePicture;
    }
}
