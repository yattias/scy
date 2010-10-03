package eu.scy.eloimporter.contentextractors;

import java.util.HashMap;

public class ContentExtractorFactory {
    
    private static HashMap<String, IContentExtractor> extractors = new HashMap<String, IContentExtractor>();
    
    static {
        extractors.put("text/plain", new PlainTextExtractor());
        extractors.put("text/xml", new XMLExtractor());
    }
    
    public static IContentExtractor getContentExtractor(String mimetype) {
        if (extractors.containsKey(mimetype)) {
            return extractors.get(mimetype);
        }
        return new EmptyExtractor();
    }
    
}
