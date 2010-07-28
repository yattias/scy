package eu.scy.agents.impl;

import java.util.Map;

/**
 * This class is used for creating spring beans that represent a certain agent configuration.
 * 
 * @author weinbrenner
 * 
 */
public class AgentStartConfiguration {

    public AgentStartConfiguration(String className, Map<String, Object> properties) {
        this.className = className;
        this.properties = properties;
    }

    private String className;

    private Map<String, Object> properties;

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className
     *            the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the properties
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * @param properties
     *            the properties to set
     */
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

}
