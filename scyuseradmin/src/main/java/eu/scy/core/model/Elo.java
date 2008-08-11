package eu.scy.core.model;

import javax.persistence.*;

import java.util.List;

/**
 * Created by Intermedia
 * User: Jeremy / Thomas
 * Date: 11.aug.2008
 * Time: 04:00:01
 * A attempt of an ELO
 */
@Entity
@Table (name = "elo")
public class Elo extends SCYBaseObject {

	private List<User> users;
    
	@OneToMany(targetEntity = User.class, mappedBy = "elo", cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
