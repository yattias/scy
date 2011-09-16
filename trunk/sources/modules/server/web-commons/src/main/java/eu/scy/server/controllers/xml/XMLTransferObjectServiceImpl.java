package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.server.controllers.xml.converters.LearningGoalConverter;
import eu.scy.core.model.transfer.*;

import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 28.jan.2011
 * Time: 11:59:13
 * To change this template use File | Settings | File Templates.
 */
public class XMLTransferObjectServiceImpl implements XMLTransferObjectService {

    private static Logger log = Logger.getLogger("XMLTransferObjectServiceImpl.class");

    private XStream xstream;

    protected int getXStreamMode() {
        return XStream.XPATH_RELATIVE_REFERENCES;
    }


    @Override
    public XStream getXStreamInstance() {
        this.xstream = new XStream(new XppDriver() {

            public HierarchicalStreamWriter createWriter(Writer out) {

                return new SCYPrintWriter(out) {

                    boolean cdata = false;

                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                        if (name.equals("id")) cdata = false;
                        else cdata = true;
                    }

                    protected void writeText(QuickWriter writer, String text) {

                        if (text.indexOf("#") > -1) {
                            try {
                                text = URLEncoder.encode(text, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                log.warning(e.getMessage());
                            }
                            log.info("NEW TEXT IS : " + text);
                        }

                        if (cdata) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                        } else {
                            writer.write(text);
                        }
                    }
                };
            }
        }
        );

        xstream.setMode(getXStreamMode());
        addAliases(xstream);

        return xstream;

    }

    @Override
    public XStream getToObjectXStream() {
        this.xstream = new XStream(new XppDriver() {


            public HierarchicalStreamWriter createWriter(Writer out) {

                return new SCYPrintWriter(out) {

                    boolean cdata = false;

                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                        if (name.equals("id")) cdata = false;
                        else cdata = true;
                    }

                    protected void writeText(QuickWriter writer, String text) {
                        writer.write(text);
                    }
                };
            }
        }
        );
        xstream.setMode(getXStreamMode());
        addAliases(xstream);

        return xstream;
    }

    @Override
    public Object getObject(String xml) {
        if (xml != null && xml.length() > 0) {
            if (xml.startsWith("<feedback>")) {
                xml = fixXml(xml);
            }
        }


            /*xml = xml.replaceAll("feedbackeloelo", "feedbackelo");
           if(xml.startsWith("<feedback")) {
               xml = xml.replaceAll("<feedback>", "<feedbackelo>");
               xml = xml.replaceAll("</feedback>", "</feedbackelo>");
           } */

            if (xml == null || xml.length() == 0) {
                log.warning("NO OBJECT FOUND");
                return null;
            }


            if (xml.contains("<content")) {
                Integer end = xml.indexOf(">");
                xml = xml.substring(end + 1, xml.length());
                if (xml.indexOf("</content") > 0) {
                    xml = xml.replaceAll("</content>", "");
                }
            }

            if (xml.contains("<studentglg>")) {
                String start = xml.substring(0, xml.indexOf("<studentglg>"));
                String end = xml.substring(xml.indexOf("</studentglg>") + "</studentglg>".length(), xml.length());
                xml = start + end;//FUCK!
            }

            if (xml.contains("<studentslg>")) {
                String start = xml.substring(0, xml.indexOf("<studentslg>"));
                String end = xml.substring(xml.indexOf("</studentslg>") + "</studentslg>".length(), xml.length());
                xml = start + end;//FUCK!
            }

            xml = xml.replaceAll("<goal />", "");

            //log.info("XML AFTER WASH: " + xml);

            return getToObjectXStream().fromXML(xml);
        }

    private String fixXml(String xmlString) {

        String feedback = "<feedback>";
        String feedbackEnd = "</feedback>";
        xmlString = xmlString.substring(feedback.length(), xmlString.length());
        xmlString = xmlString.substring(0, xmlString.length() - feedbackEnd.length());

        xmlString = "<feedbackelo>" + xmlString + "</feedbackelo>";

        return xmlString;
    }

    private void addAliases(XStream xStream) {

        xStream.alias("elo", TransferElo.class);
        xStream.alias("learninggoal", LearningGoal.class);
        xStream.alias("searchresult", ELOSearchResult.class);
        xStream.alias("portfoliocontainer", PortfolioContainer.class);
        xStream.alias("portfolios", LinkedList.class);
        xStream.alias("elo", TransferElo.class);
        xStream.alias("portfolio", Portfolio.class);
        xStream.alias("portfolioconfig", PortfolioConfig.class);
        xStream.alias("portfolioeffort", PortfolioEffortScale.class);
        xStream.alias("tab", Tab.class);
        xStream.alias("action", ActionLogEntry.class);
        xStream.alias("attribute", ActionLogEntryAttribute.class);
        xStream.alias("serviceMessage", ServiceMessage.class);
        xStream.alias("rawdata", RawData.class);
        xStream.alias("newestelos", NewestElos.class);
        xStream.alias("feedbackelo", FeedbackEloTransfer.class);
        xStream.alias("feedback", FeedbackTransfer.class);
        xStream.alias("reply", FeedbackReplyTransfer.class);
        xStream.alias("learninggoals", LearningGoals.class);


        xStream.aliasField("portfolioConfigService".toLowerCase(), ToolURLContainer.class, "portfolioConfigService");
        xStream.aliasField("actionLogger".toLowerCase(), ToolURLContainer.class, "actionLogger");
        xStream.aliasField("newestElosFeedbackService".toLowerCase(), ToolURLContainer.class, "newestElosFeedbackService");
        xStream.aliasField("feedbackEloService".toLowerCase(), ToolURLContainer.class, "feedbackEloService");
        xStream.aliasField("retrieveSingleELOService".toLowerCase(), ToolURLContainer.class, "retrieveSingleELOService");
        xStream.aliasField("AutoSaveService.as".toLowerCase(), ToolURLContainer.class, "AutoSaveService.as");

        xStream.aliasField("fullScreen".toLowerCase(), RawData.class, "fullScreen");
        xStream.aliasField("dataSet".toLowerCase(), RawData.class, "dataSet");

        xStream.aliasField("createddate", TransferElo.class, "createdDate");
        xStream.aliasField("modified", TransferElo.class, "lastModified");
        xStream.aliasField("uri".toLowerCase(), TransferElo.class, "uri");
        xStream.aliasField("catname".toLowerCase(), TransferElo.class, "catname");
        xStream.aliasField("thumbnail".toLowerCase(), TransferElo.class, "thumbnail");
        xStream.aliasField("fullsize".toLowerCase(), TransferElo.class, "fullsize");
        xStream.aliasField("customname".toLowerCase(), TransferElo.class, "myname");
        xStream.aliasField("modified".toLowerCase(), TransferElo.class, "modified");
        xStream.aliasField("studentDescription".toLowerCase(), TransferElo.class, "studentDescription");
        xStream.aliasField("studentReflection".toLowerCase(), TransferElo.class, "studentReflection");
        xStream.aliasField("studentInquiry".toLowerCase(), TransferElo.class, "studentInquiry");
        xStream.aliasField("assessed".toLowerCase(), TransferElo.class, "assessed");
        xStream.aliasField("grade".toLowerCase(), TransferElo.class, "grade");
        xStream.aliasField("assessmentComment".toLowerCase(), TransferElo.class, "assessmentComment");
        xStream.aliasField("reflectionComment".toLowerCase(), TransferElo.class, "reflectionComment");
        xStream.aliasField("createdDate".toLowerCase(), TransferElo.class, "createdDate");
        xStream.aliasField("lastModified".toLowerCase(), TransferElo.class, "lastModified");
        xStream.aliasField("createdBy".toLowerCase(), TransferElo.class, "createdBy");
        xStream.aliasField("eloId".toLowerCase(), TransferElo.class, "eloId");
        xStream.aliasField("studentgenerallearninggoals", TransferElo.class, "generalLearningGoals");
        xStream.aliasField("assessmentComment".toLowerCase(), TransferElo.class, "assessmentComment");
        xStream.aliasField("reflectionComment".toLowerCase(), TransferElo.class, "reflectionComment");
        xStream.aliasField("hasBeenReflectedOn".toLowerCase(), TransferElo.class, "hasBeenReflectedOn");
        xStream.aliasField("inquiryQuestion".toLowerCase(), TransferElo.class, "inquiryQuestion");
        xStream.aliasField("technicalformat".toLowerCase(), TransferElo.class, "technicalFormat");
        xStream.aliasField("rawData".toLowerCase(), TransferElo.class, "rawData");
        xStream.aliasField("hasBeenSelectedForSubmit".toLowerCase(), TransferElo.class, "hasBeenSelectedForSubmit");
        xStream.aliasField("studentspecificlearninggoals", TransferElo.class, "specificLearningGoals");
        xStream.aliasField("feedbackEloUrl".toLowerCase(), TransferElo.class, "feedbackEloUrl");
        xStream.aliasField("reflectionQuestion".toLowerCase(), TransferElo.class, "reflectionQuestion");

        xStream.aliasField("createdBy".toLowerCase(), FeedbackEloTransfer.class, "createdBy");
        xStream.aliasField("createdByPicture".toLowerCase(), FeedbackEloTransfer.class, "createdByPicture");
        xStream.aliasField("calendarDate".toLowerCase(), FeedbackEloTransfer.class, "calendarDate");
        xStream.aliasField("calendarTime".toLowerCase(), FeedbackEloTransfer.class, "calendarTime");
        xStream.aliasField("comment".toLowerCase(), FeedbackEloTransfer.class, "comment");

        xStream.aliasField("comment".toLowerCase(), FeedbackTransfer.class, "comment");
        xStream.aliasField("createdBy".toLowerCase(), FeedbackTransfer.class, "createdBy");
        xStream.aliasField("createdByPicture".toLowerCase(), FeedbackTransfer.class, "createdByPicture");
        xStream.aliasField("calendarDate".toLowerCase(), FeedbackTransfer.class, "calendarDate");
        xStream.aliasField("calendarTime".toLowerCase(), FeedbackTransfer.class, "calendarTime");

        xStream.aliasField("comment".toLowerCase(), FeedbackReplyTransfer.class, "comment");
        xStream.aliasField("createdBy".toLowerCase(), FeedbackReplyTransfer.class, "createdBy");
        xStream.aliasField("createdByPicture".toLowerCase(), FeedbackReplyTransfer.class, "createdByPicture");
        xStream.aliasField("calendarDate".toLowerCase(), FeedbackReplyTransfer.class, "calendarDate");
        xStream.aliasField("calendarTime".toLowerCase(), FeedbackReplyTransfer.class, "calendarTime");

        xStream.aliasField("eloURI".toLowerCase(), ActionLogEntry.class, "eloURI");
        xStream.aliasField("technicalformat".toLowerCase(), ActionLogEntry.class, "technicalformat");
        xStream.aliasField("rawData".toLowerCase(), ActionLogEntry.class, "rawData");

        xStream.addImplicitCollection(NewestElos.class, "elos", TransferElo.class);

        xStream.aliasField("reflectionOnMissionQuestion".toLowerCase(), PortfolioConfig.class, "reflectionOnMissionQuestion");
        xStream.aliasField("reflectionOnCollaborationQuestion".toLowerCase(), PortfolioConfig.class, "reflectionOnCollaborationQuestion");
        xStream.aliasField("reflectionOnInquiryQuestion".toLowerCase(), PortfolioConfig.class, "reflectionOnInquiryQuestion");
        xStream.aliasField("reflectionOnEffortQuestion".toLowerCase(), PortfolioConfig.class, "reflectionOnEffortQuestion");
        xStream.aliasField("portfolioEffortScaleItems".toLowerCase(), PortfolioConfig.class, "portfolioEffortScale");
        xStream.aliasField("portfolioReflectionTabs".toLowerCase(), PortfolioConfig.class, "portfolioReflectionTabs");
        xStream.aliasField("setEloReflection".toLowerCase(), PortfolioConfig.class, "setEloReflection");

        xStream.aliasField("score".toLowerCase(), PortfolioEffortScale.class, "score");
        xStream.aliasField("text".toLowerCase(), PortfolioEffortScale.class, "text");
        xStream.aliasField("url".toLowerCase(), PortfolioEffortScale.class, "url");

        xStream.aliasField("generalLearningGoals ".toLowerCase(), LearningGoals.class, "generalLearningGoals ");
        xStream.aliasField("specificLearningGoals ".toLowerCase(), LearningGoals.class, "specificLearningGoals ");

        xStream.aliasField("portfoliostatus", Portfolio.class, "portfolioStatus");
        xStream.aliasField("missionname", Portfolio.class, "missionName");
        xStream.aliasField("reflectioncollaboration", Portfolio.class, "reflectionCollaboration");
        xStream.aliasField("reflectioninquiry", Portfolio.class, "reflectionInquiry");
        xStream.aliasField("reflectioneffort", Portfolio.class, "reflectionEffort");
        xStream.aliasField("reflectionmission", Portfolio.class, "reflectionMission");
        xStream.aliasField("assessmentportfoliocomment", Portfolio.class, "assessmentPortfolioComment");
        xStream.aliasField("assessmentportfoliorating", Portfolio.class, "assessmentPortfolioRating");
        xStream.aliasField("mission", Portfolio.class, "missionName");
        xStream.aliasField("missionruntimeuri", Portfolio.class, "missionRuntimeURI");
    }

}
