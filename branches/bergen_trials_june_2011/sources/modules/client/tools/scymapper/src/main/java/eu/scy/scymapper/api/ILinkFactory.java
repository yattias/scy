package eu.scy.scymapper.api;

import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.impl.model.ComboNodeLinkModel;
import eu.scy.scymapper.impl.model.NodeLinkModel;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Bjoerge Naess Date: 30.okt.2009 Time: 13:07:28
 */
public interface ILinkFactory {

    public enum Type {
        SCY(ComboNodeLinkModel.class),
        COLLIDE(NodeLinkModel.class);

        private Type(Class<?> modelClazz) {}



    }

    ILinkModel create();

    Type getType();
}
