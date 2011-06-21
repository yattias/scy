package eu.scy.agents.processguidanceservice;

import java.util.Arrays;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LASModel extends AbstractGuidanceObject {

	public static final String DESIGN = "DESIGN";
	public static final String EXPERIMENT = "EXPERIMENT";
	public static final String INFORMATION = "INFORMATION";
	public static final String CONCEPTUALISATION = "CONCEPTUALISATION";
	public static final String REFLECTION = "REFLECTION";
	public static final String EVALUATION = "EVALUATION";
	public static final String CONSTRUCTION = "CONSTRUCTION";
	public static final String ORIENTATION = "ORIENTATION";
	public static final String REPORTING = "REPORTING";

    private String type;
    private MissionModel missionModel;    
	private ELOModel mainAnchor;
	private ELOModel[] intermediateELOModels = new ELOModel[0];
	//private ELOModel[] inputELOModels;		

	//used in building a model with a xml doc
    public LASModel(MissionModel aMissionModel) {
    	missionModel = aMissionModel;
    }	

    //used in building a model with a xml doc (according to the "next_las")
    public LASModel(String lasID, MissionModel aMissionModel) {
    	id = lasID;
    	missionModel = aMissionModel;
    }	

    //used in manually creating a model
    public LASModel(String lasID, String lasType) {
    	id = lasID;
    	type = lasType;
    }	

    public void buildLASModel(Node aLASNode) {
        try {
        	NodeList lasInfoNodes = (NodeList) aLASNode.getChildNodes();         	
        	for (int i = 0; i < lasInfoNodes.getLength(); i++) {        		
        		if (lasInfoNodes.item(i).getNodeName().equalsIgnoreCase("id")) {
        			setId(lasInfoNodes.item(i).getTextContent());
        		} else if (lasInfoNodes.item(i).getNodeName().equalsIgnoreCase("lasType")) {
        			setType(lasInfoNodes.item(i).getTextContent());
        			
        			System.out.println();
                	System.out.println("aLAS = new LASModel(\""+
                    		id+"\", \""+type+"\");");
                	
        		} else if (lasInfoNodes.item(i).getNodeName().equalsIgnoreCase("mainAnchor")) { 
        			NodeList aNodeLists = (NodeList) lasInfoNodes.item(i).getChildNodes(); 
        			for (int j = 0; j < aNodeLists.getLength(); j++) {   
        				if (aNodeLists.item(j).getNodeName().equalsIgnoreCase("eloUri")) {
        					ELOModel aELOModel = new ELOModel(this);        			
                			aELOModel.buildELOModel(aNodeLists.item(j).getTextContent());
                			this.setMainAnchorELOModel(aELOModel);
                			this.getMissionModel().addELOModel(aELOModel);
                			                			
                			System.out.println("aELO = new ELOModel(\""+aELOModel.getId()+
                					"\", \""+aELOModel.getTitle()+"\", \""+aELOModel.getType()+"\");");
                			System.out.println("aLAS.setMainAnchorELOModel(aELO);");
                			System.out.println("aELO.setLASModel(aLAS);");
                			System.out.println("aModel.addELOModel(aELO);");

                			break;
        				}
        			}        			
        		} else if (lasInfoNodes.item(i).getNodeName().equalsIgnoreCase("intermediateAnchors")) {        			 
        			NodeList intermediateAnchorNodes = (NodeList) lasInfoNodes.item(i).getChildNodes(); 
        			for (int j = 0; j < intermediateAnchorNodes.getLength(); j++) {   			
        				if (intermediateAnchorNodes.item(j).getNodeName().equalsIgnoreCase("intermediateAnchor")) {
        					NodeList aNodeLists = (NodeList) intermediateAnchorNodes.item(j).getChildNodes(); 
        					for (int k = 0; k < aNodeLists.getLength(); k++) {   
        						if (aNodeLists.item(k).getNodeName().equalsIgnoreCase("eloUri")) {
        							ELOModel aELOModel = new ELOModel(this);
                        			aELOModel.buildELOModel(aNodeLists.item(k).getTextContent());
                        			this.addIntermediateELOModel(aELOModel);
                        			this.getMissionModel().addELOModel(aELOModel);
                        			
                        			System.out.println("aELO = new ELOModel(\""+aELOModel.getId()+
                        					"\", \""+aELOModel.getTitle()+"\", \""+aELOModel.getType()+"\");");
                        			System.out.println("aLAS.addIntermediateELOModel(aELO);");
                        			System.out.println("aELO.setLASModel(aLAS);");
                        			System.out.println("aModel.addELOModel(aELO);");

                        			break;
        						}
        					}
        				}        				
        			}        			
        		} else if (lasInfoNodes.item(i).getNodeName().equalsIgnoreCase("nextLasses")) {        			 
        			NodeList nextLasNodes = (NodeList) lasInfoNodes.item(i).getChildNodes(); 
        			for (int j = 0; j < nextLasNodes.getLength(); j++) {   
        				LASModel aLASModel = this.getMissionModel().findLASModelByID(nextLasNodes.item(j).getTextContent());
        				if (aLASModel==null) {
        					aLASModel = new LASModel(nextLasNodes.item(j).getTextContent(), getMissionModel());
        					getMissionModel().addLASModel(aLASModel);
        				}
        			}        			
        		}      		
        	}
        	System.out.println("aModel.addLASModel(aLAS);");
        } catch (DOMException e) {
            e.printStackTrace();
		}	
    	intermediateELOModels = new ELOModel[0];
    }	
	
    public String getType() {
        return type;
    }

    public void setType(String aType) {
        this.type = aType;
    }
    
    public void setMissionModel(MissionModel aMissionModel) {
    	missionModel = aMissionModel;
    }
    
    public MissionModel getMissionModel() {
    	return missionModel;
    }
    
    /*
    public ELOModel[] getInputELOModels() {
        return inputELOModels;
    }

    public void addInputELOModel(ELOModel elo) {
        ELOModel[] newELOModels = (ELOModel[]) Arrays.copyOf(inputELOModels, inputELOModels.length + 1);
        newELOModels[newELOModels.length - 1] = elo;
        inputELOModels = newELOModels;
    }
    */
    
    public ELOModel[] getIntermediateELOModels() {
        return intermediateELOModels;
    }
    
    public ELOModel getMainAnchorELOModels() {
        return mainAnchor;
    }
    
    public void addIntermediateELOModel(ELOModel elo) {
        ELOModel[] newELOModels = (ELOModel[]) Arrays.copyOf(intermediateELOModels, intermediateELOModels.length + 1);
        newELOModels[newELOModels.length - 1] = elo;
        intermediateELOModels = newELOModels;
    }
    
    public void setMainAnchorELOModel(ELOModel elo) {
        mainAnchor = elo;
    }

    public String toString() {
    	String result = new String("LAS id="+id+", type="+type+"\n");
    	result += new String("main anchor: ")+mainAnchor.toString() + new String("\n");
    	result += new String("intermediateELOs: \n");
    	for (int i=0; i<intermediateELOModels.length; i++) {
    		result += intermediateELOModels[i].toString() + new String("\n");
    	}	
    	return result;
    }
    
}
