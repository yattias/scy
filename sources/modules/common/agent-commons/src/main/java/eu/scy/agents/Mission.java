package eu.scy.agents;


public enum Mission {

    MISSION1("http://www.scy.eu/co2house#", "Design a CO2 friendly house"), MISSION2("http://www.scy.eu/ecosystem#", "ECO mission"), MISSION3("http://www.scy.eu/pizza#", "A Healthy Pizza"), MISSION4("http://www.scy.eu/dna#", "DNA"), UNKNOWN_MISSION("", "");;
    
    private String namespace;
    
    private String name;
    
    private Mission(String namespace, String name) {
        this.name = name;
        this.namespace = namespace;
    }
    
    public String getName() {
        return name;
    }
    
    public String getNamespace() {
        return namespace;
    }

    public static Mission getForName(String name) {
        for (Mission mo : Mission.values()) {
            if (mo.getName().equals(name)) {
                return mo;
            }
        }
        return Mission.UNKNOWN_MISSION;
    }
    
}
