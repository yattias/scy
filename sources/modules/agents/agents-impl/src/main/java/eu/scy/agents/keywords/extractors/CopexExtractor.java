/*
 * Created on 21.09.2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package eu.scy.agents.keywords.extractors;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.io.StringReader;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.SAXException;

import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.ExtractKeywordsAgent;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;

/**
 * @author Jörg Kindermann
 * 
 *         Keyword extractor for ELOs produced by scy/copex
 */
public class CopexExtractor implements KeywordExtractor {

  private final static Logger logger = Logger.getLogger(TextExtractor.class);

  private TupleSpace tupleSpace;

  private SAXBuilder builder;

  private org.w3c.dom.Document document;

  private static String[] XMLPATH = { "experimental_procedure", "learner_proc", "proc_hypothesis",
                                     "hypothesis" };

  private ArrayList<String> path;

  public CopexExtractor() {
    // DocumentBuilderFactory builderFactory =
    // javax.xml.parsers.DocumentBuilderFactory.newInstance();
    // try {
    builder = new SAXBuilder();// builderFactory.newDocumentBuilder();
    // } catch (ParserConfigurationException e) {
    // logger.fatal(e.toString());
    // }
    path = new ArrayList<String>();
    for (int i = 0; i < XMLPATH.length; i++) {
      path.add(XMLPATH[i]);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.scy.agents.keywords.extractors.KeywordExtractor#getKeywords(roolo.elo.api.IELO)
   */
  @Override
  public List<String> getKeywords(IELO elo) {
    String text = getEloText(elo);
    if (!"".equals(text)) {
      return getKeywords(text);
    } else {
      return new ArrayList<String>();
    }
  }

  private String getEloText(IELO elo) {
    IContent content = elo.getContent();
    if (content == null) {
      logger.fatal("Content of elo is null");
      return "";
    }
    String text = "";

    String contentText = content.getXmlString();
    Document document;
    try {
      StringReader stringReader = new StringReader(contentText);
      document = builder.build(stringReader);
      Element element = document.getRootElement();
      text = findElementContent(path, element);
      String name = element.getName();
    } catch (IOException e) {
      logger.fatal(e.toString());
    } catch (JDOMException e) {
      logger.fatal("Content of elo XML is malformed");
      e.printStackTrace();
    }
    logger.debug("Got text " + text);
    return text;
  }

  private String findElementContent(ArrayList<String> xmlPath, Element element) {
    String text = "";
    String name = element.getName();
    if (xmlPath.size() == 0) {// end of path is reached -this must be the text element we
      // are looking for
      text = element.getText();
    } else { // move on down the XMLPATH
      String pathElement = xmlPath.get(0);
      if (name.equals(pathElement)) {
        List descendants = element.getChildren();
        Iterator iterator = descendants.iterator();
        if (iterator.hasNext()) {
          while (text.equals("") & iterator.hasNext()) { // if there are no further descendants, we
                                                         // return an empty text
            Element child = (Element) iterator.next();
            text = findElementContent(new ArrayList(xmlPath.subList(1, xmlPath.size())), child);
          }
        } else {
          text = element.getText();
        }
      }
    }
    return text;
  }

  private List<String> getKeywords(String text) {
    try {
      String queryId = new VMID().toString();
      Tuple extractKeywordsTriggerTuple = new Tuple(ExtractKeywordsAgent.EXTRACT_KEYWORDS,
                                                    AgentProtocol.QUERY, queryId, text);
      extractKeywordsTriggerTuple.setExpiration(7200000);
      Tuple responseTuple = null;
      if (this.tupleSpace.isConnected()) {
        this.tupleSpace.write(extractKeywordsTriggerTuple);
        responseTuple = this.tupleSpace.waitToTake(new Tuple(ExtractKeywordsAgent.EXTRACT_KEYWORDS,
                                                             AgentProtocol.RESPONSE, queryId,
                                                             Field.createWildCardField()));
      }
      if (responseTuple != null) {
        ArrayList<String> keywords = new ArrayList<String>();
        for (int i = 3; i < responseTuple.getNumberOfFields(); i++) {
          String keyword = (String) responseTuple.getField(i).getValue();
          keywords.add(keyword);
        }
        return keywords;
      }
    } catch (TupleSpaceException e) {
      e.printStackTrace();
    }
    return new ArrayList<String>();
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.scy.agents.keywords.extractors.KeywordExtractor#getTupleSpace()
   */
  @Override
  public TupleSpace getTupleSpace() {
    return tupleSpace;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.scy.agents.keywords.extractors.KeywordExtractor#setTupleSpace(info.collide.sqlspaces.client
   * .TupleSpace)
   */
  @Override
  public void setTupleSpace(TupleSpace tupleSpace) {
    this.tupleSpace = tupleSpace;
  }

}
