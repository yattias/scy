package eu.scy.agents;


public enum Mission {

    MISSION1("http://www.scy.eu/co2house#", "Design a CO2 friendly house", "mission1"),
    MISSION2("http://www.scy.eu/ecosystem#", "ECO mission", "mission2"),
    MISSION3("http://www.scy.eu/pizza#", "A Healthy Pizza", "mission3"),
    MISSION4("http://www.scy.eu/dna#", "DNA", "mission4"),
    UNKNOWN_MISSION("", "", "");

    private String namespace;

    private String name;

    private String id;

    private Mission(String namespace, String name, String id) {
        this.name = name;
        this.namespace = namespace;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getId() {
        return id;
    }

    public static Mission getForName(String name) {
        for (Mission mo : Mission.values()) {
            if (mo.getName().equals(name)) {
                return mo;
            }
        }
        return Mission.UNKNOWN_MISSION;
    }

    public static Mission getForId(String id) {
        for (Mission mo : Mission.values()) {
            if (mo.getId().equals(id)) {
                return mo;
            }
        }
        return Mission.UNKNOWN_MISSION;
    }
}
