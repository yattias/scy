/*
 * Created on 21.09.2010
 */
package eu.scy.agents.keywords.extractors;

import eu.scy.agents.Mission;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RichTextExtractorTest extends AbstractExtractorTest {

    @Before
    public void setup() throws Exception {
        elo = loadElo("/richTextExampleElo.xml", "TestCopex", "scy/copex");
        extractor = new RichTextExtractor();
        extractor.setMission(Mission.MISSION1);
        extractor.setTupleSpace(getCommandSpace());
    }

    @Test
    public void testGetKeywords() {
        List<String> keywords = extractor.getKeywords(elo);
        assertEquals(37, keywords.size());
        assertTrue(hasItems(keywords, "source de", "elle est", "test", "une source", "il faut", "influence", "plus elle", "de chaleur", "l'épaisseur des", "la boite", "par", "le chauffage", "une source de", "et les", "triple", "toute", "pour", "taille de", "des boites", "ont besoin", "mais avec", "la maison", "des murs", "plus", "Plus elle est", "le mieux", "source de chaleur", "les chambres", "double", "un", "qui représente", "une source de chaleur", "et un", "voir avec", "Plus elle", "le soleil", "comment"));
    }
}
