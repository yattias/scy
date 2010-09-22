/*
 * Created on 21.09.2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package eu.scy.agents.keywords.extractors;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.content.BasicContent;

import eu.scy.agents.AbstractTestFixture;

public class CopexExtractorTest extends AbstractTestFixture {

  private IELO elo;

  private CopexExtractor copexExtractor;

  @Before
  public void setup() throws IOException {
//     File file = new File("src/test/resources/test.xml");
//    String eloContent = "";
//     try {
//     eloContent = readFile1(file);
//     } catch (IOException e) {
//     // TODO Auto-generated catch block
//     e.printStackTrace();
//     }
    InputStream inStream = this.getClass().getResourceAsStream("/copexExampleElo.xml");
    String eloContent = readFile(inStream);
    elo = createNewElo("TestCopex", "scy/copex");
    elo.setContent(new BasicContent(eloContent));
    copexExtractor = new CopexExtractor();
  }

  @Test
  public void testGetKeywords() {
    List<String> keywords = copexExtractor.getKeywords(elo);
    assertEquals(4, keywords.size());
    assertTrue(hasItems(keywords, "total moment", "moment child 1", "moment child 2", "moment"));
  }

  private boolean hasItems(List<String> keywords, String... values) {
    for (String value : values) {
      if (!keywords.contains(value)) {
        return false;
      }
    }
    return true;
  }

  private String readFile(InputStream inStream) throws IOException {
    // reads text from file and creates one String
    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
    String text = "";
    String line = "";
    while ((line = reader.readLine()) != null) {
      if (line.matches("\\s")) {
        continue;
      }
      text = text + " " + line;
    }
    reader.close();
    return text;
  }

//  private String readFile1(File inStream) throws IOException {
//    // reads text from file and creates one String
//    BufferedReader reader = new BufferedReader(new FileReader(inStream));
//    String text = "";
//    String line = "";
//    while ((line = reader.readLine()) != null) {
//      if (line.matches("\\s")) {
//        continue;
//      }
//      text = text + " " + line;
//    }
//    reader.close();
//    return text;
//  }

}
