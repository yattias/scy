package eu.scy.server.externalcomponents;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class ExternalComponentManager implements InitializingBean, DisposableBean {

    private Logger log = Logger.getLogger("ExternalComponentManager.class");
    private List<IExternalComponent> externalComponents;

    public ExternalComponentManager() {
        externalComponents = new ArrayList<IExternalComponent>();
    }

    public void startupExternalComponents() throws ExternalComponentFailedException {
        log.info("****************************************************************************");
        log.info("****************** STARTING EXTERNAL COMPONENTS ****************************");
        log.info("****************************************************************************");

        ArrayList<Exception> exceptions = new ArrayList<Exception>();
        for (IExternalComponent iExternalComponent : externalComponents) {
            log.info("Starting: " + iExternalComponent.getClass().getName());
            try {
                iExternalComponent.startComponent();
            } catch (ExternalComponentFailedException e) {
                exceptions.add(e);
            }
            log.info("Done starting : " + iExternalComponent.getClass().getName());
        }
        if (!exceptions.isEmpty()) {
            StringBuilder sb = new StringBuilder("Got " + exceptions.size() + " exceptions:\n");
            for (Exception e : exceptions) {
                sb.append(e.getMessage());
                sb.append("\n");
            }
            sb.append("Notice: The stacktrace belongs only to the first exception!");
            throw new ExternalComponentFailedException(sb.toString(), exceptions.get(0));
        }
    }

    public void stopExternalComponents() throws ExternalComponentFailedException {
        log.info("****************************************************************************");
        log.info("****************** STOPPING EXTERNAL COMPONENTS ****************************");
        log.info("****************************************************************************");

        ArrayList<Exception> exceptions = new ArrayList<Exception>();
        for (int i = externalComponents.size() - 1; i >= 0; i--) {
            IExternalComponent iExternalComponent = externalComponents.get(i);
            log.info("Stopping: " + iExternalComponent.getClass().getName());
            try {
                iExternalComponent.stopComponent();
            } catch (ExternalComponentFailedException e) {
                exceptions.add(e);
            }
            log.info("Done stopping : " + iExternalComponent.getClass().getName());
        }
        if (!exceptions.isEmpty()) {
            StringBuilder sb = new StringBuilder("Got " + exceptions.size() + " exceptions:\n");
            for (Exception e : exceptions) {
                sb.append(e.getMessage());
                sb.append("\n");
            }
            sb.append("Notice: The stacktrace belongs only to the first exception!");
            throw new ExternalComponentFailedException(sb.toString(), exceptions.get(0));
        }
    }

    public void addExternalComponent(IExternalComponent externalComponent) {
        if (!getExternalComponents().contains(externalComponent)) {
            getExternalComponents().add(externalComponent);
        }
    }

    public List<IExternalComponent> getExternalComponents() {
        return externalComponents;
    }

    public void setExternalComponents(List<IExternalComponent> externalComponents) {
        this.externalComponents = externalComponents;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startupExternalComponents();
    }

    @Override
    public void destroy() throws Exception {
        stopExternalComponents();
    }
}
