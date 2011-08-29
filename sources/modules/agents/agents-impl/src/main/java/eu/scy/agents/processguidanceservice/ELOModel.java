package eu.scy.agents.processguidanceservice;

import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.util.XMLUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ELOModel extends AbstractGuidanceObject {

    // value domain of relation
    public static final String DEPENDING = "depending";

    public static final String DEPENDED = "depended";

    public static final String PRECEDING = "preceding";

    public static final String SUCCEDING = "succeding";

    private String title;

    private String type;

    private LASModel lasModel; // in which the ELO is located,

    // private int phase;

    // define fields for judging the work state,
    private long estimatedAmountOfWork = 0; // expert estimates

    // private long averageAmountOfWork = 300; //calculated
    private long changeWorkThreshold = 0; // expert estimates

    private long estimatedExecutionTime = 0; // expert estimates

    // private long averageExecutionTime = 15; //calculated
    private long changeTimeThreshold = 0; // expert estimates

    private double workRateThreshold = 0; // expert estimates

    private double timeRateThreshold = 0;// expert estimates

    private ELOModel[] dependingELOs = new ELOModel[0];

    private ELOModel[] dependedELOs = new ELOModel[0];

    private ELOModel[] precedingELOs = new ELOModel[0];

    private ELOModel[] succedingELOs = new ELOModel[0];

    public ELOModel(LASModel aLasModel) {
        lasModel = aLasModel;
    }

    public ELOModel(String id, String aTitle, String aType) {
        this.id = id;
        this.title = aTitle;
        this.type = aType;
    }

    public void buildELOModel(String id) {
        this.id = id;
        try {
            String elo_xml = loadELO(id);
            Document doc = XMLUtils.parseString(elo_xml);
            XPath xPath = XPathFactory.newInstance().newXPath();
            this.title = (String) xPath.evaluate("/elo/metadata/lom/general/title/string", doc, XPathConstants.STRING);
            this.type = (String) xPath.evaluate("/elo/metadata/lom/technical/format", doc, XPathConstants.STRING);
            if (type.equalsIgnoreCase("scy/rtf")) {
                setThresholds(300, 100, 15, 9, 0.9, 0.9); // the values are set for test
            } else if (type.equalsIgnoreCase("scy/mapping")) {
                setThresholds(5, 2, 15, 9, 0.9, 0.9); // the values are set for test
            } // else if ()
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String aTitle) {
        this.title = aTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String aType) {
        this.type = aType;
    }

    public LASModel getLASModel() {
        return lasModel;
    }

    public void setLASModel(LASModel aModel) {
        this.lasModel = aModel;
    }

    public void setThresholds(long estimatedAmountOfWork, long changeWorkThreshold, long estimatedExecutionTime, long changeTimeThreshold, double workRateThreshold, double timeRateThreshold) {
        this.estimatedAmountOfWork = estimatedAmountOfWork;
        this.changeWorkThreshold = changeWorkThreshold;
        this.estimatedExecutionTime = estimatedExecutionTime;
        this.changeTimeThreshold = changeTimeThreshold;
        this.workRateThreshold = workRateThreshold;
        this.timeRateThreshold = timeRateThreshold;
    }

    public long getEstimatedAmountOfWork() {
        return estimatedAmountOfWork;
    }

    public void setEstimatedAmountOfWork(long aWork) {
        this.estimatedAmountOfWork = aWork;
    }

    public long getChangeWorkThreshold() {
        return changeWorkThreshold;
    }

    public void setChangeWorkThreshold(long aWork) {
        this.changeWorkThreshold = aWork;
    }

    public long getEstimatedExecutionTime() {
        return estimatedExecutionTime;
    }

    public void setEstimatedExecutionTime(long aTime) {
        this.estimatedExecutionTime = aTime;
    }

    public long getChangeTimeThreshold() {
        return changeTimeThreshold;
    }

    public void setChangeTimeThreshold(long aTime) {
        this.changeTimeThreshold = aTime;
    }

    public double getWorkRateThreshold() {
        return workRateThreshold;
    }

    public void setWorkRateThreshold(double aThreshold) {
        this.workRateThreshold = aThreshold;
    }

    public double getTimeRateThreshold() {
        return timeRateThreshold;
    }

    public void setTimeRateThreshold(double aThreshold) {
        this.timeRateThreshold = aThreshold;
    }

    public ELOModel[] getDependingELOs() {
        return dependingELOs;
    }

    public ELOModel[] getDependedELOs() {
        return dependedELOs;
    }

    public ELOModel[] getPrecedingELOs() {
        return precedingELOs;
    }

    public ELOModel[] getSuccedingELOs() {
        return succedingELOs;
    }

    public void addDependingELO(ELOModel elo) {
        ELOModel[] newELOModels = (ELOModel[]) Arrays.copyOf(dependingELOs, dependingELOs.length + 1);
        newELOModels[newELOModels.length - 1] = elo;
        dependingELOs = newELOModels;
    }

    public void addDependedELO(ELOModel elo) {
        ELOModel[] newELOModels = (ELOModel[]) Arrays.copyOf(dependedELOs, dependedELOs.length + 1);
        newELOModels[newELOModels.length - 1] = elo;
        dependedELOs = newELOModels;
    }

    public void addPrecedingELO(ELOModel elo) {
        ELOModel[] newELOModels = (ELOModel[]) Arrays.copyOf(precedingELOs, precedingELOs.length + 1);
        newELOModels[newELOModels.length - 1] = elo;
        precedingELOs = newELOModels;
    }

    public void addSuccedingELO(ELOModel elo) {
        ELOModel[] newELOModels = (ELOModel[]) Arrays.copyOf(succedingELOs, succedingELOs.length + 1);
        newELOModels[newELOModels.length - 1] = elo;
        succedingELOs = newELOModels;
    }

    public ELOModel[] getUpsteamDependedELOs() {
        ArrayList<ELOModel> openList = new ArrayList<ELOModel>();
        for (int i = 0; i < dependedELOs.length; i++) {
            if (!openList.contains(dependedELOs[i])) {
                openList.add(dependedELOs[i]);
                getReachedELOs(openList, dependedELOs[i], DEPENDED);
            }
        }
        return (ELOModel[]) openList.toArray(new ELOModel[openList.size()]);
    }

    public ELOModel[] getUpsteamPrecedingELOs() {
        ArrayList<ELOModel> openList = new ArrayList<ELOModel>();
        for (int i = 0; i < precedingELOs.length; i++) {
            if (!openList.contains(precedingELOs[i])) {
                openList.add(precedingELOs[i]);
                getReachedELOs(openList, precedingELOs[i], PRECEDING);
            }
        }
        return (ELOModel[]) openList.toArray(new ELOModel[openList.size()]);
    }

    public ELOModel[] getDownsteamDependingELOs() {
        ArrayList<ELOModel> openList = new ArrayList<ELOModel>();
        for (int i = 0; i < dependingELOs.length; i++) {
            if (!openList.contains(dependingELOs[i])) {
                openList.add(dependingELOs[i]);
                getReachedELOs(openList, dependingELOs[i], DEPENDING);
            }
        }
        return (ELOModel[]) openList.toArray(new ELOModel[openList.size()]);
    }

    public ELOModel[] getDownsteamSuccedingELOs() {
        ArrayList<ELOModel> openList = new ArrayList<ELOModel>();
        for (int i = 0; i < succedingELOs.length; i++) {
            if (!openList.contains(succedingELOs[i])) {
                openList.add(succedingELOs[i]);
                getReachedELOs(openList, succedingELOs[i], SUCCEDING);
            }
        }
        return (ELOModel[]) openList.toArray(new ELOModel[openList.size()]);
    }

    public ELOModel[] getNotStartedDependedELOs(MissionRun aMissionRun) {
        ArrayList<ELOModel> openList = new ArrayList<ELOModel>();
        ELOModel[] aELOModelList = getUpsteamDependedELOs();
        for (int i = 0; i < aELOModelList.length; i++) {
            ELORun aELORun = aMissionRun.findELORunByELOModel(aELOModelList[i]);
            if (aELORun == null) {
                openList.add(aELOModelList[i]);
            }
        }
        return (ELOModel[]) openList.toArray(new ELOModel[openList.size()]);
    }

    public ELORun[] getIncompletedDependedELORuns(MissionRun aMissionRun) {
        ArrayList<ELORun> openList = new ArrayList<ELORun>();
        ELOModel[] aELOModelList = getUpsteamDependedELOs();
        for (int i = 0; i < aELOModelList.length; i++) {
            ELORun aELORun = aMissionRun.findELORunByELOModel(aELOModelList[i]);
            if ((aELORun != null) && (!aELORun.getActivityStatus().equalsIgnoreCase(ELORun.COMPLETED))) {
                openList.add(aELORun);
            }
        }
        return (ELORun[]) openList.toArray(new ELORun[openList.size()]);
    }

    public ELORun[] getInfluencedCompletedELORuns(MissionRun aMissionRun) {
        ArrayList<ELORun> openList = new ArrayList<ELORun>();
        ELOModel[] aELOModelList = getDownsteamDependingELOs();
        for (int i = 0; i < aELOModelList.length; i++) {
            ELORun aELORun = aMissionRun.findELORunByELOModel(aELOModelList[i]);
            if (aELORun != null && aELORun.getActivityStatus().equalsIgnoreCase(ELORun.COMPLETED)) {
                openList.add(aELORun);
            }
        }
        return (ELORun[]) openList.toArray(new ELORun[openList.size()]);
    }

    private void getReachedELOs(ArrayList<ELOModel> openList, ELOModel elo, String type) {

        if (type.equalsIgnoreCase(DEPENDING)) {
            for (int i = 0; i < elo.getDependingELOs().length; i++) {
                openList.add(elo.getDependingELOs()[i]);
                getReachedELOs(openList, elo.getDependingELOs()[i], DEPENDING);
            }
        } else if (type.equalsIgnoreCase(DEPENDED)) {
            for (int i = 0; i < elo.getDependedELOs().length; i++) {
                openList.add(elo.getDependedELOs()[i]);
                getReachedELOs(openList, elo.getDependedELOs()[i], DEPENDED);
            }
        } else if (type.equalsIgnoreCase(PRECEDING)) {
            for (int i = 0; i < elo.getPrecedingELOs().length; i++) {
                openList.add(elo.getPrecedingELOs()[i]);
                getReachedELOs(openList, elo.getPrecedingELOs()[i], PRECEDING);
            }
        } else if (type.equalsIgnoreCase(SUCCEDING)) {
            for (int i = 0; i < elo.getSuccedingELOs().length; i++) {
                openList.add(elo.getSuccedingELOs()[i]);
                getReachedELOs(openList, elo.getSuccedingELOs()[i], SUCCEDING);
            }
        } else {
            // no another type
        }
        return;
    }

    @Override
    public String toString() {
        String result = new String("ELOModel id=" + getCode() + ", title=" + title + ", type=" + type + ", inLAS=" + lasModel.getId() + "/ ");

        if (dependingELOs.length > 0) {
            result += new String("dependingELOs: ");
            for (int i = 0; i < dependingELOs.length; i++) {
                result += dependingELOs[i].getCode() + new String("/ ");
            }
            if (dependedELOs.length > 0) {
                result += new String("dependedELOs: ");
                for (int i = 0; i < dependedELOs.length; i++) {
                    result += dependedELOs[i].getCode() + new String("/ ");
                }
            }
        }
        if (precedingELOs.length > 0) {
            result += new String("precedingELOs: ");
            for (int i = 0; i < precedingELOs.length; i++) {
                result += precedingELOs[i].getCode() + new String("/ ");
            }
        }
        if (succedingELOs.length > 0) {
            result += new String("succedingELOs: ");
            for (int i = 0; i < succedingELOs.length; i++) {
                result += succedingELOs[i].getCode() + new String("/ ");
            }
        }
        return result;
    }

    public String getAnUnfinishedAncestor(RunUser aRunUser) {
        // check all Ancestors
        ELOModel[] eloModels = getPrecedingELOs();
        for (int i = 0; i < eloModels.length; i++) {
            ELOModel aELOModel = eloModels[i];
            String aTitle = aELOModel.getAnUnfinishedAncestor(aRunUser);
            if (aTitle != null)
                return aTitle;
        }
        // check the node itself
        ELORun aELORun = aRunUser.getMissionRun().findELORunByELOModel(this);
        if (aELORun == null) {
            // the ELO has not started
            return new String("you would better work on the ELO: \"" + this.getTitle() + "\" in the LAS: \"" + this.getLASModel().getId() + "\"");
        } else {
            if (aELORun.getActivityStatus() != ELORun.COMPLETED) {
                return new String("you would better work on the ELO: \"" + aELORun.getTitle() + "\" in the LAS: \"" + this.getLASModel().getId() + "\"");
            } else {
                return null;
            }
        }
    }

    public String getAnUnfinishedDescendant(RunUser aRunUser) {
        String aTitle = null;
        // check the SuccedingELOs and their ancestors
        ELOModel[] succedingELOModels = (getSuccedingELOs());
        for (int i = 0; i < succedingELOModels.length; i++) {
            ELOModel aSuccedingELOModel = succedingELOModels[i];
            aTitle = aSuccedingELOModel.getAnUnfinishedAncestor(aRunUser);
            if (aTitle != null) {
                return aTitle;
            }
        }
        // check descendants further
        for (int i = 0; i < succedingELOModels.length; i++) {
            ELOModel aSuccedingELOModel = succedingELOModels[i];
            aTitle = aSuccedingELOModel.getAnUnfinishedDescendant(aRunUser);
            if (aTitle != null) {
                return aTitle;
            }
        }
        // all finished
        return null;
    }

}
