package eu.scy.core.model.impl;

import eu.scy.core.model.SCYGroup;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:00:11
 */
@Entity
@DiscriminatorValue(value = "SCYGroup")
public class SCYGroupImpl extends GroupImpl implements SCYGroup {

    private SCYGroup parent;

    @Override
    @ManyToOne(targetEntity = SCYGroupImpl.class, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent_fk")
    public SCYGroup getParent() {
        return parent;
    }

    @Override
    public void setParent(SCYGroup parent) {
        this.parent = parent;
    }
}
