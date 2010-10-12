package eu.scy.eloimporter.contentextractors;

import java.io.File;

import roolo.elo.api.IContent;

public interface IContentExtractor {
    
    IContent getContent(File file);

}
