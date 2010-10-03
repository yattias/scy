package eu.scy.core.media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 10:14:39
 * To change this template use File | Settings | File Templates.
 */
public interface ImageConverter {
    File handleImageConversion(File imageFile) throws IOException, FileNotFoundException;
}
