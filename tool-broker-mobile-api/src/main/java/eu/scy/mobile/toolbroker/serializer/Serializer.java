package eu.scy.mobile.toolbroker.serializer;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 02.mar.2009
 * Time: 12:12:28
 * To change this template use File | Settings | File Templates.
 */
public interface Serializer {
    String getLocalId();

    String getRemoteId();

    Object deserialize(Object obj);

    Object serialize(Object obj);
}
