package eu.scy.eloimporter.contentextractors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import roolo.elo.api.IContent;
import roolo.elo.content.BasicContent;

public class PlainTextExtractor implements IContentExtractor {
    
    public IContent getContent(File file) {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            while (line != null) {
                line = reader.readLine();
                buffer.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BasicContent(buffer.toString());
    }
}
