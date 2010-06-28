package eu.scy.eloimporter.contentextractors;

import java.io.File;

import roolo.elo.api.IContent;
import roolo.elo.content.BasicContent;

public class EmptyExtractor implements IContentExtractor {
    
    public IContent getContent(File file) {
        System.err.println("**************** content not extractable for file " + file.getName());
        return new BasicContent("");
    }
    
}
