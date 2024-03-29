package eu.scy.agents.processguidanceservice;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.util.XMLUtils;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ELORun extends AbstractRun {

    public enum ActivityStatus {
        ENABLED,
        ACTIVATED,
        COMPLETED,
        NEED2CHECK;
    }

    private String title = null;

    private ELOModel associatedELOModel = null;

    private ActivityStatus activityStatus = ActivityStatus.ENABLED;

    // private String eloStatus = ENABLED;
    private long amountOfWork = 0;

    private long amountOfChangeWork = 0;

    private long executionTime = 0;

    private long changeTime = 0;

    private long startTime;

    private long lastAccessTime;

    private long finishedTime;

    public ELORun(TupleSpace commandSpace, TupleSpace guidanceSpace, String eloRunID) {
        super(commandSpace, guidanceSpace);
        this.id = eloRunID;
    }

    public ELORun(TupleSpace commandSpace, TupleSpace guidanceSpace, String id, String aTitle, ELOModel aELOModel, TupleID aTupleID, ActivityStatus actvityStatus, long amountOfWork, long amountOfChange, long executionTime, long modificationTime) {
        super(commandSpace, guidanceSpace);
        this.id = id;
        this.title = aTitle;
        this.associatedELOModel = aELOModel;
        this.myTupleID = aTupleID;
        this.activityStatus = actvityStatus;
        // this.eloStatus = eloStatus;
        this.amountOfWork = amountOfWork;
        this.amountOfChangeWork = amountOfChange;
        this.executionTime = executionTime;
        this.changeTime = modificationTime;
    }

    public ELOModel getELOModel() {
        return associatedELOModel;
    }

    public void setELOModel(ELOModel aELOModel) {
        associatedELOModel = aELOModel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String aTitle) {
        this.title = aTitle;
    }

    public ActivityStatus getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(ActivityStatus aStatus) {
        this.activityStatus = aStatus;
    }

    /*
     * public String getELOStatus() { return eloStatus; }
     * 
     * public void setELOStatus(String aStatus) { this.eloStatus = aStatus; }
     */
    public long getAmountOfWork() {
        return amountOfWork;
    }

    public void setAmountOfWork(long aWork) {
        this.amountOfWork = aWork;
    }

    public void addAmountOfWork(long aWork) {
        amountOfWork += aWork;
    }

    public long getAmountOfChangeWork() {
        return amountOfChangeWork;
    }

    public void setAmountOfChangeWork(long aChange) {
        this.amountOfChangeWork = aChange;
    }

    public void addAmountOfChangeWork(long aChange) {
        amountOfChangeWork += aChange;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long aTime) {
        this.executionTime = aTime;
    }

    public void addExecutionTime(long aTime) {
        executionTime += aTime;
    }

    public long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(long aTime) {
        this.changeTime = aTime;
    }

    public void addChangeTime(long aTime) {
        changeTime += aTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long aTime) {
        this.startTime = aTime;
    }

    public long getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(long aTime) {
        this.finishedTime = aTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long aTime) {
        this.lastAccessTime = aTime;
    }

    @Override
    public String toString() {
        return new String("ELORun id=" + id + ", title=" + getTitle() + ", modelID=" + associatedELOModel.getId() + ", activityStatus=" + activityStatus + // ", eloStatus="+eloStatus+
        ", amountOfWork=" + amountOfWork + ", amountOfChangeWork=" + amountOfChangeWork + ", executionTime=" + executionTime + ", changeTime=" + changeTime);
    }

    public void updateELOTuple(RunUser user) {
        try {
            guidanceSpace.update(myTupleID, new Tuple("elo", user.getId(), user.getMissionRun().getMissionModel().getId(), getELOModel().getId(), getId(), getTitle(), activityStatus.name(), // eloStatus,
            String.valueOf(amountOfWork), String.valueOf(amountOfChangeWork), String.valueOf(executionTime), String.valueOf(changeTime)));
        } catch (TupleSpaceException e) {
            ProcessGuidanceAgent.logger.info("Error in guidance TupleSpace while updating an eloRun");
        }
    }

    public void handleELORunTuple(RunUser user) {
        try {
            Tuple t = guidanceSpace.read(new Tuple("elo", user.getId(), user.getMissionRun().getMissionModel().getId(), getELOModel().getId(), getId(), getTitle(), Field.createWildCardField()));
            if (t != null) {
                this.myTupleID = t.getTupleID();
                String activityStatusStr = (String) t.getField(6).getValue();
                this.activityStatus = ActivityStatus.valueOf(activityStatusStr);
                // this.eloStatus = (String)t.getField(7).getValue();
                this.amountOfWork = Long.parseLong((String) t.getField(7).getValue());
                this.amountOfChangeWork = Long.parseLong((String) t.getField(8).getValue());
                this.executionTime = Long.parseLong((String) t.getField(9).getValue());
                this.changeTime = Long.parseLong((String) t.getField(10).getValue());
            } else {
                myTupleID = guidanceSpace.write(new Tuple("elo", user.getId(), user.getMissionRun().getMissionModel().getId(), getELOModel().getId(), getId(), getTitle(), activityStatus.name(),
                // eloStatus,
                String.valueOf(amountOfWork), String.valueOf(amountOfChangeWork), String.valueOf(executionTime), String.valueOf(changeTime)));
            }
        } catch (TupleSpaceException e) {
            ProcessGuidanceAgent.logger.info("Error in guidance TupleSpace while building an eloRun tuple");
        }
    }

    public void renameELORun(String elo_uri) {
        try {
            String elo_xml = loadELO(elo_uri);
            Document doc = XMLUtils.parseXMLString(elo_xml);
            XPath xPath = XPathFactory.newInstance().newXPath();
            String title = (String) xPath.evaluate("/elo/metadata/lom/general/title/string", doc, XPathConstants.STRING);
            setTitle(title);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    private void handleStartEvent(RunUser aUser, long time) {

	setActivityStatus(ELORun.ActivityStatus.ACTIVATED);
	aUser.sendMessageNotification("You start to working on the ELO titled \"" + this.getTitle() + "\".", time);
        aUser.sendStatusNotification(this, 0);
        updateELOTuple(aUser);
	
	ELOModel[] aModelList = getELOModel().getNotStartedDependedELOs(aUser.getMissionRun());
        ELORun[] aRunList = getELOModel().getIncompletedDependedELORuns(aUser.getMissionRun());

        if ((aModelList.length > 0) || (aRunList.length > 0)) {
            
            //boolean answer = aUser.sendConfirmation("Are you sure that you start to working on the \"" + this.getTitle() + "\"");
            //if (answer) {
                
                if (aModelList.length > 0) {
                    String message = new String(" This ELO depends on the ELO");
                    if (aModelList.length == 1) {
                        message += ": \"" + aModelList[0].getTitle() + "\" in the las: \"" + aModelList[0].getLASModel().getId();
                    } else if (aModelList.length == 2) {
                        message += "s: \"" + aModelList[0].getTitle() + "\" in the las: \"" + aModelList[0].getLASModel().getId() + "\" ";
                        message += "and \"" + aModelList[1].getTitle() + "\" in the las: \"" + aModelList[1].getLASModel().getId();

                    } else if (aModelList.length > 2) {
                        message += "s: \"";
                        for (int i = 0; i < aModelList.length - 1; i++) {
                            message += aModelList[i].getTitle() + "\" in the las: \"" + aModelList[i].getLASModel().getId() + "\", ";
                        }
                        message += "and \"" + aModelList[aModelList.length - 1].getTitle() + "\" in the las: \"" + aModelList[aModelList.length - 1].getLASModel().getId();
                    }
                    message += "\", which you have not started. You would better complete that activities before doing this activity.";
                    aUser.sendMessageNotification(message, time);
                }

                if (aRunList.length > 0) {
                    /*
                    String message = new String("This ELO depends on the ELO" + (aRunList.length > 1 ? "s " : " ") + ", which you might have not finished.");
                    aUser.sendMessageNotification(message, time);
                    
                    int count = 0;
                    for (int i = 0; i < aRunList.length; i++) {
                        answer = aUser.sendConfirmation("Are you sure that you have finished \"" + aRunList[i].getTitle() + "\" in the las: \"" + aRunList[i].getELOModel().getLASModel().getId() + "\"");
                        if (answer) {
                            // aRunList[i].setELOStatus(ELORun.COMPLETED);
                            aRunList[i].setActivityStatus(ELORun.ActivityStatus.COMPLETED);
                            aRunList[i].setChangeTime(0);
                            aRunList[i].setAmountOfChangeWork(0);
                            aRunList[i].updateELOTuple(aUser);
                            count++;
                            
                        }
                    }
                    if (count > 0) {aUser.sendMessageNotification("You would better work on the uncompleted activities before doing this activity.", time);
                }
                updateELOTuple(aUser);
            }
        }*/
                    
                    String message = new String("You start to working on the ELO titled \"" + this.getTitle() + "\". This ELO depends on the ELO");
                    if (aRunList.length == 1) {
                	aRunList[0].setActivityStatus(ELORun.ActivityStatus.NEED2CHECK);
                        aRunList[0].setChangeTime(0);
                        aRunList[0].setAmountOfChangeWork(0);
                        aRunList[0].updateELOTuple(aUser);
                        aUser.sendStatusNotification(aRunList[0], 0);
                        message += ": \"" + aRunList[0].getTitle() ;
                    } else if (aRunList.length == 2) {
                	aRunList[0].setActivityStatus(ELORun.ActivityStatus.NEED2CHECK);
                        aRunList[0].setChangeTime(0);
                        aRunList[0].setAmountOfChangeWork(0);
                        aRunList[0].updateELOTuple(aUser);
                        aUser.sendStatusNotification(aRunList[0], 0);
                        message += "s: \"" + aRunList[0].getTitle() + "\" ";
                	aRunList[1].setActivityStatus(ELORun.ActivityStatus.NEED2CHECK);
                        aRunList[1].setChangeTime(0);
                        aRunList[1].setAmountOfChangeWork(0);
                        aRunList[1].updateELOTuple(aUser);
                        aUser.sendStatusNotification(aRunList[1], 0);
                        message += "and \"" + aRunList[1].getTitle() ;

                    } else if (aRunList.length > 2) {
                        message += "s: \"";
                        for (int i = 0; i < aRunList.length - 1; i++) {
                            aRunList[i].setActivityStatus(ELORun.ActivityStatus.NEED2CHECK);
                            aRunList[i].setChangeTime(0);
                            aRunList[i].setAmountOfChangeWork(0);
                            aRunList[i].updateELOTuple(aUser);
                            aUser.sendStatusNotification(aRunList[i], 0);

                            message += aRunList[i].getTitle() + "\", ";
                        }
                        message += "and \"" + aRunList[aRunList.length - 1].getTitle();
                    }
                    message +=  "\", which haven't finished. You would better complete before doing this activity.";
                    aUser.sendMessageNotification(message, time);
                }
                
            //}
        }
    }

    private void handleModifyEvent(RunUser aUser, long time) {
        setActivityStatus(ELORun.ActivityStatus.ACTIVATED);
        aUser.sendMessageNotification("You have modified the ELO titled \"" + this.getTitle() + "\".", time);
        aUser.sendStatusNotification(this, 0);

        ELORun[] aRunList = getELOModel().getInfluencedCompletedELORuns(aUser.getMissionRun());
        if (aRunList.length > 0) {
            String message = " Such a change may influence on the";
            if (aRunList.length == 1) {
        	aRunList[0].setActivityStatus(ELORun.ActivityStatus.NEED2CHECK);
                aRunList[0].setChangeTime(0);
                aRunList[0].setAmountOfChangeWork(0);
                aRunList[0].updateELOTuple(aUser);
                aUser.sendStatusNotification(aRunList[0], time);

                message += " ELO titled \"" + aRunList[aRunList.length - 1].getTitle() + "\"";
            } else {
                message += " ELOs: ";
                int last = aRunList.length - 1;
                for (int i = 0; i < aRunList.length - 1; i++) {
                    aRunList[i].setActivityStatus(ELORun.ActivityStatus.NEED2CHECK);
                    aRunList[i].setChangeTime(0);
                    aRunList[i].setAmountOfChangeWork(0);
                    aRunList[i].updateELOTuple(aUser);
                    aUser.sendStatusNotification(aRunList[i], time);

                    message += "\"" + aRunList[i].getTitle() + "\", ";
                }
                aRunList[last].setActivityStatus(ELORun.ActivityStatus.NEED2CHECK);
                aRunList[last].setChangeTime(0);
                aRunList[last].setAmountOfChangeWork(0);
                aRunList[last].updateELOTuple(aUser);
                aUser.sendStatusNotification(aRunList[last], time);

                message += "and \"" + aRunList[last].getTitle() + "\"";
            }
            message +=  "\". Please chech whether the influenced ELO needs modifing accordingly.";
            aUser.sendMessageNotification(message, time);

            /*
             * boolean answer = aUser.sendConfirmation(prompt, aTime);
 
            if (answer) {
                for (int i = 0; i < aRunList.length; i++) {
                    aRunList[i].setActivityStatus(ActivityStatus.NEED2CHECK);
                    aRunList[i].setChangeTime(0);
                    aRunList[i].setAmountOfChangeWork(0);
                    aRunList[i].updateELOTuple(aUser);
                }
                setActivityStatus(ActivityStatus.ACTIVATED);
                updateELOTuple(aUser);
            }
             */
        }
    }

    public void handleCompleteEvent(RunUser aUser, long time) {
        setActivityStatus(ActivityStatus.COMPLETED);
        setFinishedTime(time);
        updateELOTuple(aUser);
        aUser.setFocusedELORun(this);
        aUser.provideGuidanceAfterComplete();
    }

    public void handleResumeEvent(RunUser aUser) {
        setActivityStatus(ActivityStatus.ACTIVATED);
        updateELOTuple(aUser);
    }

    public void handleApproveEvent(RunUser aUser, long time) {
        setActivityStatus(ActivityStatus.COMPLETED);
        setFinishedTime(time);
        updateELOTuple(aUser);
    }

    public void handleContentIncreased(RunUser aUser, long aLength, long aTime) {
        addAmountOfWork(aLength);
        addAmountOfChangeWork(aLength);
        addExecutionTime(3);
        addChangeTime(3);
        setLastAccessTime(aTime);
        aUser.setFocusedELORun(this);

        if ((getAmountOfChangeWork() > getELOModel().getChangeWorkThreshold()) && (getChangeTime() > getELOModel().getChangeTimeThreshold())) {

            if (getActivityStatus() == ActivityStatus.ENABLED) {
                handleStartEvent(aUser, aTime);
            } else if (getActivityStatus() == ActivityStatus.COMPLETED) {
                handleModifyEvent(aUser, aTime);
            }
        }
    }
    
    public void handleDataEdited(RunUser aUser, long aTime) {
        addAmountOfWork(1);
        addAmountOfChangeWork(1);
        //addExecutionTime(3);
        //addChangeTime(3);
        setLastAccessTime(aTime);
        aUser.setFocusedELORun(this);

        if (getAmountOfChangeWork() > getELOModel().getChangeWorkThreshold()) {

            if (getActivityStatus() == ActivityStatus.ENABLED) {
                handleStartEvent(aUser, aTime);
            } else if (getActivityStatus() == ActivityStatus.COMPLETED) {
                handleModifyEvent(aUser, aTime);
            }
        }
    }

    public void handleContentDecreased(RunUser aUser, long aLength, long aTime) {
        addAmountOfWork(-aLength);
        addAmountOfChangeWork(aLength);
        setLastAccessTime(aTime);
        updateELOTuple(aUser);
        aUser.setFocusedELORun(this);
    }

    private String getRelationNodeURI(RunUser aRunUser, Node aRelationNode) {
        NodeList aNodeLists = (NodeList) aRelationNode.getChildNodes();
        for (int j = 0; j < aNodeLists.getLength(); j++) { // kind and resource
            if (aNodeLists.item(j).getNodeName().equalsIgnoreCase("resource")) {
                NodeList aResourceNodeLists = (NodeList) aNodeLists.item(j).getChildNodes();
                for (int k = 0; k < aResourceNodeLists.getLength(); k++) {
                    if (aResourceNodeLists.item(k).getNodeName().equalsIgnoreCase("identifier")) {

                        NodeList aIdentiferNodeLists = (NodeList) ((Node) aResourceNodeLists.item(k)).getChildNodes();
                        for (int l = 0; l < aIdentiferNodeLists.getLength(); l++) {
                            if (aIdentiferNodeLists.item(l).getNodeName().equalsIgnoreCase("entry")) {
                                return ((Node) aIdentiferNodeLists.item(l)).getTextContent();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void buildELORun(RunUser aRunUser, String elo_uri) {
        try {
            String elo_xml = loadELO(elo_uri);
            if (elo_xml != null) {
                Document doc = XMLUtils.parseXMLString(elo_xml);
                XPath xPath = XPathFactory.newInstance().newXPath();
                if (title == null) {
                    title = (String) xPath.evaluate("/elo/metadata/lom/general/title/string", doc, XPathConstants.STRING);
                }
                NodeList relationNodes = (NodeList) xPath.evaluate("/elo/metadata/lom/relation", doc, XPathConstants.NODESET);
                relation: for (int i = 0; i < relationNodes.getLength(); i++) { // relation
                    Node relationNode = (Node) relationNodes.item(i);
                    /*
                     * String aValue = (String) xPath.evaluate("//kind/value", relationNode,
                     * XPathConstants.STRING); if ((aValue.equalsIgnoreCase("is_fork_of")) ||
                     * (aValue.equalsIgnoreCase("is_version_of"))) { String aURI = (String)
                     * xPath.evaluate("//resource/identifier/entry", relationNode,
                     * XPathConstants.STRING); buildELORun(aRunUser, aURI); return; }
                     */
                    NodeList aNodeLists = (NodeList) relationNode.getChildNodes();
                    for (int j = 0; j < aNodeLists.getLength(); j++) { // kind and resource
                        if (aNodeLists.item(j).getNodeName().equalsIgnoreCase("kind")) {
                            NodeList aKindNodeLists = (NodeList) aNodeLists.item(j).getChildNodes();
                            for (int k = 0; k < aKindNodeLists.getLength(); k++) {
                                if (aKindNodeLists.item(k).getNodeName().equalsIgnoreCase("value")) {

                                    if ((!aKindNodeLists.item(k).getTextContent().equalsIgnoreCase("is_fork_of")) && (!aKindNodeLists.item(k).getTextContent().equalsIgnoreCase("is_version_of"))) {
                                        continue relation;
                                    } else {
                                        String aURI = getRelationNodeURI(aRunUser, relationNode);
                                        if (aURI != null)
                                            buildELORun(aRunUser, aURI);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
                // it is assumed that the node is an elo model
                String aURI = (String) xPath.evaluate("/elo/metadata/lom/general/identifier/entry", doc, XPathConstants.STRING);
                ELOModel aELOModel = aRunUser.getMissionRun().getMissionModel().findELOModelByID(aURI);
                if (aELOModel != null) {
                    // a planned elo
                    setELOModel(aELOModel);
                    handleELORunTuple(aRunUser);
                } else {
                    // an unplanned elo, ignore
                }
            } else {
                System.out.println("no " + elo_uri + " in the repository");
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
}
