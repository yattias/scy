package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.server.controllers.xml.converters.LearningGoalConverter;
import eu.scy.core.model.transfer.*;

import java.io.Writer;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 28.jan.2011
 * Time: 11:59:13
 * To change this template use File | Settings | File Templates.
 */
public class XMLTransferObjectServiceImpl implements XMLTransferObjectService {

    private XStream xstream;

    protected int getXStreamMode() {
        return XStream.XPATH_RELATIVE_REFERENCES;
    }


    @Override
    public XStream getXStreamInstance() {
        this.xstream = new XStream(new XppDriver() {

            public HierarchicalStreamWriter createWriter(Writer out) {

                return new PrettyPrintWriter(out) {

                    boolean cdata = false;

                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                        if(name.equals("id")) cdata=false;
                        else cdata = true;
                    }

                    protected void writeText(QuickWriter writer, String text) {
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

                return new PrettyPrintWriter(out) {

                    boolean cdata = false;

                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                        if(name.equals("id")) cdata = false;
                        else cdata = true;
                    }

                    protected void writeText(QuickWriter writer, String text) {

                        /*String thing = "\\[";
                        String cdata = "<!CDATA";
                        String end = "\\]";
                        // System.out.println("TEXT IS: " + text);
                        text = text.replaceAll(thing, "");
                        text = text.replaceAll(cdata, "");
                        text = text.replace(end, "");
                        // System.out.println("TEXT_ NOW: " + text);
                        */
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
    public Object getObject(String xml){
        /*
        String thing = "\\[";
        String cdata = "<!CDATA";
        //String cdata = "<!\\[CDATA\\[";
        String end = "\\]";

        xml = xml.replaceAll(thing,"");
        xml = xml.replaceAll(cdata,"");
        xml = xml.replaceAll(end, "");

        // System.out.println("xml" + xml);
        */
        return getToObjectXStream().fromXML(xml);
    }

    private void addAliases(XStream xStream) {

        xStream.alias("elo", TransferElo.class);
        xStream.alias("goal", LearningGoal.class);
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



        xStream.aliasField("portfolioConfigService".toLowerCase(), ToolURLContainer.class, "portfolioConfigService");
        xStream.aliasField("actionLogger".toLowerCase(), ToolURLContainer.class, "actionLogger");

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
        xStream.aliasField("studentglg", TransferElo.class, "generalLearningGoals");
        xStream.aliasField("assessmentComment".toLowerCase(), TransferElo.class, "assessmentComment");
        xStream.aliasField("reflectionComment".toLowerCase(), TransferElo.class, "reflectionComment");
        xStream.aliasField("hasBeenReflectedOn".toLowerCase(), TransferElo.class, "hasBeenReflectedOn");
        xStream.aliasField("inquiryQuestion".toLowerCase(), TransferElo.class, "inquiryQuestion");
        xStream.aliasField("technicalformat".toLowerCase(), TransferElo.class, "technicalFormat");
        xStream.aliasField("rawData".toLowerCase(), TransferElo.class, "rawData");
        xStream.aliasField("hasBeenSelectedForSubmit".toLowerCase(), TransferElo.class, "hasBeenSelectedForSubmit");
        xStream.aliasField("studentslg", TransferElo.class, "specificLearningGoals");
        xStream.aliasField("feedbackEloUrl".toLowerCase(), TransferElo.class, "feedbackEloUrl");

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

        xStream.addImplicitCollection(TransferElo.class, "generalLearningGoals", LearningGoal.class);
        xStream.addImplicitCollection(TransferElo.class, "specificLearningGoals", LearningGoal.class);
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

        xStream.aliasField("generalLearningGoals".toLowerCase(), LearningGoals.class, "generalLearningGoals");
        xStream.aliasField("specificLearningGoals".toLowerCase(), LearningGoals.class, "specificLearningGoals");

        xStream.aliasField("generallearninggoals", LearningGoals.class, "generalLearningGoals ");
        xStream.aliasField("specificlearninggoals", LearningGoals.class, "specificLearningGoals ");



        xStream.registerConverter(new LearningGoalConverter());

    }

}
