/*
 * Created on 16.09.2011
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package eu.scy.agents.groupformation.strategies.features;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import eu.scy.agents.impl.EloTypes;
import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.common.scyelo.ScyElo;

import roolo.elo.api.IELO;

public class HypothesisFeatureExtractor extends AbstractFeatureExtractor{

    @Override
    public Map<String, double[]> getFeatures(Set<String> availableUsers, String mission, IELO elo) {
        Map<String, double[]> results = new LinkedHashMap<String, double[]>();
        for (String user : availableUsers) {
            IELO userELO = retrieveEloFromRepository(user, EloTypes.SCY_XPROC); // TODO: check
                                                                                // return value if
                                                                                // no elo is found!
            if (userELO == null) {
                userELO = retrieveEloFromRepository(user, EloTypes.SCY_RICHTEXT);
                if (userELO != null) {
                    if (((new ScyElo(userELO, repository)).getFunctionalRole()) != EloFunctionalRole.HYPOTHESIS) {
                        userELO = null;
                    }
                }
            }
            results.put(user, this.getFeatures(user, mission, elo, userELO));
        }
        return results;
    }

     private boolean isRichtextElo(ScyElo elo) {
        String technicalFormat = elo.getTechnicalFormat();
        if (EloTypes.SCY_RICHTEXT.equals(technicalFormat)) {
            if (elo.getFunctionalRole() == EloFunctionalRole.HYPOTHESIS) {
                return true;
            }
        }
        return false;
    }

    private boolean isScyEdElo(ScyElo elo) {
        String technicalFormat = elo.getTechnicalFormat();
        if (EloTypes.SCY_XPROC.equals(technicalFormat)) {
            return true;
        }
        return false;
    }

    @Override
       public boolean canRun(IELO elo) {
        return isRichtextElo(new ScyElo(elo, repository)) || isScyEdElo(new ScyElo(elo, repository));
    }

    @Override
    protected double[] getFeatures(String user, String mission, IELO elo, IELO userELO) {
        // TODO Auto-generated method stub
        return null;
    }

}
