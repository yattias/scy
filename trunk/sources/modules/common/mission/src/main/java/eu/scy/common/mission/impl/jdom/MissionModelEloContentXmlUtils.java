package eu.scy.common.mission.impl.jdom;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.LasType;
import eu.scy.common.mission.MissionAnchor;
import eu.scy.common.mission.MissionModelEloContent;
import eu.scy.common.mission.impl.BasicLas;
import eu.scy.common.mission.impl.BasicMissionAnchor;
import eu.scy.common.mission.impl.BasicMissionModelEloContent;
import eu.scy.common.scyelo.ColorSchemeId;

import static eu.scy.common.mission.impl.jdom.JDomConversionUtils.*;

public class MissionModelEloContentXmlUtils
{

   private final static String missionModelName = "missionModel";
   private final static String idName = "id";
   private final static String loElosName = "loElos";
   private final static String otherElosName = "otherElos";
   private final static String activeLasName = "activeLas";
   private final static String missionMapBackgroundImageUriName = "missionMapBackgroundImageUri";
   private final static String missionMapInstructionUriName = "missionMapInstructionUri";
   private final static String missionMapButtonIconTypeName = "missionMapButtonIconType";
   private final static String lassesName = "lasses";
   private final static String lasName = "las";
   private final static String nextLassesName = "nextLasses";
   private final static String anchorsName = "anchors";
   private final static String anchorName = "anchor";
   private final static String xPosName = "xPos";
   private final static String yPosName = "yPos";
   private final static String instructionUriName = "instructionUri";
   private final static String lasTypeName = "lasType";
   private final static String eloUriName = "eloUri";
   private final static String iconTypeName = "iconType";
   private final static String mainAnchorName = "mainAnchor";
   private final static String toolTipName = "toolTip";
   private final static String titleName = "title";
   private final static String nextAnchorsName = "nextAnchors";
   private final static String inputAnchorsName = "inputAnchors";
   private final static String resourceElosName = "resourceElos";
   private final static String intermediateAnchorsName = "intermediateAnchors";
   private final static String intermediateAnchorName = "intermediateAnchor";
   private final static String relationsName = "relations";
   private final static String relationName = "relation";
   private final static String dependingOnMissionAnchorIdsName = "dependingOnMissionAnchorIds";
   private final static String dependingOnMissionAnchorIdName = "dependingOnMissionAnchorId";
   private final static String targetDescriptionUriName = "targetDescriptionUri";
   private final static String assignmentUriName = "assignmentUri";
   private final static String resourcesUriName = "resourcesUri";
   private final static String helpUriName = "helpUri";
   private final static String colorSchemeName = "colorScheme";
   private final static String windowStatesXmlsName = "windowStatesXmls";
   private final static String windowStatesXmlName = "windowStatesXml";
   private final static Logger logger = Logger.getLogger(MissionModelEloContentXmlUtils.class);
   private final static JDomStringConversion jdomStringConversion = new JDomStringConversion();

   private MissionModelEloContentXmlUtils()
   {
   }

   public static String missionModelToXml(MissionModelEloContent missionModel)
   {
      Element root = new Element(missionModelName);
      // root.addContent(createElement(idName,missionMapModel.id));
      // root.addContent(createElement(nameName,missionModel.getName()));
      String selectedLasId = "";
      if (missionModel.getSelectedLas() != null)
      {
         selectedLasId = missionModel.getSelectedLas().getId();
      }
      root.addContent(createElement(activeLasName, selectedLasId));
      root.addContent(createElement(missionMapBackgroundImageUriName, missionModel.getMissionMapBackgroundImageUri()));
      root.addContent(createElement(missionMapInstructionUriName, missionModel.getMissionMapInstructionUri()));
      root.addContent(createElement(missionMapButtonIconTypeName, missionModel.getMissionMapButtonIconType()));
      root.addContent(createElement(loElosName, eloUriName, missionModel.getLoEloUris()));
      Element lasses = new Element(lassesName);
      root.addContent(lasses);
      for (Las las : missionModel.getLasses())
      {
         lasses.addContent(createLasXml(las));
      }
      root.addContent(createWindowStatesXmls(missionModel));
      return new JDomStringConversion().xmlToString(root);
   }

   private static Element createLasXml(Las las)
   {
      Element lasRoot = new Element(lasName);
      lasRoot.addContent(createElement(idName, las.getId()));
      lasRoot.addContent(createElement(xPosName, las.getXPos()));
      lasRoot.addContent(createElement(yPosName, las.getYPos()));
      lasRoot.addContent(createElement(toolTipName, las.getToolTip()));
      lasRoot.addContent(createElement(titleName, las.getTitle()));
      lasRoot.addContent(createElement(instructionUriName, las.getInstructionUri()));
      lasRoot.addContent(createElement(lasTypeName, las.getLasType()));
      lasRoot.addContent(createElement(loElosName, eloUriName, las.getLoEloUris()));
      lasRoot.addContent(createElement(otherElosName, eloUriName, las.getOtherEloUris()));
      lasRoot.addContent(createMissionAnchorXml(mainAnchorName, las.getMissionAnchor()));
      Element intermediateAnchorsRoot = new Element(intermediateAnchorsName);
      lasRoot.addContent(intermediateAnchorsRoot);
      for (MissionAnchor intermediaAnchor : las.getIntermediateAnchors())
      {
         intermediateAnchorsRoot.addContent(createMissionAnchorXml(intermediateAnchorName,
            intermediaAnchor));
      }
      Element nextLassesRoot = new Element(nextLassesName);
      lasRoot.addContent(nextLassesRoot);
      for (Las nextLas : las.getNextLasses())
      {
         nextLassesRoot.addContent(createElement(lasName, nextLas.getId()));
      }

      return lasRoot;
   }

   private static Element createMissionAnchorXml(String tagName, MissionAnchor missionAnchor)
   {
      Element root = new Element(tagName);
      root.addContent(createElement(eloUriName, missionAnchor.getEloUri()));
      root.addContent(createElement(idName, missionAnchor.getId()));
      root.addContent(createElement(iconTypeName, missionAnchor.getIconType()));
      // root.addContent(createElement(mainAnchorName, missionAnchor.mainAnchor));
      root.addContent(createElement(loElosName, eloUriName, missionAnchor.getLoEloUris()));
      root.addContent(createAnchorListXml(inputAnchorsName, missionAnchor.getInputMissionAnchors()));
      root.addContent(createElement(relationsName, eloUriName, missionAnchor.getRelationNames()));
      root.addContent(createElement(targetDescriptionUriName, missionAnchor.getTargetDescriptionUri()));
      root.addContent(createElement(assignmentUriName, missionAnchor.getAssignmentUri()));
      root.addContent(createElement(resourcesUriName, missionAnchor.getResourcesUri()));
      root.addContent(createElement(helpUriName, missionAnchor.getHelpUri()));
      root.addContent(createElement(colorSchemeName, missionAnchor.getColorSchemeId()));
      root.addContent(createElement(dependingOnMissionAnchorIdsName, dependingOnMissionAnchorIdName, missionAnchor.getDependingOnMissionAnchorIds()));
      return root;
   }

   private static Element createAnchorListXml(String tagName,
      List<MissionAnchor> inputMissionAnchors)
   {
      Element anchorList = new Element(tagName);
      for (MissionAnchor anchor : inputMissionAnchors)
      {
         anchorList.addContent(createElement(anchorName, anchor.getEloUri()));
      }
      return anchorList;
   }

   private static Element createWindowStatesXmls(MissionModelEloContent missionModel)
   {
      final Element windowStatesXmlsElement = new Element(windowStatesXmlsName);
      for (String lasId : missionModel.getWindowStatesXmlIds())
      {
         final String windowStateXml = missionModel.getWindowStatesXml(lasId);
         final Element windowStateXmlElement = new Element(windowStatesXmlName);
         windowStateXmlElement.setAttribute(idName, lasId);
         if (windowStateXml != null && windowStateXml.length() > 0)
         {
            Element xmlElement = jdomStringConversion.stringToXml(windowStateXml);
            windowStateXmlElement.addContent(xmlElement);
         }
         windowStatesXmlsElement.addContent(windowStateXmlElement);
      }
      return windowStatesXmlsElement;
   }

   public static MissionModelEloContent missionModelFromXml(String xml) throws URISyntaxException
   {
      Element root = new JDomStringConversion().stringToXml(xml);
      if (root == null || !missionModelName.equals(root.getName()))
      {
         return null;
      }
      BasicMissionModelEloContent missionModel = new BasicMissionModelEloContent();
      missionModel.setLoEloUris(getUriListValue(root.getChild(loElosName), eloUriName));
      HashMap<String, Las> lassesMap = new HashMap<String, Las>();
      HashMap<URI, MissionAnchor> anchorsMap = new HashMap<URI, MissionAnchor>();
      List<Las> lasses = new ArrayList<Las>();
      Element lassesRoot = root.getChild(lassesName);
      if (lassesRoot != null)
      {
         @SuppressWarnings("unchecked")
         List<Element> lasChildrenRoot = lassesRoot.getChildren(lasName);
         if (lasChildrenRoot != null)
         {
            for (Element lasChild : lasChildrenRoot)
            {
               Las las = createLas(lasChild, anchorsMap);
               lasses.add(las);
               lassesMap.put(las.getId(), las);
            }
         }
      }
      missionModel.setLasses(lasses);
      String selectedLasId = root.getChildTextTrim(activeLasName);
      if (selectedLasId != null)
      {
         missionModel.setSelectedLas(lassesMap.get(selectedLasId));
      }
      missionModel.setMissionMapBackgroundImageUri(getUriValue(root, missionMapBackgroundImageUriName));
      missionModel.setMissionMapInstructionUri(getUriValue(root, missionMapInstructionUriName));
      missionModel.setMissionMapButtonIconType(root.getChildTextTrim(missionMapButtonIconTypeName));
      if (!missionModel.getLasses().isEmpty())
      {
         fillInMissingLinks(lassesMap, anchorsMap, lassesRoot);
      }
      setWindowStatesXmls(root, missionModel);
      return missionModel;
   }

   private static Las createLas(Element root, HashMap<URI, MissionAnchor> anchorsMap)
      throws URISyntaxException
   {
      BasicLas las = new BasicLas();
      las.setId(root.getChildTextTrim(idName));
      las.setXPos(Float.parseFloat(root.getChildTextTrim(xPosName)));
      las.setYPos(Float.parseFloat(root.getChildTextTrim(yPosName)));
      las.setToolTip(root.getChildTextTrim(toolTipName));
      las.setTitle(root.getChildTextTrim(titleName));
      las.setLoEloUris(getUriListValue(root.getChild(loElosName), eloUriName));
      las.setOtherEloUris(getUriListValue(root.getChild(otherElosName), eloUriName));
      las.setMissionAnchor(createMissionAnchor(root.getChild(mainAnchorName)));
      las.setInstructionUri(getUriValue(root, instructionUriName));
      las.setLasType(getEnumValue(LasType.class, root, lasTypeName));
      las.getMissionAnchor().setLas(las);
      anchorsMap.put(las.getMissionAnchor().getEloUri(), las.getMissionAnchor());
      Element intermediateAnchorsRoot = root.getChild(intermediateAnchorsName);
      List<MissionAnchor> intermediateAnchors = new ArrayList<MissionAnchor>();
      @SuppressWarnings("unchecked")
      List<Element> intermediateAnchorslist = intermediateAnchorsRoot.getChildren(intermediateAnchorName);
      if (intermediateAnchorslist != null)
      {
         for (Element intermediateAnchorRoot : intermediateAnchorslist)
         {
            MissionAnchor intermediateAnchor = createMissionAnchor(intermediateAnchorRoot);
            intermediateAnchors.add(intermediateAnchor);
            intermediateAnchor.setLas(las);
            anchorsMap.put(intermediateAnchor.getEloUri(), intermediateAnchor);
         }
      }
      las.setIntermediateAnchors(intermediateAnchors);
      return las;
   }

   private static MissionAnchor createMissionAnchor(Element root) throws URISyntaxException
   {
      BasicMissionAnchor missionAnchor = new BasicMissionAnchor();
      missionAnchor.setEloUri(getUriValue(root, eloUriName));
      missionAnchor.setId(root.getChildText(idName));
      missionAnchor.setIconType(root.getChildText(iconTypeName));
      missionAnchor.setLoEloUris(getUriListValue(root.getChild(loElosName), eloUriName));
      missionAnchor.setRelationNames(getStringListValue(root, relationsName, relationName));
      missionAnchor.setTargetDescriptionUri(getUriValue(root, targetDescriptionUriName));
      missionAnchor.setAssignmentUri(getUriValue(root, assignmentUriName));
      missionAnchor.setResourcesUri(getUriValue(root, resourcesUriName));
      missionAnchor.setHelpUri(getUriValue(root, helpUriName));
      missionAnchor.setColorSchemeId(getEnumValue(ColorSchemeId.class, root, colorSchemeName));
      missionAnchor.setDependingOnMissionAnchorIds(getStringListValue(root, dependingOnMissionAnchorIdsName, dependingOnMissionAnchorIdName));
      return missionAnchor;
   }

   private static void fillInMissingLinks(HashMap<String, Las> lassesMap,
      HashMap<URI, MissionAnchor> anchorsMap, Element lassesRoot) throws URISyntaxException
   {
      @SuppressWarnings("unchecked")
      List<Element> lasChildrenRoot = lassesRoot.getChildren(lasName);
      for (Element lasChild : lasChildrenRoot)
      {
         String lasId = lasChild.getChildTextTrim(idName);
         Las las = lassesMap.get(lasId);
         List<Las> nextLasses = new ArrayList<Las>();
         List<String> nextLassesIds = getStringListValue(lasChild, nextLassesName, lasName);
         for (String nextLasId : nextLassesIds)
         {
            nextLasses.add(lassesMap.get(nextLasId));
         }
         las.setNextLasses(nextLasses);
         fillInMissingAnchorLinks(lasChild.getChild(mainAnchorName), anchorsMap);
         Element intermediateAnchorsRoot = lasChild.getChild(intermediateAnchorsName);
         @SuppressWarnings("unchecked")
         List<Element> intermediateAnchorslist = intermediateAnchorsRoot.getChildren(intermediateAnchorName);
         if (intermediateAnchorslist != null)
         {
            for (Element intermediateAnchorRoot : intermediateAnchorslist)
            {
               fillInMissingAnchorLinks(intermediateAnchorRoot, anchorsMap);
            }
         }
      }
   }

   private static void fillInMissingAnchorLinks(Element anchorRoot,
      HashMap<URI, MissionAnchor> anchorsMap) throws URISyntaxException
   {
      String eloUriString = anchorRoot.getChildTextTrim(eloUriName);
      MissionAnchor anchor = anchorsMap.get(new URI(eloUriString));
      if (anchor == null)
      {
         logger.warn("cannot find mission anchor for uri: " + eloUriString);
         return;
      }
      List<String> inputAnchorsUriStrings = getStringListValue(anchorRoot, inputAnchorsName,
         anchorName);
      List<MissionAnchor> inputMissionAnchors = new ArrayList<MissionAnchor>();
      for (String inputAnchorUriString : inputAnchorsUriStrings)
      {
         MissionAnchor inputAnchor = anchorsMap.get(new URI(inputAnchorUriString));
         if (inputAnchor != null)
         {
            inputMissionAnchors.add(inputAnchor);
         }
         else
         {
            logger.warn("mission anchor " + anchor.getEloUri()
               + " has a not created anchor as input: " + inputAnchorUriString);
         }
      }
      anchor.setInputMissionAnchors(inputMissionAnchors);
   }

   private static void setWindowStatesXmls(Element root, MissionModelEloContent missionModel)
   {
      final Element windowStatesXmlsElement = root.getChild(windowStatesXmlsName);
      if (windowStatesXmlsElement != null)
      {
         @SuppressWarnings("unchecked")
         List<Element> windowStatesXmlList = windowStatesXmlsElement.getChildren(windowStatesXmlName);
         if (windowStatesXmlList != null)
         {
            for (Element windowStatesXmlElement : windowStatesXmlList)
            {
               final String lasId = windowStatesXmlElement.getAttributeValue(idName);
               String xml = "";
               List<Element> children = windowStatesXmlElement.getChildren();
               if (children != null && children.size() > 0)
               {
                  xml = jdomStringConversion.xmlToString(children.get(0));
               }
               missionModel.setWindowStatesXml(lasId, xml);
            }
         }
      }
   }
}
