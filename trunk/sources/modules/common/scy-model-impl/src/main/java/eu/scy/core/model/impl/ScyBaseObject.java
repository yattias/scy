package eu.scy.core.model.impl;

import eu.scy.core.model.ScyBase;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 05:59:39
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public class ScyBaseObject implements ScyBase, Serializable {

    private String name = null;
    private String id = null;
    private String description = null;

    private Long timeCreated;

    public ScyBaseObject() {
        timeCreated = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
	@GeneratedValue(generator = "nboid")
	@org.hibernate.annotations.GenericGenerator(name = "nboid", strategy = "uuid")
	@Column(name = "primKey", length = 55)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimeCreated(Long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScyBaseObject)) return false;

        ScyBaseObject that = (ScyBaseObject) o;

        if (timeCreated != null ? !timeCreated.equals(that.timeCreated) : that.timeCreated != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return timeCreated != null ? timeCreated.hashCode() : 0;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScyBaseObject)) return false;

        ScyBaseObject that = (ScyBaseObject) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 29 * result + timeCreated.hashCode();
        return result;
    }
    */

    @Override
    public String toString() {
        return name;
    }
}
