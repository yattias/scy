package eu.scy.core.model.impl;

import eu.scy.core.model.ScyBase;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
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


}
