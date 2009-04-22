package eu.scy.mobile.toolbroker;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 02.mar.2009
 * Time: 11:13:28
 */
public interface IMobileToolBroker {
    public IELOService getELOService();
    public IUserService getUserService();
}
