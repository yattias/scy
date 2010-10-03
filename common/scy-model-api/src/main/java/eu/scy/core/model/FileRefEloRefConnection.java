package eu.scy.core.model;

import eu.scy.core.model.pedagogicalplan.BaseObject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jun.2010
 * Time: 12:27:37
 * To change this template use File | Settings | File Templates.
 */
public interface FileRefEloRefConnection extends FileRefConnection{

    BaseObject getConnectedElement();

    void setConnectedElement(ELORef eloRef);
}
