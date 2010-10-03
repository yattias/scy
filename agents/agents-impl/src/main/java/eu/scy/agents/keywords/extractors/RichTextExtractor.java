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
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.log4j.Logger;

import roolo.elo.api.IELO;
import util.Utilities;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.ExtractKeywordsAgent;

/**
 * @author Jörg Kindermann
 * 
 *         Keyword extractor for ELOs produced by scy/copex
 */
public class RichTextExtractor implements KeywordExtractor {

  private final static Logger logger = Logger.getLogger(RichTextExtractor.class);

  private TupleSpace tupleSpace;

  public static List<String> XMLPATH = Arrays.asList("elo","content","RichText");

  public RichTextExtractor() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.scy.agents.keywords.extractors.KeywordExtractor#getKeywords(roolo.elo.api.IELO)
   */
  @Override
  public List<String> getKeywords(IELO elo) {
    String text = getText(elo);
    if (!"".equals(text)) {
      return getKeywords(text);
    } else {
      return new ArrayList<String>();
    }
  }

  private String getText(IELO elo) {
    String text = "";
    try {
      text = unRTF(Utilities.getEloText(elo, XMLPATH, logger));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return text;
  }


  /**
   * Converts Rich-Text-Format (RTF) to plain text
   * @see javax.swing.text.rtf 
   * @param rtf a String in RTF 
   * @return plain text
   * @throws IOException
   */
  protected static String unRTF(String rtf) throws IOException {
      RTFEditorKit editor = new RTFEditorKit();
      JTextPane text = new JTextPane();
      StringReader sr = new StringReader(rtf);
      try {
          editor.read(sr, text.getDocument(), 0);
      } catch (BadLocationException e1) {
          e1.printStackTrace();
      }
      String txt;
      try {
          txt = text.getDocument().getText(0, text.getDocument().getLength());
          return txt;
      } catch (BadLocationException e) {
          e.printStackTrace();
          System.exit(1);     
      }
      return null;
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
