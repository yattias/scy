package eu.scy.agents.processguidanceservice;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.ArrayList;

import eu.scy.agents.processguidanceservice.ELORun.ActivityStatus;

public class MissionRun extends AbstractRun {

    // value domain of guidance levels
    public static final String HIGH = "high";

    public static final String MEDIUM = "medium";

    public static final String LOW = "low";

    public static final String NO = "no";

    private RunUser user;

    private MissionModel associatedMissionModel;

    private ArrayList<ELORun> eloRuns = new ArrayList<ELORun>();

    private String missionGuidanceLevel;

    public MissionRun(TupleSpace commandSpace, TupleSpace guidanceSpace, String id, RunUser aUser, MissionModel aMissionModel) {
        super(commandSpace, guidanceSpace);
        this.id = id;
        user = aUser;
        associatedMissionModel = aMissionModel;
    }

    public RunUser getUser() {
        return user;
    }

    public MissionModel getMissionModel() {
        return associatedMissionModel;
    }

    public String getGuidanceLevel() {
        return missionGuidanceLevel;
    }

    public void setGuidanceLevel(String aLevel) {
        missionGuidanceLevel = aLevel;
    }

    public String increaseGuidanceLevel() {
        if ((missionGuidanceLevel.equalsIgnoreCase(MEDIUM)) || (missionGuidanceLevel.equalsIgnoreCase(HIGH))) {
            missionGuidanceLevel = HIGH;
        } else if (missionGuidanceLevel.equalsIgnoreCase(LOW)) {
            missionGuidanceLevel = MEDIUM;
        } else if (missionGuidanceLevel.equalsIgnoreCase(NO)) {
            missionGuidanceLevel = LOW;
        }
        return missionGuidanceLevel;
    }

    public String decreaseGuidanceLevel() {
        if ((missionGuidanceLevel.equalsIgnoreCase(NO)) || (missionGuidanceLevel.equalsIgnoreCase(LOW))) {
            missionGuidanceLevel = NO;
        } else if (missionGuidanceLevel.equalsIgnoreCase(MEDIUM)) {
            missionGuidanceLevel = LOW;
        } else if (missionGuidanceLevel.equalsIgnoreCase(HIGH)) {
            missionGuidanceLevel = MEDIUM;
        }
        return missionGuidanceLevel;
    }

    public void addELORun(ELORun aELORun) {
        if ((aELORun != null) && (!eloRuns.contains(aELORun)))
            eloRuns.add(aELORun);
    }

    public void removeELORun(ELORun aELORun) {
        eloRuns.remove(aELORun);
    }

    public ELORun findELORunByURI(String aURI) {
        for (int i = 0; i < eloRuns.size(); i++) {
            if (aURI.equalsIgnoreCase(((ELORun) eloRuns.get(i)).getId())) {
                return (ELORun) eloRuns.get(i);
            }
        }
        return null;
    }

    public ELORun findELORunByELOModel(ELOModel aELOModel) {
        for (int i = 0; i < eloRuns.size(); i++) {
            if (aELOModel == ((ELORun) eloRuns.get(i)).getELOModel()) {
                return (ELORun) eloRuns.get(i);
            }
        }
        return null;
    }

    public void replaceELORunId(String old_uri, String elo_uri) {
        for (int i = 0; i < eloRuns.size(); i++) {
            if (old_uri.equalsIgnoreCase(((ELORun) eloRuns.get(i)).getId())) {
                ELORun aELORun = (ELORun) eloRuns.get(i);
                aELORun.setId(elo_uri);
            }
        }
    }

    public void replaceELORunIdAndName(String old_uri, String elo_uri) {
        for (int i = 0; i < eloRuns.size(); i++) {
            if (old_uri.equalsIgnoreCase(((ELORun) eloRuns.get(i)).getId())) {
                ELORun aELORun = (ELORun) eloRuns.get(i);
                aELORun.setId(elo_uri);
                aELORun.renameELORun(elo_uri);
            }
        }
    }

    @Override
    public String toString() {
        String result;
        result = new String("missionRunID=" + id + ", user name=" + user.getId() + ", mission name=" + associatedMissionModel.getName() + ", guidance level=" + missionGuidanceLevel + ", ELORuns: ");
        for (int i = 0; i < eloRuns.size(); i++) {
            result += eloRuns.get(i).toString() + new String("/ ");
        }

        return result;
    }

    public void updateMissionRunTuple() {
        try {
            guidanceSpace.update(myTupleID, new Tuple("mission", user.getId(), associatedMissionModel.getId(), id, user.getGeneralGuidanceLevel() /*
                                                                                                                                                   * ,
                                                                                                                                                   * start_time
                                                                                                                                                   */));
        } catch (TupleSpaceException e) {
            ProcessGuidanceAgent.logger.info("Error in guidance TupleSpace while read user info");
        }
    }

    public void buildMissionRun() {
        try {
            // check whether this user has run the mission before
            Tuple aMissionRunTuple = guidanceSpace.read(new Tuple("mission", user.getId(), associatedMissionModel.getId(), Field.createWildCardField()));
            if (aMissionRunTuple == null) {
                // the user hasn't run this mission
                // set the general guidance level as the guidance level of this mission
                setGuidanceLevel(user.getGeneralGuidanceLevel());
                myTupleID = guidanceSpace.write(new Tuple("mission", user.getId(), associatedMissionModel.getId(), id, user.getGeneralGuidanceLevel() /*
                                                                                                                                                       * ,
                                                                                                                                                       * start_time
                                                                                                                                                       */));

            } else {
                // the user has already run this mission
                myTupleID = aMissionRunTuple.getTupleID();
                setGuidanceLevel((String) aMissionRunTuple.getField(4).getValue());

                Tuple[] allELOsInMission = guidanceSpace.readAll(new Tuple("elo", user.getId(), associatedMissionModel.getId(), Field.createWildCardField()));

                for (Tuple t : allELOsInMission) {
                    // build eloRuns
                    String eloModelID = (String) t.getField(3).getValue();
                    ELOModel aELOModel = associatedMissionModel.findELOModelByID(eloModelID);
                    String eloRunID = (String) t.getField(4).getValue();
                    String eloRunTitle = (String) t.getField(5).getValue();
                    String activityStatusStr = (String) t.getField(6).getValue();
                    ELORun anELORun = new ELORun(commandSpace, guidanceSpace, eloRunID, eloRunTitle, aELOModel, t.getTupleID(), ActivityStatus.valueOf(activityStatusStr),
                    // (String)t.getField(7).getValue(),
                    Long.parseLong((String) t.getField(7).getValue()), Long.parseLong((String) t.getField(8).getValue()), Long.parseLong((String) t.getField(9).getValue()), Long.parseLong((String) t.getField(10).getValue()));
                    addELORun(anELORun);
                }
            }
        } catch (TupleSpaceException e) {
            ProcessGuidanceAgent.logger.info("Error in guidance TupleSpace while read all elo runs");
        }
    }

}
