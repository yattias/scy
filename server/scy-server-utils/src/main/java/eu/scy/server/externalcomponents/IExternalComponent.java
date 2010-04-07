package eu.scy.server.externalcomponents;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.nov.2009
 * Time: 13:31:37
 * To change this template use File | Settings | File Templates.
 */
public interface IExternalComponent {

    public void startComponent() throws ExternalComponentFailedException;

    public void stopComponent() throws ExternalComponentFailedException;

}
