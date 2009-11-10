package eu.scy.server.externalcomponents;

import org.springframework.beans.factory.InitializingBean;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

public class ExternalComponentManager implements InitializingBean {

    private Logger log = Logger.getLogger("ExternalComponentManager.class");
    private Set<IExternalComponent> externalComponents;

    public ExternalComponentManager() {
        externalComponents = new TreeSet<IExternalComponent>(new Comparator<IExternalComponent>() {

            @Override
            public int compare(IExternalComponent o1, IExternalComponent o2) {
                return o1.getPriority() - o2.getPriority();
            }

        });
    }

    public void startupExternalComponents() throws ExternalComponentFailedException {
        log.info("****************************************************************************");
        log.info("****************** STARTING EXTERNAL COMPONENTS ****************************");
        log.info("****************************************************************************");

        for (IExternalComponent iExternalComponent : externalComponents) {
            log.info("Starting: " + iExternalComponent.getClass().getName());
            iExternalComponent.startComponent();
            log.info("Done starting : " + iExternalComponent.getClass().getName());

        }
    }

    public void stopExternalComponents() throws ExternalComponentFailedException {
        for (IExternalComponent iExternalComponent : externalComponents) {
            iExternalComponent.stopComponent();
        }
    }

    public void addExternalComponent(IExternalComponent externalComponent) {
        if (!getExternalComponents().contains(externalComponent)) {
            getExternalComponents().add(externalComponent);
        }
    }

    public Set<IExternalComponent> getExternalComponents() {
        return externalComponents;
    }

    public void setExternalComponents(Set<IExternalComponent> externalComponents) {
        this.externalComponents = externalComponents;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startupExternalComponents();
    }
}
