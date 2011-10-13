/*
 * Created on 16.09.2011
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package eu.scy.agents.groupformation.strategies.features;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.scy.agents.impl.EloTypes;
import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.common.scyelo.ScyElo;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.KeyValuePair;

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
        ScyElo userScyElo = new ScyElo(userELO, repository);
        IMetadataKey hypoValueKey = repository.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.HYPO_VALUE.getId());
        IMetadataValueContainer hypoValueContainer = userELO.getMetadata().getMetadataValueContainer(hypoValueKey);
        List<String> valueList = (List<String>) hypoValueContainer.getValueList();
        double[] result = new double[4];
        for (Iterator<String> pairIt = valueList.iterator(); pairIt.hasNext();) {
            String pair = pairIt.next().toString();
            String[] split = pair.split(",");
            int index = Integer.valueOf(split[0].replaceAll("\\(", ""));
            if (index < result.length){
            double value = Double.valueOf(split[1].replaceAll("\\)", ""));
            result[index] = value;
            }
        }
        return result;
    }

}
