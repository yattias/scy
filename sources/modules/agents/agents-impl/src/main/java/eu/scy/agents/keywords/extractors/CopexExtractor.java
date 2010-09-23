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
import util.Utilities;

/**
 * @author Jörg Kindermann
 * 
 *         Keyword extractor for ELOs produced by scy/copex
 */
public class CopexExtractor implements KeywordExtractor {

  private final static Logger logger = Logger.getLogger(TextExtractor.class);

  private TupleSpace tupleSpace;

  private static String[] XMLPATH = { "experimental_procedure", "learner_proc", "proc_hypothesis",
                                     "hypothesis" };

  private ArrayList<String> path;

  public CopexExtractor() {
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
    String text = Utilities.getEloText(elo, path, logger);
    if (!"".equals(text)) {
      return getKeywords(text);
    } else {
      return new ArrayList<String>();
    }
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
