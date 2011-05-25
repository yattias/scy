package eu.scy.actionlogging;

import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.api.IContextService;

public class ContextService implements IActionLogger, IContextService {

    private String username;

    private String missionSpecificationURI;

    private String currentLAS = "";

    private String session;

    private Map<String, Set<String>> currentlyOpenedELOs;

    public ContextService() {
        this.session = new VMID().toString();
        currentlyOpenedELOs = new HashMap<String, Set<String>>();
        currentlyOpenedELOs.put(currentLAS, new HashSet<String>());
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getMissionSpecificationURI() {
        return missionSpecificationURI;
    }

    public void setMissionSpecificationURI(String missionSpecificationURI) {
        this.missionSpecificationURI = missionSpecificationURI;
    }

    @Override
    public String getCurrentLAS() {
        return currentLAS;
    }

    @Override
    public String getSession() {
        return session;
    }

    @Override
    public Set<String> getCurrentlyOpenedELOs() {
        return currentlyOpenedELOs.get(currentLAS);
    }

    @Override
    public void log(IAction action) {
        if (action.getType().equals("las_changed")) {
            currentLAS = action.getAttributes().get("newLasId");
            if (!currentlyOpenedELOs.containsKey(currentLAS)) {
                currentlyOpenedELOs.put(currentLAS, new HashSet<String>());
            }
        } else if (action.getType().equals("elo_loaded")) {
            currentlyOpenedELOs.get(currentLAS).add(action.getContext(ContextConstants.eloURI));
        } else if (action.getType().equals("tool_closed")) {
            currentlyOpenedELOs.get(currentLAS).remove(action.getContext(ContextConstants.eloURI));
        }
    }

    @Override
    @Deprecated
    public void log(String username, String source, IAction action) {
        log(action);
    }
}
