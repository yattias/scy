package eu.scy.agents;


public enum Mission {

    MISSION1("http://www.scy.eu/co2house#", "Design a CO2 friendly house", "mission1"),
    MISSION1_UT("http://www.scy.eu/co2house#", "Design a CO2 friendly house (UT version)", "mission1"),
    MISSION1_OSLO("http://www.scy.eu/co2house#", "Design a CO2 friendly house (Oslo version)", "mission1"),

    MISSION2("http://www.scy.eu/ecosystem#", "ECO mission", "mission2"),

    MISSION3("http://www.scy.eu/pizza#", "A Healthy Pizza", "mission3"),
    MISSION3_DUTCH("http://www.scy.eu/pizza#", "Een gezonde pizza", "mission3"),
    MISSION3_GREEK("http://www.scy.eu/pizza#", "Υγιεινή πίτσα", "mission3"),

    MISSION4("http://www.scy.eu/dna#", "Forensic mission", "mission4"),
    MISSION4_FRENCH("http://www.scy.eu/dna#", "Le laboratoire de police scientifique", "mission4"),
    GMO("http://www.scy.eu/dna#", "GMO mission", "mission4"),

    MISSION5("http://www.scy.eu/dna#", "Run for your life!", "mission5"),

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
            if (mo.getName().equalsIgnoreCase(name)) {
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
