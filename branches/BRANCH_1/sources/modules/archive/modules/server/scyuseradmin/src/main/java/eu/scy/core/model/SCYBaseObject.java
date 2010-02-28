package eu.scy.core.model;

import org.apache.tapestry5.beaneditor.NonVisual;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.jun.2008
 * Time: 22:55:08
 * A base object for all (?) SCY data objects
 */
@MappedSuperclass
public abstract class SCYBaseObject implements Serializable {
    private String name = null;
    private String id = null;

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

    @NonVisual
    public void setId(String id) {
        this.id = id;
    }

    // NEED TO IMPLEMENT HASHCODDE AND EQUALS!
}