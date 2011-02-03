package eu.scy.core;

import com.thoughtworks.xstream.XStream;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.feb.2011
 * Time: 13:06:44
 * To change this template use File | Settings | File Templates.
 */
public interface XMLTransferObjectService {
    XStream getXStreamInstance();

    XStream getToObjectXStream();

    Object getObject(String xml);
}
