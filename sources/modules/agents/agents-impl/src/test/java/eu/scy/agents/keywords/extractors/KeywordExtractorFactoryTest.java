package eu.scy.agents.keywords.extractors;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class KeywordExtractorFactoryTest {

	private KeywordExtractorFactory factory;

	@Before
	public void setup() {
		factory = new KeywordExtractorFactory();
	}

	@Test
	public void testGetKeywordExtractor() {
		assertTrue(factory.getKeywordExtractor("scy/text") instanceof TextExtractor);
		assertTrue(factory.getKeywordExtractor("scy/rtf") instanceof RichTextExtractor);
		assertTrue(factory.getKeywordExtractor("scy/rtf") instanceof RichTextExtractor);
		assertTrue(factory.getKeywordExtractor("scy/rtf") instanceof RichTextExtractor);
		assertTrue(factory.getKeywordExtractor("scy/rtf") instanceof RichTextExtractor);
		assertTrue(factory.getKeywordExtractor("scy/rtf") instanceof RichTextExtractor);
	}

	@Test
	public void testNotPresentExtractor() {
		assertTrue(factory.getKeywordExtractor("scy/something") instanceof NoneExtractor);
	}

	@Test()
	public void testNullEloTypeExtractor() {
		assertTrue(factory.getKeywordExtractor(null) instanceof NoneExtractor);
	}

}
