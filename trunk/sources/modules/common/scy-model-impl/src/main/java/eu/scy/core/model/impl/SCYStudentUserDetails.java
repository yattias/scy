package eu.scy.core.model.impl;

import eu.scy.core.model.StudentUserDetails;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 04.feb.2010
 * Time: 06:56:37
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "student_user_details")
public class SCYStudentUserDetails extends SCYUserDetails implements StudentUserDetails {

    private String firstname;
    private String lastname;
    private Integer gender;
    private Date birthday;
    private Date lastLoginTime;
    private Date signupdate;
    private String accountQuestion;
    private String accountAnswer;
    private Integer numberOfLogins;

    @Override
    public String getFirstname() {
        return firstname;
    }

    @Override
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    @Override
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public Integer getGender() {
        return gender;
    }

    @Override
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public Date getBirthday() {
        return birthday;
    }

    @Override
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    @Override
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String getAccountQuestion() {
        return accountQuestion;
    }

    @Override
    public void setAccountQuestion(String accountQuestion) {
        this.accountQuestion = accountQuestion;
    }

    @Override
    public String getAccountAnswer() {
        return accountAnswer;
    }

    @Override
    public void setAccountAnswer(String accountAnswer) {
        this.accountAnswer = accountAnswer;
    }

    @Override
    public Integer getNumberOfLogins() {
        return numberOfLogins;
    }

    @Override
    public void setNumberOfLogins(Integer numberOfLogins) {
        this.numberOfLogins = numberOfLogins;
    }

    @Override
    public Date getSignupdate() {
        return signupdate;
    }

    @Override
    public void setSignupdate(Date signupdate) {
        this.signupdate = signupdate;
    }
}
