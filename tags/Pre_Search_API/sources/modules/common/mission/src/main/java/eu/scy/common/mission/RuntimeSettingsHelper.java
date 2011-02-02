package eu.scy.common.mission;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.nov.2010
 * Time: 11:52:10
 * To change this template use File | Settings | File Templates.
 */
public interface RuntimeSettingsHelper extends RuntimeSettingsManager, PropertyAccessor{

    public void setScaffoldingLevel(Integer scaffoldingLevel);

    public Integer getScaffoldingLevel();
}
