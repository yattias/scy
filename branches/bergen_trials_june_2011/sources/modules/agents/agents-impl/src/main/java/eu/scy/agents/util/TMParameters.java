package eu.scy.agents.util;

/**
 * Class that holds all parameter names and constants used in the text mining agents.
 * 
 * @author Florian Schulz, Jörg Kindermann
 * 
 */
public abstract class TMParameters {

	private TMParameters() {
		// not allowed to construct
	}

	public static final String GRAPH = "Graph";
	
	public static final String DIRECTORY = "directory";
	public static final String XML_FILE = "xmlFile";
	public static final String OUTPUT_FILE_SUFFIX = "Suffix";
	public static final String COUNTER = "Counter";
	public static final String INPUT = "dpaInput";
	public static final String RANDOM = "Random";
	public static final String MAX_DOCUMENTS = "MaximumDocuments";
	public static final String START_DOCUMENT_NO = "StartDocumentNumber";
	public static final String END_DOCUMENT_NO = "EndDocumentNumber";
	public static final String ID4FILENAME = "docIdForFilename";
	public static final String DOCUMENT_FREQUENCY_MODEL = "DocumentFrequencyModel";
	public static final String NUM_TOKENS = "NumTokens";
	public static final String OUTPUT_PATH = "OutputPath";
	public static final String UTF8 = "UTF-8";
	public static final String LANGUAGE = "Language";
	public static final String GERMAN = "German";
	public static final String ENGLISH = "English";

	/* switches to configure preprocessing operators */
    public static final String REMOVE_HTML_TAGS = "RemoveHTMLTags";
    public static final String PROVIDE_TERM_FREQUENCY = "ProvideTermFrequency";
    public static final String PROVIDE_TOKENS = "ProvideTokens";
    public static final String STEM_TOKENS = "StemTokens";
    public static final String REMOVE_STOPWORDS = "RemoveStopwords";
    
	/**
	 * Property to configure wether a dpa corpus or a onedocperfile corpus is
	 * read.
	 */
	public static final String PARSER_TYPE = "IsCorpusDPA";

    /**
     * regular expressions needed to tokenize text and split into sentences
     */
    public static final String ALPHA_PATTERN = "a-zA";
    public static final String URL_PATTERN = "(http|https)://[-_\\.\\~" + ALPHA_PATTERN
            + "0-9/=&\\?]+";
    public static final String EMAIL_PATTERN = "[-" + ALPHA_PATTERN + "0-9\\.]+@[-" + ALPHA_PATTERN
            + "0-9\\.]+";
    public static final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])[\\./](0?[1-9]|1[012])[\\./]((19|20)\\d\\d)";
    public static final String MONTH_PATTERN = "(0?[1-9]|[12][0-9]|3[01])[\\./] *([A-Z][a-z]+)([\\./]*((19|20)\\d\\d))*";
    public static final String TIME_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    public static final String NUMBER_PATTERN = "[\\-\\+]*[0-9]+([,\\.][0-9]+)*";
    public static final String WORD_PATTERN = "[" + ALPHA_PATTERN + "°\\'\\`²]+";
    public static final String PHRASE_PATTERN = WORD_PATTERN + "([-&]" + WORD_PATTERN + ")*";
    public static final String PUNCT_PATTERN = "\\-\\!\"/\\?\\\\\\*\\+\\~_:\\.,;=#";
    public static final String BRACKET_PATTERN = "\\]\\(\\)\\[\\}\\{><";
    public static final String SENTENCE_DELIMITER = "(W[\\.:\\!;\\?]W|\\)[\\.:\\!;\\?]W|N[\\.:\\!;\\?]W|W[\\.:\\!;\\?]N|\"[\\.:\\!;\\?]W|\\.\\.\\.)";

 }
