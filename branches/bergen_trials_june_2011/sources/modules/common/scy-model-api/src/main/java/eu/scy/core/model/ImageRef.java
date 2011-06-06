package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 09:59:46
 * To change this template use File | Settings | File Templates.
 */
public interface ImageRef extends FileRef{

    FileData getIcon();

    void setIcon(FileData icon);
}
