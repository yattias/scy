package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.BaseObject;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 15:05:38
 */
public class BaseObjectImpl  implements BaseObject {
    private String name = null;
    private String description = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return getName();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
