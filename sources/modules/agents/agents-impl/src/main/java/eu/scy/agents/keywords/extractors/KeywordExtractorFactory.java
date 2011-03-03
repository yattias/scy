package eu.scy.agents.keywords.extractors;

import java.util.concurrent.ConcurrentHashMap;

import eu.scy.agents.impl.EloTypes;

/**
 * Provides a keyword extractor for each mimetype. Until now there are
 * extractors defined for
 * <ul>
 * <li>scy/text via call to ExtractKeywordsAgent</li>
 * <li>scy/webresourcer via call to ExtractKeywordsAgent</li>
 * <li>scy/mapping</li>
 * <li>scy/richtext</li>
 * <li>scy/copex</li>
 * <li>scy/interview</li>
 * </ul>
 * Extractors to come are for
 * <ul>
 * <li>scy/model</li>
 * </ul>
 * 
 * @author fschulz
 * 
 */
public class KeywordExtractorFactory {

	private static ConcurrentHashMap<String, KeywordExtractor> keywordExtractors = new ConcurrentHashMap<String, KeywordExtractor>();

	private static final NoneExtractor NONE_EXTRACTOR = new NoneExtractor();

	public KeywordExtractorFactory() {
		setKeywordExtractor(EloTypes.SCY_TEXT, new TextExtractor());
		setKeywordExtractor(EloTypes.SCY_RICHTEXT, new RichTextExtractor());
		setKeywordExtractor(EloTypes.SCY_WEBRESOURCER,
				new WebresourceExtractor());
		setKeywordExtractor(EloTypes.SCY_MAPPING, new ConceptMapExtractor());
		setKeywordExtractor(EloTypes.SCY_XPROC, new CopexExtractor());
        setKeywordExtractor(EloTypes.SCY_DATA, new FitexExtractor());
		setKeywordExtractor(EloTypes.SCY_INTERVIEW,
				new InterviewToolExtractor());
	}

	/**
	 * Get the {@link KeywordExtractor} for the specified elo type.
	 * 
	 * @param eloType
	 *            The type of the elo.
	 * @return An appropriate extractor for the specified type.
	 */
	public KeywordExtractor getKeywordExtractor(String eloType) {
		if (eloType == null) {
			return NONE_EXTRACTOR;
		}

		KeywordExtractor extractor = keywordExtractors.get(eloType);

		if (extractor == null) {
			return NONE_EXTRACTOR;
		}
		return extractor;
	}

	/**
	 * Register a new Extractor for a elo type.
	 * 
	 * @param eloType
	 *            The elo type the {@link KeywordExtractor} should work on.
	 * @param extractor
	 *            The extractor that handles elos of the specified type.
	 */
	public void setKeywordExtractor(String eloType, KeywordExtractor extractor) {
		keywordExtractors.put(eloType, extractor);
	}

}
