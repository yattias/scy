package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:00:03
 * A group in the SCY system. Groups can be arranged hierarchically within projects and can contain users
 */
public interface SCYGroup extends Group {
    SCYGroup getParent();

    void setParent(SCYGroup parent);
}