package eu.scy.agents.processguidanceservice;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.util.XMLUtils;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MissionModel extends AbstractGuidanceObject {

    private String name;

    private String pedagogicalScenario;

    private LASModel[] lasModels = new LASModel[0];

    private ELOModel[] eloModels = new ELOModel[0];

    public MissionModel(TupleSpace commandSpace, TupleSpace guidanceSpace, String missionID, String missionName) {
	super(commandSpace, guidanceSpace);
	id = missionID;
	name = missionName;
    }

    public String getName() {
	return name;
    }

    public void setName(String aName) {
	name = aName;
    }

    public String getPedagogicalModel() {
	return pedagogicalScenario;
    }

    public void addELOModel(ELOModel eloModel) {
	ELOModel[] newELOModels = (ELOModel[]) Arrays.copyOf(eloModels, eloModels.length + 1);
	newELOModels[newELOModels.length - 1] = eloModel;
	eloModels = newELOModels;
    }

    public ELOModel findELOModelByID(String eloID) {
	for (int i = 0; i < eloModels.length; i++) {
	    if (eloID.equalsIgnoreCase(eloModels[i].getId())) {
		return eloModels[i];
	    }
	}
	return null;
    }

    public void addLASModel(LASModel lasModel) {
	LASModel[] newLASModels = (LASModel[]) Arrays.copyOf(lasModels, lasModels.length + 1);
	newLASModels[newLASModels.length - 1] = lasModel;
	lasModels = newLASModels;

    }

    public LASModel findLASModelByID(String lasID) {
	for (int i = 0; i < lasModels.length; i++) {
	    if (lasID.equalsIgnoreCase(lasModels[i].getId())) {
		return lasModels[i];
	    }
	}
	return null;
    }

    @Override
    public String toString() {
	String result;

	result = new String("missionID=" + getCode() + ", name=" + name + "\n\n");
	for (int i = 0; i < lasModels.length; i++) {
	    result += lasModels[i].toString() + new String("\n");
	}

	/*
	 * result = new String("missionModel id=" + getCode() + ", name=" + name + ", ELOs: "+"\n "); for (int i = 0; i < eloModels.length; i++) { result += eloModels[i].toString() + new String("\n "); }
	 */
	return result;
    }

    public void buildMissionModel() {
	try {
	    String elo_xml = loadELO(id);
	    Document doc = XMLUtils.parseString(elo_xml);
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    String model_URI = (String) xPath.evaluate("/elo/content/missionSpeicification/missionMapModelEloUri", doc, XPathConstants.STRING);

	    elo_xml = loadELO(model_URI); // missionMapModelEloUri
	    doc = XMLUtils.parseString(elo_xml);
	    xPath = XPathFactory.newInstance().newXPath();

	    NodeList lasNodes = (NodeList) xPath.evaluate("/elo/content/missionModel/lasses/las", doc, XPathConstants.NODESET);
	    for (int i = 0; i < lasNodes.getLength(); i++) {
		LASModel aLASModel = new LASModel(commandSpace, guidanceSpace, this);
		this.addLASModel(aLASModel);
		aLASModel.buildLASModel((Node) lasNodes.item(i));
	    }
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

    public void buildPizzaMission() {
	LASModel aLAS = new LASModel(commandSpace, guidanceSpace, "design", "DESIGN");
	ELOModel aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/240.240#0", "My Optimized Healthy Pizza", "scy/simconfig");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/238.238#0", "My Favourite Pizza", "scy/simconfig");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/239.239#0", "My First Healthy Pizza", "scy/simconfig");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "experiment", "EXPERIMENT");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/257.257#0", "My Health Passport", "scy/resultcard");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/205.205#0", "Food And Exercise Diary", "scy/pds");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/206.206#0", "Daily Calorie Intake", "scy/pds");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	/*
	 * aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/232.232#0", "Evaluate Your Diet (Health Passport)", "scy/rtf"); aLAS.addIntermediateELOModel(aELO); aELO.setLASModel(aLAS); addELOModel(aELO); aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/233.233#0", "Basal Metabolic Rate (Health Passport)", "scy/rtf"); aLAS.addIntermediateELOModel(aELO); aELO.setLASModel(aLAS); addELOModel(aELO); aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/234.234#0", "Body Mass Index (Health Passport)", "scy/rtf"); aLAS.addIntermediateELOModel(aELO); aELO.setLASModel(aLAS); addELOModel(aELO); aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/235.235#0", "Heart Rate (Health Passport)", "scy/rtf"); aLAS.addIntermediateELOModel(aELO); aELO.setLASModel(aLAS); addELOModel(aELO);
	 */

	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/209.209#0", "Estimated Energy Requirements (Health Passport)", "scy/pds");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "information", "INFORMATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/214.214#0", "Questions About Pizza Benefits", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/213.213#0", "Notes On Unhealthy Diet", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "conceptualization1", "CONCEPTUALISATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/258.258#0", "Nutrient And Energy Calculations", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/215.215#0", "Nutrition Table", "scy/pds");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/217.217#0", "Pizza Ingredient Table", "scy/pds");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "conceptualization2", "CONCEPTUALISATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/219.219#0", "Construction Of The Food Pyramid", "scy/model");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/218.218#0", "Questions About The Food Pyramid", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "conceptualization3", "CONCEPTUALISATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/220.220#0", "Energy Fact Sheet", "scy/mapping");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "conceptualization4", "CONCEPTUALISATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/255.255#0", "mapOfDigestiveSystem", "scy/drawing");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/222.222#0", "Fact Sheet Of One Organ", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "reflection1", "REFLECTION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/223.223#0", "Personal Comments", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "reflection2", "REFLECTION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/228.228#0", "Criteria Final Table", "scy/pds");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/224.224#0", "Methodology Steps", "scy/xproc");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/225.225#0", "Reflection On Importance Of Criteria", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/226.226#0", "Criteria Table", "scy/pds");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/227.227#0", "Criteria Weight Table", "scy/pds");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "evaluation", "EVALUATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/232.232#0", "Letter To School Canteen", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/229.229#0", "Individual Report", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/230.230#0", "Group Report", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "build", "CONSTRUCTION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/231.231#0", "Taste Scores", "scy/pds");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	buildPizzaMissionPath();
	buildPizzaMissionDependances();
	//System.out.println("PizzaMission:" + this.toString());
    }

    public void buildECOMission() {

	LASModel aLAS = new LASModel(commandSpace, guidanceSpace, "startPage", "ORIENTATION");
	ELOModel aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/175.175#0", "Challenge", "scy/url");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "conceptualisatsionConceptMap", "CONCEPTUALISATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/176.176#0", "Concept map", "scy/mapping");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "reportingVideo", "REPORTING");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/188.188#0", "Video report", "scy/youtuber");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "orientation_B1", "ORIENTATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/159.159#0", "Hypotheses", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/157.157#0", "Problem formulation", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/158.158#0", "Research questions", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "orientation_B2", "ORIENTATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/153.153#0", "Hypotheses", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/151.151#0", "Problem formulation", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/152.152#0", "Research questions", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "orientation_B3", "ORIENTATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/178.178#0", "Hypotheses", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/150.150#0", "Problem formulation", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/177.177#0", "Research questions", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "orientation_C1", "ORIENTATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/156.156#0", "Hypotheses", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/154.154#0", "Problem formulation", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/155.155#0", "Research questions", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "experiment_B1", "EXPERIMENT");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/180.180#0", "Inferences", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/181.181#0", "Experimental procedure", "scy/xproc");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/173.173#0", "Influence of nutrients on biomass", "scy/model");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/179.179#0", "Data from experiments", "scy/pds");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "experiment_B2", "EXPERIMENT");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/164.164#0", "Inferences", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/165.165#0", "Experimental procedure", "scy/xproc");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/166.166#0", "Data from experiments", "scy/pds");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "experiment_B3", "EXPERIMENT");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/167.167#0", "Inferences", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/168.168#0", "Experimental procedure", "scy/xproc");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/174.174#0", "Prey-predator relationships", "scy/model");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/169.169#0", "Data from experiments", "scy/pds");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "experiment_C1", "EXPERIMENT");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/170.170#0", "Inferences", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/171.171#0", "Experimental procedure", "scy/xproc");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/172.172#0", "Data from experiments", "scy/pds");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "design_B1", "DESIGN");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/160.160#0", "Problem solution", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "design_B2", "DESIGN");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/161.161#0", "Problem solution", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "design_B3", "DESIGN");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/162.162#0", "Problem solution", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "design_C1", "DESIGN");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/163.163#0", "Problem solution", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);
    }

    public void buildHouseMission() {
	LASModel aLAS = new LASModel(commandSpace, guidanceSpace, "startPage", "ORIENTATION");
	ELOModel aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/3.3#0", "Mission notes", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "conceptualizationDesign", "CONCEPTUALISATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/8.8#0", "First ideas of my design group", "scy/mapping");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "conceptualizationExperts", "CONCEPTUALISATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/12.12#0", "Hypotheses of my expert group", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/10.10#0", "Research questions of my expert group", "scy/rtf");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "experimentExperts", "EXPERIMENT");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/20.20#0", "Conclusion of expert experiments", "scy/rtf");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/44.44#0", "Experimental procedure", "scy/xproc");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/17.17#0", "Data from the Thermal simulation", "scy/simconfig");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/48.48#0", "Data from the Converter", "scy/simconfig");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/18.18#0", "Data from real experiments", "scy/pds");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "conceptualizationIndividual", "CONCEPTUALISATION");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/5.5#0", "Concept map on CO2 emission", "scy/mapping");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "construction", "DESIGN");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/31.31#0", "House drawings", "scy/skp");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "design", "DESIGN");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/28.28#0", "House choices", "scy/mapping");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/26.26#0", "Inventory of expert solutions", "scy/mapping");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "reportingExperts", "REPORTING");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/24.24#0", "Expert presentation", "scy/ppt");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/22.22#0", "Expert concept map", "scy/mapping");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "experimentDesign", "DESIGN");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/49.49#0", "House data from the Converter", "scy/simconfig");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/30.30#0", "House data from the Thermal simulation", "scy/simconfig");
	aLAS.addIntermediateELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "reportingDesign", "REPORTING");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/33.33#0", "Presentation of house design", "scy/ppt");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);

	aLAS = new LASModel(commandSpace, guidanceSpace, "reportingIndividual", "REPORTING");
	aELO = new ELOModel(commandSpace, guidanceSpace, "roolo://scy.collide.info/scy-collide-server/35.35#0", "Individual report", "scy/doc");
	aLAS.setMainAnchorELOModel(aELO);
	aELO.setLASModel(aLAS);
	addELOModel(aELO);
	addLASModel(aLAS);
    }

    private void buildRelation(String fromCode, String toCode, String type) {
	String fromID = new String("roolo://scy.collide.info/scy-collide-server/" + fromCode + "." + fromCode + "#0");
	String toID = new String("roolo://scy.collide.info/scy-collide-server/" + toCode + "." + toCode + "#0");
	ELOModel fromELO = findELOModelByID(fromID);
	ELOModel toELO = findELOModelByID(toID);
	if (type.equalsIgnoreCase(ELOModel.DEPENDING)) {
	    fromELO.addDependingELO(toELO);
	    toELO.addDependedELO(fromELO);
	} else if (type.equalsIgnoreCase(ELOModel.PRECEDING)) {
	    fromELO.addSuccedingELO(toELO);
	    toELO.addPrecedingELO(fromELO);
	}
    }

    private void buildPizzaMissionPath() {
	// String pathNodes[] = { "237", "212", "213", "204", "214", "255", "216", "217", "218",
	// "205", "232", "219", "233", "208", "248", "221", "222", "234", "235", "249", "238", "223", "224", "225", "226", "227", "239", "228", "229", "230", "231" };

	String pathNodes[] = { "238", "213", "214", "205", "215", "258", "217", "218", "219", "206", "220", "209", "255", "222", "223", "257", "239", "224", "225", "226", "227", "228", "240", "229", "230", "231", "232" };

	for (int i = 0; i < pathNodes.length - 1; i++) {
	    buildRelation(pathNodes[i], pathNodes[i + 1], ELOModel.PRECEDING);
	}
    }

    private void buildPizzaMissionDependances() {
	// Data flow of ELOs related to the Health Passport
	/*
	 * buildRelation("204", "205", ELOModel.DEPENDING); buildRelation("204", "232", ELOModel.DEPENDING); buildRelation("205", "249", ELOModel.DEPENDING); buildRelation("232", "249", ELOModel.DEPENDING); buildRelation("219", "249", ELOModel.DEPENDING); buildRelation("233", "249", ELOModel.DEPENDING); buildRelation("234", "249", ELOModel.DEPENDING); buildRelation("235", "249", ELOModel.DEPENDING);
	 * 
	 * // Data flow of ELOs related to pizza creation and optimization buildRelation("237", "225", ELOModel.DEPENDING); buildRelation("238", "225", ELOModel.DEPENDING); buildRelation("225", "227", ELOModel.DEPENDING); buildRelation("226", "227", ELOModel.DEPENDING); buildRelation("227", "239", ELOModel.DEPENDING);
	 */
	// 206 (dailyCalorieIntake) is dependingOn
	//	205 ->foodAndExerciseDiary
	buildRelation("205", "206", ELOModel.DEPENDING);
	// 257 (healthPassport) is dependingOn
	//	205 ->foodAndExerciseDiary
	//	206 ->dailyCalorieIntake
	//	220 ->energyFactSheet
	buildRelation("205", "257", ELOModel.DEPENDING);
	buildRelation("206", "257", ELOModel.DEPENDING);
	buildRelation("220", "257", ELOModel.DEPENDING);

	// Data flow of ELOs related to pizza creation and optimization
	//226 (criteriaTable) is dependingOn
	//	238 -> myFavouritePizza
	//	239 -> myFirstHealthyPizza
	buildRelation("238", "226", ELOModel.DEPENDING);
	buildRelation("239", "226", ELOModel.DEPENDING);
	
	//228 (criteriaFinalTable) is dependingOn
	//	226 -> criteriaTable
	//	227 -> criteriaWeightTable
	buildRelation("226", "228", ELOModel.DEPENDING);
	buildRelation("227", "228", ELOModel.DEPENDING);
	
	buildRelation("228", "240", ELOModel.DEPENDING);

    }
    
    /*
     * private void buildECOMissionPath() { String pathNodes[] = {"", "", "", "", "", "", "", "", "", ""}; for (int i=0; i<pathNodes.length-1; i++){ buildRelation(pathNodes[i], pathNodes[i+1], ELOModel.PRECEDING); } }
     * 
     * private void buildECOMissionDependances() { buildRelation("", "", ELOModel.DEPENDING); buildRelation("", "", ELOModel.DEPENDING); buildRelation("", "", ELOModel.DEPENDING); }
     */
}
