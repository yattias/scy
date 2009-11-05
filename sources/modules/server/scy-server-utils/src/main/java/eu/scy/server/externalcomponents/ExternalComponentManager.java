package eu.scy.server.externalcomponents;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.nov.2009
 * Time: 13:31:21
 * To change this template use File | Settings | File Templates.
 */
public class ExternalComponentManager {

    private List<IExternalComponent> externalComponents = new LinkedList<IExternalComponent>();

    public void startupExternalComponents() throws ExternalComponentFailedException {
        for (int i = 0; i < externalComponents.size(); i++) {
            IExternalComponent iExternalComponent = externalComponents.get(i);
            iExternalComponent.startComponent();
        }
    }
    
    public void stopExternalComponents() throws ExternalComponentFailedException {
        for (int i = 0; i < externalComponents.size(); i++) {
            IExternalComponent iExternalComponent = externalComponents.get(i);
            iExternalComponent.stopComponent();
        }
    }

    public void addExternalComponent(IExternalComponent externalComponent) {
        if(!getExternalComponents().contains(externalComponent)) {
            getExternalComponents().add(externalComponent);
        }
    }

    public List<IExternalComponent> getExternalComponents() {
        return externalComponents;
    }

    public void setExternalComponents(List<IExternalComponent> externalComponents) {
        this.externalComponents = externalComponents;
    }
}
