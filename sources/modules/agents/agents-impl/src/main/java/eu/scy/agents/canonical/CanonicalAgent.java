package eu.scy.agents.canonical;

import java.util.Map;

import eu.scy.agents.impl.PrologAgent;

public class CanonicalAgent extends PrologAgent {

    public CanonicalAgent(Map<String, Object> map) {
        super("Canonical Agent", (String) map.get("id"), "canonical_agent.pl", "canonical_agent");
    }

}
