package eu.scy.agents.serviceprovider.ontology;

import java.io.File;
import java.io.PrintStream;
import java.util.Map;

import eu.scy.agents.impl.AbstractForeignAgent;

public class OntologyLookupAgent extends AbstractForeignAgent {

    public OntologyLookupAgent(@SuppressWarnings("unused") Map<String, Object> map) {
        super("Ontology Lookup Agent", (String) map.get("id"));
    }

    public static void main(String[] args) throws Exception {
        new OntologyLookupAgent(null).start();
    }

    @Override
    protected String[] getCommandline() {
        File exe = findExecutable("swipl", "swi-prolog", "pl", "plcon.exe");
        return new String[] { exe.getAbsolutePath(), "-f", "src" + File.separator + "main" + File.separator + "prolog" + File.separator + "ontology_agent.pl", "-g", "start_agent(onto, 2525)" };
    }

    @Override
    protected PrintStream routeErrTo() {
        return System.err;
    }

    @Override
    protected PrintStream routeOutTo() {
        return System.out;
    }

   
}
