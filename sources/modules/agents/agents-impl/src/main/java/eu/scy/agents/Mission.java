package eu.scy.agents;


public enum Mission {

    MISSION1("http://www.scy.eu/co2house#", "Design a CO2 friendly house", "mission1", "co2_2"),
    MISSION1_UT("http://www.scy.eu/co2house#", "Design a CO2 friendly house (UT version)", "mission1", "co2_2"),
    MISSION1_OSLO("http://www.scy.eu/co2house#", "Design a CO2 friendly house (Oslo version)", "mission1", "co2_2"),

    MISSION2("http://www.scy.eu/ecosystem#", "ECO mission", "mission2", "eco"),
    MISSION2_ESTONIAN("http://www.scy.eu/ecosystem#", "Ökoloogiamissioon", "mission2", "eco"),

    MISSION3("http://www.scy.eu/pizza#", "A Healthy Pizza", "mission3", "pizza2"),
    MISSION3_DUTCH("http://www.scy.eu/pizza#", "Een gezonde pizza", "mission3", "pizza2"),
    MISSION3_GREEK("http://www.scy.eu/pizza#", "Υγιεινή πίτσα", "mission3", "pizza2"),

    MISSION4("http://www.scy.eu/dna#", "Forensic mission", "mission4", "forensic"),
    MISSION4_FRENCH("http://www.scy.eu/dna#", "Le laboratoire de police scientifique", "mission4", "forensic"),
    GMO("http://www.scy.eu/dna#", "GMO mission", "mission4", "forensic"),

    MISSION5("http://www.scy.eu/dna#", "Run for your life!", "mission5", "mini1"),

    UNKNOWN_MISSION("", "", "", "");

    private String namespace;

    private String name;

    private String id;
    private String missionId;

    private Mission(String namespace, String name, String id, String missionId) {
        this.name = name;
        this.namespace = namespace;
        this.id = id;
        this.missionId = missionId;
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

    public String getMissionId() {
        return missionId;
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

    public static Mission getForMissionId(String missionId) {
        for (Mission mo : Mission.values()) {
            if (mo.getMissionId().equals(missionId)) {
                return mo;
            }
        }
        return Mission.UNKNOWN_MISSION;
    }
}
